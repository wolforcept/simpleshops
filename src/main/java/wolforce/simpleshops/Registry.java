package wolforce.simpleshops;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			SimpleShops.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			SimpleShops.MODID);
	public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SimpleShops.MODID);

	public static void registerBus(IEventBus bus) {
		TAB = new CreativeModeTab(CreativeModeTab.getGroupCountSafe(), "Simple Shops") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(Registry.SIMPLE_SHOP.get());
			}
		};

		BLOCKS.register(bus);
		ITEMS.register(bus);
		TILES.register(bus);
	}

	public static CreativeModeTab TAB = null;

	// block materials
	public static Block.Properties wood = Block.Properties.of(Material.WOOD, MaterialColor.WOOD).sound(SoundType.WOOD)//
			.strength(-1.0F, 3600000.0F).noLootTable().noOcclusion();

	// Simple Shop
	public static final String SIMPLE_SHOP_REGNAME = "simple_shop";
	public static final RegistryObject<Block> SIMPLE_SHOP = //
			BLOCKS.register(SIMPLE_SHOP_REGNAME, () -> new SimpleShopBlock(wood, false));
	public static final RegistryObject<Item> SIMPLE_SHOP_ITEM = ITEMS.register(SIMPLE_SHOP_REGNAME,
			() -> new BlockItem(SIMPLE_SHOP.get(), new Item.Properties().tab(TAB)));

	// Creative Simple Shop
	public static final String SIMPLE_SHOP_CREATIVE_REGNAME = "creative_simple_shop";
	public static final RegistryObject<Block> SIMPLE_SHOP_CREATIVE = //
			BLOCKS.register(SIMPLE_SHOP_CREATIVE_REGNAME, () -> new SimpleShopBlock(wood, true));
	public static final RegistryObject<Item> SIMPLE_SHOP_CREATIVE_ITEM = ITEMS.register(SIMPLE_SHOP_CREATIVE_REGNAME,
			() -> new BlockItem(SIMPLE_SHOP_CREATIVE.get(), new Item.Properties().tab(TAB)));

	// Stockbars
	public static final RegistryObject<Item> WOODEN_STOCKBAR = ITEMS.register("wooden_stockbar",
			() -> new StockBarItem(new Item.Properties().tab(TAB),16,11));
	public static final RegistryObject<Item> QUARTZ_STOCKBAR = ITEMS.register("quartz_stockbar",
			() -> new StockBarItem(new Item.Properties().tab(TAB),16,13));
	public static final RegistryObject<Item> GREEN_STOCKBAR = ITEMS.register("green_stockbar",
			() -> new StockBarItem(new Item.Properties().tab(TAB),0,11));
	public static final RegistryObject<Item> RED_STOCKBAR = ITEMS.register("red_stockbar",
			() -> new StockBarItem(new Item.Properties().tab(TAB),0,13));

	// Tile
	public static final RegistryObject<BlockEntityType<SimpleShopTileEntity>> SIMPLE_SHOP_TILE = TILES
			.register(SIMPLE_SHOP_REGNAME, () -> BlockEntityType.Builder
					.of(SimpleShopTileEntity::new, SIMPLE_SHOP.get(), SIMPLE_SHOP_CREATIVE.get()).build(null));

}
