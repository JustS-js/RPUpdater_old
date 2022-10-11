package net.just_s.rpupdater.mixin;

import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PackListWidget.class)
public interface IPackListWidgetAccessor {
    @Accessor("INCOMPATIBLE_CONFIRM")
    public static Text getIncConText() {
        throw new AssertionError();
    }
}
