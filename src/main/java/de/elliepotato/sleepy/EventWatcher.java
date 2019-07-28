package de.elliepotato.sleepy;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Set;

/**
 * @author Ellie :: 24/07/2019
 */
public class EventWatcher implements Listener {

    private SleepWatcher watcher;

    public EventWatcher(SleepWatcher sleepWatcher) {
        this.watcher = sleepWatcher;
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        if (watcher.getConfigDataHolder().getDisabledWorlds().contains(e.getPlayer().getWorld().getName())) return;

        Sleepy.debug("Player joined into non-disabled world, " + e.getPlayer().getWorld().getName());

        watcher.processPlayerWorldConnectionEvent(e.getPlayer().getWorld());
    }


    @EventHandler
    public void on(PlayerQuitEvent e) {
        if (watcher.getConfigDataHolder().getDisabledWorlds().contains(e.getPlayer().getWorld().getName())) return;

        Sleepy.debug("Player quit from non-disabled world, " + e.getPlayer().getWorld().getName());

        watcher.processPlayerWorldConnectionEvent(e.getPlayer().getWorld());
    }

    @EventHandler
    public void on(PlayerChangedWorldEvent e) {
        final Set<String> disabledWorlds = watcher.getConfigDataHolder().getDisabledWorlds();

        if (!disabledWorlds.contains(e.getFrom().getName()))
            watcher.processPlayerWorldConnectionEvent(e.getFrom());


        if (!disabledWorlds.contains(e.getPlayer().getWorld().getName())) {
            watcher.processPlayerWorldConnectionEvent(e.getPlayer().getWorld());
        }

    }

    @EventHandler
    public void on(PlayerBedEnterEvent e) {

        final PlayerBedEnterEvent.BedEnterResult bedEnterResult = e.getBedEnterResult();

        if (bedEnterResult != PlayerBedEnterEvent.BedEnterResult.OK) return;

        final Player player = e.getPlayer();

        if (watcher.getConfigDataHolder().getDisabledWorlds().contains(player.getWorld().getName())) return;

        watcher.processSleeperEvent(player, true);
    }

    @EventHandler
    public void on(PlayerBedLeaveEvent e) {

        final Player player = e.getPlayer();

        if (watcher.getConfigDataHolder().getDisabledWorlds().contains(player.getWorld().getName())) return;

        // If player time GREATER THAN 0 or time LESS THAN 12010
        if (player.getWorld().getTime() >= Sleepy.DAY_MIN && player.getWorld().getTime() < Sleepy.DAY_MAX) {
            watcher.presumeNightOverEvent(player);
        } else watcher.processSleeperEvent(player, false);

    }

}
