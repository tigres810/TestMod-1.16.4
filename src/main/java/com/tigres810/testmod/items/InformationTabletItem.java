package com.tigres810.testmod.items;

import com.tigres810.testmod.guis.TestScreen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class InformationTabletItem extends ItemBase {
	
	public InformationTabletItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(worldIn.isRemote) {
			TestScreen.open();
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
