package net.just_s.rpupdater.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.just_s.rpupdater.RPUpdMod;
import net.just_s.rpupdater.network.ModMessages;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public class KeyboardTestMixin {
    @Inject(method = "processF3", at = @At("HEAD"))
    private void inject(int key, CallbackInfoReturnable<Boolean> cir) {
        RPUpdMod.LOGGER.info("Sent checkC2Spacket");
        ClientPlayNetworking.send(ModMessages.CHECK_FOR_UPD_C2S, PacketByteBufs.create());
    }
}
