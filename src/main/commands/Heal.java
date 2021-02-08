package main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

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
					/*SkeletonHorse sh = (SkeletonHorse) p.getWorld().spawnEntity(p.getLocation(), EntityType.SKELETON_HORSE);
					sh.setRemoveWhenFarAway(true);
					sh.setTamed(true);
					net.minecraft.server.v1_16_R3.EntityHorseSkeleton skeletonhorse = (EntityHorseSkeleton) ((CraftSkeletonHorse) sh).getHandle();
					// SkeletonTrap = true
					skeletonhorse.t(true);*/
				}
			}
		
		return false;
	}
}
