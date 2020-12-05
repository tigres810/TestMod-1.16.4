package com.tigres810.testmod.tileentitys;

import com.tigres810.testmod.util.RegistryHandler;

import net.minecraft.tileentity.TileEntity;

public class TileEnergyDispenserBlock extends TileEntity {

	public TileEnergyDispenserBlock() {
        super(RegistryHandler.ENERGYDISPENSER_BLOCK_TILE.get());
    }

}
