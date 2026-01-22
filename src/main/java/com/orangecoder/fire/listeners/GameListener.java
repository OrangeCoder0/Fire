package com.orangecoder.fire.listeners;

import com.orangecoder.fire.Fire;
import com.orangecoder.fire.game.Game;
import com.orangecoder.fire.managers.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.SmallFireball;

public class GameListener implements Listener {

    private Fire plugin;
    private GameManager gameManager;

    public GameListener(Fire plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Game game = gameManager.getPlayerGame(victim);

        if (game == null || game.getState() != Game.GameState.PLAYING) {
            event.setCancelled(true);
            return;
        }

        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Game attackerGame = gameManager.getPlayerGame(attacker);

            if (attackerGame != game) {
                event.setCancelled(true);
                return;
            }

            // Track kills
            game.addKill(attacker);
        } else if (event.getDamager() instanceof Fireball || event.getDamager() instanceof SmallFireball) {
            // Fireball damage in game is allowed
            event.setDamage(8.0); // Standardize fireball damage
        }

        // Check if player died
        if (victim.getHealth() - event.getFinalDamage() <= 0) {
            victim.setHealth(0);
            event.setCancelled(true);

            // Remove player from game
            game.removePlayer(victim);
            gameManager.playerGameMap.remove(victim);
            victim.setSpectatorTarget(null);

            // Broadcast elimination
            for (Player p : game.getPlayers()) {
                p.sendMessage("§c" + victim.getName() + " was eliminated!");
            }

            // Check win condition
            if (game.getPlayerCount() == 1) {
                Player winner = game.getPlayers().get(0);
                gameManager.endGame(game, winner);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Game game = gameManager.getPlayerGame(player);

        if (game == null) {
            return;
        }

        if (game.getState() != Game.GameState.PLAYING) {
            // Prevent damage outside of game
            if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            Game game = gameManager.getPlayerGame(shooter);

            if (game != null && game.getState() == Game.GameState.PLAYING) {
                // Allow projectiles in game
                return;
            }

            // Cancel projectiles outside of game
            event.getEntity().remove();
        }
    }
}
