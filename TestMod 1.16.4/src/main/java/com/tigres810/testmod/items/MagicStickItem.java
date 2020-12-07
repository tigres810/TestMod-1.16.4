package com.tigres810.testmod.items;

import com.tigres810.testmod.tileentitys.TileEnergyDispenserBlock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MagicStickItem extends ItemBase {
	
	public static BlockPos pos1 = BlockPos.ZERO;
	public static BlockPos pos2 = BlockPos.ZERO;

	public MagicStickItem(Properties properties) {
		super(properties);
	}
	
	public BlockPos getposition1() {
		return pos1;
	}
	
	public BlockPos getposition2() {
		return pos2;
	}
	
	public void setpositions(BlockPos pos, World world, PlayerEntity player) {
		if(pos1 == BlockPos.ZERO) {
			pos1 = pos;
			world.addParticle(ParticleTypes.WITCH, pos1.getX() + 0.5d, pos1.getY(), pos1.getZ() + 0.5d, 0, 1, 0);
			player.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 1f, 1f);
		} else if(pos2 == BlockPos.ZERO) {
			pos2 = pos;
			world.addParticle(ParticleTypes.WITCH, pos2.getX() + 0.5d, pos2.getY(), pos2.getZ() + 0.5d, 0, 1, 0);
			player.playSound(SoundEvents.BLOCK_BEACON_POWER_SELECT, 1f, 1f);
			
			TileEntity te1 = world.getTileEntity(pos1);
			TileEntity te2 = world.getTileEntity(pos2);
			
			if(te1 != null && te2 != null) {
				if(te1 instanceof TileEnergyDispenserBlock) {
					((TileEnergyDispenserBlock) te1).setConnectedTo(pos2);
					pos1 = BlockPos.ZERO;
					pos2 = BlockPos.ZERO;
				}
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(playerIn.isSneaking()) {
			if(pos1 != BlockPos.ZERO) {
				pos1 = BlockPos.ZERO;
				pos2 = BlockPos.ZERO;
				playerIn.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE, 1f, 1f);
			}
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

}
