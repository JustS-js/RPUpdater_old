package net.just_s.rpupdater;

import net.fabricmc.api.ClientModInitializer;
import net.just_s.rpupdater.network.ModMessages;
import net.just_s.rpupdater.util.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

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
}
