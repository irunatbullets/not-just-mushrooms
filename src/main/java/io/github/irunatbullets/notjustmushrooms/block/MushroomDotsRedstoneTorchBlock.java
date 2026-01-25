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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class MushroomDotsRedstoneTorchBlock extends MushroomDotsBlock {

    // Tracks redstone signal level (0–15)
    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

    public MushroomDotsRedstoneTorchBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .noOcclusion()
                .lightLevel(state -> state.getValue(POWER)) // light depends on POWER
        );

        // default facing + no power
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWER);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // default facing
        Direction face = context.getClickedFace();
        BlockState state = this.defaultBlockState().setValue(FACING, face);

        // get the strongest redstone signal at this position
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        int power = world.getBestNeighborSignal(pos);

        // set POWER based on current signal
        return state.setValue(POWER, power);
    }


    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        // get strongest redstone power from neighbors
        int power = world.getBestNeighborSignal(pos);

        if (state.getValue(POWER) != power) {
            // update block state if power changed → triggers light recalculation
            world.setBlock(pos, state.setValue(POWER, power), 3);
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(POWER); // emit light level 0–15
    }
}
