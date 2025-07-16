package net.typho.datamod.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.block.AbstractBlock;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public interface DynamicBlock {
    void setSettings(AbstractBlock.Settings settings);

    static DynamicBlock cast(Object object) {
        return (DynamicBlock) object;
    }

    static AbstractBlock.Settings readSettings(JsonElement json) {
        if (json == null) {
            return AbstractBlock.Settings.create();
        }

        if (json instanceof JsonObject object) {
            if (object.has("copy")) {
                return AbstractBlock.Settings.copy(Registries.BLOCK.get(Identifier.of(object.get("copy").getAsString())));
            }
        }

        return AbstractBlock.Settings.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
    }
}
