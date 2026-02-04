package io.github.irunatbullets.notjustmushrooms.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

/**
 * BlockEntity for the Tiny Chest.
 *
 * Acts as a single-slot container with hopper support,
 * comparator output, and a standard chest-style menu.
 */
public class TinyChestBlockEntity extends BlockEntity
        implements Container, WorldlyContainer, MenuProvider {

    /** The single stored item */
    private ItemStack stack = ItemStack.EMPTY;

    public TinyChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TINY_CHEST.get(), pos, state);
    }

    /**
     * Sync block state to clients and update neighbors.
     * Required for comparator output and visual updates.
     */
    private void sync() {
        if (level == null || level.isClientSide) return;

        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
    }

    // ---------------------------------------------------------------------
    // Container implementation
    // ---------------------------------------------------------------------

    @Override
    public int getContainerSize() {
        return 9; // Required for ChestMenu (only slot 0 is used)
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? stack : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot != 0) return ItemStack.EMPTY;

        ItemStack result = stack.split(amount);
        if (!result.isEmpty()) {
            setChanged();
            sync();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != 0) return ItemStack.EMPTY;

        ItemStack result = stack;
        stack = ItemStack.EMPTY;
        setChanged();
        sync();
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        if (slot == 0) {
            stack = item;
            setChanged();
            sync();
        }
    }

    @Override
    public void clearContent() {
        stack = ItemStack.EMPTY;
        setChanged();
        sync();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    // ---------------------------------------------------------------------
    // Hopper / sided inventory support
    // ---------------------------------------------------------------------

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0 };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack item, Direction dir) {
        return stack.isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack item, Direction dir) {
        return true;
    }

    // ---------------------------------------------------------------------
    // Client sync (block update packets)
    // ---------------------------------------------------------------------

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        if (!stack.isEmpty()) {
            tag.put("Item", stack.save(registries, new CompoundTag()));
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("Item")) {
            stack = ItemStack.parse(registries, tag.getCompound("Item"))
                    .orElse(ItemStack.EMPTY);
        } else {
            stack = ItemStack.EMPTY;
        }
    }

    // ---------------------------------------------------------------------
    // Persistent NBT (world save / load)
    // ---------------------------------------------------------------------

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!stack.isEmpty()) {
            tag.put("Item", stack.save(registries, new CompoundTag()));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("Item")) {
            stack = ItemStack.parse(registries, tag.getCompound("Item"))
                    .orElse(ItemStack.EMPTY);
        } else {
            stack = ItemStack.EMPTY;
        }
    }

    // ---------------------------------------------------------------------
    // Menu / UI
    // ---------------------------------------------------------------------

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new ChestMenu(
                MenuType.GENERIC_9x1,
                id,
                playerInv,
                this,
                1
        );
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.notjustmushrooms.tiny_chest");
    }
}
