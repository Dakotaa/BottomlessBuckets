package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;


public class ContainerBlock implements Listener {

    // list of containers to block
    final InventoryType[] blockedContainers = {
            InventoryType.DISPENSER,
            InventoryType.FURNACE,
            InventoryType.BLAST_FURNACE,
            InventoryType.SMOKER
    };

    @EventHandler
    public void onBottomlessBucketDispense(BlockDispenseEvent e) {
        ItemStack item = e.getItem();
        if (Util.isBottomlessBucket(item)) e.setCancelled(true);
    }

    @EventHandler
    public void hopperToDispener(InventoryMoveItemEvent e) {
        ItemStack item = e.getItem();
        if (Arrays.asList(blockedContainers).contains(e.getDestination().getType())) {
            if (Util.isBottomlessBucket(item)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onShiftInventoryClick(InventoryClickEvent e) {
        if (Arrays.asList(blockedContainers).contains(e.getInventory().getType())) {
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
        if (Arrays.asList(blockedContainers).contains(e.getInventory().getType())) {
            Inventory clicked = e.getClickedInventory();
            if (clicked != e.getWhoClicked().getInventory()) {
                ItemStack onCursor = e.getCursor();
                if (Util.isBottomlessBucket(onCursor)) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (Arrays.asList(blockedContainers).contains(e.getInventory().getType())) {
            ItemStack dragged = e.getOldCursor();
            if (Util.isBottomlessBucket(dragged)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHotbarKey(InventoryClickEvent e) {
        if (Arrays.asList(blockedContainers).contains(e.getInventory().getType())) {
            if (e.getAction().name().contains("HOTBAR")) {
                ItemStack item = e.getCurrentItem();
                if (Util.isBottomlessBucket(item)) e.setCancelled(true);
            }
        }
    }
}
