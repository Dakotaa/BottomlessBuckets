package io.github.dakotaa.bottomlessbuckets;

//import com.SirBlobman.combatlogx.api.ICombatLogX;
import io.github.dakotaa.bottomlessbuckets.config.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

public class BottomlessBuckets extends JavaPlugin {
    public static BottomlessBuckets plugin;
    public static InventoryType[] blockedContainers;
    private ConfigWrapper langFile = new ConfigWrapper(this, "", "lang.yml");
    private FileConfiguration config = getConfig();
    private int version;
    //private ICombatLogX combatLogX;
    private boolean useCombatTagging;

    @Override
    public void onEnable() {
        plugin = this;

        reloadConfig();

        getServer().getPluginManager().registerEvents(new BucketUseListener(), this);
        getServer().getPluginManager().registerEvents(new BucketSwitchListener(), this);
        getServer().getPluginManager().registerEvents(new ContainerBlock(), this);

        this.getCommand("buckets").setExecutor(new CommandBuckets());

        langFile.createNewFile("Loading BottomlessBuckets lang.yml", "BottomlessBuckets lang file");
        loadLang();

        version = Util.getVersion();

        // list of containers to block
        if (version >= 14) {
             blockedContainers = new InventoryType[]{
                     InventoryType.DISPENSER,
                     InventoryType.FURNACE,
                     InventoryType.BLAST_FURNACE,
                     InventoryType.SMOKER
             };
        } else {
            // list of containers to block
            blockedContainers = new InventoryType[]{
                    InventoryType.DISPENSER,
                    InventoryType.FURNACE
            };
        }

        useCombatTagging = getConfig().getBoolean("combat-tag.useCombatTagging");

//        try {
//            combatLogX = (ICombatLogX) Bukkit.getPluginManager().getPlugin("CombatLogX");
//            if (useCombatTagging) getLogger().info("CombatLogX found, bucket combat tag checking enabled.");
//        } catch (NoClassDefFoundError e) {
//            combatLogX = null;
//            if (useCombatTagging) getLogger().info("CombatLogX not found, cannot use bucket combat tag checking.");
//            useCombatTagging = false;
//        }

        getLogger().info("Enabled");
    }

    private void loadLang() {
        Lang.setFile(langFile.getConfig());

        for (final Lang value : Lang.values()) {
            langFile.getConfig().addDefault(value.getPath(),
                    value.getDefault());
        }

        langFile.getConfig().options().copyDefaults(true);
        langFile.saveConfig();
    }

    public void sendNoPermsMsg(Player p, String permNeeded) {
        Util.message(p, true, Lang.NO_PERMISSION.getConfigValue(new String[] {"%permission%"}, new String[] {permNeeded}));
    }

    /**
     * Get a coloured value from the config and translate the colour code, to be used for checking against
     * item name/lores.
     * @param path path to the config value
     * @return the config value with colour code translated
     */
    public static String getColouredConfigValue(String path) {
        String value = BottomlessBuckets.plugin.getConfig().getString(path);
        try {
            assert value != null;
            value = ChatColor.translateAlternateColorCodes('&', value);
        } catch (Exception e) {
            BottomlessBuckets.plugin.getLogger().info("Failed to retrieve config value: " + path);
        }
        return value;
    }


    public void reload() {
        reloadConfig();

        langFile.createNewFile("Loading BottomlessBuckets lang.yml", "BottomlessBuckets lang file");
        loadLang();

        getLogger().info("Reloaded successfully.");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }

    public boolean usingCombatTagging() {
        return useCombatTagging;
    }

    public int getVersion() {
        return version;
    }

//    public ICombatLogX getCombatLogX() {
//        return this.combatLogX;
//    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }
}
