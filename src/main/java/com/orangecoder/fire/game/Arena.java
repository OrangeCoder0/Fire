package com.orangecoder.fire.game;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import java.util.*;

public class Arena {

    private String name;
    private Location spawn1;
    private Location spawn2;
    private Location worldBorder;
    private List<Location> spectatorSpawns = new ArrayList<>();
    private int minPlayers;
    private int maxPlayers;
    private boolean enabled;

    public Arena(String name, Location spawn1, Location spawn2) {
        this.name = name;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
        this.minPlayers = 2;
        this.maxPlayers = 2;
        this.enabled = true;
    }

    public String getName() {
        return name;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public Location getSpawnForPlayer(int index) {
        return index == 0 ? spawn1 : spawn2;
    }

    public void setSpawn1(Location spawn1) {
        this.spawn1 = spawn1;
    }

    public void setSpawn2(Location spawn2) {
        this.spawn2 = spawn2;
    }

    public Location getWorldBorder() {
        return worldBorder;
    }

    public void setWorldBorder(Location worldBorder) {
        this.worldBorder = worldBorder;
    }

    public List<Location> getSpectatorSpawns() {
        return spectatorSpawns;
    }

    public void addSpectatorSpawn(Location location) {
        spectatorSpawns.add(location);
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
