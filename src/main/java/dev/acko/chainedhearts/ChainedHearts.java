package dev.acko.chainedhearts;

import dev.acko.chainedhearts.commands.ChainedHeartsCommand;
import dev.acko.chainedhearts.listeners.DamageListener;
import dev.acko.chainedhearts.listeners.HungerListener;
import dev.acko.chainedhearts.listeners.OneHeartListener;
import dev.acko.chainedhearts.listeners.PotionEffectListener;
import dev.acko.chainedhearts.listeners.SettingsListener;
import dev.acko.chainedhearts.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChainedHearts extends JavaPlugin {
    
    private boolean chainedEnabled = true;
    private ConfigManager configManager;
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        configManager = new ConfigManager(this);
        chainedEnabled = getConfig().getBoolean("enabled", true);
        
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new HungerListener(this), this);
        getServer().getPluginManager().registerEvents(new OneHeartListener(this), this);
        getServer().getPluginManager().registerEvents(new PotionEffectListener(this), this);
        getServer().getPluginManager().registerEvents(new SettingsListener(this), this);
        getCommand("chainedhearts").setExecutor(new ChainedHeartsCommand(this));
        
        getLogger().info(configManager.getMessage("plugin.enabled"));
    }
    
    @Override
    public void onDisable() {
        getLogger().info(configManager.getMessage("plugin.disabled"));
    }
    
    public boolean isChainedEnabled() {
        return chainedEnabled;
    }
    
    public void setChainedEnabled(boolean enabled) {
        this.chainedEnabled = enabled;
        getConfig().set("enabled", enabled);
        saveConfig();
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
