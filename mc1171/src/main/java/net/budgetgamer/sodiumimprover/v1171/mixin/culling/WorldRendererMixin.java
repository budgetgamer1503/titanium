package net.budgetgamer.sodiumimprover.v1171.mixin.culling;

import net.budgetgamer.sodiumimprover.core.CullingEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.minecraft.client.render.WorldRenderer")
public class WorldRendererMixin {

    @Inject(
        method = "render",
        at = @At("HEAD"),
        remap = false,
        require = 0
    )
    private void onRenderWorld(CallbackInfo ci) {
        CullingEngine.onFrameStart();
    }
}
