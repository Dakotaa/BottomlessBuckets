package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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

        switchBucket(p);
    }

    /**
     * Switches the mode of the player's equipped bucket, if they are holding a valid Bottomless Bucket
     * @param p the player whose tool will be checked
     * @param auto whether this switch is automatic (when player fills/empties bucket)
     */
    public static ItemStack switchBucket(Player p) {
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
            p.sendMessage(Lang.STACKED_CHANGE_MODE.getConfigValue(null));
            return null;
        }

        // get item lore
        List<String> lore = meta.getLore();
        if (lore == null || lore.size() == 0) return null;

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

        // switch bucket item depending on mode
        if (mode.equals("place")) {
            p.sendMessage(Lang.SWITCH_FILL.getConfigValue(null));
            item.setType(Material.BUCKET);
            lore.set(modeLine, ChatColor.translateAlternateColorCodes('&', "&7Mode: &fFill"));
        } else {
            item.setType(bucketType);
            p.sendMessage(Lang.SWITCH_PLACE.getConfigValue(null));
            lore.set(modeLine, ChatColor.translateAlternateColorCodes('&', "&7Mode: &fPlace"));
        }

        // TODO configurable sound
        if (BottomlessBuckets.plugin.getConfig().getBoolean("sounds.enableModeSwitchSound")) {
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 0.5f, 1.3f);
        }
        // update the item lore and give the item to the player
        meta.setLore(lore);
        item.setItemMeta(meta);
        p.getInventory().setItemInMainHand(item);

        return item;
    }
}
