package net.typho.datamod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.component.Component;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;

public class DataMod implements ModInitializer {
    public static final String MOD_ID = "datamod";

    private static <T> void put(Item.Settings settings, Component<T> component) {
        settings.component(component.type(), component.value());
    }

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new DataItemReloadListener());
    }
}
