package net.just_s.rpupdater;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPUpdMod implements ClientModInitializer {
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static final Logger LOGGER = LoggerFactory.getLogger("rpupd");

	@Override
	public void onInitializeClient() {
		MC.getResourcePackDir();
		LOGGER.info("Hello Fabric world!");
	}
}
