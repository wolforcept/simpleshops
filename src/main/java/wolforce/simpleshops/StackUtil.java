package wolforce.simpleshops;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapeArray;
import net.minecraft.util.math.shapes.VoxelShapePart;

public class StackUtil {

	public static ItemStack setCount(ItemStack stack, int newCount) {
		ItemStack newStack = stack.copy();
		newStack.setCount(newCount);
		return newStack;
	}

	public static VoxelShape voxelShape(double x1, double y1, double z1, double x2, double y2, double z2) {
		try {
			Constructor<VoxelShapeArray> ctor = VoxelShapeArray.class.getConstructor(VoxelShapePart.class, DoubleList.class,
					DoubleList.class, DoubleList.class);
			ctor.setAccessible(true);
			return ctor.newInstance(new BitSetVoxelShapePart(0, 0, 0), (DoubleList) (new DoubleArrayList(new double[] { 0.0D })),
					(DoubleList) (new DoubleArrayList(new double[] { 0.0D })), (DoubleList) (new DoubleArrayList(new double[] { 0.0D })));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

}
