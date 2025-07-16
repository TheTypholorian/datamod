package net.typho.datamod.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.item.Item;
import net.typho.datamod.item.DynamicItem;
import org.spongepowered.asm.mixin.*;

@Mixin(Item.class)
@Implements(@Interface(
        iface = DynamicItem.class,
        prefix = "datamod$"
))
public abstract class ItemMixin {
    @Shadow
    @Final
    @Mutable
    private ComponentMap components;

    @Unique
    public void datamod$setComponents(ComponentMap map) {
        components = map;
    }
}
