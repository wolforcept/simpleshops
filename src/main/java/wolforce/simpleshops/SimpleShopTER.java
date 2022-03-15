package wolforce.simpleshops;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class SimpleShopTER extends TileEntityRenderer<SimpleShopTileEntity> {

	public SimpleShopTER(TileEntityRendererDispatcher d) {
		super(d);
	}

	@Override
	public void render(SimpleShopTileEntity tile, float partialTickTime, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight,
			int combinedOverlay) {

		if (tile == null)
			return;

		World world = tile.getLevel();
		if (world == null)
			return;

		BlockState state = world.getBlockState(tile.getBlockPos());
		if (!(state.getBlock() instanceof SimpleShopBlock))
			return;

		matrix.pushPose();

		switch (state.getValue(SimpleShopBlock.FACING)) {
		case SOUTH:
			matrix.translate(0.5, 0, 0.5);
			matrix.mulPose(new Quaternion(Vector3f.YN, 90, true));
			matrix.translate(-0.5, 0, -0.5);
			break;
		case WEST:
			matrix.translate(0.5, 0, 0.5);
			matrix.mulPose(new Quaternion(Vector3f.YN, 180, true));
			matrix.translate(-0.5, 0, -0.5);
			break;
		case NORTH:
			matrix.translate(0.5, 0, 0.5);
			matrix.mulPose(new Quaternion(Vector3f.YN, 270, true));
			matrix.translate(-0.5, 0, -0.5);
			break;
		default:
		}

		ItemStack renderStack = tile.getCost();
		FontRenderer fontRenderer = renderer.getFont();
		ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();
		if (!renderStack.isEmpty()) {

			IBakedModel itemModel = renderItem.getModel(renderStack, null, null);
			boolean render3D = itemModel.isGui3d();

			if (render3D)
				RenderHelper.setupFor3DItems();
			else
				RenderHelper.setupForFlatItems();

			matrix.pushPose(); // START RENDER ITEM
			matrix.translate(-.055, .131, .35);
			matrix.mulPose(new Quaternion(Vector3f.ZN, 22.5f, true));
			matrix.scale(0, render3D ? .666f : .5f, render3D ? .666f : .5f);
			if (render3D) {
				matrix.mulPose(new Quaternion(Vector3f.ZP, 30f, true));
				matrix.translate(0, -.1, 0);
			}
			matrix.mulPose(new Quaternion(Vector3f.YN, render3D ? 45f : 90, true));
			renderItem.render(renderStack, ItemCameraTransforms.TransformType.GROUND, false, matrix, buffer, combinedLight, combinedOverlay,
					itemModel);
			matrix.popPose(); // FINNISH RENDER ITEM

			matrix.pushPose(); // START RENDER TEXT
			matrix.translate(-.055, .132, .35);
			matrix.mulPose(new Quaternion(Vector3f.ZN, 22.5f, true));
			matrix.mulPose(new Quaternion(Vector3f.YN, 90, true));
			matrix.mulPose(new Quaternion(Vector3f.ZN, 180, true));
			matrix.mulPose(new Quaternion(Vector3f.YN, 180, true));
			matrix.scale(.028f, .028f, .028f);
			matrix.translate(4.5f, -5.8f, 0);
			if (renderStack.getCount() > 9) {
				matrix.scale(.7f, .7f, .7f);
				matrix.translate(-2, 3, 0);
			}
			fontRenderer.draw(matrix, new StringTextComponent("x" + renderStack.getCount()), 0, 0, 0);
			matrix.popPose(); // END RENDER TEXT

		}

		ItemStack renderStack2 = tile.getOutputStack();
		if (!renderStack2.isEmpty()) {

			IBakedModel itemModel = renderItem.getModel(renderStack2, null, null);
			boolean render3D = itemModel.isGui3d();
			if (render3D)
				RenderHelper.setupFor3DItems();
			else
				RenderHelper.setupForFlatItems();

			matrix.pushPose(); 
			matrix.translate(0.5F, render3D ? .75f : .9f, 0.5F);
			long a = System.currentTimeMillis() / 40 % 360;
			matrix.mulPose(new Quaternion(Vector3f.YP, a, true));
			renderItem.render(renderStack2, ItemCameraTransforms.TransformType.GROUND, false, matrix, buffer, combinedLight,
					combinedOverlay, itemModel);
			matrix.popPose();

			matrix.pushPose();
			matrix.scale(.025f, .025f, .025f);
			matrix.mulPose(new Quaternion(Vector3f.ZN, 180, true));
			matrix.mulPose(new Quaternion(Vector3f.YN, 90, true));
			matrix.mulPose(new Quaternion(Vector3f.XN, 90, true));
			matrix.translate(renderStack2.getCount() > 9 ? 11 : 15, -12, -27.55);
			fontRenderer.draw(matrix, new StringTextComponent("x" + renderStack2.getCount()), 0, 0, 0);
			matrix.popPose();
		}

		matrix.popPose();
	}

}
