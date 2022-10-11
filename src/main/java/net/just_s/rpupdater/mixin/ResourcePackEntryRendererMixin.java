package net.just_s.rpupdater.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.just_s.rpupdater.RPUpdMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourcePackCompatibility;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PackListWidget.ResourcePackEntry.class)
public class ResourcePackEntryRendererMixin {
    @Shadow @Final private ResourcePackOrganizer.Pack pack;
    @Shadow @Final
    private PackListWidget widget;
    @Shadow @Final
    protected Screen screen;
    @Shadow @Final
    protected MinecraftClient client;


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V"),
    slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/pack/ResourcePackOrganizer$Pack;getCompatibility()Lnet/minecraft/resource/ResourcePackCompatibility;"),
            to = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V")
    ))
    private void injectRender(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (pack.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD)) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.2F);
            DrawableHelper.fill(matrices, x - 1, y - 1, x + entryWidth - 9, y + entryHeight + 1, RPUpdMod.CONFIG.RP_COLOR);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void injectMouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        double d = mouseX - (double)this.widget.getRowLeft();
        double e = mouseY - (double)((IEntryListWidgetInvoker)this.widget).invokeGetRowTop(this.widget.children().indexOf(this));
        if (this.isSelectable() && d <= 32.0D) {
            if (this.pack.canBeEnabled()) {
                ResourcePackCompatibility resourcePackCompatibility = this.pack.getCompatibility();
                if (resourcePackCompatibility.isCompatible()) {
                    pack.enable();
                } else {
                    Text text = resourcePackCompatibility.getConfirmMessage();
                    this.client.setScreen(new ConfirmScreen((confirmed) -> {
                        this.client.setScreen(this.screen);
                        if (confirmed) {
                            this.pack.enable();
                        }

                    }, IPackListWidgetAccessor.getIncConText(), text));
                }

                cir.setReturnValue(true);
                return;
            }

            if (d < 16.0D && this.pack.canBeDisabled()) {
                this.pack.disable();
                cir.setReturnValue(true);
                return;
            }

            if (d > 16.0D && e < 16.0D && this.pack.canMoveTowardStart()) {
                if (this.pack.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD)) {
                    cir.setReturnValue(false);
                    return;
                }
                this.pack.moveTowardStart();
                cir.setReturnValue(true);
                return;
            }

            if (d > 16.0D && e > 16.0D && this.pack.canMoveTowardEnd()) {
                if (this.pack.getSource().equals(RPUpdMod.PACK_SOURCE_RPUPD)) {
                    cir.setReturnValue(false);
                    return;
                }
                this.pack.moveTowardEnd();
                cir.setReturnValue(true);
                return;
            }
        }

        cir.setReturnValue(false);
    }

    @Shadow @Final
    private boolean isSelectable() {
        return true;
    }
}
