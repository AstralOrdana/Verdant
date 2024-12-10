package com.ordana.verdant.worldgen.feature_configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.stream.Stream;

public class SaguaroFeatureConfig implements FeatureConfiguration {

    public static final Codec<SaguaroFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(

            IntProvider.CODEC.fieldOf("height").forGetter((config) -> config.height))

            .apply(instance, SaguaroFeatureConfig::new));

    public final IntProvider height;

    public SaguaroFeatureConfig(IntProvider height) {
        this.height = height;
    }

    @Override
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return FeatureConfiguration.super.getFeatures();
    }
}
