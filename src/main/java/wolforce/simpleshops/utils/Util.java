package wolforce.simpleshops.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Util {
	
	public static boolean hasRequiredStack(Player player, ItemStack stack) {
		return player.getInventory().countItem(stack.getItem()) >= stack.getCount();
	}
	
	public static void removeRequiredStack(Player player, ItemStack stack) {
		int i = stack.getCount();
		int maxTimes = 1000;
		while (i > 0 && maxTimes > 0) {
			int slot = player.getInventory().findSlotMatchingItem(stack);
			player.getInventory().removeItem(slot, 1);
			i--;
			maxTimes--;
		}
	}
	
	public static MutableComponent text(String text) {
		return Component.translatable(text);
	}
	
	public static boolean isValid(ItemStack stack) {
		return stack != null && stack.getCount() > 0 && !stack.isEmpty() && !stack.getItem().equals(Items.AIR);
	}
	
	public static ItemStack setCount(ItemStack stack, int newCount) {
		ItemStack newStack = stack.copy();
		newStack.setCount(newCount);
		return newStack;
	}
}
