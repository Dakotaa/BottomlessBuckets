package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.logging.Logger;

public class BucketSwitchListener implements Listener {

    @EventHandler
    public void onBucketClick(PlayerInteractEvent e) {
        Logger l = Bukkit.getLogger();
        if (!e.getAction().equals(Action.LEFT_CLICK_AIR)) return;
        Player p = e.getPlayer();
        // get player's equipped item
        ItemStack item = p.getInventory().getItemInMainHand();
        Material type = item.getType();
        // check for bucket type
        if (type != Material.BUCKET && type != Material.WATER_BUCKET && type != Material.LAVA_BUCKET) return;
        // get item meta
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        // check item name
        if (!meta.getDisplayName().equals(BucketUseListener.BUCKET_DISPLAY_NAME)) return;
        // get item lore
        List<String> lore = meta.getLore();
        if (lore == null || lore.size() == 0) return;

        if (e.getClickedBlock() != null) p.sendMessage(e.getClickedBlock().toString());

        // get mode and type of the bucket
        // TODO: Configurable bucket lore lines
        int modeLine = -1;
        Material bucketType = null;
        String mode = "";
        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            // get the bucket type
            if (s.contains("Type")) {
                if (s.contains("Water")) {
                    bucketType = Material.WATER_BUCKET;
                } else if (s.contains("Lava")) {
                    bucketType = Material.LAVA_BUCKET;
                } else return;
            }
            // get the bucket mode
            if (s.contains("Mode")) {
                modeLine = i;
                if (s.contains("Place")) {
                    mode = "place";
                } else if (s.contains("Fill")) {
                    mode = "fill";
                } else return;
            }
        }

        // no bucket type was found
        if (bucketType == null) return;

        // TODO player messages
        // switch bucket item depending on mode
        if (mode.equals("place")) {
            p.sendMessage("switch-fill");
            item.setType(Material.BUCKET);
            lore.set(modeLine, ChatColor.translateAlternateColorCodes('&', "&7Mode: &fFill"));
        } else {
            item.setType(bucketType);
            p.sendMessage("switch-place");
            lore.set(modeLine, ChatColor.translateAlternateColorCodes('&', "&7Mode: &fPlace"));
        }

        // TODO switch sound effect
        // update the item lore and give the item to the player
        meta.setLore(lore);
        item.setItemMeta(meta);
        p.getInventory().setItemInMainHand(item);
    }
}
