package wolforce.simpleshops;

import net.minecraft.world.item.Item;

public class StockBarItem extends Item {

	public final int barx, bary;

	public StockBarItem(Properties props, int barx, int bary) {
		super(props);
		this.barx = barx;
		this.bary = bary;
	}

}
