package com.ordana.verdant.fabric;

import com.ordana.verdant.Verdant;
import com.ordana.verdant.reg.ModEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;


public class VerdantFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        //loads registries
        Verdant.commonInit();

        //events
        UseBlockCallback.EVENT.register(VerdantFabric::onRightClickBlock);
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return ModEvents.onBlockCLicked(player.getItemInHand(hand), player, level, hand, hitResult);
    }

}
