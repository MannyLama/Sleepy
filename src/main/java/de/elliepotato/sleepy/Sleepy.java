package de.elliepotato.sleepy;

import de.elliepotato.sleepy.conf.ConfigHandle;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Ellie :: 24/07/2019
 */
public class Sleepy extends JavaPlugin {

    public static long DAY_MIN = 0;
    public static long DAY_MAX = 12010;

    private String permissionReload = "sleepy.reload";

    private ConfigHandle configHandle;
    private SleepWatcher sleepWatcher;

    @Override
    public void onEnable() {

        this.configHandle = new ConfigHandle (this);
        this.sleepWatcher = new SleepWatcher (this);

        getCommand("sleepy").setExecutor((sender, command, label, args) -> {

            if (args.length > 0 && sender.hasPermission(permissionReload)) {

                if ("reload".equals(args[0].toLowerCase())) {
                    reloadConfig();

                    configHandle.reload();
                    sleepWatcher.setConfigDataHolder(configHandle.getConfigDataHolder());

                    sender.sendMessage(ChatColor.RED + "Plugin reloaded.");
                }

                return true;
            }

            sender.sendMessage(ChatColor.RED + "Sleepy version " + getDescription().getVersion() + " by Ellie#0006");

            if (sender.hasPermission(permissionReload))
                sender.sendMessage(ChatColor.RED + "/sleepy reload - " + ChatColor.GRAY + "Reload the config + plugin.");

            return true;
        });

        new Metrics(this);

        logInfo ("Sleepy, version " + getDescription().getVersion() + ", is loaded.");
    }

    @Override
    public void onDisable() {

        logInfo ("Sleepy, version " + getDescription().getVersion() + ", is disabled.");
    }

    public void logInfo (String info) {
        getLogger().info(info);
    }

    public void logWarn (String warn) {
        getLogger().warning(warn);
    }

    public void logError (String error) {
        getLogger().severe(error);
    }

    public ConfigHandle getConfigHandle() {
        return configHandle;
    }

}
