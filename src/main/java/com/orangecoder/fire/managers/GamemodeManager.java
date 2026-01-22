package com.orangecoder.fire.managers;

import com.orangecoder.fire.gamemodes.Gamemode;
import com.orangecoder.fire.gamemodes.FireballGamemode;
import com.orangecoder.fire.gamemodes.ClassicPvPGamemode;
import com.orangecoder.fire.gamemodes.BowGamemode;

import java.util.*;

public class GamemodeManager {

    private Map<String, Gamemode> gamemodes = new HashMap<>();

    public GamemodeManager() {
        registerDefaultGamemodes();
    }

    public void registerGamemode(Gamemode gamemode) {
        gamemodes.put(gamemode.getName().toLowerCase(), gamemode);
    }

    public void registerDefaultGamemodes() {
        registerGamemode(new FireballGamemode());
        registerGamemode(new ClassicPvPGamemode());
        registerGamemode(new BowGamemode());
    }

    public Gamemode getGamemode(String name) {
        return gamemodes.get(name.toLowerCase());
    }

    public Map<String, Gamemode> getGamemodes() {
        return new HashMap<>(gamemodes);
    }

    public Map<String, Gamemode> getEnabledGamemodes() {
        Map<String, Gamemode> enabled = new HashMap<>();
        for (String key : gamemodes.keySet()) {
            Gamemode gm = gamemodes.get(key);
            if (gm.isEnabled()) {
                enabled.put(key, gm);
            }
        }
        return enabled;
    }

    public void setGamemodeEnabled(String name, boolean enabled) {
        Gamemode gm = getGamemode(name);
        if (gm != null) {
            gm.setEnabled(enabled);
        }
    }

    public int getGamemodeCount() {
        return gamemodes.size();
    }

    public int getEnabledGamemodeCount() {
        return getEnabledGamemodes().size();
    }
}
