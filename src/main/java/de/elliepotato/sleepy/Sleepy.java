package de.elliepotato.sleepy;

import de.elliepotato.sleepy.conf.ConfigHandle;
import de.elliepotato.sleepy.version.VersionChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Ellie :: 24/07/2019
 */
public class Sleepy extends JavaPlugin {

    // Dev mode
    private static final boolean DEV_OUT = false;
    // Constants
    public static long DAY_MIN = 0;
    public static long DAY_MAX = 12010;
    // Command perm
    private String permissionReload = "sleepy.reload";

    private ConfigHandle configHandle;
    private SleepWatcher sleepWatcher;

    private String updateMeMessage;

    public static void debug(String message) {
        if (DEV_OUT)
            System.out.println("[Sleepy Debug] " + message);
    }

    @Override
    public void onEnable() {

        debug("This is a development build, not for release.");

        debug("Init config handle.");
        this.configHandle = new ConfigHandle(this);

        debug("Init sleep watcher");
        this.sleepWatcher = new SleepWatcher(this);

        debug("Init command sleepy");
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

        debug("Init Metrics.");
        new Metrics(this);

        if (configHandle.getConfigDataHolder().isDoVersionCheck()) {
            debug("Do version check");
            new VersionChecker(this);

            // Notifier
            getServer().getPluginManager().registerEvents(new Listener() {

                @EventHandler
                public void on(PlayerJoinEvent e) {
                    final Player player = e.getPlayer();

                    if (!player.hasPermission(permissionReload) || updateMeMessage == null) return;

                    player.sendMessage(ChatColor.RED + "[Sleepy] " + updateMeMessage);
                }

            }, this);

        }

        logInfo("Sleepy, version " + getDescription().getVersion() + ", is loaded.");
    }

    @Override
    public void onDisable() {

        logInfo("Sleepy, version " + getDescription().getVersion() + ", is disabled.");
    }

    public String getUpdateMeMessage() {
        return updateMeMessage;
    }

    public void setUpdateMeMessage(String updateMeMessage) {
        this.updateMeMessage = updateMeMessage + " You can download a new version at https://www.spigotmc.org/resources/sleepy.69678/";
    }

    public void logInfo(String info) {
        getLogger().info(info);
    }

    public void logWarn(String warn) {
        getLogger().warning(warn);
    }

    public void logError(String error) {
        getLogger().severe(error);
    }

    public ConfigHandle getConfigHandle() {
        return configHandle;
    }

}
