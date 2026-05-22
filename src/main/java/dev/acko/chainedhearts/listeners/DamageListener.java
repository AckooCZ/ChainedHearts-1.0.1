package dev.acko.chainedhearts.listeners;

import dev.acko.chainedhearts.ChainedHearts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DamageListener implements Listener {
    
    private final ChainedHearts plugin;
    private final Set<UUID> processingPlayers = new HashSet<>();
    
    public DamageListener(ChainedHearts plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!plugin.isChainedEnabled() || !plugin.getConfigManager().isSharedDamageEnabled()) {
            return;
        }
        
        if (event.isCancelled() || event.getDamage() <= 0) {
            return;
        }
        
        if (!(event.getEntity() instanceof Player damagedPlayer)) {
            return;
        }
        
        if (shouldExcludePlayer(damagedPlayer)) {
            return;
        }
        
        if (damagedPlayer.isBlocking()) {
            return;
        }
        
        double finalDamage = event.getFinalDamage();
        double damagedPlayerHealth = damagedPlayer.getHealth() - finalDamage;
        
        damagedPlayerHealth = Math.max(0, damagedPlayerHealth);
        
        UUID playerId = damagedPlayer.getUniqueId();
        if (processingPlayers.contains(playerId)) {
            return;
        }
        
        processingPlayers.add(playerId);
        
        try {
            for (org.bukkit.entity.Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.equals(damagedPlayer)) {
                    continue; 
                }
                
                if (shouldExcludePlayer(player)) {
                    continue;
                }
                
                if (player.isDead() || player.getHealth() <= 0) {
                    continue;
                }
                
                player.setHealth(damagedPlayerHealth);
                
                if (plugin.getConfigManager().isDamageMessagesEnabled()) {
                    sendDamageMessages(player, damagedPlayer, event.getDamage());
                }
            }
        } finally {
            processingPlayers.remove(playerId);
        }
    }
    
    private boolean shouldExcludePlayer(Player player) {
        if (plugin.getConfigManager().shouldExcludeCreative() && player.hasPermission("chainedhearts.exclude.creative")) {
            return true;
        }
        
        if (plugin.getConfigManager().shouldExcludeSpectator() && player.hasPermission("chainedhearts.exclude.spectator")) {
            return true;
        }
        
        return player.hasPermission("chainedhearts.exclude");
    }
    
    private void sendDamageMessages(Player receiver, Player damaged, double damage) {
        String damageStr = formatDamage(damage);
        String formatStyle = plugin.getConfigManager().getActionbarFormatStyle();
        
        if (plugin.getConfigManager().isDamagedPlayerMessagesEnabled() && receiver.equals(damaged)) {
            if (plugin.getConfigManager().isChatMessagesEnabled()) {
                receiver.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', 
                    "&cYou took damage: " + damageStr + " HP!"));
            }
            if (plugin.getConfigManager().isActionbarMessagesEnabled()) {
                String actionbarMessage = formatActionbarMessage(damageStr, damaged.getName(), formatStyle, true);
                receiver.sendActionBar(org.bukkit.ChatColor.translateAlternateColorCodes('&', actionbarMessage));
            }
        }
        
        if (plugin.getConfigManager().isOtherPlayersMessagesEnabled() && !receiver.equals(damaged)) {
            if (plugin.getConfigManager().isChatMessagesEnabled()) {
                receiver.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', 
                    "&6" + damaged.getName() + " took damage: " + damageStr + " HP!"));
            }
            if (plugin.getConfigManager().isActionbarMessagesEnabled()) {
                String actionbarMessage = formatActionbarMessage(damageStr, damaged.getName(), formatStyle, false);
                receiver.sendActionBar(org.bukkit.ChatColor.translateAlternateColorCodes('&', actionbarMessage));
            }
        }
        

        if (plugin.getConfigManager().isAllPlayersMessagesEnabled()) {
            for (org.bukkit.entity.Player allPlayer : plugin.getServer().getOnlinePlayers()) {
                if (plugin.getConfigManager().isChatMessagesEnabled()) {
                    allPlayer.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', 
                        "&e" + damaged.getName() + " took damage: " + damageStr + " HP!"));
                }
            }
        }
    }
    
    private String formatDamage(double damage) {
        if (damage == Math.floor(damage)) {
            return String.format("%.0f", damage); 
        } else {
            return String.format("%.1f", damage); 
        }
    }
    
    private String formatActionbarMessage(String damageStr, String playerName, String formatStyle, boolean isDamagedPlayer) {
        return switch (formatStyle.toLowerCase()) {
            case "detailed" -> isDamagedPlayer ? 
                "&cYou took " + damageStr + " HP damage!" : 
                "&6" + playerName + " took " + damageStr + " HP damage!";
            case "emoji" -> isDamagedPlayer ? 
                "&c-" + damageStr + " HP" : 
                "&6" + playerName + ": -" + damageStr + " HP";
            default -> 
                isDamagedPlayer ? 
                    "&c-" + damageStr + " HP" : 
                    "&6" + playerName + ": -" + damageStr + " HP";
        };
    }
}
