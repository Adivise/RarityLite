package me.adivise.raritylite;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

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
                    // get all rarity in config
                    ArrayList<String> rarity = new ArrayList<String>();
                    for (String r : this.getConfig().getConfigurationSection("rarity").getKeys(false)) {
                        rarity.add(r);
                    }
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("list.message").replace("%rarity%",
                                    String.join(", ", rarity))));
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

                // Get all rarity in config
                for (String rarity : this.getConfig().getConfigurationSection("rarity").getKeys(false)) {
                    if (args[0].equalsIgnoreCase(rarity)) {
                        ItemMeta m = item.getItemMeta();
                        // DISPLAY ITEM NAME
                        if (m.hasDisplayName()) {
                            m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                    this.getConfig().getString("rarity." + rarity + ".prefixName") + m.getDisplayName() + this.getConfig().getString("rarity." + rarity + ".suffixName")));
                        } else {
                            m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                    this.getConfig().getString("rarity." + rarity + ".prefixName") + item.getI18NDisplayName() + this.getConfig().getString("rarity." + rarity + ".suffixName")));
                        }

                        // DISPLAY ITEM LORE
                        ArrayList<String> lore = new ArrayList<String>();
                        for (String msg : this.getConfig().getStringList("rarity." + rarity + ".loreName")) {
                            lore.add(ChatColor.translateAlternateColorCodes('&', msg));
                        }
                        m.setLore(lore);

                        // /enchanted common glow
                        if (args.length >= 2 && args[1].equalsIgnoreCase("glow")) {
                            m.addEnchant(Enchantment.DURABILITY, 1, false);
                            for (String msg : this.getConfig().getStringList("rarity." + rarity + ".flags")) {
                                m.addItemFlags(ItemFlag.valueOf(msg));
                            }
                        }

                        // Check unbreakable from config
                        m.setUnbreakable(this.getConfig().getBoolean("rarity." + rarity + ".unbreakable"));

                        item.setItemMeta(m);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("rarity." + rarity + ".success")));
                    }
                }

            }
        }

        return false;
    }

    // PickupEvent
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) // Pickup item all item on ground and set the lore
    public void onPickUpItem(ItemSpawnEvent event) {
        ItemStack item = event.getEntity().getItemStack();
        ItemMeta m = item.getItemMeta();
        // For Lore
        if (m.hasLore()) {
            /// Not set when having lore
        } else {
            // Default item pickup
            ArrayList<String> lore = new ArrayList<String>();
            for (String msg : this.getConfig().getStringList("pickup.default.loreName")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', msg));
            }
            m.setLore(lore);
            item.setItemMeta(m);
            /// Add Custom item pickup
            for (String msg : this.getConfig().getConfigurationSection("pickup.custom").getKeys(false)) {
                if (item.getType() == Material.valueOf(msg)) {
                    ArrayList<String> lore2 = new ArrayList<String>();
                    for (String msg2 : this.getConfig().getStringList("pickup.custom." + msg + ".loreName")) {
                        lore2.add(ChatColor.translateAlternateColorCodes('&', msg2));
                    }
                    m.setLore(lore2);
                    item.setItemMeta(m);
                }
            }
        }
        // For Name
        if (m.hasDisplayName()) {
            /// Not set when having name
        } else {
            // Default item pickup
            m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    this.getConfig().getString("pickup.default.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("pickup.default.suffixName")));
            item.setItemMeta(m);
            /// Add Custom item pickup
            for (String msg : this.getConfig().getConfigurationSection("pickup.custom").getKeys(false)) {
                if (item.getType() == Material.valueOf(msg)) {
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("pickup.custom." + msg + ".prefixName") + item.getI18NDisplayName() + this.getConfig().getString("pickup.custom." + msg + ".suffixName")));
                    item.setItemMeta(m);
                }
            }
        }
    }
    // InventoryUpdateEvent
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) // InventoryUpdateEvent
    public void onInventoryUpdate(InventoryPickupItemEvent event) { // For Hopper and Dispenser ETC.
        ItemStack item = event.getItem().getItemStack();
        ItemMeta m = item.getItemMeta();
        // For Lore
        if (m.hasLore()) {
            /// Not set when having lore
        } else {
            // Default item pickup
            ArrayList<String> lore = new ArrayList<String>();
            for (String msg : this.getConfig().getStringList("pickup.default.loreName")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', msg));
            }
            m.setLore(lore);
            item.setItemMeta(m);
            /// Add Custom item pickup
            for (String msg : this.getConfig().getConfigurationSection("pickup.custom").getKeys(false)) {
                if (item.getType() == Material.valueOf(msg)) {
                    ArrayList<String> lore2 = new ArrayList<String>();
                    for (String msg2 : this.getConfig().getStringList("pickup.custom." + msg + ".loreName")) {
                        lore2.add(ChatColor.translateAlternateColorCodes('&', msg2));
                    }
                    m.setLore(lore2);
                    item.setItemMeta(m);
                }
            }
        }
        // For Name
        if (m.hasDisplayName()) {
            /// Not set when having name
        } else {
            // Default item pickup
            m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    this.getConfig().getString("pickup.default.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("pickup.default.suffixName")));
            item.setItemMeta(m);
            /// Add Custom item pickup
            for (String msg : this.getConfig().getConfigurationSection("pickup.custom").getKeys(false)) {
                if (item.getType() == Material.valueOf(msg)) {
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("pickup.custom." + msg + ".prefixName") + item.getI18NDisplayName() + this.getConfig().getString("pickup.custom." + msg + ".suffixName")));
                    item.setItemMeta(m);
                }
            }
        }
    }
    // CraftItemEvent
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) // CraftItemEvent
    public void onCraftItem(CraftItemEvent event) {
        ItemStack item = event.getCurrentItem();
        ItemMeta m = item.getItemMeta();
        // For Lore
        if (m.hasLore()) {
            /// Not set when having lore
        } else {
            // Default item pickup
            ArrayList<String> lore = new ArrayList<String>();
            for (String msg : this.getConfig().getStringList("pickup.default.loreName")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', msg));
            }
            m.setLore(lore);
            item.setItemMeta(m);
            /// Add Custom item pickup
            for (String msg : this.getConfig().getConfigurationSection("pickup.custom").getKeys(false)) {
                if (item.getType() == Material.valueOf(msg)) {
                    ArrayList<String> lore2 = new ArrayList<String>();
                    for (String msg2 : this.getConfig().getStringList("pickup.custom." + msg + ".loreName")) {
                        lore2.add(ChatColor.translateAlternateColorCodes('&', msg2));
                    }
                    m.setLore(lore2);
                    item.setItemMeta(m);
                }
            }
        }
        // For Name
        if (m.hasDisplayName()) {
            /// Not set when having name
        } else {
            // Default item pickup
            m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    this.getConfig().getString("pickup.default.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("pickup.default.suffixName")));
            item.setItemMeta(m);
            /// Add Custom item pickup
            for (String msg : this.getConfig().getConfigurationSection("pickup.custom").getKeys(false)) {
                if (item.getType() == Material.valueOf(msg)) {
                    m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("pickup.custom." + msg + ".prefixName") + item.getI18NDisplayName() + this.getConfig().getString("pickup.custom." + msg + ".suffixName")));
                    item.setItemMeta(m);
                }
            }
        }
    }
    // update item name and lore when open inventory
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getType() == InventoryType.PLAYER) {
            return;
        }
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null) {
                continue;
            }
            ItemMeta m = item.getItemMeta();
            // For Lore
            if (m.hasLore()) {
                /// Not set when having lore
            } else {
                // Default item pickup
                ArrayList<String> lore = new ArrayList<String>();
                for (String msg : this.getConfig().getStringList("pickup.default.loreName")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', msg));
                }
                m.setLore(lore);
                item.setItemMeta(m);
                /// Add Custom item pickup
                for (String msg : this.getConfig().getConfigurationSection("pickup.custom").getKeys(false)) {
                    if (item.getType() == Material.valueOf(msg)) {
                        ArrayList<String> lore2 = new ArrayList<String>();
                        for (String msg2 : this.getConfig().getStringList("pickup.custom." + msg + ".loreName")) {
                            lore2.add(ChatColor.translateAlternateColorCodes('&', msg2));
                        }
                        m.setLore(lore2);
                        item.setItemMeta(m);
                    }
                }
            }
            // For Name
            if (m.hasDisplayName()) {
                /// Not set when having name
            } else {
                // Default item pickup
                m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                        this.getConfig().getString("pickup.default.prefixName") + item.getI18NDisplayName() + this.getConfig().getString("pickup.default.suffixName")));
                item.setItemMeta(m);
                /// Add Custom item pickup
                for (String msg : this.getConfig().getConfigurationSection("pickup.custom").getKeys(false)) {
                    if (item.getType() == Material.valueOf(msg)) {
                        m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                this.getConfig().getString("pickup.custom." + msg + ".prefixName") + item.getI18NDisplayName() + this.getConfig().getString("pickup.custom." + msg + ".suffixName")));
                        item.setItemMeta(m);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) // Blocked Placing of Items have rarity!
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().hasItemMeta()) {
            // Name of the item in the config
            if (event.getItemInHand().getItemMeta().hasDisplayName()) {
                for (String msg : this.getConfig().getStringList("blocked.name")) { // Name of the item in the config
                    if (event.getItemInHand().getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                        event.setCancelled(true);
                    }
                }
            }
            // Lore of the item in the config
            for (String msg : this.getConfig().getStringList("blocked.lore")) {
                if (event.getItemInHand().getItemMeta().hasLore()) {
                    for (String lore : event.getItemInHand().getItemMeta().getLore()) {
                        if (lore.contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
            // Flags of the item in the config
            for (String flags : this.getConfig().getStringList("blocked.flags")) {
                if (event.getItemInHand().getItemMeta().hasItemFlag(ItemFlag.valueOf(flags))) {
                    event.setCancelled(true);
                }
            }
        }
    } // Blocked Use of Items have rarity!
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().hasItemMeta()) {
                // Name of the item in the config
                if (event.getItem().getItemMeta().hasDisplayName()) {
                    for (String msg : this.getConfig().getStringList("blocked.name")) { // Name of the item in the config
                        if (event.getItem().getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                            event.setCancelled(true);
                        }
                    }
                }
                // Lore of the item in the config
                for (String msg : this.getConfig().getStringList("blocked.lore")) {
                    if (event.getItem().getItemMeta().hasLore()) {
                        for (String lore : event.getItem().getItemMeta().getLore()) {
                            if (lore.contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
                // Flags of the item in the config
                for (String flags : this.getConfig().getStringList("blocked.flags")) {
                    if (event.getItem().getItemMeta().hasItemFlag(ItemFlag.valueOf(flags))) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    // Cancel interaction mobs when item have rarity!
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMobsInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof LivingEntity) {
            if (event.getPlayer().getItemInHand().hasItemMeta()) {
                // Name of the item in the config
                if (event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) {
                    for (String msg : this.getConfig().getStringList("blocked.name")) { // Name of the item in the config
                        if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                            event.setCancelled(true);
                        }
                    }
                }
                // Lore of the item in the config
                for (String msg : this.getConfig().getStringList("blocked.lore")) {
                    if (event.getPlayer().getItemInHand().getItemMeta().hasLore()) {
                        for (String lore : event.getPlayer().getItemInHand().getItemMeta().getLore()) {
                            if (lore.contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
                // Flags of the item in the config
                for (String flags : this.getConfig().getStringList("blocked.flags")) {
                    if (event.getPlayer().getItemInHand().getItemMeta().hasItemFlag(ItemFlag.valueOf(flags))) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
