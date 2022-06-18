package wolforce.simpleshops;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import wolforce.utils.stacks.UtilItemStack;

public class SimpleShopTileEntity extends BlockEntityParent {

//	static Supplier<Collection<Block>> valid = () -> setOfBlocks(Registry.SIMPLE_SHOP.get());

	private static final int COST_SLOT = 1;
	private static final int RESULT_SLOT = 0;
	private static final int BAR_SLOT = 2;

	public SimpleShopTileEntity(BlockPos pos, BlockState state) {
		super(Registry.SIMPLE_SHOP_TILE.get(), pos, state);
	}

//	private static Set<Block> setOfBlocks(Block... blocks) {
//		HashSet<Block> set = new HashSet<Block>();
//		for (Block b : blocks)
//			set.add(b);
//		return set;
//	}

	private UUID owner = new UUID(0, 0);
	private NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
	private int invNr = 0, gainsNr = 0;

	public void setOwner(LivingEntity entity) {
		this.owner = entity.getUUID();
		setChanged();
	}

	public boolean isOwner(Block _block, LivingEntity entity) {

		if (!(_block instanceof SimpleShopBlock))
			return false;

		SimpleShopBlock block = (SimpleShopBlock) _block;

		if (block.isCreative)
			return (entity instanceof Player && ((Player) entity).isCreative());

		return owner.equals(entity.getUUID());
	}

	int getStockNr() {
		return getOutputStack().getCount() == 0 ? 0 : invNr / getOutputStack().getCount();
	}

	public ItemStack getCost() {
		return inventory.get(COST_SLOT).copy();
	}

	public void setCost(ItemStack stack) {
		inventory.set(COST_SLOT, stack.copy());
		sendUpdate();
	}

	public void setBar(ItemStack stack) {
		inventory.set(BAR_SLOT, stack.copy());
		sendUpdate();
	}

	public ItemStack getBar() {
		return inventory.get(BAR_SLOT).copy();
	}

	public ItemStack getOutputStack() {
		return inventory.get(RESULT_SLOT).copy();
	}

	public void setOutputStack(ItemStack stack) {
		inventory.set(RESULT_SLOT, stack.copy());
		sendUpdate();
	}

	public void clearCostAndOutputStacks() {
		inventory.set(COST_SLOT, ItemStack.EMPTY);
		inventory.set(RESULT_SLOT, ItemStack.EMPTY);
		sendUpdate();
	}
	//
	//
	// PUBLIC METHODS

	public ItemStack insertInv(Player player, ItemStack stackToInsert) {
		ItemStack output = getOutputStack();
		if (output.isEmpty()) {
			setOutputStack(stackToInsert);
			invNr += stackToInsert.getCount();
			return ItemStack.EMPTY;
		}
		int countToInsert = output.getCount();
		if (!stackToInsert.sameItem(output) || !tagsEqualOrNull(stackToInsert, output) || stackToInsert.getCount() < countToInsert)
			return stackToInsert;
		invNr += countToInsert;
		sendUpdate();
		return UtilItemStack.setCount(stackToInsert, stackToInsert.getCount() - countToInsert);
	}

	private boolean tagsEqualOrNull(ItemStack a1, ItemStack a2) {
		CompoundTag s1 = a1.getTag();
		CompoundTag s2 = a2.getTag();
		if ((s1 == null && s2 != null) || (s2 == null && s1 != null))
			return false;
		if (s1 == null && s2 == null)
			return true;
		return s1.equals(s2);
	}

	public void tryBuy(Player player, ItemStack input, boolean isCreative) {
		if (getStockNr() > 0 || isCreative) {
			ItemStack cost = getCost();
			if (input.sameItem(cost) && input.getCount() >= cost.getCount()) {
				ItemStack result = getOutputStack();
				ItemStack change = UtilItemStack.setCount(input, input.getCount() - cost.getCount());
				if (!isCreative)
					invNr -= result.getCount();
				gainsNr += cost.getCount();
				spawn(player.level, player.position(), result);
				player.setItemInHand(InteractionHand.MAIN_HAND, change);
				sendUpdate();
			}
		}
	}

	public void dropShop(Player player, BlockPos pos) {
		dropAllInv(player);
		dropAllGains(player);

		if (!getCost().isEmpty()) {
			clearCostAndOutputStacks();
		} else {
			ItemStack bar = getBar();
			if (bar != null && !bar.isEmpty())
				spawn(level, player.position(), bar);
			player.level.removeBlock(pos, true);
			spawn(level, player.position(), new ItemStack(Registry.SIMPLE_SHOP_ITEM.get()));
		}
	}

	public void popGains(Player player) {
		if (gainsNr <= 0)
			return;
		dropAllGains(player);
		sendUpdate();
	}

	//
	//
	// PRIVATE METHODS

	private void dropAllInv(Player player) {
		ItemStack item = getOutputStack();
		while (invNr > 64) {
			invNr -= 64;
			spawn(player.level, player.position(), item, 64);
		}
		spawn(player.level, player.position(), item, invNr);
		invNr = 0;
	}

	private void dropAllGains(Player player) {
		ItemStack item = getCost();
		while (gainsNr > 64) {
			gainsNr -= 64;
			spawn(player.level, player.position(), item, 64);
		}
		spawn(player.level, player.position(), item, gainsNr);
		gainsNr = 0;
	}

	public void spawn(Level world, Vec3 pos, ItemStack stack, int amount) {
		spawn(world, pos, UtilItemStack.setCount(stack, amount));
	}

	public void spawn(Level world, Vec3 pos, ItemStack stack) {
		world.addFreshEntity(new ItemEntity(world, pos.x, pos.y, pos.z, stack.copy()));
	}

	//
	//
	// OVERRIDES

	private static final String NBT_INV = "inv";
	private static final String NBT_INV_NR = "inv_nr";
	private static final String NBT_GAINS_NR = "gains_nr";
	private static final String NBT_OWNER1 = "owner_part1";
	private static final String NBT_OWNER2 = "owner_part2";

	public void writePacketNBT(CompoundTag nbt) {
		CompoundTag invNbt = ContainerHelper.saveAllItems(new CompoundTag(), inventory);
		nbt.put(NBT_INV, invNbt);
		nbt.putInt(NBT_INV_NR, invNr);
		nbt.putInt(NBT_GAINS_NR, gainsNr);
		nbt.putLong(NBT_OWNER1, owner.getMostSignificantBits());
		nbt.putLong(NBT_OWNER2, owner.getLeastSignificantBits());
	}

	public void readPacketNBT(CompoundTag nbt) {

		inventory = NonNullList.withSize(3, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt.getCompound(NBT_INV), inventory);
		this.invNr = nbt.getInt(NBT_INV_NR);
		this.gainsNr = nbt.getInt(NBT_GAINS_NR);
		this.owner = new UUID(nbt.getLong(NBT_OWNER1), nbt.getLong(NBT_OWNER2));
	}

	private void sendUpdate() {
		setChanged();
		BlockState defaultBlockState = Registry.SIMPLE_SHOP.get().defaultBlockState();
		level.sendBlockUpdated(worldPosition, defaultBlockState, defaultBlockState, 3);
	}

//	@Override
//	public boolean triggerEvent(int id, int arg) {
//		setChanged();
//		markBlockForUpdate(getBlockPos(), null);
//		return true;
//	}
//
//	public void markBlockForUpdate(BlockPos pos, @Nullable BlockState newState) {
//		BlockState state = level.getBlockState(pos);
//		if (newState == null)
//			newState = state;
//		level.sendBlockUpdated(pos, state, newState, 3);
//		level.updateNeighborsAt(pos, newState.getBlock());
//	}

}
