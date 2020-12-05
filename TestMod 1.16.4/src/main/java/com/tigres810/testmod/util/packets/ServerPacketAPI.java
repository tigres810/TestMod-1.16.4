package com.tigres810.testmod.util.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ServerPacketAPI {

	public void DestroyBlockServerSide(BlockPos pos, World world) {
		MinecraftServer server = Minecraft.getInstance().getIntegratedServer();
		
		if(server != null) {
			server.getWorld(world.getDimensionKey()).destroyBlock(pos, true);
		}
	}
}
