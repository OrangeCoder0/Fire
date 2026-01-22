package com.orangecoder.fire.kits;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Kit {

    private String name;
    private ItemStack[] armor;
    private ItemStack[] inventory;
    private List<PotionEffect> effects;
    private double health;

    public Kit(String name) {
        this.name = name;
        this.effects = new ArrayList<>();
        this.health = 20.0;
    }

    public String getName() {
        return name;
    }

    public void setArmor(ItemStack[] armor) {
        this.armor = armor;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public void addEffect(PotionEffect effect) {
        effects.add(effect);
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void giveKit(Player player) {
        // Clear player inventory
        player.getInventory().clear();
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        // Set armor
        if (armor != null) {
            player.getInventory().setArmorContents(armor);
        }

        // Set inventory
        if (inventory != null) {
            player.getInventory().setContents(inventory);
        }

        // Add potion effects
        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }

        // Set health
        player.setHealth(Math.min(health, 20.0));
    }

    public static Kit getDefaultFireballKit() {
        Kit kit = new Kit("Fireball");

        // Create armor
        ItemStack[] armor = new ItemStack[4];
        armor[0] = createItem(Material.DIAMOND_BOOTS, 1, "Diamond Boots");
        armor[1] = createItem(Material.DIAMOND_LEGGINGS, 1, "Diamond Leggings");
        armor[2] = createItem(Material.DIAMOND_CHESTPLATE, 1, "Diamond Chestplate");
        armor[3] = createItem(Material.DIAMOND_HELMET, 1, "Diamond Helmet");

        // Create inventory
        ItemStack[] inventory = new ItemStack[36];
        inventory[0] = createItem(Material.FIRE_CHARGE, 64, "§cFireball");
        inventory[1] = createItem(Material.GOLDEN_APPLE, 3, "§cGolden Apple");
        inventory[2] = createItem(Material.DIAMOND_SWORD, 1, "§bDiamond Sword");
        inventory[3] = createItem(Material.SNOWBALL, 16, "§bSnowball");

        kit.setArmor(armor);
        kit.setInventory(inventory);
        kit.setHealth(20.0);

        // Add speed effect
        kit.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));

        return kit;
    }

    public static ItemStack createItem(Material material, int amount, String name) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
}
