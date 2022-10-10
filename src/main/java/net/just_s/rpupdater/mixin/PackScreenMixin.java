package net.just_s.rpupdater.mixin;

import net.just_s.rpupdater.RPUpdMod;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(PackScreen.class)
public class PackScreenMixin {
    @Inject(method = "updatePackList", at = @At("HEAD"), cancellable = true)
    private void injectUpdatePackList(PackListWidget widget, Stream<ResourcePackOrganizer.Pack> packs, CallbackInfo ci) {
        widget.children().clear();
        List<ResourcePackOrganizer.Pack> rpupdPacks = new ArrayList<>();
        List<ResourcePackOrganizer.Pack> simplePacks = new ArrayList<>();
        packs.forEach((pack) -> {
            if (pack.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD))
                rpupdPacks.add(pack);
            else
                simplePacks.add(pack);
        });
        for (ResourcePackOrganizer.Pack pack : rpupdPacks)
            widget.children().add(new PackListWidget.ResourcePackEntry(RPUpdMod.MC, widget, (PackScreen)(Object)this, pack));
        for (ResourcePackOrganizer.Pack pack : simplePacks)
            widget.children().add(new PackListWidget.ResourcePackEntry(RPUpdMod.MC, widget, (PackScreen)(Object)this, pack));
        ci.cancel();
    }
}
