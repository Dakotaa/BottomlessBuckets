package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.Material;
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
import java.util.List;

public class DispenserBlock implements Listener {

    @EventHandler
    public void onBottomlessBucketDispense(BlockDispenseEvent e) {
        ItemStack item = e.getItem();
        if (isBottomlessBucket(item)) e.setCancelled(true);
    }

    @EventHandler
    public void hopperToDispener(InventoryMoveItemEvent e) {
        ItemStack item = e.getItem();
        if (e.getDestination().getType() == InventoryType.DISPENSER) {
            if (isBottomlessBucket(item)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onShiftInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.DISPENSER) {
            if (e.getClick().isShiftClick()) {
                Inventory clicked = e.getClickedInventory();
                if (clicked == null) return;
                if (clicked.equals(e.getWhoClicked().getInventory())) {
                    ItemStack clickedOn = e.getCurrentItem();
                    if (isBottomlessBucket(clickedOn)) e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.DISPENSER) {
            Inventory clicked = e.getClickedInventory();
            if (clicked != e.getWhoClicked().getInventory()) {
                ItemStack onCursor = e.getCursor();
                if (isBottomlessBucket(onCursor)) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().getType() == InventoryType.DISPENSER) {
            ItemStack dragged = e.getOldCursor();
            if (isBottomlessBucket(dragged)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHotbarKey(InventoryClickEvent e) {
        if (e.getInventory().getType() == org.bukkit.event.inventory.InventoryType.DISPENSER) {
            if (e.getAction().name().contains("HOTBAR")) {
                ItemStack item = e.getCurrentItem();
                if (isBottomlessBucket(item)) e.setCancelled(true);
            }
        }
    }

    public boolean isBottomlessBucket(ItemStack item) {
        if (item == null) return false;
        if (!(Arrays.asList(Material.WATER_BUCKET, Material.LAVA_BUCKET, Material.BUCKET).contains(item.getType()))) return false;
        if (item.getItemMeta() == null) return false;
        return (item.getItemMeta().getDisplayName().equals(BottomlessBuckets.getColouredConfigValue("bucket-item.name")));
    }
}
