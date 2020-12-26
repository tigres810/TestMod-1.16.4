package com.tigres810.testmod.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.tigres810.testmod.items.MagicStickItem;
import com.tigres810.testmod.tileentitys.TileEnergyDispenserBlock;
import com.tigres810.testmod.tileentitys.TileFluidTankBlock;
import com.tigres810.testmod.util.RegistryHandler;
import com.tigres810.testmod.util.interfaces.IPipeConnect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class EnergyDispenserBlock extends Block implements IPipeConnect {
	
	private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE_N = Stream.of(
		Block.makeCuboidShape(5.5, 0.5, 5.5, 10.5, 2.79, 10.5),Block.makeCuboidShape(10.5, 0, 5, 11, 0.5, 11),
		Block.makeCuboidShape(10.5, 2.79, 5, 11, 3.29, 11),Block.makeCuboidShape(5, 0, 5, 5.5, 0.5, 11),
		Block.makeCuboidShape(5, 2.79, 5, 5.5, 3.29, 11),Block.makeCuboidShape(7, 2.79, 7, 7.5, 3.79, 7.5),
		Block.makeCuboidShape(8.5, 2.79, 7, 9, 3.79, 7.5),Block.makeCuboidShape(7, 2.79, 8.5, 7.5, 3.79, 9),
		Block.makeCuboidShape(8.5, 2.79, 8.5, 9, 3.79, 9),Block.makeCuboidShape(5.5, 0, 5, 10.5, 0.5, 5.5),
		Block.makeCuboidShape(5.5, 2.79, 5, 10.5, 3.29, 5.5),Block.makeCuboidShape(5.5, 0, 10.5, 10.5, 0.5, 11),
		Block.makeCuboidShape(5.5, 2.79, 10.5, 10.5, 3.29, 11),Block.makeCuboidShape(10, 0, 10, 10.5, 0.5, 10.5),
		Block.makeCuboidShape(9.5, 0, 9.5, 10, 0.5, 10),Block.makeCuboidShape(9, 0, 9, 9.5, 0.5, 9.5),
		Block.makeCuboidShape(7, 0, 8.5, 9, 0.5, 9),Block.makeCuboidShape(6.5, 0, 9, 7, 0.5, 9.5),
		Block.makeCuboidShape(6, 0, 9.5, 6.5, 0.5, 10),Block.makeCuboidShape(5.5, 0, 10, 6, 0.5, 10.5),
		Block.makeCuboidShape(7, 0, 7, 9, 0.5, 7.5),Block.makeCuboidShape(9, 0, 6.5, 9.5, 0.5, 7),
		Block.makeCuboidShape(9.5, 0, 6, 10, 0.5, 6.5),Block.makeCuboidShape(10, 0, 5.5, 10.5, 0.5, 6),
		Block.makeCuboidShape(9.5, 2.8, 5.5, 10.5, 3.3, 6.5),Block.makeCuboidShape(5.5, 2.8, 5.5, 6.5, 3.3, 6.5),
		Block.makeCuboidShape(6, 3.3, 6, 7, 3.8, 7),Block.makeCuboidShape(6.5, 3.8, 6.5, 7.5, 4.3, 7.5),
		Block.makeCuboidShape(7, 4.3, 7, 9, 4.8, 9),Block.makeCuboidShape(8.5, 3.8, 6.5, 9.5, 4.3, 7.5),
		Block.makeCuboidShape(9, 3.3, 6, 10, 3.8, 7),Block.makeCuboidShape(9.5, 2.8, 9.5, 10.5, 3.3, 10.5),
		Block.makeCuboidShape(5.5, 2.8, 9.5, 6.5, 3.3, 10.5),Block.makeCuboidShape(6, 3.3, 9, 7, 3.8, 10),
		Block.makeCuboidShape(6.5, 3.8, 8.5, 7.5, 4.3, 9.5),Block.makeCuboidShape(8.5, 3.8, 8.5, 9.5, 4.3, 9.5),
		Block.makeCuboidShape(9, 3.3, 9, 10, 3.8, 10),Block.makeCuboidShape(6.5, 0, 6.5, 7, 0.5, 7),
		Block.makeCuboidShape(6, 0, 6, 6.5, 0.5, 6.5),Block.makeCuboidShape(5.5, 0, 5.5, 6, 0.5, 6)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_S = Stream.of(
		Block.makeCuboidShape(5.5, 0.5, 5.5, 10.5, 2.79, 10.5),Block.makeCuboidShape(10.5, 0, 5, 11, 0.5, 11),
		Block.makeCuboidShape(10.5, 2.79, 5, 11, 3.29, 11),Block.makeCuboidShape(5, 0, 5, 5.5, 0.5, 11),
		Block.makeCuboidShape(5, 2.79, 5, 5.5, 3.29, 11),Block.makeCuboidShape(7, 2.79, 7, 7.5, 3.79, 7.5),
		Block.makeCuboidShape(8.5, 2.79, 7, 9, 3.79, 7.5),Block.makeCuboidShape(7, 2.79, 8.5, 7.5, 3.79, 9),
		Block.makeCuboidShape(8.5, 2.79, 8.5, 9, 3.79, 9),Block.makeCuboidShape(5.5, 0, 5, 10.5, 0.5, 5.5),
		Block.makeCuboidShape(5.5, 2.79, 5, 10.5, 3.29, 5.5),Block.makeCuboidShape(5.5, 0, 10.5, 10.5, 0.5, 11),
		Block.makeCuboidShape(5.5, 2.79, 10.5, 10.5, 3.29, 11),Block.makeCuboidShape(10, 0, 10, 10.5, 0.5, 10.5),
		Block.makeCuboidShape(9.5, 0, 9.5, 10, 0.5, 10),Block.makeCuboidShape(9, 0, 9, 9.5, 0.5, 9.5),
		Block.makeCuboidShape(7, 0, 8.5, 9, 0.5, 9),Block.makeCuboidShape(6.5, 0, 9, 7, 0.5, 9.5),
		Block.makeCuboidShape(6, 0, 9.5, 6.5, 0.5, 10),Block.makeCuboidShape(5.5, 0, 10, 6, 0.5, 10.5),
		Block.makeCuboidShape(7, 0, 7, 9, 0.5, 7.5),Block.makeCuboidShape(9, 0, 6.5, 9.5, 0.5, 7),
		Block.makeCuboidShape(9.5, 0, 6, 10, 0.5, 6.5),Block.makeCuboidShape(10, 0, 5.5, 10.5, 0.5, 6),
		Block.makeCuboidShape(9.5, 2.8, 5.5, 10.5, 3.3, 6.5),Block.makeCuboidShape(5.5, 2.8, 5.5, 6.5, 3.3, 6.5),
		Block.makeCuboidShape(6, 3.3, 6, 7, 3.8, 7),Block.makeCuboidShape(6.5, 3.8, 6.5, 7.5, 4.3, 7.5),
		Block.makeCuboidShape(7, 4.3, 7, 9, 4.8, 9),Block.makeCuboidShape(8.5, 3.8, 6.5, 9.5, 4.3, 7.5),
		Block.makeCuboidShape(9, 3.3, 6, 10, 3.8, 7),Block.makeCuboidShape(9.5, 2.8, 9.5, 10.5, 3.3, 10.5),
		Block.makeCuboidShape(5.5, 2.8, 9.5, 6.5, 3.3, 10.5),Block.makeCuboidShape(6, 3.3, 9, 7, 3.8, 10),
		Block.makeCuboidShape(6.5, 3.8, 8.5, 7.5, 4.3, 9.5),Block.makeCuboidShape(8.5, 3.8, 8.5, 9.5, 4.3, 9.5),
		Block.makeCuboidShape(9, 3.3, 9, 10, 3.8, 10),Block.makeCuboidShape(6.5, 0, 6.5, 7, 0.5, 7),
		Block.makeCuboidShape(6, 0, 6, 6.5, 0.5, 6.5),Block.makeCuboidShape(5.5, 0, 5.5, 6, 0.5, 6)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_E = Stream.of(
		Block.makeCuboidShape(5.5, 0.5, 5.5, 10.5, 2.79, 10.5),Block.makeCuboidShape(5, 0, 5, 11, 0.5, 5.5),
		Block.makeCuboidShape(5, 2.79, 5, 11, 3.29, 5.5),Block.makeCuboidShape(5, 0, 10.5, 11, 0.5, 11),
		Block.makeCuboidShape(5, 2.79, 10.5, 11, 3.29, 11),Block.makeCuboidShape(7, 2.79, 8.5, 7.5, 3.79, 9),
		Block.makeCuboidShape(7, 2.79, 7, 7.5, 3.79, 7.5),Block.makeCuboidShape(8.5, 2.79, 8.5, 9, 3.79, 9),
		Block.makeCuboidShape(8.5, 2.79, 7, 9, 3.79, 7.5),Block.makeCuboidShape(5, 0, 5.5, 5.5, 0.5, 10.5),
		Block.makeCuboidShape(5, 2.79, 5.5, 5.5, 3.29, 10.5),Block.makeCuboidShape(10.5, 0, 5.5, 11, 0.5, 10.5),
		Block.makeCuboidShape(10.5, 2.79, 5.5, 11, 3.29, 10.5),Block.makeCuboidShape(10, 0, 5.5, 10.5, 0.5, 6),
		Block.makeCuboidShape(9.5, 0, 6, 10, 0.5, 6.5),Block.makeCuboidShape(9, 0, 6.5, 9.5, 0.5, 7),
		Block.makeCuboidShape(8.5, 0, 7, 9, 0.5, 9),Block.makeCuboidShape(9, 0, 9, 9.5, 0.5, 9.5),
		Block.makeCuboidShape(9.5, 0, 9.5, 10, 0.5, 10),Block.makeCuboidShape(10, 0, 10, 10.5, 0.5, 10.5),
		Block.makeCuboidShape(7, 0, 7, 7.5, 0.5, 9),Block.makeCuboidShape(6.5, 0, 6.5, 7, 0.5, 7),
		Block.makeCuboidShape(6, 0, 6, 6.5, 0.5, 6.5),Block.makeCuboidShape(5.5, 0, 5.5, 6, 0.5, 6),
		Block.makeCuboidShape(5.5, 2.8, 5.5, 6.5, 3.3, 6.5),Block.makeCuboidShape(5.5, 2.8, 9.5, 6.5, 3.3, 10.5),
		Block.makeCuboidShape(6, 3.3, 9, 7, 3.8, 10),Block.makeCuboidShape(6.5, 3.8, 8.5, 7.5, 4.3, 9.5),
		Block.makeCuboidShape(7, 4.3, 7, 9, 4.8, 9),Block.makeCuboidShape(6.5, 3.8, 6.5, 7.5, 4.3, 7.5),
		Block.makeCuboidShape(6, 3.3, 6, 7, 3.8, 7),Block.makeCuboidShape(9.5, 2.8, 5.5, 10.5, 3.3, 6.5),
		Block.makeCuboidShape(9.5, 2.8, 9.5, 10.5, 3.3, 10.5),Block.makeCuboidShape(9, 3.3, 9, 10, 3.8, 10),
		Block.makeCuboidShape(8.5, 3.8, 8.5, 9.5, 4.3, 9.5),Block.makeCuboidShape(8.5, 3.8, 6.5, 9.5, 4.3, 7.5),
		Block.makeCuboidShape(9, 3.3, 6, 10, 3.8, 7),Block.makeCuboidShape(6.5, 0, 9, 7, 0.5, 9.5),
		Block.makeCuboidShape(6, 0, 9.5, 6.5, 0.5, 10),Block.makeCuboidShape(5.5, 0, 10, 6, 0.5, 10.5)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_W = Stream.of(
		Block.makeCuboidShape(5.5, 0.5, 5.5, 10.5, 2.79, 10.5),Block.makeCuboidShape(5, 0, 5, 11, 0.5, 5.5),
		Block.makeCuboidShape(5, 2.79, 5, 11, 3.29, 5.5),Block.makeCuboidShape(5, 0, 10.5, 11, 0.5, 11),
		Block.makeCuboidShape(5, 2.79, 10.5, 11, 3.29, 11),Block.makeCuboidShape(7, 2.79, 8.5, 7.5, 3.79, 9),
		Block.makeCuboidShape(7, 2.79, 7, 7.5, 3.79, 7.5),Block.makeCuboidShape(8.5, 2.79, 8.5, 9, 3.79, 9),
		Block.makeCuboidShape(8.5, 2.79, 7, 9, 3.79, 7.5),Block.makeCuboidShape(5, 0, 5.5, 5.5, 0.5, 10.5),
		Block.makeCuboidShape(5, 2.79, 5.5, 5.5, 3.29, 10.5),Block.makeCuboidShape(10.5, 0, 5.5, 11, 0.5, 10.5),
		Block.makeCuboidShape(10.5, 2.79, 5.5, 11, 3.29, 10.5),Block.makeCuboidShape(10, 0, 5.5, 10.5, 0.5, 6),
		Block.makeCuboidShape(9.5, 0, 6, 10, 0.5, 6.5),Block.makeCuboidShape(9, 0, 6.5, 9.5, 0.5, 7),
		Block.makeCuboidShape(8.5, 0, 7, 9, 0.5, 9),Block.makeCuboidShape(9, 0, 9, 9.5, 0.5, 9.5),
		Block.makeCuboidShape(9.5, 0, 9.5, 10, 0.5, 10),Block.makeCuboidShape(10, 0, 10, 10.5, 0.5, 10.5),
		Block.makeCuboidShape(7, 0, 7, 7.5, 0.5, 9),Block.makeCuboidShape(6.5, 0, 6.5, 7, 0.5, 7),
		Block.makeCuboidShape(6, 0, 6, 6.5, 0.5, 6.5),Block.makeCuboidShape(5.5, 0, 5.5, 6, 0.5, 6),
		Block.makeCuboidShape(5.5, 2.8, 5.5, 6.5, 3.3, 6.5),Block.makeCuboidShape(5.5, 2.8, 9.5, 6.5, 3.3, 10.5),
		Block.makeCuboidShape(6, 3.3, 9, 7, 3.8, 10),Block.makeCuboidShape(6.5, 3.8, 8.5, 7.5, 4.3, 9.5),
		Block.makeCuboidShape(7, 4.3, 7, 9, 4.8, 9),Block.makeCuboidShape(6.5, 3.8, 6.5, 7.5, 4.3, 7.5),
		Block.makeCuboidShape(6, 3.3, 6, 7, 3.8, 7),Block.makeCuboidShape(9.5, 2.8, 5.5, 10.5, 3.3, 6.5),
		Block.makeCuboidShape(9.5, 2.8, 9.5, 10.5, 3.3, 10.5),Block.makeCuboidShape(9, 3.3, 9, 10, 3.8, 10),
		Block.makeCuboidShape(8.5, 3.8, 8.5, 9.5, 4.3, 9.5),Block.makeCuboidShape(8.5, 3.8, 6.5, 9.5, 4.3, 7.5),
		Block.makeCuboidShape(9, 3.3, 6, 10, 3.8, 7),Block.makeCuboidShape(6.5, 0, 9, 7, 0.5, 9.5),
		Block.makeCuboidShape(6, 0, 9.5, 6.5, 0.5, 10),Block.makeCuboidShape(5.5, 0, 10, 6, 0.5, 10.5)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

	public EnergyDispenserBlock() {
		super(Block.Properties.create(Material.IRON)
				.hardnessAndResistance(5.0f, 3.000f)
				.sound(SoundType.METAL)
				.harvestLevel(1)
				.harvestTool(ToolType.PICKAXE)
				.setRequiresTool()
		);
	}
	
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos.down());
			if(te != null) {
				if (te instanceof TileFluidTankBlock) {
					Direction bl = te.getBlockState().get(FACING);
	
	            	if(bl != state.get(FACING)) {
	            		if(bl.getOpposite() != state.get(FACING)) {
	            			worldIn.destroyBlock(pos, true);
	            		}
	            	}
	            }
			}
		}
	}
	
	@Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isRemote) {
        	Item handitem = player.getHeldItem(hand).getItem();
        	if(handitem == RegistryHandler.MAGIC_STICK.get()) {
        		if(((MagicStickItem) handitem).getposition1() != pos) {
        			if(((MagicStickItem) handitem).getposition2() != pos) {
		        		((MagicStickItem) handitem).setpositions(pos, world, player);
        			}
        		}
        	}
        }
        
        return ActionResultType.SUCCESS;
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		
		if(tileentity instanceof TileEnergyDispenserBlock) {
			BlockPos connectedto = ((TileEnergyDispenserBlock) tileentity).getConnectedTo();
			TileEntity connectedtotileentity = worldIn.getTileEntity(connectedto);
			
			if(connectedtotileentity != null) {
				((TileEnergyDispenserBlock) connectedtotileentity).setConnectedTo(BlockPos.ZERO);
			}
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return RegistryHandler.ENERGYDISPENSER_BLOCK_TILE.get().create();
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
		return 0.6f;
	}

	@Override
	public List<Direction> getConnectableSides(BlockState state) {
		List<Direction> faces = new ArrayList<Direction>();
		faces.add(Direction.UP);
		return faces;
	}

}