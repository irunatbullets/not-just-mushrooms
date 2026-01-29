package io.github.irunatbullets.notjustmushrooms.block;

import io.github.irunatbullets.notjustmushrooms.NotJustMushrooms;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(NotJustMushrooms.MODID);

    public static final DeferredHolder<Block, Block> MUSHROOM_DOTS_LAMP =
            BLOCKS.register("mushroom_dots_lamp",
                    () -> new MushroomDotsLampBlock(false));

    public static final DeferredHolder<Block, Block> MUSHROOM_DOTS_LAMP_INVERTED =
            BLOCKS.register("mushroom_dots_lamp_inverted",
                    () -> new MushroomDotsLampBlock(true));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
