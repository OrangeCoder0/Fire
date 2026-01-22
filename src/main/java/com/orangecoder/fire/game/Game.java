package com.orangecoder.fire.game;

import org.bukkit.entity.Player;
import com.orangecoder.fire.gamemodes.Gamemode;
import java.util.*;

public class Game {

    public enum GameState {
        WAITING_FOR_PLAYERS,
        STARTING,
        PLAYING,
        ENDING,
        ENDED
    }

    private String id;
    private Arena arena;
    private Gamemode gamemode;
    private List<Player> players = new ArrayList<>();
    private List<Player> spectators = new ArrayList<>();
    private GameState state = GameState.WAITING_FOR_PLAYERS;
    private int countdownTicks;
    private int gameDurationTicks;
    private Map<Player, Integer> playerStats = new HashMap<>();
    private Map<Player, Integer> kills = new HashMap<>();
    private Player winner;
    private long createdAt;

    public Game(String id, Arena arena, Gamemode gamemode) {
        this.id = id;
        this.arena = arena;
        this.gamemode = gamemode;
        this.createdAt = System.currentTimeMillis();
        this.countdownTicks = 200; // 10 seconds
        this.gameDurationTicks = 0;
    }

    public Game(String id, Arena arena) {
        this(id, arena, null);
    }

    public String getId() {
        return id;
    }

    public Arena getArena() {
        return arena;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public List<Player> getSpectators() {
        return new ArrayList<>(spectators);
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            playerStats.put(player, 0);
            kills.put(player, 0);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        playerStats.remove(player);
        kills.remove(player);
    }

    public void addSpectator(Player player) {
        if (!spectators.contains(player)) {
            spectators.add(player);
        }
    }

    public void removeSpectator(Player player) {
        spectators.remove(player);
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getCountdownTicks() {
        return countdownTicks;
    }

    public void setCountdownTicks(int countdownTicks) {
        this.countdownTicks = countdownTicks;
    }

    public int getGameDurationTicks() {
        return gameDurationTicks;
    }

    public void setGameDurationTicks(int gameDurationTicks) {
        this.gameDurationTicks = gameDurationTicks;
    }

    public void addGameDurationTicks(int ticks) {
        this.gameDurationTicks += ticks;
    }

    public Map<Player, Integer> getKills() {
        return kills;
    }

    public void addKill(Player player) {
        kills.put(player, kills.getOrDefault(player, 0) + 1);
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isFull() {
        return players.size() >= arena.getMaxPlayers();
    }

    public boolean canStart() {
        return players.size() >= arena.getMinPlayers();
    }

    public int getPlayerCount() {
        return players.size();
    }

    public int getSpectatorCount() {
        return spectators.size();
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public void setGamemode(Gamemode gamemode) {
        this.gamemode = gamemode;
    }
}
