package io.github.dakotaa.bottomlessbuckets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CommandBuckets implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (command.getName().equalsIgnoreCase("buckets")) {
            // send the help list
            if (args.length == 0) {
                sendHelp(sender);
                return true;
            }
            // reload command
            if (args[0].equalsIgnoreCase("reload")) {
                BottomlessBuckets.plugin.reload();
                Util.message(sender, true, "&aPlugin reloaded.");
                return true;
            }
            // /buckets get <water/lava> <capacity> <amount>
            if (args[0].equalsIgnoreCase("get")) {
                if (args.length >= 3) {
                    if (!(sender instanceof Player)) {
                        Util.message(sender, true, Lang.COMMAND_PLAYER_ONLY.getConfigValue());
                        return true;
                    }
                    Player player = (Player) sender;
                    if (!(args[1].equalsIgnoreCase("water") || args[1].equalsIgnoreCase("lava"))) {
                        Util.message(sender, true, Lang.COMMAND_INVALID_TYPE.getConfigValue());
                        return true;
                    }
                    if (args.length == 3) {
                        if (Util.isInt(args[2])) {
                            giveBucketItem(player, player, args[1], Integer.parseInt(args[2]), 1);
                            return true;
                        }
                    } else if (args.length == 4) {
                        if (Util.isInt(args[2]) && Util.isInt(args[3])) {
                            giveBucketItem(player, player, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                            return true;
                        }
                    }
                }
            }
            // /buckets give <player> <water/lava> <capacity> <amount>
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length >= 4) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        Util.message(sender, true, Lang.COMMAND_PLAYER_OFFLINE.getConfigValue());
                        return true;
                    }
                    if (!(args[2].equalsIgnoreCase("water") || args[2].equalsIgnoreCase("lava"))) {
                        Util.message(sender, true, Lang.COMMAND_INVALID_TYPE.getConfigValue());
                        return true;
                    }
                    if (args.length == 4) {
                        if (Util.isInt(args[3])) {
                            giveBucketItem(sender, player, args[2], Integer.parseInt(args[3]), 1);
                            return true;
                        }
                    } else if (args.length == 5) {
                        if (Util.isInt(args[3]) && Util.isInt(args[4])) {
                            giveBucketItem(sender, player, args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                            return true;
                        }
                    }
                }
            }
        }

        sendHelp((Player) sender);
        return true;
    }

    private void giveBucketItem(CommandSender sender, Player p, String type, int capacity, int quantity) {
        if (capacity <= 0) {
            Util.message(sender, true, Lang.COMMAND_INVALID_QUANTITY.getConfigValue());
            return;
        }
        ItemStack bucket = new ItemStack(Material.BUCKET, quantity);
        ItemMeta meta = bucket.getItemMeta();

        if (type.equalsIgnoreCase("water")) {
            meta.setDisplayName(BottomlessBuckets.getColouredConfigValue("bucket-item.name-water"));
        } else {
            meta.setDisplayName(BottomlessBuckets.getColouredConfigValue("bucket-item.name-lava"));
        }

        List<String> lore = BottomlessBuckets.plugin.getConfig().getStringList("bucket-item.lore.lines");
        for (int i = 0; i < lore.size(); i++) {
            String line = ChatColor.translateAlternateColorCodes('&', lore.get(i));
            if (line.contains("type-label")) {
                String typeLabel = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.type.label");
                String typeValue;
                if (type.equals("water")) typeValue = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.type.water");
                else typeValue = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.type.lava");
                lore.set(i, (line.replace("%type-label%", typeLabel).replace("%type-value%", typeValue)));
            } else if (line.contains("amount-label")) {
                String amountLabel = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.amount.label");
                lore.set(i, line.replace("%amount-label%", amountLabel).replace("%amount-value%", "0"));
            } else if (line.contains("capacity-label")) {
                String capacityLabel = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.capacity.label");
                lore.set(i, line.replace("%capacity-label%", capacityLabel).replace("%capacity-value%", String.valueOf(capacity)));
            } else if (line.contains("mode-label")) {
                String modeLabel = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.mode.label");
                String modeFill = BottomlessBuckets.getColouredConfigValue("bucket-item.lore.mode.fill");
                lore.set(i, line.replace("%mode-label%", modeLabel).replace("%mode-value%", modeFill));
            }
        }
        meta.setLore(lore);
        bucket.setItemMeta(meta);
        p.getInventory().addItem(bucket);

        if (!sender.equals(p)) {
            Util.message((Player) sender, true, Lang.COMMAND_BUCKET_GIVEN.getConfigValue(
                    new String[] {"%type%", "%capacity%", "%player%"},
                    new String[] {type, String.valueOf(capacity), p.getName()}));
        }

        if (quantity == 1) Util.message(p, true, Lang.COMMAND_BUCKET_RECEIVED.getConfigValue(
                new String[] {"%type%", "%capacity%"},
                new String[] {type, String.valueOf(capacity)}));
        else Util.message(p, true, Lang.COMMAND_BUCKETS_RECEIVED.getConfigValue(
                new String[] {"%type%", "%quantity%", "%player%"},
                new String[] {type, String.valueOf(quantity), String.valueOf(capacity)}));
    }

    private void sendHelp(CommandSender p) {
        Util.message(p, false, "&8]&8&m----------&8[ &b&lBottomless Buckets &8]&8&m----------&8[ ");
        Util.message(p, false, "&f/buckets &7- Shows this help list");
        Util.message(p, false, "&b/buckets get [water|lava] &b<capacity> [<quantity>] &7- Give yourself bottomless bucket(s).");
        Util.message(p, false, "&b/buckets give <player> [water|lava] &b<capacity> [<quantity>] &7- Give a player bottomless bucket(s).");
        Util.message(p, false, "&b/buckets reload &7- Reload the config and lang.");
        Util.message(p, false, "&8]&8&m------------------------------------------&8[ ");
    }
}
