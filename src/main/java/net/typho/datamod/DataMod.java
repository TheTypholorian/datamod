package net.typho.datamod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.typho.datamod.block.BlockConstructor;
import net.typho.datamod.block.DynamicBlock;
import net.typho.datamod.block.DynamicStairBlock;
import net.typho.datamod.item.DynamicBlockItem;
import net.typho.datamod.item.DynamicItem;
import net.typho.datamod.item.ItemConstructor;

public class DataMod implements ModInitializer {
    public static final String MOD_ID = "datamod";

    public static final RegistryKey<Registry<ItemConstructor>> ITEM_CONSTRUCTORS_KEY = RegistryKey.ofRegistry(Identifier.of(MOD_ID, "item_constructors"));
    public static final Registry<ItemConstructor> ITEM_CONSTRUCTORS = FabricRegistryBuilder.createSimple(ITEM_CONSTRUCTORS_KEY).buildAndRegister();

    public static final RegistryKey<Registry<BlockConstructor>> BLOCK_CONSTRUCTORS_KEY = RegistryKey.ofRegistry(Identifier.of(MOD_ID, "block_constructors"));
    public static final Registry<BlockConstructor> BLOCK_CONSTRUCTORS = FabricRegistryBuilder.createSimple(BLOCK_CONSTRUCTORS_KEY).buildAndRegister();

    public static final ItemConstructor DEFAULT_ITEM_CONSTRUCTOR = Registry.register(ITEM_CONSTRUCTORS, Identifier.of(MOD_ID, "default"), new ItemConstructor() {
        @Override
        public Item createItem(Identifier id, JsonObject json) {
            return new Item(ItemConstructor.componentsToSettings(ItemConstructor.extractComponents(json)));
        }

        @Override
        public void modifyItem(Item item, Identifier id, JsonObject json) {
            ((DynamicItem) item).setComponents(ItemConstructor.extractComponents(json));
        }
    });
    public static final ItemConstructor BLOCK_ITEM_CONSTRUCTOR = Registry.register(ITEM_CONSTRUCTORS, Identifier.of(MOD_ID, "block_item"), new ItemConstructor() {
        @Override
        public Item createItem(Identifier id, JsonObject json) {
            return new BlockItem(Registries.BLOCK.get(getIdentifier(json.get("block"))), ItemConstructor.extractSettings(json));
        }

        @Override
        public void modifyItem(Item item, Identifier id, JsonObject json) {
            ((DynamicItem) item).setComponents(ItemConstructor.extractComponents(json));
            ((DynamicBlockItem) item).setBlock(Registries.BLOCK.get(getIdentifier(json.get("block"))));
        }
    });

    public static final BlockConstructor DEFAULT_BLOCK_CONSTRUCTOR = Registry.register(BLOCK_CONSTRUCTORS, Identifier.of(MOD_ID, "default"), new BlockConstructor() {
        @Override
        public Block createBlock(Identifier id, JsonObject json) {
            return new Block(BlockConstructor.extractSettings(json));
        }

        @Override
        public void modifyBlock(Block block, Identifier id, JsonObject json) {
            ((DynamicBlock) block).setSettings(BlockConstructor.extractSettings(json));
        }
    });
    public static final BlockConstructor STAIRS_BLOCK_CONSTRUCTOR = Registry.register(BLOCK_CONSTRUCTORS, Identifier.of(MOD_ID, "stairs"), new BlockConstructor() {
        @Override
        public Block createBlock(Identifier id, JsonObject json) {
            return new StairsBlock(getBlockState(json.get("base_block_state")), BlockConstructor.extractSettings(json));
        }

        @Override
        public void modifyBlock(Block block, Identifier id, JsonObject json) {
            System.out.println(block.getRegistryEntry().isIn(BlockTags.STAIRS));
            ((DynamicBlock) block).setSettings(BlockConstructor.extractSettings(json));
            ((DynamicStairBlock) block).setBaseBlockState(getBlockState(json.get("base_block_state")));
        }
    });

    public static Identifier getIdentifier(JsonElement json) {
        return Identifier.of(json.getAsString());
    }

    public static BlockState getBlockState(JsonElement json) {
        if (json == null) {
            throw new NullPointerException();
        }

        if (json.isJsonPrimitive()) {
            return Registries.BLOCK.get(getIdentifier(json)).getDefaultState();
        } else {
            return BlockState.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
        }
    }

    public static void loadItem(Identifier id, JsonObject json) {
        ItemConstructor constructor = json.has("constructor") ? ITEM_CONSTRUCTORS.getOrEmpty(getIdentifier(json.get("constructor"))).orElse(DEFAULT_ITEM_CONSTRUCTOR) : DEFAULT_ITEM_CONSTRUCTOR;
        Item existing = Registries.ITEM.get(id);

        if (existing == Items.AIR) {
            Registry.register(Registries.ITEM, id, constructor.createItem(id, json));
        } else {
            constructor.modifyItem(existing, id, json);
        }
    }

    public static void loadBlock(Identifier id, JsonObject json) {
        BlockConstructor constructor = json.has("constructor") ? BLOCK_CONSTRUCTORS.getOrEmpty(getIdentifier(json.get("constructor"))).orElse(DEFAULT_BLOCK_CONSTRUCTOR) : DEFAULT_BLOCK_CONSTRUCTOR;
        Block existing = Registries.BLOCK.get(id);
        System.out.println(existing + " " + constructor + " " + json);

        if (existing == Blocks.AIR) {
            Registry.register(Registries.BLOCK, id, constructor.createBlock(id, json));
        } else {
            constructor.modifyBlock(existing, id, json);
        }
    }

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new DataItemReloadListener());
    }
}
