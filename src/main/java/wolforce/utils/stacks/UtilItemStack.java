package wolforce.utils.stacks;

import java.security.InvalidParameterException;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class UtilItemStack {

	public static boolean isValid(ItemStack stack) {
		return stack != null && stack.getCount() > 0 && !stack.isEmpty() && !stack.getItem().equals(Items.AIR);
	}

	public static boolean equalExceptAmount(ItemStack stack1, ItemStack stack2) {
		if (!isValid(stack1) && !isValid(stack2))
			return true;
		if (!isValid(stack1) && isValid(stack2))
			return false;
		if (isValid(stack1) && !isValid(stack2))
			return false;
		return stack1.getItem() == stack2.getItem() && stack1.getDamageValue() == stack2.getDamageValue() && ( //
		/*    */(!stack1.hasTag() && !stack2.hasTag()) || //
				(stack1.getTag().equals(stack2.getTag())) //
		);
	}

	public static ItemStack stack(Object object) {
		if (object instanceof ItemStack)
			return (ItemStack) object;
		if (object instanceof Block)
			return new ItemStack((Block) object);
		if (object instanceof Item)
			return new ItemStack((Item) object);
		throw new InvalidParameterException(
				"Object of type" + object.getClass() + " cannot be made into an ItemStack.");
	}

	public static ItemStack setCount(ItemStack stack, int newCount) {
		ItemStack newStack = stack.copy();
		newStack.setCount(newCount);
		return newStack;
	}

	public static ItemStack[] stackArray(Object... items) {
		ItemStack[] arr = new ItemStack[items.length];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = stack(items[i]);
		}
		return arr;
	}

}
