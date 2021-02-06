package main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import main.Explosion;
import main.Main;
import main.listener.ItemSaver;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Trash implements CommandExecutor{
	
	public void sortTrashItems() {
	
		int offset = 0;
		List<ItemStack> itemlist = new ArrayList<>();
		Main.main.getLogger().warning("cleared" + ItemSaver.trash_invs.size());
		ItemStack item;
		for(int iv = 0; iv < ItemSaver.trash_invs.size(); iv++) {
			Main.main.getLogger().warning(iv +  " iv");
			for(int i = 0; i < 54; i++) {
				item = ItemSaver.trash_invs.get(iv).getItem(i);
				if(item!=null) {
					Main.main.getLogger().warning(i +  " i §dset" + (offset+i));
					itemlist.add(item);
				}
			}
			offset=offset+54;
		}
		
		if(!itemlist.isEmpty()) {
			
			ItemSaver.trash_invs.clear();
			Inventory firstinv = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Mülleimer "+ChatColor.DARK_GRAY+"- Seite 1");
			ItemSaver.trash_invs.add(firstinv);
			
			for(int i=0; i < itemlist.size(); i++) {
				ItemSaver.saveItem(itemlist.get(i));
				/*for(int iv=0; iv < ItemSaver.trash_invs.size(); iv++) {
					if(ItemSaver.trash_invs.get(iv).addItem(itemlist.get(i)).isEmpty()) {
						iv = itemlist.size(); // Oder einfach eine größere Zahl
					}
					else {
						if(iv+1 == ItemSaver.trash_invs.size()) {
							Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Mülleimer "+ChatColor.DARK_GRAY+"- Seite "+(iv+2));
							ItemSaver.trash_invs.add(inv);
							
						}
					}
				}*/
			}
		}
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
							Main.main.getLogger().warning("NumberFormatException");
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
