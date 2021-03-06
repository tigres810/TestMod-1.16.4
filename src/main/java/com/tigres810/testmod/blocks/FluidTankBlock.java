package com.tigres810.testmod.blocks;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.tigres810.testmod.tileentitys.TileFluidTankBlock;
import com.tigres810.testmod.util.RegistryHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidTankBlock extends Block {
	
	private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE_N = Stream.of(
		Block.makeCuboidShape(10.7, 0.8, 11, 12, 11.9, 13),Block.makeCuboidShape(4, 0.8, 11, 5.3, 11.9, 13),
		Block.makeCuboidShape(3, 0, 4, 4, 12, 12),Block.makeCuboidShape(4, 0.8, 3, 5.3, 11.9, 5),
		Block.makeCuboidShape(3, 0, 3, 13, 1, 4),Block.makeCuboidShape(10.7, 0.8, 3, 12, 11.9, 5),
		Block.makeCuboidShape(12, 0, 4, 13, 12, 12),Block.makeCuboidShape(5.5, 13.21, 5.5, 10.5, 15.5, 10.5),
		Block.makeCuboidShape(5.3, 11.4, 3, 10.7, 11.9, 4),Block.makeCuboidShape(5.3, 11.4, 12, 10.7, 11.9, 13),
		Block.makeCuboidShape(3, 0, 12, 13, 1, 13),Block.makeCuboidShape(5.3, 1, 12, 10.7, 1.5, 13),
		Block.makeCuboidShape(5.3, 1, 3, 10.7, 1.5, 4),Block.makeCuboidShape(4, 11.8, 3.5, 12, 12.7, 12.5),
		Block.makeCuboidShape(4, 0, 4, 12, 1, 12),Block.makeCuboidShape(12, 1, 12, 12.5, 12.4, 12.5),
		Block.makeCuboidShape(12, 1, 3.5, 12.5, 12.4, 4),Block.makeCuboidShape(3.5, 1, 3.5, 4, 12.4, 4),
		Block.makeCuboidShape(3.5, 1, 12, 4, 12.4, 12.5),Block.makeCuboidShape(3.5, 11.9, 4, 4, 12.4, 12),
		Block.makeCuboidShape(5, 12.7, 5, 11, 13.2, 11),Block.makeCuboidShape(10.5, 15.5, 5, 11, 16, 11),
		Block.makeCuboidShape(5, 15.5, 5, 5.5, 16, 11),Block.makeCuboidShape(5.5, 15.5, 5, 10.5, 16, 5.5),
		Block.makeCuboidShape(5.5, 15.5, 10.5, 10.5, 16, 11),Block.makeCuboidShape(12, 11.9, 4, 12.5, 12.4, 12),
		Block.makeCuboidShape(2.5, 0, 2.5, 13.5, 0.5, 3),Block.makeCuboidShape(2.5, 0, 3, 3, 0.5, 13),
		Block.makeCuboidShape(13, 0, 3, 13.5, 0.5, 13),Block.makeCuboidShape(2.5, 0, 13, 13.5, 0.5, 13.5),
		Block.makeCuboidShape(10, 15.5, 10, 10.5, 16, 10.5),Block.makeCuboidShape(9.5, 15.5, 9.5, 10, 16, 10),
		Block.makeCuboidShape(9, 15.5, 9, 9.5, 16, 9.5),Block.makeCuboidShape(7, 15.5, 8.5, 9, 16, 9),
		Block.makeCuboidShape(6.5, 15.5, 9, 7, 16, 9.5),Block.makeCuboidShape(6, 15.5, 9.5, 6.5, 16, 10),
		Block.makeCuboidShape(5.5, 15.5, 10, 6, 16, 10.5),Block.makeCuboidShape(7, 15.5, 7, 9, 16, 7.5),
		Block.makeCuboidShape(9, 15.5, 6.5, 9.5, 16, 7),Block.makeCuboidShape(9.5, 15.5, 6, 10, 16, 6.5),
		Block.makeCuboidShape(10, 15.5, 5.5, 10.5, 16, 6),Block.makeCuboidShape(6.5, 15.5, 6.5, 7, 16, 7),
		Block.makeCuboidShape(6, 15.5, 6, 6.5, 16, 6.5),Block.makeCuboidShape(5.5, 15.5, 5.5, 6, 16, 6)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_S = Stream.of(
		Block.makeCuboidShape(10.7, 0.8, 11, 12, 11.9, 13),Block.makeCuboidShape(4, 0.8, 11, 5.3, 11.9, 13),
		Block.makeCuboidShape(3, 0, 4, 4, 12, 12),Block.makeCuboidShape(4, 0.8, 3, 5.3, 11.9, 5),
		Block.makeCuboidShape(3, 0, 3, 13, 1, 4),Block.makeCuboidShape(10.7, 0.8, 3, 12, 11.9, 5),
		Block.makeCuboidShape(12, 0, 4, 13, 12, 12),Block.makeCuboidShape(5.5, 13.21, 5.5, 10.5, 15.5, 10.5),
		Block.makeCuboidShape(5.3, 11.4, 3, 10.7, 11.9, 4),Block.makeCuboidShape(5.3, 11.4, 12, 10.7, 11.9, 13),
		Block.makeCuboidShape(3, 0, 12, 13, 1, 13),Block.makeCuboidShape(5.3, 1, 12, 10.7, 1.5, 13),
		Block.makeCuboidShape(5.3, 1, 3, 10.7, 1.5, 4),Block.makeCuboidShape(4, 11.8, 3.5, 12, 12.7, 12.5),
		Block.makeCuboidShape(4, 0, 4, 12, 1, 12),Block.makeCuboidShape(12, 1, 12, 12.5, 12.4, 12.5),
		Block.makeCuboidShape(12, 1, 3.5, 12.5, 12.4, 4),Block.makeCuboidShape(3.5, 1, 3.5, 4, 12.4, 4),
		Block.makeCuboidShape(3.5, 1, 12, 4, 12.4, 12.5),Block.makeCuboidShape(3.5, 11.9, 4, 4, 12.4, 12),
		Block.makeCuboidShape(5, 12.7, 5, 11, 13.2, 11),Block.makeCuboidShape(10.5, 15.5, 5, 11, 16, 11),
		Block.makeCuboidShape(5, 15.5, 5, 5.5, 16, 11),Block.makeCuboidShape(5.5, 15.5, 5, 10.5, 16, 5.5),
		Block.makeCuboidShape(5.5, 15.5, 10.5, 10.5, 16, 11),Block.makeCuboidShape(12, 11.9, 4, 12.5, 12.4, 12),
		Block.makeCuboidShape(2.5, 0, 2.5, 13.5, 0.5, 3),Block.makeCuboidShape(2.5, 0, 3, 3, 0.5, 13),
		Block.makeCuboidShape(13, 0, 3, 13.5, 0.5, 13),Block.makeCuboidShape(2.5, 0, 13, 13.5, 0.5, 13.5),
		Block.makeCuboidShape(10, 15.5, 10, 10.5, 16, 10.5),Block.makeCuboidShape(9.5, 15.5, 9.5, 10, 16, 10),
		Block.makeCuboidShape(9, 15.5, 9, 9.5, 16, 9.5),Block.makeCuboidShape(7, 15.5, 8.5, 9, 16, 9),
		Block.makeCuboidShape(6.5, 15.5, 9, 7, 16, 9.5),Block.makeCuboidShape(6, 15.5, 9.5, 6.5, 16, 10),
		Block.makeCuboidShape(5.5, 15.5, 10, 6, 16, 10.5),Block.makeCuboidShape(7, 15.5, 7, 9, 16, 7.5),
		Block.makeCuboidShape(9, 15.5, 6.5, 9.5, 16, 7),Block.makeCuboidShape(9.5, 15.5, 6, 10, 16, 6.5),
		Block.makeCuboidShape(10, 15.5, 5.5, 10.5, 16, 6),Block.makeCuboidShape(6.5, 15.5, 6.5, 7, 16, 7),
		Block.makeCuboidShape(6, 15.5, 6, 6.5, 16, 6.5),Block.makeCuboidShape(5.5, 15.5, 5.5, 6, 16, 6)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_E = Stream.of(
		Block.makeCuboidShape(11, 0.8, 4, 13, 11.9, 5.300000000000001),Block.makeCuboidShape(11, 0.8, 10.7, 13, 11.9, 12),
		Block.makeCuboidShape(4, 0, 12, 12, 12, 13),Block.makeCuboidShape(3, 0.8, 10.7, 5, 11.9, 12),
		Block.makeCuboidShape(3, 0, 3, 4, 1, 13),Block.makeCuboidShape(3, 0.8, 4, 5, 11.9, 5.300000000000001),
		Block.makeCuboidShape(4, 0, 3, 12, 12, 4),Block.makeCuboidShape(5.5, 13.21, 5.5, 10.5, 15.5, 10.5),
		Block.makeCuboidShape(3, 11.4, 5.300000000000001, 4, 11.9, 10.7),Block.makeCuboidShape(12, 11.4, 5.300000000000001, 13, 11.9, 10.7),
		Block.makeCuboidShape(12, 0, 3, 13, 1, 13),Block.makeCuboidShape(12, 1, 5.300000000000001, 13, 1.5, 10.7),
		Block.makeCuboidShape(3, 1, 5.300000000000001, 4, 1.5, 10.7),Block.makeCuboidShape(3.5, 11.8, 4, 12.5, 12.7, 12),
		Block.makeCuboidShape(4, 0, 4, 12, 1, 12),Block.makeCuboidShape(12, 1, 3.5, 12.5, 12.4, 4),
		Block.makeCuboidShape(3.5, 1, 3.5, 4, 12.4, 4),Block.makeCuboidShape(3.5, 1, 12, 4, 12.4, 12.5),
		Block.makeCuboidShape(12, 1, 12, 12.5, 12.4, 12.5),Block.makeCuboidShape(4, 11.9, 12, 12, 12.4, 12.5),
		Block.makeCuboidShape(5, 12.7, 5, 11, 13.2, 11),Block.makeCuboidShape(5, 15.5, 5, 11, 16, 5.5),
		Block.makeCuboidShape(5, 15.5, 10.5, 11, 16, 11),Block.makeCuboidShape(5, 15.5, 5.5, 5.5, 16, 10.5),
		Block.makeCuboidShape(10.5, 15.5, 5.5, 11, 16, 10.5),Block.makeCuboidShape(4, 11.9, 3.5, 12, 12.4, 4),
		Block.makeCuboidShape(2.5, 0, 2.5, 3, 0.5, 13.5),Block.makeCuboidShape(3, 0, 13, 13, 0.5, 13.5),
		Block.makeCuboidShape(3, 0, 2.5, 13, 0.5, 3),Block.makeCuboidShape(13, 0, 2.5, 13.5, 0.5, 13.5),
		Block.makeCuboidShape(10, 15.5, 5.5, 10.5, 16, 6),Block.makeCuboidShape(9.5, 15.5, 6, 10, 16, 6.5),
		Block.makeCuboidShape(9, 15.5, 6.5, 9.5, 16, 7),Block.makeCuboidShape(8.5, 15.5, 7, 9, 16, 9),
		Block.makeCuboidShape(9, 15.5, 9, 9.5, 16, 9.5),Block.makeCuboidShape(9.5, 15.5, 9.5, 10, 16, 10),
		Block.makeCuboidShape(10, 15.5, 10, 10.5, 16, 10.5),Block.makeCuboidShape(7, 15.5, 7, 7.5, 16, 9),
		Block.makeCuboidShape(6.5, 15.5, 6.5, 7, 16, 7),Block.makeCuboidShape(6, 15.5, 6, 6.5, 16, 6.5),
		Block.makeCuboidShape(5.5, 15.5, 5.5, 6, 16, 6),Block.makeCuboidShape(6.5, 15.5, 9, 7, 16, 9.5),
		Block.makeCuboidShape(6, 15.5, 9.5, 6.5, 16, 10),Block.makeCuboidShape(5.5, 15.5, 10, 6, 16, 10.5)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_W = Stream.of(
		Block.makeCuboidShape(11, 0.8, 4, 13, 11.9, 5.300000000000001),Block.makeCuboidShape(11, 0.8, 10.7, 13, 11.9, 12),
		Block.makeCuboidShape(4, 0, 12, 12, 12, 13),Block.makeCuboidShape(3, 0.8, 10.7, 5, 11.9, 12),
		Block.makeCuboidShape(3, 0, 3, 4, 1, 13),Block.makeCuboidShape(3, 0.8, 4, 5, 11.9, 5.300000000000001),
		Block.makeCuboidShape(4, 0, 3, 12, 12, 4),Block.makeCuboidShape(5.5, 13.21, 5.5, 10.5, 15.5, 10.5),
		Block.makeCuboidShape(3, 11.4, 5.300000000000001, 4, 11.9, 10.7),Block.makeCuboidShape(12, 11.4, 5.300000000000001, 13, 11.9, 10.7),
		Block.makeCuboidShape(12, 0, 3, 13, 1, 13),Block.makeCuboidShape(12, 1, 5.300000000000001, 13, 1.5, 10.7),
		Block.makeCuboidShape(3, 1, 5.300000000000001, 4, 1.5, 10.7),Block.makeCuboidShape(3.5, 11.8, 4, 12.5, 12.7, 12),
		Block.makeCuboidShape(4, 0, 4, 12, 1, 12),Block.makeCuboidShape(12, 1, 3.5, 12.5, 12.4, 4),
		Block.makeCuboidShape(3.5, 1, 3.5, 4, 12.4, 4),Block.makeCuboidShape(3.5, 1, 12, 4, 12.4, 12.5),
		Block.makeCuboidShape(12, 1, 12, 12.5, 12.4, 12.5),Block.makeCuboidShape(4, 11.9, 12, 12, 12.4, 12.5),
		Block.makeCuboidShape(5, 12.7, 5, 11, 13.2, 11),Block.makeCuboidShape(5, 15.5, 5, 11, 16, 5.5),
		Block.makeCuboidShape(5, 15.5, 10.5, 11, 16, 11),Block.makeCuboidShape(5, 15.5, 5.5, 5.5, 16, 10.5),
		Block.makeCuboidShape(10.5, 15.5, 5.5, 11, 16, 10.5),Block.makeCuboidShape(4, 11.9, 3.5, 12, 12.4, 4),
		Block.makeCuboidShape(2.5, 0, 2.5, 3, 0.5, 13.5),Block.makeCuboidShape(3, 0, 13, 13, 0.5, 13.5),
		Block.makeCuboidShape(3, 0, 2.5, 13, 0.5, 3),Block.makeCuboidShape(13, 0, 2.5, 13.5, 0.5, 13.5),
		Block.makeCuboidShape(10, 15.5, 5.5, 10.5, 16, 6),Block.makeCuboidShape(9.5, 15.5, 6, 10, 16, 6.5),
		Block.makeCuboidShape(9, 15.5, 6.5, 9.5, 16, 7),Block.makeCuboidShape(8.5, 15.5, 7, 9, 16, 9),
		Block.makeCuboidShape(9, 15.5, 9, 9.5, 16, 9.5),Block.makeCuboidShape(9.5, 15.5, 9.5, 10, 16, 10),
		Block.makeCuboidShape(10, 15.5, 10, 10.5, 16, 10.5),Block.makeCuboidShape(7, 15.5, 7, 7.5, 16, 9),
		Block.makeCuboidShape(6.5, 15.5, 6.5, 7, 16, 7),Block.makeCuboidShape(6, 15.5, 6, 6.5, 16, 6.5),
		Block.makeCuboidShape(5.5, 15.5, 5.5, 6, 16, 6),Block.makeCuboidShape(6.5, 15.5, 9, 7, 16, 9.5),
		Block.makeCuboidShape(6, 15.5, 9.5, 6.5, 16, 10),Block.makeCuboidShape(5.5, 15.5, 10, 6, 16, 10.5)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	
	public static int fluidamount;

	public FluidTankBlock() {
		super(Block.Properties.create(Material.IRON)
				.hardnessAndResistance(5.0f, 2.000f)
				.sound(SoundType.METAL)
				.harvestLevel(1)
				.harvestTool(ToolType.PICKAXE)
				.setRequiresTool()
		);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (tileEntity instanceof TileFluidTankBlock) {
            FluidStack tankFluidStack = ((TileFluidTankBlock) tileEntity).getTank().getFluid();
            fluidamount = tankFluidStack.getAmount();
        }
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);

            if (te instanceof TileFluidTankBlock) {
                FluidTank tankFluidTank = ((TileFluidTankBlock) te).getTank();
                tankFluidTank.fill(new FluidStack(RegistryHandler.FLUX_FLUID.get(), fluidamount), IFluidHandler.FluidAction.EXECUTE);
            }
		}
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return RegistryHandler.FLUIDTANK_BLOCK_TILE.get().create();
	}
	
	@Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity != null) {
                LazyOptional<IFluidHandler> fluidHandlerCap = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);

                if (fluidHandlerCap.isPresent()) {
                    IFluidHandler fluidHandler = fluidHandlerCap.orElseThrow(IllegalStateException::new);

                    if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                        if (fluidHandler.drain(500, IFluidHandler.FluidAction.SIMULATE).getAmount() == 500) {
                            fluidHandler.drain(500, IFluidHandler.FluidAction.EXECUTE);

                            player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1f, 1f);

                            heldItem.shrink(1);
                            ItemStack itemPotion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);

                            if (!player.addItemStackToInventory(itemPotion)) {
                                spawnAsEntity(world, player.getPosition(), itemPotion);
                            }

                            return ActionResultType.SUCCESS;
                        }
                    } else if (heldItem.getItem() == Items.POTION && heldItem.getTag() != null) {
                        if (heldItem.getTag().getString("Potion").equals("minecraft:water")) {
                            if (fluidHandler.fill(new FluidStack(RegistryHandler.FLUX_FLUID.get(), 500), IFluidHandler.FluidAction.SIMULATE) == 500) {
                                fluidHandler.fill(new FluidStack(RegistryHandler.FLUX_FLUID.get(), 500), IFluidHandler.FluidAction.EXECUTE);

                                player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.PLAYERS, 1f, 1f);

                                heldItem.shrink(1);
                                ItemStack itemBottle = new ItemStack(Items.GLASS_BOTTLE);

                                if (!player.addItemStackToInventory(itemBottle)) {
                                    spawnAsEntity(world, player.getPosition(), itemBottle);
                                }

                                return ActionResultType.SUCCESS;
                            }
                        }
                    } else {
                    	if(heldItem.getItem() == Items.BUCKET) {
                    		if(!fluidHandler.drain(1000, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                    			player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 1f, 1f);
                    		}
                    		return (FluidUtil.interactWithFluidHandler(player, hand, fluidHandler)) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
                    	} else if(heldItem.getItem() == RegistryHandler.FLUX_FLUID_BUCKET.get()) {
                    		if(fluidHandler.fill(new FluidStack(RegistryHandler.FLUX_FLUID.get(), 1000), IFluidHandler.FluidAction.SIMULATE) == 1000) {
                    			player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.PLAYERS, 1f, 1f);
                    		}
                    		return (FluidUtil.interactWithFluidHandler(player, hand, fluidHandler)) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
                    	} else {
                    		return(ActionResultType.FAIL);
                    	}
                    }
                }
            }
        }

        return ActionResultType.SUCCESS;
    }
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(FACING)) {
		case NORTH:
			return SHAPE_N;
		case SOUTH:
			return SHAPE_S;
		case EAST:
			return SHAPE_E;
		case WEST:
			return SHAPE_W;
		default:
			return SHAPE_N;
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 0.8f;
	}

}
