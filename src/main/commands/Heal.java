package main.commands;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftSkeleton;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftSkeletonHorse;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import main.Explosion;
import main.Main;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityHorseSkeleton;
import net.minecraft.server.v1_16_R3.EntitySkeleton;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.NBTTagCompound;

public class Heal implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if(p.isOp()) {
					p.setHealthScale(20);
					p.setSaturation(20);
					p.setHealth(20);
					p.setFireTicks(0);
					p.setFoodLevel(20);
					
					p.removePotionEffect(PotionEffectType.WITHER);
					p.removePotionEffect(PotionEffectType.POISON);
					p.removePotionEffect(PotionEffectType.BLINDNESS);
					p.removePotionEffect(PotionEffectType.WEAKNESS);
					p.removePotionEffect(PotionEffectType.SLOW);
					p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
					p.removePotionEffect(PotionEffectType.LEVITATION);
					p.sendMessage("§7Du wurdest geheilt.");
					
					
					// Spawne SkeletonHorse zu debug Zwecken:
					SkeletonHorse sh = (SkeletonHorse) p.getWorld().spawnEntity(p.getLocation(), EntityType.SKELETON_HORSE);
					sh.setRemoveWhenFarAway(true);
					sh.setTamed(true);
					net.minecraft.server.v1_16_R3.EntityHorseSkeleton skeletonhorse = (EntityHorseSkeleton) ((CraftSkeletonHorse) sh).getHandle();
					// SkeletonTrap = true
					skeletonhorse.t(true);
				}
			}
		
		return false;
	}
}
