package net.typho.datamod.mixin;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.typho.datamod.RegistryReferenceAccessor;
import org.spongepowered.asm.mixin.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(RegistryEntry.Reference.class)
@Implements(@Interface(
        iface = RegistryReferenceAccessor.class,
        prefix = "datamod$"
))
public abstract class RegistryEntryReferenceMixin<T> {
    @Unique
    private final Set<TagKey<T>> extraTags = new HashSet<>();

    @Shadow
    protected abstract void setValue(T value);

    @Shadow private Set<TagKey<T>> tags;

    public Set<TagKey<T>> datamod$getExtraTags() {
        return extraTags;
    }

    @Intrinsic
    public void datamod$setValue(T value) {
        setValue(value);
    }

    /**
     * @author The Typhothanian
     * @reason Extra tags
     */
    @Overwrite
    public boolean isIn(TagKey<T> tag) {
        return tags.contains(tag) || extraTags.contains(tag);
    }

    /**
     * @author The Typhothanian
     * @reason Extra tags
     */
    @Overwrite
    public Stream<TagKey<T>> streamTags() {
        return Stream.concat(tags.stream(), extraTags.stream());
    }
}
