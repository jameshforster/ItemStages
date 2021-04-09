package com.tol.researchstages.registries;

import com.tol.researchstages.items.StoneTablet;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "researchstages");

	public static final RegistryObject<Item> STONE_TABLET = ItemRegistry.ITEMS.register("stone_tablet", () -> new StoneTablet(new Item.Properties().group(ItemGroup.TOOLS)));

}
