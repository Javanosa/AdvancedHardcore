package main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Biomelist implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				
				
				
				p.sendMessage(
					"§a Beach\n"
					+ "Desert\n"
					+ "Desert_hills\n"
					+ "Extreme_hills\n"
					+ "Forest\n"
					+ "Forest_hills\n"
					+ "Frozen_Ocean\n"
					+ "Frozen_River\n"
					+ "§cHell§a\n"
					+ "Ice_Mountains\n"
					+ "Ice_Plains\n"
					+ "Jungle\n"
					+ "Jungle_Hills\n"
					+ "Mushroom_Island\n"
					+ "Mushroom_Shore\n"
					+ "Ocean\n"
					+ "Plains\n"
					+ "River\n"
					+ "§cSky§a\n"
					+ "Small_Mountains\n"
					+ "Swampland\n"
					+ "Taiga\n"
					+ "Taiga_Hills\n"
				);
			}
		
		return false;
	}
}
