package io.github.irunatbullets.notjustmushrooms;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-side setup for Not Just Mushrooms.
 * Only loaded on the client.
 */
@Mod(value = NotJustMushrooms.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = NotJustMushrooms.MODID, value = Dist.CLIENT)
public class NotJustMushroomsClient {

    public NotJustMushroomsClient(ModContainer container) {
        // Nothing to do yet — no config screen needed
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Optional client-side logging
        NotJustMushrooms.LOGGER.info("HELLO FROM CLIENT SETUP");
        NotJustMushrooms.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
