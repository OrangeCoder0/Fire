package com.orangecoder.fire.gamemodes;

import com.orangecoder.fire.game.Arena;
import com.orangecoder.fire.game.Game;
import com.orangecoder.fire.kits.Kit;
import org.bukkit.entity.Player;

public abstract class Gamemode {

    protected String name;
    protected String description;
    protected int minPlayers;
    protected int maxPlayers;
    protected boolean enabled;

    public Gamemode(String name, String description, int minPlayers, int maxPlayers) {
        this.name = name;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.enabled = true;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Get the kit for this gamemode
     */
    public abstract Kit getKit();

    /**
     * Called when the game starts
     */
    public abstract void onGameStart(Game game);

    /**
     * Called when a player joins
     */
    public abstract void onPlayerJoin(Game game, Player player);

    /**
     * Called when a player is eliminated
     */
    public abstract void onPlayerEliminated(Game game, Player player);

    /**
     * Called every tick
     */
    public abstract void onGameTick(Game game);

    /**
     * Get gamemode-specific rules as string
     */
    public abstract String getRules();
}
