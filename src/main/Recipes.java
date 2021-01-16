package main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class Recipes {
	public static ItemStack[] recipe_items = {};
	
	public static ItemStack[] register_recipes() {
		ItemStack firebomb = new ItemStack(Material.SPLASH_POTION);
		PotionMeta meta = (PotionMeta) firebomb.getItemMeta();
		meta.setDisplayName("§c§lMolotowcocktail");
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		meta.setColor(Color.ORANGE);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Setzt alle Mobs und Blöcke im Umkreis von");
		lore.add("§74 Blöcken in Brand.");
		meta.setLore(lore);
		
		meta.setLocalizedName("§c§lMolotowcocktail");
		
		firebomb.setItemMeta(meta);
		
		NamespacedKey firebomb_key = new NamespacedKey(Main.main, "firebomb");
		ShapedRecipe firebomb_recipe = new ShapedRecipe(firebomb_key, firebomb);
		firebomb_recipe.shape(" A ","DBD"," C ");
		firebomb_recipe.setIngredient('A', Material.BLAZE_POWDER);
		firebomb_recipe.setIngredient('B', Material.GUNPOWDER);
		firebomb_recipe.setIngredient('C', Material.NETHERRACK);
		firebomb_recipe.setIngredient('D', Material.CHARCOAL);
		Bukkit.addRecipe(firebomb_recipe);
		
		
		
		ItemStack icebomb = new ItemStack(Material.SNOWBALL);
		ItemMeta meta2 = icebomb.getItemMeta();
		meta2.setDisplayName("§b§lEisbombe");
		meta2.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add("§7Friert alle Monster und Blöcke im Umkreis von");
		lore2.add("§78 Blöcken ein.");
		lore2.add("§cAchtung: Kann Tiere töten!");
		meta2.setLore(lore2);
		
		meta2.setLocalizedName("§b§lEisbombe");
		
		icebomb.setItemMeta(meta2);
		
		NamespacedKey icebomb_key = new NamespacedKey(Main.main, "icebomb");
		ShapedRecipe icebomb_recipe = new ShapedRecipe(icebomb_key, icebomb);
		icebomb_recipe.shape("AAA","ABA","ACA");
		icebomb_recipe.setIngredient('A', Material.ICE);
		icebomb_recipe.setIngredient('B', Material.GUNPOWDER);
		icebomb_recipe.setIngredient('C', Material.WATER_BUCKET);
		Bukkit.addRecipe(icebomb_recipe);
		
		
		
		ItemStack mobtrap = new ItemStack(Material.IRON_BARS);
		ItemMeta meta3 = mobtrap.getItemMeta();
		meta3.setDisplayName("§7§lMobfalle");
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add("§7Sperrt alle Mobs im Umkreis von 10 Blöcken");
		lore3.add("§7in Käfige.");
		lore3.add("§cFunktioniert am Besten auf freiem Gelände.");
		meta3.setLore(lore3);
		meta3.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta3.setLocalizedName("§7§lMobfalle");
		
		mobtrap.setItemMeta(meta3);
		
		NamespacedKey mobtrap_key = new NamespacedKey(Main.main, "mobtrap");
		ShapedRecipe mobtrap_recipe = new ShapedRecipe(mobtrap_key, mobtrap);
		mobtrap_recipe.shape("AAA","DCD","BBB");
		mobtrap_recipe.setIngredient('B', Material.IRON_INGOT);
		mobtrap_recipe.setIngredient('C', Material.GUNPOWDER);
		mobtrap_recipe.setIngredient('A', Material.ANDESITE);
		mobtrap_recipe.setIngredient('D', Material.OAK_LOG);
		Bukkit.addRecipe(mobtrap_recipe);
		
		
		
		ItemStack waterbomb = new ItemStack(Material.SPLASH_POTION);
		PotionMeta meta4 = (PotionMeta) waterbomb.getItemMeta();
		meta4.setDisplayName("§3§lWasserbombe");
		meta4.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta4.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta4.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		meta4.setColor(Color.TEAL);
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add("§7Löscht brennende Entitys und Blöcke im");
		lore4.add("§7Umkreis von 6 Blöcken.");
		meta4.setLore(lore4);
		meta4.setLocalizedName("§3§lWasserbombe");
		
		waterbomb.setItemMeta(meta4);
		
		NamespacedKey waterbomb_key = new NamespacedKey(Main.main, "waterbomb");
		ShapedRecipe waterbomb_recipe = new ShapedRecipe(waterbomb_key, waterbomb);
		waterbomb_recipe.shape("AAA","ABA","DCD");
		waterbomb_recipe.setIngredient('A', Material.GLASS);
		waterbomb_recipe.setIngredient('B', Material.GUNPOWDER);
		waterbomb_recipe.setIngredient('C', Material.WATER_BUCKET);
		waterbomb_recipe.setIngredient('D', Material.CLAY_BALL);
		Bukkit.addRecipe(waterbomb_recipe);
		
		
		
		ItemStack implosion = new ItemStack(Material.EGG);
		ItemMeta meta6 = implosion.getItemMeta();
		meta6.setDisplayName("§6§lImplosion");
		ArrayList<String> lore6 = new ArrayList<String>();
		lore6.add("§7Zieht Mobs im Umkreis von 8 Blöcken zu");
		lore6.add("§7einem bestimmten Punkt.");
		meta6.setLore(lore6);
		meta6.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta6.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta6.setLocalizedName("§6§lImplosion");
		
		implosion.setItemMeta(meta6);
		implosion.setAmount(2);
		
		NamespacedKey implosion_key = new NamespacedKey(Main.main, "implosion");
		ShapedRecipe implosion_recipe = new ShapedRecipe(implosion_key, implosion);
		implosion_recipe.shape("BA ","ACA"," AB");
		implosion_recipe.setIngredient('B', Material.REDSTONE);
		implosion_recipe.setIngredient('C', Material.GUNPOWDER);
		implosion_recipe.setIngredient('A', Material.ORANGE_CONCRETE);
		Bukkit.addRecipe(implosion_recipe);
		
		
		ItemStack explosion = new ItemStack(Material.EGG);
		ItemMeta meta5 = explosion.getItemMeta();
		meta5.setDisplayName("§e§lDruckwelle");
		meta5.setLocalizedName("");
		ArrayList<String> lore5 = new ArrayList<String>();
		lore5.add("§7Erzeugt eine heftige Druckwelle, welche");
		lore5.add("§7Mobs im Umkreis von 8 Blöcken wegschleudert.");
		meta5.setLore(lore5);
		meta5.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta5.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta5.setLocalizedName("§e§lDruckwelle");
		
		explosion.setItemMeta(meta5);
		explosion.setAmount(2);
		
		NamespacedKey explosion_key = new NamespacedKey(Main.main, "explosion");
		ShapedRecipe explosion_recipe = new ShapedRecipe(explosion_key, explosion);
		explosion_recipe.shape("BA ","ACA"," AB");
		explosion_recipe.setIngredient('B', Material.REDSTONE);
		explosion_recipe.setIngredient('C', Material.GUNPOWDER);
		explosion_recipe.setIngredient('A', Material.YELLOW_CONCRETE);
		Bukkit.addRecipe(explosion_recipe);
		
		
		ItemStack spiderbomb = new ItemStack(Material.SPIDER_SPAWN_EGG);
		ItemMeta meta7 = spiderbomb.getItemMeta();
		meta7.setDisplayName("§5§lSpinnenfalle");
		ArrayList<String> lore7 = new ArrayList<String>();
		lore7.add("§7Spawnt einige Spinnen welche die Monster einweben");
		lore7.add("§7und angreifen.");
		lore7.add("§cFunktioniert am Besten auf freiem Gelände.");
		lore7.add("§4Vorsicht §cvor Creepern!");
		meta7.setLore(lore7);
		meta7.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta7.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta7.setLocalizedName("§5§lSpinnenfalle");
		
		spiderbomb.setItemMeta(meta7);
		
		NamespacedKey spiderbomb_key = new NamespacedKey(Main.main, "spidertrap");
		ShapedRecipe spiderbomb_recipe = new ShapedRecipe(spiderbomb_key, spiderbomb);
		spiderbomb_recipe.shape("BBB","ACA","DDD");
		spiderbomb_recipe.setIngredient('B', Material.STRING);
		spiderbomb_recipe.setIngredient('C', Material.SPIDER_EYE);
		spiderbomb_recipe.setIngredient('A', Material.COBWEB);
		spiderbomb_recipe.setIngredient('D', Material.WHITE_WOOL);
		Bukkit.addRecipe(spiderbomb_recipe);
		
		
		ItemStack[] items = {firebomb, spiderbomb, mobtrap, waterbomb, implosion, explosion, icebomb};
		
		return items;
		
		
		
	}
}
