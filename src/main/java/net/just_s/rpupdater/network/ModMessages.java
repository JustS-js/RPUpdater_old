package net.just_s.rpupdater.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.just_s.rpupdater.RPUpdMod;
import net.just_s.rpupdater.network.packet.C2S.CheckC2SPacket;
import net.just_s.rpupdater.network.packet.S2C.CheckS2CPacket;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static Identifier CHECK_FOR_UPD_C2S = new Identifier(RPUpdMod.MODID, "check_for_upd_c2s");
    public static Identifier CHECK_FOR_UPD_S2C = new Identifier(RPUpdMod.MODID, "check_for_upd_s2c");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CHECK_FOR_UPD_S2C, CheckS2CPacket::receive);
    }

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(CHECK_FOR_UPD_C2S, CheckC2SPacket::receive);
    }
}
