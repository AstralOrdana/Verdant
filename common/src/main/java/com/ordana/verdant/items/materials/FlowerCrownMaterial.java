package com.ordana.verdant.items.materials;

import com.ordana.verdant.reg.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class FlowerCrownMaterial implements ArmorMaterial {

    public static final FlowerCrownMaterial INSTANCE = new FlowerCrownMaterial();

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return 64;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return 0;
    }

    @Override
    public int getEnchantmentValue() {
        return 64;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.AZALEA_FLOWERS.get());
    }

    @Override
    public String getName() {
        return "flower";
    }

    @Override
    public float getToughness() {
        return 0f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
