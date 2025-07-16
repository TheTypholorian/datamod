package net.typho.datamod;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.block.AbstractBlock;

public interface DynamicBlock {
    void setSettings(AbstractBlock.Settings settings);

    static DynamicBlock cast(Object object) {
        return (DynamicBlock) object;
    }

    static AbstractBlock.Settings readSettings(JsonElement json) {
        return AbstractBlock.Settings.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
    }
}
