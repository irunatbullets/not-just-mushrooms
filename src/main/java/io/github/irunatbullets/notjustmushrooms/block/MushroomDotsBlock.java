package io.github.irunatbullets.notjustmushrooms.block;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.Property;

public class MushroomDotsBlock extends Block {

    public static final DirectionProperty FACING =
            DirectionProperty.create("facing", Direction.values());

    private static final VoxelShape NORTH_SHAPE = Block.box(2, 3, 13, 14, 14, 16);
    private static final VoxelShape SOUTH_SHAPE = Block.box(2, 3, 0, 14, 14, 3);
    private static final VoxelShape WEST_SHAPE = Block.box(13, 3, 2, 16, 14, 14);
    private static final VoxelShape EAST_SHAPE = Block.box(0, 3, 2, 3, 14, 14);
    private static final VoxelShape DOWN_SHAPE = Block.box(2, 13, 2, 14, 16, 14); // ceiling
    private static final VoxelShape UP_SHAPE = Block.box(2, 0, 2, 14, 3, 14);

    public MushroomDotsBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public MushroomDotsBlock() {
        this(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .noOcclusion()
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            case DOWN -> DOWN_SHAPE;
            case UP -> UP_SHAPE;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.getClickedFace();
        BlockState state = defaultBlockState().setValue(FACING, face);

        return state.canSurvive(context.getLevel(), context.getClickedPos())
                ? state
                : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos attachedPos = pos.relative(facing.getOpposite());
        BlockState attachedBlock = world.getBlockState(attachedPos);

        return attachedBlock.isFaceSturdy(world, attachedPos, facing);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState,
                                  LevelAccessor world, BlockPos currentPos, BlockPos neighborPos) {
        if (facing == state.getValue(FACING).getOpposite() && !state.canSurvive(world, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        return List.of(new ItemStack(Items.RED_MUSHROOM, 3));
    }
}
