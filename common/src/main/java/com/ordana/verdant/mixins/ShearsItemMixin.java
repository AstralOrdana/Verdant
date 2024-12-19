package com.ordana.verdant.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.ordana.verdant.reg.ModTags;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {

    @WrapOperation(method = "getDestroySpeed",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/Blocks;VINE:Lnet/minecraft/world/level/block/Block;")
            ),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0)

    )
    private boolean addShearables(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || instance.is(ModTags.SHEARABLE);
    }
}
