package net.just_s.rpupdater.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.just_s.rpupdater.RPUpdMod;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RPVisibilityButton extends TexturedButtonWidget {
    private int this_x;
    private int this_y;
    public RPVisibilityButton(int x, int y, PressAction pressAction) {
        super(x, y, 20, 20, 0, 0, ModTextures.VISIBILITY_BTN, pressAction);
        this_x = x;
        this_y = y;
    }

    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Cascade the rendering
        super.renderButton(matrices, mouseX, mouseY, delta);

        // Render the current session status
        RenderSystem.setShaderTexture(0, ModTextures.VISIBILITY_BTN);
        final int v;
        if (RPUpdMod.CONFIG.shouldRenderInMenu()) {
            v = 0;
        } else
            v = 20;
        drawTexture(matrices, this_x, this_y, 0, v, 20, 20, 20, 40);
    }
}
