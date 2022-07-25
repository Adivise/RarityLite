package me.adivise.raritylite;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class Raritylite extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        /// NEED PERMISSION TO USE THIS COMMAND!
        if (label.equalsIgnoreCase("enchanted")) {
            if (!sender.hasPermission("raritylite.enchanted")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        this.getConfig().getString("permission.message")));
                return true;
            }

            // NEED PLAYER TO RUN THIS COMMAND
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        this.getConfig().getString("console.message")));
                return true;
            }

            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();

            if (args.length == 0) {
                // /enchanted
                for (String msg : this.getConfig().getStringList("info.message")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
                return true;
            }

            if (args.length > 0) {
                // /enchanted
                if (args[0].equalsIgnoreCase("help")) {
                    for (String msg : this.getConfig().getStringList("info.message")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    return true;
                }

                // /enchanted list
                if (args[0].equalsIgnoreCase("list")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("list.message")));
                    return true;
                }

                // /enchanted list
                if (args[0].equalsIgnoreCase("reload")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("reload.message")));
                    this.saveDefaultConfig();
                    this.reloadConfig();
                    return true;
                }

                // RETURN WHEN PLAYER NOT HOLDING ITEM
                if (item.getType() == Material.AIR) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("hold_item.message")));
                    return true;
                }

                if (args[0].equalsIgnoreCase("hideflags")) {
                    ItemMeta m = item.getItemMeta();

                    m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    m.addItemFlags(ItemFlag.HIDE_DESTROYS);
                    m.addItemFlags(ItemFlag.HIDE_DYE);
                    m.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                    m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    m.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("hideflags.message")));
                }

                if (args[0].equalsIgnoreCase("common")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("common.displayName") + item.getI18NDisplayName()));

                    // DISPLAY ITEM LORE
                    m.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("common.loreName"))));

                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("common.success")));
                }
                if (args[0].equalsIgnoreCase("uncommon")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("uncommon.displayName") + item.getI18NDisplayName()));

                    // DISPLAY ITEM LORE
                    m.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("uncommon.loreName"))));

                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("uncommon.success")));
                }
                if (args[0].equalsIgnoreCase("rare")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("rare.displayName") + item.getI18NDisplayName()));

                    // DISPLAY ITEM LORE
                    m.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("rare.loreName"))));

                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("rare.success")));
                }
                if (args[0].equalsIgnoreCase("epic")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("epic.displayName") + item.getI18NDisplayName()));

                    // DISPLAY ITEM LORE
                    m.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("epic.loreName"))));

                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("epic.success")));
                }
                if (args[0].equalsIgnoreCase("legendary")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("legendary.displayName") + item.getI18NDisplayName()));

                    // DISPLAY ITEM LORE
                    m.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("legendary.loreName"))));

                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("legendary.success")));
                }
                if (args[0].equalsIgnoreCase("mythical")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("mythical.displayName") + item.getI18NDisplayName()));

                    // DISPLAY ITEM LORE
                    m.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("mythical.loreName"))));

                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("mythical.success")));
                }
                if (args[0].equalsIgnoreCase("divine")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("divine.displayName") + item.getI18NDisplayName()));

                    // DISPLAY ITEM LORE
                    m.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("divine.loreName"))));

                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("divine.success")));
                }
                if (args[0].equalsIgnoreCase("special")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("special.displayName") + item.getI18NDisplayName()));

                    // DISPLAY ITEM LORE
                    m.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("special.loreName"))));

                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("special.success")));
                }
            }
        }

        return false;
    }

  //  @EventHandler


}
