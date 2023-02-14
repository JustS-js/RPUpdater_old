package net.just_s.rpupdater.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.just_s.rpupdater.RPUpdServer;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class DownloadCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("rpupd").
                        then(
                                CommandManager.literal("download").
                                        then(
                                                CommandManager.argument("filename", StringArgumentType.greedyString()).executes(
                                                        (context) -> executeDownload(context.getSource(), StringArgumentType.getString(context, "filename"))
                                                )
                                        )
                        )
        );
    }

    private static int executeDownload(ServerCommandSource commandSource, String filename) {
        RPUpdServer.LOGGER.info(commandSource.getPlayer().getEntityName() + " wants to download " + filename);
        return 1;
    }
}
