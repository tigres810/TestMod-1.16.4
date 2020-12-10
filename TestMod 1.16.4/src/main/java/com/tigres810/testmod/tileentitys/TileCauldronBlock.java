package com.tigres810.testmod.tileentitys;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.tigres810.testmod.util.RegistryHandler;
import com.tigres810.testmod.util.interfaces.ICauldronRecipes;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileCauldronBlock extends TileEntity implements ITickableTileEntity, ICauldronRecipes {
	protected FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 5) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == Fluids.WATER;
        }
        
        @Override
        protected void onContentsChanged() {
        	BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        };
    };
    
    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);
    private List<ItemStack> recipe = new ArrayList<ItemStack>();
    private List<ItemStack> selectedrecipe = new ArrayList<ItemStack>();
    private List<ItemStack> resultitems = new ArrayList<ItemStack>();
    private boolean matchingrecipes = false;

	public TileCauldronBlock() {
        super(RegistryHandler.CAULDRON_BLOCK_TILE.get());
    }
	
	@Override
	public void tick() {
		if(!this.world.isRemote) {
			List<ItemEntity> stuff = this.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1)));
			
			for (ItemEntity item : stuff) {
				recipe.add(item.getItem().copy());
				item.getItem().setCount(0);
				this.world.playSound(null, this.pos, SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundCategory.PLAYERS, 1f, 1f);
			}
			
			ItemStack[][][] recipes = getRecipeList();
			
			// Throws items in order
			if(recipe.size() > 0) {
				for(int e = 0; e < recipes.length; e++) {
					for(int r = 0; r < recipes[e][0].length; r++) {
						for(int i = 0; i < recipe.size(); i++) {
							if(recipes[e][0][r].getItem() == recipe.get(i).getItem()) {
								if(selectedrecipe.size() <= 0) {
									for(int b = 0; b < recipes[e][0].length; b++) {
										selectedrecipe.add(recipes[e][0][b]);
									}
									
									for(int c = 0; c < recipes[e][1].length; c++) {
										resultitems.add(recipes[e][1][c]);
									}
								}
							}
						}
					}
				}
				if(selectedrecipe.size() > 0) {
					if(recipe.size() >= selectedrecipe.size()) {
						for(int b = 0; b < selectedrecipe.size(); b++) {
							if(recipe.get(b).getItem() == selectedrecipe.get(b).getItem()) {
								matchingrecipes = true;
							} else {
								matchingrecipes = false;
							}
							
							if(b >= selectedrecipe.size() - 1) {
								if(matchingrecipes) {
									PlayerEntity player = this.world.getClosestPlayer(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 2f, false);
									
									if(player != null) {
										for(int i = 0; i < resultitems.size(); i++) {
											player.addItemStackToInventory(resultitems.get(i).copy());
											
											if(i >= resultitems.size() - 1) {
												this.world.playSound(null, this.pos, SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 1f, 1f);
												recipe.clear();
												selectedrecipe.clear();
												resultitems.clear();
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
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
        this.getTank().readFromNBT(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        this.getTank().writeToNBT(tag);
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
