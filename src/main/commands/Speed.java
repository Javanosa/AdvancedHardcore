package main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Speed implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				
				if(p.isOp()) {
				
					float speed;
					
					if(args.length==2) {
						speed = Float.parseFloat(args[1]) / 10;
						String type = args[0];
						if(type.equals("fly")) {
							Float f1 = p.getFlySpeed();
							p.setFlySpeed(speed);
							Float f2 = p.getFlySpeed();
							p.sendMessage("§7Set Flyspeed from §e"+f1+" §7to §6"+f2);
							
						}
						else if(type.equals("go")){
							Float g1 = p.getWalkSpeed();
							p.setWalkSpeed(speed);
							Float g2 = p.getWalkSpeed();
							p.sendMessage("§7Set Walkspeed from §e"+g1+" §7to §6"+g2);
						}
						else {
							p.sendMessage("§cUngültiger Geschwindigkeitstype!");
						}
					}
					else if(args.length==1){
						speed = Float.parseFloat(args[0]) / 10;
						if(p.isFlying()) {
							Float f1 = p.getFlySpeed();
							p.setFlySpeed(speed);
							Float f2 = p.getFlySpeed();
							p.sendMessage("§7Set Flyspeed from §e"+f1+" §7to §6"+f2);
						}
						else {
							Float g1 = p.getWalkSpeed();
							p.setWalkSpeed(speed);
							Float g2 = p.getWalkSpeed();
							p.sendMessage("§7Set Walkspeed from §e"+g1+" §7to §6"+g2);
						}
					}
					else {
						p.sendMessage(" §7§o/speed (go/fly) <float>\n§6§oDefault Speed: Go 2 / Fly 1");
					}
				}
			}
		
		return false;
	}
}
