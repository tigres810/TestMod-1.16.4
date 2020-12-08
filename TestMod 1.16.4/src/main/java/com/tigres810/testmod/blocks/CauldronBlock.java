package com.tigres810.testmod.blocks;

import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class CauldronBlock extends Block {
	
	private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE_N = Stream.of(
		Block.makeCuboidShape(2.9901, 3, 4, 3.9901, 13, 12),Block.makeCuboidShape(3, 2, 3, 13, 3, 13),
		Block.makeCuboidShape(4, 3, 12, 12, 13, 13),Block.makeCuboidShape(3.5, 3, 12, 4, 13.5, 12.5),
		Block.makeCuboidShape(3.5, 3, 3.5, 4, 13.5, 4),Block.makeCuboidShape(12, 3, 12, 12.5, 13.5, 12.5),
		Block.makeCuboidShape(12, 13, 4, 12.5, 13.5, 12),Block.makeCuboidShape(3.5, 13, 4, 4, 13.5, 12),
		Block.makeCuboidShape(4, 13, 12, 12, 13.5, 12.5),Block.makeCuboidShape(4, 13, 3.5, 12, 13.5, 4),
		Block.makeCuboidShape(12, 3, 3.5, 12.5, 13.5, 4),Block.makeCuboidShape(2.5, 2, 2.5, 13.5, 2.5, 3),
		Block.makeCuboidShape(2.5, 2, 13, 13.5, 2.5, 13.5),Block.makeCuboidShape(2.5, 2, 3, 3, 2.5, 13),
		Block.makeCuboidShape(13, 2, 3, 13.5, 2.5, 13),Block.makeCuboidShape(4, 3, 3, 12, 13, 4),
		Block.makeCuboidShape(4, 13, 4, 12, 14, 5),Block.makeCuboidShape(11, 13, 5, 12, 14, 11),
		Block.makeCuboidShape(4, 13, 5, 5, 14, 11),Block.makeCuboidShape(4, 13, 11, 12, 14, 12),
		Block.makeCuboidShape(12.01, 3, 4, 13.01, 13, 12),Block.makeCuboidShape(13, 0, 3, 15, 2, 5),
		Block.makeCuboidShape(13, 0, 7, 15, 2, 9),Block.makeCuboidShape(13, 0, 11, 15, 2, 13),
		Block.makeCuboidShape(3, 0, 3, 13, 2, 13),Block.makeCuboidShape(11, 0, 13, 13, 2, 15),
		Block.makeCuboidShape(7, 0, 13, 9, 2, 15),Block.makeCuboidShape(3, 0, 13, 5, 2, 15),
		Block.makeCuboidShape(1, 0, 11, 3, 2, 13),Block.makeCuboidShape(1, 0, 7, 3, 2, 9),
		Block.makeCuboidShape(1, 0, 3, 3, 2, 5),Block.makeCuboidShape(3, 0, 1, 5, 2, 3),
		Block.makeCuboidShape(7, 0, 1, 9, 2, 3),Block.makeCuboidShape(11, 0, 1, 13, 2, 3)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_S = Stream.of(
		Block.makeCuboidShape(2.9901, 3, 4, 3.9901, 13, 12),Block.makeCuboidShape(3, 2, 3, 13, 3, 13),
		Block.makeCuboidShape(4, 3, 12, 12, 13, 13),Block.makeCuboidShape(3.5, 3, 12, 4, 13.5, 12.5),
		Block.makeCuboidShape(3.5, 3, 3.5, 4, 13.5, 4),Block.makeCuboidShape(12, 3, 12, 12.5, 13.5, 12.5),
		Block.makeCuboidShape(12, 13, 4, 12.5, 13.5, 12),Block.makeCuboidShape(3.5, 13, 4, 4, 13.5, 12),
		Block.makeCuboidShape(4, 13, 12, 12, 13.5, 12.5),Block.makeCuboidShape(4, 13, 3.5, 12, 13.5, 4),
		Block.makeCuboidShape(12, 3, 3.5, 12.5, 13.5, 4),Block.makeCuboidShape(2.5, 2, 2.5, 13.5, 2.5, 3),
		Block.makeCuboidShape(2.5, 2, 13, 13.5, 2.5, 13.5),Block.makeCuboidShape(2.5, 2, 3, 3, 2.5, 13),
		Block.makeCuboidShape(13, 2, 3, 13.5, 2.5, 13),Block.makeCuboidShape(4, 3, 3, 12, 13, 4),
		Block.makeCuboidShape(4, 13, 4, 12, 14, 5),Block.makeCuboidShape(11, 13, 5, 12, 14, 11),
		Block.makeCuboidShape(4, 13, 5, 5, 14, 11),Block.makeCuboidShape(4, 13, 11, 12, 14, 12),
		Block.makeCuboidShape(12.01, 3, 4, 13.01, 13, 12),Block.makeCuboidShape(13, 0, 3, 15, 2, 5),
		Block.makeCuboidShape(13, 0, 7, 15, 2, 9),Block.makeCuboidShape(13, 0, 11, 15, 2, 13),
		Block.makeCuboidShape(3, 0, 3, 13, 2, 13),Block.makeCuboidShape(11, 0, 13, 13, 2, 15),
		Block.makeCuboidShape(7, 0, 13, 9, 2, 15),Block.makeCuboidShape(3, 0, 13, 5, 2, 15),
		Block.makeCuboidShape(1, 0, 11, 3, 2, 13),Block.makeCuboidShape(1, 0, 7, 3, 2, 9),
		Block.makeCuboidShape(1, 0, 3, 3, 2, 5),Block.makeCuboidShape(3, 0, 1, 5, 2, 3),
		Block.makeCuboidShape(7, 0, 1, 9, 2, 3),Block.makeCuboidShape(11, 0, 1, 13, 2, 3)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_E = Stream.of(
		Block.makeCuboidShape(2.9901, 3, 4, 3.9901, 13, 12),Block.makeCuboidShape(3, 2, 3, 13, 3, 13),
		Block.makeCuboidShape(4, 3, 12, 12, 13, 13),Block.makeCuboidShape(3.5, 3, 12, 4, 13.5, 12.5),
		Block.makeCuboidShape(3.5, 3, 3.5, 4, 13.5, 4),Block.makeCuboidShape(12, 3, 12, 12.5, 13.5, 12.5),
		Block.makeCuboidShape(12, 13, 4, 12.5, 13.5, 12),Block.makeCuboidShape(3.5, 13, 4, 4, 13.5, 12),
		Block.makeCuboidShape(4, 13, 12, 12, 13.5, 12.5),Block.makeCuboidShape(4, 13, 3.5, 12, 13.5, 4),
		Block.makeCuboidShape(12, 3, 3.5, 12.5, 13.5, 4),Block.makeCuboidShape(2.5, 2, 2.5, 13.5, 2.5, 3),
		Block.makeCuboidShape(2.5, 2, 13, 13.5, 2.5, 13.5),Block.makeCuboidShape(2.5, 2, 3, 3, 2.5, 13),
		Block.makeCuboidShape(13, 2, 3, 13.5, 2.5, 13),Block.makeCuboidShape(4, 3, 3, 12, 13, 4),
		Block.makeCuboidShape(4, 13, 4, 12, 14, 5),Block.makeCuboidShape(11, 13, 5, 12, 14, 11),
		Block.makeCuboidShape(4, 13, 5, 5, 14, 11),Block.makeCuboidShape(4, 13, 11, 12, 14, 12),
		Block.makeCuboidShape(12.01, 3, 4, 13.01, 13, 12),Block.makeCuboidShape(13, 0, 3, 15, 2, 5),
		Block.makeCuboidShape(13, 0, 7, 15, 2, 9),Block.makeCuboidShape(13, 0, 11, 15, 2, 13),
		Block.makeCuboidShape(3, 0, 3, 13, 2, 13),Block.makeCuboidShape(11, 0, 13, 13, 2, 15),
		Block.makeCuboidShape(7, 0, 13, 9, 2, 15),Block.makeCuboidShape(3, 0, 13, 5, 2, 15),
		Block.makeCuboidShape(1, 0, 11, 3, 2, 13),Block.makeCuboidShape(1, 0, 7, 3, 2, 9),
		Block.makeCuboidShape(1, 0, 3, 3, 2, 5),Block.makeCuboidShape(3, 0, 1, 5, 2, 3),
		Block.makeCuboidShape(7, 0, 1, 9, 2, 3),Block.makeCuboidShape(11, 0, 1, 13, 2, 3)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_W = Stream.of(
		Block.makeCuboidShape(2.9901, 3, 4, 3.9901, 13, 12),Block.makeCuboidShape(3, 2, 3, 13, 3, 13),
		Block.makeCuboidShape(4, 3, 12, 12, 13, 13),Block.makeCuboidShape(3.5, 3, 12, 4, 13.5, 12.5),
		Block.makeCuboidShape(3.5, 3, 3.5, 4, 13.5, 4),Block.makeCuboidShape(12, 3, 12, 12.5, 13.5, 12.5),
		Block.makeCuboidShape(12, 13, 4, 12.5, 13.5, 12),Block.makeCuboidShape(3.5, 13, 4, 4, 13.5, 12),
		Block.makeCuboidShape(4, 13, 12, 12, 13.5, 12.5),Block.makeCuboidShape(4, 13, 3.5, 12, 13.5, 4),
		Block.makeCuboidShape(12, 3, 3.5, 12.5, 13.5, 4),Block.makeCuboidShape(2.5, 2, 2.5, 13.5, 2.5, 3),
		Block.makeCuboidShape(2.5, 2, 13, 13.5, 2.5, 13.5),Block.makeCuboidShape(2.5, 2, 3, 3, 2.5, 13),
		Block.makeCuboidShape(13, 2, 3, 13.5, 2.5, 13),Block.makeCuboidShape(4, 3, 3, 12, 13, 4),
		Block.makeCuboidShape(4, 13, 4, 12, 14, 5),Block.makeCuboidShape(11, 13, 5, 12, 14, 11),
		Block.makeCuboidShape(4, 13, 5, 5, 14, 11),Block.makeCuboidShape(4, 13, 11, 12, 14, 12),
		Block.makeCuboidShape(12.01, 3, 4, 13.01, 13, 12),Block.makeCuboidShape(13, 0, 3, 15, 2, 5),
		Block.makeCuboidShape(13, 0, 7, 15, 2, 9),Block.makeCuboidShape(13, 0, 11, 15, 2, 13),
		Block.makeCuboidShape(3, 0, 3, 13, 2, 13),Block.makeCuboidShape(11, 0, 13, 13, 2, 15),
		Block.makeCuboidShape(7, 0, 13, 9, 2, 15),Block.makeCuboidShape(3, 0, 13, 5, 2, 15),
		Block.makeCuboidShape(1, 0, 11, 3, 2, 13),Block.makeCuboidShape(1, 0, 7, 3, 2, 9),
		Block.makeCuboidShape(1, 0, 3, 3, 2, 5),Block.makeCuboidShape(3, 0, 1, 5, 2, 3),
		Block.makeCuboidShape(7, 0, 1, 9, 2, 3),Block.makeCuboidShape(11, 0, 1, 13, 2, 3)
		).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

	public CauldronBlock() {
		super(Block.Properties.create(Material.IRON)
				.hardnessAndResistance(10.0f, 6.000f)
				.sound(SoundType.METAL)
				.harvestLevel(1)
				.harvestTool(ToolType.PICKAXE)
				.setRequiresTool()
		);
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
		return 1f;
	}

}
