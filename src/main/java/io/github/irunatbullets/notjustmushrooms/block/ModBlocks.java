package io.github.irunatbullets.notjustmushrooms.block;

import io.github.irunatbullets.notjustmushrooms.NotJustMushrooms;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(NotJustMushrooms.MODID);

    public static final DeferredHolder<Block, Block> MUSHROOM_DOTS =
            BLOCKS.register("mushroom_dots", () -> new MushroomDotsBlock());


    public static final DeferredHolder<Block, Block> MUSHROOM_DOTS_TORCH =
            BLOCKS.register("mushroom_dots_torch", () -> new MushroomDotsTorchBlock());

    public static final DeferredHolder<Block, Block> MUSHROOM_DOTS_REDSTONE_TORCH =
            BLOCKS.register("mushroom_dots_redstone_torch", () -> new MushroomDotsRedstoneTorchBlock());

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
