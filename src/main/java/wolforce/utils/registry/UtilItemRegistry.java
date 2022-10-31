package wolforce.utils.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UtilItemRegistry {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface RegItem {
		public String regName() default "";
	}

	private static Class<?> itemsClass;

	public static void init(Class<?> itemsClass) {
		UtilItemRegistry.itemsClass = itemsClass;
	}

	@SubscribeEvent
	public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
		if (itemsClass == null)
			return;
		try {
			IForgeRegistry<Item> registry = event.getRegistry();

			for (Field field : itemsClass.getDeclaredFields()) {

				if (field.isAnnotationPresent(RegItem.class) && field.get(null) instanceof Item item) {
					RegItem reg = field.getAnnotation(RegItem.class);
					item.setRegistryName(reg.regName());
					registry.register(item);
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