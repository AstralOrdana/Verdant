package com.ordana.verdant.entities;

import com.ordana.verdant.blocks.LayerBlock;
import com.ordana.verdant.blocks.LeafPileBlock;
import com.ordana.verdant.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FallingLayerEntity extends FallingBlockEntity {


    public FallingLayerEntity(EntityType<FallingLayerEntity> type, Level level) {
        super(type, level);
    }

    public FallingLayerEntity(Level level) {
        this(ModEntities.FALLING_LAYER.get(), level);
    }

    public FallingLayerEntity(Level level, BlockPos pos,
                              BlockState blockState) {
        super(ModEntities.FALLING_LAYER.get(), level);
        this.blocksBuilding = true;
        this.xo = pos.getX() + 0.5D;
        this.yo = pos.getY();
        this.zo = pos.getZ() + 0.5D;
        this.setPos(xo, yo + (double) ((1.0F - this.getBbHeight()) / 2.0F), zo);
        this.setDeltaMovement(Vec3.ZERO);
        this.setStartPos(this.blockPosition());
        this.setBlockState(blockState);
    }

    public void setBlockState(BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            state = state.setValue(BlockStateProperties.WATERLOGGED, false);
        }
        CompoundTag tag = new CompoundTag();
        tag.put("BlockState", NbtUtils.writeBlockState(state));
        tag.putInt("Time", this.time);
        this.readAdditionalSaveData(tag);
    }

    public static FallingLayerEntity fall(Level level, BlockPos pos, BlockState state) {
        FallingLayerEntity entity = new FallingLayerEntity(level, pos, state);
        level.setBlock(pos, state.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(entity);
        return entity;
    }

    @Nullable
    @Override
    public ItemEntity spawnAtLocation(ItemLike pItem) {
        this.dropItemAndBreak(this.getBlockState(), this.blockPosition());
        return null;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        var level = this.level();
        if (level.isClientSide) {
            super.tick();
            return;
        }
        BlockState blockState = this.getBlockState();
        if (blockState.isAir() || !(blockState.getBlock() instanceof LayerBlock block)) {
            this.discard();
        } else {
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
            BlockPos pos = this.blockPosition();
            if (level.getFluidState(pos).is(FluidTags.WATER)) {
                //shitty case for leaf piles
                BlockState onState = level.getBlockState(pos);
                if(block instanceof LeafPileBlock && onState.is(Blocks.WATER) && block.getLayers(blockState)==1){
                    blockState = blockState.setValue(block.layerProperty(),0);
                }
                //TODO: fix placing on water. this is a mess
             //   //TODO: fix underwater
                //                        discardAndDrop(blockState,pos);
             //   return;
            }
            if (this.getDeltaMovement().lengthSqr() > 1.0D) {
                BlockHitResult blockhitresult = level.clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
                if (blockhitresult.getType() != HitResult.Type.MISS && level.getFluidState(blockhitresult.getBlockPos()).is(FluidTags.WATER)) {
                    discardAndDrop(blockState,pos);
                    //what the hell is this for?
                    return;
                }
            }


            //fall
            if (!this.onGround()) {
                if (this.time > 100 && (pos.getY() <= level.getMinBuildHeight() || pos.getY() > level.getMaxBuildHeight()) || this.time > 600) {
                    discardAndDrop(blockState,pos);
                    return;
                }
                //place
            } else {
                BlockState onState = level.getBlockState(pos);
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
                if (!onState.is(Blocks.MOVING_PISTON)) {

                    boolean canBeReplaced = onState.canBeReplaced(new DirectionalPlaceContext(level, pos, Direction.DOWN,
                            new ItemStack(blockState.getBlock().asItem()), Direction.UP));
                    boolean isFree = block.shouldFall(blockState, level.getBlockState(pos.below()));
                    boolean canSurvive = blockState.canSurvive(level, pos) && !isFree;
                    if (canBeReplaced && canSurvive) {

                        int remaining = 0;

                        if (onState.is(blockState.getBlock())) {

                            IntegerProperty layers_property = block.layerProperty();

                            int layers = blockState.getValue(layers_property);
                            int toLayers = onState.getValue(layers_property);
                            int total = layers + toLayers;
                            int target = Mth.clamp(total, 1, block.getMaxLayers());
                            remaining = total - target;
                            blockState = blockState.setValue(layers_property, target);
                        }

                        if (level.setBlock(pos, blockState, 3)) {
                            ((ServerLevel) level).getChunkSource().chunkMap.broadcast(this,
                                    new ClientboundBlockUpdatePacket(pos, level.getBlockState(pos)));

                            block.onLand(level, pos, blockState, onState, this);

                            this.discard();

                            if (remaining != 0) {
                                BlockPos above = pos.above();
                                blockState = blockState.setValue(block.layerProperty(), remaining);
                                if (level.getBlockState(above).canBeReplaced()) {
                                    if (!level.setBlock(above, blockState, 3)) {
                                        ((ServerLevel) level).getChunkSource().chunkMap.broadcast(this,
                                                new ClientboundBlockUpdatePacket(above, level.getBlockState(above)));
                                        this.dropItemAndBreak(blockState, pos);
                                    }
                                }
                            }


                            return;
                        }
                    }
                    discardAndDrop(blockState, pos);
                }
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        }
    }

    //TODO: merge these two
    private void discardAndDrop(BlockState state, BlockPos pos) {
        if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.callOnBrokenAfterFall(state.getBlock(), pos);
            this.dropItemAndBreak(state, pos);
        }

        this.discard();
    }

    private void dropItemAndBreak(BlockState state, BlockPos pos) {
        Block.dropResources(state, this.level(), pos, null, null, ItemStack.EMPTY);

        this.level().levelEvent(null, 2001, pos, Block.getId(state));
    }
}
