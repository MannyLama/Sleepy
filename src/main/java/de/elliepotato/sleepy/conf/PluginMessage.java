package de.elliepotato.sleepy.conf;

/**
 * @author Ellie :: 24/07/2019
 */
public enum PluginMessage {

    PLAYER_SLEEPING("playerNowSleeping"),
    PLAYER_STOP_SLEEPING("playerNoLongerSleeping"),

    SKIP_NEEDED("toSkipNeeded"),
    ENOUGH_TO_SKIP("toSkipSatisfied"),
    POST_SKIP("afterNightSkipped");

    private String key;

    PluginMessage (String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
