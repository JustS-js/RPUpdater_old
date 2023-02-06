package net.just_s.rpupdater.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.just_s.rpupdater.RPUpdMod;
import net.just_s.rpupdater.network.ModMessages;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.ClientGameSession.class)
public class ClientGameSession {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void inject(ClientWorld world, ClientPlayerEntity player, ClientPlayNetworkHandler networkHandler, CallbackInfo ci) {
        if (!networkHandler.getConnection().isLocal()) {
            RPUpdMod.LOGGER.info("Sent checkC2Spacket");
            ClientPlayNetworking.send(ModMessages.CHECK_FOR_UPD_C2S, PacketByteBufs.create());
        }
    }
}
