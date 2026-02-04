package io.github.irunatbullets.notjustmushrooms;

import com.mojang.logging.LogUtils;
import io.github.irunatbullets.notjustmushrooms.block.ModBlocks;
import io.github.irunatbullets.notjustmushrooms.blockentity.ModBlockEntities;
import io.github.irunatbullets.notjustmushrooms.creative.ModCreativeTabs;
import io.github.irunatbullets.notjustmushrooms.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(NotJustMushrooms.MODID)
public class NotJustMushrooms {

    public static final String MODID = "notjustmushrooms";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NotJustMushrooms(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }
}
