package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ContainerBlock implements Listener {

    @EventHandler
    public void onBottomlessBucketDispense(BlockDispenseEvent e) {
        ItemStack item = e.getItem();
        if (Util.isBottomlessBucket(item)) e.setCancelled(true);
    }

    @EventHandler
    public void hopperToDispener(InventoryMoveItemEvent e) {
        ItemStack item = e.getItem();
        if (e.getDestination().getType().equals(InventoryType.DISPENSER) || e.getDestination().getType().equals(InventoryType.FURNACE)) {
            if (Util.isBottomlessBucket(item)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onShiftInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getType().equals(InventoryType.DISPENSER) || e.getInventory().getType().equals(InventoryType.FURNACE)) {
            if (e.getClick().isShiftClick()) {
                Inventory clicked = e.getClickedInventory();
                if (clicked == null) return;
                if (clicked.equals(e.getWhoClicked().getInventory())) {
                    ItemStack clickedOn = e.getCurrentItem();
                    if (Util.isBottomlessBucket(clickedOn)) e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getType().equals(InventoryType.DISPENSER) || e.getInventory().getType().equals(InventoryType.FURNACE)) {
            Inventory clicked = e.getClickedInventory();
            if (clicked != e.getWhoClicked().getInventory()) {
                ItemStack onCursor = e.getCursor();
                if (Util.isBottomlessBucket(onCursor)) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().getType().equals(InventoryType.DISPENSER) || e.getInventory().getType().equals(InventoryType.FURNACE)) {
            ItemStack dragged = e.getOldCursor();
            if (Util.isBottomlessBucket(dragged)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHotbarKey(InventoryClickEvent e) {
        if (e.getInventory().getType().equals(InventoryType.DISPENSER) || e.getInventory().getType().equals(InventoryType.FURNACE)) {
            if (e.getAction().name().contains("HOTBAR")) {
                ItemStack item = e.getCurrentItem();
                if (Util.isBottomlessBucket(item)) e.setCancelled(true);
            }
        }
    }
}
