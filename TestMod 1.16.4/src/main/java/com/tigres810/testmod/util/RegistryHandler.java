package com.tigres810.testmod.util;

import com.tigres810.testmod.Test;
import com.tigres810.testmod.blocks.BlockItemBase;
import com.tigres810.testmod.blocks.FluidTankBlock;
import com.tigres810.testmod.blocks.TestBlock;
import com.tigres810.testmod.items.ItemBase;
import com.tigres810.testmod.tileentitys.TileFluidTankBlock;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {

	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Test.MOD_ID);
	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Test.MOD_ID);
	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Test.MOD_ID);
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Test.MOD_ID);
	
	
	public static void init() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	// Items
	public static final RegistryObject<Item> TESTITEM = ITEMS.register("testitem", () -> new ItemBase(new Item.Properties().group(Test.TAB)));
	
	// Blocks
	public static final RegistryObject<Block> TEST_BLOCK = BLOCKS.register("test_block", TestBlock::new);
	public static final RegistryObject<Block> FLUIDTANK_BLOCK = BLOCKS.register("fluidtank_block", FluidTankBlock::new);
	
	// Tile Entity's
	public static final RegistryObject<TileEntityType<TileFluidTankBlock>> FLUIDTANK_BLOCK_TILE = TILES.register("fluidtank_block", () -> TileEntityType.Builder.create(TileFluidTankBlock::new, FLUIDTANK_BLOCK.get()).build(null));
	
	// Block Items
	public static final RegistryObject<Item> TEST_BLOCK_ITEM = ITEMS.register("test_block", () -> new BlockItemBase(TEST_BLOCK.get(), new Item.Properties().group(Test.TAB)));
	public static final RegistryObject<Item> FLUIDTANK_BLOCK_ITEM = ITEMS.register("fluidtank_block", () -> new BlockItemBase(FLUIDTANK_BLOCK.get(), new Item.Properties().group(Test.TAB)));
	
	// Containers
}
