package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
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
        // check if the item is a bottomless bucket
        if (!Util.isBottomlessBucket(item)) return;
        // get item meta
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        e.setCancelled(true);

        if (p.hasPermission("bottomlessbuckets.use")) {
            switchBucket(p);
        } else {
            p.sendMessage(Lang.NO_PERMISSION_USE_BUCKETS.getConfigValue());
        }
    }

    /**
     * Switches the mode of the player's equipped bucket, if they are holding a valid Bottomless Bucket
     * @param p the player whose tool will be checked
     */
    public static ItemStack switchBucket(Player p) {
        Configuration config = BottomlessBuckets.plugin.getConfig();

        // get player's equipped item
        ItemStack item = p.getInventory().getItemInMainHand();
        Material type = item.getType();
        // check if the item is a bottomless bucket
        if (!Util.isBottomlessBucket(item)) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        // do not allow swapping stacked buckets
        if (item.getAmount() != 1) {
            p.sendMessage(Lang.STACKED_CHANGE_MODE.getConfigValue());
            return null;
        }
        // get item lore
        List<String> lore = meta.getLore();
        if (lore == null || lore.size() == 0) return null;

        // get the item lore
        int modeLine = -1;
        Material bucketType = null;
        String mode = "";
        // get the required config values to compare this bucket to the configured bottomless bucket
        String typeLabel = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.type.label");
        String typeWater = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.type.water");
        String typeLava = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.type.lava");
        String modeLabel = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.mode.label");
        String modeFill = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.mode.fill");
        String modePlace = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.mode.place");
        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            // get the bucket type
            if (s.contains(typeLabel)) {
                if (s.contains(typeWater)) {
                    bucketType = Material.WATER_BUCKET;
                } else if (s.contains(typeLava)) {
                    bucketType = Material.LAVA_BUCKET;
                } else return null;
            }
            // get the bucket mode
            if (s.contains(modeLabel)) {
                modeLine = i;
                if (s.contains(modePlace)) {
                    mode = "place";
                } else if (s.contains(modeFill)) {
                    mode = "fill";
                } else return null;
            }
        }

        // no bucket type was found
        if (bucketType == null) return null;

        // switch bucket item depending on mode
        // get the formatting for the mode lore from the config
        String configLore = ChatColor.translateAlternateColorCodes('&',
                (String) BottomlessBuckets.plugin.getConfig().getList("bucket-item.lore.lines").get(modeLine))
                .replace("%mode-label%", modeLabel);
        if (mode.equals("place")) {
            p.sendMessage(Lang.SWITCH_FILL.getConfigValue());
            item.setType(Material.BUCKET);
            configLore = configLore.replace("%mode-value%", modeFill);
        } else {
            item.setType(bucketType);
            p.sendMessage(Lang.SWITCH_PLACE.getConfigValue());
            configLore = configLore.replace("%mode-value%", modePlace);
        }
        lore.set(modeLine, configLore);

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
