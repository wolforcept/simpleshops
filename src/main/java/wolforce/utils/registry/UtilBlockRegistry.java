package wolforce.utils.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UtilBlockRegistry {

	public interface BlockWithItemProperties {
		Item.Properties getItemProperties();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface RegBlock {
		public String regName() default "";
	}

	private static Class<?> blocksClass;

	public static void init(Class<?> blocksClass) {
		UtilBlockRegistry.blocksClass = blocksClass;
	}

	@SubscribeEvent
	public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
		try {
			IForgeRegistry<Item> registry = event.getRegistry();

			for (Field field : blocksClass.getDeclaredFields()) {

				if (field.isAnnotationPresent(RegBlock.class) && field.get(null) instanceof Block block) {
					RegBlock reg = field.getAnnotation(RegBlock.class);
					BlockItem item = block instanceof BlockWithItemProperties blockWithProps //
							? new BlockItem(block, blockWithProps.getItemProperties()) //
							: new BlockItem(block, new Item.Properties());
					item.setRegistryName(reg.regName());
					registry.register(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
		try {
			IForgeRegistry<Block> registry = event.getRegistry();

			for (Field field : blocksClass.getDeclaredFields()) {

				if (field.isAnnotationPresent(RegBlock.class) && field.get(null) instanceof Block block) {
					RegBlock reg = field.getAnnotation(RegBlock.class);
					block.setRegistryName(reg.regName());
					registry.register(block);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@SubscribeEvent
//	public static void registerItems(final RegistryEvent.Register<Item> event) {
//		HearthWell.setupItems();
//		IForgeRegistry<Item> registry = event.getRegistry();
//		for (Item item : HearthWell.items.values()) {
//			registry.register(item);
//		}
//	}

}