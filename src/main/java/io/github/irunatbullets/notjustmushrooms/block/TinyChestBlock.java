package io.github.irunatbullets.notjustmushrooms.block;

import io.github.irunatbullets.notjustmushrooms.blockentity.TinyChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Tiny Chest block.
 *
 * A small, floor-mounted container with:
 * - Custom hitbox
 * - BlockEntity-backed inventory
 * - Comparator output
 * - Standard chest-style UI
 */
public class TinyChestBlock extends Block implements EntityBlock {

    /**
     * Visual, collision, and interaction shape.
     * Centered and smaller than a full block.
     */
    private static final VoxelShape SHAPE =
            Block.box(5, 0, 5, 11, 6, 11);

    public TinyChestBlock(Properties properties) {
        super(properties);
    }

    // ---------------------------------------------------------------------
    // Shapes
    // ---------------------------------------------------------------------

    /** Outline (white selection box) */
    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return SHAPE;
    }

    /** Physical collision */
    @Override
    public VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return SHAPE;
    }

    /** Ray tracing / clicking shape */
    @Override
    public VoxelShape getInteractionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos
    ) {
        return SHAPE;
    }

    // ---------------------------------------------------------------------
    // Block Entity
    // ---------------------------------------------------------------------

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TinyChestBlockEntity(pos, state);
    }

    // ---------------------------------------------------------------------
    // Rendering
    // ---------------------------------------------------------------------

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // ---------------------------------------------------------------------
    // Redstone
    // ---------------------------------------------------------------------

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TinyChestBlockEntity tiny) {
            // Binary output: empty = 0, occupied = 15
            return tiny.isEmpty() ? 0 : 15;
        }
        return 0;
    }

    // ---------------------------------------------------------------------
    // Player Interaction
    // ---------------------------------------------------------------------

    @Override
    public InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TinyChestBlockEntity tiny) {
                player.openMenu(tiny);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
