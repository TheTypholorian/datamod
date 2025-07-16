package net.typho.datamod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Unit;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class DataItemReloadListener implements IdentifiableResourceReloadListener, SynchronousResourceReloader {
    @Override
    public Identifier getFabricId() {
        return Identifier.of(DataMod.MOD_ID, "reload");
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        return synchronizer.whenPrepared(Unit.INSTANCE).thenRunAsync(() -> {
            applyProfiler.startTick();
            applyProfiler.push("listener");
            this.reload(manager);
            applyProfiler.pop();
            applyProfiler.endTick();
        }, applyExecutor);
    }

    public static void loadItems(ResourceManager manager) {
        Map<Identifier, Resource> resources = manager.findResources(
                "item",
                id -> id.getPath().endsWith(".json")
        );

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            String path = entry.getKey().getPath();
            Identifier id = Identifier.of(entry.getKey().getNamespace(), path.substring(5, path.length() - 5));
            JsonObject json;

            try (InputStreamReader reader = new InputStreamReader(entry.getValue().getInputStream())) {
                json = JsonHelper.deserialize(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            DataMod.loadItem(id, json);
        }
    }

    public static void loadBlocks(ResourceManager manager) {
        Map<Identifier, Resource> resources = manager.findResources(
                "block",
                id -> id.getPath().endsWith(".json")
        );

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            String path = entry.getKey().getPath();
            Identifier id = Identifier.of(entry.getKey().getNamespace(), path.substring(6, path.length() - 5));
            JsonObject json;

            try (InputStreamReader reader = new InputStreamReader(entry.getValue().getInputStream())) {
                json = JsonHelper.deserialize(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            DataMod.loadBlock(id, json);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void putTag(RegistryReferenceAccessor<T> accessor, TagKey<?> tag) {
        accessor.getExtraTags().add((TagKey<T>) tag);
    }

    public static void loadDynamicTags(ResourceManager manager) {
        Map<Identifier, List<Resource>> resources = manager.findAllResources(
                "tag",
                id -> id.getPath().endsWith("dynamic_tag.json")
        );

        System.out.println(resources);

        for (List<Resource> list : resources.values()) {
            System.out.println(list);

            for (Resource resource : list) {
                JsonObject json;

                try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
                    json = JsonHelper.deserialize(reader);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                    System.out.println(entry);
                    Registry<?> registry = Registries.REGISTRIES.get(Identifier.of(entry.getKey()));

                    if (registry == null) {
                        throw new NullPointerException();
                    }

                    for (Map.Entry<String, JsonElement> entry1 : entry.getValue().getAsJsonObject().entrySet()) {
                        System.out.println("\t" + entry1);
                        RegistryReferenceAccessor<?> accessor = (RegistryReferenceAccessor<?>) registry.getEntry(Identifier.of(entry1.getKey())).orElseThrow();

                        for (JsonElement tag : entry1.getValue().getAsJsonArray()) {
                            System.out.println("\t\t" + tag);
                            putTag(accessor, TagKey.of(registry.getKey(), Identifier.of(tag.getAsString())));
                        }
                    }
                }
            }
        }

        Registries.BLOCK.forEach(block -> {
            Identifier id = Registries.BLOCK.getId(block);

            if (!Objects.equals(id.getNamespace(), "minecraft")) {
                System.out.println(block);
                System.out.println(((RegistryReferenceAccessor<?>) Registries.BLOCK.getEntry(id).orElseThrow()).getExtraTags());
            }
        });

        System.out.println(Registries.BLOCK.get(Identifier.of("beryllium", "test_block_stairs")));
    }

    @Override
    public void reload(ResourceManager manager) {
        loadBlocks(manager);
        loadItems(manager);
        loadDynamicTags(manager);
    }
}
