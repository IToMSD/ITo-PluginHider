package me.athish.pluginHider;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class PluginHider extends JavaPlugin implements CommandExecutor {

    boolean opProtectionEnabled;
    List<String> opWhitelist;
    public Set<String> groups;
    public Map<String, List<String>> groupCommandsMap = new HashMap<>();
    public FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        opProtectionEnabled = config.getBoolean("op-protection.enabled");
        opWhitelist = config.getStringList("op-protection.whitelist");

        groups = Objects.requireNonNull(config.getConfigurationSection("groups")).getKeys(false);
        for (String group : groups) {
            List<String> allowedCommands = config.getStringList("groups." + group + ".commands");
            groupCommandsMap.put(group, allowedCommands);
        }

        getServer().getPluginManager().registerEvents(new CommandPreprocessListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}