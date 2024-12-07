package com.ordana.verdant.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SendCustomParticlesPacket implements Message {

    private final EventType type;
    private final int extraData;
    private final BlockPos pos;

    public SendCustomParticlesPacket(FriendlyByteBuf buffer) {
        this.extraData = buffer.readInt();
        this.type = EventType.values()[buffer.readByte()];
        this.pos = buffer.readBlockPos();
    }

    public SendCustomParticlesPacket(EventType type, BlockPos pos, int extraData) {
        this.type = type;
        this.pos = pos;
        this.extraData = extraData;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.extraData);
        buf.writeByte(type.ordinal());
        buf.writeBlockPos(pos);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        clientStuff(type, pos, extraData);
    }

    @Environment(EnvType.CLIENT)
    public void clientStuff( EventType type, BlockPos pos, int extraData) {
        Player player = Minecraft.getInstance().player;
        var level = player.level();
        if (type == EventType.DECAY_LEAVES) {
            BlockState state = Block.stateById(extraData);
            var leafParticle = new BlockParticleOption(ParticleTypes.BLOCK, state);
            int color = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);

            //add more than one?
            for (int i = 0; i < 20; i++) {
                double d = pos.getX() + level.random.nextDouble();
                double e = pos.getY() - 0.05;
                double f = pos.getZ() + level.random.nextDouble();
                level.addParticle(leafParticle, d, e, f, 0.0, color, 0.0);
            }

            level.playSound(player, pos, SoundEvents.AZALEA_LEAVES_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    public enum EventType {
        DECAY_LEAVES
    }
}