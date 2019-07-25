package de.elliepotato.sleepy;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.elliepotato.sleepy.conf.ConfigDataHolder;
import de.elliepotato.sleepy.conf.CustomMessage;
import de.elliepotato.sleepy.conf.PluginMessage;
import de.elliepotato.sleepy.util.NumberTools;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Optional;

/**
 * @author Ellie :: 24/07/2019
 */
public class SleepWatcher implements Listener  {

    private Sleepy sleepy;
    private ConfigDataHolder configDataHolder;

    private Map <World, WorldData> worldData;

    public SleepWatcher (Sleepy sleepy) {
        this.sleepy = sleepy;
        this.configDataHolder = sleepy.getConfigHandle().getConfigDataHolder();

        sleepy.getServer().getPluginManager().registerEvents(new EventWatcher(this), sleepy);

        this.worldData = Maps.newHashMap ();
    }

    public void processPlayerWorldConnectionEvent (World world) {

        if (!worldData.containsKey(world)) {
            createWorldData(world);
        }

        WorldData worldData = this.worldData.get(world);

        switch (configDataHolder.getConditionType()) {
            case FIXED_VALUE:
                 worldData.setCurrentlyRequiredSleepers(configDataHolder.getSleepingMinFixed());
                break;
            case ALL:
                worldData.setCurrentlyRequiredSleepers(world.getPlayers().size());
                break;
            case HALF:
                worldData.setCurrentlyRequiredSleepers((int) NumberTools.halfFloor(world.getPlayers().size()));
                break;
        }

    }

    public void processSleeperEvent (Player player, boolean inBed) {

        if (!worldData.containsKey(player.getWorld())) {
            createWorldData(player.getWorld());
        }

        final WorldData worldData = this.worldData.get(player.getWorld());

        if (inBed) worldData.getSleepers().add(player);
        else worldData.getSleepers().remove(player);

        configDataHolder.getMessage(inBed ? PluginMessage.PLAYER_SLEEPING : PluginMessage.PLAYER_STOP_SLEEPING).ifPresent(message ->
                        message.getMessage().ifPresent(s -> player.getWorld().getPlayers().forEach(p ->
                                p.spigot().sendMessage(message.getChatMessageType(), TextComponent.fromLegacyText(message.applyPlaceholders(player))))));

        if (worldData.isSkippingNight()) return;

        if (worldData.getSleepers().size() >= worldData.getCurrentlyRequiredSleepers()) {

            worldData.setSkippingNight(true);
            startTask(player.getWorld(), worldData, 0);

            Bukkit.getScheduler().scheduleSyncDelayedTask(sleepy, () -> {
                player.getWorld().setTime(configDataHolder.getTimeNewDay());
                worldData.setSkippingNight(false);

                worldData.cancelTask();
            }, configDataHolder.getNewDayDelay());

            return;
        }

        // Cancel all.
        if (worldData.getSleepers().size() == 0) {
            worldData.cancelTask();
            return;
        }

        int need = Math.max(0, worldData.getCurrentlyRequiredSleepers() - worldData.getSleepers().size());

        startTask(player.getWorld(), worldData, need);
    }

    public void presumeNightOverEvent (Player player) {

        if (worldData.containsKey(player.getWorld())) {
            final WorldData worldData = this.worldData.get(player.getWorld());
            worldData.getSleepers().clear();
            worldData.nightOver();
        }

        configDataHolder.getMessage(PluginMessage.POST_SKIP).ifPresent(message ->
                message.getMessage().ifPresent(s -> player.getWorld().getPlayers().forEach(p ->
                        p.spigot().sendMessage(message.getChatMessageType(), TextComponent.fromLegacyText(message.applyPlaceholders(player))))));
    }

    public ConfigDataHolder getConfigDataHolder () {
        return configDataHolder;
    }

    public void setConfigDataHolder (ConfigDataHolder configDataHolder) {
        this.configDataHolder = configDataHolder;
    }

    public Map<World, WorldData> getWorldData () {
        return worldData;
    }

    public void startTask (World world, WorldData worldData, int need) {
        worldData.cancelTask();

        final Optional<CustomMessage> message = configDataHolder.getMessage(need == 0 ? PluginMessage.ENOUGH_TO_SKIP : PluginMessage.SKIP_NEEDED);
        if (!message.isPresent() || !message.get().getMessage().isPresent()) {
            return;
        }

        final String s = message.get().getRawMessage().replace("%value%", String.valueOf(need));

        worldData.setReminderTask(Bukkit.getScheduler().runTaskTimer(sleepy, () ->
                world.getPlayers().forEach(o -> o.spigot().sendMessage(message.get().getChatMessageType(), TextComponent.fromLegacyText(s))),
                0L, 20L).getTaskId());
    }

    private void createWorldData (World world) {
        if (!worldData.containsKey(world))
            worldData.put(world, new WorldData());
    }

}
