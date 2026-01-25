package io.github.irunatbullets.notjustmushrooms.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class MushroomDotsTorchBlock extends MushroomDotsBlock {

    public MushroomDotsTorchBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .noOcclusion()
                .lightLevel(state -> 14)
        );

        // default facing
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

}
