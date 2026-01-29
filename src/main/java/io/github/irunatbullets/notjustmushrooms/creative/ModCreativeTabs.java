package io.github.irunatbullets.notjustmushrooms.creative;

import io.github.irunatbullets.notjustmushrooms.NotJustMushrooms;
import io.github.irunatbullets.notjustmushrooms.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NotJustMushrooms.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB =
            TABS.register("notjustmushrooms", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.notjustmushrooms"))
                            .icon(() -> ModItems.MUSHROOM_DOTS_LAMP.get().getDefaultInstance())
                            .displayItems((params, output) -> {
                                output.accept(ModItems.MUSHROOM_DOTS_LAMP.get());
                                output.accept(ModItems.MUSHROOM_DOTS_LAMP_INVERTED.get());
                            })

                            .build()
            );

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
