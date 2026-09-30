/*
 * SPDX-License-Identifier: MIT
 * Author: nattapat2871 (https://nattapat2871.me)
 */
package org.hohigamer.nbtviewer.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.hohigamer.nbtviewer.client.NbtTooltipHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void nbtviewer$trackModifiers(long window, int action, KeyEvent event, CallbackInfo ci) {
        NbtTooltipHandler.onKeyboardEvent(event);
    }
}
