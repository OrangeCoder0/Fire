package com.orangecoder.fire.gamemodes;

import com.orangecoder.fire.game.Game;
import com.orangecoder.fire.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ClassicPvPGamemode extends Gamemode {

    public ClassicPvPGamemode() {
        super("ClassicPvP", "§ePure Sword Combat - Classic 1v1 Battle!", 2, 2);
    }

    @Override
    public Kit getKit() {
        Kit kit = new Kit("ClassicPvP");

        // Iron armor
        ItemStack[] armor = new ItemStack[4];
        armor[0] = Kit.createItem(Material.IRON_BOOTS, 1, "§7Iron Boots");
        armor[1] = Kit.createItem(Material.IRON_LEGGINGS, 1, "§7Iron Leggings");
        armor[2] = Kit.createItem(Material.IRON_CHESTPLATE, 1, "§7Iron Chestplate");
        armor[3] = Kit.createItem(Material.IRON_HELMET, 1, "§7Iron Helmet");

        ItemStack[] inventory = new ItemStack[36];
        inventory[0] = Kit.createItem(Material.DIAMOND_SWORD, 1, "§cDiamond Sword");
        inventory[1] = Kit.createItem(Material.GOLDEN_APPLE, 5, "§eGolden Apple");
        inventory[2] = Kit.createItem(Material.COOKED_BEEF, 32, "§bCooked Beef");

        kit.setArmor(armor);
        kit.setInventory(inventory);
        kit.setHealth(20.0);

        // Strength effect (damage boost)
        kit.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

        return kit;
    }

    @Override
    public void onGameStart(Game game) {
        for (Player p : game.getPlayers()) {
            p.sendMessage("§a════════ §eClassic PvP§a ════════");
            p.sendMessage("§eRules:");
            p.sendMessage("§e• Sword only combat");
            p.sendMessage("§e• Use golden apples to heal");
            p.sendMessage("§e• Strength I buff enabled");
            p.sendMessage("§e• Last player standing wins!");
            p.sendMessage("§e═══════════════════════════════");
        }
    }

    @Override
    public void onPlayerJoin(Game game, Player player) {
        player.sendMessage("§a✓ Welcome to §eClassic PvP§a!");
    }

    @Override
    public void onPlayerEliminated(Game game, Player player) {
        for (Player p : game.getPlayers()) {
            p.sendMessage("§c" + player.getName() + " fell in combat!");
        }
    }

    @Override
    public void onGameTick(Game game) {
        // No special ticks needed
    }

    @Override
    public String getRules() {
        return "§eClassic PvP - Pure sword combat, may the best warrior win!";
    }
}
