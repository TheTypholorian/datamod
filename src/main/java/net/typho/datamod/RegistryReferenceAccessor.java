package net.typho.datamod;

import net.minecraft.registry.tag.TagKey;

import java.util.Set;

public interface RegistryReferenceAccessor<T> {
    Set<TagKey<T>> getExtraTags();

    void setValue(T value);
}
