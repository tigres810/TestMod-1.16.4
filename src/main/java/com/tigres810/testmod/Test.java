package com.tigres810.testmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tigres810.testmod.tileentitys.renders.RenderCauldronBlock;
import com.tigres810.testmod.tileentitys.renders.RenderFluidTankBlock;
import com.tigres810.testmod.util.RegistryHandler;
import com.tigres810.testmod.world.gen.OreGeneration;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Test.MOD_ID)
public class Test
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "tmod";
    public static IEventBus MOD_EVENT_BUS;

    public Test() {
    	MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_EVENT_BUS.addListener(this::setup);
        MOD_EVENT_BUS.addListener(this::doClientStuff);
        
        RegistryHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
    	OreGeneration.registerOres();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	// Tile entity's
    	ClientRegistry.bindTileEntityRenderer(RegistryHandler.FLUIDTANK_BLOCK_TILE.get(), RenderFluidTankBlock::new);
    	ClientRegistry.bindTileEntityRenderer(RegistryHandler.CAULDRON_BLOCK_TILE.get(), RenderCauldronBlock::new);
    	
    	//Containers
    	//ScreenManager.registerFactory(RegistryHandler.TEST_CONTAINER.get(), TestContainerScreen::new);
    }
    
    public static final ItemGroup TAB = new ItemGroup("testTab") {
    	@Override
    	public ItemStack createIcon() {
    		return new ItemStack(RegistryHandler.FLUIDTANK_BLOCK_ITEM.get());
    	}
    };
    
    public static MinecraftServer getServer()
    {
        return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
    }
}
