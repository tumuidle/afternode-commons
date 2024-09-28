package cn.afternode.commons.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public record OpenedGui(
        Player holder,
        InventoryView inventory,
        IGui gui
) {
}
