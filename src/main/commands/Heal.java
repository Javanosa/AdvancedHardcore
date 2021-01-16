package main.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Heal implements CommandExecutor{
	
	Map<ItemStack, Inventory> craftings = new HashMap<>();
	
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
				}
			}
		
		return false;
	}
}
