package com.ordana.verdant.reg;

import com.ordana.verdant.PlatformSpecific;
import com.ordana.verdant.Verdant;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModWorldgen {

    public static void init() {

        //grasses
        ResourceKey<PlacedFeature> dune_grass_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("dune_grass_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_DUNE_GRASS, dune_grass_patch);

        ResourceKey<PlacedFeature> cattail_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("cattail_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.HAS_SWAMP_HUT, cattail_patch);

        ResourceKey<PlacedFeature> clover_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("clover_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.HAS_VILLAGE_PLAINS, clover_patch);

        ResourceKey<PlacedFeature> dense_grass_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("dense_grass_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_SAVANNA, dense_grass_patch);

        ResourceKey<PlacedFeature> barley_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("barley_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.HAS_VILLAGE_PLAINS, barley_patch);

        //duckweed
        ResourceKey<PlacedFeature> duckweed_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("duckweed_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.HAS_SWAMP_HUT, duckweed_patch);

        //moss
        ResourceKey<PlacedFeature> moss_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("moss_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_MOSS, moss_patch);

        //ivy
        ResourceKey<PlacedFeature> ivy_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("ivy_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_IVY, ivy_patch);

        //flowers
        ResourceKey<PlacedFeature> muscari_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("muscari_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_JUNGLE, muscari_patch);



    }
}
