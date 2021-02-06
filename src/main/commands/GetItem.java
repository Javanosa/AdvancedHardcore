package main.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.Main;
import main.Recipes;

public class GetItem implements CommandExecutor{
	
	public static ItemStack superblock = new ItemStack(Material.SANDSTONE);
	static{ItemMeta meta = superblock.getItemMeta();
		meta.setDisplayName("§cUnzerstörbar");
		superblock.setItemMeta(meta);
	}

	public static ItemStack armor_helmet = new ItemStack(Material.NETHERITE_HELMET);
	static{ItemMeta meta = armor_helmet.getItemMeta();
		meta.setDisplayName("§bSuperHelm");
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		meta.addEnchant(Enchantment.THORNS, 10, true);
		meta.addEnchant(Enchantment.DURABILITY, 10, true);
		meta.addEnchant(Enchantment.WATER_WORKER, 1, true); // 1
		meta.addEnchant(Enchantment.OXYGEN, 10, true);
		armor_helmet.setItemMeta(meta);
	}
	
	public static ItemStack armor_chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
	
	static{ItemMeta meta = armor_chestplate.getItemMeta();
		meta.setDisplayName("§bSuperBrustpanzer");
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		meta.addEnchant(Enchantment.THORNS, 10, true);
		meta.addEnchant(Enchantment.DURABILITY, 10, true);
		armor_chestplate.setItemMeta(meta);
	}
	
	public static ItemStack armor_leggins = new ItemStack(Material.NETHERITE_LEGGINGS);
	
	static{ItemMeta meta = armor_leggins.getItemMeta();
		meta.setDisplayName("§bSuperBeinschutz");
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		meta.addEnchant(Enchantment.THORNS, 10, true);
		meta.addEnchant(Enchantment.DURABILITY, 10, true);
		armor_leggins.setItemMeta(meta);
	}
	
	
	public static ItemStack armor_boots = new ItemStack(Material.NETHERITE_BOOTS);
	static{ItemMeta meta = armor_boots.getItemMeta();
		meta.setDisplayName("§bSuperStiefel");
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		meta.addEnchant(Enchantment.THORNS, 10, true);
		meta.addEnchant(Enchantment.DURABILITY, 10, true);
		meta.addEnchant(Enchantment.PROTECTION_FALL, 10, true);
		armor_boots.setItemMeta(meta);
	}
	
	public static ItemStack super_weapon = new ItemStack(Material.NETHERITE_AXE);
	static{ItemMeta meta = super_weapon.getItemMeta();
		meta.setDisplayName("§bSuperwaffe");
		meta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
		meta.addEnchant(Enchantment.FIRE_ASPECT, 10, true);
		meta.addEnchant(Enchantment.DURABILITY, 10, true);
		meta.addEnchant(Enchantment.KNOCKBACK, 5, true);
		super_weapon.setItemMeta(meta);
	}
	
	public static ItemStack[] superarmor = {armor_boots, armor_leggins, armor_chestplate, armor_helmet};
	
	public static ItemStack superbow = new ItemStack(Material.BOW);
	static{ItemMeta meta = superbow.getItemMeta();
		meta.setDisplayName("§cExplosiverBogen");
		meta.addEnchant(Enchantment.DURABILITY, 10, true);
		meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setLocalizedName("§cExplosiverBogen");
		meta.setUnbreakable(true);
		superbow.setItemMeta(meta);
	}
	
	public static ItemStack bow_bomb = new ItemStack(Material.TNT);
	static{ItemMeta meta = bow_bomb.getItemMeta();
		meta.setDisplayName("§5FeuerBombe");
		meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		bow_bomb.setItemMeta(meta);
	}
	
	public static ItemStack bow_rocket = new ItemStack(Material.FIREWORK_ROCKET);
	static{ItemMeta meta = bow_rocket.getItemMeta();
		meta.setDisplayName("§5Rakete");
		meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		bow_rocket.setItemMeta(meta);
	}
	
	public static ItemStack bow_nuklear = new ItemStack(Material.FIRE_CHARGE);
	static{ItemMeta meta = bow_nuklear.getItemMeta();
		meta.setDisplayName("§5NuklearRakete");
		meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		bow_nuklear.setItemMeta(meta);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if(p.isOp()) {
					if(args.length >= 1) {
						Inventory inv = p.getInventory();
						switch(args[0]) {
							case "superblock":
								inv.addItem(superblock);
								break;
							case "superbow":
								inv.addItem(superbow);
								inv.addItem(bow_nuklear);
								inv.addItem(bow_rocket);
								inv.addItem(bow_bomb);
								inv.addItem(new ItemStack(Material.ARROW));
								break;
							case "superarmor":
								p.getEquipment().setArmorContents(superarmor);
								inv.addItem(super_weapon);
								break;
							case "recipes":
								inv.setContents(Recipes.recipe_items);
								break;
								
								
						}
					}
					else {
						p.sendMessage(" §7§o/get (superblock/superarmor/superbow)");
					}
				}
			}
		
		return false;
	}
}
