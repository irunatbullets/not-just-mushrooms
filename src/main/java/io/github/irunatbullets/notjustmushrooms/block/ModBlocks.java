package io.github.irunatbullets.notjustmushrooms.block;

import io.github.irunatbullets.notjustmushrooms.NotJustMushrooms;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(NotJustMushrooms.MODID);

    public static final DeferredBlock<Block> MUSHROOM_DOTS =
            BLOCKS.register("mushroom_dots", MushroomDotsBlock::new);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
