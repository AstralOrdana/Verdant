package com.ordana.verdant.util;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.ordana.verdant.blocks.LeafPileBlock;
import com.ordana.verdant.configs.CommonConfigs;
import com.ordana.verdant.reg.ModBlocks;
import com.ordana.verdant.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class WeatheringHelper {

    public static void addOptional(ImmutableBiMap.Builder<Block, Block> map,
                                   String moddedId, String moddedId2) {
        var o1 = BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(moddedId));
        var o2 = BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(moddedId2));
        if (o1.isPresent() && o2.isPresent()) {
            map.put(o1.get(), o2.get());
        }
    }

    public static final Supplier<BiMap<Block, Block>> FLOWERY_BLOCKS = Suppliers.memoize(() -> {
        var builder = ImmutableBiMap.<Block, Block>builder()
                .put(Blocks.FLOWERING_AZALEA, Blocks.AZALEA)
                .put(Blocks.FLOWERING_AZALEA_LEAVES, Blocks.AZALEA_LEAVES)
                .put(ModBlocks.LEAF_PILES.get(LeavesTypeRegistry.getValue(new ResourceLocation("flowering_azalea"))),
                        ModBlocks.LEAF_PILES.get(LeavesTypeRegistry.getValue(new ResourceLocation("azalea"))));
        addOptional(builder, "quark:flowering_azalea_hedge", "quark:azalea_hedge");
        addOptional(builder, "quark:flowering_azalea_leaf_carpet", "quark:azalea_leaf_carpet");
        return builder.build();
    });


    public static Optional<BlockState> getAzaleaGrowth(BlockState state) {
        return Optional.ofNullable(FLOWERY_BLOCKS.get().inverse().get(state.getBlock()))
                .map(block -> block.withPropertiesOf(state));
    }

    public static Optional<BlockState> getAzaleaSheared(BlockState state) {
        return Optional.ofNullable(FLOWERY_BLOCKS.get().get(state.getBlock()))
                .map(block -> block.withPropertiesOf(state));
    }


    public static final Supplier<Map<Block, LeafPileBlock>> LEAVES_TO_PILES = Suppliers.memoize(() -> {
                var b = ImmutableMap.<Block, LeafPileBlock>builder();
                ModBlocks.LEAF_PILES.forEach((key, value) -> b.put(key.leaves, value));
                return b.build();
            }
    );

    public static Optional<Block> getFallenLeafPile(BlockState state) {
        Block b = state.getBlock();
        if (CommonConfigs.LEAF_PILES_BLACKLIST.get().contains(BuiltInRegistries.BLOCK.getKey(b).toString()))
            return Optional.empty();
        return Optional.ofNullable(LEAVES_TO_PILES.get().get(b));
    }

    @Nullable
    public static Item getBarkToStrip(BlockState normalLog) {
        WoodType woodType = BlockSetAPI.getBlockTypeOf(normalLog.getBlock(), WoodType.class);
        if (woodType != null) {
            boolean log = false;

            String childKey = woodType.getChildKey(normalLog.getBlock());
            if (("log".equals(childKey) && woodType.getChild("stripped_log") != null)
                || ("wood".equals(childKey)  && woodType.getChild("stripped_wood") != null)) {
                log = true;
            }
            if (log) {
                String s = CommonConfigs.GENERIC_BARK.get();
                if (!s.isEmpty()) {
                    var bark = BuiltInRegistries.ITEM.getOptional(new ResourceLocation(s));
                    if (bark.isPresent()) {
                        return bark.get();
                    }
                }
                return woodType.getItemOfThis("verdant:bark");
            }
        }
        return null;
    }

    public static Optional<Pair<Item, Block>> getBarkForStrippedLog(BlockState stripped) {
        WoodType woodType = BlockSetAPI.getBlockTypeOf(stripped.getBlock(), WoodType.class);
        if (woodType != null) {
            Object log = null;
            if (woodType.getChild("stripped_log") == stripped.getBlock()) {
                log = woodType.getChild("log");
            } else if (woodType.getChild("stripped_wood") == stripped.getBlock()) {
                log = woodType.getChild("wood");
            }
            if (log instanceof Block unStripped) {
                String s = CommonConfigs.GENERIC_BARK.get();
                if (!s.isEmpty()) {
                    var bark = BuiltInRegistries.ITEM.getOptional(new ResourceLocation(s));
                    if (bark.isPresent()) {
                        return Optional.of(Pair.of(bark.get(), unStripped));
                    }
                } else {
                    Item bark = woodType.getItemOfThis("verdant:bark");
                    if (bark != null) return Optional.of(Pair.of(bark, unStripped));
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<Pair<Item, Block>> getWoodFromLog(BlockState sourceLog) {
        WoodType woodType = BlockSetAPI.getBlockTypeOf(sourceLog.getBlock(), WoodType.class);
        if (woodType != null) {
            Object log = null;
            if (woodType.getChild("log") == sourceLog.getBlock()) {
                log = woodType.getChild("wood");
            }
            if (log instanceof Block unStripped) {
                String s = CommonConfigs.GENERIC_BARK.get();
                if (!s.isEmpty()) {
                    var bark = BuiltInRegistries.ITEM.getOptional(new ResourceLocation(s));
                    if (bark.isPresent()) {
                        return Optional.of(Pair.of(bark.get(), unStripped));
                    }
                } else {
                    Item bark = woodType.getItemOfThis("verdant:bark");
                    if (bark != null) return Optional.of(Pair.of(bark, unStripped));
                }
            }
        }
        return Optional.empty();
    }

    public static final Supplier<BiMap<Block, Block>> RAW_TO_STRIPPED = Suppliers.memoize(() -> {
        var builder = ImmutableBiMap.<Block, Block>builder()
            .put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG)
            .put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG)
            .put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG)
            .put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG)
            .put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
            .put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG)
            .put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG)
            .put(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG)
            .put(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK)
            .put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM)
            .put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM)
            .put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD)
            .put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD)
            .put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
            .put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD)
            .put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD)
            .put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
            .put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD)
            .put(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD)
            .put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE)
            .put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE);
        return builder.build();
    });

    public static final Supplier<BiMap<Block, Block>> STRIPPED_TO_RAW = Suppliers.memoize(() -> RAW_TO_STRIPPED.get().inverse());

    public static final Supplier<BiMap<Block, Item>> WOOD_TO_BARK = Suppliers.memoize(() -> {
        var builder = ImmutableBiMap.<Block, Item>builder()
            .put(Blocks.OAK_LOG,       ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("oak"))))
            .put(Blocks.BIRCH_LOG,     ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("birch"))))
            .put(Blocks.JUNGLE_LOG,    ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("jungle"))))
            .put(Blocks.SPRUCE_LOG,    ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("spruce"))))
            .put(Blocks.ACACIA_LOG,    ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("acacia"))))
            .put(Blocks.DARK_OAK_LOG,  ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("dark_oak"))))
            .put(Blocks.MANGROVE_LOG,  ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("mangrove"))))
            .put(Blocks.CHERRY_LOG,    ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("cherry"))))
            .put(Blocks.BAMBOO_BLOCK,  ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("bamboo"))))
            .put(Blocks.CRIMSON_STEM,  ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("crimson"))))
            .put(Blocks.WARPED_STEM,   ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("warped"))))
            .put(Blocks.OAK_WOOD,      ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("oak"))))
            .put(Blocks.BIRCH_WOOD,    ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("birch"))))
            .put(Blocks.JUNGLE_WOOD,   ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("jungle"))))
            .put(Blocks.SPRUCE_WOOD,   ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("spruce"))))
            .put(Blocks.ACACIA_WOOD,   ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("acacia"))))
            .put(Blocks.DARK_OAK_WOOD, ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("dark_oak"))))
            .put(Blocks.MANGROVE_WOOD, ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("mangrove"))))
            .put(Blocks.CHERRY_WOOD,   ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("cherry"))))
            .put(Blocks.CRIMSON_HYPHAE,ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("crimson"))))
            .put(Blocks.WARPED_HYPHAE, ModItems.BARK.get(WoodTypeRegistry.getValue(new ResourceLocation("warped"))));
        return builder.build();
    });

    public static Optional<Item> getBark(Block block) {
        return Optional.ofNullable(WOOD_TO_BARK.get().get(block));
    }

    public static boolean isLog(BlockState state) {
        return state.is(BlockTags.LOGS) && (!state.hasProperty(RotatedPillarBlock.AXIS) ||
                state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y) &&
                !BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("stripped");
    }

    public static void growHangingRoots(ServerLevel level, RandomSource random, BlockPos pos) {
        Direction dir = Direction.values()[1 + random.nextInt(5)].getOpposite();
        BlockPos targetPos = pos.relative(dir);
        BlockState targetState = level.getBlockState(targetPos);
        FluidState fluidState = level.getFluidState(targetPos);
        boolean bl = fluidState.is(Fluids.WATER);
        if (targetState.canBeReplaced()) {
            BlockState newState = dir == Direction.DOWN ?
                Blocks.HANGING_ROOTS.defaultBlockState() :
                ModBlocks.HANGING_ROOTS_WALL.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
            level.setBlockAndUpdate(targetPos, newState.setValue(BlockStateProperties.WATERLOGGED, bl));
        }
    }

}
