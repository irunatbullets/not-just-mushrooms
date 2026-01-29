package io.github.irunatbullets.notjustmushrooms.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;

public class MushroomDotsLampBlock extends MushroomDotsBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final boolean inverted;

    public MushroomDotsLampBlock(boolean inverted) {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .noOcclusion()
                .lightLevel(state -> state.getValue(LIT) ? 15 : 0)
        );

        this.inverted = inverted;

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, inverted)); // inverted lamps default ON
    }

    private boolean shouldBeLit(Level level, BlockPos pos) {
        boolean hasSignal = level.hasNeighborSignal(pos);
        return inverted ? !hasSignal : hasSignal;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState base = super.getStateForPlacement(context);
        if (base == null) return null;

        return base.setValue(
                LIT,
                shouldBeLit(context.getLevel(), context.getClickedPos())
        );
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos,
                                Block block, BlockPos fromPos, boolean isMoving) {

        boolean lit = shouldBeLit(world, pos);

        if (state.getValue(LIT) != lit) {
            world.setBlock(pos, state.setValue(LIT, lit), 3);
        }
    }


    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }
}
