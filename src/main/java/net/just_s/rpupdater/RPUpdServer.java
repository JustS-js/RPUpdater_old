package net.just_s.rpupdater;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.just_s.rpupdater.network.ModMessages;
import net.just_s.rpupdater.util.RPObject;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ZipResourcePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.HashSet;
import java.util.Set;

public class RPUpdServer implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(RPUpdMod.MODID+"-server");
    public static File RPUPD_DIR;

    private static final Set<RPObject> registeredPacks = new HashSet<>();
    @Override
    public void onInitializeServer() {
        try {
            RPUPD_DIR = getOrCreateDir();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        registerPacks();
        ModMessages.registerC2SPackets();
    }

    private static File getOrCreateDir() throws FileNotFoundException {
        File dir = new File(FabricLoader.getInstance().getGameDir().toFile(), "RPUpdDir");
        if (!dir.exists())
            if (!dir.mkdir()) {
                throw new FileNotFoundException("Could not create directory for RPUpdater");
            }
        return dir;
    }

    private void registerPacks() {
        if (!RPUPD_DIR.exists()) return;
        FileFilter POSSIBLE_PACK = (file) -> {
            boolean bl = file.isFile() && file.getName().endsWith(".zip");
            boolean bl2 = file.isDirectory() && (new File(file, "pack.mcmeta")).isFile();
            return bl || bl2;
        };
        File[] packs = RPUPD_DIR.listFiles(POSSIBLE_PACK);
        if (packs == null) {
            LOGGER.warn("No ResourcePack found in /RPUpdDir");
            return;
        }

        for (File pack : packs) {
            RPObject rpObject;
            long time = getPackFileTime(pack);

            if (pack.isDirectory()) {
                rpObject = new RPObject(new DirectoryResourcePack(pack), time);
            } else {
                rpObject = new RPObject( new ZipResourcePack(pack), time);
            }
            registeredPacks.add(rpObject);
        }
    }

    public static ImmutableSet<RPObject> getRegisteredPacks() {
        return ImmutableSet.copyOf(registeredPacks);
    }

    public static void changePackTimeMetadata(File pack, long time) {

    }

    public static long getPackFileTime(File pack) {
        try {
            FileTime timestamp = Files.readAttributes(pack.toPath(), BasicFileAttributes.class).lastModifiedTime();
            return timestamp.toMillis();
        } catch (IOException e) {LOGGER.error("IOException while counting pack's time: " + e.getMessage());}
        return -1;
    }
}
