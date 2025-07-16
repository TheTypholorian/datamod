package net.typho.datamod.mixin;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Optional;
import java.util.stream.Stream;

@Mixin(DynamicRegistryManager.class)
public interface DynamicRegistryManagerMixin {
    @Shadow
    Stream<DynamicRegistryManager.Entry<?>> streamAllRegistries();

    /**
     * @author The Typhothanian
     * @reason Mutable registries
     */
    @Overwrite
    default DynamicRegistryManager.Immutable toImmutable() {
        class Immutablized extends DynamicRegistryManager.ImmutableImpl implements DynamicRegistryManager.Immutable {
            protected Immutablized(final Stream<DynamicRegistryManager.Entry<?>> entryStream) {
                super(entryStream);
            }
        }

        return new Immutablized(streamAllRegistries());
    }
}
