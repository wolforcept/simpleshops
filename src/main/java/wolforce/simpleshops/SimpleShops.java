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
		
//		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		// Register the commonSetup method for modloading
//		modEventBus.addListener(this::commonSetup);
	}
	
//	private void commonSetup(CreativeModeTab.Register event) {
//		event.registerCreativeModeTab(new ResourceLocation(MODID, "example"), builder ->
//		// Set name of tab to display
//		builder.title(Component.translatable("item_group." + MODID + ".example"))
//				// Set icon of creative tab
//				.icon(() -> new ItemStack(Registry.SIMPLE_SHOP.get()))
//				// Add default items to tab
//				.displayItems((enabledFlags, populator, hasPermissions) -> {
//					populator.accept(Registry.SIMPLE_SHOP.get());
//					populator.accept(Registry.SIMPLE_SHOP_CREATIVE.get());
//					populator.accept(Registry.WOODEN_STOCKBAR.get());
//					populator.accept(Registry.QUARTZ_STOCKBAR.get());
//					populator.accept(Registry.GREEN_STOCKBAR.get());
//					populator.accept(Registry.RED_STOCKBAR.get());
//				}));
//	}
	
}
