package io.github.irunatbullets.notjustmushrooms.blockentity;

import io.github.irunatbullets.notjustmushrooms.NotJustMushrooms;
import io.github.irunatbullets.notjustmushrooms.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NotJustMushrooms.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TinyChestBlockEntity>> TINY_CHEST =
            BLOCK_ENTITIES.register("tiny_chest",
                    () -> BlockEntityType.Builder.of(
                            TinyChestBlockEntity::new,
                            ModBlocks.TINY_CHEST.get()
                    ).build(null)
            );

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
