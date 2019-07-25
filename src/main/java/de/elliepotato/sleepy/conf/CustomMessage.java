package de.elliepotato.sleepy.conf;

import net.md_5.bungee.api.ChatMessageType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Ellie :: 25/07/2019
 */
public class CustomMessage {

    private String message;
    private ChatMessageType chatMessageType;

    public CustomMessage (String message, String serializedChatMessageType)
            throws IllegalArgumentException {

        this.message = message;
        try {
            this.chatMessageType = ChatMessageType.valueOf(serializedChatMessageType);
        } catch (IllegalArgumentException e) {
            this.chatMessageType = ChatMessageType.CHAT;
            throw new IllegalArgumentException(serializedChatMessageType + " is not a valid type for ChatMessageType, see config for guidance");
        }
    }

    public String applyPlaceholders (Player player) {
        if (player == null) return message;
        return message.replace("%player%", player.getName())
                .replace("%world%", player.getWorld().getName())
                .replace("%time%", String.valueOf(player.getWorld().getTime()));
    }

    public Optional<String> getMessage () {
        return StringUtils.isEmpty(message) ? Optional.empty() : Optional.of(message);
    }

    public String getRawMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public ChatMessageType getChatMessageType () {
        return chatMessageType;
    }

    public void setChatMessageType (ChatMessageType chatMessageType) {
        this.chatMessageType = chatMessageType;
    }

}
