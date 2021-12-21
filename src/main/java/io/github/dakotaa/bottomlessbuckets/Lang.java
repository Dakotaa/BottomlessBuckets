package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Lang class by clip (https://www.spigotmc.org/threads/language-configuration.33079/)
 */
public enum Lang {
    NO_PERMISSION("no_permission", "&4Permission denied (&f{0} &4required)"),
    NO_PERMISSION_USE_BUCKETS("no_permission_use_buckets", "&4You do not have permission to use Bottomless Buckets!"),
    STACKED_CHANGE_MODE("stacked_change_mode", "&cYou cannot change bucket modes with a stacked bottomless bucket!"),
    STACKED_PLACE("stacked_place", "&cYou cannot place stacked bottomless buckets!"),
    STACKED_FILL("stacked_fill", "&cYou cannot fill stacked bottomless buckets!"),
    BUCKET_EMPTY("bucket_empty", "&cBucket empty! Press F to switch bucket mode."),
    BUCKET_FULL("bucket_full", "&cBucket full! Press F to switch bucket mode."),
    WRONG_TYPE("wrong_bucket_type", "&cYou cannot fill this bucket with that type of liquid!"),
    SWITCH_FILL("switch_fill_mode", "&7Your Bottomless Bucket is now in &e&lfill &7mode."),
    SWITCH_PLACE("switch_place_mode", "&7Your Bottomless Bucket is now in &a&lplace &7mode."),
    COMMAND_PLAYER_ONLY("command_player_only", "&cThis command can only be used by players!"),
    COMMAND_INVALID_TYPE("command_invalid_type", "&cInvalid bucket type (use &c&owater &cor &c&olava&c)"),
    COMMAND_BUCKET_GIVEN("command_bucket_given", "&aSuccessfully gave Bottomless {0} Bucket with capacity {1} to {3}."),
    COMMAND_BUCKET_RECEIVED("command_bucket_received", "&aYou received a Bottomless {0} Bucket with capacity {1}."),
    COMMAND_BUCKETS_RECEIVED("command_buckets_received", "&aYou received {0} Bottomless {1} Buckets with capacity {2}."),
    COMMAND_INVALID_QUANTITY("command_invalid_quantity", "&cInvalid quantity!"),
    COMMAND_PLAYER_OFFLINE("command_player_offline", "&cThat player is not online!")
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