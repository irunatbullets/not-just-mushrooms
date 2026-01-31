package io.github.irunatbullets.notjustmushrooms.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

public class MushroomLampBlock extends Block {

    public static final DirectionProperty FACING =
            DirectionProperty.create("facing", Direction.values());

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");

    private static final VoxelShape NORTH = Block.box(2, 3, 13, 14, 14, 16);
    private static final VoxelShape SOUTH = Block.box(2, 3, 0, 14, 14, 3);
    private static final VoxelShape WEST  = Block.box(13, 3, 2, 16, 14, 14);
    private static final VoxelShape EAST  = Block.box(0, 3, 2, 3, 14, 14);
    private static final VoxelShape DOWN  = Block.box(2, 13, 2, 14, 16, 14);
    private static final VoxelShape UP    = Block.box(2, 0, 2, 14, 3, 14);

    public MushroomLampBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .noOcclusion()
                .lightLevel(state -> state.getValue(LIT) ? 15 : 0)
        );

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(INVERTED, false)
                .setValue(LIT, false)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST  -> WEST;
            case EAST  -> EAST;
            case DOWN  -> DOWN;
            case UP    -> UP;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction face = ctx.getClickedFace();
        boolean inverted = ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown();

        BlockState state = defaultBlockState()
                .setValue(FACING, face)
                .setValue(INVERTED, inverted);

        if (!state.canSurvive(ctx.getLevel(), ctx.getClickedPos())) {
            return null;
        }

        boolean powered = ctx.getLevel().hasNeighborSignal(ctx.getClickedPos());
        boolean lit = inverted ? !powered : powered;

        return state.setValue(LIT, lit);
    }


    private boolean shouldBeLit(Level level, BlockPos pos, BlockState state) {
        boolean powered = level.hasNeighborSignal(pos);
        return state.getValue(INVERTED) ? !powered : powered;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean moving) {

        boolean lit = shouldBeLit(level, pos, state);
        if (state.getValue(LIT) != lit) {
            level.setBlock(pos, state.setValue(LIT, lit), 3);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos support = pos.relative(facing.getOpposite());
        return world.getBlockState(support)
                .isFaceSturdy(world, support, facing);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor,
                                  LevelAccessor world, BlockPos pos, BlockPos neighborPos) {

        if (dir == state.getValue(FACING).getOpposite()
                && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;
        if (random.nextFloat() > 0.25f) return;

        Direction facing = state.getValue(FACING);
        Direction out = facing.getOpposite(); // ← THIS is the important bit

        // Block center
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        // Push toward the actual lamp model
        double faceOffset = 0.125;
        x += out.getStepX() * faceOffset;
        y += out.getStepY() * faceOffset;
        z += out.getStepZ() * faceOffset;

        // Jitter only in the face plane
        double jitter = 0.10;

        if (out.getAxis() != Direction.Axis.X) {
            x += (random.nextDouble() - 0.5) * jitter;
        }
        if (out.getAxis() != Direction.Axis.Y) {
            y += (random.nextDouble() - 0.5) * jitter;
        }
        if (out.getAxis() != Direction.Axis.Z) {
            z += (random.nextDouble() - 0.5) * jitter;
        }

        // Gentle upward drift
        double vx = 0.0;
        double vy = 0.004 + random.nextDouble() * 0.004;
        double vz = 0.0;

        Vector3f color = random.nextBoolean()
                ? new Vector3f(0.980f, 1.000f, 1.000f) // #faffff
                : new Vector3f(0.973f, 1.000f, 0.722f); // #f8ffb8

        level.addParticle(
                new DustParticleOptions(color, 0.75f),
                x, y, z,
                vx, vy, vz
        );
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, INVERTED);
    }
}
