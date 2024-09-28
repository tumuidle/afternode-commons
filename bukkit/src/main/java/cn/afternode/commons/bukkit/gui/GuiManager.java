package cn.afternode.commons.bukkit.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Inventory GUI helper
 */
public class GuiManager implements Listener {
    private final Map<UUID, OpenedGui> opened = new HashMap<>();

    /**
     * Create and register events
     * @param plugin Plugin instance for registering events
     */
    public GuiManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Create and open inventory GUI for player
     * @param target Target player
     * @param gui Gui
     * @return Opened GUI record
     */
    public OpenedGui open(Player target, IGui gui) {
        Inventory inv = gui.createInventory(target);
        InventoryView view = target.openInventory(inv);
        OpenedGui open = new OpenedGui(target, view, gui);
        this.opened.put(target.getUniqueId(), open);
        return open;
    }

    /**
     * Get opened GUI
     * @param holder holder
     * @return Opened GUI data or null
     */
    public OpenedGui getOpened(InventoryHolder holder) {
        if (holder instanceof Player player)
            return this.opened.get(player.getUniqueId());
        return null;
    }

    /**
     * InventoryCloseEvent handler to remove gui state
     * @param event Event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        this.opened.remove(event.getPlayer().getUniqueId());
    }

    /**
     * InventoryClickEvent handler for slot click
     * @param event Event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity who = event.getWhoClicked();
        if (who instanceof Player player && opened.containsKey(player.getUniqueId())) {
            OpenedGui gui = opened.get(player.getUniqueId());
            if (event.getSlot() == event.getRawSlot()) {
                event.setCancelled(gui.gui().onSlotClick(player, event, gui));
            } else if (event.getSlotType() == InventoryType.SlotType.QUICKBAR) {
                event.setCancelled(true);
            }
        }
    }
}
