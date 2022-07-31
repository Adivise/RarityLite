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
import org.bukkit.event.block.Action;
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

        this.getCommand("raritylite").setTabCompleter(new RarityTab());
        this.getCommand("rtl").setTabCompleter(new RarityTab());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        /// NEED PERMISSION TO USE THIS COMMAND! /// /raritylite & /rtl
        if (label.equalsIgnoreCase("raritylite") || label.equalsIgnoreCase("rtl")) {
            if (!sender.hasPermission("raritylite.admin")) {
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
                if (args[0].equalsIgnoreCase("flag")) {
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

    public void applyLore(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore()) {
            /// Not set when having lore
        } else {
            // Default item pickup
            ArrayList<String> lore = new ArrayList<String>();
            for (String msg : this.getConfig().getStringList("pickup.default.loreName")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', msg));
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            /// Add Custom item pickup
            for (String msg : this.getConfig().getConfigurationSection("pickup.custom").getKeys(false)) {
                if (itemStack.getType() == Material.valueOf(msg)) {
                    ArrayList<String> lore2 = new ArrayList<String>();
                    for (String msg2 : this.getConfig().getStringList("pickup.custom." + msg + ".loreName")) {
                        lore2.add(ChatColor.translateAlternateColorCodes('&', msg2));
                    }
                    itemMeta.setLore(lore2);
                    itemStack.setItemMeta(itemMeta);
                }
            }
        }
        // For Name
        if (itemMeta.hasDisplayName()) {
            /// Not set when having name
        } else {
            // Default item pickup
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    this.getConfig().getString("pickup.default.prefixName") + itemStack.getI18NDisplayName() + this.getConfig().getString("pickup.default.suffixName")));
            itemStack.setItemMeta(itemMeta);
            /// Add Custom item pickup
            for (String msg : this.getConfig().getConfigurationSection("pickup.custom").getKeys(false)) {
                if (itemStack.getType() == Material.valueOf(msg)) {
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            this.getConfig().getString("pickup.custom." + msg + ".prefixName") + itemStack.getI18NDisplayName() + this.getConfig().getString("pickup.custom." + msg + ".suffixName")));
                    itemStack.setItemMeta(itemMeta);
                }
            }
        }
    }

    // Update spawn item event
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickUpItem(ItemSpawnEvent event) {
        this.applyLore(event.getEntity().getItemStack());
    }

    // Update on open something hopper, furnace, etc.
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onOpenUpdate(InventoryPickupItemEvent event) {
        this.applyLore(event.getItem().getItemStack());
    }

    // Update on open inventory
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onOpenInventory(InventoryOpenEvent event) { // because "itemStack" is null
        Inventory inventory = event.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) {
                this.applyLore(itemStack);
            }
        }
    }

    // PrepareItemCraftEvent
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPrepareItemCraft(PrepareItemCraftEvent event) { // "itemStack" is null when crafting item
        if (event.getRecipe() != null) {
            ItemStack itemStack = event.getInventory().getResult();
            if (itemStack != null) {
                this.applyLore(itemStack);
            }
        }
    }

    // Prevent block place event
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            //// BLOCKED MAIN-HAND ITEM PLACING
            if (event.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
                // Name of the item in the config
                if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                    for (String msg : this.getConfig().getStringList("blocked.name")) { // Name of the item in the config
                        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                            event.setCancelled(true);
                        }
                    }
                }
                // Lore of the item in the config
                for (String msg : this.getConfig().getStringList("blocked.lore")) {
                    if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                        for (String lore : event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore()) {
                            if (lore.contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
                // Flags of the item in the config
                for (String flags : this.getConfig().getStringList("blocked.flags")) {
                    if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasItemFlag(ItemFlag.valueOf(flags))) {
                        event.setCancelled(true);
                    }
                }
            }

            //// BLOCKED OFF-HAND ITEM PLACING
            if (event.getPlayer().getInventory().getItemInOffHand().hasItemMeta()) {
                // Name of the item in the config
                if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta().hasDisplayName()) {
                    for (String msg : this.getConfig().getStringList("blocked.name")) { // Name of the item in the config
                        if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                            event.setCancelled(true);
                        }
                    }
                }
                // Lore of the item in the config
                for (String msg : this.getConfig().getStringList("blocked.lore")) {
                    if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta().hasLore()) {
                        for (String lore : event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getLore()) {
                            if (lore.contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
                // Flags of the item in the config
                for (String flags : this.getConfig().getStringList("blocked.flags")) {
                    if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta().hasItemFlag(ItemFlag.valueOf(flags))) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    // Prevent blocked use in anvil
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAnvilUse(PrepareAnvilEvent event) {
        if (event.getInventory().getItem(0) != null) {
            if (event.getInventory().getItem(0).hasItemMeta()) {
                for (String msg : this.getConfig().getStringList("blocked.name")) { // Name of the item in the config
                    if (event.getInventory().getItem(0).getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                        event.setResult(new ItemStack(Material.AIR));
                    }
                }
            }
            for (String msg : this.getConfig().getStringList("blocked.lore")) {
                if (event.getInventory().getItem(0).getItemMeta().hasLore()) {
                    for (String lore : event.getInventory().getItem(0).getItemMeta().getLore()) {
                        if (lore.contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                            event.setResult(new ItemStack(Material.AIR));
                        }
                    }
                }
            }
            for (String flags : this.getConfig().getStringList("blocked.flags")) {
                if (event.getInventory().getItem(0).getItemMeta().hasItemFlag(ItemFlag.valueOf(flags))) {
                    event.setResult(new ItemStack(Material.AIR));
                }
            }
        }
        if (event.getInventory().getItem(1) != null) {
            if (event.getInventory().getItem(1).hasItemMeta()) {
                for (String msg : this.getConfig().getStringList("blocked.name")) { // Name of the item in the config
                    if (event.getInventory().getItem(1).getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                        event.setResult(new ItemStack(Material.AIR));
                    }
                }
            }
            for (String msg : this.getConfig().getStringList("blocked.lore")) {
                if (event.getInventory().getItem(1).getItemMeta().hasLore()) {
                    for (String lore : event.getInventory().getItem(1).getItemMeta().getLore()) {
                        if (lore.contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                            event.setResult(new ItemStack(Material.AIR));
                        }
                    }
                }
            }
            for (String flags : this.getConfig().getStringList("blocked.flags")) {
                if (event.getInventory().getItem(1).getItemMeta().hasItemFlag(ItemFlag.valueOf(flags))) {
                    event.setResult(new ItemStack(Material.AIR));
                }
            }
        }
    }

    // Prevent interact to mobs event
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof LivingEntity) {

            //// BLOCKED MAIN-HAND ITEM TAMING
            if (event.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
                // Name of the item in the config
                if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                    for (String msg : this.getConfig().getStringList("blocked.name")) { // Name of the item in the config
                        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                            event.setCancelled(true);
                        }
                    }
                }
                // Lore of the item in the config
                for (String msg : this.getConfig().getStringList("blocked.lore")) {
                    if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                        for (String lore : event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore()) {
                            if (lore.contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
                // Flags of the item in the config
                for (String flags : this.getConfig().getStringList("blocked.flags")) {
                    if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasItemFlag(ItemFlag.valueOf(flags))) {
                        event.setCancelled(true);
                    }
                }
            }

            //// BLOCKED OFF-HAND ITEM TAMING
            if (event.getPlayer().getInventory().getItemInOffHand().hasItemMeta()) {
                // Name of the item in the config
                if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta().hasDisplayName()) {
                    for (String msg : this.getConfig().getStringList("blocked.name")) { // Name of the item in the config
                        if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                            event.setCancelled(true);
                        }
                    }
                    // Lore of the item in the config
                    for (String msg : this.getConfig().getStringList("blocked.lore")) {
                        if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta().hasLore()) {
                            for (String lore : event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getLore()) {
                                if (lore.contains(ChatColor.translateAlternateColorCodes('&', msg))) {
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                    // Flags of the item in the config
                    for (String flags : this.getConfig().getStringList("blocked.flags")) {
                        if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta().hasItemFlag(ItemFlag.valueOf(flags))) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
