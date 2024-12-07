package com.ordana.verdant.blocks;

import dev.architectury.injectables.annotations.PlatformOnly;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WeedsBlock extends CropBlock {

    private static final int FIRE_SPREAD = 60;
    private static final int FLAMMABILITY = 100;

    public WeedsBlock(Properties settings) {
        super(settings);
        RegHelper.registerBlockFlammability(this, FIRE_SPREAD, FLAMMABILITY);
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return FIRE_SPREAD;
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return FLAMMABILITY;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = this.getAge(state);
        if (i < this.getMaxAge()) {
            float f = getGrowthSpeed(this, level, pos);
            if (random.nextInt((int) (25.0F / f) + 1) == 0) {
                level.setBlock(pos, this.getStateForAge(i + 1), 2);
            }
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return this;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (this.getAge(state) == this.getMaxAge() && random.nextInt(10) == 0) {
            double r = 0.3;
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * r;
            double y = pos.getY() + 0.8 + (random.nextDouble() - 0.5) * r;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * r;
            level.addParticle(ParticleTypes.WHITE_ASH, x, y, z, 0.1D, 0.5D, 0.1D);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.random.nextFloat() < 0.1 && entity instanceof Player player) { //no more dead villagers
            if (!level.isClientSide && state.getValue(AGE) > 0 && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                if (player.getItemBySlot(EquipmentSlot.FEET).isEmpty()) {
                    double d0 = Math.abs(entity.getX() - entity.xOld);
                    double d1 = Math.abs(entity.getZ() - entity.zOld);
                    if (d0 >= 0.003F || d1 >=  0.003F) {
                        entity.hurt(level.damageSources().sweetBerryBush(), 1.0F);
                    }
                }
            }
        }
    }

    //not needed on forge
    //commented out the PlatformOnlys because it wasnt working on forge?? ~o

    //@PlatformOnly(PlatformOnly.FABRIC)
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return this.mayPlaceOn(level.getBlockState(below), level, below);
    }

    //@PlatformOnly(PlatformOnly.FABRIC)
    protected boolean mayPlaceOn(BlockState floor, BlockGetter level, BlockPos pos) {
        return floor.is(BlockTags.DIRT) || (floor.getBlock() instanceof FarmBlock);
    }

}
