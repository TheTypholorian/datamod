package net.typho.datamod.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.typho.datamod.block.DynamicBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

@Mixin(AbstractBlock.class)
@Implements(@Interface(
        iface = DynamicBlock.class,
        prefix = "datamod$"
))
public abstract class AbstractBlockMixin {
    @Shadow
    @Final
    @Mutable
    protected boolean collidable;

    @Shadow
    @Nullable
    @Mutable
    protected RegistryKey<LootTable> lootTableKey;

    @Shadow
    @Final
    @Mutable
    protected float resistance;

    @Shadow
    @Final
    @Mutable
    protected boolean randomTicks;

    @Shadow
    @Final
    @Mutable
    protected BlockSoundGroup soundGroup;

    @Shadow
    @Final
    @Mutable
    protected float slipperiness;

    @Shadow
    @Final
    @Mutable
    protected float velocityMultiplier;

    @Shadow
    @Final
    @Mutable
    protected float jumpVelocityMultiplier;

    @Shadow
    @Final
    @Mutable
    protected boolean dynamicBounds;

    @Shadow
    @Final
    @Mutable
    protected AbstractBlock.Settings settings;

    @Unique
    public void datamod$setSettings(AbstractBlock.Settings settings) {
        BlockSettingsAccessor accessor = (BlockSettingsAccessor) settings;

        collidable = accessor.getCollidable();
        lootTableKey = accessor.getLootTableKey();
        resistance = accessor.getResistance();
        randomTicks = accessor.getRandomTicks();
        soundGroup = accessor.getSoundGroup();
        slipperiness = accessor.getSlipperiness();
        velocityMultiplier = accessor.getVelocityMultiplier();
        jumpVelocityMultiplier = accessor.getJumpVelocityMultiplier();
        dynamicBounds = accessor.getDynamicBounds();
        this.settings = settings;
    }
}
