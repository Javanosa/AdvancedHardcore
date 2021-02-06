package main.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import main.Explosion;

public class Blockdata implements CommandExecutor{
	
	public static boolean blockdata = false;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
					if(blockdata) {
						blockdata = false;
						p.sendMessage("§eBlockdata InspectModus aus");
					}
					else{
						blockdata = true;
						p.sendMessage("§eBlockdata InspectModus an");
					}
			}
		
		return false;
	}
}
