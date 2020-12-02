package com.tigres810.testmod.tileentitys;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.tigres810.testmod.util.RegistryHandler;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileFluidTankBlock extends TileEntity {
	protected FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 5) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == RegistryHandler.FLUX_FLUID.get();
        }
        
        @Override
        protected void onContentsChanged() {
        	BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        };
    };
    
    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public TileFluidTankBlock() {
        super(RegistryHandler.FLUIDTANK_BLOCK_TILE.get());
    }
    
    public int fluidamount;
    
    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }
    
    public FluidTank getTank() {
        return this.tank;
    }
	
	@Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        getTank().readFromNBT(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        getTank().writeToNBT(tag);
        return tag;
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
      this.read(null,packet.getNbtCompound());
    }
	
}
