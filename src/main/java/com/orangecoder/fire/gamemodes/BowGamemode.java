package com.orangecoder.fire.gamemodes;

import com.orangecoder.fire.game.Game;
import com.orangecoder.fire.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BowGamemode extends Gamemode {

    public BowGamemode() {
        super("Bow", "§6Archery Duel - Use bows to eliminate your opponent!", 2, 2);
    }

    @Override
    public Kit getKit() {
        Kit kit = new Kit("Bow");

        // Leather armor (fast, less tanky)
        ItemStack[] armor = new ItemStack[4];
        armor[0] = Kit.createItem(Material.LEATHER_BOOTS, 1, "§6Leather Boots");
        armor[1] = Kit.createItem(Material.LEATHER_LEGGINGS, 1, "§6Leather Leggings");
        armor[2] = Kit.createItem(Material.LEATHER_CHESTPLATE, 1, "§6Leather Chestplate");
        armor[3] = Kit.createItem(Material.LEATHER_HELMET, 1, "§6Leather Helmet");

        ItemStack[] inventory = new ItemStack[36];
        inventory[0] = Kit.createItem(Material.BOW, 1, "§6Bow");
        inventory[1] = Kit.createItem(Material.ARROW, 64, "§fArrows");
        inventory[2] = Kit.createItem(Material.GOLDEN_APPLE, 2, "§eGolden Apple");
        inventory[3] = Kit.createItem(Material.STONE_SWORD, 1, "§7Stone Sword");

        kit.setArmor(armor);
        kit.setInventory(inventory);
        kit.setHealth(20.0);

        kit.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));

        return kit;
    }

    @Override
    public void onGameStart(Game game) {
        for (Player p : game.getPlayers()) {
            p.sendMessage("§a════════ §6Bow Duel§a ════════");
            p.sendMessage("§eRules:");
            p.sendMessage("§e• Use bows to shoot arrows");
            p.sendMessage("§e• Speed II buff enabled");
            p.sendMessage("§e• Melee sword as backup");
            p.sendMessage("§e• Last player standing wins!");
            p.sendMessage("§e════════════════════════════════");
        }
    }

    @Override
    public void onPlayerJoin(Game game, Player player) {
        player.sendMessage("§a✓ Welcome to §6Bow Duel§a!");
    }

    @Override
    public void onPlayerEliminated(Game game, Player player) {
        for (Player p : game.getPlayers()) {
            p.sendMessage("§c" + player.getName() + " was shot down!");
        }
    }

    @Override
    public void onGameTick(Game game) {
        // No special ticks needed
    }

    @Override
    public String getRules() {
        return "§6Bow Duel - Master the bow and take down your opponent!";
    }
}
