package com.tigres810.testmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class BlockFluxOre extends OreBlock {
	
	public BlockFluxOre() {
		super(Block.Properties.create(Material.ROCK)
				.hardnessAndResistance(8.0F, 4.000f)
				.sound(SoundType.STONE)
				.harvestLevel(1)
				.setRequiresTool()
		);
	}
	
	@Override
	public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
		return (int) ((Math.random() * 6) + 3);
	}

}
