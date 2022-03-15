package wolforce.simpleshops;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SimpleShopTileEntity extends TileEntity {

	public static final DeferredRegister<TileEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
			SimpleShops.MODID);

	static Supplier<Collection<Block>> valid = () -> SimpleShops.blocks.values();
	public static final RegistryObject<TileEntityType<SimpleShopTileEntity>> TYPE = REGISTER.register("simple_shop_tile_entity",
			() -> new TileEntityType<>(SimpleShopTileEntity::new, setOf(valid.get()), null));

	private static final int COST_SLOT = 1;
	private static final int RESULT_SLOT = 0;

	public SimpleShopTileEntity(TileEntityType<?> type) {
		super(type);
	}

	private static <T> Set<T> setOf(Collection<T> collection) {
		return new HashSet<T>(collection);
	}

	public SimpleShopTileEntity() {
		this(TYPE.get());
	}

	private UUID owner = new UUID(0, 0);
	private NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);
	private int invNr = 0, gainsNr = 0;

	public void setOwner(LivingEntity entity) {
		this.owner = entity.getUUID();
		setChanged();
	}

	public boolean isOwner(Block block, LivingEntity entity) {
		return block == SimpleShops.creative_simple_shop //
				? (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()) //
				: owner.equals(entity.getUUID());
	}

	private boolean isSoldOut() {
		return invNr < getOutputStack().getCount();
	}

	public ItemStack getCost() {
		return inventory.get(COST_SLOT).copy();
	}

	public void setCost(ItemStack stack) {
		inventory.set(COST_SLOT, stack.copy());
		sendUpdate();
	}

	public ItemStack getOutputStack() {
		return inventory.get(RESULT_SLOT).copy();
	}

	public void setOutputStack(ItemStack stack) {
		inventory.set(RESULT_SLOT, stack.copy());
		sendUpdate();
	}

	public void clearCostAndOutputStacks() {
		inventory.clear();
		sendUpdate();
	}
	//
	//
	// PUBLIC METHODS

	public ItemStack insertInv(PlayerEntity player, ItemStack stackToInsert) {
		ItemStack output = getOutputStack();
		if (output.isEmpty()) {
			setOutputStack(stackToInsert);
			invNr += stackToInsert.getCount();
			return ItemStack.EMPTY;
		}
		int countToInsert = output.getCount();
		if (!stackToInsert.sameItem(output) || !stackToInsert.getTag().equals(output.getTag()) || stackToInsert.getCount() < countToInsert)
			return stackToInsert;
		invNr += countToInsert;
		sendUpdate();
		return StackUtil.setCount(stackToInsert, stackToInsert.getCount() - countToInsert);
	}

	public void tryBuy(PlayerEntity player, ItemStack input, boolean isCreative) {
		if (isSoldOut() && !isCreative)
			return;
		ItemStack cost = getCost();
		if (input.sameItem(cost) && input.getCount() >= cost.getCount()) {
			ItemStack result = getOutputStack();
			ItemStack change = StackUtil.setCount(input, input.getCount() - cost.getCount());
			if (!isCreative)
				invNr -= result.getCount();
			gainsNr += cost.getCount();
			spawn(player.level, player.position(), result);
			player.setItemInHand(Hand.MAIN_HAND, change);
			sendUpdate();
		}
		return;
	}

	public void dropShop(PlayerEntity player, BlockPos pos) {
		dropAllInv(player);
		dropAllGains(player);

		if (!getCost().isEmpty()) {
			clearCostAndOutputStacks();
		} else {
			player.level.removeBlock(pos, true);
			spawn(level, player.position(), new ItemStack(SimpleShops.simple_shop));
		}
	}

	public void popGains(PlayerEntity player) {
		if (gainsNr <= 0)
			return;
		dropAllGains(player);
		sendUpdate();
	}

	//
	//
	// PRIVATE METHODS

	private void dropAllInv(PlayerEntity player) {
		ItemStack item = getOutputStack();
		while (invNr > 64) {
			invNr -= 64;
			spawn(player.level, player.position(), item, 64);
		}
		spawn(player.level, player.position(), item, invNr);
		invNr = 0;
	}

	private void dropAllGains(PlayerEntity player) {
		ItemStack item = getCost();
		while (gainsNr > 64) {
			gainsNr -= 64;
			spawn(player.level, player.position(), item, 64);
		}
		spawn(player.level, player.position(), item, gainsNr);
		gainsNr = 0;
	}

	private void spawn(World world, Vector3d pos, ItemStack stack, int amount) {
		spawn(world, pos, StackUtil.setCount(stack, amount));
	}

	private void spawn(World world, Vector3d pos, ItemStack stack) {
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

	public void write(CompoundNBT nbt) {
		CompoundNBT invNbt = ItemStackHelper.saveAllItems(new CompoundNBT(), inventory);
		nbt.put(NBT_INV, invNbt);
		nbt.putInt(NBT_INV_NR, invNr);
		nbt.putInt(NBT_GAINS_NR, gainsNr);
		nbt.putLong(NBT_OWNER1, owner.getMostSignificantBits());
		nbt.putLong(NBT_OWNER2, owner.getLeastSignificantBits());
	}

	public void read(BlockState state, CompoundNBT nbt) {
		inventory = NonNullList.withSize(2, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt.getCompound(NBT_INV), inventory);
		this.invNr = nbt.getInt(NBT_INV_NR);
		this.gainsNr = nbt.getInt(NBT_GAINS_NR);
		this.owner = new UUID(nbt.getLong(NBT_OWNER1), nbt.getLong(NBT_OWNER2));
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		write(nbt);
		return super.save(nbt);
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		read(state, nbt);
		super.load(state, nbt);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return new SUpdateTileEntityPacket(getBlockPos(), -1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		read(level.getBlockState(worldPosition), pkt.getTag());
	}

	private void sendUpdate() {
		setChanged();
		level.sendBlockUpdated(worldPosition, SimpleShops.simple_shop.defaultBlockState(), SimpleShops.simple_shop.defaultBlockState(), 3);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = super.getUpdateTag();
		write(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		read(state, tag);
		super.handleUpdateTag(state, tag);
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
