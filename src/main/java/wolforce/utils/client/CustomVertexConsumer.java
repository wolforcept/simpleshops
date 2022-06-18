package wolforce.utils.client;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class CustomVertexConsumer implements VertexConsumer {

	public record RGBA(int r, int g, int b, int a) {
	}

	public static interface ColorAction {
		RGBA apply(int r, int g, int b, int a);
	}

	private VertexConsumer vc;
	private ColorAction colorAction;

	public CustomVertexConsumer(VertexConsumer vc, ColorAction colorAction) {
		this.vc = vc;
		this.colorAction = colorAction;
	}

	@Override
	public VertexConsumer vertex(double p_85945_, double p_85946_, double p_85947_) {
		vc.vertex(p_85945_, p_85946_, p_85947_);
		return this;
	}

	@Override
	public VertexConsumer color(int r, int g, int b, int a) {
		RGBA color = colorAction.apply(r, g, b, a);
		vc.color(color.r, color.g, color.b, color.a);
		return this;
	}

	@Override
	public VertexConsumer uv(float p_85948_, float p_85949_) {
		vc.uv(p_85948_, p_85949_);
		return this;
	}

	@Override
	public VertexConsumer overlayCoords(int p_85971_, int p_85972_) {
		vc.overlayCoords(p_85971_, p_85972_);
		return this;
	}

	@Override
	public VertexConsumer uv2(int p_86010_, int p_86011_) {
		vc.uv2(p_86010_, p_86011_);
		return this;
	}

	@Override
	public VertexConsumer normal(float p_86005_, float p_86006_, float p_86007_) {
		vc.normal(p_86005_, p_86006_, p_86007_);
		return this;
	}

	@Override
	public void endVertex() {
		vc.endVertex();
	}

	@Override
	public void defaultColor(int p_166901_, int p_166902_, int p_166903_, int p_166904_) {
		vc.defaultColor(p_166901_, p_166902_, p_166903_, p_166904_);
	}

	@Override
	public void unsetDefaultColor() {
		vc.unsetDefaultColor();
	}
}