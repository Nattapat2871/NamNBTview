/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.terraformersmc.modmenu.api.ConfigScreenFactory
 *  com.terraformersmc.modmenu.api.ModMenuApi
 */
package org.hohigamer.nbtviewer.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.hohigamer.nbtviewer.client.NbtviewerConfigScreen;

public final class NbtviewerModMenu
implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return NbtviewerConfigScreen::create;
    }
}
