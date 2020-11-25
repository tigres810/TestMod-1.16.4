package com.tigres810.testmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tigres810.testmod.proxy.IProxy;
import com.tigres810.testmod.proxy.Proxy;
import com.tigres810.testmod.proxy.ProxyClient;
import com.tigres810.testmod.util.RegistryHandler;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("tmod")
public class Test
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ProxyClient::new, () -> Proxy::new);
    public static final String MOD_ID = "tmod";

    public Test() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        
        RegistryHandler.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
    	
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	
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
