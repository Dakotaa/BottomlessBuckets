package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Lang class based on clip's (https://www.spigotmc.org/threads/language-configuration.33079/)
 */
public enum Lang {
    PREFIX("prefix", "&b&lBottomlessBuckets &f»"),
    NO_PERMISSION("no_permission", "&4Permission denied (&f%permission% &4required)"),
    NO_PERMISSION_USE_BUCKETS("no_permission_use_buckets", "&4You do not have permission to use Bottomless Buckets!"),
    STACKED_CHANGE_MODE("stacked_change_mode", "&cYou cannot change bucket modes with a stacked bottomless bucket!"),
    STACKED_PLACE("stacked_place", "&cYou cannot place stacked bottomless buckets!"),
    STACKED_FILL("stacked_fill", "&cYou cannot fill stacked bottomless buckets!"),
    BUCKET_EMPTY("bucket_empty", "&cBucket empty! Press F to switch bucket mode."),
    BUCKET_FULL("bucket_full", "&cBucket full! Press F to switch bucket mode."),
    WRONG_TYPE("wrong_bucket_type", "&cYou cannot fill this bucket with that type of liquid!"),
    FILL_IN_COMBAT("fill_in_combat", "&cYou cannot fill this bucket while in combat"),
    PLACE_FROM_OFFHAND("place_from_offhand", "&cYou cannot use Bottomless Buckets from your off-hand!"),
    PLACE_IN_COMBAT("place_in_combat", "&cYou cannot place this bucket while in combat!"),
    SWITCH_FILL("switch_fill_mode", "&7Your Bottomless Bucket is now in &e&lfill &7mode."),
    SWITCH_PLACE("switch_place_mode", "&7Your Bottomless Bucket is now in &a&lplace &7mode."),
    COMMAND_PLAYER_ONLY("command_player_only", "&cThis command can only be used by players!"),
    COMMAND_INVALID_TYPE("command_invalid_type", "&cInvalid bucket type (use &c&owater &cor &c&olava&c)"),
    COMMAND_BUCKET_GIVEN("command_bucket_given", "&aSuccessfully gave Bottomless %type% Bucket with capacity %capacity% to %player%."),
    COMMAND_BUCKET_RECEIVED("command_bucket_received", "&aYou received a Bottomless %type% Bucket with capacity %capacity%."),
    COMMAND_BUCKETS_RECEIVED("command_buckets_received", "&aYou received %quantity% Bottomless %type% Buckets with capacity %capacity%."),
    COMMAND_INVALID_QUANTITY("command_invalid_quantity", "&cInvalid quantity!"),
    COMMAND_PLAYER_OFFLINE("command_player_offline", "&cThat player is not online!")
    ;

    private final String path;
    private final String def;
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

    public String getConfigValue(final String[] args, final String[] values) {
        if (args.length != values.length) return "";

        String value = ChatColor.translateAlternateColorCodes('&',
                LANG.getString(this.path, this.def));

        for (int i = 0; i < args.length; i++) {
            value = value.replace(args[i], values[i]);
        }

        return value;
    }

    public String getConfigValue() {
        return ChatColor.translateAlternateColorCodes('&',
                LANG.getString(this.path, this.def));
    }
}