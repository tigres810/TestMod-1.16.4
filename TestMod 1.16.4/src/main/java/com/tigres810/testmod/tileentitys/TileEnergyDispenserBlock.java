package com.tigres810.testmod.tileentitys;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.tigres810.testmod.energy.CustomEnergy;
import com.tigres810.testmod.util.RegistryHandler;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEnergyDispenserBlock extends TileEntity implements ITickableTileEntity {
	private CustomEnergy storage = new CustomEnergy(5);
	public int energy = storage.getEnergyStored();
	public Boolean sendEnergy = false;
	
	private final LazyOptional<IEnergyStorage> holder = LazyOptional.of(() -> storage);
	
	private int ticks;
	public static BlockPos connectedto = BlockPos.ZERO;

	public TileEnergyDispenserBlock() {
        super(RegistryHandler.ENERGYDISPENSER_BLOCK_TILE.get());
    }
	
	@Override
	public void tick() {
		if(!this.world.isRemote) {
			if(this.ticks >= 200) {
				if(this.sendEnergy == false) {
					if(this.energy < this.getMaxEnergyFromStorage()) {
						TileEntity tank = this.world.getTileEntity(this.pos.down());
						
						if(tank != null) {
							LazyOptional<IFluidHandler> fluidHandlerCap = tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
							
							if(fluidHandlerCap.isPresent()) {
								IFluidHandler fluidHandler = fluidHandlerCap.orElseThrow(IllegalStateException::new);
								
								if (fluidHandler.drain(1000, IFluidHandler.FluidAction.SIMULATE).getAmount() == 1000) {
									this.ticks = 0;
									fluidHandler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
									this.energy += 1;
									this.sendEnergy = true;
									this.markDirty();
								} else {
									this.ticks = 0;
									this.sendEnergy = true;
								}
							} else {
								this.ticks = 0;
								this.sendEnergy = true;
							}
						} else {
							this.ticks = 0;
							this.sendEnergy = true;
						}
					} else {
						this.ticks = 0;
						this.sendEnergy = true;
					}
				} else {
					TileEntity anotherStorage = this.world.getTileEntity(connectedto);
					
					if(anotherStorage != null) {
						if(this.getEnergyFromStorage() > 0) {
							if(((TileEnergyDispenserBlock) anotherStorage).energy < ((TileEnergyDispenserBlock) anotherStorage).getMaxEnergyFromStorage()) {
								this.ticks = 0;
								this.energy -= 1;
								this.sendEnergy = false;
								((TileEnergyDispenserBlock) anotherStorage).energy += 1;
								this.markDirty();
								((TileEnergyDispenserBlock) anotherStorage).markDirty();
							} else {
								this.ticks = 0;
								this.sendEnergy = false;
							}
						} else {
							this.ticks = 0;
							this.sendEnergy = false;
						}
					} else {
						this.ticks = 0;
						this.sendEnergy = false;
					}
				}
			} else {
				this.ticks++;
			}
		}
	}
	
	@Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }
	
	public BlockPos getConnectedTo() {
		return connectedto;
	}
	
	public void setConnectedTo(BlockPos pos) {
		connectedto = pos;
		this.markDirty();
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
		connectedto = new BlockPos(nbt.getInt("ConnectedToX"), nbt.getInt("ConnectedToY"), nbt.getInt("ConnectedToZ"));
		this.storage = new CustomEnergy(nbt.getInt("Energy"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
		compound.putInt("Energy", this.storage.getEnergyStored());
		compound.putInt("ConnectedToX", connectedto.getX());
		compound.putInt("ConnectedToY", connectedto.getY());
		compound.putInt("ConnectedToZ", connectedto.getZ());
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
