package wolforce.simpleshops.util;

import java.util.Arrays;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class UtilInventory {
    public static boolean hasRequiredStacks(Player player, Object[] ingredients) {
        for (Object object : ingredients) {
            if (object instanceof TagStack tagStack) {
                ItemStack[] arr = Arrays.stream(tagStack.stacks).map(s -> new ItemStack(s.getItem(), tagStack.count)).toArray(ItemStack[]::new);
                if (!hasOneOfRequiredStacks(player, arr))
                    return false;
            }

            if (object instanceof ItemStack stack) {
                if (!hasOneOfRequiredStacks(player, stack))
                    return false;
            }

        }
        return true;
    }

    public static boolean hasOneOfRequiredStacks(Player player, ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            if (hasRequiredStack(player, stack))
                return true;
        }
        return false;
    }

    public static boolean hasRequiredStack(Player player, ItemStack stack) {
        return player.getInventory().countItem(stack.getItem()) >= stack.getCount();
    }

    public static void removeRequiredStacks(Player player, Object[] ingredients) {
        for (Object object : ingredients) {
            if (object instanceof TagStack tagStack) {
                ItemStack[] arr = Arrays.stream(tagStack.stacks).map(s -> new ItemStack(s.getItem(), tagStack.count)).toArray(ItemStack[]::new);
                removeOneOfRequiredStacks(player, arr);
            }

            if (object instanceof ItemStack stack)
                removeOneOfRequiredStacks(player, stack);
        }
    }

    public static void removeOneOfRequiredStacks(Player player, ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            if (hasRequiredStack(player, stack)) {
                removeRequiredStack(player, stack);
                return;
            }
        }
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
}
