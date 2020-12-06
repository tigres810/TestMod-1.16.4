package com.tigres810.testmod.util.packets;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class DestroyBlockMessage {
	
	private BlockPos pos;

	public DestroyBlockMessage(BlockPos pos) {
		this.pos = pos;
	}
	
	public void serialize(PacketBuffer buffer) {
		buffer.writeBlockPos(this.pos);
	}
	
	public static DestroyBlockMessage deserialize(PacketBuffer buffer) {
		BlockPos pos = buffer.readBlockPos();
		return new DestroyBlockMessage(pos);
	}

	
	public static boolean handle(DestroyBlockMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		boolean fail = true;
		
		if(context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			World world = context.getSender().world;
			if(!world.isRemote) {
				world.destroyBlock(message.pos, true);
			}
		}
		return fail;
	}
}
