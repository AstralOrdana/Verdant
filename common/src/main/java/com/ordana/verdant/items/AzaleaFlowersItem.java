package com.ordana.verdant.items;


import com.ordana.verdant.reg.ModParticles;
import com.ordana.verdant.util.WeatheringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AzaleaFlowersItem extends BlockItem {
    public AzaleaFlowersItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState state = level.getBlockState(pos);
        var flowery = WeatheringHelper.getAzaleaGrowth(state).orElse(null);

        if (flowery != null) {
            Player player = context.getPlayer();

            level.playSound(player, pos, SoundEvents.FLOWERING_AZALEA_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ModParticles.GRAVITY_AZALEA_FLOWER.get(), UniformInt.of(3, 5));
            if (player != null && !player.getAbilities().instabuild) context.getItemInHand().shrink(1);
            level.setBlockAndUpdate(pos, flowery);
            return InteractionResult.CONSUME;
        }

        var ret = super.useOn(context);
        if (ret == InteractionResult.FAIL) return InteractionResult.PASS;

        return ret;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (level.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.WATER) {
            BlockHitResult blockHitResult2 = blockHitResult.withPosition(blockHitResult.getBlockPos().above());
            InteractionResult interactionResult = super.useOn(new UseOnContext(player, hand, blockHitResult2));
            return new InteractionResultHolder<>(interactionResult, player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        return super.canPlace(context, state) &&
                context.getLevel().getFluidState(context.getClickedPos()).isEmpty();
    }
}
