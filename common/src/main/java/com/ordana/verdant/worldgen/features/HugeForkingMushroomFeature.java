package com.ordana.verdant.worldgen.features;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.ordana.verdant.worldgen.feature_configs.HugeForkingMushroomFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Set;
import java.util.function.BiConsumer;

public class HugeForkingMushroomFeature extends Feature<HugeForkingMushroomFeatureConfig> {
    public HugeForkingMushroomFeature(Codec<HugeForkingMushroomFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<HugeForkingMushroomFeatureConfig> context) {

        WorldGenLevel level = context.level();
        RandomSource random = level.getRandom();
        BlockPos blockPos = context.origin();
        HugeForkingMushroomFeatureConfig config = context.config();
        Set<BlockPos> set = Sets.newHashSet();
        BiConsumer<BlockPos, BlockState> blockSetter = (blockPosx, blockState) -> {
            set.add(blockPosx.immutable());
            level.setBlock(blockPosx, blockState, 19);
        };

        BiConsumer<BlockPos, BlockState> blockSetter2 = (blockPosx, blockState) -> {
            set.add(blockPosx.immutable());
            level.setBlock(blockPosx, blockState.setValue(PipeBlock.NORTH, false).setValue(PipeBlock.SOUTH, false).setValue(PipeBlock.EAST, false).setValue(PipeBlock.WEST, false), 19);
        };

        int radius = random.nextInt(2) + (config.height);


        this.placeBranches(level, blockSetter, random, config, blockPos);

        //this.placeLeavesRow(level, blockSetter, random, config, blockPos, radius, 0, large);

        return true;
    }

    protected void placeCap(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, HugeForkingMushroomFeatureConfig config, BlockPos pos) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);
        if (config.largeCap) {
            for (Direction dir : Direction.values()) {
                if (dir != Direction.DOWN)
                    blockSetter.accept(mutableBlockPos.relative(dir), config.foliageProvider.getState(random, mutableBlockPos));
            }
            blockSetter.accept(mutableBlockPos.relative(Direction.NORTH).relative(Direction.EAST), config.foliageProvider.getState(random, mutableBlockPos));
            blockSetter.accept(mutableBlockPos.relative(Direction.SOUTH).relative(Direction.EAST), config.foliageProvider.getState(random, mutableBlockPos));
            blockSetter.accept(mutableBlockPos.relative(Direction.NORTH).relative(Direction.WEST), config.foliageProvider.getState(random, mutableBlockPos));
            blockSetter.accept(mutableBlockPos.relative(Direction.SOUTH).relative(Direction.WEST), config.foliageProvider.getState(random, mutableBlockPos));

            mutableBlockPos.move(Direction.UP);
            for (Direction dir : Direction.values()) {
                if (dir != Direction.DOWN && dir != Direction.UP)
                    blockSetter.accept(mutableBlockPos.relative(dir), config.foliageProvider.getState(random, mutableBlockPos));
            }
        }
        else {
            for (int i = 0; i < config.capHeight; ++i) {
                for (Direction dir : Direction.values()) {
                    if (dir != Direction.DOWN)
                        blockSetter.accept(mutableBlockPos.relative(dir), config.foliageProvider.getState(random, mutableBlockPos));
                }
                mutableBlockPos.move(Direction.UP);
            }
            mutableBlockPos.move(Direction.DOWN);
            if (config.lightProvider != null)
                blockSetter.accept(mutableBlockPos, config.lightProvider.getState(random, mutableBlockPos));
        }
    }


    protected void placeBranches(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, HugeForkingMushroomFeatureConfig config, BlockPos pos) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);
        var state = config.stemProvider.getState(random, pos);

        Direction dirf = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Direction dir2 = dirf.getClockWise();
        Direction dir3 = dir2.getClockWise();

        if (random.nextBoolean() && config.centerHeight > 0) {
            for (int i = 0; i < config.centerHeight; ++i) {
                blockSetter.accept(mutableBlockPos, state);
                mutableBlockPos.move(Direction.UP);
            }
            if (random.nextBoolean()) {
                blockSetter.accept(mutableBlockPos, state);
                mutableBlockPos.move(Direction.UP);
                blockSetter.accept(mutableBlockPos, state);
            }
            blockSetter.accept(mutableBlockPos, state);
            this.placeCap(level, blockSetter, random, config, mutableBlockPos);
            mutableBlockPos.set(pos);
        }
        for(Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
            blockSetter.accept(mutableBlockPos.relative(dir).relative(Direction.UP), state);
            if (random.nextBoolean()) {
                blockSetter.accept(mutableBlockPos, state);
                mutableBlockPos.move(Direction.UP);
                blockSetter.accept(mutableBlockPos, state);
            }
            if (random.nextBoolean()) {
                for (int i = 0; i < config.height; ++i) {
                    blockSetter.accept(mutableBlockPos, state);
                    mutableBlockPos.move(Direction.UP).move(dir);
                    blockSetter.accept(mutableBlockPos, state);
                    mutableBlockPos.move(Direction.UP);
                    blockSetter.accept(mutableBlockPos, state);
                }
                if (random.nextBoolean()) {
                    mutableBlockPos.move(Direction.UP);
                    blockSetter.accept(mutableBlockPos, state);
                }
                this.placeCap(level, blockSetter, random, config, mutableBlockPos);
            }
            else {
                for (int i = 0; i < config.height; ++i) {
                    blockSetter.accept(mutableBlockPos, state);
                    mutableBlockPos.move(Direction.UP).move(dir);
                    blockSetter.accept(mutableBlockPos, state);
                    mutableBlockPos.move(Direction.UP).move(dir);
                }
                if (random.nextBoolean()) {
                    blockSetter.accept(mutableBlockPos, state);
                    mutableBlockPos.move(Direction.UP);
                    blockSetter.accept(mutableBlockPos, state);
                }
            }
            blockSetter.accept(mutableBlockPos, state);
            this.placeCap(level, blockSetter, random, config, mutableBlockPos);
            mutableBlockPos.set(pos);
            if (random.nextFloat() >= 0.5) break;
        }

        /*
        blockSetter.accept(mutableBlockPos.relative(dir), state);
        mutableBlockPos.move(Direction.UP);
        blockSetter.accept(mutableBlockPos, state);
        mutableBlockPos.move(Direction.UP).move(dir);
        blockSetter.accept(mutableBlockPos, state);
        mutableBlockPos.move(Direction.UP);
        blockSetter.accept(mutableBlockPos, state);

        for(Direction dfir : Direction.allShuffled(random)) {
            if (dir != Direction.DOWN && dir != Direction.UP) {
                blockSetter.accept(mutableBlockPos.relative(dir), config.foliageProvider.getState(random, pos));
            }
        }
         */
    }



    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        {
            return localX == range && localZ == range && range > 0;
        }
    }

    protected boolean shouldSkipLocationSignedOg(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        int i;
        int j;
        if (large) {
            i = Math.min(Math.abs(localX), Math.abs(localX - 1));
            j = Math.min(Math.abs(localZ), Math.abs(localZ - 1));
        } else {
            i = Math.abs(localX);
            j = Math.abs(localZ);
        }

        return this.shouldSkipLocation(random, i, localY, j, range, large);
    }
}