package wolforce.simpleshops;

import java.util.HashMap;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import wolforce.simpleshops.utils.Util;

public class SimpleShopBlock extends Block implements EntityBlock {
	
	private static final float F = 1f / 16f;
	private static final VoxelShape shape = Shapes.box(F, 0, F, 15 * F, 11 * F, 15 * F);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public final boolean isCreative;
	
	public SimpleShopBlock(Properties props, boolean isCreative) {
		super(props);
		this.isCreative = isCreative;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	protected void fillStateContainer(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		if (world.isClientSide)
			return;
		BlockEntity _te = world.getBlockEntity(pos);
		if (!(_te instanceof SimpleShopTileEntity))
			return;
		SimpleShopTileEntity te = (SimpleShopTileEntity) _te;
		te.setOwner(entity);
	}
	
	private HashMap<String, Long> playersLastClick = new HashMap<>();
	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		if (world.isClientSide)
			return InteractionResult.SUCCESS;
		BlockEntity _te = world.getBlockEntity(pos);
		if (!(_te instanceof SimpleShopTileEntity))
			return InteractionResult.PASS;
		SimpleShopTileEntity te = (SimpleShopTileEntity) _te;
		
		if (te.isOwner(this, player)) {
			ItemStack handStack = player.getItemInHand(hand);
			if (!handStack.isEmpty()) {
				
				if (!isCreative && handStack.getItem() instanceof StockBarItem) {
					ItemStack prevBar = te.getBar();
					if (prevBar != null && !prevBar.isEmpty()) {
						te.spawn(player.level, player.position(), prevBar, 1);
					}
					ItemStack newbar = handStack.copy();
					newbar.setCount(1);
					te.setBar(newbar);
					handStack.shrink(1);
					return InteractionResult.SUCCESS;
				}
				
				ItemStack cost = te.getCost();
				if (cost.isEmpty()) {
					te.setCost(handStack);
				} else {
					ItemStack out = te.getOutputStack();
					if (isCreative) {
						te.setOutputStack(handStack);
					} else if (out.isEmpty() || out.sameItem(handStack) && handStack.getCount() >= out.getCount()) {
						player.setItemInHand(hand, te.insertInv(player, handStack));
						
					}
				}
			} else if (!isCreative) {
				String id = player.getStringUUID();
				if (playersLastClick.containsKey(id) && (System.currentTimeMillis() - playersLastClick.get(id) < 1000)) {
					ItemStack out = te.getOutputStack().copy();
					if (Util.hasRequiredStack(player, out)) {
						Util.removeRequiredStack(player, out);
						te.insertInv(player, out);
					}
				}
				playersLastClick.put(id, System.currentTimeMillis());
			}
		} else {
			ItemStack out = te.getOutputStack();
			if (out != null && !out.isEmpty())
				te.tryBuy(player, player.getItemInHand(hand), isCreative);
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		if (world.isClientSide)
			return;
		BlockEntity _te = world.getBlockEntity(pos);
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
	public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
		return shape;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> lines, TooltipFlag f) {
		super.appendHoverText(stack, world, lines, f);
		for (SimplesShopsTutorial line : SimplesShopsTutorial.values()) {
			if (!line.isCreative || (line.isCreative && isCreative))
				lines.add(line.createTextComponent());
		}
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SimpleShopTileEntity(pos, state);
	}
}
