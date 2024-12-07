package com.ordana.verdant.reg;

import com.ordana.verdant.Verdant;
import com.ordana.verdant.blocks.*;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesType;
import net.mehvahdjukaar.moonlight.api.set.leaves.LeavesTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks {

    public static void init() {
        BlockSetAPI.addDynamicBlockRegistration(ModBlocks::registerLeafPiles, LeavesType.class);
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    public static <T extends Block> Supplier<T> regBlock(String name, Supplier<T> block) {
        return RegHelper.registerBlock(Verdant.res(name), block);
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> blockFactory) {
        Supplier<T> block = regBlock(name, blockFactory);
        regBlockItem(name, block, new Item.Properties());
        return block;
    }

    public static void regBlockItem(String name, Supplier<? extends Block> blockSup, Item.Properties properties) {
        RegHelper.registerItem(Verdant.res(name), () -> new BlockItem(blockSup.get(), properties));
    }

    //predicates
    private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> CAN_SPAWN_ON_LEAVES = (a, b, c, t) ->
            t == EntityType.OCELOT || t == EntityType.PARROT;

    private static final BlockBehaviour.StatePredicate NEVER = (s, w, p) -> false;


    public static final BlockBehaviour.Properties LEAF_PILE_PROPERTIES = BlockBehaviour.Properties.of()
            .randomTicks().instabreak().sound(SoundType.GRASS)
            .noOcclusion().isValidSpawn(CAN_SPAWN_ON_LEAVES)
            .isSuffocating(NEVER).isViewBlocking(NEVER);


    public static final Map<LeavesType, LeafPileBlock> LEAF_PILES = new LinkedHashMap<>();

    public static final Supplier<LeafPileBlock> AZALEA_FLOWER_PILE = regBlock("azalea_flower_pile", () ->
            new LeafPileBlock(LEAF_PILE_PROPERTIES.sound(SoundType.AZALEA), LeavesTypeRegistry.OAK_TYPE));


    //vegetation

    public static final Supplier<Block> MOSS = regBlock("moss", () ->
            new MossMultifaceBlock(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).randomTicks().instabreak().sound(SoundType.MOSS_CARPET).noOcclusion().noCollission()));
    public static final Supplier<Block> WEEDS = regWithItem("weeds", () ->
            new WeedsBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noCollission().instabreak().sound(SoundType.GRASS)));
    public static final Supplier<Block> DUNE_GRASS = regWithItem("dune_grass", () ->
            new DuneGrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS).noCollission().instabreak().sound(SoundType.GRASS)));
    public static final Supplier<Block> BARLEY = regWithItem("barley", () ->
            new ModGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> CATTAIL = regWithItem("cattail", () ->
            new CattailBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).replaceable().noCollission().instabreak().sound(SoundType.WET_GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> DENSE_GRASS = regWithItem("dense_grass", () ->
            new ModGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().strength(3.0f).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> BOXWOOD = regWithItem("boxwood", () ->
            new ModGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().strength(3.0f).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> CLOVER = regWithItem("clover", () ->
            new CloverBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));


    public static final Supplier<Block> MUSCARI = regWithItem("muscari", () ->
            new MuscariBlock(MobEffects.POISON, 12, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> TALL_MUSCARI = regBlock("tall_muscari", () ->
            new TallMuscariBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> RED_PRIMROSE = regWithItem("red_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.RED).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> ORANGE_PRIMROSE = regWithItem("orange_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> YELLOW_PRIMROSE = regWithItem("yellow_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> LIME_PRIMROSE = regWithItem("lime_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIME).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> GREEN_PRIMROSE = regWithItem("green_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> CYAN_PRIMROSE = regWithItem("cyan_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.CYAN).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> BLUE_PRIMROSE = regWithItem("blue_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BLUE).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> LIGHT_BLUE_PRIMROSE = regWithItem("light_blue_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_BLUE).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> PURPLE_PRIMROSE = regWithItem("purple_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.PURPLE).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> MAGENTA_PRIMROSE = regWithItem("magenta_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.MAGENTA).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> PINK_PRIMROSE = regWithItem("pink_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.PINK).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> WHITE_PRIMROSE = regWithItem("white_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> BLACK_PRIMROSE = regWithItem("black_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> GRAY_PRIMROSE = regWithItem("gray_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> LIGHT_GRAY_PRIMROSE = regWithItem("light_gray_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> BROWN_PRIMROSE = regWithItem("brown_primrose", () ->
            new PrimroseBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BROWN).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));


    public static final Supplier<Block> DUCKWEED = regBlock("duckweed", () ->
            new DuckweedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> HANGING_ROOTS_WALL = regBlock("hanging_roots_wall", () ->
            new WallRootsBlock(BlockBehaviour.Properties.copy(Blocks.HANGING_ROOTS)));
    public static final Supplier<IvyBlock> IVY = regWithItem("ivy", () ->
            new IvyBlock(BlockBehaviour.Properties.copy(Blocks.VINE).noCollission().strength(0.2f)
                    .sound(SoundType.AZALEA_LEAVES)));

    private static void registerLeafPiles(Registrator<Block> event, Collection<LeavesType> leavesTypes) {
        for (LeavesType type : leavesTypes) {
            String name = type.getVariantId("leaf_pile", false);

            LeafPileBlock block = new LeafPileBlock(LEAF_PILE_PROPERTIES, type);
            event.register(Verdant.res(name), block);

            LEAF_PILES.put(type, block);
            type.addChild("verdant:leaf_pile", block);
        }
    }
}