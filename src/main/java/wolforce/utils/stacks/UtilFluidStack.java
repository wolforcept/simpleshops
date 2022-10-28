package wolforce.utils.stacks;

import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class UtilFluidStack {

	public static FluidStack fluidStack(Fluid f) {
		return new FluidStack(f, f.getAmount(f.defaultFluidState()));
	}

}
