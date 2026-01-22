package com.orangecoder.fire;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import com.orangecoder.fire.managers.GameManager;
import com.orangecoder.fire.managers.ArenaManager;
import com.orangecoder.fire.managers.GamemodeManager;
import com.orangecoder.fire.commands.FireballCommand;
import com.orangecoder.fire.commands.ChatCommand;
import com.orangecoder.fire.listeners.GameListener;
import com.orangecoder.fire.listeners.PlayerListener;

public class Fire extends JavaPlugin {

    private static Fire instance;
    private GameManager gameManager;
    private ArenaManager arenaManager;
    private GamemodeManager gamemodeManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Create default config
        saveDefaultConfig();
        
        // Initialize managers
        this.gamemodeManager = new GamemodeManager();
        this.arenaManager = new ArenaManager(this);
        this.gameManager = new GameManager(this);
        
        // Auto-setup default arena if none exist
        if (arenaManager.getArenaCount() == 0) {
            setupDefaultArena();
        }
        
        // Register commands
        new FireballCommand(this);
        new ChatCommand(this);
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        
        getLogger().info("§a[Fire] Fireball Fight Plugin enabled!");
        getLogger().info("§e[Fire] Loaded " + gamemodeManager.getGamemodeCount() + " gamemodes!");
        getLogger().info("§e[Fire] Use /fireballwars to get started!");
    }

    @Override
    public void onDisable() {
        if (gameManager != null) {
            gameManager.stopAllGames();
        }
        getLogger().info("§c[Fire] Fireball Fight Plugin disabled!");
    }

    private void setupDefaultArena() {
        getLogger().info("§e[Fire] Setting up default arena...");
        
        Location world = getServer().getWorlds().get(0).getSpawnLocation();
        
        Location spawn1 = new Location(world.getWorld(), -10, 100, 0, 0, 0);
        Location spawn2 = new Location(world.getWorld(), 10, 100, 0, 180, 0);
        
        arenaManager.createArena("Default", spawn1, spawn2);
        
        getLogger().info("§a[Fire] Default arena created!");
        getLogger().info("§e[Fire] Use '/fireballwars arena setspawn1 Default' and '/fireballwars arena setspawn2 Default' to set actual spawn points");
    }

    public static Fire getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public GamemodeManager getGamemodeManager() {
        return gamemodeManager;
    }
}
