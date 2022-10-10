package net.just_s.rpupdater;

import net.fabricmc.api.ClientModInitializer;
import net.just_s.rpupdater.util.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class RPUpdMod implements ClientModInitializer {
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static final Logger LOGGER = LoggerFactory.getLogger("rpupd");
	public static final ResourcePackSource PACK_SOURCE_RPUPD = ResourcePackSource.nameAndSource("pack.source.rpupd");
	public static final Config CONFIG = new Config();

	@Override
	public void onInitializeClient() {}

	public static int RGBToNegativeDecimal(int r, int g, int b) {
		int alpha = -16777216;
		alpha = alpha + r*256*256 + g*256 + b;
		return alpha;
	}

	public static ResourcePackProfile PackToProfile(ResourcePackOrganizer.Pack pack) {
		for (ResourcePackProfile profile : MC.getResourcePackManager().getProfiles()) {
			if (profile.getDisplayName().equals(pack.getDisplayName())) {
				if (profile.getDescription().equals(pack.getDescription()) &
				profile.getCompatibility().equals(pack.getCompatibility()))
					return profile;
			}
		}
		return null;
	}
}
