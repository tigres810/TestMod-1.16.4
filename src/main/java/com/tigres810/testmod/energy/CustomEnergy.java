package com.tigres810.testmod.energy;

import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergy extends EnergyStorage {
	
	public CustomEnergy(int capacity)
    {
        super(capacity, capacity, capacity, 0);
    }

    public CustomEnergy(int capacity, int maxTransfer)
    {
    	super(capacity, maxTransfer, maxTransfer, 0);
    }

    public CustomEnergy(int capacity, int maxReceive, int maxExtract)
    {
    	super(capacity, maxReceive, maxExtract, 0);
    }
    
    public CustomEnergy(int capacity, int maxReceive, int maxExtract, int energy)
    {
    	super(capacity, maxReceive, maxExtract, energy);
    }

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return super.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return super.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored() {
		return super.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return super.getMaxEnergyStored();
	}

	@Override
	public boolean canExtract() {
		return super.canExtract();
	}

	@Override
	public boolean canReceive() {
		return super.canReceive();
	}
	
	/*
	public void read(CompoundNBT compound)
    {
    	this.energy = compound.getInt("Energy");
    	this.capacity = compound.getInt("Capacity");
    	this.maxReceive = compound.getInt("MaxReceive");
    	this.maxExtract = compound.getInt("MaxExtract");
    }
    
    public CompoundNBT write(CompoundNBT compound)
    {
    	compound.putInt("Energy", this.energy);
    	compound.putInt("Capacity", this.capacity);
    	compound.putInt("MaxReceive", this.maxReceive);
    	compound.putInt("MaxExtract", this.maxExtract);
		return compound;
    }
    */

}
