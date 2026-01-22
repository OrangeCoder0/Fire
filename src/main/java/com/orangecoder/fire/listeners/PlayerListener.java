package com.orangecoder.fire.listeners;

import com.orangecoder.fire.Fire;
import com.orangecoder.fire.game.Game;
import com.orangecoder.fire.managers.GameManager;
import com.orangecoder.fire.utils.GameUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.Material;

public class PlayerListener implements Listener {

    private Fire plugin;
    private GameManager gameManager;

    public PlayerListener(Fire plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("§a§l=== Welcome to Fireball Wars ===");
        player.sendMessage("§eUse §c/fireballwars §eto get started!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Game game = gameManager.getPlayerGame(player);

        if (game != null) {
            // Remove player from game
            game.removePlayer(player);
            gameManager.playerGameMap.remove(player);

            // Check win condition
            if (game.getState() == Game.GameState.PLAYING) {
                if (game.getPlayerCount() == 1) {
                    Player winner = game.getPlayers().get(0);
                    gameManager.endGame(game, winner);
                } else if (game.getPlayerCount() == 0) {
                    gameManager.endGame(game, null);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Game game = gameManager.getPlayerGame(player);

        // Allow drops during gameplay
        if (game != null && game.getState() == Game.GameState.PLAYING) {
            return;
        }

        // Prevent drops outside gameplay
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Game game = gameManager.getPlayerGame(player);

        if (game == null || game.getState() != Game.GameState.PLAYING) {
            // Allow normal interactions outside game
            return;
        }

        // Handle fireball firing (fire charges)
        if (event.getMaterial() == Material.FIRE_CHARGE) {
            // This is handled automatically by Minecraft/Spigot
            return;
        }
    }
}
