package main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import main.Main;
import main.listener.ItemSaver;
import main.listener.ItemSaverNew;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Trash implements CommandExecutor{
	
	public void sortTrashItems() {
	
		int offset = 0;
		List<ItemStack> itemlist = new ArrayList<>();
		ItemStack item;
		for(int iv = 0; iv < ItemSaver.trash_invs.size(); iv++) {
			for(int i = 0; i < 54; i++) {
				item = ItemSaver.trash_invs.get(iv).getItem(i);
				if(item!=null) {
					itemlist.add(item);
				}
			}
			offset=offset+54;
		}
		
		if(!itemlist.isEmpty()) {
			
			ItemSaver.trash_invs.clear();
			Inventory firstinv = Bukkit.createInventory(null, 54, "§1Mülleimer §8- Seite 1");
			ItemSaver.trash_invs.add(firstinv);
			
			for(int i=0; i < itemlist.size(); i++) {
				ItemSaver.saveItem(itemlist.get(i));
			}
		}
		
		Main.log.info("§bTrash -> §7sorted "+ itemlist.size() + " Itemstacks");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if(p.isOp()) {
					int page = 1;
					Boolean openinv = true;
					String message = null;
					if(args.length == 1) {
						String string = args[0];
						
						if(string.equalsIgnoreCase("clear")) {
							openinv = false;
							ItemSaver.trash_invs.clear();
							ItemSaver.trash_invs.add(Bukkit.createInventory(null, 54, "§1Mülleimer §8- Seite 1"));
							Main.log.warning("Cleared all Trashinvs");
							message = "§bMülleimer geleert.";
						}
						else if(string.equalsIgnoreCase("sort")) {
							openinv = false;
							sortTrashItems();
							message = "§bMülleimer sortiert.";
						}
						else try{
							page = Integer.valueOf(args[0]);
						} catch(NumberFormatException ec) {
							Main.log.warning("NumberFormatException");
						}
						if(page > ItemSaver.trash_invs.size() || page < 1)
							page = 1;
					}
					if(openinv) {
						message = "§bSeite "+page+" / "+ItemSaver.trash_invs.size();
						p.openInventory(ItemSaver.trash_invs.get(page - 1));
					}
					
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
				}
			}
		
		return false;
	}
}
