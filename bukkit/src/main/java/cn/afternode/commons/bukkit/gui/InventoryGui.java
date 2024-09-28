package cn.afternode.commons.bukkit.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryGui implements IGui {
    private int size;
    private Component title;

    private ItemStack[] items;
    private final Map<Integer, GuiClickCallback> callback = new HashMap<>();
    private final Set<Integer> dontBlockOperation = new HashSet<>();

    /**
     * Create new chest GUI with specified size and title
     * @param size size
     * @param title title
     */
    public InventoryGui(int size, Component title) {
        this.size = size;
        this.title = title;
        this.items = new ItemStack[size];
    }

    /**
     * Create new chest GUI with specified size
     * @param size size
     */
    public InventoryGui(int size) {
        this(size, Component.empty());
    }

    /**
     * Create new chest GUi with specified title
     * @param title title
     */
    public InventoryGui(Component title) {
        this(27, title);
    }

    /**
     * Create new chest GUI with default size and title
     */
    public InventoryGui() {
        this(27, Component.empty());
    }

    @Override
    public void putItem(int slot, ItemStack stack) {
        this.items[slot] = stack;
    }

    @Override
    public void putCallback(int slot, GuiClickCallback callback) {
        this.callback.put(slot, callback);
    }

    @Override
    public void dontBlockOperation(int slot) {
        this.dontBlockOperation.add(slot);
    }

    @Override
    public boolean isDontBlockOperation(int slot) {
        return this.dontBlockOperation.contains(slot);
    }

    @Override
    public void setTitle(Component component) {
        this.title = component;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
        this.items = new ItemStack[size];
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public Inventory createInventory(Player holder) {
        Inventory inv = Bukkit.createInventory(holder, this.size, this.title);
        inv.setContents(this.items);
        return inv;
    }

    @Override
    public boolean onSlotClick(Player who, InventoryClickEvent event, OpenedGui gui) {
        int slot = event.getSlot();

        GuiClickCallback callback = this.callback.get(slot);
        if (callback != null)
            callback.callback(event, who, items[slot], gui);

        return !this.dontBlockOperation.contains(event.getRawSlot());
    }
}
