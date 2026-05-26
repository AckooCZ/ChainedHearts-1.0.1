package dev.acko.chainedhearts.listeners;

import dev.acko.chainedhearts.ChainedHearts;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettingsListener implements Listener {
    
    private final ChainedHearts plugin;
    
    public SettingsListener(ChainedHearts plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&6&lChainedHearts Settings"))) {
            return;
        }
        
        event.setCancelled(true);
        
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) {
            return;
        }
        
        String itemName = clicked.getItemMeta().getDisplayName();
        
        switch (itemName) {
            case "§6§lPlugin OFF/ON":
                togglePluginStatus(player);
                break;
                
            case "§6Shared DMG OFF/ON":
                toggleSharedDamage(player);
                break;
                
            case "§6Shared Hunger OFF/ON":
                toggleSharedHunger(player);
                break;
                
            case "§6Shared Effects OFF/ON":
                toggleSharedEffects(player);
                break;
                
            case "§6Action Bar Messages OFF/ON":
                toggleActionbarMessages(player);
                break;
                
            case "§6One Heart Mode OFF/ON":
                toggleOneHeartMode(player);
                break;
                
            case "§cClose Menu":
                player.closeInventory();
                break;
        }
    }
    
    private void togglePluginStatus(Player player) {
        boolean newState = !plugin.isChainedEnabled();
        plugin.setChainedEnabled(newState);
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            newState ? "&aPlugin has been enabled!" : "&cPlugin has been disabled!"));
        
        player.closeInventory();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.performCommand("chainedhearts settings");
        }, 1L);
    }
    
        
    private void toggleSharedDamage(Player player) {
        boolean currentState = plugin.getConfigManager().isSharedDamageEnabled();
        plugin.getConfigManager().setSharedDamageEnabled(!currentState);
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            !currentState ? "&aShared DMG has been enabled!" : "&cShared DMG has been disabled!"));
        
        player.closeInventory();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.performCommand("chainedhearts settings");
        }, 1L);
    }
    
    private void toggleSharedHunger(Player player) {
        boolean currentState = plugin.getConfigManager().isSharedHungerEnabled();
        plugin.getConfigManager().setSharedHungerEnabled(!currentState);
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            !currentState ? "&aShared Hunger has been enabled!" : "&cShared Hunger has been disabled!"));
        
        player.closeInventory();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.performCommand("chainedhearts settings");
        }, 1L);
    }
    
    private void toggleSharedEffects(Player player) {
        boolean currentState = plugin.getConfigManager().isSharedEffectsEnabled();
        plugin.getConfigManager().setSharedEffectsEnabled(!currentState);
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            !currentState ? "&aShared effects have been enabled!" : "&cShared effects have been disabled!"));
        
        player.closeInventory();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.performCommand("chainedhearts settings");
        }, 1L);
    }
    
    private void toggleActionbarMessages(Player player) {
        boolean currentState = plugin.getConfigManager().isActionbarMessagesEnabled();
        plugin.getConfigManager().setActionbarMessagesEnabled(!currentState);
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            !currentState ? "&aAction bar messages have been enabled!" : "&cAction bar messages have been disabled!"));
        
        player.closeInventory();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.performCommand("chainedhearts settings");
        }, 1L);
    }
    
    private void toggleOneHeartMode(Player player) {
        boolean currentState = plugin.getConfigManager().isOneHeartModeEnabled();
        boolean newState = !currentState;
        plugin.getConfigManager().setOneHeartModeEnabled(newState);
        
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            OneHeartListener.setMaxHealth(onlinePlayer, newState ? 2.0 : 20.0);
        }
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            newState ? "&aOne heart mode has been enabled!" : "&cOne heart mode has been disabled!"));
        
        player.closeInventory();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.performCommand("chainedhearts settings");
        }, 1L);
    }
}
