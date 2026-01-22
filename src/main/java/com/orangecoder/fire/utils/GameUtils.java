package com.orangecoder.fire.utils;

import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

public class GameUtils {

    public static void resetPlayer(Player player) {
        // Clear inventory
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);

        // Remove potion effects
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        // Reset health and hunger
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(10.0f);

        // Set to survival mode
        player.setGameMode(GameMode.SURVIVAL);

        // Reset other attributes
        player.setLevel(0);
        player.setExp(0.0f);
    }

    public static double getDistance(org.bukkit.Location loc1, org.bukkit.Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            return Double.MAX_VALUE;
        }
        return loc1.distance(loc2);
    }

    public static int secondsToTicks(int seconds) {
        return seconds * 20;
    }

    public static int ticksToSeconds(int ticks) {
        return ticks / 20;
    }
}
