package net.budgetgamer.sodiumimprover.v1206.mixin.gui;

import net.budgetgamer.sodiumimprover.v1206.rso.SodiumPageBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI", remap = false)
public class Sodium06OptionsGUIMixin {

    @Inject(method = "<init>", at = @At("RETURN"), remap = false, require = 0)
    private void onInit(CallbackInfo ci) {
        SodiumPageBuilder.injectImproverPage(this);
    }
}
