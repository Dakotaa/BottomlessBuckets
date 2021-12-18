package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.logging.Logger;

public class BucketListener implements Listener {

    @EventHandler
    public void onBucketFillEvent(PlayerBucketFillEvent e) {
        Logger l = Bukkit.getLogger();
        Player p = e.getPlayer();

        // get the item in the player's hand
        // TODO: Make this work for buckets in the off-hand
        l.info(p.getInventory().getItemInMainHand().toString());

        l.info("Itemstack: " + Objects.requireNonNull(e.getItemStack()).toString());
        e.setItemStack(new ItemStack(Material.BUCKET));
    }
}
