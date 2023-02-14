package net.just_s.rpupdater.network.packet.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.just_s.rpupdater.RPUpdMod;
import net.just_s.rpupdater.util.RPObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class CheckS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Client code
        RPUpdMod.LOGGER.info("Processed checkS2Cpacket");
        byte length = buf.readByte();
        boolean flag = true;
        for (int i = 0; i < length; i++) {
            String name = buf.readString();
            long timestamp = buf.readLong();
//            RPUpdMod.LOGGER.info("Got pack: " + name + " with time " + timestamp);
            RPObject rpObject = RPUpdMod.getPackByName(name);
//            if (rpObject != null) {RPUpdMod.LOGGER.info("Found local pack: " + name + " with time " + rpObject.getTimestamp());}
            if (rpObject == null || rpObject.getTimestamp() != timestamp) {
                if (flag) {
                    flag = false;
                    client.inGameHud.getChatHud().addMessage(Text.of("§2New packs available:"));
                }
                Text message = Text.of("§2- " + name);
                Style style = Style.EMPTY;
                style = style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rpupd download " + name));
                style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("§6Скачать §a\"" + name + "\"")));
                message.getWithStyle(style).forEach((m) -> client.inGameHud.getChatHud().addMessage(m));
            }
        }
    }
}
