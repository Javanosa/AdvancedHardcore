package main.listener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import main.Main;
import net.md_5.bungee.api.ChatColor;

public class ItemSaverNew implements Listener {
	
	public static List<Inventory> trash_invs = new ArrayList<>();
	
	public static void saveTrashItems() {
		
		
		ConfigurationSection section = Main.main.getConfig().createSection("TrashItems");
		int offset = 0;
		List<ItemStack> itemlist = new ArrayList<>();
		ItemStack item;
		for(int iv = 0; iv < trash_invs.size(); iv++) {
			Main.main.getLogger().warning(iv +  " iv");
			for(int i = 0; i < 54; i++) {
				item = trash_invs.get(iv).getItem(i);
				if(item!=null) {
					Main.main.getLogger().warning(i +  " i §dset" + (offset+i));
					itemlist.add(item);
				}
			}
			offset=offset+54;
		}
		section.set("All",itemlist);
	}
	
	static {
		ConfigurationSection section = Main.main.getConfig().getConfigurationSection("TrashItems");
		
		if(section==null) {
			section = Main.main.getConfig().createSection("TrashItems");
			section.set("All", new ArrayList<>());
		}
		
		List<?> itemlist = section.getList("All");
		
		if(!itemlist.isEmpty()) {
			
			int keys = itemlist.size();
			
			int inv_keys = (int) Math.ceil(keys / 54);
			
			Main.main.getLogger().info(inv_keys +  " inv_keys");
			for(int iv = 0; iv < keys; iv=iv+54) {
				Main.main.getLogger().info(iv +  " iv");
				Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Mülleimer "+ChatColor.DARK_GRAY+"- Seite "+ Math.round(Math.ceil(iv/54)+1));
				for(int i = 0; i < 54; i++) {
					Main.main.getLogger().info(i +  "Item");
					
					ItemStack itemstack = (ItemStack) itemlist.get((iv+i));
					inv.setItem(i, itemstack);
				}
				trash_invs.add(inv);
				
			}
			//Main.main.getLogger().info("Loaded TrashItems! " + keys + "/" + inv_keys);
			
		}
		else {
			Main.main.getLogger().warning("No TrashItems found!");
			
			Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Mülleimer "+ChatColor.DARK_GRAY+"- Seite "+1);
			trash_invs.add(inv);
			
		}
	}
	
	public void saveItem(ItemStack itemstack) {
		for(int i=0; i < trash_invs.size(); i++) {
			Bukkit.broadcastMessage("Empty: "+trash_invs.get(i).addItem(itemstack).isEmpty());
			
			if(trash_invs.get(i).addItem(itemstack).isEmpty()) {
				i = trash_invs.size(); // Oder einfach eine größere Zahl
			}
			else {
				if(i+1 == trash_invs.size()) {
					Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Mülleimer "+ChatColor.DARK_GRAY+"- Seite "+(i+2));
					//trash_invs.add(inv);
				}
			}
		}
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent e) {
		
		
		if(!e.isCancelled()) {
			saveItem(e.getEntity().getItemStack());
			Bukkit.broadcastMessage(
			"§e-- §7" + e.getEntity().getPickupDelay()
			+ " / " + e.getEntity().getLastDamageCause()
			+ " / " + e.getEntity().getOwner()
			+ " / " + e.getEntity().getThrower() + "\nItemDespawn §e--" + e.isCancelled() + "c");
		}
	}
	
	@EventHandler(ignoreCancelled = false)
	public void onItemDeath(EntityDeathEvent e) {
		Bukkit.broadcastMessage(e.getEntityType()+" type");
		if(e.getEntityType().equals(EntityType.DROPPED_ITEM)) {
			saveItem(((Item) e.getEntity()).getItemStack());
			Bukkit.broadcastMessage("§eItemDeath");
		}
	}
	
	@EventHandler
	public void onPlayerItemDamage(PlayerItemDamageEvent e) {
		saveItem(e.getItem());
		Bukkit.broadcastMessage("§ePlayerItemDamage");
	}
	
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent e) {
		saveItem(e.getBrokenItem());
		Bukkit.broadcastMessage("§ePlayerItemBreak");
	}
	
	
	
}
