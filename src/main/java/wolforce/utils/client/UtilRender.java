package wolforce.utils.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public class UtilRender {

	public static Minecraft MC = Minecraft.getInstance();

	public static ItemRenderer renderItem = MC.getItemRenderer();
	public static TextureManager textureManager = MC.textureManager;
	public static LocalPlayer player = Minecraft.getInstance().player;

	public static void renderImage(PoseStack poseStack, ResourceLocation imageLocation, int textureWidth,
			int textureHeight, float dx, float dy, float w, float h, float u1, float v1, float u2, float v2,
			int combinedLight) {

		RenderSystem.setShaderTexture(0, imageLocation);
		Matrix4f m = poseStack.last().pose();
		RenderSystem.enableDepthTest();
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		float x1 = dx / 16;
		float x2 = (dx + w) / 16;
		float y1 = 1 - dy / 16;
		float y2 = 1 - (dy + h) / 16;
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		int a = combinedLight;
		bufferbuilder.vertex(m, x1, y1, 0).uv(u1 / textureWidth, v1 / textureHeight).color(a, a, a, 255).endVertex();
		bufferbuilder.vertex(m, x1, y2, 0).uv(u1 / textureWidth, v2 / textureHeight).color(a, a, a, 255).endVertex();
		bufferbuilder.vertex(m, x2, y2, 0).uv(u2 / textureWidth, v2 / textureHeight).color(a, a, a, 255).endVertex();
		bufferbuilder.vertex(m, x2, y1, 0).uv(u2 / textureWidth, v1 / textureHeight).color(a, a, a, 255).endVertex();
		bufferbuilder.end();
		RenderSystem.disableDepthTest();
	}

	/** @param colorStr e.g. "#FFFFFF" */
	public static float[] hex2Rgb(String colorStr) {
		return new float[] { //
				Integer.valueOf(colorStr.substring(0, 2), 16) / 256f, //
				Integer.valueOf(colorStr.substring(2, 4), 16) / 256f, //
				Integer.valueOf(colorStr.substring(4, 6), 16) / 256f //
		};
	}

	public static void colorBlit(PoseStack pMatrixStack, int pX, int pY, float pUOffset, float pVOffset, int pUWidth,
			int pVHeight, int color) {
		innerBlit(pMatrixStack, pX, pX + pUWidth, pY, pY + pVHeight, 0, pUWidth, pVHeight, pUOffset, pVOffset, pUWidth,
				pVHeight, color);
	}

	private static void innerBlit(PoseStack pMatrixStack, int pX1, int pX2, int pY1, int pY2, int pBlitOffset,
			int pUWidth, int pVHeight, float pUOffset, float pVOffset, int pTextureWidth, int pTextureHeight,
			int color) {
		innerBlit(pMatrixStack.last().pose(), pX1, pX2, pY1, pY2, pBlitOffset, (pUOffset + 0.0F) / pTextureWidth,
				(pUOffset + pUWidth) / pTextureWidth, (pVOffset + 0.0F) / pTextureHeight,
				(pVOffset + pVHeight) / pTextureHeight, color);
	}

	private static void innerBlit(Matrix4f pMatrix, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU,
			float pMaxU, float pMinV, float pMaxV, int color) {
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
		bufferbuilder.vertex(pMatrix, pX1, pY2, pBlitOffset)
				.color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255).uv(pMinU, pMaxV).endVertex();
		bufferbuilder.vertex(pMatrix, pX2, pY2, pBlitOffset)
				.color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255).uv(pMaxU, pMaxV).endVertex();
		bufferbuilder.vertex(pMatrix, pX2, pY1, pBlitOffset)
				.color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255).uv(pMaxU, pMinV).endVertex();
		bufferbuilder.vertex(pMatrix, pX1, pY1, pBlitOffset)
				.color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255).uv(pMinU, pMinV).endVertex();
		bufferbuilder.end();
	}

}
