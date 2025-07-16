package net.typho.datamod;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.*;

public class DynamicDefaultedRegistry<T> extends SimpleDefaultedRegistry<T> {
    public DynamicDefaultedRegistry(String defaultId, RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, boolean intrusive) {
        super(defaultId, key, lifecycle, intrusive);
    }

    /*
    public final Identifier defaultId;
    private RegistryEntry.Reference<T> defaultEntry;
    final RegistryKey<? extends Registry<T>> key;
    private Lifecycle lifecycle;
    private final Map<RegistryKey<T>, T> map = new HashMap<>();
    private final Map<RegistryEntry.Reference<T>, Integer> references = new HashMap<>();
    private int id = 0;

    public DynamicDefaultedRegistry(String defaultId, RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, boolean intrusive) {
        this.defaultId = Identifier.of(defaultId);
        this.key = key;
        this.lifecycle = lifecycle;
    }

    @Override
    public RegistryKey<? extends Registry<T>> getKey() {
        return null;
    }

    @Override
    public @NotNull Identifier getId(T value) {
        for (RegistryEntry.Reference<T> ref : references.keySet()) {
            if (ref.value() == value) {
                return ref.registryKey().getValue();
            }
        }

        return defaultId;
    }

    @Override
    public Optional<RegistryKey<T>> getKey(T entry) {
        for (RegistryEntry.Reference<T> ref : references.keySet()) {
            if (ref.value() == entry) {
                return Optional.of(ref.registryKey());
            }
        }

        return Optional.of(defaultEntry.registryKey());
    }

    @Override
    public int getRawId(@Nullable T value) {
        for (Map.Entry<RegistryEntry.Reference<T>, Integer> entry : references.entrySet()) {
            if (entry.getKey().value() == value) {
                return entry.getValue();
            }
        }

        return -1;
    }

    @Override
    public @Nullable T get(@Nullable RegistryKey<T> key) {
        return map.get(key);
    }

    @Override
    public @NotNull T get(@Nullable Identifier id) {
        for (Map.Entry<RegistryKey<T>, T> entry : map.entrySet()) {
            if (entry.getKey().getValue().equals(id)) {
                return entry.getValue();
            }
        }

        return defaultEntry.value();
    }

    @Override
    public Optional<RegistryEntryInfo> getEntryInfo(RegistryKey<T> key) {
        return Optional.empty();
    }

    @Override
    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    @Override
    public Optional<RegistryEntry.Reference<T>> getDefaultEntry() {
        return Optional.empty();
    }

    @Override
    public Set<Identifier> getIds() {
        return map.keySet().stream().map(RegistryKey::getValue).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Map.Entry<RegistryKey<T>, T>> getEntrySet() {
        return map.entrySet();
    }

    @Override
    public Set<RegistryKey<T>> getKeys() {
        return map.keySet();
    }

    @Override
    public Optional<RegistryEntry.Reference<T>> getRandom(Random random) {
        return Optional.empty();
    }

    @Override
    public boolean containsId(Identifier id) {
        return map.keySet().stream().anyMatch(key -> key.getValue().equals(id));
    }

    @Override
    public boolean contains(RegistryKey<T> key) {
        return map.containsKey(key);
    }

    @Override
    public Registry<T> freeze() {
        return this;
    }

    @Override
    @SuppressWarnings("deprecation")
    public RegistryEntry.Reference<T> createEntry(T value) {
        for (RegistryEntry.Reference<T> entry : references.keySet()) {
            if (entry.value() == value) {
                return entry;
            }
        }

        RegistryEntry.Reference<T> ref = RegistryEntry.Reference.intrusive(getReadOnlyWrapper(), value);
        references.put(ref, id++);
        return ref;
    }

    @Override
    public Optional<RegistryEntry.Reference<T>> getEntry(int rawId) {
        return references.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == rawId)
                .findAny()
                .map(Map.Entry::getKey);
    }

    @Override
    public Optional<RegistryEntry.Reference<T>> getEntry(Identifier id) {
        return references.entrySet()
                .stream()
                .filter(entry -> entry.getKey().registryKey().getValue().equals(id))
                .findAny()
                .map(Map.Entry::getKey);
    }

    @Override
    public Optional<RegistryEntry.Reference<T>> getEntry(RegistryKey<T> key) {
        return references.entrySet()
                .stream()
                .filter(entry -> entry.getKey().registryKey().equals(key))
                .findAny()
                .map(Map.Entry::getKey);
    }

    @Override
    public RegistryEntry<T> getEntry(T value) {
        return references.entrySet()
                .stream()
                .filter(entry -> entry.getKey().value() == value)
                .findAny()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public Stream<RegistryEntry.Reference<T>> streamEntries() {
        return references.keySet().stream();
    }

    @Override
    public Optional<RegistryEntryList.Named<T>> getEntryList(TagKey<T> tag) {
        return Optional.empty();
    }

    @Override
    public RegistryEntryList.Named<T> getOrCreateEntryList(TagKey<T> tag) {
        return null;
    }

    @Override
    public Stream<Pair<TagKey<T>, RegistryEntryList.Named<T>>> streamTagsAndEntries() {
        return Stream.empty();
    }

    @Override
    public Stream<TagKey<T>> streamTags() {
        return Stream.empty();
    }

    @Override
    public void clearTags() {

    }

    @Override
    public void populateTags(Map<TagKey<T>, List<RegistryEntry<T>>> tagEntries) {

    }

    @Override
    public RegistryEntryOwner<T> getEntryOwner() {
        return null;
    }

    @Override
    public RegistryWrapper.Impl<T> getReadOnlyWrapper() {
        return null;
    }

    @Override
    public @NotNull T get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Identifier getDefaultId() {
        return null;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return null;
    }
     */
}
