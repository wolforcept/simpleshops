package wolforce.simpleshops;

import java.util.HashMap;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimpleShops.MODID)
public class SimpleShops {

	public static final String MODID = "simpleshops";

	public SimpleShops() {
		MinecraftForge.EVENT_BUS.register(this);
		SimpleShopTileEntity.REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static HashMap<String, Block> blocks = new HashMap<>();

	public static ItemGroup group = new ItemGroup(ItemGroup.getGroupCountSafe(), "Simple Shops") {

		@Override
		public ItemStack makeIcon() {
			return new ItemStack(simple_shop);
		}
	};

	public static Block simple_shop, creative_simple_shop;

	public static void setupBlocks() {

		Block.Properties wood = Properties.of(Material.WOOD, MaterialColor.WOOD).sound(SoundType.WOOD)//
				.strength(-1.0F, 3600000.0F).noDrops().noOcclusion();

		simple_shop = addBlock("simple_shop", new SimpleShopBlock(wood, false));
		creative_simple_shop = addBlock("creative_simple_shop", new SimpleShopBlock(wood, true));

	}

	private static Block addBlock(String regId, Block block) {
		block.setRegistryName(new ResourceLocation(MODID, regId));
		blocks.put(regId, block);
		return block;
	}

}
