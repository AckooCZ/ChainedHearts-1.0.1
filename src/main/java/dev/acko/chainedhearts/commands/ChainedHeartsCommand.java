package dev.acko.chainedhearts.commands;

import dev.acko.chainedhearts.ChainedHearts;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChainedHeartsCommand implements CommandExecutor, TabCompleter {
    
    private final ChainedHearts plugin;
    
    public ChainedHeartsCommand(ChainedHearts plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chainedhearts.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("commands.no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "settings" -> {
                if (sender instanceof Player player) {
                    openSettingsMenu(player);
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTento příkaz může použít pouze hráč!"));
                }
            }
            
            case "reload" -> {
                plugin.reloadConfig();
                plugin.getConfigManager().reloadMenuConfig();
                sender.sendMessage(plugin.getConfigManager().getMessage("commands.reloaded"));
            }
            
            default -> sendHelp(sender);
        }
        
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(plugin.getConfigManager().getMessage("commands.help.header"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/chainedhearts settings &7- Otevře GUI menu"));
        sender.sendMessage(plugin.getConfigManager().getMessage("commands.help.reload"));
    }
    
    private void openSettingsMenu(Player player) {
        FileConfiguration menuConfig = plugin.getConfigManager().getMenuConfig();
        Inventory settingsInv = Bukkit.createInventory(null, 
            menuConfig.getInt("menu.size", 54), 
            ChatColor.translateAlternateColorCodes('&', menuConfig.getString("menu.title", "&6&lChainedHearts Nastavení")));
        
        createMenuItem(settingsInv, "plugin_toggle", 10, 
            plugin.isChainedEnabled(), 
            plugin.isChainedEnabled() ? "&aPlugin is enabled" : "&cPlugin is disabled");
        
        createMenuItem(settingsInv, "shared_damage", 11, 
            plugin.getConfigManager().isSharedDamageEnabled(), 
            plugin.getConfigManager().isSharedDamageEnabled() ? "&aShared DMG is enabled" : "&cShared DMG is disabled");
        
        createMenuItem(settingsInv, "shared_hunger", 13, 
            plugin.getConfigManager().isSharedHungerEnabled(), 
            plugin.getConfigManager().isSharedHungerEnabled() ? "&aShared Hunger is enabled" : "&cShared Hunger is disabled");
        
        createMenuItem(settingsInv, "shared_effects", 14, 
            plugin.getConfigManager().isSharedEffectsEnabled(), 
            plugin.getConfigManager().isSharedEffectsEnabled() ? "&aShared effects are enabled" : "&cShared effects are disabled");
        
        createMenuItem(settingsInv, "actionbar_messages", 15, 
            plugin.getConfigManager().isActionbarMessagesEnabled(), 
            plugin.getConfigManager().isActionbarMessagesEnabled() ? "&aAction bar messages are enabled" : "&cAction bar messages are disabled");
        
        createMenuItem(settingsInv, "one_heart", 16, 
            plugin.getConfigManager().isOneHeartModeEnabled(), 
            plugin.getConfigManager().isOneHeartModeEnabled() ? "&aOne heart mode is enabled" : "&cOne heart mode is disabled");
        
        if (menuConfig.contains("menu.items.separator.slots")) {
            for (int slot : menuConfig.getIntegerList("menu.items.separator.slots")) {
                ItemStack separator = createItem(Material.valueOf(menuConfig.getString("menu.items.separator.material", "GRAY_STAINED_GLASS_PANE")), 
                    ChatColor.translateAlternateColorCodes('&', menuConfig.getString("menu.items.separator.name", "&f")));
                settingsInv.setItem(slot, separator);
            }
        }
        
        if (menuConfig.contains("menu.items.close")) {
            ItemStack closeItem = createItem(Material.valueOf(menuConfig.getString("menu.items.close.material", "BARRIER")), 
                ChatColor.translateAlternateColorCodes('&', menuConfig.getString("menu.items.close.name", "&cZavřít menu")));
            settingsInv.setItem(menuConfig.getInt("menu.items.close.slot", 53), closeItem);
        }
        
        player.openInventory(settingsInv);
    }
    
    private void createMenuItem(Inventory inv, String configKey, int defaultSlot, boolean enabled, String customLore) {
        FileConfiguration menuConfig = plugin.getConfigManager().getMenuConfig();
        String path = "menu.items." + configKey;
        
        int slot = menuConfig.getInt(path + ".slot", defaultSlot);
        Material material = enabled ? 
            Material.valueOf(menuConfig.getString(path + ".material_enabled", "LIME_CONCRETE")) :
            Material.valueOf(menuConfig.getString(path + ".material_disabled", "RED_CONCRETE"));
        
        String name = ChatColor.translateAlternateColorCodes('&', menuConfig.getString(path + ".name", "&6" + configKey));
        String lore = ChatColor.translateAlternateColorCodes('&', customLore);
        
        ItemStack item = createItem(material, name, lore);
        inv.setItem(slot, item);
    }
    
        
    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("chainedhearts.admin")) {
            return new ArrayList<>();
        }
        
        if (args.length == 1) {
            return Arrays.asList("settings", "reload");
        }
        
        return new ArrayList<>();
    }
}
