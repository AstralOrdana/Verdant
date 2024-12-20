package com.ordana.verdant.items;

import com.ordana.verdant.reg.ModBlocks;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

public class DuckweedItem extends BlockItem {
    public DuckweedItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();

        if (!level.getBlockState(pos).is(ModBlocks.DUCKWEED.get())) return InteractionResult.PASS;
        else return super.useOn(context);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockHitResult blockHitResult2 = blockHitResult.withPosition(blockHitResult.getBlockPos().above());
        InteractionResult interactionResult = super.useOn(new UseOnContext(player, usedHand, blockHitResult2));
        return new InteractionResultHolder<>(interactionResult, player.getItemInHand(usedHand));
    }
}
