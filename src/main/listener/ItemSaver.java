package main.listener;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
	
	static {
		
		ConfigurationSection section = Main.main.getConfig().getConfigurationSection("TrashItems");
		
		if(section == null) {
			section = Main.main.getConfig().createSection("TrashItems");
			section.set("All", new ArrayList<ItemStack>());
		}
		
		@SuppressWarnings("unchecked")
		List<ItemStack> itemlist = (List<ItemStack>) section.getList("All");
		
		if(!itemlist.isEmpty()) {
			
			int keys = itemlist.size();
			int inv_counter = 1;
			
			Main.main.getLogger().info(keys +  " inv_keys");
			for (int iv = 0; iv < keys; iv++) {
				Main.main.getLogger().info(iv +  " iv");
				Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Mülleimer " + ChatColor.DARK_GRAY + "- Seite " + inv_counter++);
				for (int i = 0; i < 54 && itemlist.size() < iv+i; i++) {
					Main.main.getLogger().info(i +  "Item");
					inv.setItem(i, itemlist.get(iv+i));
				}
				trash_invs.add(inv);
				
			}
			//Main.main.getLogger().info("Loaded TrashItems! " + keys + "/" + inv_keys);
			
		}
		else {
			Main.main.getLogger().warning("No TrashItems found!");
			
			Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Mülleimer " + ChatColor.DARK_GRAY + "- Seite " + 1);
			trash_invs.add(inv);
			
		}
	}
	
	
	public static void saveTrashItems() {
		ConfigurationSection section = Main.main.getConfig().createSection("TrashItems");
		int offset = 0;
		List<ItemStack> itemlist = new ArrayList<>();
		for(Inventory inventory : trash_invs) {
			if (!inventory.isEmpty()) {
				for (ItemStack item : inventory.getContents()) {
					if (item != null) {
						Main.main.getLogger().warning("Not null");
						itemlist.add(item);
					}
					
				}
			} else {
				Main.main.getLogger().warning("Inventory is empty");
			}
			
		}
		itemlist.add(new ItemStack(Material.ACACIA_BOAT, 2));
		itemlist.add(new ItemStack(Material.ACACIA_BOAT, 2));
		itemlist.add(new ItemStack(Material.ACACIA_BOAT, 2));
		itemlist.add(new ItemStack(Material.ACACIA_BOAT, 2));
		itemlist.add(new ItemStack(Material.ACACIA_BOAT, 2));
		section.set("All", itemlist);
	}
	

	public void saveItem(ItemStack itemstack) {
		Bukkit.broadcastMessage("Saving---");
		for(int i = 0; i < trash_invs.size(); i++) {
			Bukkit.broadcastMessage("Empty: " + trash_invs.get(i).addItem(itemstack).isEmpty());
			
			if (trash_invs.get(i).addItem(itemstack).isEmpty()) {
				i = trash_invs.size(); // Oder einfach eine größere Zahl
			}
			else {
				if(i + 1 == trash_invs.size()) {
					//Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Mülleimer " + ChatColor.DARK_GRAY + "- Seite " + (i + 2));
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
