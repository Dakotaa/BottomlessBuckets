package io.github.dakotaa.bottomlessbuckets;

import io.github.dakotaa.bottomlessbuckets.config.ConfigWrapper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BottomlessBuckets extends JavaPlugin {

    private ConfigWrapper langFile = new ConfigWrapper(this, "", "lang.yml");
    private FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getLogger().info("Enabled");
        getServer().getPluginManager().registerEvents(new BucketUseListener(), this);
        getServer().getPluginManager().registerEvents(new BucketSwitchListener(), this);

        langFile.createNewFile("Loading BottomlessBuckets lang.yml", "BottomlessBuckets lang file");
        loadLang();

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

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }
}
