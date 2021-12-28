package io.github.dakotaa.bottomlessbuckets;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BucketUseListener implements Listener {

    @EventHandler
    public void onBucketFillEvent(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("bottomlessbuckets.use")) {
            ItemStack item = p.getInventory().getItemInMainHand();
            updateBucket(item, "fill", p, e);
        } else {
            Util.message(p, true, Lang.NO_PERMISSION_USE_BUCKETS.getConfigValue());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        updateBucket(item, "empty", p, e);
    }

    /**
     * Updates the player's Bottomless Bucket item when they fill the bucket or place it. Overwrites the item resulting
     * from the PlayerBucketEvent to update the amount in the player's bucket.
     * @param item the bucket item to update
     * @param mode the bucket mode, either "fill" or "empty"
     * @param p the player using the bucket
     * @param e a PlayerBucketEvent
     */
    public void updateBucket(ItemStack item, String mode, Player p, PlayerBucketEvent e) {
        // check if the item is a bottomless bucket
        if (!Util.isBottomlessBucket(item)) return;
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            // player attempting to fill/empty a stacked bucket
            if (item.getAmount() != 1) {
                if (mode.equals("fill")) {
                    Util.message(p, true, Lang.STACKED_FILL.getConfigValue());
                } else {
                    Util.message(p, true, Lang.STACKED_PLACE.getConfigValue());
                }
                e.setCancelled(true);
                return;
            }

            // update the lore of the bucket to change the amount
            List<String> lore = meta.getLore();

            if (lore == null || lore.size() == 0) return;

            // lore line indices for the fill amount and capacity of this bucket
            // get the required config values to compare this bucket to the configured bottomless bucket
            String typeLabel = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.type.label");
            String typeWater = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.type.water");
            String typeLava = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.type.lava");
            String amountLabel = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.amount.label");
            String capacityLabel = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.capacity.label");
            int amount = -1, amountLine = -1, capacity = -1, capacityLine = -1;
            Material type = null;
            for (int i = 0; i < lore.size(); i++) {
                String s = lore.get(i);
                // get the type of bucket
                if (s.contains(typeLabel)) {
                    if (s.contains(typeWater)) {
                        type = Material.WATER;
                    } else if (s.contains(typeLava)) {
                        type = Material.LAVA;
                    } else return;
                }
                // get the amount currently in the bucket
                if (s.contains(amountLabel)) {
                    amountLine = i;
                    amount = Integer.parseInt(ChatColor.stripColor(s).replaceAll("[^0-9]", ""));
                }
                // get the capacity of the bucket
                if (s.contains(capacityLabel)) {
                    capacityLine = i;
                    capacity = Integer.parseInt(ChatColor.stripColor(s).replaceAll("[^0-9]", ""));
                }
            }

            // ensure the bucket has a type
            if (type == null) return;

            // check if the player is in combat and this type of bucket use should be blocked while in combat
            if (BottomlessBuckets.plugin.usingCombatTagging()) {
                if (Util.isInCombat(p)) {
                    String liquid;
                    if (type.equals(Material.WATER)) liquid = "water"; else liquid = "lava";
                    if (mode.equals("fill")) {
                        if (BottomlessBuckets.plugin.getConfig().getBoolean("combat-tag." + liquid + ".blockFill")) {
                            e.setCancelled(true);
                            Util.message(p, true, Lang.FILL_IN_COMBAT.getConfigValue());
                            return;
                        }
                    } else {
                        if (BottomlessBuckets.plugin.getConfig().getBoolean("combat-tag." + liquid + ".blockPlace")) {
                            e.setCancelled(true);
                            Util.message(p, true, Lang.PLACE_IN_COMBAT.getConfigValue());
                            return;
                        }
                    }
                }
            }

            // ensure the liquid type matches the bucket type when filling
            if (mode.equals("fill")) {
                if (!e.getBlock().getType().equals(type)) {
                    Util.message(p, true, Lang.WRONG_TYPE.getConfigValue());
                    e.setCancelled(true);
                    return;
                }
            }

            // ensure all values were set
            if (amount == -1 || amountLine == -1 || capacity == -1 || capacityLine == -1) return;

            // check for bucket empty/at capacity
            if (mode.equals("fill")) {
                if (amount >= capacity) {
                    e.setCancelled(true);
                    Util.message(p, true, Lang.BUCKET_FULL.getConfigValue());
                    return;
                }
            } else {
                if (amount <= 0) {
                    e.setCancelled(true);
                    Util.message(p, true, Lang.BUCKET_EMPTY.getConfigValue());
                    return;
                }
            }

            int newAmount;
            // TODO: Configurable amount per bucket fill
            if (mode.equals("fill")) {
                newAmount = amount + 1;
            } else {
                newAmount = amount - 1;
            }

            // get the formatting for the mode lore from the config
            String configLore = ChatColor.translateAlternateColorCodes('&',
                    (String) BottomlessBuckets.plugin.getConfig().getList("bucket-item.lore.lines").get(amountLine))
                    .replace("%amount-label%", amountLabel)
                    .replace("%amount-value%", String.valueOf(newAmount));

            // update the amount lore line with the new amount
            lore.set(amountLine, configLore);
            // update the lore of the item meta, apply it to the item
            meta.setLore(lore);
            item.setItemMeta(meta);

            // send the user an actionbar message with the new bucket amount
            if (BottomlessBuckets.plugin.getConfig().getBoolean("actionBarMessage")) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(newAmount + "/" + capacity));
            }

            // give the player the updated item
            e.setItemStack(item);

            // auto switch bucket mode if bucket is now at capacity or empty
            if (newAmount == capacity || newAmount == 0) {
                if (BottomlessBuckets.plugin.getConfig().getBoolean("autoSwitchMode")) {
                    ItemStack newBucket = BucketSwitchListener.switchBucket(p);
                    if (newBucket != null) e.setItemStack(newBucket);
                }
            }
        }
    }
}
