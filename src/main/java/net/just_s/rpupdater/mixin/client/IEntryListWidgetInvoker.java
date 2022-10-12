package net.just_s.rpupdater.mixin.client;

import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntryListWidget.class)
public interface IEntryListWidgetInvoker {
    @Invoker("getRowTop")
    public int invokeGetRowTop(int index);
}
