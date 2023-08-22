package wolforce.simpleshops.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import wolforce.simpleshops.*;
import wolforce.simpleshops.util.UtilRender;

import java.awt.*;

public class SimpleShopTER implements BlockEntityRenderer<SimpleShopTileEntity> {

    public SimpleShopTER(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(@NotNull SimpleShopTileEntity tile,
                       float partialTickTime,
                       @NotNull PoseStack matrix,
                       @NotNull MultiBufferSource buffer,
                       int combinedLight, int combinedOverlay) {

        ClientLevel world;
        if (tile.getLevel() != null & tile.getLevel() instanceof ClientLevel)
            world = (ClientLevel) tile.getLevel();
        else
            return;

        BlockState state = world.getBlockState(tile.getBlockPos());
        if (!(state.getBlock() instanceof SimpleShopBlock))
            return;

        matrix.pushPose();

        switch (state.getValue(SimpleShopBlock.FACING)) {
            case SOUTH -> {
                matrix.translate(0.5, 0, 0.5);
                matrix.mulPose(Axis.YN.rotationDegrees(90f));
                matrix.translate(-0.5, 0, -0.5);
            }
            case WEST -> {
                matrix.translate(0.5, 0, 0.5);
                matrix.mulPose(Axis.YN.rotationDegrees(180f));
                matrix.translate(-0.5, 0, -0.5);
            }
            case NORTH -> {
                matrix.translate(0.5, 0, 0.5);
                matrix.mulPose(Axis.YN.rotationDegrees(270f));
                matrix.translate(-0.5, 0, -0.5);
            }
        }

        Font font = Minecraft.getInstance().font;
        ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();
        int stock = Math.min(10, tile.getStockNr());

        //
        // MIDDLE PART: STOCK

        ItemStack barStack = tile.getBar();
        if (barStack != null && !barStack.isEmpty() && barStack.getItem() instanceof StockBarItem) {
            StockBarItem bar = (StockBarItem) barStack.getItem();

            matrix.pushPose();
            matrix.mulPose(Axis.YN.rotationDegrees(90f));

            ResourceLocation img = new ResourceLocation(SimpleShops.MODID, "textures/block/simple_shop.png");

            int light1 = world.getLightEngine().getLayerListener(LightLayer.BLOCK).getLightValue(tile.getBlockPos());
            int light2 = world.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(tile.getBlockPos());
            int light = Math.max(20, Math.min(255, (light1 + light2) * 140 / 15));
            int lightLower = Math.max(20, Math.min(255, (light1 + light2) * 100 / 15));
            if (bar == Registry.WOODEN_STOCKBAR.get()) {
                light = (int) Math.max(20, Math.min(255, light * 1.5));
            }

            matrix.translate(0, 0, -1 / 16f + .0001f);
            UtilRender.renderImage(matrix, img, 32, 32, // texSize
                    3, 7, 10, 2, // xywh
                    18, 18, 28, 20, // uvs
                    lightLower);

            matrix.translate(0, 0, .0001f);
            UtilRender.renderImage(matrix, img, 32, 32, // texSize
                    3, 7, stock, 2, // xywh
                    bar.barx, bar.bary, bar.barx + stock, bar.bary + 2, // uvs
                    light);

            matrix.popPose();
        }

        //
        // BOTTOM PART: COST

        ItemStack renderStack = tile.getCost();
        if (!renderStack.isEmpty()) {

            BakedModel itemModel = renderItem.getModel(renderStack, null, null, 0);
//            boolean render3D = renderStack.getItem() instanceof BlockItem;
            boolean render3D = itemModel.isGui3d();

            if (render3D)
                Lighting.setupFor3DItems();
            else
                Lighting.setupForFlatItems();

            matrix.pushPose(); // START RENDER ITEM
            matrix.translate(-.055, .131, .35);
            matrix.mulPose(Axis.ZN.rotationDegrees(22.5f));
            matrix.scale(0, render3D ? .666f : .5f, render3D ? .666f : .5f);
            if (render3D) {
                matrix.mulPose(Axis.ZP.rotationDegrees(30f));
                matrix.translate(0, -.1, 0);
            }
            matrix.mulPose(Axis.YN.rotationDegrees(render3D ? 45f : 90f));

            renderItem.render(renderStack, ItemDisplayContext.GROUND, false, matrix, buffer, combinedLight,
                    combinedOverlay, itemModel);
            matrix.popPose(); // FINNISH RENDER ITEM

            matrix.pushPose(); // START RENDER TEXT
            matrix.translate(-.055, .132, .35);

            matrix.mulPose(Axis.ZN.rotationDegrees(22.5f));
            matrix.mulPose(Axis.YN.rotationDegrees(90f));
            matrix.mulPose(Axis.ZN.rotationDegrees(180f));
            matrix.mulPose(Axis.YN.rotationDegrees(180f));
            matrix.scale(.028f, .028f, .028f);
            matrix.translate(4.5f, -5.8f, 0);
            if (renderStack.getCount() > 9) {
                matrix.scale(.7f, .7f, .7f);
                matrix.translate(-2, 3, 0);
            }
            font.drawInBatch("x" + renderStack.getCount(),
                    0f, 0f,
                    0, false,
                    matrix.last().pose(), buffer,
                    Font.DisplayMode.NORMAL,
                    0, combinedLight
            );
            matrix.popPose(); // END RENDER TEXT

        }

        //
        // TOP PART: VALUE

        ItemStack renderStack2 = tile.getOutputStack();
        if (!renderStack2.isEmpty()) {

            BakedModel itemModel = renderItem.getModel(renderStack2, null, null, 0);
            boolean isRender3D = itemModel.isGui3d();

            matrix.pushPose();

            matrix.translate(.5, isRender3D ? .75 : .85, .5);
            if (!isRender3D)
                matrix.scale(.7f, .7f, .7f);
            long time = System.currentTimeMillis() / 40 % 360;
            matrix.mulPose(Axis.YP.rotationDegrees(time));

            renderItem.render(renderStack2, ItemDisplayContext.GROUND, false, matrix, buffer, combinedLight,
                    combinedOverlay, itemModel);
            matrix.popPose();

            matrix.pushPose();
            matrix.scale(.025f, .025f, .025f);
            matrix.mulPose(Axis.ZN.rotationDegrees(180f));
            matrix.mulPose(Axis.YN.rotationDegrees(90f));
            matrix.mulPose(Axis.XN.rotationDegrees(90f));
            matrix.translate(renderStack2.getCount() > 9 ? 11 : 15, -12, -27.55);
            font.drawInBatch("x" + renderStack2.getCount(),
                    0f, 0f,
                    0, false,
                    matrix.last().pose(), buffer,
                    Font.DisplayMode.NORMAL,
                    0, combinedLight
            );
            matrix.popPose();
        }

        matrix.popPose();
    }

}
