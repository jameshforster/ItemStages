package com.tol.itemstages.registries;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static Block BASIC_RESEARCH_TABLE = null;

    public static Block registerItemBlock(String name, ItemGroup itemGroup, Block block) {
        BlockItem itemBlock = new BlockItem(block, new Item.Properties().group(itemGroup));
        block.setRegistryName(name);
        itemBlock.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(itemBlock);
        return block;
    }
}
