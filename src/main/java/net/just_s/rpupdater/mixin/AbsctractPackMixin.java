package net.just_s.rpupdater.mixin;

import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourcePackOrganizer.Pack.class)
public class AbsctractPackMixin {
    private void test(CallbackInfoReturnable<Boolean> cir) {

    }
}
