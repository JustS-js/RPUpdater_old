package net.just_s.rpupdater.util;

import net.just_s.rpupdater.RPUpdMod;

public class Config {
    public int RP_COLOR = RPUpdMod.RGBToNegativeDecimal(53, 217, 255);
    private boolean DO_SHOW_RP = true;

    public void hideRP() {
        DO_SHOW_RP = false;
    }

    public void showRP() {
        DO_SHOW_RP = true;
    }

    public boolean shouldRenderInMenu() {
        return DO_SHOW_RP;
    }
}
