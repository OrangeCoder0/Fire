package com.orangecoder.fire.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.orangecoder.fire.Fire;
import com.orangecoder.fire.game.Game;
import com.orangecoder.fire.managers.GameManager;
import com.orangecoder.fire.chat.ChatManager;

public class ChatCommand implements CommandExecutor {

    private Fire plugin;
    private GameManager gameManager;
    private ChatManager chatManager;

    public ChatCommand(Fire plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.chatManager = new ChatManager();

        plugin.getCommand("gc").setExecutor(this);
        plugin.getCommand("gamechat").setExecutor(this);
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        Game game = gameManager.getPlayerGame(player);

        if (game == null) {
            player.sendMessage("§c✗ You must be in a game to chat!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cUsage: /gc <message>");
            return true;
        }

        // Check cooldown
        if (!chatManager.canChat(player)) {
            player.sendMessage("§c✗ Slow down! Please wait before sending another message.");
            return true;
        }

        // Build message
        StringBuilder messageBuilder = new StringBuilder();
        for (String arg : args) {
            messageBuilder.append(arg).append(" ");
        }
        String message = messageBuilder.toString().trim();

        // Limit message length
        if (message.length() > 100) {
            message = message.substring(0, 100);
        }

        // Send message to all in game
        chatManager.recordChatMessage(player);
        chatManager.sendPlayerMessage(game, player, message);

        return true;
    }
}
