package net.budgetgamer.titanium.v1171.mixin.culling;

import net.budgetgamer.titanium.core.CullingEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher")
public class BlockEntityRenderDispatcherMixin {

    @Inject(
        method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 0
    )
    private void onRenderBlockEntity(
            Object blockEntity,
            float tickDelta,
            Object matrices,
            Object vertexConsumers,
            CallbackInfo ci
    ) {
        if (blockEntity == null) {
            return;
        }

        try {
            String className = blockEntity.getClass().getSimpleName().toLowerCase();
            CullingEngine.BlockEntityType type = CullingEngine.BlockEntityType.OTHER;

            if (className.contains("chest")) {
                type = CullingEngine.BlockEntityType.CHEST;
            } else if (className.contains("hopper") || className.contains("shulker")) {
                type = CullingEngine.BlockEntityType.HOPPER_SHULKER;
            } else if (className.contains("sign") || className.contains("banner")) {
                type = CullingEngine.BlockEntityType.SIGN_BANNER;
            } else if (className.contains("beacon") || className.contains("bell")) {
                type = CullingEngine.BlockEntityType.BEACON_BELL;
            }

            java.lang.reflect.Method getPosMethod = blockEntity.getClass().getMethod("getPos");
            Object pos = getPosMethod.invoke(blockEntity);
            if (pos != null) {
                java.lang.reflect.Method getXMethod = pos.getClass().getMethod("getX");
                java.lang.reflect.Method getYMethod = pos.getClass().getMethod("getY");
                java.lang.reflect.Method getZMethod = pos.getClass().getMethod("getZ");

                double x = ((Number) getXMethod.invoke(pos)).doubleValue();
                double y = ((Number) getYMethod.invoke(pos)).doubleValue();
                double z = ((Number) getZMethod.invoke(pos)).doubleValue();

                if (CullingEngine.shouldCullBlockEntity(x, y, z, x + 1.0, y + 1.0, z + 1.0, type)) {
                    ci.cancel();
                }
            }
        } catch (Throwable ignored) {
        }
    }
}
