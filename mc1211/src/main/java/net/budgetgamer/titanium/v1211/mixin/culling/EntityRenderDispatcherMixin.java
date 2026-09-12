package net.budgetgamer.titanium.v1211.mixin.culling;

import net.budgetgamer.titanium.core.CullingEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.minecraft.client.render.entity.EntityRenderDispatcher")
public class EntityRenderDispatcherMixin {

    @Inject(
        method = "render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V",
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
            String className = entity.getClass().getSimpleName().toLowerCase();

            if (className.contains("dragon") || className.contains("wither")) {
                return;
            }

            try {
                java.lang.reflect.Method isGlowing = entity.getClass().getMethod("isGlowing");
                if (Boolean.TRUE.equals(isGlowing.invoke(entity))) {
                    return;
                }
            } catch (Throwable ignored) {}

            boolean isMonster = className.contains("monster") || className.contains("zombie") || className.contains("skeleton") || className.contains("creeper") || className.contains("spider");
            boolean isAnimal = className.contains("animal") || className.contains("cow") || className.contains("sheep") || className.contains("pig") || className.contains("chicken") || className.contains("horse");
            boolean isItem = className.contains("itementity");
            boolean isFrame = className.contains("itemframe");
            boolean isStand = className.contains("armorstand");

            if (isFrame || className.contains("painting")) {
                try {
                    java.lang.reflect.Method getFacing = null;
                    try {
                        getFacing = entity.getClass().getMethod("getHorizontalFacing");
                    } catch (NoSuchMethodException e) {
                        getFacing = entity.getClass().getMethod("getFacing");
                    }
                    if (getFacing != null) {
                        Object dir = getFacing.invoke(entity);
                        if (dir != null) {
                            java.lang.reflect.Method getOffsetX = dir.getClass().getMethod("getOffsetX");
                            java.lang.reflect.Method getOffsetY = dir.getClass().getMethod("getOffsetY");
                            java.lang.reflect.Method getOffsetZ = dir.getClass().getMethod("getOffsetZ");
                            double nx = ((Number) getOffsetX.invoke(dir)).doubleValue();
                            double ny = ((Number) getOffsetY.invoke(dir)).doubleValue();
                            double nz = ((Number) getOffsetZ.invoke(dir)).doubleValue();
                            if (CullingEngine.isBackfaceCulled(x, y, z, nx, ny, nz)) {
                                ci.cancel();
                                return;
                            }
                        }
                    }
                } catch (Throwable ignored) {}
            }

            double minX = x - 1.0;
            double minY = y;
            double minZ = z - 1.0;
            double maxX = x + 1.0;
            double maxY = y + 2.0;
            double maxZ = z + 1.0;

            try {
                java.lang.reflect.Method getBox = entity.getClass().getMethod("getBoundingBox");
                Object box = getBox.invoke(entity);
                if (box != null) {
                    Class<?> bCls = box.getClass();
                    minX = bCls.getField("minX").getDouble(box);
                    minY = bCls.getField("minY").getDouble(box);
                    minZ = bCls.getField("minZ").getDouble(box);
                    maxX = bCls.getField("maxX").getDouble(box);
                    maxY = bCls.getField("maxY").getDouble(box);
                    maxZ = bCls.getField("maxZ").getDouble(box);
                }
            } catch (Throwable ignored) {}

            if (CullingEngine.shouldCullEntity(minX, minY, minZ, maxX, maxY, maxZ, isMonster, isAnimal, isItem, isFrame, isStand)) {
                ci.cancel();
            }
        } catch (Throwable ignored) {
        }
    }
}
