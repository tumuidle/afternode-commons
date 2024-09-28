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
     * @param callback item
     */
    void putCallback(int slot, GuiClickCallback callback);

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
     */
    @ApiStatus.Internal
    void onSlotClick(Player who, InventoryClickEvent event, OpenedGui gui);
}
