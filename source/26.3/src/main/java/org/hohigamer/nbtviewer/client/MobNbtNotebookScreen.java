/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.input.MouseButtonEvent
 *  net.minecraft.network.chat.CommonComponents
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.util.Mth
 */
package org.hohigamer.nbtviewer.client;

import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.hohigamer.nbtviewer.NbtviewerConfig;
import org.hohigamer.nbtviewer.client.SnbtHighlighter;

public final class MobNbtNotebookScreen
extends Screen {
    private static final int OPEN_ANIM_MS = 180;
    private static final int PANEL_MARGIN = 28;
    private static final int PANEL_INSET = 16;
    private static final int HEADER_HEIGHT = 44;
    private static final int FOOTER_HEIGHT = 38;
    private static final int LINE_GAP = 1;
    private static final int SCROLL_STEP = 3;
    private static final int BUTTON_WIDTH = 110;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_GAP = 8;
    private static final int SCROLLBAR_WIDTH = 7;
    private static final int SCROLLBAR_GAP = 5;
    private static final int THUMB_MIN_HEIGHT = 18;
    private static final int OUTER_BG = -267053285;
    private static final int OUTER_BORDER = -12695982;
    private static final int HEADER_BG = -14670805;
    private static final int CONTENT_BG = -15592425;
    private static final int CONTENT_BORDER = -13814981;
    private static final int FOOTER_BG = -15065565;
    private static final int SUBTLE_LINE = 572492400;
    private static final int TEXT_PRIMARY = -789258;
    private static final int TEXT_SECONDARY = -6050888;
    private static final int ACCENT = -11090011;
    private static final int SCROLL_TRACK = -14341583;
    private static final int SCROLL_THUMB = -10853262;
    private static final int BUTTON_SHADOW = -2146429928;
    private static final int BUTTON_BG = -14670291;
    private static final int BUTTON_BG_HOVER = -14208968;
    private static final int BUTTON_BORDER = -12958640;
    private static final int BUTTON_BORDER_HOVER = -11090011;
    private static final int BUTTON_INNER_LINE = 572492400;
    private static final int BUTTON_TEXT = -1511950;
    private static final int INFO_ICON_SIZE = 14;
    private static final int INFO_ICON_BG = -14670291;
    private static final int INFO_ICON_BG_HOVER = -14208968;
    private static final int INFO_ICON_TEXT = -6431541;
    private final Component mobName;
    private final String snbt;
    private final NbtviewerConfig config;
    private int panelLeft;
    private int panelTop;
    private int panelWidth;
    private int panelHeight;
    private int contentLeft;
    private int contentTop;
    private int contentWidth;
    private int contentHeight;
    private int visibleLineCount;
    private int scrollLine;
    private int maxScrollLine;
    private long openedAtMs;
    private long copiedNoticeUntilMs;
    private boolean draggingScrollbar;
    private int scrollbarDragOffset;
    private Button copyButton;
    private Button doneButton;
    private List<Component> formattedLines = List.of();

    public MobNbtNotebookScreen(Component mobName, String snbt, NbtviewerConfig config) {
        super((Component)Component.translatable((String)"nbtviewer.screen.mob_notebook", (Object[])new Object[]{mobName}));
        this.mobName = mobName;
        this.snbt = snbt;
        this.config = config;
    }

    protected void init() {
        super.init();
        this.clearWidgets();
        this.panelWidth = Math.max(360, this.width - 56);
        this.panelHeight = Math.max(220, this.height - 56);
        this.panelLeft = (this.width - this.panelWidth) / 2;
        this.panelTop = (this.height - this.panelHeight) / 2;
        this.contentLeft = this.panelLeft + 16;
        this.contentTop = this.panelTop + 44 + 8;
        this.contentWidth = this.panelWidth - 32 - 7 - 5;
        this.contentHeight = this.panelHeight - 44 - 38 - 16;
        this.openedAtMs = System.currentTimeMillis();
        this.rebuildFormattedLines();
        int buttonY = this.panelTop + this.panelHeight - 38 + 9;
        int rightEdge = this.panelLeft + this.panelWidth - 16;
        this.copyButton = this.addRenderableWidget(Button.builder(Component.translatable("nbtviewer.button.copy_entity_nbt"), button -> this.copyFullNbt()).bounds(rightEdge - 110, buttonY, 110, 20).build());
        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).bounds(rightEdge - 220 - 8, buttonY, 110, 20).build());
    }

    private void rebuildFormattedLines() {
        int charBudget = Math.max(70, (this.contentWidth - 20) / 6);
        this.formattedLines = SnbtHighlighter.format(this.snbt, false, charBudget, this.config.indentSpaces, Math.max(this.snbt.length(), this.config.maxTotalChars), new SnbtHighlighter.Colors(NbtviewerConfig.parseRgb(this.config.colorLabel, 0xAAAAAA), NbtviewerConfig.parseRgb(this.config.colorKey, 0x55FFFF), NbtviewerConfig.parseRgb(this.config.colorString, 0x55FF55), NbtviewerConfig.parseRgb(this.config.colorNumber, 0xFFAA00), NbtviewerConfig.parseRgb(this.config.colorBoolean, 0xFF55FF), NbtviewerConfig.parseRgb(this.config.colorPunctuation, 0xFFFFFF), NbtviewerConfig.parseRgb(this.config.colorIdentifier, 0xFFFFFF)));
        Objects.requireNonNull(this.font);
        int lineHeight = 9 + 1;
        this.visibleLineCount = Math.max(1, this.contentHeight / lineHeight);
        this.maxScrollLine = Math.max(0, this.formattedLines.size() - this.visibleLineCount);
        this.scrollLine = Math.max(0, Math.min(this.scrollLine, this.maxScrollLine));
    }

    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.extractTransparentBackground(guiGraphics);
        float openProgress = this.getOpenProgress();
        float alpha = 0.35f + openProgress * 0.65f;
        int yOffset = Math.round((1.0f - openProgress) * 12.0f);
        this.renderPanel(guiGraphics, mouseX, mouseY, yOffset, alpha);
        this.renderStyledButtons(guiGraphics, mouseX, mouseY, yOffset, alpha);
    }

    private float getOpenProgress() {
        if (this.openedAtMs <= 0L) {
            return 1.0f;
        }
        float t = (float)(System.currentTimeMillis() - this.openedAtMs) / 180.0f;
        t = Mth.clamp((float)t, (float)0.0f, (float)1.0f);
        float p = 1.0f - t;
        return 1.0f - p * p * p;
    }

    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.gui.setScreen(null);
        }
    }

    private void renderPanel(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int yOffset, float alpha) {
        int panelRight = this.panelLeft + this.panelWidth;
        int panelTop = this.panelTop + yOffset;
        int panelBottom = panelTop + this.panelHeight;
        int contentRight = this.contentLeft + this.contentWidth;
        int contentTop = this.contentTop + yOffset;
        int contentBottom = contentTop + this.contentHeight;
        guiGraphics.fill(this.panelLeft - 2, panelTop - 2, panelRight + 2, panelBottom + 2, MobNbtNotebookScreen.applyAlpha(-1442840576, alpha));
        guiGraphics.fill(this.panelLeft, panelTop, panelRight, panelBottom, MobNbtNotebookScreen.applyAlpha(-267053285, alpha));
        guiGraphics.fill(this.panelLeft, panelTop, panelRight, panelTop + 44, MobNbtNotebookScreen.applyAlpha(-14670805, alpha));
        guiGraphics.fill(this.panelLeft, panelBottom - 38, panelRight, panelBottom, MobNbtNotebookScreen.applyAlpha(-15065565, alpha));
        MobNbtNotebookScreen.drawBorder(guiGraphics, this.panelLeft, panelTop, panelRight, panelBottom, MobNbtNotebookScreen.applyAlpha(-12695982, alpha));
        guiGraphics.fill(this.contentLeft, contentTop, contentRight, contentBottom, MobNbtNotebookScreen.applyAlpha(-15592425, alpha));
        MobNbtNotebookScreen.drawBorder(guiGraphics, this.contentLeft, contentTop, contentRight, contentBottom, MobNbtNotebookScreen.applyAlpha(-13814981, alpha));
        int headerTextX = this.panelLeft + 16;
        guiGraphics.text(this.font, (Component)Component.translatable((String)"nbtviewer.screen.mob_notebook_header"), headerTextX, panelTop + 9, MobNbtNotebookScreen.applyAlpha(-6050888, alpha), false);
        guiGraphics.text(this.font, this.mobName, headerTextX, panelTop + 21, MobNbtNotebookScreen.applyAlpha(-789258, alpha), false);
        this.renderInfoIcon(guiGraphics, mouseX, mouseY, panelTop, alpha);
        if (System.currentTimeMillis() < this.copiedNoticeUntilMs) {
            MutableComponent copied = Component.translatable((String)"nbtviewer.screen.copied");
            guiGraphics.text(this.font, (Component)copied, headerTextX, panelBottom - 38 + 12, MobNbtNotebookScreen.applyAlpha(-11090011, alpha), false);
        }
        this.renderContent(guiGraphics, contentTop, contentRight, contentBottom, alpha);
        this.renderScrollbar(guiGraphics, contentTop, contentRight, contentBottom, alpha);
    }

    private void renderContent(GuiGraphicsExtractor guiGraphics, int contentTop, int contentRight, int contentBottom, float alpha) {
        for (int y = contentTop + 1; y < contentBottom; y += 9 + 1) {
            Objects.requireNonNull(this.font);
            Objects.requireNonNull(this.font);
            guiGraphics.fill(this.contentLeft + 1, y + 9, contentRight - 1, y + 9 + 1, MobNbtNotebookScreen.applyAlpha(572492400, alpha));
            Objects.requireNonNull(this.font);
        }
        guiGraphics.enableScissor(this.contentLeft + 1, contentTop + 1, contentRight - 1, contentBottom - 1);
        Objects.requireNonNull(this.font);
        int lineHeight = 9 + 1;
        int end = Math.min(this.formattedLines.size(), this.scrollLine + this.visibleLineCount);
        int y = contentTop + 8;
        for (int i = this.scrollLine; i < end; ++i) {
            guiGraphics.text(this.font, this.formattedLines.get(i), this.contentLeft + 10, y, MobNbtNotebookScreen.applyAlpha(-1, alpha), false);
            y += lineHeight;
        }
        guiGraphics.disableScissor();
    }

    private void renderScrollbar(GuiGraphicsExtractor guiGraphics, int contentTop, int contentRight, int contentBottom, float alpha) {
        if (this.maxScrollLine <= 0) {
            return;
        }
        int trackLeft = this.getScrollbarLeft();
        int trackRight = this.getScrollbarRight();
        guiGraphics.fill(trackLeft, contentTop, trackRight, contentBottom, MobNbtNotebookScreen.applyAlpha(-14341583, alpha));
        MobNbtNotebookScreen.drawBorder(guiGraphics, trackLeft, contentTop, trackRight, contentBottom, MobNbtNotebookScreen.applyAlpha(-13814981, alpha));
        int thumbTop = this.getThumbTop() + (contentTop - this.contentTop);
        int thumbBottom = thumbTop + this.getThumbHeight();
        guiGraphics.fill(trackLeft + 1, thumbTop, trackRight - 1, thumbBottom, MobNbtNotebookScreen.applyAlpha(-10853262, alpha));
    }

    private void renderInfoIcon(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int panelTop, float alpha) {
        int left = this.panelLeft + this.panelWidth - 16 - 14;
        int top = panelTop + 10;
        int right = left + 14;
        int bottom = top + 14;
        boolean hovered = mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
        guiGraphics.fill(left, top + 1, right, bottom + 1, MobNbtNotebookScreen.applyAlpha(-2146429928, alpha));
        guiGraphics.fill(left, top, right, bottom, MobNbtNotebookScreen.applyAlpha(hovered ? -14208968 : -14670291, alpha));
        MobNbtNotebookScreen.drawBorder(guiGraphics, left, top, right, bottom, MobNbtNotebookScreen.applyAlpha(hovered ? -11090011 : -12958640, alpha));
        guiGraphics.centeredText(this.font, (Component)Component.literal((String)"i"), left + 7, top + 3, MobNbtNotebookScreen.applyAlpha(-6431541, alpha));
        if (hovered) {
            guiGraphics.setComponentTooltipForNextFrame(this.font, List.of(Component.translatable((String)"nbtviewer.screen.info_icon.line1"), Component.translatable((String)"nbtviewer.screen.info_icon.line2")), mouseX, mouseY);
        }
    }

    private void renderStyledButtons(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int yOffset, float alpha) {
        this.renderStyledButton(guiGraphics, this.doneButton, mouseX, mouseY, yOffset, alpha);
        this.renderStyledButton(guiGraphics, this.copyButton, mouseX, mouseY, yOffset, alpha);
    }

    private void renderStyledButton(GuiGraphicsExtractor guiGraphics, Button button, int mouseX, int mouseY, int yOffset, float alpha) {
        if (button == null || !button.visible) {
            return;
        }
        int left = button.getX();
        int top = button.getY() + yOffset;
        int right = left + button.getWidth();
        int bottom = top + button.getHeight();
        boolean hovered = button.active && button.isMouseOver((double)mouseX, (double)mouseY);
        int borderColor = hovered ? -11090011 : -12958640;
        int backgroundColor = hovered ? -14208968 : -14670291;
        int textColor = button.active ? -1511950 : -6050888;
        guiGraphics.fill(left, top + 1, right, bottom + 1, MobNbtNotebookScreen.applyAlpha(-2146429928, alpha));
        guiGraphics.fill(left, top, right, bottom, MobNbtNotebookScreen.applyAlpha(backgroundColor, alpha));
        MobNbtNotebookScreen.drawBorder(guiGraphics, left, top, right, bottom, MobNbtNotebookScreen.applyAlpha(borderColor, alpha));
        guiGraphics.fill(left + 1, top + 1, right - 1, top + 2, MobNbtNotebookScreen.applyAlpha(572492400, alpha));
        guiGraphics.fill(left + 1, bottom - 2, right - 1, bottom - 1, MobNbtNotebookScreen.applyAlpha(0x18000000, alpha));
        Component component = button.getMessage();
        int n = left + button.getWidth() / 2;
        int n2 = button.getHeight();
        Objects.requireNonNull(this.font);
        guiGraphics.centeredText(this.font, component, n, top + (n2 - 9) / 2 + 1, MobNbtNotebookScreen.applyAlpha(textColor, alpha));
    }

    private static int applyAlpha(int color, float alphaMul) {
        int alpha = color >>> 24 & 0xFF;
        if (alpha == 0 && (color & 0xFFFFFF) != 0) {
            alpha = 255;
        }
        int scaledAlpha = Mth.clamp((int)Math.round((float)alpha * alphaMul), (int)0, (int)255);
        return scaledAlpha << 24 | color & 0xFFFFFF;
    }

    private int getScrollbarLeft() {
        return this.contentLeft + this.contentWidth + 5;
    }

    private int getScrollbarRight() {
        return this.getScrollbarLeft() + 7;
    }

    private int getThumbHeight() {
        int trackHeight = this.contentHeight;
        return Math.max(18, (int)((float)this.visibleLineCount / (float)this.formattedLines.size() * (float)trackHeight));
    }

    private int getThumbTop() {
        int thumbHeight = this.getThumbHeight();
        int trackHeight = this.contentHeight;
        int maxThumbOffset = Math.max(0, trackHeight - thumbHeight);
        if (this.maxScrollLine <= 0 || maxThumbOffset <= 0) {
            return this.contentTop;
        }
        return this.contentTop + Math.round((float)this.scrollLine / (float)this.maxScrollLine * (float)maxThumbOffset);
    }

    private boolean isInsideScrollbar(double mouseX, double mouseY) {
        return mouseX >= (double)this.getScrollbarLeft() && mouseX <= (double)this.getScrollbarRight() && mouseY >= (double)this.contentTop && mouseY <= (double)(this.contentTop + this.contentHeight);
    }

    private void setScrollFromMouse(double mouseY) {
        int thumbHeight = this.getThumbHeight();
        int minTop = this.contentTop;
        int maxTop = this.contentTop + this.contentHeight - thumbHeight;
        int desiredTop = Mth.clamp((int)((int)Math.round(mouseY) - this.scrollbarDragOffset), (int)minTop, (int)maxTop);
        int maxThumbOffset = Math.max(1, maxTop - minTop);
        float progress = (float)(desiredTop - minTop) / (float)maxThumbOffset;
        this.scrollLine = Math.round(progress * (float)this.maxScrollLine);
    }

    private static void drawBorder(GuiGraphicsExtractor guiGraphics, int left, int top, int right, int bottom, int color) {
        guiGraphics.fill(left, top, right, top + 1, color);
        guiGraphics.fill(left, bottom - 1, right, bottom, color);
        guiGraphics.fill(left, top, left + 1, bottom, color);
        guiGraphics.fill(right - 1, top, right, bottom, color);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY > 0.0) {
            this.scrollLine = Math.max(0, this.scrollLine - 3);
            return true;
        }
        if (scrollY < 0.0) {
            this.scrollLine = Math.min(this.maxScrollLine, this.scrollLine + 3);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0 && this.maxScrollLine > 0 && this.isInsideScrollbar(event.x(), event.y())) {
            int thumbTop = this.getThumbTop();
            int thumbBottom = thumbTop + this.getThumbHeight();
            if (event.y() >= (double)thumbTop && event.y() <= (double)thumbBottom) {
                this.scrollbarDragOffset = (int)Math.round(event.y()) - thumbTop;
            } else {
                this.scrollbarDragOffset = this.getThumbHeight() / 2;
                this.setScrollFromMouse(event.y());
            }
            this.draggingScrollbar = true;
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (event.button() == 0 && this.draggingScrollbar && this.maxScrollLine > 0) {
            this.setScrollFromMouse(event.y());
            return true;
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == 0 && this.draggingScrollbar) {
            this.draggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(event);
    }

    private void copyFullNbt() {
        if (this.minecraft == null) {
            return;
        }
        this.minecraft.keyboardHandler.setClipboard(this.snbt);
        this.copiedNoticeUntilMs = System.currentTimeMillis() + 1500L;
    }
}
