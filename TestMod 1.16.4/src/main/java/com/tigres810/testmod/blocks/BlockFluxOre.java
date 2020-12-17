package com.tigres810.testmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockFluxOre extends Block {

	public BlockFluxOre(Properties properties) {
		super(Block.Properties.create(Material.ROCK)
				.hardnessAndResistance(8.0F, 4.000f)
				.sound(SoundType.STONE)
				.harvestLevel(1)
				.setRequiresTool());
	}

}
