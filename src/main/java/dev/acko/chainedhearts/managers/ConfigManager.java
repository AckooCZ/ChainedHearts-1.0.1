package dev.acko.chainedhearts.managers;

import dev.acko.chainedhearts.ChainedHearts;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {
    
    private final ChainedHearts plugin;
    private FileConfiguration menuConfig;
    
    public ConfigManager(ChainedHearts plugin) {
        this.plugin = plugin;
        loadMenuConfig();
    }
    
    private void loadMenuConfig() {
        File menuFile = new File(plugin.getDataFolder(), "menu.yml");
        if (!menuFile.exists()) {
            plugin.saveResource("menu.yml", false);
        }
        menuConfig = YamlConfiguration.loadConfiguration(menuFile);
    }
    
    public void reloadMenuConfig() {
        loadMenuConfig();
    }
    
    public FileConfiguration getMenuConfig() {
        return menuConfig;
    }
    
    public String getMessage(String path) {
        String message = plugin.getConfig().getString("messages." + path, "Message not found: " + path);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public boolean isDamageMultiplierEnabled() {
        return plugin.getConfig().getBoolean("damage.multiplier-enabled", true);
    }
    
    public double getDamageMultiplier() {
        return plugin.getConfig().getDouble("damage.multiplier", 1.0);
    }
    
    public boolean shouldBroadcastDamage() {
        return plugin.getConfig().getBoolean("damage.broadcast-damage", true);
    }
    
    public boolean shouldExcludeCreative() {
        return plugin.getConfig().getBoolean("damage.exclude-creative", true);
    }
    
    public boolean shouldExcludeSpectator() {
        return plugin.getConfig().getBoolean("damage.exclude-spectator", true);
    }
    
    public boolean isSharedHpEnabled() {
        return plugin.getConfig().getBoolean("features.shared_hp.enabled", true);
    }
    
    public void setSharedHpEnabled(boolean enabled) {
        plugin.getConfig().set("features.shared_hp.enabled", enabled);
        plugin.saveConfig();
    }
    
    public boolean isSharedDamageEnabled() {
        return plugin.getConfig().getBoolean("features.shared_damage.enabled", true);
    }
    
    public void setSharedDamageEnabled(boolean enabled) {
        plugin.getConfig().set("features.shared_damage.enabled", enabled);
        plugin.saveConfig();
    }
    
    public boolean isSharedHungerEnabled() {
        return plugin.getConfig().getBoolean("features.shared_hunger.enabled", true);
    }
    
    public void setSharedHungerEnabled(boolean enabled) {
        plugin.getConfig().set("features.shared_hunger.enabled", enabled);
        plugin.saveConfig();
    }
    
    public boolean isDamageMessagesEnabled() {
        return plugin.getConfig().getBoolean("features.damage_messages.enabled", true);
    }
    
    public void setDamageMessagesEnabled(boolean enabled) {
        plugin.getConfig().set("features.damage_messages.enabled", enabled);
        plugin.saveConfig();
    }
    
    public boolean isDamagedPlayerMessagesEnabled() {
        return plugin.getConfig().getBoolean("features.damage_messages.receivers.damaged_player", true);
    }
    
    public void setDamagedPlayerMessagesEnabled(boolean enabled) {
        plugin.getConfig().set("features.damage_messages.receivers.damaged_player", enabled);
        plugin.saveConfig();
    }
    
    public boolean isOtherPlayersMessagesEnabled() {
        return plugin.getConfig().getBoolean("features.damage_messages.receivers.other_players", true);
    }
    
    public void setOtherPlayersMessagesEnabled(boolean enabled) {
        plugin.getConfig().set("features.damage_messages.receivers.other_players", enabled);
        plugin.saveConfig();
    }
    
    public boolean isAllPlayersMessagesEnabled() {
        return plugin.getConfig().getBoolean("features.damage_messages.receivers.all_players", false);
    }
    
    public void setAllPlayersMessagesEnabled(boolean enabled) {
        plugin.getConfig().set("features.damage_messages.receivers.all_players", enabled);
        plugin.saveConfig();
    }
    
    public boolean isChatMessagesEnabled() {
        return plugin.getConfig().getBoolean("features.damage_messages.output.chat", true);
    }
    
    public void setChatMessagesEnabled(boolean enabled) {
        plugin.getConfig().set("features.damage_messages.output.chat", enabled);
        plugin.saveConfig();
    }
    
    public boolean isActionbarMessagesEnabled() {
        return plugin.getConfig().getBoolean("features.damage_messages.output.actionbar", true);
    }
    
    public void setActionbarMessagesEnabled(boolean enabled) {
        plugin.getConfig().set("features.damage_messages.output.actionbar", enabled);
        plugin.saveConfig();
    }
    
    public boolean isSharedEffectsEnabled() {
        return plugin.getConfig().getBoolean("features.shared_effects.enabled", true);
    }
    
    public void setSharedEffectsEnabled(boolean enabled) {
        plugin.getConfig().set("features.shared_effects.enabled", enabled);
        plugin.saveConfig();
    }
    
    public boolean isEffectShared(String effectType) {
        return plugin.getConfig().getBoolean("features.shared_effects.effects." + effectType.toLowerCase(), true);
    }
    
    public void setEffectShared(String effectType, boolean shared) {
        plugin.getConfig().set("features.shared_effects.effects." + effectType.toLowerCase(), shared);
        plugin.saveConfig();
    }
    
    public String getActionbarFormatStyle() {
        return plugin.getConfig().getString("features.damage_messages.formats.actionbar_format_style", "simple");
    }
    
    public void setActionbarFormatStyle(String style) {
        plugin.getConfig().set("features.damage_messages.formats.actionbar_format_style", style);
        plugin.saveConfig();
    }
    
    public String getDamagedPlayerActionbarFormat() {
        return plugin.getConfig().getString("features.damage_messages.formats.damaged_player_actionbar", "&c- %damage% HP");
    }
    
    public String getOtherPlayersActionbarFormat() {
        return plugin.getConfig().getString("features.damage_messages.formats.other_players_actionbar", "&6%player%: -%damage% HP");
    }
}
