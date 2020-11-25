package com.tigres810.testmod.proxy;

import com.tigres810.testmod.Test;
import com.tigres810.testmod.tileentitys.renders.RenderFluidTankBlock;
import com.tigres810.testmod.util.RegistryHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ProxyClient implements IProxy {

    @SuppressWarnings("resource")
	@Override
    public PlayerEntity getClientPlayer()
    {
        return Minecraft.getInstance().player;
    }

    @SuppressWarnings("resource")
	@Override
    public World getClientWorld()
    {
        return Minecraft.getInstance().world;
    }
    
    @Mod.EventBusSubscriber(modid = Test.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientEventHandlers {

        @SubscribeEvent
        public static void registerTESR(FMLClientSetupEvent event) {
        	ClientRegistry.bindTileEntityRenderer(RegistryHandler.FLUIDTANK_BLOCK_TILE.get(), RenderFluidTankBlock::new);
        }
    }
}
