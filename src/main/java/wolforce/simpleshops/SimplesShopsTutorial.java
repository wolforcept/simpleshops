package wolforce.simpleshops;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

public enum SimplesShopsTutorial {
	a("Set the cost with Right Click"), //
	b("Insert stacks to sell with Right Click"), //
	c("Withdraw profits with Left Click"), //
	d("Clear/Drop shop with Shift Left Click"), //
	e("Creative Simple Shops can only be set/changed in Creative Mode.", 0xFFAAAA, true) //
	;

	public final String text;
	public final int color;
	public final boolean isCreative;

	private SimplesShopsTutorial(String text) {
		this(text, 0xAAAAAA, false);
	}

	private SimplesShopsTutorial(String text, int color) {
		this(text, color, false);
	}

	private SimplesShopsTutorial(String text, int color, boolean isCreative) {
		this.text = text;
		this.color = color;
		this.isCreative = isCreative;
	}

	Component createTextComponent() {
		return new TextComponent(text).setStyle(Style.EMPTY.withColor(color));
	}
}
