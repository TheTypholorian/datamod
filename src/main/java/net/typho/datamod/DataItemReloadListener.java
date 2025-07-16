package net.typho.datamod;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Unit;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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

            Item existing = Registries.ITEM.get(id);

            if (existing == Items.AIR) {
                Registry.register(Registries.ITEM, id, new Item(DynamicItem.readSettings(json.get("components"))));
            } else {
                DynamicItem.cast(existing).setComponents(ComponentMap.CODEC.parse(JsonOps.INSTANCE, json.get("components")).getOrThrow());
            }
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

            Block existing = Registries.BLOCK.get(id);
            System.out.println(id + " " + existing);

            if (existing == Blocks.AIR) {
                Registry.register(Registries.BLOCK, id, new Block(DynamicBlock.readSettings(json.get("settings"))));
            } else {
                DynamicBlock.cast(existing).setSettings(DynamicBlock.readSettings(json.get("settings")));
            }
        }
    }

    @Override
    public void reload(ResourceManager manager) {
        loadBlocks(manager);
        loadItems(manager);
    }
}
