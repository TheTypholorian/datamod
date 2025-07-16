package net.typho.datamod.item;

import net.minecraft.component.ComponentMap;

public interface DynamicItem {
    void setComponents(ComponentMap map);

    static DynamicItem cast(Object object) {
        return (DynamicItem) object;
    }
}
