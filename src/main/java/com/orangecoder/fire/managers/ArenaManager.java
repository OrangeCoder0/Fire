package com.orangecoder.fire.managers;

import com.orangecoder.fire.Fire;
import com.orangecoder.fire.game.Arena;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ArenaManager {

    private Fire plugin;
    private Map<String, Arena> arenas = new HashMap<>();

    public ArenaManager(Fire plugin) {
        this.plugin = plugin;
        loadArenas();
    }

    public void createArena(String name, Location spawn1, Location spawn2) {
        Arena arena = new Arena(name, spawn1, spawn2);
        arenas.put(name.toLowerCase(), arena);
        saveArenas();
    }

    public Arena getArena(String name) {
        return arenas.get(name.toLowerCase());
    }

    public Map<String, Arena> getArenas() {
        return new HashMap<>(arenas);
    }

    public void deleteArena(String name) {
        arenas.remove(name.toLowerCase());
        saveArenas();
    }

    public void saveArenas() {
        FileConfiguration config = plugin.getConfig();
        config.set("arenas", null);
        
        for (String name : arenas.keySet()) {
            Arena arena = arenas.get(name);
            String path = "arenas." + name + ".";
            
            config.set(path + "spawn1", arena.getSpawn1());
            config.set(path + "spawn2", arena.getSpawn2());
            config.set(path + "min-players", arena.getMinPlayers());
            config.set(path + "max-players", arena.getMaxPlayers());
            config.set(path + "enabled", arena.isEnabled());
        }
        
        plugin.saveConfig();
    }

    public void loadArenas() {
        FileConfiguration config = plugin.getConfig();
        
        if (!config.contains("arenas")) {
            createDefaultArena();
            return;
        }

        for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
            String path = "arenas." + key + ".";
            
            Location spawn1 = config.getLocation(path + "spawn1");
            Location spawn2 = config.getLocation(path + "spawn2");
            
            if (spawn1 != null && spawn2 != null) {
                Arena arena = new Arena(key, spawn1, spawn2);
                arena.setMinPlayers(config.getInt(path + "min-players", 2));
                arena.setMaxPlayers(config.getInt(path + "max-players", 2));
                arena.setEnabled(config.getBoolean(path + "enabled", true));
                arenas.put(key.toLowerCase(), arena);
            }
        }
    }

    private void createDefaultArena() {
        // Create a default arena if none exist
        plugin.getLogger().info("§eCreating default arena...");
    }

    public boolean arenaExists(String name) {
        return arenas.containsKey(name.toLowerCase());
    }

    public int getArenaCount() {
        return arenas.size();
    }
}
