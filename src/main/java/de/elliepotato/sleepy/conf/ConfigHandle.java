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

    public ConfigHandle(Sleepy sleepy) {
        this.sleepy = sleepy;

        reload();
    }

    public void reload() {
        sleepy.saveDefaultConfig();

        if (configDataHolder != null) {
            Sleepy.debug("Config holder != null, presuming reload. Cleaning up.");
            configDataHolder.getMessages().clear();
        }

        this.configDataHolder = null;
        Sleepy.debug("init conf data holder");
        this.configDataHolder = new ConfigDataHolder();

        // Since 1.2
        if (sleepy.getConfig().get("check-version") == null) {
            sleepy.logInfo("A new config value has spawned! 'check-version', when set to true (by default) it will check the update every restart.");
            sleepy.getConfig().set("check-version", true);
            sleepy.saveConfig();
        }

        this.configDataHolder.setDoVersionCheck(sleepy.getConfig().getBoolean("check-version"));

        // Set values.
        Sleepy.debug("Data validation");
        checkIllegalInput(sleepy, () -> this.configDataHolder.setConditionTypeSerialized(sleepy.getConfig().getString("required-sleeping")));
        checkIllegalInput(sleepy, () -> this.configDataHolder.setNewDayDelay(sleepy.getConfig().getString("new-day-delay")));
        checkIllegalInput(sleepy, () -> this.configDataHolder.setTimeNewDay(sleepy.getConfig().getString("new-day")));

        configDataHolder.setDisabledWorlds(Sets.newHashSet());
        sleepy.getConfig().getStringList("disabled-worlds").forEach(s -> configDataHolder.getDisabledWorlds().add(s));
        Sleepy.debug("Disabled worlds: " + sleepy.getConfig().getStringList("disabled-worlds"));

        configDataHolder.setMessages(Maps.newHashMap());

        for (String key : sleepy.getConfig().getConfigurationSection("messages").getKeys(false)) {
            String messageString = sleepy.getConfig().getString("messages." + key + ".message");
            Sleepy.debug("Looking for msg data : '" + key + "'");
            if (StringUtils.isEmpty(messageString)) continue;

            try {
                configDataHolder.getMessages().put(key, new CustomMessage(ChatColor.translateAlternateColorCodes('&', messageString),
                        sleepy.getConfig().getString("messages." + key + ".show")));
                Sleepy.debug(key + " : " + messageString + " : " + sleepy.getConfig().getString("messages." + key + ".show"));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

    }

    public ConfigDataHolder getConfigDataHolder() {
        return configDataHolder;
    }

    private void checkIllegalInput(Sleepy sleepy, Runnable runnable) {
        try {
            runnable.run();
        } catch (IllegalArgumentException e) {
            sleepy.logWarn(e.getMessage());
        }
    }

}
