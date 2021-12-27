package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Util {
    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns whether the given item is a Bottomless Bucket, by comparing the item's name to the configured name
     * @param item an ItemStack
     * @return true if the item is a bottomless bucket, false otherwise
     */
    public static boolean isBottomlessBucket(ItemStack item) {
        if (item == null) return false;
        if (!(Arrays.asList(Material.WATER_BUCKET, Material.LAVA_BUCKET, Material.BUCKET).contains(item.getType()))) return false;
        if (item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        return (meta.getDisplayName().equals(BottomlessBuckets.getColouredConfigValue("bucket-item.name-water")) ||
                meta.getDisplayName().equals(BottomlessBuckets.getColouredConfigValue("bucket-item.name-lava")));
    }

    /**
     * Send the player a message, with the option of including the prefix from the config
     * @param p the player to send the message to
     * @param prefixed whether the message should be prefixed with the configured prefix
     * @param message the message to send
     */
    public static void message(CommandSender p, boolean prefixed, String message) {
        if (prefixed) {
            String prefix = Lang.PREFIX.getConfigValue();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + message));
        } else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        }
    }
}
