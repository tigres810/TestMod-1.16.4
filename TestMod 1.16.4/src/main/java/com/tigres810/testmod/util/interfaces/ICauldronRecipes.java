package com.tigres810.testmod.util.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public interface ICauldronRecipes {
	
	ItemStack[][][] recipes = new ItemStack[][][] {
		{{new ItemStack(Items.IRON_INGOT), new ItemStack(Items.CHARCOAL)}, {new ItemStack(Items.COAL_BLOCK), new ItemStack(Items.COAL)}}
	};
	
	public default ItemStack[][][] getRecipeList() {
		return recipes;
	}
}
