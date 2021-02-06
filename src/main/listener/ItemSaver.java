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

public class ItemSaver implements Listener {
	
	public static List<Inventory> trash_invs = new ArrayList<>();
	
	public static void saveTrashItems() {
		
		ConfigurationSection section = Main.main.getConfig().getConfigurationSection("TrashItems");
		if(section!=null) {
			for(int iv = 0; iv < trash_invs.size(); iv++) {
				for(int i = 0; i < 54; i++) {
					section.set((iv*i)+"", trash_invs.get(iv).getItem(i));
				}
			}
		}
		else {
			Main.main.getConfig().createSection("TrashItems");
		}
	}
	
	static {
		ConfigurationSection section = Main.main.getConfig().getConfigurationSection("TrashItems");
		if(!section.getKeys(false).isEmpty()) {
			int keys = section.getKeys(false).size();
			
			int inv_keys = (int) Math.ceil(keys / 54);
			for(int iv = 0; iv < keys; iv=+54) {
				Main.main.getLogger().info(iv +  "Inventar");
				Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Mülleimer "+ChatColor.DARK_GRAY+"- Seite "+ (iv/54)+1);
				for(int i = 0; i < 54; i++) {
					Main.main.getLogger().info(i +  "Item");
					
					ItemStack itemstack = section.getItemStack((iv+i)+"");
					inv.setItem(i, itemstack);
				}
				trash_invs.add(inv);
			}
			Main.main.getLogger().info("Loaded TrashItems! " + keys + "/" + inv_keys);
			
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
					Inventory inv = Bukkit.createInventory(null, 54, "§1Mülleimer §8- Seite "+(i+1));
					trash_invs.add(inv);
				}
			}
		}
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent e) {
		saveItem(e.getEntity().getItemStack());
		
		Bukkit.broadcastMessage(
			"§e-- §7" + e.getEntity().getPickupDelay()
			+ " / " + e.getEntity().getLastDamageCause()
			+ " / " + e.getEntity().getOwner()
			+ " / " + e.getEntity().getThrower() + "\nItemDespawn §e--");
	}
	
	@EventHandler(ignoreCancelled = false)
	public void onItemDeath(EntityDeathEvent e) {
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
