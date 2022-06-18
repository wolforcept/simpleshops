package wolforce.utils;

public class UtilTags {

//	private static HashMap<String, TagKey<Item>> items = new HashMap<>();
//
//	static {
//		items.put("wool", ItemTags.WOOL);
//		items.put("planks", ItemTags.PLANKS);
//		items.put("stone_bricks", ItemTags.STONE_BRICKS);
//		items.put("wooden_buttons", ItemTags.WOODEN_BUTTONS);
//		items.put("buttons", ItemTags.BUTTONS);
//		items.put("carpets", ItemTags.CARPETS);
//		items.put("wooden_doors", ItemTags.WOODEN_DOORS);
//		items.put("wooden_stairs", ItemTags.WOODEN_STAIRS);
//		items.put("wooden_slabs", ItemTags.WOODEN_SLABS);
//		items.put("wooden_fences", ItemTags.WOODEN_FENCES);
//		items.put("wooden_pressure_plates", ItemTags.WOODEN_PRESSURE_PLATES);
//		items.put("wooden_trapdoors", ItemTags.WOODEN_TRAPDOORS);
//		items.put("doors", ItemTags.DOORS);
//		items.put("saplings", ItemTags.SAPLINGS);
//		items.put("logs_that_burn", ItemTags.LOGS_THAT_BURN);
//		items.put("logs", ItemTags.LOGS);
//		items.put("dark_oak_logs", ItemTags.DARK_OAK_LOGS);
//		items.put("oak_logs", ItemTags.OAK_LOGS);
//		items.put("birch_logs", ItemTags.BIRCH_LOGS);
//		items.put("acacia_logs", ItemTags.ACACIA_LOGS);
//		items.put("jungle_logs", ItemTags.JUNGLE_LOGS);
//		items.put("spruce_logs", ItemTags.SPRUCE_LOGS);
//		items.put("crimson_stems", ItemTags.CRIMSON_STEMS);
//		items.put("warped_stems", ItemTags.WARPED_STEMS);
//		items.put("banners", ItemTags.BANNERS);
//		items.put("sand", ItemTags.SAND);
//		items.put("stairs", ItemTags.STAIRS);
//		items.put("slabs", ItemTags.SLABS);
//		items.put("walls", ItemTags.WALLS);
//		items.put("anvil", ItemTags.ANVIL);
//		items.put("rails", ItemTags.RAILS);
//		items.put("leaves", ItemTags.LEAVES);
//		items.put("trapdoors", ItemTags.TRAPDOORS);
//		items.put("small_flowers", ItemTags.SMALL_FLOWERS);
//		items.put("beds", ItemTags.BEDS);
//		items.put("fences", ItemTags.FENCES);
//		items.put("tall_flowers", ItemTags.TALL_FLOWERS);
//		items.put("flowers", ItemTags.FLOWERS);
//		items.put("piglin_repellents", ItemTags.PIGLIN_REPELLENTS);
//		items.put("piglin_loved", ItemTags.PIGLIN_LOVED);
//		items.put("ignored_by_piglin_babies", ItemTags.IGNORED_BY_PIGLIN_BABIES);
//		items.put("piglin_food", ItemTags.PIGLIN_FOOD);
//		items.put("fox_food", ItemTags.FOX_FOOD);
//		items.put("gold_ores", ItemTags.GOLD_ORES);
//		items.put("iron_ores", ItemTags.IRON_ORES);
//		items.put("diamond_ores", ItemTags.DIAMOND_ORES);
//		items.put("redstone_ores", ItemTags.REDSTONE_ORES);
//		items.put("lapis_ores", ItemTags.LAPIS_ORES);
//		items.put("coal_ores", ItemTags.COAL_ORES);
//		items.put("emerald_ores", ItemTags.EMERALD_ORES);
//		items.put("copper_ores", ItemTags.COPPER_ORES);
//		items.put("non_flammable_wood", ItemTags.NON_FLAMMABLE_WOOD);
//		items.put("soul_fire_base_blocks", ItemTags.SOUL_FIRE_BASE_BLOCKS);
//		items.put("candles", ItemTags.CANDLES);
//		items.put("dirt", ItemTags.DIRT);
//		items.put("terracotta", ItemTags.TERRACOTTA);
//		items.put("boats", ItemTags.BOATS);
//		items.put("fishes", ItemTags.FISHES);
//		items.put("signs", ItemTags.SIGNS);
//		items.put("music_discs", ItemTags.MUSIC_DISCS);
//		items.put("creeper_drop_music_discs", ItemTags.CREEPER_DROP_MUSIC_DISCS);
//		items.put("coals", ItemTags.COALS);
//		items.put("arrows", ItemTags.ARROWS);
//		items.put("lectern_books", ItemTags.LECTERN_BOOKS);
//		items.put("beacon_payment_items", ItemTags.BEACON_PAYMENT_ITEMS);
//		items.put("stone_tool_materials", ItemTags.STONE_TOOL_MATERIALS);
//		items.put("stone_crafting_materials", ItemTags.STONE_CRAFTING_MATERIALS);
//		items.put("freeze_immune_wearables", ItemTags.FREEZE_IMMUNE_WEARABLES);
//		items.put("axolotl_tempt_items", ItemTags.AXOLOTL_TEMPT_ITEMS);
//		items.put("occludes_vibration_signals", ItemTags.OCCLUDES_VIBRATION_SIGNALS);
//		items.put("cluster_max_harvestables", ItemTags.CLUSTER_MAX_HARVESTABLES);
//	}

//	public static List<Block> getTagBlocks(String tagName) {
//		TagKey<Block> tagKey = ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation(tagName));
//		if (ForgeRegistries.BLOCKS.tags().isKnownTagName(tagKey))
//			return ForgeRegistries.BLOCKS.tags().getTag(tagKey).stream().toList();
//		return null;
//	}
//
//	public static List<Item> getTagItems(String tagName) {
//		TagKey<Item> tagKey = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(tagName));
//		if (ForgeRegistries.ITEMS.tags().isKnownTagName(tagKey))
//			return ForgeRegistries.ITEMS.tags().getTag(tagKey).stream().toList();
//		return null;
//	}
//
//	public static boolean tagHasBlock(String tagName, Block b) {
//		TagKey<Block> tagKey = ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation(tagName));
//		if (ForgeRegistries.BLOCKS.tags().isKnownTagName(tagKey))
//			return ForgeRegistries.BLOCKS.tags().getTag(tagKey).contains(b);
//		return false;
//	}
//
//	public static boolean tagHasItem(String tagName, Item b) {
//		TagKey<Item> tagKey = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(tagName));
//		if (ForgeRegistries.ITEMS.tags().isKnownTagName(tagKey))
//			return ForgeRegistries.ITEMS.tags().getTag(tagKey).contains(b);
//		return false;
//	}

}
