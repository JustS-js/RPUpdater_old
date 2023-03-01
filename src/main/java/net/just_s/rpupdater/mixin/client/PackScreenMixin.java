package net.just_s.rpupdater.mixin.client;

import net.just_s.rpupdater.RPUpdMod;
import net.just_s.rpupdater.util.RPVisibilityButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Mixin(PackScreen.class)
public class PackScreenMixin extends Screen {

    protected PackScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    private void updatePackLists() {};

    @Inject(method = "init", at = @At("HEAD"))
    private void addButtons(CallbackInfo ci) {
        addDrawableChild(
                new RPVisibilityButton(this.width - 30, 10, (btn) -> {
                    if (RPUpdMod.CONFIG.shouldRenderInMenu()) {
                        RPUpdMod.CONFIG.hideRP();
                    } else {
                        RPUpdMod.CONFIG.showRP();
                    }
                    this.updatePackLists();
                })
        );
    }

    @Inject(method = "updatePackList", at = @At("HEAD"), cancellable = true)
    private void hideLists(PackListWidget widget, Stream<ResourcePackOrganizer.Pack> packs, CallbackInfo ci) {
        widget.children().clear();
        packs.forEach((pack) -> {
            if (pack.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD)) {
                if (RPUpdMod.CONFIG.shouldRenderInMenu()) {
                    widget.children().add(new PackListWidget.ResourcePackEntry(this.client, widget, this, pack));
                }
            } else {
                widget.children().add(new PackListWidget.ResourcePackEntry(this.client, widget, this, pack));
            }
        });
        ci.cancel();
    }
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
