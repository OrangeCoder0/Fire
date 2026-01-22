# Fire Plugin - Advanced Features & Customization

## Advanced Game Configurations

### Modify Game Settings

Edit `plugins/Fire/config.yml`:

```yaml
# Game timing
game:
  countdown-seconds: 10      # Change countdown time
  max-game-duration: 600     # Change max game length
  
  # Player limits
  player-min: 2              # Minimum to start
  player-max: 2              # Maximum per game

# Custom messages
messages:
  game-started: "§a✓ Custom Start Message!"
  player-eliminated: "§c{player} is OUT!"
  game-won: "§a§l⭐ {player} is the KING!"
```

---

## Customize Kits

Edit `src/main/java/com/orangecoder/fire/kits/Kit.java`:

### Add More Items
```java
// In getDefaultFireballKit() method:
inventory[4] = createItem(Material.SHIELD, 1, "§bShield");
inventory[5] = createItem(Material.ENDER_PEARL, 4, "§5Ender Pearl");
```

### Change Armor
```java
armor[0] = createItem(Material.IRON_BOOTS, 1, "Iron Boots");
armor[1] = createItem(Material.IRON_LEGGINGS, 1, "Iron Leggings");
// etc...
```

### Add Potion Effects
```java
kit.addEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0));
kit.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
```

---

## Create Custom Kits

### In Kit.java:
```java
public static Kit getUltraFireballKit() {
    Kit kit = new Kit("Ultra Fireball");
    
    // More fire charges
    ItemStack[] inventory = new ItemStack[36];
    inventory[0] = createItem(Material.FIRE_CHARGE, 128, "Unlimited Fireballs");
    // ... rest of items
    
    kit.setInventory(inventory);
    return kit;
}
```

### Give Custom Kit in GameManager.java:
```java
// In startGame() method:
Kit kit = Kit.getUltraFireballKit(); // Change this line
for (int i = 0; i < players.size(); i++) {
    // ... rest stays same
}
```

---

## Advanced Arena Setup

### Create Multiple Arenas
```
/fireballwars arena create sky_arena
/fireballwars arena create lava_arena
/fireballwars arena create void_arena
```

### Set Different Spawn Heights
- Sky Arena: Spawn at y=200
- Lava Arena: Spawn at y=65 (above lava)
- Void Arena: Spawn on floating platforms

---

## Modify Game Logic

### Change Win Condition

Edit `GameManager.java` in the `tickGame()` method:

```java
case PLAYING:
    // Current: Last player wins
    // Change to: First to 5 kills wins
    
    int MAX_KILLS = 5;
    for (Player p : game.getPlayers()) {
        if (game.getKills().getOrDefault(p, 0) >= MAX_KILLS) {
            endGame(game, p);
            return;
        }
    }
    break;
```

### Add Time Limits

```java
int MAX_TICKS = 20 * 60; // 1 minute
if (game.getGameDurationTicks() > MAX_TICKS) {
    // Find player with most kills
    Player winner = game.getPlayers().stream()
        .max((p1, p2) -> game.getKills().get(p1) - game.getKills().get(p2))
        .orElse(null);
    endGame(game, winner);
}
```

---

## Sound Effects

### Add Sounds When Fireballs Hit

In `GameListener.java`:

```java
@EventHandler
public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    // ... existing code ...
    
    if (event.getDamager() instanceof Fireball) {
        Player victim = (Player) event.getEntity();
        victim.playSound(victim.getLocation(), Sound.ENTITY_WITHER_HURT, 1, 1);
        // Or
        victim.playSound(victim.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
    }
}
```

---

## Particle Effects

### Add Fireball Impact Particles

```java
// In GameListener.java, add:
if (event.getDamager() instanceof Fireball) {
    Fireball fireball = (Fireball) event.getDamager();
    Location loc = fireball.getLocation();
    
    // Spawn particles
    fireball.getWorld().spawnParticle(
        Particle.FLAME, 
        loc, 
        10, 
        0.5, 0.5, 0.5, 
        0.1
    );
}
```

---

## Cosmetic Customization

### Add Custom Item Names

Edit `Kit.java`:

```java
ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
ItemMeta meta = sword.getItemMeta();
meta.setDisplayName("§c§l⚔ INFERNO BLADE ⚔");
meta.setLore(Arrays.asList("§7Deals extra damage", "§7in fireball battles"));
sword.setItemMeta(meta);
```

### Custom Armor Colors (Leather Armor)

```java
ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
meta.setColor(Color.fromRGB(255, 100, 0)); // Orange fire color
helmet.setItemMeta(meta);
```

---

## Team Features (Advanced)

### Modify for Team Mode (2v2)

Edit `Game.java`:

```java
public enum GameState {
    WAITING_FOR_PLAYERS,
    STARTING,
    PLAYING,
    ENDING,
    ENDED
}

// Add team tracking:
private Map<Player, Integer> playerTeam = new HashMap<>(); // 0 or 1

public void setPlayerTeam(Player player, int team) {
    playerTeam.put(player, team);
}
```

Edit `GameManager.java`:

```java
// In joinGame():
if (game.getPlayerCount() >= 4) { // 2v2
    startGameCountdown(game);
}

// In startGame():
List<Player> players = game.getPlayers();
for (int i = 0; i < players.size(); i++) {
    Player player = players.get(i);
    int team = i % 2; // Alternate teams
    game.setPlayerTeam(player, team);
    player.setScoreboard(createTeamScoreboard(team));
}
```

---

## Database Integration (Optional)

### Add Stats Persistence

Create a new class `StatsManager.java`:

```java
public class StatsManager {
    private Map<UUID, Integer> playerStats = new HashMap<>();
    
    public void saveStats(UUID playerUUID, int kills) {
        playerStats.put(playerUUID, kills);
        // Save to file or database
    }
    
    public int getStats(UUID playerUUID) {
        return playerStats.getOrDefault(playerUUID, 0);
    }
}
```

---

## Performance Optimization

### Reduce Tick Rate for Large Servers

In `GameManager.java`:

```java
// Change from:
gameTickRunnable.runTaskTimer(plugin, 0L, 1L);

// To (every 2 ticks = less frequent updates):
gameTickRunnable.runTaskTimer(plugin, 0L, 2L);
```

### Limit Concurrent Games

```java
public void joinGame(Player player, Game game) {
    // Cap total games
    if (games.size() > 50) {
        player.sendMessage("§cServer at capacity!");
        return;
    }
    // ... rest of method
}
```

---

## Debugging

### Add Debug Logging

In `GameManager.java`:

```java
private void tickGame(Game game) {
    // Add debug:
    if (game.getGameDurationTicks() % 100 == 0) {
        plugin.getLogger().info("Game " + game.getId() + " - State: " + game.getState());
    }
    // ... rest of method
}
```

---

## Testing Commands

### Test in-game:
```
/fireballwars arena create test_arena
/fireballwars arena setspawn1 test_arena
/fireballwars arena setspawn2 test_arena
/fireballwars join test_arena
# Join with another player
/fireballwars stats
```

---

## Troubleshooting Custom Changes

### Recompile After Changes:
```bash
mvn clean compile
mvn package -DskipTests
```

### Check for Errors:
```bash
mvn compile 2>&1 | grep ERROR
```

### Reload Plugin (if supporting plugins allow):
```
/plugman reload Fire
```

---

## What's Next?

1. **Add more customization** - Modify kits, messages, timings
2. **Create skins** - Add custom player skins for players
3. **Add leaderboard** - Track top players
4. **Add rewards** - Give items/money to winners
5. **Add spectator chat** - Let spectators chat
6. **Add replay system** - Record matches

---

**The plugin is designed to be fully customizable! Have fun tweaking! 🔥**
