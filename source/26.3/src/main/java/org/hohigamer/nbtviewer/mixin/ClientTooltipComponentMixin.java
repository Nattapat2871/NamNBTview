/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
 *  net.minecraft.world.inventory.tooltip.TooltipComponent
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package org.hohigamer.nbtviewer.mixin;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.hohigamer.nbtviewer.client.tooltip.NbtViewerTooltipData;
import org.hohigamer.nbtviewer.client.tooltip.ScaledNbtClientTooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientTooltipComponent.class})
public interface ClientTooltipComponentMixin {
    @Inject(method={"create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;"}, at={@At(value="HEAD")}, cancellable=true)
    private static void nbtviewer$createCustom(TooltipComponent component, CallbackInfoReturnable<ClientTooltipComponent> cir) {
        if (component instanceof NbtViewerTooltipData) {
            NbtViewerTooltipData data = (NbtViewerTooltipData)component;
            cir.setReturnValue(new ScaledNbtClientTooltip(data));
        }
    }
}
