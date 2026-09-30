/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  com.mojang.blaze3d.platform.Window
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.DynamicOps
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
 *  net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
 *  net.minecraft.advancements.predicates.NbtPredicate
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.KeyMapping$Category
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.server.IntegratedServer
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.NbtOps
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.resources.Identifier
 *  net.minecraft.resources.RegistryOps
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.world.entity.Display
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.inventory.tooltip.TooltipComponent
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix3x2fStack
 */
package org.hohigamer.nbtviewer.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.advancements.predicates.NbtPredicate;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.hohigamer.nbtviewer.NbtviewerClient;
import org.hohigamer.nbtviewer.NbtviewerConfig;
import org.hohigamer.nbtviewer.NbtviewerConfigIO;
import org.hohigamer.nbtviewer.client.MobNbtNotebookScreen;
import org.hohigamer.nbtviewer.client.SnbtHighlighter;
import org.hohigamer.nbtviewer.client.tooltip.NbtViewerTooltipData;
import org.joml.Matrix3x2fStack;

public final class NbtTooltipHandler {
    private static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register((Identifier)Identifier.fromNamespaceAndPath((String)"nbtviewer", (String)"keybindings"));
    private static final KeyMapping TOGGLE_MOB_NBT_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping("key.nbtviewer.toggle_mob_nbt", InputConstants.Type.KEYBOARD, InputConstants.KEY_N, KEY_CATEGORY));
    private static final KeyMapping OPEN_MOB_NOTEBOOK_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping("key.nbtviewer.open_mob_nbt_notebook", InputConstants.Type.KEYBOARD, InputConstants.KEY_O, KEY_CATEGORY));
    private static final KeyMapping LOCK_MOB_TARGET_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping("key.nbtviewer.lock_mob_nbt_target", InputConstants.Type.KEYBOARD, InputConstants.KEY_L, KEY_CATEGORY));
    private static final int POPUP_TOTAL_MS = 1700;
    private static final int POPUP_SLIDE_MS = 220;
    private static final int POPUP_FADE_MS = 420;
    private static final int OVERLAY_MARGIN = 10;
    private static final int OVERLAY_PAD_X = 8;
    private static final int OVERLAY_PAD_Y = 6;
    private static final int OVERLAY_LINE_GAP = 1;
    private static final int OVERLAY_BG_COLOR = Integer.MIN_VALUE;
    private static final int OVERLAY_BORDER_COLOR = 0x60FFFFFF;
    private static final int OVERLAY_ENTER_MS = 160;
    private static final int OVERLAY_EXIT_MS = 140;
    private static final float OVERLAY_MAX_SCREEN_WIDTH_RATIO = 0.42f;
    private static final int SHORTCUT_HINT_PAD_X = 8;
    private static final int SHORTCUT_HINT_PAD_Y = 6;
    private static final int SHORTCUT_HINT_LINE_GAP = 2;
    private static final int SHORTCUT_HINT_BG_COLOR = 0x70000000;
    private static final int SHORTCUT_HINT_BORDER_COLOR = 0x50FFFFFF;
    private static final float SHORTCUT_HINT_MAX_WIDTH_RATIO = 0.34f;
    private static final long ENTITY_CACHE_MS = 100L;
    private static final double DISPLAY_PICK_RANGE = 8.0;
    private static final double DISPLAY_PICK_MARGIN = 0.45;
    private static final double DISPLAY_PICK_SEARCH_PAD = 1.0;
    private static String lastTooltipSnbt = null;
    private static long lastTooltipTimeMs = 0L;
    private static boolean lastCDown = false;
    private static boolean lastUDown = false;
    private static volatile int lastInputModifiers = 0;
    private static long popupStartMs = -1L;
    private static Component popupText = null;
    private static int cachedEntityId = Integer.MIN_VALUE;
    private static long cachedEntityCacheTimeMs = 0L;
    private static EntityOverlayData cachedEntityOverlay = null;
    private static int lockedEntityId = Integer.MIN_VALUE;
    private static EntityOverlayData activeOverlayData = null;
    private static int activeOverlayEntityId = Integer.MIN_VALUE;
    private static long activeOverlayStartMs = 0L;
    private static long activeOverlayFadeOutStartMs = -1L;

    private NbtTooltipHandler() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            NbtTooltipHandler.handleToggleShortcut(client);
            NbtTooltipHandler.handleLockShortcut(client);
            NbtTooltipHandler.handleOpenNotebookShortcut(client);
            NbtTooltipHandler.handleCopyShortcut(client);
        });
        ScreenEvents.AFTER_INIT.register((client, screen, w, h) -> ScreenEvents.afterExtract((Screen)screen).register((scr, gg, mouseX, mouseY, delta) -> NbtTooltipHandler.renderPopup(gg, scr.width, scr.height)));
    }

    private static void handleToggleShortcut(Minecraft client) {
        while (TOGGLE_MOB_NBT_KEY.consumeClick()) {
            if (client.gui.screen() != null) continue;
            NbtviewerConfig cfg = NbtviewerClient.CONFIG;
            boolean bl = cfg.mobNbtOverlay = !cfg.mobNbtOverlay;
            if (!cfg.mobNbtOverlay) {
                NbtTooltipHandler.clearLockedEntity();
            }
            NbtviewerConfigIO.save(cfg);
            NbtTooltipHandler.showPopup((Component)Component.translatable((String)(cfg.mobNbtOverlay ? "nbtviewer.popup.mob_nbt_on" : "nbtviewer.popup.mob_nbt_off")));
        }
    }

    private static void handleLockShortcut(Minecraft client) {
        while (LOCK_MOB_TARGET_KEY.consumeClick()) {
            if (client.gui.screen() != null || !NbtTooltipHandler.isShiftDown()) continue;
            Entity lockedEntity = NbtTooltipHandler.getLockedEntity(client);
            Entity lookedAtEntity = NbtTooltipHandler.getLookedAtEntity(client);
            if (lockedEntity != null) {
                if (lookedAtEntity != null && lookedAtEntity.getId() != lockedEntity.getId()) {
                    NbtTooltipHandler.setLockedEntity(lookedAtEntity);
                    NbtTooltipHandler.showPopup((Component)Component.translatable((String)"nbtviewer.popup.entity_locked", (Object[])new Object[]{lookedAtEntity.getDisplayName()}));
                    continue;
                }
                NbtTooltipHandler.clearLockedEntity();
                NbtTooltipHandler.showPopup((Component)Component.translatable((String)"nbtviewer.popup.entity_unlocked"));
                continue;
            }
            if (lookedAtEntity == null) continue;
            NbtTooltipHandler.setLockedEntity(lookedAtEntity);
            NbtTooltipHandler.showPopup((Component)Component.translatable((String)"nbtviewer.popup.entity_locked", (Object[])new Object[]{lookedAtEntity.getDisplayName()}));
        }
    }

    private static void handleOpenNotebookShortcut(Minecraft client) {
        boolean notebookRequested = false;
        while (OPEN_MOB_NOTEBOOK_KEY.consumeClick()) {
            if (client.gui.screen() == null && NbtTooltipHandler.isShiftDown()) {
                notebookRequested = true;
            }
        }

        boolean ctrl = InputConstants.isKeyDown(InputConstants.KEY_LCONTROL) || InputConstants.isKeyDown(InputConstants.KEY_RCONTROL);
        boolean uDown = InputConstants.isKeyDown(InputConstants.KEY_U);
        boolean ctrlURequested = uDown && !lastUDown && ctrl && client.gui.screen() == null;
        lastUDown = uDown;

        if (ctrlURequested) {
            NbtTooltipHandler.openTargetNotebook(client);
        } else if (notebookRequested) {
            NbtTooltipHandler.openMobNotebook(client);
        }
    }

    private static void handleCopyShortcut(Minecraft client) {
        boolean ctrl = InputConstants.isKeyDown(InputConstants.KEY_LCONTROL)
                || InputConstants.isKeyDown(InputConstants.KEY_RCONTROL);
        boolean shift = InputConstants.isKeyDown(InputConstants.KEY_LSHIFT)
                || InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
        boolean cDown = InputConstants.isKeyDown(InputConstants.KEY_C);
        if (cDown && !lastCDown && ctrl && shift) {
            NbtTooltipHandler.tryCopy();
        }
        lastCDown = cDown;
    }

    private static Tag toNbtTag(ItemStack stack, HolderLookup.Provider registries) {
        if (stack == null || stack.isEmpty() || registries == null) {
            return null;
        }

        var ops = RegistryOps.create(NbtOps.INSTANCE, registries);
        Tag encoded = ItemStack.CODEC.encodeStart(ops, stack).result().orElse(null);
        if (encoded != null) {
            return encoded;
        }

        CompoundTag fallback = new CompoundTag();
        fallback.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        fallback.putInt("count", stack.getCount());

        DataComponentPatch patch = stack.getComponentsPatch();
        if (!patch.isEmpty()) {
            Tag components = DataComponentPatch.CODEC.encodeStart(ops, patch).result().orElse(null);
            if (components != null) {
                fallback.put("components", components);
            } else {
                fallback.putString("components_debug", patch.toString());
            }
        }
        return fallback;
    }

    private static Tag toNbtTag(ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        return mc.level == null ? null : toNbtTag(stack, mc.level.registryAccess());
    }

    public static List<Component> appendItemNbtTooltip(ItemStack stack, Item.TooltipContext context, List<Component> original) {
        NbtviewerConfig cfg = NbtviewerClient.CONFIG;
        if (cfg == null || !cfg.enabled || stack == null || stack.isEmpty()) {
            return original;
        }

        ArrayList<Component> lines = new ArrayList<>(original);
        boolean shiftDown = NbtTooltipHandler.isShiftDown();
        if (cfg.shiftOnly && !shiftDown) {
            if (cfg.showHintLine) {
                lines.add(Component.translatable("nbtviewer.tooltip.hold_shift").setStyle(NbtTooltipHandler.hintStyle(cfg)));
            }
            return lines;
        }

        Tag tag = NbtTooltipHandler.toNbtTag(stack, context.registries());
        if (tag == null) {
            return lines;
        }

        String snbt = tag.toString();
        NbtTooltipHandler.rememberVisibleSnbt(snbt);
        lines.addAll(NbtTooltipHandler.createFormattedSnbtLines(cfg, snbt));
        if (cfg.showHintLine) {
            lines.add(Component.translatable("nbtviewer.tooltip.copy_hint").setStyle(NbtTooltipHandler.hintStyle(cfg)));
        }
        return lines;
    }

    public static Optional<TooltipComponent> fabric$maybeProvideTooltipComponent(ItemStack stack, Optional<TooltipComponent> original) {
        if (stack == null || stack.isEmpty()) {
            return original;
        }
        NbtviewerConfig cfg = NbtviewerClient.CONFIG;
        boolean shiftDown = NbtTooltipHandler.isShiftDown();
        if (cfg.shiftOnly && !shiftDown) {
            if (cfg.showHintLine) {
                List<Component> hint = List.of(Component.translatable("nbtviewer.tooltip.hold_shift").setStyle(NbtTooltipHandler.hintStyle(cfg)));
                return Optional.of(new NbtViewerTooltipData(hint, 1.0f));
            }
            return original;
        }
        Tag tag = NbtTooltipHandler.toNbtTag(stack);
        if (tag == null) {
            return original;
        }
        String snbt = tag.toString();
        NbtTooltipHandler.rememberVisibleSnbt(snbt);
        List<Component> lines = NbtTooltipHandler.createFormattedSnbtLines(cfg, snbt);
        if (lines.isEmpty()) {
            return original;
        }
        if (cfg.showHintLine) {
            lines.add((Component)Component.translatable((String)"nbtviewer.tooltip.copy_hint").setStyle(NbtTooltipHandler.hintStyle(cfg)));
        }
        float scale = NbtTooltipHandler.computeScale(cfg, lines.size(), snbt.length());
        return Optional.of(new NbtViewerTooltipData(lines, scale));
    }

    private static void renderMobOverlay(GuiGraphicsExtractor gg, int screenW, int screenH) {
        float enterProgress;
        EntityOverlayData overlay;
        Entity entity;
        NbtviewerConfig cfg = NbtviewerClient.CONFIG;
        if (!cfg.mobNbtOverlay) {
            activeOverlayData = null;
            activeOverlayEntityId = Integer.MIN_VALUE;
            activeOverlayFadeOutStartMs = -1L;
            NbtTooltipHandler.clearLockedEntity();
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        long now = System.currentTimeMillis();
        if (mc.level == null) {
            activeOverlayData = null;
            activeOverlayEntityId = Integer.MIN_VALUE;
            activeOverlayFadeOutStartMs = -1L;
            NbtTooltipHandler.clearLockedEntity();
            return;
        }
        Entity lockedEntity = NbtTooltipHandler.getLockedEntity(mc);
        boolean locked = lockedEntity != null;
        Entity entity2 = entity = locked ? lockedEntity : NbtTooltipHandler.getLookedAtEntity(mc);
        if (entity != null) {
            overlay = NbtTooltipHandler.getEntityOverlayData(entity, cfg);
            if (overlay == null || overlay.lines().isEmpty()) {
                return;
            }
            activeOverlayData = overlay;
            if (activeOverlayEntityId != entity.getId()) {
                activeOverlayEntityId = entity.getId();
                activeOverlayStartMs = now;
            }
            activeOverlayFadeOutStartMs = -1L;
            enterProgress = NbtTooltipHandler.easeOutCubic(NbtTooltipHandler.clamp01((float)(now - activeOverlayStartMs) / 160.0f));
        } else {
            float fadeOut;
            if (activeOverlayData == null || activeOverlayData.lines().isEmpty()) {
                activeOverlayEntityId = Integer.MIN_VALUE;
                activeOverlayFadeOutStartMs = -1L;
                return;
            }
            if (activeOverlayFadeOutStartMs < 0L) {
                activeOverlayFadeOutStartMs = now;
            }
            if ((enterProgress = 1.0f - NbtTooltipHandler.easeOutCubic(fadeOut = NbtTooltipHandler.clamp01((float)(now - activeOverlayFadeOutStartMs) / 140.0f))) <= 0.01f) {
                activeOverlayData = null;
                activeOverlayEntityId = Integer.MIN_VALUE;
                activeOverlayFadeOutStartMs = -1L;
                return;
            }
            overlay = activeOverlayData;
        }
        Font font = mc.font;
        PreparedOverlayLines prepared = NbtTooltipHandler.prepareOverlayLines(overlay.lines(), overlay.scale(), font, mc.getWindow().getGuiScaledWidth(), screenH);
        if (prepared.lines().isEmpty()) {
            return;
        }
        NbtTooltipHandler.drawOverlayBox(gg, font, prepared, overlay.scale(), enterProgress);
        if (cfg.showHintLine && entity != null) {
            NbtTooltipHandler.renderEntityShortcutHints(gg, font, screenW, screenH, cfg, locked);
        }
    }

    private static EntityOverlayData getEntityOverlayData(Entity entity, NbtviewerConfig cfg) {
        long now = System.currentTimeMillis();
        if (cachedEntityOverlay != null && cachedEntityId == entity.getId() && now - cachedEntityCacheTimeMs <= 100L) {
            return cachedEntityOverlay;
        }
        CompoundTag tag = NbtTooltipHandler.getBestEntityTag(entity);
        if (tag == null) {
            return null;
        }
        String snbt = tag.toString();
        ArrayList<Component> lines = new ArrayList<Component>();
        lines.add((Component)NbtTooltipHandler.createEntityTitle(entity, cfg));
        lines.addAll(NbtTooltipHandler.createEntityDebugLines(entity, tag, cfg));
        lines.add(NbtTooltipHandler.createSectionLabel(cfg, "nbtviewer.overlay.section_snbt"));
        lines.addAll(NbtTooltipHandler.createFormattedSnbtLines(cfg, snbt));
        float scale = NbtTooltipHandler.computeScale(cfg, lines.size(), snbt.length());
        EntityOverlayData overlay = new EntityOverlayData(lines, scale);
        cachedEntityId = entity.getId();
        cachedEntityCacheTimeMs = now;
        cachedEntityOverlay = overlay;
        return overlay;
    }

    private static MutableComponent createEntityTitle(Entity entity, NbtviewerConfig cfg) {
        return Component.translatable((String)"nbtviewer.overlay.mob_title").setStyle(NbtTooltipHandler.styleFromRgb(NbtviewerConfig.parseRgb(cfg.colorLabel, 0xAAAAAA))).append((Component)entity.getDisplayName().copy().setStyle(NbtTooltipHandler.styleFromRgb(NbtviewerConfig.parseRgb(cfg.colorKey, 0x55FFFF))));
    }

    private static List<Component> createEntityDebugLines(Entity entity, CompoundTag tag, NbtviewerConfig cfg) {
        String team;
        ArrayList<Component> lines = new ArrayList<Component>();
        String typeId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
        lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.type", typeId, 120));
        lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.runtime_id", Integer.toString(entity.getId()), 24));
        lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.uuid", entity.getStringUUID(), 160));
        lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.tags", NbtTooltipHandler.formatTags(entity, tag), 220));
        lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.pos", NbtTooltipHandler.formatPosition(entity), 48));
        lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.rot", NbtTooltipHandler.formatRotation(entity), 48));
        String flags = NbtTooltipHandler.formatFlags(entity, tag);
        if (!flags.isBlank()) {
            lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.flags", flags, 160));
        }
        if (!(team = tag.getString("Team").orElse("")).isBlank()) {
            lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.team", team, 96));
        }
        if (entity.getCustomName() != null) {
            lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.custom_name", entity.getCustomName().getString(), 140));
        }
        if (!entity.getPassengers().isEmpty()) {
            lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.passengers", Integer.toString(entity.getPassengers().size()), 16));
        }
        NbtTooltipHandler.appendArmorStandDebugLine(lines, tag, cfg, typeId);
        NbtTooltipHandler.appendDisplayDebugLine(lines, tag, cfg, typeId);
        return lines;
    }

    private static void appendArmorStandDebugLine(List<Component> lines, CompoundTag tag, NbtviewerConfig cfg, String typeId) {
        if (!"minecraft:armor_stand".equals(typeId)) {
            return;
        }
        ArrayList<String> parts = new ArrayList<String>();
        if (tag.getBoolean("Marker").orElse(false).booleanValue()) {
            parts.add("marker");
        }
        if (tag.getBoolean("Small").orElse(false).booleanValue()) {
            parts.add("small");
        }
        if (tag.getBoolean("ShowArms").orElse(false).booleanValue()) {
            parts.add("arms");
        }
        if (tag.getBoolean("NoBasePlate").orElse(false).booleanValue()) {
            parts.add("no_base_plate");
        }
        if (!parts.isEmpty()) {
            lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.armor_stand", String.join((CharSequence)", ", parts), 120));
        }
    }

    private static void appendDisplayDebugLine(List<Component> lines, CompoundTag tag, NbtviewerConfig cfg, String typeId) {
        String text;
        Float viewRange;
        if (!typeId.endsWith("_display")) {
            return;
        }
        ArrayList<String> parts = new ArrayList<>();
        String billboard = NbtTooltipHandler.getStringIfPresent(tag, "billboard", "Billboard");
        if (!billboard.isBlank()) {
            parts.add("billboard=" + billboard);
        }
        if ((viewRange = NbtTooltipHandler.getFloatIfPresent(tag, "view_range", "ViewRange")) != null) {
            parts.add("view=" + NbtTooltipHandler.trimFloat(viewRange.floatValue()));
        }
        if ("minecraft:item_display".equals(typeId)) {
            String itemDisplayMode;
            CompoundTag itemTag = NbtTooltipHandler.getCompoundIfPresent(tag, "item", "Item");
            String itemSummary = NbtTooltipHandler.formatItemSummary(itemTag);
            if (!itemSummary.isBlank()) {
                parts.add(itemSummary);
            }
            if (!(itemDisplayMode = NbtTooltipHandler.getStringIfPresent(tag, "item_display", "ItemDisplay")).isBlank()) {
                parts.add("mode=" + itemDisplayMode);
            }
        } else if ("minecraft:block_display".equals(typeId)) {
            String blockName;
            CompoundTag blockState = NbtTooltipHandler.getCompoundIfPresent(tag, "block_state", "blockState", "BlockState");
            String string = blockName = blockState == null ? "" : blockState.getString("Name").orElse("");
            if (!blockName.isBlank()) {
                parts.add("block=" + blockName);
            }
        } else if ("minecraft:text_display".equals(typeId) && !(text = NbtTooltipHandler.getStringIfPresent(tag, "text", "Text")).isBlank()) {
            parts.add("text=" + NbtTooltipHandler.ellipsize(text.replace('\n', ' '), 140));
        }
        if (!parts.isEmpty()) {
            lines.add(NbtTooltipHandler.createInfoLine(cfg, "nbtviewer.overlay.info.display", String.join((CharSequence)", ", parts), 220));
        }
    }

    private static Component createSectionLabel(NbtviewerConfig cfg, String key) {
        return Component.translatable((String)key).setStyle(NbtTooltipHandler.styleFromRgb(NbtviewerConfig.parseRgb(cfg.colorLabel, 0xAAAAAA)));
    }

    private static Component createInfoLine(NbtviewerConfig cfg, String labelKey, String value, int maxLen) {
        int labelRgb = NbtviewerConfig.parseRgb(cfg.colorLabel, 0xAAAAAA);
        int punctRgb = NbtviewerConfig.parseRgb(cfg.colorPunctuation, 0xFFFFFF);
        int valueRgb = NbtviewerConfig.parseRgb(cfg.colorIdentifier, 0xFFFFFF);
        return Component.translatable((String)labelKey).setStyle(NbtTooltipHandler.styleFromRgb(labelRgb)).append((Component)Component.literal((String)": ").setStyle(NbtTooltipHandler.styleFromRgb(punctRgb))).append((Component)Component.literal((String)NbtTooltipHandler.ellipsize(value, maxLen)).setStyle(NbtTooltipHandler.styleFromRgb(valueRgb)));
    }

    private static CompoundTag getBestEntityTag(Entity entity) {
        CompoundTag serverTag = NbtTooltipHandler.getIntegratedServerEntityTag(entity);
        if (serverTag != null) {
            return serverTag;
        }
        return NbtPredicate.getEntityTagToCompare((Entity)entity);
    }

    private static CompoundTag getIntegratedServerEntityTag(Entity entity) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || !mc.hasSingleplayerServer()) {
            return null;
        }
        IntegratedServer server = mc.getSingleplayerServer();
        if (server == null) {
            return null;
        }
        try {
            return (CompoundTag)server.submit(() -> NbtTooltipHandler.lambda$getIntegratedServerEntityTag$0((MinecraftServer)server, entity)).join();
        }
        catch (RuntimeException ignored) {
            return null;
        }
    }

    private static String formatTags(Entity entity, CompoundTag tag) {
        ArrayList<String> tagValues = new ArrayList<String>();
        ListTag tagList = tag.getList("Tags").orElse(null);
        if (tagList != null) {
            for (int i = 0; i < tagList.size(); ++i) {
                String value = tagList.getString(i).orElse("");
                if (value.isBlank()) continue;
                tagValues.add(value);
            }
        }
        if (!tagValues.isEmpty()) {
            return String.join((CharSequence)", ", tagValues);
        }
        Set entityTags = entity.entityTags();
        if (entityTags != null && !entityTags.isEmpty()) {
            return String.join((CharSequence)", ", entityTags);
        }
        return "-";
    }

    private static String formatPosition(Entity entity) {
        return String.format(Locale.ROOT, "%.2f %.2f %.2f", entity.getX(), entity.getY(), entity.getZ());
    }

    private static String formatRotation(Entity entity) {
        return String.format(Locale.ROOT, "yaw %.1f, pitch %.1f", Float.valueOf(entity.getYRot()), Float.valueOf(entity.getXRot()));
    }

    private static String formatFlags(Entity entity, CompoundTag tag) {
        ArrayList<String> flags = new ArrayList<String>();
        if (entity.isInvisible()) {
            flags.add("invisible");
        }
        if (entity.isOnFire()) {
            flags.add("on_fire");
        }
        if (tag.getBoolean("NoGravity").orElse(false).booleanValue()) {
            flags.add("no_gravity");
        }
        if (tag.getBoolean("Invulnerable").orElse(false).booleanValue()) {
            flags.add("invulnerable");
        }
        if (tag.getBoolean("Glowing").orElse(false).booleanValue()) {
            flags.add("glowing");
        }
        if (tag.getBoolean("Silent").orElse(false).booleanValue()) {
            flags.add("silent");
        }
        if (tag.getBoolean("OnGround").orElse(false).booleanValue()) {
            flags.add("on_ground");
        }
        if (tag.getBoolean("NoAI").orElse(false).booleanValue()) {
            flags.add("no_ai");
        }
        if (tag.getBoolean("PersistenceRequired").orElse(false).booleanValue()) {
            flags.add("persistent");
        }
        return String.join((CharSequence)", ", flags);
    }

    private static CompoundTag getCompoundIfPresent(CompoundTag tag, String ... keys) {
        for (String key : keys) {
            CompoundTag value = tag.getCompound(key).orElse(null);
            if (value == null) continue;
            return value;
        }
        return null;
    }

    private static String getStringIfPresent(CompoundTag tag, String ... keys) {
        for (String key : keys) {
            Optional value = tag.getString(key);
            if (!value.isPresent()) continue;
            return (String)value.get();
        }
        return "";
    }

    private static Float getFloatIfPresent(CompoundTag tag, String ... keys) {
        for (String key : keys) {
            Optional value = tag.getFloat(key);
            if (!value.isPresent()) continue;
            return (Float)value.get();
        }
        return null;
    }

    private static String formatItemSummary(CompoundTag itemTag) {
        if (itemTag == null) {
            return "";
        }
        String id = NbtTooltipHandler.getStringIfPresent(itemTag, "id", "Id");
        if (id.isBlank()) {
            return "";
        }
        String count = "";
        count = itemTag.getInt("count").map(v -> " x" + v).orElseGet(() -> itemTag.getByte("Count").map(v -> " x" + v).orElseGet(() -> itemTag.getInt("Count").map(v -> " x" + v).orElse("")));
        return "item=" + id + count;
    }

    private static String trimFloat(float value) {
        return String.format(Locale.ROOT, "%.2f", Float.valueOf(value));
    }

    private static String ellipsize(String value, int maxLen) {
        if (value == null) {
            return "";
        }
        String normalized = value.replace('\n', ' ').trim();
        if (normalized.length() <= maxLen) {
            return normalized;
        }
        return normalized.substring(0, Math.max(0, maxLen - 3)) + "...";
    }

    private static List<Component> createFormattedSnbtLines(NbtviewerConfig cfg, String snbt) {
        return SnbtHighlighter.format(snbt, cfg.singleLine, cfg.maxLineLen, cfg.indentSpaces, cfg.maxTotalChars, new SnbtHighlighter.Colors(NbtviewerConfig.parseRgb(cfg.colorLabel, 0xAAAAAA), NbtviewerConfig.parseRgb(cfg.colorKey, 0x55FFFF), NbtviewerConfig.parseRgb(cfg.colorString, 0x55FF55), NbtviewerConfig.parseRgb(cfg.colorNumber, 0xFFAA00), NbtviewerConfig.parseRgb(cfg.colorBoolean, 0xFF55FF), NbtviewerConfig.parseRgb(cfg.colorPunctuation, 0xFFFFFF), NbtviewerConfig.parseRgb(cfg.colorIdentifier, 0xFFFFFF)));
    }

    private static PreparedOverlayLines prepareOverlayLines(List<Component> lines, float scale, Font font, int screenW, int screenH) {
        int maxInnerHeight = Math.max(0, screenH - 20 - 12);
        if (maxInnerHeight <= 0 || lines.isEmpty()) {
            return new PreparedOverlayLines(List.of(), 0);
        }
        int maxInnerWidth = Math.max(180, Math.min((int)((float)screenW * 0.42f), 360) - 16);
        Objects.requireNonNull(font);
        int maxRawHeight = Math.max(9, (int)Math.floor((float)maxInnerHeight / Math.max(scale, 0.01f)));
        int maxRawWidth = Math.max(90, (int)Math.floor((float)maxInnerWidth / Math.max(scale, 0.01f)));
        ArrayList<FormattedCharSequence> visible = new ArrayList<FormattedCharSequence>();
        int rawHeight = 0;
        for (Component line : lines) {
            List<FormattedCharSequence> wrappedParts = font.split(line, maxRawWidth);
            for (FormattedCharSequence wrapped : wrappedParts) {
                int addedHeight = visible.isEmpty() ? 9 : 10;
                if (rawHeight + addedHeight > maxRawHeight) {
                    return new PreparedOverlayLines(visible, NbtTooltipHandler.computeWrappedWidth(font, visible));
                }
                visible.add(wrapped);
                rawHeight += addedHeight;
            }
        }
        return new PreparedOverlayLines(visible, NbtTooltipHandler.computeWrappedWidth(font, visible));
    }

    private static int computeWrappedWidth(Font font, List<FormattedCharSequence> lines) {
        int maxWidth = 0;
        for (FormattedCharSequence line : lines) {
            maxWidth = Math.max(maxWidth, font.width(line));
        }
        return maxWidth;
    }

    private static void openMobNotebook(Minecraft client) {
        Entity entity = NbtTooltipHandler.getSelectedOverlayEntity(client);
        if (entity == null) {
            return;
        }
        CompoundTag tag = NbtTooltipHandler.getBestEntityTag(entity);
        if (tag == null) {
            return;
        }
        NbtTooltipHandler.openNotebook(client, entity.getDisplayName().copy(), tag);
    }

    private static void openTargetNotebook(Minecraft client) {
        if (client.level == null) {
            return;
        }

        HitResult hit = client.hitResult;
        if (hit instanceof EntityHitResult entityHit) {
            Entity entity = entityHit.getEntity();
            if (entity != null && !entity.isRemoved()) {
                CompoundTag tag = NbtTooltipHandler.getBestEntityTag(entity);
                if (tag != null) {
                    NbtTooltipHandler.openNotebook(client, entity.getDisplayName().copy(), tag);
                }
                return;
            }
        }

        if (hit instanceof BlockHitResult blockHit) {
            BlockPos pos = blockHit.getBlockPos();
            var state = client.level.getBlockState(pos);
            CompoundTag tag;
            BlockEntity blockEntity = client.level.getBlockEntity(pos);
            if (blockEntity != null) {
                try {
                    tag = blockEntity.saveWithFullMetadata(client.level.registryAccess());
                } catch (RuntimeException ignored) {
                    tag = NbtUtils.writeBlockState(state);
                }
            } else {
                tag = NbtUtils.writeBlockState(state);
            }

            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
            String blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
            Component title = Component.literal(blockId + " @ " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
            NbtTooltipHandler.openNotebook(client, title, tag);
        }
    }

    private static void openNotebook(Minecraft client, Component targetName, CompoundTag tag) {
        String snbt = tag.toString();
        NbtTooltipHandler.rememberVisibleSnbt(snbt);
        client.gui.setScreen((Screen)new MobNbtNotebookScreen(targetName, snbt, NbtviewerClient.CONFIG));
    }

    private static Entity getSelectedOverlayEntity(Minecraft client) {
        Entity lockedEntity = NbtTooltipHandler.getLockedEntity(client);
        return lockedEntity != null ? lockedEntity : NbtTooltipHandler.getLookedAtEntity(client);
    }

    private static Entity getLookedAtEntity(Minecraft client) {
        EntityHitResult entityHitResult;
        Entity entity;
        HitResult hitResult = client.hitResult;
        if (hitResult instanceof EntityHitResult && (entity = (entityHitResult = (EntityHitResult)hitResult).getEntity()) != null && !entity.isRemoved()) {
            return entity;
        }
        return NbtTooltipHandler.findDisplayEntityFallback(client);
    }

    private static Entity getLockedEntity(Minecraft client) {
        if (lockedEntityId == Integer.MIN_VALUE || client.level == null) {
            return null;
        }
        Entity entity = client.level.getEntity(lockedEntityId);
        if (entity == null || entity.isRemoved()) {
            NbtTooltipHandler.clearLockedEntity();
            return null;
        }
        return entity;
    }

    private static void setLockedEntity(Entity entity) {
        lockedEntityId = entity.getId();
    }

    private static void clearLockedEntity() {
        lockedEntityId = Integer.MIN_VALUE;
    }

    private static Entity findDisplayEntityFallback(Minecraft client) {
        if (client.level == null) {
            return null;
        }
        Entity camera = client.getCameraEntity();
        if (camera == null) {
            return null;
        }
        double maxDistance = NbtTooltipHandler.getFallbackPickDistance(client, camera);
        Vec3 start = camera.getEyePosition(1.0f);
        Vec3 end = start.add(camera.getViewVector(1.0f).scale(maxDistance));
        AABB searchBox = camera.getBoundingBox().expandTowards(end.subtract(start)).inflate(1.0);
        List<Entity> candidates = client.level.getEntities(camera, searchBox, entity -> NbtTooltipHandler.isDisplayEntity(entity) && !entity.isRemoved());
        Entity bestEntity = null;
        double bestDistanceSqr = maxDistance * maxDistance;
        for (Entity candidate : candidates) {
            double distanceSqr;
            AABB hitBox = NbtTooltipHandler.getDisplayPickBox(candidate);
            if (hitBox.contains(start)) {
                return candidate;
            }
            Optional clip = hitBox.clip(start, end);
            if (clip.isEmpty() || !((distanceSqr = start.distanceToSqr((Vec3)clip.get())) <= bestDistanceSqr)) continue;
            bestDistanceSqr = distanceSqr;
            bestEntity = candidate;
        }
        return bestEntity;
    }

    private static double getFallbackPickDistance(Minecraft client, Entity camera) {
        double distance = 8.0;
        HitResult currentHit = client.hitResult;
        if (currentHit != null && currentHit.getType() != HitResult.Type.MISS) {
            distance = Math.min(distance, currentHit.distanceTo(camera));
        }
        return Math.max(0.5, distance);
    }

    private static boolean isDisplayEntity(Entity entity) {
        String path = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath();
        return path.endsWith("_display");
    }

    private static AABB getDisplayPickBox(Entity entity) {
        if (entity instanceof Display) {
            Display display = (Display)entity;
            AABB cullingBox = display.getBoundingBox().inflate(0.45);
            if (cullingBox.getSize() > 0.01) {
                return cullingBox;
            }
            double width = Math.max(0.8, (double)display.getWidth() + 0.45);
            double height = Math.max(0.8, (double)display.getHeight() + 0.45);
            return AABB.ofSize((Vec3)entity.position(), (double)width, (double)height, (double)width);
        }
        return entity.getBoundingBox().inflate(0.45);
    }

    private static void drawOverlayBox(GuiGraphicsExtractor gg, Font font, PreparedOverlayLines prepared, float scale, float enterProgress) {
        float animatedScale = scale * (0.94f + enterProgress * 0.06f);
        float animatedAlpha = 0.25f + enterProgress * 0.75f;
        int textHeight = NbtTooltipHandler.getTextHeight(font, prepared.lines().size());
        int boxW = (int)Math.ceil((float)prepared.textWidth() * animatedScale) + 16;
        int boxH = (int)Math.ceil((float)textHeight * animatedScale) + 12;
        int x0 = 10;
        int y0 = 10;
        int x1 = x0 + boxW;
        int y1 = y0 + boxH;
        gg.fill(x0, y0, x1, y1, NbtTooltipHandler.applyAlpha(Integer.MIN_VALUE, animatedAlpha));
        gg.fill(x0, y0, x1, y0 + 1, NbtTooltipHandler.applyAlpha(0x60FFFFFF, animatedAlpha));
        gg.fill(x0, y1 - 1, x1, y1, NbtTooltipHandler.applyAlpha(0x60FFFFFF, animatedAlpha));
        gg.fill(x0, y0, x0 + 1, y1, NbtTooltipHandler.applyAlpha(0x60FFFFFF, animatedAlpha));
        gg.fill(x1 - 1, y0, x1, y1, NbtTooltipHandler.applyAlpha(0x60FFFFFF, animatedAlpha));
        Matrix3x2fStack pose = gg.pose();
        pose.pushMatrix();
        pose.translate((float)(x0 + 8), (float)(y0 + 6));
        pose.scale(animatedScale, animatedScale);
        int dy = 0;
        for (FormattedCharSequence line : prepared.lines()) {
            gg.text(font, line, 0, dy, NbtTooltipHandler.applyAlpha(-1, animatedAlpha), true);
            Objects.requireNonNull(font);
            dy += 9 + 1;
        }
        pose.popMatrix();
    }

    private static void renderEntityShortcutHints(GuiGraphicsExtractor gg, Font font, int screenW, int screenH, NbtviewerConfig cfg, boolean locked) {
        int maxTextWidth = Math.max(150, Math.min((int)((float)screenW * 0.34f), 250));
        ArrayList<FormattedCharSequence> wrappedLines = new ArrayList<FormattedCharSequence>();
        wrappedLines.addAll(font.split((FormattedText)NbtTooltipHandler.createLockEntityHint(cfg, locked), maxTextWidth));
        wrappedLines.addAll(font.split((FormattedText)NbtTooltipHandler.createOpenNotebookHint(cfg), maxTextWidth));
        if (wrappedLines.isEmpty()) {
            return;
        }
        int textWidth = NbtTooltipHandler.computeWrappedWidth(font, wrappedLines);
        int textHeight = NbtTooltipHandler.getTextHeight(font, wrappedLines.size(), 2);
        int boxW = textWidth + 16;
        int boxH = textHeight + 12;
        int popupReservedHeight = NbtTooltipHandler.getPopupReservedHeight(font);
        int x0 = screenW - 10 - boxW;
        int y0 = screenH - 10 - popupReservedHeight - boxH;
        int x1 = x0 + boxW;
        int y1 = y0 + boxH;
        gg.fill(x0, y0, x1, y1, 0x70000000);
        gg.fill(x0, y0, x1, y0 + 1, 0x50FFFFFF);
        gg.fill(x0, y1 - 1, x1, y1, 0x50FFFFFF);
        gg.fill(x0, y0, x0 + 1, y1, 0x50FFFFFF);
        gg.fill(x1 - 1, y0, x1, y1, 0x50FFFFFF);
        int y = y0 + 6;
        for (FormattedCharSequence line : wrappedLines) {
            gg.text(font, line, x0 + 8, y, -1, true);
            Objects.requireNonNull(font);
            y += 9 + 2;
        }
    }

    private static int getTextHeight(Font font, int lineCount) {
        return NbtTooltipHandler.getTextHeight(font, lineCount, 1);
    }

    private static int getTextHeight(Font font, int lineCount, int lineGap) {
        if (lineCount <= 0) {
            return 0;
        }
        Objects.requireNonNull(font);
        return 9 * lineCount + lineGap * (lineCount - 1);
    }

    private static int getPopupReservedHeight(Font font) {
        if (popupStartMs < 0L || popupText == null) {
            return 0;
        }
        long ageMs = System.currentTimeMillis() - popupStartMs;
        if (ageMs >= 1700L) {
            return 0;
        }
        int popupPadY = 6;
        Objects.requireNonNull(font);
        return 9 + popupPadY * 2 + 8;
    }

    private static int applyAlpha(int color, float alphaMul) {
        int alpha = color >>> 24 & 0xFF;
        if (alpha == 0 && (color & 0xFFFFFF) != 0) {
            alpha = 255;
        }
        int scaledAlpha = Math.max(0, Math.min(255, Math.round((float)alpha * alphaMul)));
        return scaledAlpha << 24 | color & 0xFFFFFF;
    }

    private static void tryCopy() {
        String snbt = lastTooltipSnbt;
        long ageMs = System.currentTimeMillis() - lastTooltipTimeMs;
        if (snbt == null || ageMs > 500L) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        mc.keyboardHandler.setClipboard(snbt);
        NbtviewerConfig cfg = NbtviewerClient.CONFIG;
        if (cfg.copySfx && mc.player != null && mc.level != null) {
            double x = mc.player.getX();
            double y = mc.player.getY();
            double z = mc.player.getZ();
            mc.level.playLocalSound(x, y, z, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.AMBIENT, 0.4f, 1.0f, false);
            mc.level.playLocalSound(x, y, z, (SoundEvent)SoundEvents.NOTE_BLOCK_HAT.value(), SoundSource.AMBIENT, 1.0f, 1.0f, false);
        }
        if (cfg.copyPopup) {
            NbtTooltipHandler.showPopup((Component)Component.translatable((String)"nbtviewer.popup.copied"));
        }
    }

    private static void showPopup(Component text) {
        popupText = text;
        popupStartMs = System.currentTimeMillis();
    }

    private static void rememberVisibleSnbt(String snbt) {
        lastTooltipSnbt = snbt;
        lastTooltipTimeMs = System.currentTimeMillis();
    }

    private static void renderPopup(GuiGraphicsExtractor gg, int screenW, int screenH) {
        if (popupStartMs < 0L || popupText == null) {
            return;
        }
        long now = System.currentTimeMillis();
        int t = (int)(now - popupStartMs);
        if (t >= 1700) {
            popupStartMs = -1L;
            popupText = null;
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        String txt = popupText.getString();
        int textW = font.width(txt);
        Objects.requireNonNull(font);
        int textH = 9;
        int padX = 8;
        int padY = 6;
        int boxW = textW + padX * 2;
        int boxH = textH + padY * 2;
        int margin = 10;
        int baseX = screenW - margin - boxW;
        int baseY = screenH - margin - boxH;
        float slideP = NbtTooltipHandler.easeOutCubic(NbtTooltipHandler.clamp01((float)t / 220.0f));
        int slideOffset = (int)((1.0f - slideP) * (float)(boxH + 10));
        float alphaMul = 1.0f;
        int fadeStart = 1280;
        if (t > fadeStart) {
            float fp = (float)(t - fadeStart) / 420.0f;
            alphaMul = 1.0f - NbtTooltipHandler.clamp01(fp);
        }
        int bgAlpha = (int)(170.0f * alphaMul);
        int textAlpha = (int)(255.0f * alphaMul);
        int borderAlpha = (int)(90.0f * alphaMul);
        int bgColor = bgAlpha << 24;
        int textColor = textAlpha << 24 | 0xFFFFFF;
        int borderColor = borderAlpha << 24 | 0xFFFFFF;
        int x0 = baseX;
        int y0 = baseY + slideOffset;
        int x1 = baseX + boxW;
        int y1 = y0 + boxH;
        gg.fill(x0, y0, x1, y1, bgColor);
        gg.fill(x0, y0, x1, y0 + 1, borderColor);
        gg.fill(x0, y1 - 1, x1, y1, borderColor);
        gg.fill(x0, y0, x0 + 1, y1, borderColor);
        gg.fill(x1 - 1, y0, x1, y1, borderColor);
        gg.text(font, txt, x0 + padX, y0 + padY, textColor, true);
    }

    private static float clamp01(float v) {
        if (v < 0.0f) {
            return 0.0f;
        }
        if (v > 1.0f) {
            return 1.0f;
        }
        return v;
    }

    private static float easeOutCubic(float t) {
        float p = 1.0f - t;
        return 1.0f - p * p * p;
    }

    public static void onKeyboardEvent(KeyEvent event) {
        if (event != null) {
            lastInputModifiers = event.modifiers();
        }
    }

    private static boolean isShiftDown() {
        if ((lastInputModifiers & 0x03) != 0) {
            return true;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getWindow() == null) {
            return false;
        }
        return InputConstants.isKeyDown(InputConstants.KEY_LSHIFT)
                || InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
    }

    private static Style hintStyle(NbtviewerConfig cfg) {
        return NbtTooltipHandler.styleFromRgb(NbtviewerConfig.parseRgb(cfg.colorHint, 0xDADADA));
    }

    private static Component createOpenNotebookHint(NbtviewerConfig cfg) {
        return Component.translatable((String)"nbtviewer.tooltip.open_entity_nbt", (Object[])new Object[]{OPEN_MOB_NOTEBOOK_KEY.getTranslatedKeyMessage()}).setStyle(NbtTooltipHandler.hintStyle(cfg));
    }

    private static Component createLockEntityHint(NbtviewerConfig cfg, boolean locked) {
        return Component.translatable((String)(locked ? "nbtviewer.tooltip.unlock_entity_nbt" : "nbtviewer.tooltip.lock_entity_nbt"), (Object[])new Object[]{LOCK_MOB_TARGET_KEY.getTranslatedKeyMessage()}).setStyle(NbtTooltipHandler.hintStyle(cfg));
    }

    private static Style styleFromRgb(int rgb) {
        return Style.EMPTY.withColor(TextColor.fromRgb((int)(rgb & 0xFFFFFF)));
    }

    private static float computeScale(NbtviewerConfig cfg, int lineCount, int totalChars) {
        if (!cfg.autoScale) {
            return (float)cfg.nbtScale;
        }
        float s = lineCount <= 4 && totalChars <= 180 ? 1.08f : (lineCount <= 7 && totalChars <= 320 ? 1.0f : (lineCount <= 10 && totalChars <= 520 ? 0.92f : (lineCount <= 14 && totalChars <= 800 ? 0.85f : (lineCount <= 18 && totalChars <= 1200 ? 0.8f : 0.75f))));
        return s;
    }

    private static /* synthetic */ CompoundTag lambda$getIntegratedServerEntityTag$0(MinecraftServer server, Entity entity) {
        ServerLevel serverLevel = server.getLevel(entity.level().dimension());
        if (serverLevel == null) {
            return null;
        }
        Entity serverEntity = serverLevel.getEntity(entity.getId());
        if (serverEntity == null || serverEntity.isRemoved()) {
            return null;
        }
        return NbtPredicate.getEntityTagToCompare((Entity)serverEntity);
    }

    private record EntityOverlayData(List<Component> lines, float scale) {
    }

    private record PreparedOverlayLines(List<FormattedCharSequence> lines, int textWidth) {
    }
}
