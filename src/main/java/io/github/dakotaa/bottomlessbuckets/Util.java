package io.github.dakotaa.bottomlessbuckets;

//import com.SirBlobman.combatlogx.api.ICombatLogX;
//import com.SirBlobman.combatlogx.api.utility.ICombatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

    public static int getVersion() {
        String version = BottomlessBuckets.plugin.getServer().getBukkitVersion().split("-")[0];
        int v;
        switch(version) {
            case "1.18":
                v = 18;
                break;
            case "1.17":
                v = 17;
                break;
            case "1.16":
                v = 16;
                break;
            case "1.15":
                v = 15;
                break;
            case "1.14":
                v = 14;
                break;
            case "1.13":
                v = 13;
                break;
            case "1.12":
                v = 12;
                break;
            case "1.11":
                v = 11;
                break;
            case "1.10":
                v = 10;
                break;
            case "1.9":
                v = 9;
                break;
            case "1.8":
                v = 8;
                break;
            default:
                v= 0;
                break;
        }
        return v;
    }

    /**
     * Checks whether a player is combat tagged by CombatLogX
     * @param player the player to check
     * @return true if the player is combat tag, false if they aren't or CombatLogX is not found
     */
    public static boolean isInCombat(Player player) {
        // Make sure to check that CombatLogX is enabled before using it for anything.
        return false;
//        ICombatLogX plugin = BottomlessBuckets.plugin.getCombatLogX();
//        if (plugin == null) return false;
//        ICombatManager combatManager = plugin.getCombatManager();
//        return combatManager.isInCombat(player);
    }
}
