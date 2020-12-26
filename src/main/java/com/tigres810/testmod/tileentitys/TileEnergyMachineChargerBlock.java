package com.tigres810.testmod.tileentitys;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.tigres810.testmod.energy.CustomEnergy;
import com.tigres810.testmod.util.RegistryHandler;
import com.tigres810.testmod.util.interfaces.IMachine;
import com.tigres810.testmod.util.interfaces.IPipeConnect;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEnergyMachineChargerBlock extends TileEntity implements ITickableTileEntity {
	private CustomEnergy storage = new CustomEnergy(5);
	public int energy = storage.getEnergyStored();
	public Boolean sendEnergy = false;
	
	private final LazyOptional<IEnergyStorage> holder = LazyOptional.of(() -> storage);
	
	private int ticks;

	public TileEnergyMachineChargerBlock() {
		super(RegistryHandler.ENERGYMACHINECHARGER_BLOCK_TILE.get());
	}
	
	@Override
	public void tick() {
		if(!this.world.isRemote) {
			if(this.ticks >= 100) {
				if(this.sendEnergy == false) {
					this.ticks = 0;
					
					if(checkside(this.world, this.pos, Direction.UP)) {
						receiveenergy(this.pos.offset(Direction.UP));
					}
					
					if(checkside(this.world, this.pos, Direction.DOWN)) {
						receiveenergy(this.pos.offset(Direction.DOWN));
					}
					
					if(checkside(this.world, this.pos, Direction.NORTH)){
						receiveenergy(this.pos.offset(Direction.NORTH));
					}
					
					if(checkside(this.world, this.pos, Direction.EAST)) {
						receiveenergy(this.pos.offset(Direction.EAST));
					}
					
					if(checkside(this.world, this.pos, Direction.SOUTH)) {
						receiveenergy(this.pos.offset(Direction.SOUTH));
					}
					
					if(checkside(this.world, this.pos, Direction.WEST)) {
						receiveenergy(this.pos.offset(Direction.WEST));
					}
				} else {
					this.ticks = 0;
					
					if(checkside(this.world, this.pos, Direction.UP)) {
						sendenergy(this.pos.offset(Direction.UP));
					}
					
					if(checkside(this.world, this.pos, Direction.DOWN)) {
						sendenergy(this.pos.offset(Direction.DOWN));
					}
					
					if(checkside(this.world, this.pos, Direction.NORTH)){
						sendenergy(this.pos.offset(Direction.NORTH));
					}
					
					if(checkside(this.world, this.pos, Direction.EAST)) {
						sendenergy(this.pos.offset(Direction.EAST));
					}
					
					if(checkside(this.world, this.pos, Direction.SOUTH)) {
						sendenergy(this.pos.offset(Direction.SOUTH));
					}
					
					if(checkside(this.world, this.pos, Direction.WEST)) {
						sendenergy(this.pos.offset(Direction.WEST));
					}
				}
			} else {
				this.ticks++;
			}
		}
	}
	
	private void receiveenergy(BlockPos pos) {
		if(this.getEnergyFromStorage() < this.getMaxEnergyFromStorage()) {
			TileEntity tetoreceiveenergy = this.world.getTileEntity(pos);
			
			if(tetoreceiveenergy != null) {
				LazyOptional<IEnergyStorage> energyHandlerCap = tetoreceiveenergy.getCapability(CapabilityEnergy.ENERGY);
				
				if(energyHandlerCap.isPresent()) {
					IEnergyStorage energyHandler = energyHandlerCap.orElseThrow(IllegalStateException::new);
					
					if(energyHandler.getEnergyStored() >= 1000) {
						energyHandler.extractEnergy(1000, false);
						this.storage.receiveEnergy(1, false);
						this.sendEnergy = true;
						this.markDirty();
					}
				}
			}
		}
	}
	
	private void sendenergy(BlockPos pos) {
		if(this.getEnergyFromStorage() > 0) {
			final BlockState state = world.getBlockState(pos);
			
			if(state != null) {
				TileEntity tetosendenergy = world.getTileEntity(pos);
				
				if(tetosendenergy != null) {
					LazyOptional<IEnergyStorage> energyHandlerCap = tetosendenergy.getCapability(CapabilityEnergy.ENERGY);
					
					if(energyHandlerCap.isPresent()) {
						IEnergyStorage energyHandler = energyHandlerCap.orElseThrow(IllegalStateException::new);
						if((state.getBlock() instanceof IMachine)) {
							this.storage.extractEnergy(1, false);
							this.sendEnergy = false;
							energyHandler.receiveEnergy(1, false);
							this.markDirty();
							tetosendenergy.markDirty();
						}
					}
				}
			}
		}
	}
	
	private boolean checkside(World world, BlockPos pos, Direction side) {
		final BlockState state = world.getBlockState(pos.offset(side));
        if(state==null) return false;
        	TileEntity te = world.getTileEntity(pos.offset(side));
        	if(te==null) return false;
        	LazyOptional<IEnergyStorage> energyHandlerCap = te.getCapability(CapabilityEnergy.ENERGY);
        	if(energyHandlerCap.isPresent()) return true;
            if(!( state.getBlock() instanceof IPipeConnect)) return false;
                List<Direction> faces = ((IPipeConnect)state.getBlock()).getConnectableSides(state);     
                    if(!faces.contains(side)) return false;
                    return true;
	}
	
	@Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }
	
	public EnergyStorage getStorage() {
        return this.storage;
    }
	
	public int getEnergyFromStorage() {
		return this.energy;
	}
	
	public int getMaxEnergyFromStorage() {
		return this.storage.getMaxEnergyStored();
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		this.storage = new CustomEnergy(nbt.getInt("Energy"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
		compound.putInt("Energy", this.storage.getEnergyStored());
		return compound;
	}
	
	@Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
      return write(new CompoundNBT());    // okay to send entire inventory on chunk load
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
      return new SUpdateTileEntityPacket(getPos(), 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
      read(null,packet.getNbtCompound());
    }

}
