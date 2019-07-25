package de.elliepotato.sleepy.conf;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.elliepotato.sleepy.Sleepy;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

/**
 * @author Ellie :: 24/07/2019
 */
public class ConfigHandle {

    private Sleepy sleepy;
    private ConfigDataHolder configDataHolder;

    public ConfigHandle (Sleepy sleepy) {
        this.sleepy = sleepy;

        reload();
    }

    public void reload () {
        sleepy.saveDefaultConfig ();

        if (configDataHolder != null) configDataHolder.getMessages().clear();

        this.configDataHolder = null;
        this.configDataHolder = new ConfigDataHolder ();

        // Set values.
        checkIllegalInput(sleepy, () -> this.configDataHolder.setConditionTypeSerialized(sleepy.getConfig().getString("required-sleeping")));
        checkIllegalInput(sleepy, () -> this.configDataHolder.setNewDayDelay(sleepy.getConfig().getString("new-day-delay")));
        checkIllegalInput(sleepy, () -> this.configDataHolder.setTimeNewDay(sleepy.getConfig().getString("new-day")));

        configDataHolder.setDisabledWorlds(Sets.newHashSet());
        sleepy.getConfig().getStringList("disabled-worlds").forEach(s -> configDataHolder.getDisabledWorlds().add(s));

        configDataHolder.setMessages(Maps.newHashMap());

        for (String key : sleepy.getConfig().getConfigurationSection("messages").getKeys(false)) {
            String messageString = sleepy.getConfig().getString("messages." + key + ".message");
            if (StringUtils.isEmpty(messageString)) continue;

            try {
                configDataHolder.getMessages().put(key, new CustomMessage(ChatColor.translateAlternateColorCodes('&', messageString),
                        sleepy.getConfig().getString("messages." + key + ".show")));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

    }

    public ConfigDataHolder getConfigDataHolder () {
        return configDataHolder;
    }

    private void checkIllegalInput (Sleepy sleepy, Runnable runnable) {
        try {
            runnable.run();
        } catch (IllegalArgumentException e) {
            sleepy.logWarn(e.getMessage());
        }
    }

}
