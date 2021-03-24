package com.tol.itemstages.registries;

import com.tol.itemstages.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "researchstages");

    public static final RegistryObject<PrimitiveResearchTable> PRIMITIVE_RESEARCH_TABLE = BLOCKS.register("primitive_research_table", PrimitiveResearchTable::new);
    public static final RegistryObject<Item> PRIMITIVE_RESEARCH_TABLE_ITEM = ItemRegistry.ITEMS.register("primitive_research_table", () -> new BlockItem(PRIMITIVE_RESEARCH_TABLE.get(), new Item.Properties().group(ItemGroup.TOOLS)));

    public static final RegistryObject<BasicResearchTable> BASIC_RESEARCH_TABLE = BLOCKS.register("basic_research_table", BasicResearchTable::new);
    public static final RegistryObject<Item> BASIC_RESEARCH_TABLE_ITEM = ItemRegistry.ITEMS.register("basic_research_table", () -> new BlockItem(BASIC_RESEARCH_TABLE.get(), new Item.Properties().group(ItemGroup.TOOLS)));

    public static final RegistryObject<AdvancedResearchTable> ADVANCED_RESEARCH_TABLE = BLOCKS.register("advanced_research_table", AdvancedResearchTable::new);
    public static final RegistryObject<Item> ADVANCED_RESEARCH_TABLE_ITEM = ItemRegistry.ITEMS.register("advanced_research_table", () -> new BlockItem(ADVANCED_RESEARCH_TABLE.get(), new Item.Properties().group(ItemGroup.TOOLS)));

    public static final RegistryObject<ExpertResearchTable> EXPERT_RESEARCH_TABLE = BLOCKS.register("expert_research_table", ExpertResearchTable::new);
    public static final RegistryObject<Item> EXPERT_RESEARCH_TABLE_ITEM = ItemRegistry.ITEMS.register("expert_research_table", () -> new BlockItem(EXPERT_RESEARCH_TABLE.get(), new Item.Properties().group(ItemGroup.TOOLS)));

    public static final RegistryObject<MasterResearchTable> MASTER_RESEARCH_TABLE = BLOCKS.register("master_research_table", MasterResearchTable::new);
    public static final RegistryObject<Item> MASTER_RESEARCH_TABLE_ITEM = ItemRegistry.ITEMS.register("master_research_table", () -> new BlockItem(MASTER_RESEARCH_TABLE.get(), new Item.Properties().group(ItemGroup.TOOLS)));

}
