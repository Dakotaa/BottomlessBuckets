package io.github.dakotaa.bottomlessbuckets;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.logging.Logger;

public class BucketUseListener implements Listener {

    public static final String BUCKET_DISPLAY_NAME = ChatColor.AQUA + "Bottomless Bucket";

    @EventHandler
    public void onBucketFillEvent(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();

        // get the item in the player's hand
        // TODO: Make this work for buckets in the off-hand
        ItemStack item = p.getInventory().getItemInMainHand();

        if (item.getType() != Material.BUCKET) return;

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            // TODO: Configurable bucket name
            if (!meta.getDisplayName().equals(BUCKET_DISPLAY_NAME)) return;

            if (item.getAmount() != 1) {
                p.sendMessage(Lang.STACKED_FILL.getConfigValue(null));
                e.setCancelled(true);
                return;
            }

            // check the lore of the item, return if lore is empty
            List<String> lore = meta.getLore();
            if (lore == null || lore.size() == 0) return;

            // lore line indices for the fill amount and capacity of this bucket
            // TODO: Configurable bucket lore lines
            int amount = -1, amountLine = -1, capacity = -1, capacityLine = -1;
            Material type = null;
            for (int i = 0; i < lore.size(); i++) {
                String s = lore.get(i);
                // get the type of bucket
                if (s.contains("Water")) {
                    type = Material.WATER;
                } else if (s.contains("Lava")) {
                    type = Material.LAVA;
                }
                // get the amount currently in the bucket
                if (s.contains("Amount")) {
                    amountLine = i;
                    amount = Integer.parseInt(ChatColor.stripColor(s).replaceAll("[^0-9]", ""));
                }
                // get the capacity of the bucket
                if (s.contains("Capacity")) {
                    capacityLine = i;
                    capacity = Integer.parseInt(ChatColor.stripColor(s).replaceAll("[^0-9]", ""));
                }
            }

            // ensure the bucket has a type
            if (type == null) return;

            // ensure the liquid type matches the bucket type
            if (!e.getBlock().getType().equals(type)) {
                // TODO Message player error
                p.sendMessage(Lang.WRONG_TYPE.getConfigValue(null));
                e.setCancelled(true);
                return;
            }

            // ensure all values were set
            if (amount == -1 || amountLine == -1 || capacity == -1 || capacityLine == -1) return;

            // check for bucket at/above capacity
            if (amount >= capacity) {
                e.setCancelled(true);
                p.sendMessage(Lang.BUCKET_FULL.getConfigValue(null));
                return;
            }

            // TODO: Configurable amount per bucket fill
            int newAmount = amount + 1;

            // update the amount lore line with the new amount
            lore.set(amountLine, ChatColor.translateAlternateColorCodes('&', "&7Amount: &f" + newAmount));
            meta.setLore(lore);
            item.setItemMeta(meta);

            // send the user an actionbar message with the new bucket amount
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(newAmount + "/" + capacity));

            // give the player the updated item instead of a water bucket
            e.setItemStack(item);

            if (newAmount == capacity) {
                if (BottomlessBuckets.plugin.getConfig().getBoolean("autoSwitchMode")) {
                    ItemStack newBucket = BucketSwitchListener.switchBucket(p);
                    if (newBucket != null) e.setItemStack(newBucket);
                }
            }
        }
    }

    @EventHandler
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent e) {
        Logger l = Bukkit.getLogger();
        Player p = e.getPlayer();

        // get the item in the player's hand
        // TODO: Make this work for buckets in the off-hand
        ItemStack item = p.getInventory().getItemInMainHand();


        if (item.getType() != Material.LAVA_BUCKET && item.getType() != Material.WATER_BUCKET) return;

        l.info(item.toString());

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            // TODO: Configurable bucket name
            if (!meta.getDisplayName().equals(BUCKET_DISPLAY_NAME)) return;

            if (item.getAmount() != 1) {
                p.sendMessage(Lang.STACKED_PLACE.getConfigValue(null));
                e.setCancelled(true);
                return;
            }

            // check the lore of the item, return if lore is empty
            List<String> lore = meta.getLore();
            if (lore == null || lore.size() == 0) return;

            // lore line indices for the fill amount and capacity of this bucket
            // TODO: Configurable bucket lore lines
            int amount = -1, amountLine = -1, capacity = -1, capacityLine = -1;
            for (int i = 0; i < lore.size(); i++) {
                String s = lore.get(i);
                // get the amount currently in the bucket
                if (s.contains("Amount")) {
                    amountLine = i;
                    amount = Integer.parseInt(ChatColor.stripColor(s).replaceAll("[^0-9]", ""));
                }
                // get the capacity of the bucket
                if (s.contains("Capacity")) {
                    capacityLine = i;
                    capacity = Integer.parseInt(ChatColor.stripColor(s).replaceAll("[^0-9]", ""));
                }
            }

            // ensure all values were set
            if (amount == -1 || amountLine == -1 || capacity == -1 || capacityLine == -1) return;

            // check for empty bucket
            if (amount <= 0) {
                e.setCancelled(true);
                p.sendMessage(Lang.BUCKET_EMPTY.getConfigValue(null));
                return;
            }

            // TODO: Configurable amount per bucket fill
            int newAmount = amount - 1;

            // update the amount lore line with the new amount
            lore.set(amountLine, ChatColor.translateAlternateColorCodes('&', "&7Amount: &f" + newAmount));
            meta.setLore(lore);
            item.setItemMeta(meta);

            // send the user an actionbar message with the new bucket amount
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(newAmount + "/" + capacity));

            // give the player the updated item instead of a water bucket
            e.setItemStack(item);

            if (newAmount == 0) {
                if (BottomlessBuckets.plugin.getConfig().getBoolean("autoSwitchMode")) {
                    ItemStack newBucket = BucketSwitchListener.switchBucket(p);
                    if (newBucket != null) e.setItemStack(newBucket);
                }
            }
        }
    }
}
