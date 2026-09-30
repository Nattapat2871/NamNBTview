/*
 * SPDX-License-Identifier: MIT
 * Author: nattapat2871 (https://nattapat2871.me)
 */
package org.hohigamer.nbtviewer.mixin;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.hohigamer.nbtviewer.client.NbtTooltipHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackTooltipLinesMixin {
    @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
    private void nbtviewer$appendNbtTooltip(
            Item.TooltipContext context,
            Player player,
            TooltipFlag flag,
            CallbackInfoReturnable<List<Component>> cir
    ) {
        ItemStack self = (ItemStack) (Object) this;
        cir.setReturnValue(NbtTooltipHandler.appendItemNbtTooltip(self, context, cir.getReturnValue()));
    }
}
