package main.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
					if(p.isOp()) {
						
						if(args.length == 1) {
							Player target = Bukkit.getPlayer(args[0]);
							if(target != null) {
								if (target.getAllowFlight()) {
									target.setAllowFlight(false);
									p.sendMessage("§7Fliegen §caus§7 für "+args[0]+".");
									target.sendMessage("§7Fliegen §caus§7.");
								} else {
									target.setAllowFlight(true);
									p.sendMessage("§7Fliegen §aan§7 für "+args[0]+".");
									target.sendMessage("§7Fliegen §aan§7.");
								}
							}
							else {
								p.sendMessage("§cSpieler nicht gefunden");
							}
						}
						else {
							if (p.getAllowFlight()) {
								p.setAllowFlight(false);
								p.sendMessage("§7Fliegen §caus§7.");
							} else {
								p.setAllowFlight(true);
								p.sendMessage("§7Fliegen §aan§7.");
							}
						}
					}
			}
		
		return false;
	}
}
