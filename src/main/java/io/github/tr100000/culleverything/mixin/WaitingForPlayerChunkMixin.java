package io.github.tr100000.culleverything.mixin;

import net.minecraft.client.multiplayer.LevelLoadTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelLoadTracker.WaitingForPlayerChunk.class)
public abstract class WaitingForPlayerChunkMixin {
    @Inject(method = "isReady", at = @At("HEAD"), cancellable = true)
    private void isReady(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
