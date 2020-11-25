package com.tigres810.testmod.util;

import net.minecraft.nbt.CompoundNBT;

public interface IRestorableTileEntity {
	
	void readRestorableFromNBT(CompoundNBT compound);

    void writeRestorableToNBT(CompoundNBT compound);

}
