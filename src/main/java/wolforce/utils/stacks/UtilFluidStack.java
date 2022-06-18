package wolforce.utils.stacks;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class UtilFluidStack {

	public static FluidStack fluidStack(Fluid f) {
		return new FluidStack(f, FluidAttributes.BUCKET_VOLUME);
	}

}
