package net.budgetgamer.sodiumimprover.v1211.mixin.culling;

import net.budgetgamer.sodiumimprover.core.CullingEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.minecraft.client.render.entity.EntityRenderDispatcher")
public class EntityRenderDispatcherMixin {

    @Inject(
        method = "render",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 0
    )
    private void onRenderEntity(
            Object entity,
            double x, double y, double z,
            float yaw, float tickDelta,
            Object matrices,
            Object vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (entity == null) {
            return;
        }

        try {
            double minX = x - 1.0;
            double minY = y;
            double minZ = z - 1.0;
            double maxX = x + 1.0;
            double maxY = y + 2.0;
            double maxZ = z + 1.0;

            String className = entity.getClass().getSimpleName().toLowerCase();
            boolean isMonster = className.contains("monster") || className.contains("zombie") || className.contains("skeleton") || className.contains("creeper");
            boolean isAnimal = className.contains("animal") || className.contains("cow") || className.contains("sheep") || className.contains("pig") || className.contains("chicken");
            boolean isItem = className.contains("itementity");
            boolean isFrame = className.contains("itemframe");
            boolean isStand = className.contains("armorstand");

            if (CullingEngine.shouldCullEntity(minX, minY, minZ, maxX, maxY, maxZ, isMonster, isAnimal, isItem, isFrame, isStand)) {
                ci.cancel();
            }
        } catch (Throwable ignored) {
        }
    }
}
