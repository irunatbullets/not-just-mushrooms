package io.github.irunatbullets.notjustmushrooms.item;

import io.github.irunatbullets.notjustmushrooms.NotJustMushrooms;
import io.github.irunatbullets.notjustmushrooms.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(NotJustMushrooms.MODID);


    public static final DeferredItem<BlockItem> MUSHROOM_DOTS_LAMP =
            ITEMS.registerSimpleBlockItem(
                    "mushroom_dots_lamp",
                    ModBlocks.MUSHROOM_DOTS_LAMP
            );

    public static final DeferredItem<BlockItem> MUSHROOM_DOTS_LAMP_INVERTED =
            ITEMS.registerSimpleBlockItem(
                    "mushroom_dots_lamp_inverted",
                    ModBlocks.MUSHROOM_DOTS_LAMP_INVERTED
            );

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
