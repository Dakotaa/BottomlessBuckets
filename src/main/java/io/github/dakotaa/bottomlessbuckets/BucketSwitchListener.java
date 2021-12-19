package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.logging.Logger;

public class BucketSwitchListener implements Listener {

    @EventHandler
    public void onBucketSwitch(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();

        // check if the item being switched is a bottomless bucket
        ItemStack item = p.getInventory().getItemInMainHand();
        Material type = item.getType();
        // check for bucket type
        if (type != Material.BUCKET && type != Material.WATER_BUCKET && type != Material.LAVA_BUCKET) return;
        // get item meta
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        // check item name
        if (!meta.getDisplayName().equals(BucketUseListener.BUCKET_DISPLAY_NAME)) return;

        e.setCancelled(true);

        switchBucket(p, false);
    }

    /**
     * Switches the mode of the player's equipped bucket, if they are holding a valid Bottomless Bucket
     * @param p the player whose tool will be checked
     * @param auto whether this switch is automatic (when player fills/empties bucket)
     */
    public static ItemStack switchBucket(Player p, boolean auto) {
        // get mode and type of the bucket
        // TODO: Configurable bucket lore lines
        // get player's equipped item
        ItemStack item = p.getInventory().getItemInMainHand();
        Material type = item.getType();
        // check for bucket type
        if (type != Material.BUCKET && type != Material.WATER_BUCKET && type != Material.LAVA_BUCKET) return null;
        // get item meta
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        // check item name
        if (!meta.getDisplayName().equals(BucketUseListener.BUCKET_DISPLAY_NAME)) return null;
        // do not allow swapping stacked buckets
        if (item.getAmount() != 1) {
            p.sendMessage("stacked-bucket-swap");
            return null;
        }

        // get item lore
        List<String> lore = meta.getLore();
        if (lore == null || lore.size() == 0) return null;

        // check if water is in the player's line of sight, since filling the bucket will sometimes call
        // the interact event and cause the bucket to swtich
        if (!auto) {
            List<Block> los = p.getLineOfSight(null, 6);
            for (Block b : los) {
                if (b.getType().equals(Material.WATER) || b.getType().equals(Material.LAVA)) return null;
            }
        }

        // get the item lore
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
                } else return null;
            }
            // get the bucket mode
            if (s.contains("Mode")) {
                modeLine = i;
                if (s.contains("Place")) {
                    mode = "place";
                } else if (s.contains("Fill")) {
                    mode = "fill";
                } else return null;
            }
        }

        // no bucket type was found
        if (bucketType == null) return null;

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

        return item;
    }
}
