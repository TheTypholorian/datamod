package net.typho.datamod.block;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public interface BlockConstructor {
    static AbstractBlock.Settings extractSettings(JsonObject json) {
        if (json.has("settings")) {
            JsonObject settings = json.getAsJsonObject("settings");

            if (settings.has("copy")) {
                return AbstractBlock.Settings.copy(Registries.BLOCK.get(Identifier.of(settings.get("copy").getAsString())));
            } else {
                return AbstractBlock.Settings.CODEC.parse(JsonOps.INSTANCE, settings).getOrThrow();
            }
        }

        return AbstractBlock.Settings.create();
    }

    Block createBlock(Identifier id, JsonObject json);

    void modifyBlock(Block block, Identifier id, JsonObject json);
}
