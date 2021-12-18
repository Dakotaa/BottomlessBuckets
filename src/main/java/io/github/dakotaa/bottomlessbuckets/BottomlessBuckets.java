package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.plugin.java.JavaPlugin;

public class BottomlessBuckets extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabled");
        getServer().getPluginManager().registerEvents(new BucketListener(), this);
        getServer().getPluginManager().registerEvents(new BucketSwitchListener(), this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }
}
