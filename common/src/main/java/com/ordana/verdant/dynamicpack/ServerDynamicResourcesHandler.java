package com.ordana.verdant.dynamicpack;

import com.ordana.verdant.Verdant;
import com.ordana.verdant.reg.ModBlocks;
import com.ordana.verdant.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ServerDynamicResourcesHandler extends DynServerResourcesGenerator {

    public static final ServerDynamicResourcesHandler INSTANCE = new ServerDynamicResourcesHandler();

    public ServerDynamicResourcesHandler() {
        super(new DynamicDataPack(Verdant.res("generated_pack")));
        this.dynamicPack.setGenerateDebugResources(PlatHelper.isDev());
    }

    @Override
    public Logger getLogger() {
        return Verdant.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {

        //tag
        SimpleTagBuilder tag = SimpleTagBuilder.of(Verdant.res("leaf_piles"));
        tag.addEntries(ModBlocks.LEAF_PILES.values());
        dynamicPack.addTag(tag, Registries.BLOCK);
        dynamicPack.addTag(tag, Registries.BLOCK);

        dynamicPack.addTag(SimpleTagBuilder.of(Verdant.res("bark"))
                .addEntries(ModItems.BARK.values()), Registries.ITEM);

        StaticResource lootTable = StaticResource.getOrLog(manager, ResType.BLOCK_LOOT_TABLES.getPath(Verdant.res("oak_leaf_pile")));
        StaticResource recipe = StaticResource.getOrLog(manager, ResType.RECIPES.getPath(Verdant.res("oak_leaf_pile")));

        for (var e : ModBlocks.LEAF_PILES.entrySet()) {
            LeavesType leafType = e.getKey();
            if (!leafType.isVanilla()) {
                var v = e.getKey();

                String path = leafType.getNamespace() + "/" + leafType.getTypeName();
                String id = path + "_leaf_pile";

                String leavesId = Utils.getID(leafType.leaves).toString();

                //TODO: use new system
                try {
                    addLeafPileJson(Objects.requireNonNull(lootTable), id, leavesId);
                } catch (Exception ex) {
                    getLogger().error("Failed to generate Leaf Pile loot table for {} : {}", v, ex);
                }

                try {
                    addLeafPileJson(Objects.requireNonNull(recipe), id, leavesId);
                } catch (Exception ex) {
                    getLogger().error("Failed to generate Leaf Pile recipe for {} : {}", v, ex);
                }

            }
        }

    }

    public void addLeafPileJson(StaticResource resource, String id, String leafBlockId) {
        String string = new String(resource.data, StandardCharsets.UTF_8);

        String path = resource.location.getPath().replace("oak_leaf_pile", id);

        string = string.replace("oak_leaf_pile", id);
        string = string.replace("minecraft:oak_leaves", leafBlockId);

        //adds modified under my namespace
        ResourceLocation newRes = Verdant.res(path);
        dynamicPack.addBytes(newRes, string.getBytes(), ResType.GENERIC);
    }
}
