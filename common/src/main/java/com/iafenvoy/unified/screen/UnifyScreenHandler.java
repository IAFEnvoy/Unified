package com.iafenvoy.unified.screen;

import com.google.common.collect.Lists;
import com.iafenvoy.unified.Unified;
import com.iafenvoy.unified.data.TagCache;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

public class UnifyScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final Property selectedItem;
    private final World world;
    private List<Item> availableItems;
    private ItemStack inputStack;
    long lastTakeTime;
    final Slot inputSlot;
    final Slot outputSlot;
    Runnable contentsChangedListener;
    public final Inventory input;
    final CraftingResultInventory output;

    public UnifyScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public UnifyScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(Unified.HANDLER_TYPE.get(), syncId);
        this.selectedItem = Property.create();
        this.availableItems = Lists.newArrayList();
        this.inputStack = ItemStack.EMPTY;
        this.contentsChangedListener = () -> {
        };
        this.input = new SimpleInventory(1) {
            @Override
            public void markDirty() {
                super.markDirty();
                UnifyScreenHandler.this.onContentChanged(this);
                UnifyScreenHandler.this.contentsChangedListener.run();
            }
        };
        this.output = new CraftingResultInventory();
        this.context = context;
        this.world = playerInventory.player.getWorld();
        this.inputSlot = this.addSlot(new Slot(this.input, 0, 20, 33));
        this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraft(player.getWorld(), player, stack.getCount());
                UnifyScreenHandler.this.output.unlockLastRecipe(player, this.getInputStacks());
                ItemStack itemStack = UnifyScreenHandler.this.inputSlot.takeStack(1);
                if (!itemStack.isEmpty())
                    UnifyScreenHandler.this.populateResult();
                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (UnifyScreenHandler.this.lastTakeTime != l)
                        UnifyScreenHandler.this.lastTakeTime = l;
                });
                super.onTakeItem(player, stack);
            }

            private List<ItemStack> getInputStacks() {
                return List.of(UnifyScreenHandler.this.inputSlot.getStack());
            }
        });
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        this.addProperty(this.selectedItem);
    }

    public int getSelectedItem() {
        return this.selectedItem.get();
    }

    public List<Item> getAvailableItems() {
        return this.availableItems;
    }

    public int getAvailableItemCount() {
        return this.availableItems.size();
    }

    public boolean canCraft() {
        return this.inputSlot.hasStack() && !this.availableItems.isEmpty();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.isInBounds(id)) {
            this.selectedItem.set(id);
            this.populateResult();
        }
        return true;
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < this.availableItems.size();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack itemStack = this.inputSlot.getStack();
        if (!itemStack.isOf(this.inputStack.getItem())) {
            this.inputStack = itemStack.copy();
            this.updateInput(itemStack);
        }
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    private void updateInput(ItemStack stack) {
        this.availableItems = new LinkedList<>();
        this.selectedItem.set(-1);
        this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        if (!stack.isEmpty())
            this.availableItems = TagCache.getOrCache(stack).stream().toList();
    }

    void populateResult() {
        if (!this.availableItems.isEmpty() && this.isInBounds(this.selectedItem.get())) {
            Item item = this.availableItems.get(this.selectedItem.get());
            ItemStack itemStack = new ItemStack(item);
            if (itemStack.isItemEnabled(this.world.getEnabledFeatures()))
                this.outputSlot.setStackNoCallbacks(itemStack);
            else
                this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        } else
            this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        this.sendContentUpdates();
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            Item item = itemStack2.getItem();
            itemStack = itemStack2.copy();
            if (slot == 1) {
                item.onCraft(itemStack2, player.getWorld(), player);
                if (!this.insertItem(itemStack2, 2, 38, true))
                    return ItemStack.EMPTY;
                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (slot == 0) {
                if (!this.insertItem(itemStack2, 2, 38, false))
                    return ItemStack.EMPTY;
            } else if (!this.insertItem(itemStack2, 0, 1, false))
                return ItemStack.EMPTY;
            if (itemStack2.isEmpty()) slot2.setStack(ItemStack.EMPTY);
            slot2.markDirty();
            if (itemStack2.getCount() == itemStack.getCount()) return ItemStack.EMPTY;
            slot2.onTakeItem(player, itemStack2);
            this.sendContentUpdates();
        }
        return itemStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.output.removeStack(1);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }
}