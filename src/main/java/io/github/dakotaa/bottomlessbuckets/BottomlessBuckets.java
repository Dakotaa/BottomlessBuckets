package io.github.dakotaa.bottomlessbuckets;

import io.github.dakotaa.bottomlessbuckets.config.ConfigWrapper;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class BottomlessBuckets extends JavaPlugin {
    public static BottomlessBuckets plugin;
    private ConfigWrapper langFile = new ConfigWrapper(this, "", "lang.yml");
    private FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new BucketUseListener(), this);
        getServer().getPluginManager().registerEvents(new BucketSwitchListener(), this);

        langFile.createNewFile("Loading BottomlessBuckets lang.yml", "BottomlessBuckets lang file");
        loadLang();

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
        p.sendMessage(Lang.NO_PERMISSION.getConfigValue(new String[] { permNeeded }));
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

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }
}
