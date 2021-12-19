package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Lang class by clip (https://www.spigotmc.org/threads/language-configuration.33079/)
 */
public enum Lang {
    /**
     * No permission
     */
    NO_PERMISSION("no_permission", "&4Permission denied (&f{0} &4required)"),
    /**
     * player not online
     */
    PLAYER_NOT_ONLINE("player_not_online", "&f{0} &cis not online!"),
    /**
     * player trying to swap a stacked bucket
     */
    STACKED_CHANGE_MODE("stacked_change_mode", "&cYou cannot change bucket modes with a stacked bottomless bucket!"),
    STACKED_PLACE("stacked_place", "&cYou cannot place stacked bottomless buckets!"),
    STACKED_FILL("stacked_fill", "&cYou cannot fill stacked bottomless buckets!"),
    BUCKET_EMPTY("bucket_empty", "&cBucket empty!"),
    BUCKET_FULL("bucket_full", "&cBucket full!"),
    WRONG_TYPE("wrong_bucket_type", "&cYou cannot fill this bucket with that type of liquid!"),
    SWITCH_FILL("switch_fill_mode", "&7Your Bottomless Bucket is now in &e&lfill &7mode."),
    SWITCH_PLACE("switch_place_mode", "&7Your Bottomless Bucket is now in &a&lplace &7mode.")
    ;

    private String path, def;
    private static FileConfiguration LANG;

    Lang(final String path, final String start) {
        this.path = path;
        this.def = start;
    }

    public static void setFile(final FileConfiguration config) {
        LANG = config;
    }

    public String getDefault() {
        return this.def;
    }

    public String getPath() {
        return this.path;
    }

    public String getConfigValue(final String[] args) {
        String value = ChatColor.translateAlternateColorCodes('&',
                LANG.getString(this.path, this.def));

        Bukkit.getLogger().info(value);

        if (args == null)
            return value;
        else {
            if (args.length == 0)
                return value;

            for (int i = 0; i < args.length; i++) {
                value = value.replace("{" + i + "}", args[i]);
            }
        }

        return value;
    }
}