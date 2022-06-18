package wolforce.simpleshops;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimpleShops.MODID)
public class SimpleShops {

	public static final String MODID = "simpleshops";

	public SimpleShops() {

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		Registry.registerBus(bus);
	}

}
