/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.inventory.tooltip.TooltipComponent
 */
package org.hohigamer.nbtviewer.client.tooltip;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record NbtViewerTooltipData(List<Component> lines, float scale) implements TooltipComponent
{
}
