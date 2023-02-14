package net.just_s.rpupdater;

import net.fabricmc.api.ClientModInitializer;
import net.just_s.rpupdater.network.ModMessages;
import net.just_s.rpupdater.util.Config;
import net.just_s.rpupdater.util.RPObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ZipResourcePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Optional;

public class RPUpdMod implements ClientModInitializer {
	public static final String MODID = "rpupd";
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID+"-client");
	public static final ResourcePackSource PACK_SOURCE_RPUPD = ResourcePackSource.nameAndSource("pack.source." + MODID);
	public static final Config CONFIG = new Config();

	@Override
	public void onInitializeClient() {
		ModMessages.registerS2CPackets();
	}

	public static int RGBToNegativeDecimal(int r, int g, int b) {
		int alpha = -16777216;
		alpha = alpha + r*256*256 + g*256 + b;
		return alpha;
	}

	public static RPObject getPackByName(String name) {
		FileFilter POSSIBLE_PACK = (file) -> file.getName().equals(name);
		File[] packs = (new File(MC.getResourcePackDir().getPath(), "RPUpdDir")).listFiles(POSSIBLE_PACK);
		if (packs == null) return null;
		Optional<File> file = Arrays.stream(packs).findFirst();
		if (file.isEmpty()) return null;
		File pack = file.get();

		RPObject rpObject;
		long time = -1;
		try {
			FileTime timestamp = Files.readAttributes(pack.toPath(), BasicFileAttributes.class).lastModifiedTime();
			time = timestamp.toMillis();
		} catch (IOException e) {LOGGER.error("IOException while finding pack: " + e.getMessage());}

		if (pack.isDirectory()) {
			rpObject = new RPObject(new DirectoryResourcePack(pack), time);
		} else {
			rpObject = new RPObject( new ZipResourcePack(pack), time);
		}
		return rpObject;
	}
}
