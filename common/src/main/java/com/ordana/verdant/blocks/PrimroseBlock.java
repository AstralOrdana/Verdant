package com.ordana.verdant.blocks;

import com.ordana.verdant.reg.ModBlocks;
import com.ordana.verdant.util.WeatheringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Objects;
import java.util.Optional;

public class PrimroseBlock extends Block implements BonemealableBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private final DyeColor color;

    public PrimroseBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(BlockTags.DIRT);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        Optional<Block> flowerState = Optional.of(this);
        BlockPos flowerPos = pos.relative(Direction.Plane.HORIZONTAL.getRandomDirection(random));
        for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
            if (level.getBlockState(pos.relative(dir, 2)).getBlock() instanceof PrimroseBlock mate) {
                flowerState = WeatheringHelper.getPrimroseColor(getOffspringColor(level, mate));
                flowerPos = pos.relative(dir);
            }
        }
        level.setBlockAndUpdate(flowerPos, flowerState.get().defaultBlockState());
    }

    public final DyeColor getColor() {
        return color;
    }

    private DyeColor getOffspringColor(ServerLevel level, PrimroseBlock mate) {
        DyeColor dyeColor = color;
        DyeColor dyeColor2 = mate.getColor();
        CraftingContainer craftingContainer = makeContainer(dyeColor, dyeColor2);
        Optional<Item> var10000 = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingContainer, level).map((craftingRecipe) ->
                craftingRecipe.assemble(craftingContainer, level.registryAccess())).map(ItemStack::getItem);
        Objects.requireNonNull(DyeItem.class);
        var10000 = var10000.filter(DyeItem.class::isInstance);
        Objects.requireNonNull(DyeItem.class);
        return var10000.map(DyeItem.class::cast).map(DyeItem::getDyeColor).orElseGet(() ->
                level.random.nextBoolean() ? dyeColor : dyeColor2);
    }

    private static CraftingContainer makeContainer(DyeColor fatherColor, DyeColor motherColor) {
        CraftingContainer craftingContainer = new TransientCraftingContainer(new AbstractContainerMenu(null, -1) {
            public ItemStack quickMoveStack(Player player, int index) {
                return ItemStack.EMPTY;
            }

            public boolean stillValid(Player player) {
                return false;
            }
        }, 2, 1);
        craftingContainer.setItem(0, new ItemStack(DyeItem.byColor(fatherColor)));
        craftingContainer.setItem(1, new ItemStack(DyeItem.byColor(motherColor)));
        return craftingContainer;
    }
}
