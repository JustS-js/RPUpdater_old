package net.just_s.rpupdater.network.packet.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.just_s.rpupdater.RPUpdServer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class CheckS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Client code
        RPUpdServer.LOGGER.info("Processed checkS2Cpacket");
        byte length = buf.readByte();
        for (int i = 0; i < length; i++) {
            String name = buf.readString();
            long timestamp = buf.readLong();
            RPUpdServer.LOGGER.info("Got pack: " + name + " with time " + timestamp);
        }
    }
}
