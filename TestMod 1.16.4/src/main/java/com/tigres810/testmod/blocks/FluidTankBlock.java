package com.tigres810.testmod.blocks;

import java.util.stream.Stream;

import com.tigres810.testmod.util.RegistryHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
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

public class FluidTankBlock extends Block {
	
	private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE_N = Stream.of(
			Block.makeCuboidShape(10.7, 0.8, 11, 12, 11.9, 13),Block.makeCuboidShape(4, 0.8, 11, 5.3, 11.9, 13),
			Block.makeCuboidShape(3, 0, 4, 4, 12, 12),Block.makeCuboidShape(4, 0.8, 3, 5.3, 11.9, 5),
			Block.makeCuboidShape(3, 0, 3, 13, 1, 4),Block.makeCuboidShape(10.7, 0.8, 3, 12, 11.9, 5),
			Block.makeCuboidShape(12, 0, 4, 13, 12, 12),Block.makeCuboidShape(5.3, 11.4, 3, 10.7, 11.9, 4),
			Block.makeCuboidShape(5.3, 11.4, 12, 10.7, 11.9, 13),Block.makeCuboidShape(3, 0, 12, 13, 1, 13),
			Block.makeCuboidShape(5.3, 1, 12, 10.7, 1.5, 13),Block.makeCuboidShape(5.3, 1, 3, 10.7, 1.5, 4),
			Block.makeCuboidShape(4, 11.8, 3.5, 12, 12.7, 12.5),Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
			Block.makeCuboidShape(12, 1, 12, 12.5, 12.4, 12.5),Block.makeCuboidShape(12, 1, 3.5, 12.5, 12.4, 4),
			Block.makeCuboidShape(3.5, 1, 3.5, 4, 12.4, 4),Block.makeCuboidShape(3.5, 1, 12, 4, 12.4, 12.5),
			Block.makeCuboidShape(3.5, 11.9, 4, 4, 12.4, 12),Block.makeCuboidShape(12, 11.9, 4, 12.5, 12.4, 12),
			Block.makeCuboidShape(2.5, 0, 2.5, 13.5, 0.5, 3),Block.makeCuboidShape(2.5, 0, 3, 3, 0.5, 13),
			Block.makeCuboidShape(13, 0, 3, 13.5, 0.5, 13),Block.makeCuboidShape(2.5, 0, 13, 13.5, 0.5, 13.5)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_S = Stream.of(
			Block.makeCuboidShape(10.7, 0.8, 11, 12, 11.9, 13),Block.makeCuboidShape(4, 0.8, 11, 5.3, 11.9, 13),
			Block.makeCuboidShape(3, 0, 4, 4, 12, 12),Block.makeCuboidShape(4, 0.8, 3, 5.3, 11.9, 5),
			Block.makeCuboidShape(3, 0, 3, 13, 1, 4),Block.makeCuboidShape(10.7, 0.8, 3, 12, 11.9, 5),
			Block.makeCuboidShape(12, 0, 4, 13, 12, 12),Block.makeCuboidShape(5.3, 11.4, 3, 10.7, 11.9, 4),
			Block.makeCuboidShape(5.3, 11.4, 12, 10.7, 11.9, 13),Block.makeCuboidShape(3, 0, 12, 13, 1, 13),
			Block.makeCuboidShape(5.3, 1, 12, 10.7, 1.5, 13),Block.makeCuboidShape(5.3, 1, 3, 10.7, 1.5, 4),
			Block.makeCuboidShape(4, 11.8, 3.5, 12, 12.7, 12.5),Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
			Block.makeCuboidShape(12, 1, 12, 12.5, 12.4, 12.5),Block.makeCuboidShape(12, 1, 3.5, 12.5, 12.4, 4),
			Block.makeCuboidShape(3.5, 1, 3.5, 4, 12.4, 4),Block.makeCuboidShape(3.5, 1, 12, 4, 12.4, 12.5),
			Block.makeCuboidShape(3.5, 11.9, 4, 4, 12.4, 12),Block.makeCuboidShape(12, 11.9, 4, 12.5, 12.4, 12),
			Block.makeCuboidShape(2.5, 0, 2.5, 13.5, 0.5, 3),Block.makeCuboidShape(2.5, 0, 3, 3, 0.5, 13),
			Block.makeCuboidShape(13, 0, 3, 13.5, 0.5, 13),Block.makeCuboidShape(2.5, 0, 13, 13.5, 0.5, 13.5)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_E = Stream.of(
			Block.makeCuboidShape(3, 0.8, 10.7, 5, 11.9, 12),Block.makeCuboidShape(3, 0.8, 4, 5, 11.9, 5.3),
			Block.makeCuboidShape(4, 0, 3, 12, 12, 4),Block.makeCuboidShape(11, 0.8, 4, 13, 11.9, 5.3),
			Block.makeCuboidShape(12, 0, 3, 13, 1, 13),Block.makeCuboidShape(11, 0.8, 10.7, 13, 11.9, 12),
			Block.makeCuboidShape(4, 0, 12, 12, 12, 13),Block.makeCuboidShape(12, 11.4, 5.3, 13, 11.9, 10.7),
			Block.makeCuboidShape(3, 11.4, 5.3, 4, 11.9, 10.7),Block.makeCuboidShape(3, 0, 3, 4, 1, 13),
			Block.makeCuboidShape(3, 1, 5.3, 4, 1.5, 10.7),Block.makeCuboidShape(12, 1, 5.3, 13, 1.5, 10.7),
			Block.makeCuboidShape(3.5, 11.8, 4, 12.5, 12.7, 12),Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
			Block.makeCuboidShape(3.5, 1, 12, 4, 12.4, 12.5),Block.makeCuboidShape(12, 1, 12, 12.5, 12.4, 12.5),
			Block.makeCuboidShape(12, 1, 3.5, 12.5, 12.4, 4),Block.makeCuboidShape(3.5, 1, 3.5, 4, 12.4, 4),
			Block.makeCuboidShape(4, 11.9, 3.5, 12, 12.4, 4),Block.makeCuboidShape(4, 11.9, 12, 12, 12.4, 12.5),
			Block.makeCuboidShape(13, 0, 2.5, 13.5, 0.5, 13.5),Block.makeCuboidShape(3, 0, 2.5, 13, 0.5, 3),
			Block.makeCuboidShape(3, 0, 13, 13, 0.5, 13.5),Block.makeCuboidShape(2.5, 0, 2.5, 3, 0.5, 13.5)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_W = Stream.of(
			Block.makeCuboidShape(3, 0.8, 10.7, 5, 11.9, 12),Block.makeCuboidShape(3, 0.8, 4, 5, 11.9, 5.3),
			Block.makeCuboidShape(4, 0, 3, 12, 12, 4),Block.makeCuboidShape(11, 0.8, 4, 13, 11.9, 5.3),
			Block.makeCuboidShape(12, 0, 3, 13, 1, 13),Block.makeCuboidShape(11, 0.8, 10.7, 13, 11.9, 12),
			Block.makeCuboidShape(4, 0, 12, 12, 12, 13),Block.makeCuboidShape(12, 11.4, 5.3, 13, 11.9, 10.7),
			Block.makeCuboidShape(3, 11.4, 5.3, 4, 11.9, 10.7),Block.makeCuboidShape(3, 0, 3, 4, 1, 13),
			Block.makeCuboidShape(3, 1, 5.3, 4, 1.5, 10.7),Block.makeCuboidShape(12, 1, 5.3, 13, 1.5, 10.7),
			Block.makeCuboidShape(3.5, 11.8, 4, 12.5, 12.7, 12),Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
			Block.makeCuboidShape(3.5, 1, 12, 4, 12.4, 12.5),Block.makeCuboidShape(12, 1, 12, 12.5, 12.4, 12.5),
			Block.makeCuboidShape(12, 1, 3.5, 12.5, 12.4, 4),Block.makeCuboidShape(3.5, 1, 3.5, 4, 12.4, 4),
			Block.makeCuboidShape(4, 11.9, 3.5, 12, 12.4, 4),Block.makeCuboidShape(4, 11.9, 12, 12, 12.4, 12.5),
			Block.makeCuboidShape(13, 0, 2.5, 13.5, 0.5, 13.5),Block.makeCuboidShape(3, 0, 2.5, 13, 0.5, 3),
			Block.makeCuboidShape(3, 0, 13, 13, 0.5, 13.5),Block.makeCuboidShape(2.5, 0, 2.5, 3, 0.5, 13.5)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

	public FluidTankBlock() {
		super(Block.Properties.create(Material.IRON)
				.hardnessAndResistance(8.0f, 4.000f)
				.sound(SoundType.METAL)
				.harvestLevel(1)
				.harvestTool(ToolType.PICKAXE)
				.setRequiresTool()
		);
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
                        if (fluidHandler.drain(333, IFluidHandler.FluidAction.SIMULATE).getAmount() == 333) {
                            fluidHandler.drain(333, IFluidHandler.FluidAction.EXECUTE);

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
                            if (fluidHandler.fill(new FluidStack(Fluids.WATER, 550), IFluidHandler.FluidAction.SIMULATE) == 550) {
                                fluidHandler.fill(new FluidStack(Fluids.WATER, 550), IFluidHandler.FluidAction.EXECUTE);

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

                        return (FluidUtil.interactWithFluidHandler(player, hand, fluidHandler)) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
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
