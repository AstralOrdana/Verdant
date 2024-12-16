package com.ordana.verdant.blocks.fungi;

import com.ordana.verdant.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModMushroomBlock extends BushBlock {
    protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);

    public ModMushroomBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        if (random.nextInt(25) == 0) {
            if (state.is(ModBlocks.BUTTON_MUSHROOM.get())) {
                level.setBlock(pos, ModBlocks.CRIMINI.get().defaultBlockState(), 2);
            }
            if (state.is(ModBlocks.CRIMINI.get())) {
                level.setBlock(pos, ModBlocks.PORTABELLA.get().defaultBlockState(), 2);
            }

            int i = 5;
            boolean j = true;

            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
                if (level.getBlockState(blockPos).is(this)) {
                    --i;
                    if (i <= 0) {
                        return;
                    }
                }
            }

            BlockPos blockPos2 = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

            for(int k = 0; k < 4; ++k) {
                if (level.isEmptyBlock(blockPos2) && state.canSurvive(level, blockPos2)) {
                    pos = blockPos2;
                }

                blockPos2 = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }
            if (level.isEmptyBlock(blockPos2) && state.canSurvive(level, blockPos2) && !state.is(ModBlocks.CRIMINI.get()) && !state.is(ModBlocks.BUTTON_MUSHROOM.get())) {
                if (state.is(ModBlocks.PORTABELLA.get())) level.setBlock(blockPos2, ModBlocks.BUTTON_MUSHROOM.get().defaultBlockState(), 2);
                else if (!state.is(ModBlocks.CRIMINI.get()) && !state.is(ModBlocks.BUTTON_MUSHROOM.get())) level.setBlock(blockPos2, state, 2);
            }
        }

    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isSolidRender(level, pos);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.below();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
            return true;
        } else {
            return level.getRawBrightness(pos, 0) < 13 && this.mayPlaceOn(blockState, level, blockPos);
        }
    }
}
