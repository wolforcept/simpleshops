package wolforce.utils;

import java.lang.reflect.Field;

import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import wolforce.utils.stacks.UtilItemStack;

public class UtilWorld {

	public static void spawnItem(Level world, Vec3i pos, ItemStack stack) {
		spawnItem(world, new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), stack, 10);
	}

	public static void spawnItem(Level world, Vec3 pos, ItemStack stack) {
		spawnItem(world, pos, stack, 10);
	}

	/**
	 * default pickupDelay = 10 player throw pickupDelay = 40
	 */
	public static void spawnItem(Level world, Vec3 pos, ItemStack stack, int pickupDelay, double... speeds) {
		if (!UtilItemStack.isValid(stack))
			return;
		ItemEntity entityitem = new ItemEntity(world, pos.x(), pos.y(), pos.z(), stack);
		if (speeds.length == 0) {
			entityitem.setDeltaMovement(new Vec3(//
					Math.random() * .4 - .2, //
					Math.random() * .2, //
					Math.random() * .4 - .2 //
			));
		} else {
			entityitem.setDeltaMovement(new Vec3(//
					speeds[0], //
					speeds[1], //
					speeds[2] //
			));
		}
		entityitem.setPickUpDelay(pickupDelay);
		world.addFreshEntity(entityitem);
	}

	public static <T> T getObjectFromField(Class<?> class1, String fieldName, Object obj) {
		Field field = Util.getField(class1, fieldName);
		field.setAccessible(true);
		return Util.getObjectFromField(field, obj);
	}

}
