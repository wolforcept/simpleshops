package wolforce.simpleshops.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Matrix4f;

public class UtilRender {

    public static void renderImage(
            PoseStack poseStack,
            ResourceLocation imageLocation,
            int textureWidth, int textureHeight, // texSize
            float dx, float dy, float w, float h,
            float u1, float v1, float u2, float v2,
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

//        bufferbuilder.vertex(m, x1, y1, 0).uv(u1 / textureWidth, v1 / textureHeight).color(a, a, a, 255).endVertex();
//        bufferbuilder.vertex(m, x1, y2, 0).uv(u1 / textureWidth, v2 / textureHeight).color(a, a, a, 255).endVertex();
//        bufferbuilder.vertex(m, x2, y2, 0).uv(u2 / textureWidth, v2 / textureHeight).color(a, a, a, 255).endVertex();
//        bufferbuilder.vertex(m, x2, y1, 0).uv(u2 / textureWidth, v1 / textureHeight).color(a, a, a, 255).endVertex();

        Tesselator.getInstance().end();
//        BufferUploader.draw(bufferbuilder.end());
        RenderSystem.disableDepthTest();
    }
}
