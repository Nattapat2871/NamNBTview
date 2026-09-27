/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  me.shedaniel.clothconfig2.api.AbstractConfigListEntry
 *  me.shedaniel.clothconfig2.api.ConfigBuilder
 *  me.shedaniel.clothconfig2.api.ConfigCategory
 *  me.shedaniel.clothconfig2.api.ConfigEntryBuilder
 *  me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 */
package org.hohigamer.nbtviewer.client;

import java.util.Optional;
import java.util.function.Consumer;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.hohigamer.nbtviewer.NbtviewerClient;
import org.hohigamer.nbtviewer.NbtviewerConfig;
import org.hohigamer.nbtviewer.NbtviewerConfigIO;

public final class NbtviewerConfigScreen {
    private NbtviewerConfigScreen() {
    }

    public static Screen create(Screen parent) {
        NbtviewerConfig cfg = NbtviewerClient.CONFIG;
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle((Component)Component.translatable((String)"nbtviewer.configuration.title")).setSavingRunnable(() -> NbtviewerConfigIO.save(NbtviewerClient.CONFIG));
        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory client = builder.getOrCreateCategory((Component)Component.translatable((String)"nbtviewer.configuration.section.nbtviewer.client.toml.title"));
        client.addEntry((AbstractConfigListEntry)eb.startBooleanToggle((Component)Component.translatable((String)"nbtviewer.config.autoScale"), cfg.autoScale).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.autoScale.tooltip")}).setSaveConsumer(v -> {
            cfg.autoScale = v;
        }).build());
        client.addEntry((AbstractConfigListEntry)eb.startDoubleField((Component)Component.translatable((String)"nbtviewer.config.nbtScale"), cfg.nbtScale).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.nbtScale.tooltip")}).setMin(0.75).setMax(1.25).setSaveConsumer(v -> {
            cfg.nbtScale = v;
        }).build());
        client.addEntry((AbstractConfigListEntry)eb.startBooleanToggle((Component)Component.translatable((String)"nbtviewer.config.copyPopup"), cfg.copyPopup).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.copyPopup.tooltip")}).setSaveConsumer(v -> {
            cfg.copyPopup = v;
        }).build());
        client.addEntry((AbstractConfigListEntry)eb.startBooleanToggle((Component)Component.translatable((String)"nbtviewer.config.copySfx"), cfg.copySfx).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.copySfx.tooltip")}).setSaveConsumer(v -> {
            cfg.copySfx = v;
        }).build());
        SubCategoryBuilder generalSub = eb.startSubCategory((Component)Component.translatable((String)"nbtviewer.configuration.general")).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.configuration.general.tooltip")});
        generalSub.add((AbstractConfigListEntry)eb.startBooleanToggle((Component)Component.translatable((String)"nbtviewer.config.shiftOnly"), cfg.shiftOnly).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.shiftOnly.tooltip")}).setSaveConsumer(v -> {
            cfg.shiftOnly = v;
        }).build());
        generalSub.add((AbstractConfigListEntry)eb.startBooleanToggle((Component)Component.translatable((String)"nbtviewer.config.mobNbtOverlay"), cfg.mobNbtOverlay).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.mobNbtOverlay.tooltip")}).setSaveConsumer(v -> {
            cfg.mobNbtOverlay = v;
        }).build());
        generalSub.add((AbstractConfigListEntry)eb.startBooleanToggle((Component)Component.translatable((String)"nbtviewer.config.singleLine"), cfg.singleLine).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.singleLine.tooltip")}).setSaveConsumer(v -> {
            cfg.singleLine = v;
        }).build());
        generalSub.add((AbstractConfigListEntry)eb.startBooleanToggle((Component)Component.translatable((String)"nbtviewer.config.showHintLine"), cfg.showHintLine).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.showHintLine.tooltip")}).setSaveConsumer(v -> {
            cfg.showHintLine = v;
        }).build());
        generalSub.add((AbstractConfigListEntry)eb.startIntField((Component)Component.translatable((String)"nbtviewer.config.maxTotalChars"), cfg.maxTotalChars).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.maxTotalChars.tooltip")}).setMin(256).setMax(200000).setSaveConsumer(v -> {
            cfg.maxTotalChars = v;
        }).build());
        generalSub.add((AbstractConfigListEntry)eb.startIntField((Component)Component.translatable((String)"nbtviewer.config.maxLineLen"), cfg.maxLineLen).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.maxLineLen.tooltip")}).setMin(40).setMax(500).setSaveConsumer(v -> {
            cfg.maxLineLen = v;
        }).build());
        generalSub.add((AbstractConfigListEntry)eb.startIntSlider((Component)Component.translatable((String)"nbtviewer.config.indentSpaces"), cfg.indentSpaces, 0, 8).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.config.indentSpaces.tooltip")}).setSaveConsumer(v -> {
            cfg.indentSpaces = v;
        }).build());
        client.addEntry((AbstractConfigListEntry)generalSub.build());
        SubCategoryBuilder colorsSub = eb.startSubCategory((Component)Component.translatable((String)"nbtviewer.configuration.colors")).setTooltip(new Component[]{Component.translatable((String)"nbtviewer.configuration.colors.tooltip")});
        colorsSub.add(NbtviewerConfigScreen.hexField(eb, "nbtviewer.config.colorLabel", cfg.colorLabel, v -> {
            cfg.colorLabel = v;
        }));
        colorsSub.add(NbtviewerConfigScreen.hexField(eb, "nbtviewer.config.colorKey", cfg.colorKey, v -> {
            cfg.colorKey = v;
        }));
        colorsSub.add(NbtviewerConfigScreen.hexField(eb, "nbtviewer.config.colorString", cfg.colorString, v -> {
            cfg.colorString = v;
        }));
        colorsSub.add(NbtviewerConfigScreen.hexField(eb, "nbtviewer.config.colorNumber", cfg.colorNumber, v -> {
            cfg.colorNumber = v;
        }));
        colorsSub.add(NbtviewerConfigScreen.hexField(eb, "nbtviewer.config.colorBoolean", cfg.colorBoolean, v -> {
            cfg.colorBoolean = v;
        }));
        colorsSub.add(NbtviewerConfigScreen.hexField(eb, "nbtviewer.config.colorPunctuation", cfg.colorPunctuation, v -> {
            cfg.colorPunctuation = v;
        }));
        colorsSub.add(NbtviewerConfigScreen.hexField(eb, "nbtviewer.config.colorIdentifier", cfg.colorIdentifier, v -> {
            cfg.colorIdentifier = v;
        }));
        colorsSub.add(NbtviewerConfigScreen.hexField(eb, "nbtviewer.config.colorHint", cfg.colorHint, v -> {
            cfg.colorHint = v;
        }));
        client.addEntry((AbstractConfigListEntry)colorsSub.build());
        return builder.build();
    }

    private static AbstractConfigListEntry<String> hexField(ConfigEntryBuilder eb, String key, String current, Consumer<String> save) {
        return eb.startStrField((Component)Component.translatable((String)key), current).setTooltip(new Component[]{Component.translatable((String)(key + ".tooltip"))}).setErrorSupplier(s -> {
            int rgb = NbtviewerConfig.parseRgb(s, -1);
            return rgb == -1 ? Optional.of(Component.literal((String)"Format: #RRGGBB")) : Optional.empty();
        }).setSaveConsumer(save).build();
    }
}
