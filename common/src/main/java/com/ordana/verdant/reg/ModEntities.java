package com.ordana.verdant.reg;

import com.ordana.verdant.Verdant;
import com.ordana.verdant.entities.FallingLayerEntity;
import com.ordana.verdant.entities.FallingPropaguleEntity;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class ModEntities {

    public static void init() {
    }

    //entities
    public static Supplier<EntityType<FallingLayerEntity>> FALLING_LAYER = RegHelper.registerEntityType(
            Verdant.res("falling_layer"),
            FallingLayerEntity::new, MobCategory.MISC, 0.98F, 0.98F, 10, 20);

    public static Supplier<EntityType<FallingPropaguleEntity>> FALLING_PROPAGULE = RegHelper.registerEntityType(
            Verdant.res("falling_propagule"),
            FallingPropaguleEntity::new, MobCategory.MISC, 0.28F, 0.98F, 10, 20);

}
