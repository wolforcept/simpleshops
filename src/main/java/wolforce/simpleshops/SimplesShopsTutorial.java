package wolforce.simpleshops;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import wolforce.simpleshops.utils.Util;

public enum SimplesShopsTutorial {
	a("Set the cost with Right Click"), //
	b("Insert stacks to sell with Right Click"), //
	c("Right Click with empty hand to insert more stacks."), //
	d("Withdraw profits with Left Click"), //
	e("Clear/Drop shop with Shift Left Click"), //
	f("Creative Simple Shops can only be set/changed in Creative Mode.", 0xFFAAAA, true) //
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
		return Util.text(text).setStyle(Style.EMPTY.withColor(color));
	}
}
