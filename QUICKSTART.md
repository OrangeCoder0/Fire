# Fire - Quick Start Guide

## Setup in 5 Minutes

### Step 1: Build the Plugin
```bash
cd /workspaces/Fire
mvn clean package
```
✅ JAR file created at: `target/Fire-1.0-SNAPSHOT.jar`

### Step 2: Copy to Server
```bash
cp target/Fire-1.0-SNAPSHOT.jar /path/to/your/server/plugins/
```

### Step 3: Restart Server
Restart your Minecraft server to load the plugin.

### Step 4: Create Your First Arena
Run these commands as an admin in-game:

```
/fireballwars arena create arena1
/fireballwars arena setspawn1 arena1
/fireballwars arena setspawn2 arena1
```

Position yourself at the first player spawn, then run the setspawn1 command.
Position yourself at the second player spawn, then run the setspawn2 command.

### Step 5: Play!
```
/fireballwars join arena1
/fireballwars join arena1  (second player joins)
```

Game starts automatically with 2 players!

---

## Essential Commands

### For Players
| Command | Purpose |
|---------|---------|
| `/fireballwars` | Get help |
| `/fireballwars join` | Join a game |
| `/fireballwars quit` | Leave game |
| `/fireballwars list` | See arenas and games |
| `/fireballwars stats` | View kills |

### For Admins
| Command | Purpose |
|---------|---------|
| `/fireballwars arena create <name>` | Create arena |
| `/fireballwars arena setspawn1 <name>` | Set spawn 1 |
| `/fireballwars arena setspawn2 <name>` | Set spawn 2 |
| `/fireballwars arena list` | See all arenas |
| `/fireballwars arena delete <name>` | Delete arena |

---

## What Players Get

When they join a game:
- ⚔️ Diamond sword
- 🔥 64 Fire charges (right-click to fire)
- ❄️ 16 Snowballs
- 🍎 3 Golden apples
- 🛡️ Full diamond armor
- ⚡ Speed boost throughout game

---

## How to Win

1. Join a game
2. Opponent joins (game auto-starts with 2 players)
3. Spawn at your location
4. Defeat opponent before they defeat you
5. Last player alive = winner!

**Tips:**
- Right-click with fire charges to shoot fireballs
- Eat golden apples to heal
- Use snowballs to slow down opponents
- Use your diamond sword for close combat

---

## Default Configuration

Located at: `plugins/Fire/config.yml`

```yaml
game:
  countdown-seconds: 10      # Wait time before game starts
  max-game-duration: 600     # Max game length (10 minutes)
  player-min: 2              # Players needed to start
  player-max: 2              # Players per game (1v1)
```

---

## Troubleshooting

**Plugin won't load?**
- Check that you have Java 16+ installed
- Verify jar is in plugins folder
- Check console for errors

**Can't create arena?**
- Make sure you're OP (admin)
- Try: `/op yourname` in console

**Fireballs won't work?**
- Must use Fire Charges (given in kit)
- Right-click to shoot (not left-click)
- Game must be PLAYING

**Players keep dying to fall damage?**
- This is expected - position spawns on flat ground

---

## Need Help?

Check the full README.md for:
- Architecture details
- Advanced configuration
- Custom kit setup
- Per-permission setup

Or modify any Java files to customize the plugin!

---

**Your Fireball Fight plugin is ready! Have fun! 🔥**
