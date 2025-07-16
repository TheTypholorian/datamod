package net.typho.datamod.mixin;

import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Registries.class)
public class RegistriesMixin {
    /**
     * @author The Typhothanian
     * @reason Mutable registries
     */
    @Overwrite
    private static void freezeRegistries() {
    }
}
