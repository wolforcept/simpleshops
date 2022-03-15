package wolforce.simpleshops;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EventsRegistryClient {

	@SubscribeEvent
	public static void registerItems(FMLClientSetupEvent event) {
		ClientRegistry.bindTileEntityRenderer(SimpleShopTileEntity.TYPE.get(), SimpleShopTER::new);
	}

}