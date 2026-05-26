package dev.acko.chainedhearts.listeners;

import dev.acko.chainedhearts.ChainedHearts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PotionEffectListener implements Listener {
    
    private final ChainedHearts plugin;
    private final Set<UUID> processingPlayers = new HashSet<>();
    
    public PotionEffectListener(ChainedHearts plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPotionEffectChange(EntityPotionEffectEvent event) {
        if (!plugin.isChainedEnabled() || !plugin.getConfigManager().isSharedEffectsEnabled()) {
            return;
        }
        
        if (!(event.getEntity() instanceof Player affectedPlayer)) {
            return;
        }
        
        if (shouldExcludePlayer(affectedPlayer)) {
            return;
        }
        
        UUID playerId = affectedPlayer.getUniqueId();
        if (processingPlayers.contains(playerId)) {
            return;
        }
        
        processingPlayers.add(playerId);
        
        try {
            handleEffectChange(affectedPlayer, event);
        } finally {
            processingPlayers.remove(playerId);
        }
    }
    
    private void handleEffectChange(Player affectedPlayer, EntityPotionEffectEvent event) {
        EntityPotionEffectEvent.Action action = event.getAction();
        PotionEffect newEffect = event.getNewEffect();
        PotionEffect oldEffect = event.getOldEffect();
        
        switch (action) {
            case ADDED -> {
                if (newEffect != null && isEffectShared(newEffect.getType())) {
                    applyEffectToOtherPlayers(affectedPlayer, newEffect);
                }
            }
            case REMOVED -> {
                if (oldEffect != null && isEffectShared(oldEffect.getType())) {
                    removeEffectFromOtherPlayers(affectedPlayer, oldEffect.getType());
                }
            }
            case CHANGED -> {
                if (newEffect != null && isEffectShared(newEffect.getType())) {
                    updateEffectForOtherPlayers(affectedPlayer, newEffect);
                }
            }
        }
    }
    
    private void applyEffectToOtherPlayers(Player sourcePlayer, PotionEffect effect) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.equals(sourcePlayer)) {
                continue;
            }
            
            if (shouldExcludePlayer(player)) {
                continue;
            }
            
            if (player.isDead()) {
                continue;
            }
            
            player.addPotionEffect(effect);
        }
    }
    
    private void removeEffectFromOtherPlayers(Player sourcePlayer, PotionEffectType effectType) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.equals(sourcePlayer)) {
                continue;
            }
            
            if (shouldExcludePlayer(player)) {
                continue;
            }
            
            if (player.isDead()) {
                continue;
            }
            
            player.removePotionEffect(effectType);
        }
    }
    
    private void updateEffectForOtherPlayers(Player sourcePlayer, PotionEffect newEffect) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.equals(sourcePlayer)) {
                continue;
            }
            
            if (shouldExcludePlayer(player)) {
                continue;
            }
            
            if (player.isDead()) {
                continue;
            }
            
            player.removePotionEffect(newEffect.getType());
            player.addPotionEffect(newEffect);
        }
    }
    
    private boolean isEffectShared(PotionEffectType effectType) {
        String effectName = effectType.getName().toLowerCase();
        return plugin.getConfigManager().isEffectShared(effectName);
    }
    
    private boolean shouldExcludePlayer(Player player) {
        if (player.hasPermission("chainedhearts.exclude")) {
            return true;
        }
        
        if (plugin.getConfigManager().shouldExcludeCreative() && player.hasPermission("chainedhearts.exclude.creative")) {
            return true;
        }
        
        if (plugin.getConfigManager().shouldExcludeSpectator() && player.hasPermission("chainedhearts.exclude.spectator")) {
            return true;
        }
        
        return false;
    }
}
