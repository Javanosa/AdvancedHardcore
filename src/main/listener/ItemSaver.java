package main.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import main.Main;
import net.md_5.bungee.api.ChatColor;

public class ItemSaver implements Listener {
	
	
	public static List<Inventory> trash_invs = new ArrayList<>();
	
	static File file = new File(Main.main.getDataFolder(), "trashitems.yml");
	static FileConfiguration trashitems = YamlConfiguration.loadConfiguration(file);
	
	static {
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {}
		}
		
		ConfigurationSection section = trashitems.getConfigurationSection("TrashItems");
		
		if(section==null) {
			section = trashitems.createSection("TrashItems");
		}
		
		if(!section.getKeys(false).isEmpty()) {
			int keys = section.getKeys(false).size();
			
			int inv_keys = (int) Math.ceil(keys / 54);
			
			Main.main.getLogger().info(inv_keys +  " inv_keys");
			for(int iv = 0; iv < keys; iv=iv+54) {
				Main.main.getLogger().info(iv +  " iv");
				Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Mülleimer "+ChatColor.DARK_GRAY+"- Seite "+ Math.round(Math.ceil(iv/54)+1));
				for(int i = 0; i < 54; i++) {
					Main.main.getLogger().info(i +  "Item");
					
					ItemStack itemstack = section.getItemStack((iv+i)+"");
					inv.setItem(i, itemstack);
				}
				trash_invs.add(inv);
				
			}
			//Main.main.getLogger().info("Loaded TrashItems! " + keys + "/" + inv_keys);
			
		}
		else {
			Main.main.getLogger().warning("No TrashItems found!");
			
			Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Mülleimer "+ChatColor.DARK_GRAY+"- Seite 1");
			trash_invs.add(inv);
			
		}
	}
	
	
	public static void saveTrashItems() {
		
		ConfigurationSection section = trashitems.createSection("TrashItems");
		int offset = 0;
		for(int iv = 0; iv < trash_invs.size(); iv++) {
			Main.main.getLogger().warning(iv +  " iv");
			for(int i = 0; i < 54; i++) {
				section.set((offset+i)+"", trash_invs.get(iv).getItem(i));
				Main.main.getLogger().warning(i +  " i §dset" + (offset+i));
				
			}
			offset=offset+54;
		}
		
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			trashitems.save(file);
		} catch (IOException e) {}
	}
	
	
	public static void saveItem(ItemStack itemstack) {
		for(int i=0; i < trash_invs.size(); i++) {
			//Bukkit.broadcastMessage("Empty: "+trash_invs.get(i).addItem(itemstack).isEmpty());
			
			if(trash_invs.get(i).addItem(itemstack).isEmpty()) {
				i = trash_invs.size(); // Oder einfach eine größere Zahl
			}
			else {
				if(i+1 == trash_invs.size()) {
					Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE+"Mülleimer "+ChatColor.DARK_GRAY+"- Seite "+(i+2));
					trash_invs.add(inv);
				}
			}
		}
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent e) {
		if(!e.isCancelled() && e.getEntity().getPickupDelay() == 0) {
			saveItem(e.getEntity().getItemStack());
			Bukkit.broadcastMessage(
			"§e-- §7" + e.getEntity().getPickupDelay()
			+ " / " + e.getEntity().getLastDamageCause()
			+ " / " + e.getEntity().getOwner()
			+ " / " + e.getEntity().getItemStack().getAmount()
			+ " / " + e.getEntity().getThrower() + "\nItemDespawn §e--" + e.isCancelled());
		}
	}
	
	@EventHandler(ignoreCancelled = false)
	public void onItemDeath(EntityDeathEvent e) {
		//Bukkit.broadcastMessage(e.getEntityType()+" type");
		if(e.getEntityType().equals(EntityType.DROPPED_ITEM)) {
			saveItem(((Item) e.getEntity()).getItemStack());
			Bukkit.broadcastMessage("§eItemDeath");
		}
	}
	
	
	int currentid = 0;
	
	//List<Integer> killeditems = new ArrayList<>();
	Boolean itemalive = true;
	
	@EventHandler(ignoreCancelled = true)
	public void onItemDamage(EntityDamageEvent e) {
		if(e.getEntityType().equals(EntityType.DROPPED_ITEM)) {
			
			Item item = (Item) e.getEntity();
			
			int entityid = item.getEntityId();
			
			if(/*killeditems.contains(entityid)*/currentid != entityid) {
				//killeditems.add(entityid);
				currentid = entityid;
				
				
			}
			else {
				itemalive = true;
			}
			
			
			Bukkit.getScheduler().runTask(Main.main, new Runnable() {
				@Override
				public void run() {
					if(item.isDead() && itemalive) {
						itemalive = false;
						saveItem(item.getItemStack());
						Bukkit.broadcastMessage("§eItemDamage §6§l"+item.isDead());
					}
				}
				
			});
		}
	}
	
	
	/*@EventHandler
	public void onPlayerItemDamage(PlayerItemDamageEvent e) {
		saveItem(e.getItem());
		Bukkit.broadcastMessage("§ePlayerItemDamage");
	}
	
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent e) {
		saveItem(e.getBrokenItem());
		Bukkit.broadcastMessage("§ePlayerItemBreak");
	}*/
	
	
	
}
