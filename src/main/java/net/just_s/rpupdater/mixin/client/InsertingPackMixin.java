package net.just_s.rpupdater.mixin.client;

import net.just_s.rpupdater.RPUpdMod;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Function;


@Mixin(ResourcePackProfile.InsertionPosition.class)
public class InsertingPackMixin {
    @Inject(method = "insert", at = @At("HEAD"), cancellable = true)
    private <T> void injectInsert(List<T> items, T item, Function<T, ResourcePackProfile> profileGetter, boolean listInverted, CallbackInfoReturnable<Integer> cir) {
        ResourcePackProfile.InsertionPosition insertionPosition = listInverted ? this.inverse() : (ResourcePackProfile.InsertionPosition)(Object)this;
        int i;
        ResourcePackProfile resourcePackProfile;
        boolean bl = ((ResourcePackProfile)item).getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD);
        if (insertionPosition == ResourcePackProfile.InsertionPosition.BOTTOM) {
            for(i = 0; i < items.size(); ++i) {
                resourcePackProfile = (ResourcePackProfile)profileGetter.apply(items.get(i));
                if (!bl) {
                    if ((!resourcePackProfile.isPinned() && !resourcePackProfile.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD)) || resourcePackProfile.getInitialPosition() != (ResourcePackProfile.InsertionPosition)(Object)this) {
                        break;
                    }
                } else {
                    if (!resourcePackProfile.isPinned() || resourcePackProfile.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD) || resourcePackProfile.getInitialPosition() != (ResourcePackProfile.InsertionPosition)(Object)this) {
                        break;
                    }
                }
            }
            RPUpdMod.LOGGER.warn("BOTTOM: " + i);
            items.add(i, item);
            cir.setReturnValue(i);
        } else {
            for(i = items.size() - 1; i >= 0; --i) {
                resourcePackProfile = (ResourcePackProfile)profileGetter.apply(items.get(i));
                if (!bl) {
                    if ((!resourcePackProfile.isPinned() && !resourcePackProfile.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD)) || resourcePackProfile.getInitialPosition() != (ResourcePackProfile.InsertionPosition)(Object)this) {
                        break;
                    }
                } else {
                    if (!resourcePackProfile.isPinned() || resourcePackProfile.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD) || resourcePackProfile.getInitialPosition() != (ResourcePackProfile.InsertionPosition)(Object)this) {
                        break;
                    }
                }
            }
            RPUpdMod.LOGGER.warn("NOT BOTTOM: " + i + 1);
            items.add(i + 1, item);
            cir.setReturnValue(i + 1);
        }
    }
    @Shadow
    public ResourcePackProfile.InsertionPosition inverse() { return null; }
}
