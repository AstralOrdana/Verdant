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
    public static final TagKey<Biome> HAS_DUNE_GRASS = registerBiomeTag("has_dune_grass");
    public static final TagKey<Biome> HAS_MOSS = registerBiomeTag("has_moss");



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
