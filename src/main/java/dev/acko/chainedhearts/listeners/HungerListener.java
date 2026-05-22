package dev.acko.chainedhearts.listeners;

import dev.acko.chainedhearts.ChainedHearts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HungerListener implements Listener {
    
    private final ChainedHearts plugin;
    private final Set<UUID> processingPlayers = new HashSet<>();
    
    public HungerListener(ChainedHearts plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!plugin.isChainedEnabled() || !plugin.getConfigManager().isSharedHungerEnabled()) {
            return;
        }
        
        if (!(event.getEntity() instanceof Player changedPlayer)) {
            return;
        }
        
        UUID playerId = changedPlayer.getUniqueId();
        
        if (processingPlayers.contains(playerId)) {
            return;
        }
        
        if (shouldExcludePlayer(changedPlayer)) {
            return;
        }
        
        int newFoodLevel = event.getFoodLevel();
        
        processingPlayers.add(playerId);
        
        try {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.equals(changedPlayer) || shouldExcludePlayer(player)) {
                    continue;
                }
                
                if (player.isDead()) {
                    continue;
                }
                
                player.setFoodLevel(newFoodLevel);
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
}
