package com.orangecoder.fire.gamemodes;

import com.orangecoder.fire.game.Game;
import com.orangecoder.fire.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FireballGamemode extends Gamemode {

    public FireballGamemode() {
        super("Fireball", "§cFireball Fight - Shoot fireballs at your opponent!", 2, 2);
    }

    @Override
    public Kit getKit() {
        Kit kit = new Kit("Fireball");

        // Diamond armor
        ItemStack[] armor = new ItemStack[4];
        armor[0] = Kit.createItem(Material.DIAMOND_BOOTS, 1, "§bDiamond Boots");
        armor[1] = Kit.createItem(Material.DIAMOND_LEGGINGS, 1, "§bDiamond Leggings");
        armor[2] = Kit.createItem(Material.DIAMOND_CHESTPLATE, 1, "§bDiamond Chestplate");
        armor[3] = Kit.createItem(Material.DIAMOND_HELMET, 1, "§bDiamond Helmet");

        ItemStack[] inventory = new ItemStack[36];
        inventory[0] = Kit.createItem(Material.FIRE_CHARGE, 64, "§cFireball");
        inventory[1] = Kit.createItem(Material.GOLDEN_APPLE, 3, "§eGolden Apple");
        inventory[2] = Kit.createItem(Material.DIAMOND_SWORD, 1, "§bDiamond Sword");
        inventory[3] = Kit.createItem(Material.SNOWBALL, 16, "§bSnowball");

        kit.setArmor(armor);
        kit.setInventory(inventory);
        kit.setHealth(20.0);

        kit.addEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false
        ));

        return kit;
    }

    @Override
    public void onGameStart(Game game) {
        // Broadcast game rules
        for (Player p : game.getPlayers()) {
            p.sendMessage("§a════════ §cFireball Fight§a ════════");
            p.sendMessage("§eRules:");
            p.sendMessage("§e• Use fire charges to shoot fireballs");
            p.sendMessage("§e• Last player standing wins!");
            p.sendMessage("§e════════════════════════════════════");
        }
    }

    @Override
    public void onPlayerJoin(Game game, Player player) {
        player.sendMessage("§a✓ Welcome to §cFireball Fight§a!");
    }

    @Override
    public void onPlayerEliminated(Game game, Player player) {
        for (Player p : game.getPlayers()) {
            p.sendMessage("§c" + player.getName() + " has been eliminated!");
        }
    }

    @Override
    public void onGameTick(Game game) {
        // No special ticks needed
    }

    @Override
    public String getRules() {
        return "§cFireball Fight - Shoot your opponent with fireballs to win!";
    }
}
