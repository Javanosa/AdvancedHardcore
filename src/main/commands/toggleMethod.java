package main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.Explosion;
import main.Main;
import main.listener.ItemSaver;

public class toggleMethod implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if(p.isOp()) {
					if(Explosion.useold == 1) {
						Explosion.useold = 2;
						p.sendMessage("§eUseold = 2, benutze neue Methode VectorY+0.3");
					}
					else if(Explosion.useold == 2){
						Explosion.useold = 3;
						p.sendMessage("§eUseold = 3, benutze keine Methode");
					}
					else{
						Explosion.useold = 1;
						p.sendMessage("§eUseold = 1, benutze alte Methode Y+1");
					}
					
					int page = 0;
					if(args.length == 1)
						page = Integer.valueOf(args[0]);
					
					if(page < ItemSaver.trash_invs.size())
						p.openInventory(ItemSaver.trash_invs.get(page));
					p.sendMessage(ItemSaver.trash_invs.size()+"");
				}
			}
		
		return false;
	}
}
