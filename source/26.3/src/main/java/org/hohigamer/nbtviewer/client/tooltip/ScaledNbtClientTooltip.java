/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.util.Mth
 *  org.joml.Matrix3x2fStack
 */
package org.hohigamer.nbtviewer.client.tooltip;

import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.Mth;
import org.hohigamer.nbtviewer.client.tooltip.NbtViewerTooltipData;
import org.joml.Matrix3x2fStack;

public final class ScaledNbtClientTooltip
implements ClientTooltipComponent {
    private final List<Component> lines;
    private final float scale;

    public ScaledNbtClientTooltip(NbtViewerTooltipData data) {
        this.lines = data.lines();
        this.scale = data.scale();
    }

    public int getWidth(Font font) {
        int max = 0;
        for (Component c : this.lines) {
            max = Math.max(max, font.width((FormattedText)c));
        }
        return Mth.ceil((float)((float)max * this.scale));
    }

    public int getHeight(Font font) {
        if (this.lines.isEmpty()) {
            return 0;
        }
        int gap = 1;
        Objects.requireNonNull(font);
        int base = 9 * this.lines.size() + gap * (this.lines.size() - 1);
        return Mth.ceil((float)((float)base * this.scale));
    }

    public void extractText(GuiGraphicsExtractor gg, Font font, int x, int y) {
        Matrix3x2fStack pose = gg.pose();
        pose.pushMatrix();
        pose.translate((float)x, (float)y);
        pose.scale(this.scale, this.scale);
        int dy = 0;
        int gap = 1;
        for (Component c : this.lines) {
            gg.text(font, c, 0, dy, -1, true);
            Objects.requireNonNull(font);
            dy += 9 + gap;
        }
        pose.popMatrix();
    }
}
