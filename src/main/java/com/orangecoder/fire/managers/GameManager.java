package com.orangecoder.fire.managers;

import com.orangecoder.fire.Fire;
import com.orangecoder.fire.game.Arena;
import com.orangecoder.fire.game.Game;
import com.orangecoder.fire.kits.Kit;
import com.orangecoder.fire.utils.GameUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import java.util.*;

public class GameManager {

    private Fire plugin;
    private Map<String, Game> games = new HashMap<>();
    public Map<Player, String> playerGameMap = new HashMap<>();
    private BukkitRunnable gameTickRunnable;

    public GameManager(Fire plugin) {
        this.plugin = plugin;
        startGameTick();
    }

    public Game createGame(Arena arena) {
        String gameId = "game_" + UUID.randomUUID().toString().substring(0, 8);
        Game game = new Game(gameId, arena);
        games.put(gameId, game);
        return game;
    }

    public Game createGame(Arena arena, com.orangecoder.fire.gamemodes.Gamemode gamemode) {
        String gameId = "game_" + UUID.randomUUID().toString().substring(0, 8);
        Game game = new Game(gameId, arena, gamemode);
        games.put(gameId, game);
        return game;
    }

    public void joinGame(Player player, Game game) {
        if (game.isFull()) {
            player.sendMessage("§c✗ Game is full!");
            return;
        }

        // Check if player is already in a game
        if (isPlayerInGame(player)) {
            quitGame(player);
        }

        game.addPlayer(player);
        playerGameMap.put(player, game.getId());
        player.sendMessage("§a✓ You joined the Fireball Fight game!");
        player.setGameMode(GameMode.ADVENTURE);

        // Broadcast to other players
        for (Player p : game.getPlayers()) {
            if (p != player) {
                p.sendMessage("§e" + player.getName() + " joined the game! (" + game.getPlayerCount() + "/" + game.getArena().getMaxPlayers() + ")");
            }
        }

        // Check if we can start the game
        if (game.getPlayerCount() >= game.getArena().getMinPlayers()) {
            startGameCountdown(game);
        }
    }

    public void quitGame(Player player) {
        String gameId = playerGameMap.get(player);
        if (gameId != null) {
            Game game = games.get(gameId);
            if (game != null) {
                game.removePlayer(player);
                playerGameMap.remove(player);
                player.sendMessage("§c✗ You left the game!");

                // Reset player
                GameUtils.resetPlayer(player);

                if (game.getPlayerCount() == 0) {
                    games.remove(gameId);
                }
            }
        }
    }

    private void startGameCountdown(Game game) {
        if (game.getState() != Game.GameState.WAITING_FOR_PLAYERS) {
            return;
        }

        game.setState(Game.GameState.STARTING);
        broadcastToGame(game, "§e⏱ Game starting in 10 seconds!");
    }

    public void startGame(Game game) {
        if (game.getState() != Game.GameState.STARTING) {
            return;
        }

        game.setState(Game.GameState.PLAYING);
        List<Player> players = game.getPlayers();

        broadcastToGame(game, "§a✓ Game Started!");
        
        if (game.getGamemode() != null) {
            broadcastToGame(game, game.getGamemode().getRules());
        }

        // Give kit to all players
        com.orangecoder.fire.kits.Kit kit;
        if (game.getGamemode() != null) {
            kit = game.getGamemode().getKit();
        } else {
            kit = com.orangecoder.fire.kits.Kit.getDefaultFireballKit();
        }
        
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.teleport(game.getArena().getSpawnForPlayer(i));
            kit.giveKit(player);
            player.setGameMode(GameMode.ADVENTURE);
            
            if (game.getGamemode() != null) {
                game.getGamemode().onPlayerJoin(game, player);
            }
        }
    }

    public void endGame(Game game, Player winner) {
        if (game.getState() == Game.GameState.ENDED) {
            return;
        }

        game.setState(Game.GameState.ENDED);
        game.setWinner(winner);

        if (winner != null) {
            broadcastToGame(game, "§a§l⭐ " + winner.getName() + " won the game!");
        }

        // Teleport players back to spawn and reset them
        for (Player player : game.getPlayers()) {
            GameUtils.resetPlayer(player);
            player.teleport(player.getWorld().getSpawnLocation());
            playerGameMap.remove(player);
        }

        for (Player spectator : game.getSpectators()) {
            GameUtils.resetPlayer(spectator);
            spectator.teleport(spectator.getWorld().getSpawnLocation());
            playerGameMap.remove(spectator);
        }

        // Remove game after a delay
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            games.remove(game.getId());
        }, 100L);
    }

    public Game getPlayerGame(Player player) {
        String gameId = playerGameMap.get(player);
        return gameId != null ? games.get(gameId) : null;
    }

    public boolean isPlayerInGame(Player player) {
        return playerGameMap.containsKey(player);
    }

    public void stopAllGames() {
        for (Game game : new ArrayList<>(games.values())) {
            endGame(game, null);
        }
        if (gameTickRunnable != null) {
            gameTickRunnable.cancel();
        }
    }

    private void broadcastToGame(Game game, String message) {
        for (Player player : game.getPlayers()) {
            player.sendMessage(message);
        }
        for (Player spectator : game.getSpectators()) {
            spectator.sendMessage(message);
        }
    }

    private void startGameTick() {
        gameTickRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                List<String> gamesToRemove = new ArrayList<>();

                for (String gameId : games.keySet()) {
                    Game game = games.get(gameId);
                    tickGame(game);

                    // Clean up empty games
                    if (game.getState() == Game.GameState.ENDED && game.getGameDurationTicks() > 100) {
                        gamesToRemove.add(gameId);
                    }
                }

                for (String gameId : gamesToRemove) {
                    games.remove(gameId);
                }
            }
        };
        gameTickRunnable.runTaskTimer(plugin, 0L, 1L);
    }

    private void tickGame(Game game) {
        switch (game.getState()) {
            case WAITING_FOR_PLAYERS:
                // Do nothing
                break;

            case STARTING:
                game.setCountdownTicks(game.getCountdownTicks() - 1);
                int seconds = game.getCountdownTicks() / 20;

                if (seconds > 0 && game.getCountdownTicks() % 20 == 0) {
                    if (seconds <= 3) {
                        broadcastToGame(game, "§c" + seconds);
                    }
                }

                if (game.getCountdownTicks() <= 0) {
                    startGame(game);
                }
                break;

            case PLAYING:
                game.addGameDurationTicks(1);
                List<Player> playersAlive = game.getPlayers().stream()
                        .filter(p -> p.isOnline() && p.getHealth() > 0)
                        .toList();

                // Check win condition
                if (playersAlive.size() == 1) {
                    endGame(game, playersAlive.get(0));
                } else if (playersAlive.size() == 0) {
                    endGame(game, null);
                } else if (game.getPlayerCount() > playersAlive.size()) {
                    // Remove dead players
                    for (Player p : game.getPlayers()) {
                        if (p.getHealth() <= 0) {
                            game.removePlayer(p);
                            playerGameMap.remove(p);
                            p.setSpectatorTarget(null);
                            broadcastToGame(game, "§c" + p.getName() + " was eliminated!");
                        }
                    }
                }
                break;

            case ENDING:
            case ENDED:
                break;
        }
    }

    public Map<String, Game> getGames() {
        return new HashMap<>(games);
    }

    public int getGameCount() {
        return games.size();
    }
}
