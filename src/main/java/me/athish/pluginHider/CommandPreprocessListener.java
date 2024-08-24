package me.athish.pluginHider;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Map;

public class CommandPreprocessListener implements Listener {

    private final PluginHider plugin;

    public CommandPreprocessListener(PluginHider plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String command = message.split(" ")[0].substring(1); // Extract command without the leading '/'

        String group = getHighestPriorityGroup(player);
        List<String> allowedCommands = plugin.groupCommandsMap.get(group);

        if (command.equalsIgnoreCase("op")) {
            if (plugin.opProtectionEnabled && !plugin.opWhitelist.contains(player.getName())) {
                player.sendMessage("You are not allowed to use this command.");
                event.setCancelled(true);
                return;
            }
        }

        if (allowedCommands == null || !allowedCommands.contains(command)) {
            player.sendMessage("You do not have permission to use this command.");
            event.setCancelled(true);
        }
    }

    private String getHighestPriorityGroup(Player player) {
        List<String> groupPriority = plugin.getConfig().getStringList("group-priority");
        for (String group : groupPriority) {
            if (player.hasPermission("itomsd.pluginhider." + group)) {
                return group;
            }
        }
        return "default"; // Fallback to default group if no other group is matched
    }
}