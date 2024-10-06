package cn.afternode.commons.bukkit.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

public interface IGui {
    /**
     * Put item in specified slot
     * @param slot slot number
     * @param stack item
     */
    void putItem(int slot, ItemStack stack);

    /**
     * Set click callback in specified slot
     * @param slot slot number
     * @param callback callback
     */
    void putCallback(int slot, GuiClickCallback callback);

    /**
     * Put item in specified slot with callback
     * @param slot slot number
     * @param item item
     * @param callback callback
     */
    default void callbackItem(int slot, ItemStack item, GuiClickCallback callback) {
        this.putItem(slot, item);
        this.putCallback(slot, callback);
    }

    /**
     * Set a slot that does not block operations
     * @param slot Target slot number
     */
    void dontBlockOperation(int slot);

    /**
     * Get if a slot does not block operations
     * @param slot Target slot number
     * @return result
     */
    boolean isDontBlockOperation(int slot);

    /**
     * Set inventory title
     * @param component title
     */
    void setTitle(Component component);

    /**
     * Get current inventory title
     * @return title
     */
    Component getTitle();

    /**
     * Set maximum size (may reset contents in current instance)
     * @param size Target size
     */
    void setSize(int size);

    /**
     * Get current size
     * @return size
     */
    int getSize();

    /**
     * Fill with specified item
     * @param start start slot
     * @param end end slot
     * @param item item
     */
    default void fill(int start, int end, ItemStack item) {
        for (int i = start; i < end; i++) {
            this.putItem(i, item);
        }
    }

    /**
     * Create new inventory with specified owner
     * @param holder owner
     * @return created
     */
    Inventory createInventory(Player holder);

    /**
     * Click handler
     * @param who Who clicked
     * @param event Click event
     * @param gui Opened GUI data
     * @return If event cancellation needed
     */
    @ApiStatus.Internal
    boolean onSlotClick(Player who, InventoryClickEvent event, OpenedGui gui);
}
