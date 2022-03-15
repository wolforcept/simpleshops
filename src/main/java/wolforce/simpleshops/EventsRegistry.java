package wolforce.simpleshops;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventsRegistry {

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		SimpleShops.setupBlocks();
		IForgeRegistry<Block> registry = event.getRegistry();
		for (Block block : SimpleShops.blocks.values()) {
			registry.register(block);
		}
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		for (Block block : SimpleShops.blocks.values()) {
			BlockItem bi = new BlockItem(block, new Item.Properties().tab(SimpleShops.group));
			bi.setRegistryName(block.getRegistryName());
			registry.register(bi);
		}
	}

}