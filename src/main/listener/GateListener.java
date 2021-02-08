package main.listener;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class GateListener implements Listener{
	
	public Block return_block(int x, int y, int z, World w, int i, int face) {
		Block block = null;
		switch(face) {
		case 1:
			block = w.getBlockAt(x - i, y, z);
			break;
		case 2:
			block = w.getBlockAt(x, y, z + i);
			break;
		case 3:
			block = w.getBlockAt(x + i, y, z);
			break;
		case 4:
			block = w.getBlockAt(x, y, z - i);
			break;
		case 5:
			block = w.getBlockAt(x, y + i, z);
			break;
		case 6:
			block = w.getBlockAt(x, y - i, z);
			break;
		}
		
		return block;
		
	}
	
	/*private void cleanItem(ItemStack itemstack, ItemMeta itemmeta) {
		itemmeta.setLocalizedName(null);
		itemmeta.setDisplayName(null);
		itemmeta.setLore(null);
		for(Enchantment enchant : Enchantment.values()) {
			itemmeta.removeEnchant(enchant);
		}
		itemmeta.removeItemFlags(ItemFlag.values());
		itemstack.setItemMeta(itemmeta);
	}*/
	
	List<Material> rep_material_list = Arrays.asList(Material.SNOW, Material.WATER, Material.LAVA);
	
	@EventHandler
	public void onGateOpenClose(BlockDispenseEvent e) {
		Dispenser con = (Dispenser) e.getBlock().getState();
		//Bukkit.broadcastMessage(con.getCustomName().replace("§", "&"));
		if(con.getCustomName() != null && con.getCustomName().equals("§lTorantrieb")) {
			e.setCancelled(true);
			
			Material material = e.getItem().getType();
			Inventory inv = con.getInventory();
			ItemStack itemstack = inv.getItem(0);
			if(itemstack == null || !material.isBlock()) {
				return;
			}
			int count = itemstack.getAmount();
			
			Location l = con.getLocation();
			
			@SuppressWarnings("deprecation")
			BlockFace targetface = ((org.bukkit.material.Dispenser) con.getData()).getFacing();
			
			int face = 0;
			
			switch(targetface) {
				case WEST:
					face = 1;
					break;
				case SOUTH:
					face = 2;
					break;
				case EAST:
					face = 3;
					break;
				case NORTH:
					face = 4;
					break;
				case UP:
					face = 5;
					break;
				case DOWN:
					face = 6;
					break;
			}
			
			World w = l.getWorld();
			int x = l.getBlockX();
			int y = l.getBlockY();
			int z = l.getBlockZ();
			
			//Bukkit.broadcastMessage(targetface.name());
			
			//Bukkit.broadcastMessage(w.getName() + " / " + x + " " + y + " " + z + " / "+count+" / "+itemstack.getType().name()+" / " + inv.getSize());
			
			int length = 0;
			
			//Bukkit.broadcastMessage(e.getBlock().getRelative(BlockFace.DOWN).toString());
			//String string = e.getBlock().getBlockData().getAsString().substring(27).split(",")[0];
			
			//Bukkit.broadcastMessage(string);
			
			//return_block(x, y, z, w, 1, string[0]);
			
			BlockData bd = null;
			
			ItemMeta itemmeta = itemstack.getItemMeta();
			
			String displayname = itemmeta.getDisplayName();
			
			//Bukkit.broadcastMessage(itemmeta.getLocalizedName().toString() + itemmeta.hasLocalizedName());
			
			if(!displayname.isEmpty() && !itemmeta.hasLocalizedName() && !displayname.toLowerCase().contains("type=double")) {
				try {
					bd = Bukkit.createBlockData(material, displayname);
				}
				catch(Exception ec) {
					Main.log.warning("Gate -> Fehler beim erstellen der BlockData");
					/*Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
						@Override
						public void run() {
							for(Entity et : w.getNearbyEntities(l, 16, 16, 16)) {
								if(et instanceof Player) {
									//((Player) et).sendTitle("", "§cTor geschlossen", 10, 40, 10);
									((Player) et).spigot().sendMessage(ChatMessageType.SYSTEM, new TextComponent("§eGate -> Fehler beim erstellen der BlockData"));
								}
							}
						}
						
					}, 0);*/
					w.playSound(l, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1f, 1f);
					
					
					itemstack.setItemMeta(null);
				}
			} else{
				if(itemmeta.hasLocalizedName()) {
					itemstack.setItemMeta(null);
				}
			}
			
			/*Bukkit.broadcastMessage(bd+"");
			Bukkit.broadcastMessage(displayname);*/
			
			if(count > 1) {
				
				
				
				
				
				for(int i = 1; i <= 16; i++) {
					if(length < count - 1) {
						//Block block = w.getBlockAt(x, y + i, z);
						Block block = return_block(x, y, z, w, i, face);
						if(block.getType().isAir() || rep_material_list.contains(block.getType())) {
							block.setType(material);
							if(bd != null)
								block.setBlockData(bd);
							length++;
							
						}
						else if(i > 2){
							i = 16;
						}
						
					}
				}
				//e.setCancelled(true);
				itemstack.setAmount(count - length);
				
				w.playSound(l, Sound.BLOCK_PISTON_EXTEND, 0.3f, 0.7f);
				
				for(Entity entity : w.getNearbyEntities(l, 16, 16, 16)) {
					if(entity instanceof Player) {
						//((Player) et).sendTitle("", "§cTor geschlossen", 10, 40, 10);
						((Player) entity).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cTor geschlossen"));
					}
				}
				
			}
			else{
				for(int i = 1; i <= 16; i++) {
					//Block block = w.getBlockAt(x, y + i, z);
					Block block = return_block(x, y, z, w, i, face);
					//Bukkit.broadcastMessage(block.getBlockData().getAsString());
					if(block.getType().equals(material) && bd == null || block.getBlockData().matches(bd)) {
							block.setType(Material.AIR);
							length++;
					}
					else if(i > 2){
						i = 16;
					}
				}
				
				if(length != 0) {
					//e.setCancelled(true);
					itemstack.setAmount(length + count);
					

					w.playSound(l, Sound.BLOCK_PISTON_CONTRACT, 0.3f, 0.7f);
					
					
					for(Entity entity : w.getNearbyEntities(l, 16, 16, 16)) {
						if(entity instanceof Player) {
							//((Player) et).sendTitle("", "§aTor geöffnet", 10, 40, 10);
							((Player) entity).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aTor geöffnet"));
						}
					}
				}
				
			}
		}
	}
}
