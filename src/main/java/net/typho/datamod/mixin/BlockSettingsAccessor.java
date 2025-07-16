package net.typho.datamod.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.Settings.class)
public interface BlockSettingsAccessor {
    @Accessor("collidable")
    boolean getCollidable();

    @Accessor("lootTableKey")
    RegistryKey<LootTable> getLootTableKey();

    @Accessor("resistance")
    float getResistance();

    @Accessor("randomTicks")
    boolean getRandomTicks();

    @Accessor("soundGroup")
    BlockSoundGroup getSoundGroup();

    @Accessor("slipperiness")
    float getSlipperiness();

    @Accessor("velocityMultiplier")
    float getVelocityMultiplier();

    @Accessor("jumpVelocityMultiplier")
    float getJumpVelocityMultiplier();

    @Accessor("dynamicBounds")
    boolean getDynamicBounds();
}