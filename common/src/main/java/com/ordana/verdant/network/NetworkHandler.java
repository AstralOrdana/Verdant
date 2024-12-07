package com.ordana.verdant.network;

import com.ordana.verdant.Verdant;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.NetworkDir;

public class NetworkHandler {

    public static final ChannelHandler CHANNEL = ChannelHandler.builder(Verdant.MOD_ID)
            .register(NetworkDir.PLAY_TO_CLIENT, SendCustomParticlesPacket.class, SendCustomParticlesPacket::new)
            .build();


    public static void init() {
    }


}