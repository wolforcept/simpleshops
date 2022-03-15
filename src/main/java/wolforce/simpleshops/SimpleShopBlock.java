package wolforce.simpleshops;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SimpleShopBlock extends Block {

	private static final float F = 1f / 16f;
	private static final VoxelShape shape = VoxelShapes.box(F, 0, F, 15 * F, 11 * F, 15 * F);
	private boolean isCreative;
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

	public SimpleShopBlock(Properties props, boolean isCreative) {
		super(props);
		this.isCreative = isCreative;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return SimpleShopTileEntity.TYPE.get().create();
	}

	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		if (world.isClientSide)
			return;
		TileEntity _te = world.getBlockEntity(pos);
		if (!(_te instanceof SimpleShopTileEntity))
			return;
		SimpleShopTileEntity te = (SimpleShopTileEntity) _te;
		te.setOwner(entity);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		if (world.isClientSide)
			return ActionResultType.SUCCESS;
		TileEntity _te = world.getBlockEntity(pos);
		if (!(_te instanceof SimpleShopTileEntity))
			return ActionResultType.PASS;
		SimpleShopTileEntity te = (SimpleShopTileEntity) _te;

		if (te.isOwner(this, player)) {
			ItemStack handStack = player.getItemInHand(hand);
			if (!handStack.isEmpty()) {
				ItemStack cost = te.getCost();
				if (cost.isEmpty()) {
					te.setCost(handStack);
				} else {
					ItemStack out = te.getOutputStack();
					if (isCreative) {
						te.setOutputStack(handStack);
					} else if (out.isEmpty()) {
						player.setItemInHand(hand, te.insertInv(player, handStack));
					} else if (out.sameItem(handStack) && handStack.getCount() >= out.getCount()) {
						player.setItemInHand(hand, te.insertInv(player, handStack));
					}
				}
			}
		} else {
			te.tryBuy(player, player.getItemInHand(hand), isCreative);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void attack(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (world.isClientSide)
			return;
		TileEntity _te = world.getBlockEntity(pos);
		if (!(_te instanceof SimpleShopTileEntity))
			return;
		SimpleShopTileEntity te = (SimpleShopTileEntity) _te;
		if (te.isOwner(this, player)) {
			if (player.isShiftKeyDown()) {
				te.dropShop(player, pos);
			} else {
				te.popGains(player);
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return shape;
	}

	@Override
	public void appendHoverText(ItemStack stack, IBlockReader world, List<ITextComponent> lines, ITooltipFlag f) {
		super.appendHoverText(stack, world, lines, f);
		lines.add(new StringTextComponent("Set the cost with Right Click"));
		lines.add(new StringTextComponent("Insert stacks to sell with Right Click"));
		lines.add(new StringTextComponent("Withdraw profits with Left Click"));
		lines.add(new StringTextComponent("Clear/Drop shop with Shift Left Click"));
		if (isCreative)
			lines.add(new StringTextComponent("Creative Simple Shops can only be set/changed in Creative Mode."));
	}
}
