package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.Material;
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

    public static boolean isBottomlessBucket(ItemStack item) {
        if (item == null) return false;
        if (!(Arrays.asList(Material.WATER_BUCKET, Material.LAVA_BUCKET, Material.BUCKET).contains(item.getType()))) return false;
        if (item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        return (meta.getDisplayName().equals(BottomlessBuckets.getColouredConfigValue("bucket-item.name-water")) ||
                meta.getDisplayName().equals(BottomlessBuckets.getColouredConfigValue("bucket-item.name-lava")));
    }
}
