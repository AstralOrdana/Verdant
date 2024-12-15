package com.ordana.verdant.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SandBushBlock extends BushBlock {
  public SandBushBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
    return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND);
  }
}
