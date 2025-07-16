package net.typho.datamod.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.typho.datamod.item.DynamicBlockItem;
import org.spongepowered.asm.mixin.*;

@Mixin(BlockItem.class)
@Implements(@Interface(
        iface = DynamicBlockItem.class,
        prefix = "datamod$"
))
public abstract class BlockItemMixin {
    @Shadow
    @Final
    @Mutable
    private Block block;

    @Unique
    public void datamod$setBlock(Block block) {
        this.block = block;
    }
}
