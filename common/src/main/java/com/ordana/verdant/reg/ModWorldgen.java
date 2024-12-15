package com.ordana.verdant.reg;

import com.ordana.verdant.PlatformSpecific;
import com.ordana.verdant.Verdant;
import com.ordana.verdant.worldgen.feature_configs.SaguaroFeatureConfig;
import com.ordana.verdant.worldgen.features.SaguaroFeature;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

public class ModWorldgen {

    public static final Supplier<Feature<SaguaroFeatureConfig>> SAGUARO_FEATURE = RegHelper.registerFeature(
            Verdant.res("saguaro"), () ->
                    new SaguaroFeature(SaguaroFeatureConfig.CODEC));

    public static void init() {

        //grasses
        ResourceKey<PlacedFeature> dune_grass_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("dune_grass_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_DUNE_GRASS, dune_grass_patch);

        ResourceKey<PlacedFeature> dogwood_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("dogwood_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_DOGWOOD, dogwood_patch);

        ResourceKey<PlacedFeature> dogwood_patch_snowy = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("dogwood_patch_snowy"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_DOGWOOD_SNOWY, dogwood_patch_snowy);

        ResourceKey<PlacedFeature> cattail_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("cattail_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_CATTAILS, cattail_patch);

        ResourceKey<PlacedFeature> clover_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("clover_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_CLOVERS, clover_patch);

        ResourceKey<PlacedFeature> dense_grass_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("dense_grass_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_DENSE_GRASS, dense_grass_patch);

        ResourceKey<PlacedFeature> barley_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("barley_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_BARLEY, barley_patch);

        //plants
        ResourceKey<PlacedFeature> moss_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("moss_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_MOSS, moss_patch);

        ResourceKey<PlacedFeature> ivy_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("ivy_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_IVY, ivy_patch);

        ResourceKey<PlacedFeature> duckweed_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("duckweed_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_DUCKWEED, duckweed_patch);

        ResourceKey<PlacedFeature> saguaro_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("saguaro_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_SAGUARO, saguaro_patch);

        ResourceKey<PlacedFeature> aloe_vera_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("aloe_vera_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_ALOE_VERA, aloe_vera_patch);

        ResourceKey<PlacedFeature> boxwood_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("boxwood_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_BOXWOOD, boxwood_patch);

        ResourceKey<PlacedFeature> primrose_patch = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("primrose_patch"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_PRIMROSE, primrose_patch);

        ResourceKey<PlacedFeature> primrose_patch_swamp = ResourceKey.create(Registries.PLACED_FEATURE, Verdant.res("primrose_patch_swamp"));
        PlatformSpecific.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_PRIMROSE_SWAMP, primrose_patch_swamp);

    }
}
