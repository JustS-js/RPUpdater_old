package net.just_s.rpupdater.util;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ZipResourcePack;

import java.util.function.Supplier;

/** Representation of Resource Pack in RPUpdDir on Server */
public class RPObject {
    private String name;
    private long timestamp;
    private ResourcePack file;

    public RPObject(DirectoryResourcePack file, long timestamp) {
        this.name = file.getName();
        this.file = file;
        // Reading timestamp from file
        this.timestamp = timestamp;
    }

    public RPObject(ZipResourcePack profile, long timestamp) {
        this.name = profile.getName();
        this.file = profile;
        // Reading timestamp from file
        this.timestamp = timestamp;
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(this.name);
        buf.writeLong(this.timestamp);
    }

    public String getName() {return this.name;}
    public long getTimestamp() {return this.timestamp;}
}
