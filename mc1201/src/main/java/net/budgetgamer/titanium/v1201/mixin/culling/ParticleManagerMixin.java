package net.budgetgamer.titanium.v1201.mixin.culling;

import net.budgetgamer.titanium.core.CullingEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.minecraft.client.particle.ParticleManager")
public class ParticleManagerMixin {

    @Inject(
        method = "addParticle(Lnet/minecraft/client/particle/Particle;)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 0
    )
    private void onAddParticle(Object particle, CallbackInfo ci) {
        if (particle == null) {
            return;
        }

        try {
            java.lang.reflect.Field xField = particle.getClass().getField("x");
            java.lang.reflect.Field yField = particle.getClass().getField("y");
            java.lang.reflect.Field zField = particle.getClass().getField("z");

            double px = xField.getDouble(particle);
            double py = yField.getDouble(particle);
            double pz = zField.getDouble(particle);

            if (CullingEngine.shouldCullParticle(px, py, pz)) {
                ci.cancel();
            }
        } catch (Throwable ignored) {
        }
    }
}
