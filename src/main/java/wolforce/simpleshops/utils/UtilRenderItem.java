package wolforce.simpleshops.utils;

import org.joml.Quaternionf;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.MatrixUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import wolforce.simpleshops.utils.CustomVertexConsumer.ColorAction;

public class UtilRenderItem {
	
	public static Minecraft MC = Minecraft.getInstance();
	public static ItemRenderer renderItem = MC.getItemRenderer();
	public static TextureManager textureManager = MC.textureManager;
	public static LocalPlayer player = Minecraft.getInstance().player;
	
	//
	
	private ItemStack stack;
	private PoseStack poseStack;
	private Vec3 position = null;
	private Vec3 scale = null;
	private ColorAction colorAction = null;
	private MultiBufferSource buffer = null;
	private Integer combinedLight = null;
	private Integer combinedOverlay = null;
	private boolean rotate = false;
	private Screen screen;
	private Vec2 mousePos;
	
	private static final Vec3 DEFAULT_POSITION = new Vec3(0, 0, 0);
	private static final Vec3 DEFAULT_SCALE = new Vec3(1, 1, 1);
	private static final int DEFAULT_COMBINED_LIGHT = 15728880;
	private static final int DEFAULT_COMBINED_OVERLAY = OverlayTexture.NO_OVERLAY;
	
	private static MultiBufferSource DEFAULT_BUFFER() {
		return MC.renderBuffers().bufferSource();
	}
	
	public static UtilRenderItem init(ItemStack stack) {
		return new UtilRenderItem(stack);
	}
	
	private UtilRenderItem(ItemStack stack) {
		this.stack = stack;
	}
	
	public UtilRenderItem pose(PoseStack poseStack) {
		this.poseStack = poseStack;
		return this;
	}
	
	public UtilRenderItem pos(Vec3 position) {
		this.position = position;
		return this;
	}
	
	public UtilRenderItem scale(double x, double y, double z) {
		this.scale = new Vec3(x, y, z);
		return this;
	}
	
	public UtilRenderItem pos(double x, double y, double z) {
		this.position = new Vec3(x, y, z);
		return this;
	}
	
	public UtilRenderItem scale(Vec3 scale) {
		this.scale = new Vec3(scale.x, scale.y, scale.z);
		return this;
	}
	
	public UtilRenderItem color(ColorAction colorAction) {
		this.colorAction = colorAction;
		return this;
	}
	
	public UtilRenderItem rotate() {
		this.rotate = true;
		return this;
	}
	
	public UtilRenderItem buffer(MultiBufferSource buffer) {
		this.buffer = buffer;
		return this;
	}
	
	public UtilRenderItem combined(int combinedLight, int combinedOverlay) {
		this.combinedLight = combinedLight;
		this.combinedOverlay = combinedOverlay;
		return this;
	}
	
	public UtilRenderItem screen(Screen screen) {
		this.screen = screen;
		return this;
	}
	
	public UtilRenderItem mouse(Vec2 mousePos) {
		this.mousePos = mousePos;
		return this;
	}
	
	public void renderGui() {
		
		if (!Util.isValid(stack))
			return;
		
		PoseStack poseStack = this.poseStack != null ? this.poseStack : RenderSystem.getModelViewStack();
		MultiBufferSource buffer = this.buffer != null ? this.buffer : MC.renderBuffers().bufferSource();
		Vec3 position = this.position != null ? this.position : DEFAULT_POSITION;
		Vec3 scale = this.scale != null ? this.scale : DEFAULT_SCALE;
		int combinedLight = this.combinedLight != null ? this.combinedLight : DEFAULT_COMBINED_LIGHT;
		int combinedOverlay = this.combinedOverlay != null ? this.combinedOverlay : DEFAULT_COMBINED_OVERLAY;
		BakedModel model = renderItem.getModel(stack, null, player, 0);
		
		poseStack.pushPose();
		
		if (scale != null)
			poseStack.scale((float) scale.x, (float) scale.y, (float) scale.z);
		if (position != null)
			poseStack.translate(position.x, position.y, position.z);
		if (rotate) {
			long time = System.currentTimeMillis() / 40 % 360;
			poseStack.mulPose(new Quaternionf().rotateLocalY((float) (time / 100.0 * Math.PI * 2)));
//			poseStack.mulPose(new Quaternionf(new AxisAngle4f(time, 0, 0, 0))); // TODO YP
		}
		
		poseStack.translate(8.0D, 8.0D, 0.0D);
		poseStack.scale(1.0F, -1.0F, 1.0F);
		poseStack.scale(16.0F, 16.0F, 16.0F);
		RenderSystem.applyModelViewMatrix();
		Lighting.setupForFlatItems();
		
		if (colorAction != null)
			renderCustomVertexConsumer(stack, ItemTransforms.TransformType.GUI, false, new PoseStack(), buffer, combinedLight, combinedOverlay, model, colorAction);
		else
			renderItem.render(stack, ItemTransforms.TransformType.GUI, false, new PoseStack(), buffer, combinedLight, combinedOverlay, model);
		
		if (buffer instanceof BufferSource bufferSource)
			bufferSource.endBatch();
		
		poseStack.popPose();
		RenderSystem.applyModelViewMatrix();
		
		poseStack.pushPose();
		poseStack.setIdentity();
		if (screen != null && mousePos != null) {
			if (mousePos.x > position.x && mousePos.y > position.y && mousePos.x < position.x + 16 && mousePos.y < position.y + 16)
//				screen.renderTooltip(poseStack, screen.getTooltipFromItem(stack), combinedLight, combinedOverlay);
				screen.renderComponentTooltip(poseStack, screen.getTooltipFromItem(stack), (int) mousePos.x, (int) mousePos.y, stack);
		}
		poseStack.popPose();
		
	}
	
	public void render() {
		
		if (!Util.isValid(stack))
			return;
		
		MultiBufferSource buffer = this.buffer != null ? this.buffer : DEFAULT_BUFFER();
		Vec3 position = this.position != null ? this.position : DEFAULT_POSITION;
		Vec3 scale = this.scale != null ? this.scale : DEFAULT_SCALE;
		int combinedLight = this.combinedLight != null ? this.combinedLight : DEFAULT_COMBINED_LIGHT;
		int combinedOverlay = this.combinedOverlay != null ? this.combinedOverlay : DEFAULT_COMBINED_OVERLAY;
		
		BakedModel model = renderItem.getModel(stack, null, player, 0);
		
		poseStack.pushPose();
		
		boolean render3D = model.isGui3d();
		if (render3D)
			Lighting.setupFor3DItems();
		else
			Lighting.setupForFlatItems();
		
		if (scale != null)
			poseStack.scale((float) scale.x, (float) scale.y, (float) scale.z);
		if (position != null)
			poseStack.translate(position.x, position.y, position.z);
		if (rotate) {
			float time = System.currentTimeMillis() / 40 % 360 / 360f;
			poseStack.mulPose(new Quaternionf().rotateLocalY(time * (float) Math.PI * 2));
		}
		
		if (!render3D) {
			poseStack.scale(.8f, .8f, .8f);
			poseStack.translate(0, .1, 0);
		}
		
		if (colorAction != null) {
			renderCustomVertexConsumer(stack, ItemTransforms.TransformType.GROUND, false, poseStack, buffer, combinedLight, combinedOverlay, model, colorAction);
		} else {
			renderItem.render(stack, ItemTransforms.TransformType.GROUND, false, poseStack, buffer, combinedLight, combinedOverlay, model);
		}
		
		poseStack.popPose();
		
	}
	
	public void renderCustomVertexConsumer( //
			ItemStack stack, //
			ItemTransforms.TransformType transformType, //
			boolean p_115146_, //
			PoseStack poseStack, //
			MultiBufferSource buffer,
			int combinedLight, //
			int combinedOverlay,
			BakedModel bakedModel, //
			ColorAction colorAction//
	) {
		
		ItemRenderer itemRenderer = MC.getItemRenderer();
		
		if (!stack.isEmpty()) {
			poseStack.pushPose();
			boolean flag = transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.GROUND || transformType == ItemTransforms.TransformType.FIXED;
			if (flag) {
				if (stack.is(Items.TRIDENT)) {} else if (stack.is(Items.SPYGLASS)) {
				}
			}
			
			bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(poseStack, bakedModel, transformType, p_115146_);
			poseStack.translate(-0.5F, -0.5F, -0.5F);
			if (!bakedModel.isCustomRenderer() && (!stack.is(Items.TRIDENT) || flag)) {
				boolean flag1;
				if (transformType != ItemTransforms.TransformType.GUI && !transformType.firstPerson() && stack.getItem() instanceof BlockItem) {
					Block block = ((BlockItem) stack.getItem()).getBlock();
					flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
				} else {
					flag1 = true;
				}
				for (var model : bakedModel.getRenderPasses(stack, flag1)) {
					for (var rendertype : model.getRenderTypes(stack, flag1)) {
						VertexConsumer vertexconsumer;
						if (stack.is(ItemTags.COMPASSES) && stack.hasFoil()) {
							poseStack.pushPose();
							PoseStack.Pose posestack$pose = poseStack.last();
							if (transformType == ItemTransforms.TransformType.GUI) {
								MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.5F);
							} else if (transformType.firstPerson()) {
								MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.75F);
							}
							
							if (flag1) {
								vertexconsumer = ItemRenderer.getCompassFoilBufferDirect(buffer, rendertype, posestack$pose);
							} else {
								vertexconsumer = ItemRenderer.getCompassFoilBuffer(buffer, rendertype, posestack$pose);
							}
							
							poseStack.popPose();
						} else if (flag1) {
							vertexconsumer = ItemRenderer.getFoilBufferDirect(buffer, rendertype, true, stack.hasFoil());
						} else {
							vertexconsumer = ItemRenderer.getFoilBuffer(buffer, rendertype, true, stack.hasFoil());
						}
						
						itemRenderer.renderModelLists(model, stack, combinedLight, combinedOverlay, poseStack, new CustomVertexConsumer(vertexconsumer, colorAction));
					}
				}
			} else {
				net.minecraftforge.client.extensions.common.IClientItemExtensions.of(stack).getCustomRenderer().renderByItem(stack, transformType, poseStack, buffer, combinedLight, combinedOverlay);
			}
			
			poseStack.popPose();
		}
	}
	
//	public static void renderCustomVertexConsumer(ItemStack stack, ItemTransforms.TransformType transformType, boolean p_115146_, PoseStack poseStack, MultiBufferSource buffer, int combinedLight,
//			int combinedOverlay, BakedModel bakedModel, ColorAction colorAction) {
//		
//		ItemRenderer itemRenderer = MC.getItemRenderer();
//		ItemModelShaper itemModelShaper = itemRenderer.getItemModelShaper();
//		
//		if (!stack.isEmpty()) {
//			poseStack.pushPose();
//			
//			bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(poseStack, bakedModel, transformType, p_115146_);
//			poseStack.translate(-0.5D, -0.5D, -0.5D);
//			if (!bakedModel.isCustomRenderer() && (!stack.is(Items.TRIDENT) || flag)) {
//				boolean flag1;
//				if (transformType != ItemTransforms.TransformType.GUI && !transformType.firstPerson() && stack.getItem() instanceof BlockItem) {
//					Block block = ((BlockItem) stack.getItem()).getBlock();
//					flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
//				} else {
//					flag1 = true;
//				}
//				if (bakedModel.isLayered()) {
//					net.minecraftforge.client.ForgeHooksClient.drawItemLayered(itemRenderer, bakedModel, stack, poseStack, buffer, combinedLight, combinedOverlay, flag1);
//				} else {
//					RenderType rendertype = ItemBlockRenderTypes.getRenderType(stack, flag1);
//					VertexConsumer vertexconsumer;
//					if (stack.is(Items.COMPASS) && stack.hasFoil()) {
//						poseStack.pushPose();
//						PoseStack.Pose posestack$pose = poseStack.last();
//						if (transformType == ItemTransforms.TransformType.GUI) {
//							posestack$pose.pose().multiply(0.5F);
//						} else if (transformType.firstPerson()) {
//							posestack$pose.pose().multiply(0.75F);
//						}
//						
//						if (flag1) {
//							vertexconsumer = ItemRenderer.getCompassFoilBufferDirect(buffer, rendertype, posestack$pose);
//						} else {
//							vertexconsumer = ItemRenderer.getCompassFoilBuffer(buffer, rendertype, posestack$pose);
//						}
//						
//						poseStack.popPose();
//					} else if (flag1) {
//						vertexconsumer = ItemRenderer.getFoilBufferDirect(buffer, rendertype, true, stack.hasFoil());
//					} else {
//						vertexconsumer = ItemRenderer.getFoilBuffer(buffer, rendertype, true, stack.hasFoil());
//					}
//					
//					itemRenderer.renderModelLists(bakedModel, stack, combinedLight, combinedOverlay, poseStack, new CustomVertexConsumer(vertexconsumer, colorAction));
//				}
//			} else {
//				net.minecraftforge.client.RenderProperties.get(stack).getItemStackRenderer().renderByItem(stack, transformType, poseStack, buffer, combinedLight, combinedOverlay);
//			}
//			
//			poseStack.popPose();
//		}
//	}
	
}
