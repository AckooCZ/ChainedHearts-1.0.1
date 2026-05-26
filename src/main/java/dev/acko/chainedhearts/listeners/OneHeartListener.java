package dev.acko.chainedhearts.listeners;

import dev.acko.chainedhearts.ChainedHearts;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OneHeartListener implements Listener {
    
    private final ChainedHearts plugin;
    
    public OneHeartListener(ChainedHearts plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        applyOneHeartMode(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            applyOneHeartMode(event.getPlayer());
        }, 1L);
    }
    
    private void applyOneHeartMode(Player player) {
        if (!plugin.isChainedEnabled() || !plugin.getConfigManager().isOneHeartModeEnabled()) {
            return;
        }
        
        setMaxHealth(player, 2.0);
    }
    
    public static void setMaxHealth(Player player, double maxHealth) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) {
            return;
        }
        
        attribute.setBaseValue(maxHealth);
        if (player.getHealth() > maxHealth) {
            player.setHealth(maxHealth);
        }
    }
}
