package com.tol.itemstages.registries;

import com.tol.itemstages.blocks.BasicResearchTable;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "researchstages");

    public static final RegistryObject<BasicResearchTable> BASIC_RESEARCH_TABLE = BLOCKS.register("basic_research_table", BasicResearchTable::new);
    public static final RegistryObject<Item> BASIC_RESEARCH_TABLE_ITEM = ItemRegistry.ITEMS.register("basic_research_table", () -> new BlockItem(BASIC_RESEARCH_TABLE.get(), new Item.Properties().group(ItemGroup.TOOLS)));

}
