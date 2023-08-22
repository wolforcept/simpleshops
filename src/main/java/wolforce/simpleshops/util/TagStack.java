package wolforce.simpleshops.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class TagStack {
	public final TagKey<Item> tag;
	public final int count;

	public TagStack(TagKey<Item> tag, int count) {
		this.tag = tag;
		this.count = count;
	}

	public ItemStack[] stacks;

	public void init() {
		stacks = Ingredient.of(tag).getItems();
	}
}
