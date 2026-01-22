package com.orangecoder.fire.commands;

import com.orangecoder.fire.Fire;
import com.orangecoder.fire.game.Arena;
import com.orangecoder.fire.game.Game;
import com.orangecoder.fire.managers.GameManager;
import com.orangecoder.fire.managers.ArenaManager;
import com.orangecoder.fire.managers.GamemodeManager;
import com.orangecoder.fire.gamemodes.Gamemode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FireballCommand implements CommandExecutor {

    private Fire plugin;
    private GameManager gameManager;
    private ArenaManager arenaManager;
    private GamemodeManager gamemodeManager;

    public FireballCommand(Fire plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.arenaManager = plugin.getArenaManager();
        this.gamemodeManager = plugin.getGamemodeManager();

        plugin.getCommand("fireballwars").setExecutor(this);
        plugin.getCommand("ffight").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "join":
                return handleJoin(player, args);
            case "quit":
            case "leave":
                return handleQuit(player);
            case "list":
                return handleList(player);
            case "arena":
                return handleArena(player, args);
            case "start":
                return handleStart(player);
            case "stats":
                return handleStats(player);
            case "gamemode":
                return handleGamemode(player, args);
            default:
                sendHelp(player);
                return true;
        }
    }

    private boolean handleJoin(Player player, String[] args) {
        if (gameManager.isPlayerInGame(player)) {
            player.sendMessage("§c✗ You are already in a game!");
            return true;
        }

        Arena arena;
        String gamemodeName = "Fireball";
        
        if (args.length < 2) {
            // Get first available arena
            java.util.Map<String, Arena> arenas = arenaManager.getArenas();
            if (arenas.isEmpty()) {
                player.sendMessage("§c✗ No arenas available!");
                return true;
            }
            arena = arenas.values().iterator().next();
        } else {
            arena = arenaManager.getArena(args[1]);
            if (arena == null) {
                player.sendMessage("§c✗ Arena not found!");
                return true;
            }
        }

        // Check for gamemode argument
        if (args.length >= 3) {
            Gamemode gm = gamemodeManager.getGamemode(args[2]);
            if (gm != null) {
                gamemodeName = args[2];
            }
        }

        if (!arena.isEnabled()) {
            player.sendMessage("§c✗ This arena is disabled!");
            return true;
        }

        // Create or find a game for this arena and gamemode
        Game game = null;
        for (Game g : gameManager.getGames().values()) {
            if (g.getArena().getName().equals(arena.getName()) && 
                g.getState() == Game.GameState.WAITING_FOR_PLAYERS) {
                
                if (g.getGamemode() != null && g.getGamemode().getName().equalsIgnoreCase(gamemodeName)) {
                    game = g;
                    break;
                } else if (g.getGamemode() == null && gamemodeName.equals("Fireball")) {
                    game = g;
                    break;
                }
            }
        }

        if (game == null) {
            Gamemode gamemode = gamemodeManager.getGamemode(gamemodeName);
            game = gameManager.createGame(arena, gamemode);
        }

        gameManager.joinGame(player, game);
        return true;
    }

    private boolean handleQuit(Player player) {
        if (!gameManager.isPlayerInGame(player)) {
            player.sendMessage("§c✗ You are not in a game!");
            return true;
        }

        gameManager.quitGame(player);
        return true;
    }

    private boolean handleList(Player player) {
        java.util.Map<String, Arena> arenas = arenaManager.getArenas();

        if (arenas.isEmpty()) {
            player.sendMessage("§c✗ No arenas available!");
            return true;
        }

        player.sendMessage("§6========== Arenas ==========");
        for (Arena arena : arenas.values()) {
            String status = arena.isEnabled() ? "§a✓" : "§c✗";
            player.sendMessage(status + " §e" + arena.getName() + " §f- Players: " + arena.getMinPlayers() + "-" + arena.getMaxPlayers());
        }

        player.sendMessage("§6========== Games ==========");
        if (gameManager.getGames().isEmpty()) {
            player.sendMessage("§eNo active games");
        } else {
            for (Game game : gameManager.getGames().values()) {
                player.sendMessage("§e" + game.getArena().getName() + " - " + game.getPlayerCount() + "/" + game.getArena().getMaxPlayers() + " players - §f" + game.getState());
            }
        }
        return true;
    }

    private boolean handleArena(Player player, String[] args) {
        if (!player.hasPermission("fireballwars.admin")) {
            player.sendMessage("§c✗ You don't have permission to use this command!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§c✗ Usage: /fireballwars arena <create|delete|setspawn1|setspawn2|list> [name]");
            return true;
        }

        String subcommand = args[1].toLowerCase();

        switch (subcommand) {
            case "create":
                if (args.length < 3) {
                    player.sendMessage("§c✗ Usage: /fireballwars arena create <name>");
                    return true;
                }
                String arenaName = args[2];
                if (arenaManager.arenaExists(arenaName)) {
                    player.sendMessage("§c✗ Arena already exists!");
                    return true;
                }
                // Create arena with placeholder locations (player should set spawns)
                arenaManager.createArena(arenaName, player.getLocation(), player.getLocation());
                player.sendMessage("§a✓ Arena created! Set spawn points with §e/fireballwars arena setspawn1 " + arenaName + "§a and §e/fireballwars arena setspawn2 " + arenaName);
                return true;

            case "delete":
                if (args.length < 3) {
                    player.sendMessage("§c✗ Usage: /fireballwars arena delete <name>");
                    return true;
                }
                String deleteName = args[2];
                if (!arenaManager.arenaExists(deleteName)) {
                    player.sendMessage("§c✗ Arena not found!");
                    return true;
                }
                arenaManager.deleteArena(deleteName);
                player.sendMessage("§a✓ Arena deleted!");
                return true;

            case "setspawn1":
                if (args.length < 3) {
                    player.sendMessage("§c✗ Usage: /fireballwars arena setspawn1 <name>");
                    return true;
                }
                Arena arena1 = arenaManager.getArena(args[2]);
                if (arena1 == null) {
                    player.sendMessage("§c✗ Arena not found!");
                    return true;
                }
                arena1.setSpawn1(player.getLocation());
                arenaManager.saveArenas();
                player.sendMessage("§a✓ Spawn 1 set!");
                return true;

            case "setspawn2":
                if (args.length < 3) {
                    player.sendMessage("§c✗ Usage: /fireballwars arena setspawn2 <name>");
                    return true;
                }
                Arena arena2 = arenaManager.getArena(args[2]);
                if (arena2 == null) {
                    player.sendMessage("§c✗ Arena not found!");
                    return true;
                }
                arena2.setSpawn2(player.getLocation());
                arenaManager.saveArenas();
                player.sendMessage("§a✓ Spawn 2 set!");
                return true;

            case "list":
                if (arenaManager.getArenas().isEmpty()) {
                    player.sendMessage("§c✗ No arenas!");
                    return true;
                }
                player.sendMessage("§6========== Arenas ==========");
                for (Arena a : arenaManager.getArenas().values()) {
                    player.sendMessage("§e" + a.getName() + " - " + (a.isEnabled() ? "§aEnabled" : "§cDisabled"));
                }
                return true;

            default:
                player.sendMessage("§c✗ Unknown arena subcommand!");
                return true;
        }
    }

    private boolean handleStart(Player player) {
        Game game = gameManager.getPlayerGame(player);
        if (game == null) {
            player.sendMessage("§c✗ You are not in a game!");
            return true;
        }

        if (game.getState() != Game.GameState.WAITING_FOR_PLAYERS) {
            player.sendMessage("§c✗ Game has already started!");
            return true;
        }

        if (game.getPlayerCount() < game.getArena().getMinPlayers()) {
            player.sendMessage("§c✗ Not enough players! Need " + game.getArena().getMinPlayers());
            return true;
        }

        gameManager.createGame(game.getArena());
        game.setState(Game.GameState.STARTING);
        return true;
    }

    private boolean handleStats(Player player) {
        Game game = gameManager.getPlayerGame(player);
        if (game == null) {
            player.sendMessage("§c✗ You are not in a game!");
            return true;
        }

        player.sendMessage("§6========== Game Stats ==========");
        for (Player p : game.getPlayers()) {
            int kills = game.getKills().getOrDefault(p, 0);
            player.sendMessage("§e" + p.getName() + " - Kills: §c" + kills);
        }
        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6========== Fireball Wars Help ==========");
        player.sendMessage("§e/fireballwars join [arena] [gamemode] §f- Join a game");
        player.sendMessage("§e/fireballwars quit §f- Leave your game");
        player.sendMessage("§e/fireballwars list §f- View available arenas and games");
        player.sendMessage("§e/fireballwars gamemode §f- View available gamemodes");
        player.sendMessage("§e/fireballwars stats §f- View game statistics");
        player.sendMessage("§e/fireballwars start §f- Force start a game (if host)");
        if (player.hasPermission("fireballwars.admin")) {
            player.sendMessage("§c[ADMIN] /fireballwars arena create <name> §f- Create arena");
            player.sendMessage("§c[ADMIN] /fireballwars arena setspawn1 <name> §f- Set spawn 1");
            player.sendMessage("§c[ADMIN] /fireballwars arena setspawn2 <name> §f- Set spawn 2");
            player.sendMessage("§c[ADMIN] /fireballwars arena delete <name> §f- Delete arena");
            player.sendMessage("§c[ADMIN] /fireballwars arena list §f- List all arenas");
        }
    }

    private boolean handleGamemode(Player player, String[] args) {
        player.sendMessage("§6========== Available Gamemodes ==========");
        for (Gamemode gm : gamemodeManager.getGamemodes().values()) {
            String status = gm.isEnabled() ? "§a✓" : "§c✗";
            player.sendMessage(status + " §e" + gm.getName() + " §f- " + gm.getDescription());
        }
        player.sendMessage("§eUsage: §c/fireballwars join [arena] [gamemode]");
        return true;
    }
}
