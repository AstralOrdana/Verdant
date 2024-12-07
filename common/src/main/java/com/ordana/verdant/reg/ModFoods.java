package com.ordana.verdant.reg;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties MOSS_CLUMP = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.2F).alwaysEat().fast()
            .build();

    public static final FoodProperties GOLDEN_MOSS_CLUMP = (new FoodProperties.Builder())
            .nutrition(4).saturationMod(0.6F).alwaysEat()
            .effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1F)
            .build();

    public static final FoodProperties ENCHANTED_GOLDEN_MOSS_CLUMP = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.8F).alwaysEat()
            .effect(new MobEffectInstance(MobEffects.ABSORPTION, 1600, 2), 1F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION, 300, 1), 1F)
            .effect(new MobEffectInstance(MobEffects.WATER_BREATHING, 1600, 2), 1F).build();

}
