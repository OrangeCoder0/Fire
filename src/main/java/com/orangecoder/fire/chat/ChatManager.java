package com.orangecoder.fire.chat;

import org.bukkit.entity.Player;
import com.orangecoder.fire.game.Game;

import java.util.*;

public class ChatManager {

    private Map<Player, Long> playerCooldowns = new HashMap<>();
    private static final long CHAT_COOLDOWN_MS = 500; // 500ms between messages

    public boolean canChat(Player player) {
        long lastChat = playerCooldowns.getOrDefault(player, 0L);
        long timeSinceLastChat = System.currentTimeMillis() - lastChat;
        return timeSinceLastChat >= CHAT_COOLDOWN_MS;
    }

    public void recordChatMessage(Player player) {
        playerCooldowns.put(player, System.currentTimeMillis());
    }

    public void sendGameMessage(Game game, String message) {
        for (Player player : game.getPlayers()) {
            player.sendMessage(message);
        }
        for (Player spectator : game.getSpectators()) {
            spectator.sendMessage(message);
        }
    }

    public void sendPlayerMessage(Game game, Player sender, String message) {
        String formatted = "§e" + sender.getName() + "§f: " + message;
        sendGameMessage(game, formatted);
    }

    public void sendTeamMessage(Player sender, String message) {
        String formatted = "§a[TEAM] " + sender.getName() + "§f: " + message;
        sender.sendMessage(formatted);
    }

    public void clearCooldowns() {
        playerCooldowns.clear();
    }
}
