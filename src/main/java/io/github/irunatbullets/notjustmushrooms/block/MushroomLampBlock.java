package io.github.irunatbullets.notjustmushrooms.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Mushroom Lamp block.
 *
 * Wall / ceiling / floor mounted lamp with:
 * - Directional placement
 * - Redstone-powered lighting
 * - Optional inverted logic
 * - Toggle inversion via redstone torch interaction
 */
public class MushroomLampBlock extends Block {

    // ---------------------------------------------------------------------
    // Block state properties
    // ---------------------------------------------------------------------

    /** Facing direction (all 6 sides) */
    public static final DirectionProperty FACING =
            DirectionProperty.create("facing", Direction.values());

    /** Whether the lamp is emitting light */
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    /** Inverts redstone logic when true */
    public static final BooleanProperty INVERTED =
            BooleanProperty.create("inverted");

    // ---------------------------------------------------------------------
    // Shapes (per facing)
    // ---------------------------------------------------------------------

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

        // Default block state
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(INVERTED, false)
                .setValue(LIT, false)
        );
    }

    // ---------------------------------------------------------------------
    // Shapes
    // ---------------------------------------------------------------------

    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext ctx
    ) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST  -> WEST;
            case EAST  -> EAST;
            case DOWN  -> DOWN;
            case UP    -> UP;
        };
    }

    // ---------------------------------------------------------------------
    // Placement
    // ---------------------------------------------------------------------

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction face = ctx.getClickedFace();
        boolean inverted =
                ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown();

        BlockState state = defaultBlockState()
                .setValue(FACING, face)
                .setValue(INVERTED, inverted);

        // Abort placement if not supported
        if (!state.canSurvive(ctx.getLevel(), ctx.getClickedPos())) {
            return null;
        }

        boolean powered = ctx.getLevel().hasNeighborSignal(ctx.getClickedPos());
        boolean lit = inverted ? !powered : powered;

        return state.setValue(LIT, lit);
    }

    // ---------------------------------------------------------------------
    // Redstone logic
    // ---------------------------------------------------------------------

    /** Determines whether the lamp should currently be lit */
    private boolean shouldBeLit(Level level, BlockPos pos, BlockState state) {
        boolean powered = level.hasNeighborSignal(pos);
        return state.getValue(INVERTED) ? !powered : powered;
    }

    @Override
    public void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block block,
            BlockPos fromPos,
            boolean moving
    ) {
        boolean lit = shouldBeLit(level, pos, state);
        if (state.getValue(LIT) != lit) {
            level.setBlock(pos, state.setValue(LIT, lit), 3);
        }
    }

    // ---------------------------------------------------------------------
    // Survival / support
    // ---------------------------------------------------------------------

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos support = pos.relative(facing.getOpposite());

        return world.getBlockState(support)
                .isFaceSturdy(world, support, facing);
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction dir,
            BlockState neighbor,
            LevelAccessor world,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        // Break if supporting block is removed
        if (dir == state.getValue(FACING).getOpposite()
                && !state.canSurvive(world, pos)) {

            if (world instanceof Level level && !level.isClientSide) {
                Block.dropResources(state, level, pos);
            }

            return Blocks.AIR.defaultBlockState();
        }

        return state;
    }

    // ---------------------------------------------------------------------
    // Player interaction
    // ---------------------------------------------------------------------

    @Override
    public ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        // Only react to redstone torches
        if (!stack.is(Items.REDSTONE_TORCH)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide) {
            boolean newInverted = !state.getValue(INVERTED);

            boolean powered = level.hasNeighborSignal(pos);
            boolean newLit = newInverted ? !powered : powered;

            level.setBlock(
                    pos,
                    state.setValue(INVERTED, newInverted)
                            .setValue(LIT, newLit),
                    3
            );

            level.playSound(
                    null,
                    pos,
                    SoundEvents.REDSTONE_TORCH_BURNOUT,
                    SoundSource.BLOCKS,
                    0.6f,
                    1.6f
            );
        }

        return ItemInteractionResult.SUCCESS;
    }

    // ---------------------------------------------------------------------
    // Block state registration
    // ---------------------------------------------------------------------

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(FACING, LIT, INVERTED);
    }
}
