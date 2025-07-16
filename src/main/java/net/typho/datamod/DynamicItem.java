package net.typho.datamod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface DynamicItem {
    void setComponents(ComponentMap map);

    static DynamicItem cast(Object object) {
        return (DynamicItem) object;
    }

    private static <T> void put(Item.Settings settings, Component<T> component) {
        settings.component(component.type(), component.value());
    }

    static Item.Settings settingsOf(ComponentMap components) {
        Item.Settings settings = new Item.Settings();

        for (Component<?> component : components) {
            put(settings, component);
        }

        return settings;
    }

    static Item create(ComponentMap components, JsonObject json) {
        if (json.has("block_item")) {
            return new BlockItem(Registries.BLOCK.get(Identifier.of(json.get("block_item").getAsString())), settingsOf(components));
        }

        return new Item(settingsOf(components));
    }

    static void load(Identifier id, JsonObject json) {
        Item existing = Registries.ITEM.get(id);

        ComponentMap components = json.has("components") ? ComponentMap.CODEC.parse(JsonOps.INSTANCE, json.get("components")).getOrThrow() : DataComponentTypes.DEFAULT_ITEM_COMPONENTS;

        if (existing == Items.AIR) {
            Registry.register(Registries.ITEM, id, create(components, json));
        } else {
            DynamicItem.cast(existing).setComponents(components);
        }
    }

    static Item.Settings readSettings(JsonElement json) {
        Item.Settings settings = new Item.Settings();

        ComponentMap components = ComponentMap.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

        for (Component<?> component : components) {
            put(settings, component);
        }

        return settings;

        /*
        if (json.has("food")) {
            settings.food(FoodComponent.CODEC.parse(JsonOps.INSTANCE, json.get("food")).getOrThrow());
        }

        if (json.has("max_count")) {
            settings.maxCount(json.get("max_count").getAsInt());
        }

        if (json.has("max_damage")) {
            settings.maxDamage(json.get("max_damage").getAsInt());
        }

        if (json.has("recipe_remainder")) {
            settings.recipeRemainder(Registries.ITEM.get(Identifier.of(json.get("recipe_remainder").getAsString())));
        }

        if (json.has("rarity")) {
            settings.rarity(Rarity.valueOf(json.get("rarity").getAsString().toUpperCase()));
        }

        if (json.has("fireproof")) {
            if (json.get("fireproof").getAsBoolean()) {
                settings.fireproof();
            }
        }

        if (json.has("jukebox_playable")) {
            settings.jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(json.get("jukebox_playable").getAsString())));
        }

        if (json.has("attribute_modifiers")) {
            settings.attributeModifiers(AttributeModifiersComponent.CODEC.parse(JsonOps.INSTANCE, json.get("attribute_modifiers")).getOrThrow());
        }

        return settings;
         */
    }
}
