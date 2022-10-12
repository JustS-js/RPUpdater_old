package net.just_s.rpupdater;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.just_s.rpupdater.network.ModMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RPUpdServer implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(RPUpdMod.MODID+"-server");
    public static File RPUPD_DIR;
    @Override
    public void onInitializeServer() {
        RPUPD_DIR = getOrCreateDir();
        ModMessages.registerC2SPackets();
    }

    private static File getOrCreateDir() {
        File dir = new File(FabricLoader.getInstance().getGameDir().toFile(), "RPUpdDir");
        if (!dir.exists())
            if (!dir.mkdir())
                LOGGER.error("Could not create directory for RPUpdater");
        return dir;
    }
}
