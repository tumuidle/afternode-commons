package cn.afternode.commons.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface GuiClickCallback {
    /**
     * Click handler
     * @param event Click event
     * @param player Who clicked
     * @param clicked Which item clicked
     * @param opened Opened GUI data
     */
    void callback(InventoryClickEvent event, Player player, ItemStack clicked, OpenedGui opened);
}
