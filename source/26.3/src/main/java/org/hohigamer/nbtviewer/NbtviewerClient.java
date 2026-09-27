/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 */
package org.hohigamer.nbtviewer;

import net.fabricmc.api.ClientModInitializer;
import org.hohigamer.nbtviewer.NbtviewerConfig;
import org.hohigamer.nbtviewer.NbtviewerConfigIO;
import org.hohigamer.nbtviewer.client.NbtTooltipHandler;

public final class NbtviewerClient
implements ClientModInitializer {
    public static NbtviewerConfig CONFIG;

    public void onInitializeClient() {
        CONFIG = NbtviewerConfigIO.load();
        NbtTooltipHandler.register();
    }
}
