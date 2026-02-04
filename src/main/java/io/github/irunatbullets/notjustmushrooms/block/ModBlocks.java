package io.github.irunatbullets.notjustmushrooms.block;

import io.github.irunatbullets.notjustmushrooms.NotJustMushrooms;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(NotJustMushrooms.MODID);

    public static final DeferredHolder<Block, Block> MUSHROOM_LAMP =
            BLOCKS.register("mushroom_lamp",
                    MushroomLampBlock::new);

    public static final DeferredBlock<Block> TINY_CHEST =
            BLOCKS.register("tiny_chest",
                    () -> new TinyChestBlock(
                            BlockBehaviour.Properties.of()
                                    .strength(1.0F)
                                    .sound(SoundType.WOOD)
                                    .noOcclusion()
                    )
            );


    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
