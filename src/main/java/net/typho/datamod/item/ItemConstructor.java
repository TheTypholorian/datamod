package net.typho.datamod.item;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.typho.datamod.DataMod;

public interface ItemConstructor {
    private static <T> void put(Item.Settings settings, Component<T> component) {
        settings.component(component.type(), component.value());
    }

    static Item.Settings componentsToSettings(ComponentMap components) {
        Item.Settings settings = new Item.Settings();

        for (Component<?> component : components) {
            put(settings, component);
        }

        return settings;
    }

    static ComponentMap extractComponents(JsonObject json) {
        if (json.has("components")) {
            return parseComponents(json.getAsJsonObject("components"));
        }

        return DataComponentTypes.DEFAULT_ITEM_COMPONENTS;
    }

    static Item.Settings extractSettings(JsonObject json) {
        if (json.has("copy")) {
            return componentsToSettings(Registries.ITEM.get(DataMod.getIdentifier(json.get("copy"))).getComponents());
        } else if (json.has("components")) {
            return componentsToSettings(parseComponents(json.getAsJsonObject("components")));
        }

        return new Item.Settings();
    }

    static ComponentMap parseComponents(JsonObject json) {
        return ComponentMap.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
    }

    Item createItem(Identifier id, JsonObject json);

    void modifyItem(Item item, Identifier id, JsonObject json);
}
