# Fire - Advanced Fireball Fight Plugin with Multiple Gamemodes

A complete, production-ready Fireball Fight plugin for Minecraft Spigot/Paper servers with **multiple gamemodes**, **auto-setup**, and **full arena management**. Inspired by Minemen Club's Fireball Wars game mode, featuring intense 1v1 PvP battles with various combat styles.

## 🎮 Features

### ✨ Complete Game System
- **1v1 PvP Battles** - Intense player vs player combat
- **Automatic Matchmaking** - Find opponents automatically
- **Multiple Gamemodes** - Fireball, Classic PvP, Bow Duels
- **Auto-Setup** - Default arena created on first plugin load
- **Countdown System** - Smooth game start with countdown
- **Multiple Arena Support** - Host multiple games simultaneously
- **Spectator Mode** - Watch ongoing games
- **Win Detection** - Automatic game ending when winner is determined

### 🎯 Multi-Gamemode System

The plugin supports multiple distinct gamemodes accessible through one plugin:

**🔥 Fireball** - Fire charges, sword, and tactical positioning
- 64x Fire Charges (fireballs)
- Diamond Armor (full protection)
- Diamond Sword for melee
- Golden Apples for healing
- Snowballs for utility
- Speed I enhancement

**⚔️ Classic PvP** - Pure sword-based combat
- Iron Armor (medium protection)
- Diamond Sword for melee combat
- Cooked Beef for sustain
- Golden Apples for healing
- Strength I (damage boost)

**🏹 Bow Duel** - Archery-focused combat
- Leather Armor (lightweight)
- Bows with unlimited arrows
- Stone Sword as backup
- Golden Apples for healing
- Speed II enhancement

### 🛠️ Admin Features
- **Arena Creation** - Create unlimited arenas with `/fireballwars arena`
- **Spawn Setup** - Set spawn points for each arena
- **Arena Management** - Enable/disable arenas
- **Gamemode Control** - Enable/disable gamemodes
- **Configuration System** - Customizable game settings
- **Auto-Setup** - Default arena created automatically

### 📊 Game Statistics
- Kill tracking per player
- Game state management
- Player count monitoring
- Customizable game duration

## 🚀 Installation & Auto-Setup

1. **Build the Plugin**
   ```bash
   mvn clean package
   ```

2. **Deploy to Server**
   - Copy `target/Fire-1.0-SNAPSHOT.jar` to `plugins/` directory
   - Restart server

3. **Auto-Setup** (Automatic on First Load)
   - Plugin automatically creates "Default" arena on first load
   - Arena spawns are set to placeholder locations
   - Configure spawn points: 
     ```
     /fireballwars arena setspawn1 Default
     /fireballwars arena setspawn2 Default
     ```

## 📋 Commands

### Player Commands

| Command | Usage | Description |
|---------|-------|-------------|
| `/fireballwars join [arena] [gamemode]` | `/ffight join` | Join a Fireball Fight game |
| `/fireballwars quit` | `/ffight quit` | Leave your current game |
| `/fireballwars list` | `/ffight list` | View arenas and active games |
| `/fireballwars gamemode` | `/ffight gamemode` | List available gamemodes |
| `/fireballwars stats` | `/ffight stats` | View game statistics |
| `/fireballwars start` | `/ffight start` | Force start game (if host) |

### Admin Commands

| Command | Description |
|---------|-------------|
| `/fireballwars arena create <name>` | Create new arena |
| `/fireballwars arena setspawn1 <name>` | Set first spawn point |
| `/fireballwars arena setspawn2 <name>` | Set second spawn point |
| `/fireballwars arena delete <name>` | Delete arena |
| `/fireballwars arena list` | List all arenas |

## 🎮 How to Play

### Joining a Game

**Join Default Gamemode (Fireball)**
```
/fireballwars join
```

**Join Specific Gamemode**
```
/fireballwars join Default Fireball
/fireballwars join Default ClassicPvP
/fireballwars join Default Bow
```

### Gameplay

1. **Queue** - Join a game with `/fireballwars join`
2. **Countdown** - 10-second countdown before game starts
3. **Spawn** - Teleported to your arena spawn point
4. **Kit** - Automatically receive gamemode-specific kit
5. **Battle** - Fight your opponent using gamemode mechanics
6. **Win** - Last player standing wins!

## 🔧 Permissions

- `fireballwars.admin` - Access to admin commands (default: OP)
- `fireballwars.play` - Permission to play Fireball Fight (default: true)

## 📁 Project Structure

```
com.orangecoder.fire/
├── Fire.java                    # Main plugin class
├── game/
│   ├── Arena.java              # Arena configuration
│   └── Game.java               # Game state & logic
├── gamemodes/
│   ├── Gamemode.java           # Abstract gamemode base
│   ├── FireballGamemode.java   # Fireball implementation
│   ├── ClassicPvPGamemode.java # PvP implementation
│   └── BowGamemode.java        # Bow implementation
├── kits/
│   └── Kit.java                # Equipment system
├── managers/
│   ├── GameManager.java        # Game creation & logic
│   ├── ArenaManager.java       # Arena management
│   └── GamemodeManager.java    # Gamemode registry
├── commands/
│   └── FireballCommand.java    # Command handling
├── listeners/
│   ├── GameListener.java       # Game events
│   └── PlayerListener.java     # Player events
└── utils/
    └── GameUtils.java          # Utility functions
```

## 🎨 Gamemode Architecture

### Creating Custom Gamemodes

Extend the `Gamemode` abstract class to create new combat styles:

```java
public class CustomGamemode extends Gamemode {
    public CustomGamemode() {
        super("CustomName", "Description", 2, 2);
    }

    @Override
    public Kit getKit() {
        // Return gamemode-specific kit
    }

    @Override
    public void onGameStart(Game game) {
        // Called when game starts
    }

    @Override
    public void onPlayerJoin(Game game, Player player) {
        // Called when player joins
    }

    @Override
    public void onPlayerEliminated(Game game, Player player) {
        // Called when player dies
    }

    @Override
    public void onGameTick(Game game) {
        // Called every tick
    }

    @Override
    public String getRules() {
        // Return gamemode rules
    }
}
```

Then register in `GamemodeManager`:

```java
registerGamemode(new CustomGamemode());
```

## ⚙️ Configuration

### Default Config Location
`plugins/Fire/config.yml`

### Configuration Options
```yaml
game:
  countdown-seconds: 10      # Time before game starts
  max-game-duration: 600     # Max game length (seconds)
  player-min: 2              # Minimum players to start
  player-max: 2              # Maximum players per game
```

### Arena Configuration
Arenas are auto-saved in config after creation:
```yaml
arenas:
  Default:
    spawn1: "world,0,100,0,0,0"
    spawn2: "world,10,100,0,180,0"
    min-players: 2
    max-players: 2
    enabled: true
```

## 🎯 Features Summary

✅ **Multiple Gamemodes** - Fireball, ClassicPvP, Bow  
✅ **Auto-Setup** - Default arena on first load  
✅ **1v1 PvP** - Intense battles  
✅ **Opponent Matchmaking** - Automatic player matching  
✅ **Arena System** - Multiple arenas support  
✅ **Kit System** - Gamemode-specific equipment  
✅ **Game Countdown** - 10-second start countdown  
✅ **Player Elimination** - Automatic detection  
✅ **Win Detection** - Smart game ending  
✅ **Stats Tracking** - Kill counts  
✅ **Spectator Mode** - Watch ongoing games  
✅ **Admin Commands** - Full server control  
✅ **Permissions** - Configurable access control  
✅ **Easy Configuration** - YAML config file  
✅ **Full Commands** - `/fireballwars` and `/ffight` aliases

## 🛠️ Building

### Requirements
- Java 16+
- Maven 3.6+
- Spigot/Paper 1.20.1+

### Build Command
```bash
mvn clean package
```

### Output
- JAR File: `target/Fire-1.0-SNAPSHOT.jar`
- Size: ~42KB

## 🎓 Advanced Usage

### Creating an Arena
```
/fireballwars arena create MyArena
/fireballwars arena setspawn1 MyArena
/fireballwars arena setspawn2 MyArena
```

### Viewing All Gamemodes
```
/fireballwars gamemode
```

### Joining Specific Gamemode
```
/fireballwars join MyArena Fireball
/fireballwars join MyArena ClassicPvP
/fireballwars join MyArena Bow
```

## 🐛 Troubleshooting

**Plugin doesn't load**
- Check console for errors
- Verify Java 16+ is installed
- Check `plugins/` directory permissions

**No arenas available**
- Creates "Default" arena on first load
- If missing, create: `/fireballwars arena create Default`

**Players can't join**
- Check if arena exists: `/fireballwars list`
- Verify spawn points are set
- Check player permissions

**Fireballs don't work (Fireball gamemode)**
- Right-click fire charges to shoot
- Must have fire charges in inventory
- Game must be in PLAYING state

**Commands not recognized**
- Check `/fireballwars` loads (check for error messages)
- Try `/ffight` as alias
- Verify permissions

## 📝 File Locations

| File | Location |
|------|----------|
| JAR | `target/Fire-1.0-SNAPSHOT.jar` |
| Config | `plugins/Fire/config.yml` |
| Plugin YML | `src/main/resources/plugin.yml` |

## 🔄 Version History

**1.0.0** - Initial Release
- ✅ Fireball gamemode with fire charges
- ✅ ClassicPvP gamemode with pure sword combat
- ✅ Bow gamemode with archery
- ✅ Auto-setup default arena
- ✅ Multiple arena support
- ✅ Full gamemode system
- ✅ Complete command set
- ✅ Admin management tools

## 📜 Plugin Manifest

**Name:** Fire  
**Version:** 1.0  
**Main Class:** `com.orangecoder.fire.Fire`  
**Description:** Fireball Fight Plugin with Multiple Gamemodes  
**Author:** OrangeCoder0  

## 💡 Tips

1. **Create Multiple Arenas** - Each arena is independent, host multiple games
2. **Custom Gamemodes** - Extend `Gamemode` class to create new combat styles
3. **Fast Setup** - Auto-creates default arena, just set spawn points
4. **Admin Friendly** - Full command-based management, no file editing needed
5. **Player Friendly** - Simple commands, clear feedback messages

## 📞 Support

For issues or customization needs:
1. Check troubleshooting section
2. Verify permissions are correct
3. Check console for error messages
4. Customize gamemodes as needed

---

**Created with ❤️ for Competitive Minecraft PvP!**

Enjoy intense 1v1 battles with the Fire Fireball Fight Plugin! 🔥⚔️🏹
