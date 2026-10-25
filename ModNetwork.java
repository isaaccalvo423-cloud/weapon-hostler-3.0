package com.gunholsters;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String PROTO = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(GunHolsters.MODID, "main"), () -> PROTO, PROTO::equals, PROTO::equals);

    public static void register() {
        CHANNEL.registerMessage(0, DrawRequestPacket.class, DrawRequestPacket::encode, DrawRequestPacket::decode, DrawRequestPacket::handle);
    }
}
