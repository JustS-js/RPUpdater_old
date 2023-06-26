package net.just_s.rpupdater;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.just_s.rpupdater.network.ModMessages;
import net.just_s.rpupdater.util.ModCommands;
import net.just_s.rpupdater.util.RPObject;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ZipResourcePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
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
        ModCommands.registerCommands();
    }

    private static File getOrCreateDir() throws FileNotFoundException {
        File dir = new File(FabricLoader.getInstance().getGameDir().toFile(), "RPUpdDir");
        if (!dir.exists())
            if (!dir.mkdir()) {
                throw new FileNotFoundException("Could not create directory for RPUpdater");
            }
        return dir;
    }

    public static RPObject getPackByName(String name) {
        for (RPObject rpObject : registeredPacks) {
            if (rpObject.getName().equals(name)) {
                return rpObject;
            }
        }
        return null;
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
            changePackTimeMetadata(pack, time);
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
        if (pack.isDirectory()) {
            try {
                Path source = Path.of(pack.getPath());
                if (getMeta(source) != -1) {
                    return;
                }
                Path temp = Path.of(pack.getPath());
                if (Files.exists(temp)) {
                    throw new IOException("temp file exists, generate another name");
                }
                Files.move(source, temp);
                streamCopy(temp, source, time);
                Files.delete(temp);
            } catch (Exception e) {LOGGER.warn(e + " " +  pack.toPath());}
        } else {
            try (FileSystem fs = FileSystems.newFileSystem(pack.toPath())) {
                Path source = fs.getPath("/pack.mcmeta");
                if (getMeta(source) != -1) {
                    return;
                }
                Path temp = fs.getPath("/___pack___.mcmeta");
                if (Files.exists(temp)) {
                    throw new IOException("temp file exists, generate another name");
                }
                Files.move(source, temp);
                streamCopy(temp, source, time);
                Files.delete(temp);
            } catch (Exception e) {LOGGER.warn(e + " " +  pack.toPath());}
        }
    }

    public static long getPackFileTime(File pack) {
        try {
            long time = getMeta(pack.toPath());
            if (time != -1) return time;
            FileTime timestamp = Files.readAttributes(pack.toPath(), BasicFileAttributes.class).lastModifiedTime();
            return timestamp.toMillis();
        } catch (IOException e) {LOGGER.error("IOException while counting pack's time: " + e.getMessage());}
        return -1;
    }

    static long getMeta(Path src) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Files.newInputStream(Path.of(src.toString() + "/pack.mcmeta"))))) {

            StringBuilder textBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                textBuilder.append(line);
            }

            JsonObject jsonMeta = new JsonParser().parse(textBuilder.toString()).getAsJsonObject();
            if (!jsonMeta.has("metatime")) {
                return -1;
            }
            return jsonMeta.getAsJsonPrimitive("metatime").getAsLong();
        }
    }

    static void streamCopy(Path src, Path dst, long time) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Files.newInputStream(src)));
             BufferedWriter bw = new BufferedWriter(
                     new OutputStreamWriter(Files.newOutputStream(dst)))) {

            StringBuilder textBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                textBuilder.append(line);
            }

            JsonObject jsonMeta = new JsonParser().parse(textBuilder.toString()).getAsJsonObject();

            jsonMeta.addProperty("metatime", time);
            String text = jsonMeta.toString();
            bw.write(text);
            bw.newLine();
        }
    }
}
