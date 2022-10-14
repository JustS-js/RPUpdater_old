package net.just_s.rpupdater.network.packet.C2S;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.just_s.rpupdater.RPUpdServer;
import net.just_s.rpupdater.network.ModMessages;
import net.just_s.rpupdater.util.RPObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Set;

public class CheckC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Server code
        RPUpdServer.LOGGER.info("Processed checkC2Spacket");
        // do something
        responseSender.sendPacket(
                ModMessages.CHECK_FOR_UPD_S2C,
                checkAllPacks()
        );
    }

    private static PacketByteBuf checkAllPacks() {
        PacketByteBuf buf = PacketByteBufs.create();
        Set<RPObject> registeredPacks = RPUpdServer.getRegisteredPacks();
        buf.writeByte(registeredPacks.size());
        for (RPObject rpObject : registeredPacks) {
            rpObject.write(buf);
        }
        return buf;
    }
}
