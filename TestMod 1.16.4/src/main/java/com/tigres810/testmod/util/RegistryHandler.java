package com.tigres810.testmod.util;

import com.tigres810.testmod.Test;
import com.tigres810.testmod.blocks.BlockItemBase;
import com.tigres810.testmod.blocks.CauldronBlock;
import com.tigres810.testmod.blocks.EnergyDispenserBlock;
import com.tigres810.testmod.blocks.FluidTankBlock;
import com.tigres810.testmod.blocks.TestBlock;
import com.tigres810.testmod.items.ItemBase;
import com.tigres810.testmod.items.MagicStickItem;
import com.tigres810.testmod.tileentitys.TileFluidTankBlock;
import com.tigres810.testmod.tileentitys.TileCauldronBlock;
import com.tigres810.testmod.tileentitys.TileEnergyDispenserBlock;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {

	// Registers
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Test.MOD_ID);
	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Test.MOD_ID);
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Test.MOD_ID);
	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Test.MOD_ID);
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Test.MOD_ID);
	
	// Resource Locations
	public static final ResourceLocation FLUX_FLUID_ST = new ResourceLocation(Test.MOD_ID, "blocks/flux_fluid_still");
	public static final ResourceLocation FLUX_FLUID_FL = new ResourceLocation(Test.MOD_ID, "blocks/flux_fluid_flow");
	public static final ResourceLocation FLUX_FLUID_OV = new ResourceLocation(Test.MOD_ID, "block/flux_fluid_overlay");
	
	public static void init() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	// Items
	public static final RegistryObject<Item> TESTITEM = ITEMS.register("testitem", () -> new ItemBase(new Item.Properties().group(Test.TAB)));
	public static final RegistryObject<Item> MAGIC_STICK = ITEMS.register("magic_stick", () -> new MagicStickItem(new Item.Properties().group(Test.TAB)));
	
	// Fluid Buckets
	public static final RegistryObject<BucketItem> FLUX_FLUID_BUCKET = ITEMS.register("flux_fluid_bucket", () -> new BucketItem(() -> RegistryHandler.FLUX_FLUID.get(), new Item.Properties().group(Test.TAB)));
	
	// Blocks
	public static final RegistryObject<Block> TEST_BLOCK = BLOCKS.register("test_block", TestBlock::new);
	public static final RegistryObject<Block> FLUIDTANK_BLOCK = BLOCKS.register("fluidtank_block", FluidTankBlock::new);
	public static final RegistryObject<Block> ENERGYDISPENSER_BLOCK = BLOCKS.register("energydispenser_block", EnergyDispenserBlock::new);
	public static final RegistryObject<Block> CAULDRON_BLOCK = BLOCKS.register("cauldron_block", CauldronBlock::new);
	
	// Fluid Blocks
	public static final RegistryObject<FlowingFluidBlock> FLUX_FLUID_BLOCK = BLOCKS.register("flux_fluid_block", () -> new FlowingFluidBlock(() -> RegistryHandler.FLUX_FLUID.get(), Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0f).noDrops()));
	
	// Tile Entity's
	public static final RegistryObject<TileEntityType<TileFluidTankBlock>> FLUIDTANK_BLOCK_TILE = TILES.register("fluidtank_block", () -> TileEntityType.Builder.create(TileFluidTankBlock::new, FLUIDTANK_BLOCK.get()).build(null));
	public static final RegistryObject<TileEntityType<TileEnergyDispenserBlock>> ENERGYDISPENSER_BLOCK_TILE = TILES.register("energydispenser_block", () -> TileEntityType.Builder.create(TileEnergyDispenserBlock::new, ENERGYDISPENSER_BLOCK.get()).build(null));
	public static final RegistryObject<TileEntityType<TileCauldronBlock>> CAULDRON_BLOCK_TILE = TILES.register("cauldron_block", () -> TileEntityType.Builder.create(TileCauldronBlock::new, CAULDRON_BLOCK.get()).build(null));
	
	// Block Items
	public static final RegistryObject<Item> TEST_BLOCK_ITEM = ITEMS.register("test_block", () -> new BlockItemBase(TEST_BLOCK.get(), new Item.Properties().group(Test.TAB)));
	public static final RegistryObject<Item> FLUIDTANK_BLOCK_ITEM = ITEMS.register("fluidtank_block", () -> new BlockItemBase(FLUIDTANK_BLOCK.get(), new Item.Properties().group(Test.TAB)));
	public static final RegistryObject<Item> ENERGYDISPENSER_BLOCK_ITEM = ITEMS.register("energydispenser_block", () -> new BlockItemBase(ENERGYDISPENSER_BLOCK.get(), new Item.Properties().group(Test.TAB)));
	public static final RegistryObject<Item> CAULDRON_BLOCK_ITEM = ITEMS.register("cauldron_block", () -> new BlockItemBase(CAULDRON_BLOCK.get(), new Item.Properties().group(Test.TAB)));
	
	// Fluids
	public static final RegistryObject<FlowingFluid> FLUX_FLUID = FLUIDS.register("flux_fluid_still", () -> new ForgeFlowingFluid.Source(RegistryHandler.FLUX_FLUID_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLUX_FLUID_FLOWING = FLUIDS.register("flux_fluid_flow", () -> new ForgeFlowingFluid.Flowing(RegistryHandler.FLUX_FLUID_PROPERTIES));
	public static final ForgeFlowingFluid.Properties FLUX_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(() -> FLUX_FLUID.get(), () -> FLUX_FLUID_FLOWING.get(), FluidAttributes.builder(FLUX_FLUID_ST, FLUX_FLUID_FL).density(5).luminosity(3).rarity(Rarity.RARE).overlay(FLUX_FLUID_OV)).block(() -> FLUX_FLUID_BLOCK.get()).bucket(() -> FLUX_FLUID_BUCKET.get());
	
	// Containers
}
