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

import java.util.ArrayList;
import java.util.Arrays;

public final class Raritylite extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);

        this.getCommand("enchanted").setTabCompleter(new RarityTab());
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

                // /enchanted hideflags
                if (args[0].equalsIgnoreCase("hideflags")) {
                    ItemMeta m = item.getItemMeta();

                    for (String msg : this.getConfig().getStringList("hide_flags.flags")) {
                        m.addItemFlags(ItemFlag.valueOf(msg));
                    }

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("hide_flags.message")));
                }

                // /enchanted unbreakable
                if (args[0].equalsIgnoreCase("unbreakable")) {
                    ItemMeta m = item.getItemMeta();
                    m.setUnbreakable(true);
                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("unbreakable.message")));
                }

                // /enchanted glow
                if (args[0].equalsIgnoreCase("glow")) {
                    ItemMeta m = item.getItemMeta();

                    m.addEnchant(Enchantment.DURABILITY, 1, false);
                    m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("glow.message")));
                }

                // CUSTOM RARITY!
                if (args[0].equalsIgnoreCase("custom")) {
                    ItemMeta m = item.getItemMeta();

                    if (m.hasDisplayName()) {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                args[1] + " " + m.getDisplayName() + " " + args[2]));
                    } else {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                args[1] + " " + item.getI18NDisplayName() + " " + args[2]));
                    }

                    m.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', args[3])));

                    item.setItemMeta(m);
                    if (m.hasDisplayName()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("custom.message") + m.getDisplayName()));
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("custom.message") + item.getI18NDisplayName()));
                    }

                }
                // COMMON RARITY!
                if (args[0].equalsIgnoreCase("common")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    if (m.hasDisplayName()) {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("common.prefixName") + m.getDisplayName() + this.getConfig().getString("common.suffixName")));
                    } else {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("common.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("common.suffixName")));
                    }

                    // DISPLAY ITEM LORE
                    ArrayList<String> lore = new ArrayList<String>();

                    for (String msg : this.getConfig().getStringList("common.loreName")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    m.setLore(lore);

                    // /enchanted common glow
                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        for (String msg : this.getConfig().getStringList("common.flags")) {
                            m.addItemFlags(ItemFlag.valueOf(msg));
                        }
                    }

                    // Check unbreakable from config
                    m.setUnbreakable(this.getConfig().getBoolean("common.unbreakable"));

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("common.success")));
                }
                // UNCOMMON RARITY!
                if (args[0].equalsIgnoreCase("uncommon")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    if (m.hasDisplayName()) {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("uncommon.prefixName") + m.getDisplayName() + this.getConfig().getString("uncommon.suffixName")));
                    } else {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("uncommon.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("uncommon.suffixName")));
                    }

                    // DISPLAY ITEM LORE
                    ArrayList<String> lore = new ArrayList<String>();

                    for (String msg : this.getConfig().getStringList("uncommon.loreName")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    m.setLore(lore);

                    // /enchanted common glow
                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        for (String msg : this.getConfig().getStringList("uncommon.flags")) {
                            m.addItemFlags(ItemFlag.valueOf(msg));
                        }
                    }

                    // Check unbreakable from config
                    m.setUnbreakable(this.getConfig().getBoolean("uncommon.unbreakable"));

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("uncommon.success")));
                }
                // RARE RARITY!
                if (args[0].equalsIgnoreCase("rare")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    if (m.hasDisplayName()) {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("rare.prefixName") + m.getDisplayName() + this.getConfig().getString("rare.suffixName")));
                    } else {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("rare.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("rare.suffixName")));
                    }

                    // DISPLAY ITEM LORE
                    ArrayList<String> lore = new ArrayList<String>();

                    for (String msg : this.getConfig().getStringList("rare.loreName")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    m.setLore(lore);

                    // /enchanted common glow
                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        for (String msg : this.getConfig().getStringList("rare.flags")) {
                            m.addItemFlags(ItemFlag.valueOf(msg));
                        }
                    }

                    // Check unbreakable from config
                    m.setUnbreakable(this.getConfig().getBoolean("rare.unbreakable"));

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("rare.success")));
                }
                // EPIC RARITY!
                if (args[0].equalsIgnoreCase("epic")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    if (m.hasDisplayName()) {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("epic.prefixName") + m.getDisplayName() + this.getConfig().getString("epic.suffixName")));
                    } else {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("epic.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("epic.suffixName")));
                    }

                    // DISPLAY ITEM LORE
                    ArrayList<String> lore = new ArrayList<String>();

                    for (String msg : this.getConfig().getStringList("epic.loreName")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    m.setLore(lore);

                    // /enchanted common glow
                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        for (String msg : this.getConfig().getStringList("epic.flags")) {
                            m.addItemFlags(ItemFlag.valueOf(msg));
                        }
                    }

                    // Check unbreakable from config
                    m.setUnbreakable(this.getConfig().getBoolean("epic.unbreakable"));

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("epic.success")));
                }
                // LEGENDARY RARITY!
                if (args[0].equalsIgnoreCase("legendary")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    if (m.hasDisplayName()) {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("legendary.prefixName") + m.getDisplayName() + this.getConfig().getString("legendary.suffixName")));
                    } else {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("legendary.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("legendary.suffixName")));
                    }

                    // DISPLAY ITEM LORE
                    ArrayList<String> lore = new ArrayList<String>();

                    for (String msg : this.getConfig().getStringList("legendary.loreName")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    m.setLore(lore);

                    // /enchanted common glow
                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        for (String msg : this.getConfig().getStringList("legendary.flags")) {
                            m.addItemFlags(ItemFlag.valueOf(msg));
                        }
                    }

                    // Check unbreakable from config
                    m.setUnbreakable(this.getConfig().getBoolean("legendary.unbreakable"));

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("legendary.success")));
                }
                // MYTHICAL RARITY!
                if (args[0].equalsIgnoreCase("mythical")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    if (m.hasDisplayName()) {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("mythical.prefixName") + m.getDisplayName() + this.getConfig().getString("mythical.suffixName")));
                    } else {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("mythical.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("mythical.suffixName")));
                    }

                    // DISPLAY ITEM LORE
                    ArrayList<String> lore = new ArrayList<String>();

                    for (String msg : this.getConfig().getStringList("mythical.loreName")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    m.setLore(lore);

                    // /enchanted common glow
                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        for (String msg : this.getConfig().getStringList("mythical.flags")) {
                            m.addItemFlags(ItemFlag.valueOf(msg));
                        }
                    }

                    // Check unbreakable from config
                    m.setUnbreakable(this.getConfig().getBoolean("mythical.unbreakable"));

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("mythical.success")));
                }
                // DIVINE RARITY!
                if (args[0].equalsIgnoreCase("divine")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    if (m.hasDisplayName()) {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("divine.prefixName") + m.getDisplayName() + this.getConfig().getString("divine.suffixName")));
                    } else {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("divine.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("divine.suffixName")));
                    }

                    // DISPLAY ITEM LORE
                    ArrayList<String> lore = new ArrayList<String>();

                    for (String msg : this.getConfig().getStringList("divine.loreName")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    m.setLore(lore);

                    // /enchanted common glow
                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        for (String msg : this.getConfig().getStringList("divine.flags")) {
                            m.addItemFlags(ItemFlag.valueOf(msg));
                        }
                    }

                    // Check unbreakable from config
                    m.setUnbreakable(this.getConfig().getBoolean("divine.unbreakable"));

                    item.setItemMeta(m);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("divine.success")));
                }
                // SPECIAL RARITY!
                if (args[0].equalsIgnoreCase("special")) {
                    ItemMeta m = item.getItemMeta();
                    // DISPLAY ITEM NAME
                    if (m.hasDisplayName()) {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("special.prefixName") + m.getDisplayName() + this.getConfig().getString("special.suffixName")));
                    } else {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("special.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("special.suffixName")));
                    }

                    // DISPLAY ITEM LORE
                    ArrayList<String> lore = new ArrayList<String>();

                    for (String msg : this.getConfig().getStringList("special.loreName")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    m.setLore(lore);

                    // /enchanted common glow
                    if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                        m.addEnchant(Enchantment.DURABILITY, 1, false);
                        for (String msg : this.getConfig().getStringList("special.flags")) {
                            m.addItemFlags(ItemFlag.valueOf(msg));
                        }
                    }

                    // Check unbreakable from config
                    m.setUnbreakable(this.getConfig().getBoolean("special.unbreakable"));

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
