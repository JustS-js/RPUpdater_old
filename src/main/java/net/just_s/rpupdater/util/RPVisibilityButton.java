package net.just_s.rpupdater.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.just_s.rpupdater.RPUpdMod;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RPVisibilityButton extends TexturedButtonWidget {
    private final int this_x;
    private final int this_y;
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
        int v;
        if (this.hovered) {
            v = 20;
        } else {
            v = 0;
        }
        drawTexture(matrices, this_x, this_y, 0, v, 20, 20, 40, 40);
        if (RPUpdMod.CONFIG.shouldRenderInMenu())
            v = 0;
        else
            v = 10;
        drawTexture(matrices, this_x + 2, this_y + 5, 20, v, 18, 10, 40, 40);
    }
}
