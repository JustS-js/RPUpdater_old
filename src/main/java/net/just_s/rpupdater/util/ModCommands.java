package net.just_s.rpupdater.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.just_s.rpupdater.command.DownloadCommand;

public class ModCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(DownloadCommand::register);
    }
}
