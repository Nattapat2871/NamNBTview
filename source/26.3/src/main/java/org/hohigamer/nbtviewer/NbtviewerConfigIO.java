/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  net.fabricmc.loader.api.FabricLoader
 */
package org.hohigamer.nbtviewer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import net.fabricmc.loader.api.FabricLoader;
import org.hohigamer.nbtviewer.NbtviewerConfig;

public final class NbtviewerConfigIO {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("nbtviewer.json");

    private NbtviewerConfigIO() {
    }

    public static NbtviewerConfig load() {
        if (!Files.exists(FILE, new LinkOption[0])) {
            NbtviewerConfig cfg = new NbtviewerConfig();
            NbtviewerConfigIO.save(cfg);
            return cfg;
        }
        try {
            String json = Files.readString(FILE);
            NbtviewerConfig cfg = (NbtviewerConfig)GSON.fromJson(json, NbtviewerConfig.class);
            return cfg != null ? cfg : new NbtviewerConfig();
        }
        catch (Exception e) {
            return new NbtviewerConfig();
        }
    }

    public static void save(NbtviewerConfig cfg) {
        try {
            Files.createDirectories(FILE.getParent(), new FileAttribute[0]);
            Files.writeString(FILE, (CharSequence)GSON.toJson((Object)cfg), new OpenOption[0]);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}
