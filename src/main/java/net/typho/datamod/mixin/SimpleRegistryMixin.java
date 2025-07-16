package net.typho.datamod.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.typho.datamod.RegistryReferenceAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleRegistry.class)
public class SimpleRegistryMixin<T> {
    @Inject(
            method = "add",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 2
            )
    )
    @SuppressWarnings("unchecked")
    private void add(RegistryKey<T> key, T value, RegistryEntryInfo info, CallbackInfoReturnable<RegistryEntry.Reference<T>> cir, @Local RegistryEntry.Reference<T> reference) {
        ((RegistryReferenceAccessor<T>) reference).setValue(value);
    }
}
