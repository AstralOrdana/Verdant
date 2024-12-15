package com.ordana.verdant.worldgen.features;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.ordana.verdant.reg.ModBlocks;
import com.ordana.verdant.worldgen.feature_configs.SaguaroFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Set;
import java.util.function.BiConsumer;

public class SaguaroFeature extends Feature<SaguaroFeatureConfig> {
    public SaguaroFeature(Codec<SaguaroFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<SaguaroFeatureConfig> context) {

        WorldGenLevel level = context.level();
        RandomSource random = level.getRandom();
        BlockPos pos = context.origin();
        SaguaroFeatureConfig config = context.config();
        Set<BlockPos> set = Sets.newHashSet();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);
        var state = ModBlocks.SAGUARO_BLOCK.get().defaultBlockState();
        var armState = ModBlocks.SAGUARO_ARM.get().defaultBlockState();

        BiConsumer<BlockPos, BlockState> blockSetter = (blockPosx, blockState) -> {
            set.add(blockPosx.immutable());
            level.setBlock(blockPosx, blockState, 19);
        };

        BiConsumer<BlockPos, BlockState> blockSetter2 = (blockPosx, blockState) -> {
            set.add(blockPosx.immutable());
            level.setBlock(blockPosx, blockState.setValue(PipeBlock.NORTH, false).setValue(PipeBlock.SOUTH, false).setValue(PipeBlock.EAST, false).setValue(PipeBlock.WEST, false), 19);
        };

        int height = config.height.sample(random);
        for (int i = 0; i < height; ++i) {
            blockSetter.accept(mutableBlockPos, state.setValue(BlockStateProperties.ATTACHED, (i + 1) == height));
            if ((i + 1) == height) return false;
            if (random.nextInt(3) == 1) {
                var dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                if (level.isEmptyBlock(mutableBlockPos.relative(dir))) {
                    blockSetter.accept(mutableBlockPos.relative(dir), armState.setValue(BlockStateProperties.FACING, dir));
                    blockSetter.accept(mutableBlockPos.relative(dir).above(), armState.setValue(BlockStateProperties.FACING, Direction.DOWN));

                    if (random.nextBoolean()) {
                        mutableBlockPos.move(Direction.UP);
                        blockSetter.accept(mutableBlockPos.relative(dir), armState.setValue(BlockStateProperties.FACING, Direction.UP));
                        blockSetter.accept(mutableBlockPos.relative(dir).above(), armState.setValue(BlockStateProperties.FACING, Direction.DOWN));
                        mutableBlockPos.move(Direction.DOWN);
                    }
                }
            }
            if (random.nextInt(5) == 1) {
                var dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                if (level.isEmptyBlock(mutableBlockPos.relative(dir))) {
                    blockSetter.accept(mutableBlockPos.relative(dir), armState.setValue(BlockStateProperties.FACING, dir));
                    blockSetter.accept(mutableBlockPos.relative(dir).above(), armState.setValue(BlockStateProperties.FACING, Direction.DOWN));

                    if (random.nextBoolean()) {
                        mutableBlockPos.move(Direction.UP);
                        blockSetter.accept(mutableBlockPos.relative(dir), armState.setValue(BlockStateProperties.FACING, Direction.UP));
                        blockSetter.accept(mutableBlockPos.relative(dir).above(), armState.setValue(BlockStateProperties.FACING, Direction.DOWN));
                        mutableBlockPos.move(Direction.DOWN);
                    }
                }
            }
            mutableBlockPos.move(Direction.UP);
        }

        return false;
    }
}