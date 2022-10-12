package net.just_s.rpupdater.network.packet.C2S;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.just_s.rpupdater.RPUpdServer;
import net.just_s.rpupdater.network.ModMessages;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class CheckC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Server code
        RPUpdServer.LOGGER.info("Processed checkC2Spacket");
        // do something
        responseSender.sendPacket(ModMessages.CHECK_FOR_UPD_S2C, PacketByteBufs.create());
    }
}
