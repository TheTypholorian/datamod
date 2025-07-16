package net.typho.datamod.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.typho.datamod.block.DynamicStairBlock;
import org.spongepowered.asm.mixin.*;

@Mixin(StairsBlock.class)
@Implements(@Interface(
        iface = DynamicStairBlock.class,
        prefix = "datamod$"
))
public abstract class StairsBlockMixin {
    @Shadow
    @Final
    @Mutable
    private Block baseBlock;
    @Shadow
    @Final
    @Mutable
    protected BlockState baseBlockState;

    public void datamod$setBaseBlockState(BlockState baseBlockState) {
        this.baseBlock = baseBlockState.getBlock();
        this.baseBlockState = baseBlockState;
    }
}
