package com.ordana.verdant.blocks;

import com.ordana.verdant.entities.FallingPropaguleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.MangrovePropaguleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;

import javax.swing.text.html.BlockView;
import java.util.Random;

public class ModPropaguleBlock extends MangrovePropaguleBlock implements Fallable {
    private static final BooleanProperty WATERLOGGED;

    public ModPropaguleBlock(Properties properties) {
        super(properties);
    }

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean notify) {
        level.scheduleTick(pos, this, this.getFallDelay());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return state.getValue(HANGING) ? (level.getBlockState(pos.above()).is(Blocks.MANGROVE_LEAVES)
            || this.mayPlaceOn(level.getBlockState(pos.below()), level, pos)) : super.canSurvive(state, level, pos);
    }

    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        level.setBlockAndUpdate(pos, state.setValue(HANGING, false));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        else if ((!state.getValue(HANGING) || (state.getValue(HANGING) && state.getValue(AGE) < 4)) && !state.canSurvive(level, pos)) {
            level.removeBlock(pos, true);
        }
        level.scheduleTick(pos, this, this.getFallDelay());
        return state;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (canFallThrough(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight() && (level.isEmptyBlock(pos.above()) || (level.getBlockState(pos.above())).getBlock() instanceof LeafPileBlock) && state.getValue(HANGING) && state.getValue(AGE) == 4) {
            FallingBlockEntity fallingblockentity = FallingPropaguleEntity.fall(level, pos, state.setValue(HANGING, true).setValue(AGE, 4));
            this.configureFallingBlockEntity(fallingblockentity);
        }
    }

    protected void configureFallingBlockEntity(FallingBlockEntity entity) {
    }

    protected int getFallDelay() {
        return 2;
    }

    public static boolean canFallThrough(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced();
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (random.nextInt(16) == 0) {
            BlockPos blockPos = pos.below();
            if (canFallThrough(level.getBlockState(blockPos))) {
                double d = (double)pos.getX() + random.nextDouble();
                double e = (double)pos.getY() - 0.05D;
                double f = (double)pos.getZ() + random.nextDouble();
                level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MANGROVE_PROPAGULE.defaultBlockState()), d, e, f, 0.0D, 0.0D, 0.0D);
            }
        }

    }

    public int color(BlockState state, BlockView level, BlockPos pos) {
        return -16777216;
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }
}