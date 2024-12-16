package com.ordana.verdant.reg;

import com.ordana.verdant.Verdant;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final TagKey<Item> BARK = registerItemTag("bark");

    public static final TagKey<Block> SAGUARO = registerBlockTag("saguaro");
    public static final TagKey<Block> SAGUARO_PLANTABLE_ON = registerBlockTag("saguaro_plantable_on");

    public static final TagKey<Biome> HAS_IVY = registerBiomeTag("has_ivy");
    public static final TagKey<Biome> HAS_DOGWOOD = registerBiomeTag("has_dogwood");
    public static final TagKey<Biome> HAS_DOGWOOD_SNOWY = registerBiomeTag("has_dogwood_snowy");
    public static final TagKey<Biome> HAS_CATTAILS = registerBiomeTag("has_cattails");
    public static final TagKey<Biome> HAS_CLOVERS = registerBiomeTag("has_clovers");
    public static final TagKey<Biome> HAS_BARLEY = registerBiomeTag("has_barley");
    public static final TagKey<Biome> HAS_DENSE_GRASS = registerBiomeTag("has_dense_grass");
    public static final TagKey<Biome> HAS_DUCKWEED = registerBiomeTag("has_duckweed");
    public static final TagKey<Biome> HAS_SAGUARO = registerBiomeTag("has_saguaro");
    public static final TagKey<Biome> HAS_ALOE_VERA = registerBiomeTag("has_aloe_vera");
    public static final TagKey<Biome> HAS_BOXWOOD = registerBiomeTag("has_boxwood");
    public static final TagKey<Biome> HAS_PRIMROSE = registerBiomeTag("has_primrose");
    public static final TagKey<Biome> HAS_PRIMROSE_SWAMP = registerBiomeTag("has_primrose_swamp");
    public static final TagKey<Biome> HAS_DUNE_GRASS = registerBiomeTag("has_dune_grass");
    public static final TagKey<Biome> HAS_MOSS = registerBiomeTag("has_moss");
    public static final TagKey<Biome> HAS_SHRUB = registerBiomeTag("has_shrub");
    public static final TagKey<Biome> HAS_DUNE_BUSH = registerBiomeTag("has_dune_bush");



    private static TagKey<Item> registerItemTag(String id) {
        return TagKey.create(Registries.ITEM, Verdant.res(id));
    }

    private static TagKey<Block> registerBlockTag(String id) {
        return TagKey.create(Registries.BLOCK, Verdant.res(id));
    }

    private static TagKey<Biome> registerBiomeTag(String id) {
        return TagKey.create(Registries.BIOME, Verdant.res(id));
    }

}
