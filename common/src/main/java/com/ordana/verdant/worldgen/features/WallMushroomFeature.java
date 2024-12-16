package com.ordana.verdant.worldgen.features;

import com.mojang.serialization.Codec;
import com.ordana.verdant.blocks.fungi.FloorAndSidesMushroomBlock;
import com.ordana.verdant.worldgen.feature_configs.WallMushroomFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Collection;

public class WallMushroomFeature extends Feature<WallMushroomFeatureConfig> {
    public WallMushroomFeature(Codec<WallMushroomFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<WallMushroomFeatureConfig> context) {

        WorldGenLevel worldGenLevel = context.level();
        BlockPos blockPos = context.origin();
        WallMushroomFeatureConfig wallMushroomFeatureConfig = context.config();

        if (worldGenLevel.isEmptyBlock(blockPos)) {
            Collection<Direction> var4 = Direction.allShuffled(worldGenLevel.getRandom());

            for (Direction direction : var4) {
                if (direction == Direction.UP) return false;
                if (direction == Direction.DOWN && !wallMushroomFeatureConfig.canPlaceOnFloor) return false;

                if (VineBlock.isAcceptableNeighbour(worldGenLevel, blockPos.relative(direction), direction) && worldGenLevel.getBlockState(blockPos.relative(direction)).is(wallMushroomFeatureConfig.canBePlacedOn)) {
                    if (direction == Direction.DOWN) worldGenLevel.setBlock(blockPos, wallMushroomFeatureConfig.placeBlock.defaultBlockState().setValue(FloorAndSidesMushroomBlock.FLOOR, true), 2);
                    else worldGenLevel.setBlock(blockPos, wallMushroomFeatureConfig.placeBlock.defaultBlockState().setValue(FloorAndSidesMushroomBlock.FACING, direction.getOpposite()).setValue(FloorAndSidesMushroomBlock.FLOOR, false), 2);
                    return true;
                }
            }
        }
        return false;
    }
}
