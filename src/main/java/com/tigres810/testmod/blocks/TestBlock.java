package com.tigres810.testmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class TestBlock extends Block {

	public TestBlock() {
		super(Block.Properties.create(Material.ROCK)
				.hardnessAndResistance(1.5f, 6f)
				.sound(SoundType.STONE)
				.harvestLevel(0)
				.harvestTool(ToolType.PICKAXE)
				.setRequiresTool()
		);
	}

}
