package de.elliepotato.sleepy.conf;

import de.elliepotato.sleepy.Sleepy;
import de.elliepotato.sleepy.util.NumberTools;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Ellie :: 24/07/2019
 */
public class ConfigDataHolder {

    public enum ConditionType { FIXED_VALUE, HALF, ALL }

    private ConditionType conditionType;
    private int sleepingMinFixed;

    private int newDayDelay, timeNewDay;

    private Set<String> disabledWorlds;

    private Map<String, CustomMessage> messages;

    public ConditionType getConditionType () {
        return conditionType;
    }

    public void setConditionType (ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public void setConditionTypeSerialized (String conditionTypeSerialized)
            throws IllegalArgumentException {
        Optional<Integer> fixedValue = NumberTools.tryParse(conditionTypeSerialized);

        if (fixedValue.isPresent()) {
            conditionType = ConditionType.FIXED_VALUE;
            sleepingMinFixed = fixedValue.get();
        } else {

            final Optional<ConditionType> conditionType = Arrays.stream(ConditionType.values()).filter(value -> value != ConditionType.FIXED_VALUE
                    || !value.toString().equals(conditionTypeSerialized.toUpperCase()))
                    .findFirst();

            conditionType.ifPresent(type -> this.conditionType = type);

            if (!conditionType.isPresent()) {
                this.conditionType = ConditionType.HALF;
                throw new IllegalArgumentException (conditionTypeSerialized + " is not a valid condition type");
            }

        }

    }

    public int getSleepingMinFixed () {
        return sleepingMinFixed;
    }

    public void setSleepingMinFixed (int sleepingMinFixed) {
        this.sleepingMinFixed = sleepingMinFixed;
    }

    public int getNewDayDelay () {
        return newDayDelay;
    }

    public void setNewDayDelay (int newDayDelay) {
        this.newDayDelay = newDayDelay;
    }

    public void setNewDayDelay (String newDayDelaySerialized)
        throws IllegalArgumentException {

        Optional<Integer> delay = NumberTools.tryParse(newDayDelaySerialized);
        delay.ifPresent(value -> this.newDayDelay = value);

        if (!delay.isPresent()) {
            this.newDayDelay = 20;
            throw new IllegalArgumentException (newDayDelaySerialized + " is not a valid delay");
        }

    }

    public int getTimeNewDay () {
        return timeNewDay;
    }

    public void setTimeNewDay (int timeNewDay) {
        this.timeNewDay = timeNewDay;
    }

    public void setTimeNewDay (String timeNewDaySerialized)
            throws IllegalArgumentException {

        Optional<Integer> newDayTime = NumberTools.tryParse(timeNewDaySerialized);

        newDayTime.filter(value -> value < Sleepy.DAY_MIN || value > Sleepy.DAY_MAX).ifPresent(value -> this.timeNewDay = value);

        if (!newDayTime.isPresent()) {
            this.timeNewDay = 1000;
            throw new IllegalArgumentException (timeNewDaySerialized + " is not a valid time, see config for guidance");
        }

    }

    public Set<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    public void setDisabledWorlds(Set<String> disabledWorlds) {
        this.disabledWorlds = disabledWorlds;
    }

    public Map<String, CustomMessage> getMessages () {
        return messages;
    }

    public Optional<CustomMessage> getMessage (PluginMessage message) {
        if (messages.containsKey(message.getKey())) {
            return Optional.of(messages.get(message.getKey()));
        }

        return Optional.empty();
    }

    public void setMessages(Map<String, CustomMessage> messages) {
        this.messages = messages;
    }

}
