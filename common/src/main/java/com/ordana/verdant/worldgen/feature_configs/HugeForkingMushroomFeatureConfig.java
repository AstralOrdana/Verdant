package com.ordana.verdant.worldgen.feature_configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.stream.Stream;

public class HugeForkingMushroomFeatureConfig implements FeatureConfiguration {

    public static final Codec<HugeForkingMushroomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(

            BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((hugeForkingMushroomFeatureConfig) -> hugeForkingMushroomFeatureConfig.foliageProvider),
            BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter((hugeForkingMushroomFeatureConfig) -> hugeForkingMushroomFeatureConfig.stemProvider),
            BlockStateProvider.CODEC.fieldOf("light_provider").forGetter((hugeForkingMushroomFeatureConfig) -> hugeForkingMushroomFeatureConfig.lightProvider),
            Codec.intRange(0, 16).fieldOf("height").orElse(2).forGetter((hugeForkingMushroomFeatureConfig) -> hugeForkingMushroomFeatureConfig.height),
            Codec.intRange(0, 16).fieldOf("cap_height").orElse(1).forGetter((hugeForkingMushroomFeatureConfig) -> hugeForkingMushroomFeatureConfig.capHeight),
            Codec.intRange(0, 16).fieldOf("center_height").orElse(0).forGetter((hugeForkingMushroomFeatureConfig) -> hugeForkingMushroomFeatureConfig.centerHeight),
            Codec.BOOL.fieldOf("large_cap").orElse(false).forGetter((blockStripeFeatureConfig) -> blockStripeFeatureConfig.largeCap))

            .apply(instance, HugeForkingMushroomFeatureConfig::new));

    public final BlockStateProvider foliageProvider;
    public final BlockStateProvider lightProvider;
    public final BlockStateProvider stemProvider;
    public int height;
    public int capHeight;
    public int centerHeight;
    public boolean largeCap;

    public HugeForkingMushroomFeatureConfig(BlockStateProvider foliageProvider, BlockStateProvider stemProvider, BlockStateProvider lightProvider, int height, int capHeight, int centerHeight, boolean largeCap) {
        this.foliageProvider = foliageProvider;
        this.lightProvider = lightProvider;
        this.stemProvider = stemProvider;
        this.height = height;
        this.capHeight = capHeight;
        this.centerHeight = centerHeight;
        this.largeCap = largeCap;
    }

    @Override
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return FeatureConfiguration.super.getFeatures();
    }
}
