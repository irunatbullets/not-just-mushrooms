package io.github.irunatbullets.notjustmushrooms.client;

import io.github.irunatbullets.notjustmushrooms.NotJustMushrooms;
import io.github.irunatbullets.notjustmushrooms.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(
        modid = NotJustMushrooms.MODID,
        value = Dist.CLIENT
)

public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    ModItems.MUSHROOM_LAMP.get(),
                    ResourceLocation.fromNamespaceAndPath(
                            NotJustMushrooms.MODID,
                            "sneaking"
                    ),
                    (stack, level, entity, seed) ->
                            (entity instanceof Player player && player.isShiftKeyDown())
                                    ? 1.0F
                                    : 0.0F
            );
        });
    }
}
