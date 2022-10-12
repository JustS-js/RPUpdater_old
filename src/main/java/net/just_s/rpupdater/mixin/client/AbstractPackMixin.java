package net.just_s.rpupdater.mixin.client;

import it.unimi.dsi.fastutil.ints.Int2BooleanOpenCustomHashMap;
import net.just_s.rpupdater.RPUpdMod;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "net/minecraft/client/gui/screen/pack/ResourcePackOrganizer$AbstractPack")
public class AbstractPackMixin {
    @Final
    @Shadow
    private ResourcePackProfile profile;

    @Inject(method = "canMoveTowardStart", at = @At("HEAD"), cancellable = true)
    private void injectMoveUp(CallbackInfoReturnable<Boolean> cir) {
        List<ResourcePackProfile> list = this.getCurrentList();
        int i = list.indexOf(this.profile);
        if (profile.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD))
            cir.setReturnValue(
                    i > 0 && !((ResourcePackProfile)list.get(i - 1)).isPinned() &&
                            ((ResourcePackProfile)list.get(i - 1)).getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD)
            );
        else
            cir.setReturnValue(
                    i > 0 && !((ResourcePackProfile)list.get(i - 1)).isPinned() &&
                            !((ResourcePackProfile)list.get(i - 1)).getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD)
            );
    }

    @Inject(method = "canMoveTowardEnd", at = @At("HEAD"), cancellable = true)
    private void injectMoveDown(CallbackInfoReturnable<Boolean> cir) {
        List<ResourcePackProfile> list = this.getCurrentList();
        int i = list.indexOf(this.profile);
        if (profile.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD))
            cir.setReturnValue(
                    i >= 0 && i < list.size() - 1 && !((ResourcePackProfile)list.get(i + 1)).isPinned() &&
                            ((ResourcePackProfile)list.get(i + 1)).getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD)
            );
        else
            cir.setReturnValue(
                    i >= 0 && i < list.size() - 1 && !((ResourcePackProfile)list.get(i + 1)).isPinned() &&
                            !((ResourcePackProfile)list.get(i + 1)).getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD)
            );
    }

    @Shadow
    protected List<ResourcePackProfile> getCurrentList() { return null; }
}
