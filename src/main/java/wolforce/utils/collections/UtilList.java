package wolforce.utils.collections;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class UtilList {

	@SafeVarargs
	public static <T> List<T> of(T... objs) {
		LinkedList<T> list = new LinkedList<T>();
		for (T t : objs)
			list.add(t);
		return list;
	}

	@SafeVarargs
	public static <E> NonNullList<E> nnl(E... items) {
		NonNullList<E> list = NonNullList.create();
		for (E item : items) {
			list.add(item);
		}
		return list;
	}

	public static boolean listContains(List<ItemStack> list, Item item) {
		for (ItemStack stack : list) {
			if (stack.getItem() == item)
				return true;
		}
		return false;
	}

}
