package de.elliepotato.sleepy;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Ellie :: 25/07/2019
 */
public class WorldData {

    private int currentlyRequiredSleepers;
    private Set<Player> sleepers;

    private boolean skippingNight;
    private int reminderTask = -1;

    public WorldData () {
        this.sleepers = Sets.newHashSet ();
    }

    public Set<Player> getSleepers () {
        return sleepers;
    }

    public int getCurrentlyRequiredSleepers () {
        return currentlyRequiredSleepers;
    }

    public void setCurrentlyRequiredSleepers (int currentlyRequiredSleepers) {
        this.currentlyRequiredSleepers = currentlyRequiredSleepers;
    }

    public boolean isSkippingNight() {
        return skippingNight;
    }

    public void setSkippingNight(boolean skippingNight) {
        this.skippingNight = skippingNight;
    }

    public void nightOver () {
        sleepers.clear();

        cancelTask();

    }

    public void setReminderTask(int reminderTask) {
        this.reminderTask = reminderTask;
    }

    public void cancelTask () {
        if (reminderTask != -1) {
            Bukkit.getScheduler().cancelTask(reminderTask);
            reminderTask = -1;
        }
    }

}
