package wolforce.simpleshops.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wolforce.simpleshops.Registry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegistryClient {

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(Registry.SIMPLE_SHOP_TILE.get(), SimpleShopTER::new);
//		event.registerBlockEntityRenderer(Registry.SIMPLE_SHOP_TILE.get(), SimpleShopTER::new);
	}

}
