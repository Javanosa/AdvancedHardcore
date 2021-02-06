package main.listener;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftSkeletonHorse;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Wither;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import io.netty.util.internal.ThreadLocalRandom;
import main.Main;
import main.Recipes;
import main.commands.Blockdata;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.EntityHorseSkeleton;
import net.minecraft.server.v1_16_R3.NBTTagCompound;

public class Events implements Listener {
	
	public void eggthrower(Player p, Cancellable e, String namespace, Material material, ItemStack item) {
		Action action = ((PlayerInteractEvent) e).getAction();
		if((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && System.currentTimeMillis()-lastaction > 1) {
			Vector vec = p.getLocation().getDirection().multiply(2);
			Location loc = p.getLocation();
			loc.setY(loc.getY()+1.5);
			Egg egg = (Egg) p.getWorld().spawnEntity(loc, EntityType.EGG);
			egg.setVelocity(vec);
			egg.setItem(new ItemStack(material));
			egg.setMetadata("projectile_data", new FixedMetadataValue(Main.main, namespace));
			egg.setShooter(p);
			p.playSound(loc, Sound.ENTITY_SNOWBALL_THROW, 0.5f, 0.5f);
			
			if(!p.getGameMode().equals(GameMode.CREATIVE))
				item.setAmount(item.getAmount()-1);
			lastaction = System.currentTimeMillis();
			e.setCancelled(true);
		}
		
	}
	
	long lastaction = 0;
	
	@EventHandler(ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack mhi = p.getInventory().getItemInMainHand();
		ItemStack ohi = p.getInventory().getItemInOffHand();
		
		
		
		/*if(Blockdata.blockdata) {
			p.sendMessage(e.getClickedBlock().getBlockData().getAsString());
			e.setCancelled(true);
		}
		else */
		if(mhi.hasItemMeta()) {
				String displayname = mhi.getItemMeta().getLocalizedName();
				if(displayname.equals("§7§lMobfalle")) {
					eggthrower(p, e, "mobtrap", Material.IRON_BARS, mhi);
				}
				else if(displayname.equals("§5§lSpinnenfalle")) {
					eggthrower(p, e, "spidertrap", Material.SPIDER_SPAWN_EGG, mhi);
				}
				
					
			}
			if(ohi.hasItemMeta()) {
				String displayname = ohi.getItemMeta().getLocalizedName();
				if(displayname.equals("§7§lMobfalle")) {
					eggthrower(p, e, "mobtrap", Material.IRON_BARS, ohi);
				}
				else if(displayname.equals("§5§lSpinnenfalle")) {
					eggthrower(p, e, "spidertrap", Material.SPIDER_SPAWN_EGG, ohi);
				}
			}
		
		
		
	}
	
	/*@EventHandler(ignoreCancelled = false)
	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		if(e.getEntityType().equals(EntityType.FALLING_BLOCK)) {
			Bukkit.broadcastMessage(e.getBlock().getBlockData().getAsString());
		}
	}*/
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(p.getName().equals("Jonosa"))
			p.setNoDamageTicks(200);
		else
			p.setNoDamageTicks(400);
		
		e.setJoinMessage("§7-> §d" + p.getName()+"§7 hat den Server §abetreten§7.");
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage("§7<- §d" + p.getName()+"§7 hat den Server §cverlassen§7.");
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent e) {
		Item item = e.getEntity();
		Material itemstack = item.getItemStack().getType();
		
		if(item.hasMetadata("playerdeath")) {
			int zyklos = item.getMetadata("playerdeath").get(0).asInt();
			if(zyklos > 0) {
				item.setMetadata("playerdeath", new FixedMetadataValue(Main.main, zyklos - 1));
				Main.main.getLogger().info("playerdeath "+zyklos);
				e.setCancelled(true);
			}
		}
		else if(itemstack.equals(Material.ACACIA_SAPLING)
			|| itemstack.equals(Material.JUNGLE_SAPLING)
			|| itemstack.equals(Material.OAK_SAPLING)
			|| itemstack.equals(Material.BIRCH_SAPLING)
			|| itemstack.equals(Material.SPRUCE_SAPLING) ) {
			Block block = e.getLocation().getBlock();
			Location loc = e.getLocation();
			Material material = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ()).getType();
			if(		block.getType().equals(Material.AIR)
				|| block.getType().equals(Material.SNOW)
				|| block.getType().equals(Material.GRASS)
				|| block.getType().equals(Material.TALL_GRASS) ) {
				if(material.equals(Material.DIRT) 
					|| material.equals(Material.COARSE_DIRT)
					|| material.equals(Material.GRASS_BLOCK)
					|| material.equals(Material.PODZOL) ) {
				block.setType(itemstack);
				e.setCancelled(true);
				item.remove();
				Main.main.getLogger().info("§aPlaced " + itemstack.name() + " §7in " + loc.getWorld().getName()
				+ " at " + loc.getBlockX() + " " + loc.getBlockY() + " " +  loc.getBlockZ());
				}
			}
		}
	}
	
	Location location;
	double cord_x;
	double cord_z;
	World world;
	Location newloc;
	Block block;
	FallingBlock fallingblock;
	public static ThreadLocalRandom randomt = ThreadLocalRandom.current();
	
	@EventHandler
	public void onBlockBurnBlock(BlockBurnEvent e) {
		int random = randomt.nextInt(1, 2 + 1);
		block = e.getBlock();
		
		if(block.getType().isSolid() && !block.getType().equals(Material.TNT) && random == 2) {
			location = e.getIgnitingBlock().getLocation();
			cord_x = location.getBlockX() + 0.5;
			cord_z = location.getBlockZ() + 0.5;
			world = location.getWorld();
			
			newloc = new Location(world, cord_x, location.getBlockY(), cord_z);
			
			fallingblock = world.spawnFallingBlock(newloc , e.getBlock().getBlockData());
			fallingblock.setDropItem(false);
			fallingblock.setHurtEntities(true);
		}
		
	}
	
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
		
		/*switch(string) {
		case "minecraft:dispenser[facing=west":
			block = w.getBlockAt(x - i, y, z);
			break;
		case "minecraft:dispenser[facing=south":
			block = w.getBlockAt(x, y, z + i);
			break;
		case "minecraft:dispenser[facing=east":
			block = w.getBlockAt(x + i, y, z);
			break;
		case "minecraft:dispenser[facing=north":
			block = w.getBlockAt(x, y, z - i);
			break;
		case "minecraft:dispenser[facing=up":
			block = w.getBlockAt(x, y + i, z);
			break;
		case "minecraft:dispenser[facing=down":
			block = w.getBlockAt(x, y - i, z);
			break;
		}*/
		
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
		
		if(con.getCustomName() != null && con.getCustomName().equals("Gate")) {
			e.setCancelled(true);
			
			Material material = e.getItem().getType();
			Inventory inv = con.getInventory();
			ItemStack itemstack = inv.getItem(0);
			if(itemstack == null || !material.isBlock()) {
				return;
			}
			int count = itemstack.getAmount();
			
			Location l = con.getLocation();
			
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
					Main.main.getLogger().warning("Gate -> Fehler beim erstellen der BlockData");
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
				
				for(Entity et : w.getNearbyEntities(l, 16, 16, 16)) {
					if(et instanceof Player) {
						//((Player) et).sendTitle("", "§cTor geschlossen", 10, 40, 10);
						((Player) et).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cTor geschlossen"));
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
					
					
					for(Entity et : w.getNearbyEntities(l, 16, 16, 16)) {
						if(et instanceof Player) {
							//((Player) et).sendTitle("", "§aTor geöffnet", 10, 40, 10);
							((Player) et).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aTor geöffnet"));
						}
					}
				}
				
			}
		}
	}
	
	
	@EventHandler
	public void onEntityDamageBE(EntityDamageByEntityEvent e) {
		if(e.getEntityType().equals(EntityType.SKELETON)) {
			if(e.getDamager() instanceof Player) {
				if((((Damageable) e.getEntity()).getHealth() - e.getFinalDamage()) < 0.1) {
					int random = randomt.nextInt(1, 5 + 1);
			
					if(random==1) {
						Location loc = e.getEntity().getLocation();
						World w = loc.getWorld();
						for(int i = 0; i < 6; i++) {
							w.spawnEntity(loc, EntityType.SILVERFISH);
						}
					}
				}
			}
		}
	}
	
	/*@EventHandler
	public void onFallingBlockFake(EntityChangeBlockEvent e) {
		if(e.getEntityType().equals(EntityType.FALLING_BLOCK)) {
			//e.setCancelled(true);
			e.getBlock().setType(Material.DIAMOND);
			e.getEntity().remove();
			//e.getBlockData().merge(Bukkit.createBlockData(Material.ACACIA_STAIRS));
		}
	}*/
	
	//PotionEffect potioneffect_fire = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 1, false, true);
	
	public int enderdragons = 0;
	
	@EventHandler
	public void onSkeletonSpawn(CreatureSpawnEvent e) {
		
		EntityType type = e.getEntityType();
		SpawnReason sr = e.getSpawnReason();
		Location loc = e.getLocation();
		if(type.equals(EntityType.SKELETON) || type.equals(EntityType.PHANTOM)) {
			if(sr != SpawnReason.CUSTOM && sr != SpawnReason.SPAWNER) {
				World w = loc.getWorld();
				
				int random = randomt.nextInt(1, 4 + 1);
				if(random == 1) {
					Block b = new Location(w,loc.getX(),loc.getY()+1,loc.getZ()).getBlock();
					if(b.getType() != Material.CAVE_AIR) {
						Phantom pt = (Phantom) w.spawnEntity(loc, EntityType.PHANTOM);
						Skeleton st = (Skeleton) w.spawnEntity(loc, EntityType.SKELETON);
						pt.setRemoveWhenFarAway(true);
						st.setRemoveWhenFarAway(true);
						pt.addPassenger(st);
					}
					
					
				}
			}
		}
		
		ItemStack[] knightarmor = {
				new ItemStack(Material.GOLDEN_BOOTS), 
				new ItemStack(Material.CHAINMAIL_LEGGINGS), 
				new ItemStack(Material.IRON_CHESTPLATE), 
				new ItemStack(Material.LIGHT_GRAY_BANNER)};
		
		if(type.equals(EntityType.SKELETON) && sr == SpawnReason.NATURAL) {
			/*LivingEntity st = e.getEntity();
			st.getEquipment().setArmorContents(knightarmor);
			st.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
			st.getEquipment().setItemInOffHand(new ItemStack(Material.SHIELD));*/
			e.getEntity().remove();
			SkeletonHorse sh = (SkeletonHorse) loc.getWorld().spawnEntity(loc, EntityType.SKELETON_HORSE);
			//sh.setOwner();
			sh.setRemoveWhenFarAway(true);
			sh.setTamed(true);
			net.minecraft.server.v1_16_R3.EntityHorseSkeleton skeletonhorse = (EntityHorseSkeleton) ((CraftSkeletonHorse) sh).getHandle();
			skeletonhorse.t(true);
			
			
			//sh.teleport(sh.getLocation());
			//sh.addPassenger(st);
			//sh.getPersistentDataContainer().set(new NamespacedKey(Main.main, "firebomb"),PersistentDataType.BYTE, (byte) 1);
		}
		
		/*if(e.getEntity() instanceof Monster && Main.main.getConfig().getBoolean("RealyHardWorld") && sr != SpawnReason.CUSTOM && sr != SpawnReason.SPAWNER) {
			World w = loc.getWorld();
			int random = randomt.nextInt(1, 50 + 1);
			if(random == 1 || random == 2 || random == 3) {
				Wither wither = (Wither) w.spawnEntity(loc, EntityType.WITHER);
				Bukkit.broadcastMessage(wither.getHealth()+"wither");
				wither.setHealth(150);
				Player p = Bukkit.getPlayer("Jonosa");
				if(p != null)
					wither.setTarget(p);
				
				wither.getBossBar().setVisible(false);
				wither.setRemoveWhenFarAway(true);
			}
			else if(random == 4 && enderdragons <= 3) {
				EnderDragon enderdragon = (EnderDragon) w.spawnEntity(loc, EntityType.ENDER_DRAGON);
				enderdragon.setPhase(Phase.BREATH_ATTACK);
				Bukkit.broadcastMessage(enderdragon.getHealth()+"ender");
				enderdragon.setHealth(100);
				enderdragon.getBossBar().setVisible(false);
				//enderdragon.setRemoveWhenFarAway(true);
				enderdragons++;
				
			}
		}*/
	}
	
	/*@EventHandler
	public void onEnderDragonDeath(EntityDeathEvent e) {
		if(Main.main.getConfig().getBoolean("RealyHardWorld") && e.getEntityType().equals(EntityType.ENDER_DRAGON)) {
			enderdragons--;
		}
	}*/
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Location l = e.getEntity().getLocation();
		Main.main.getLogger().info("§cPlayer died §7in " + l.getWorld().getName()
		+ " at " + l.getBlockX() + " " + l.getBlockY() + " " +  l.getBlockZ() + " §aLevel: " + e.getEntity().getLevel());
		
		World w = l.getWorld();
		
		FixedMetadataValue metadata = new FixedMetadataValue(Main.main, 4);
		for(ItemStack itemstack : e.getDrops()) {
			Item item = w.dropItem(l, itemstack);
			//item.setItemStack(itemstack);
			item.setInvulnerable(true);
			item.setMetadata("playerdeath", metadata);
			//item.setTicksLived(18000);
		}
		e.getDrops().clear();
				
	}
	
	FallingBlock fb;
	
	@EventHandler
	public void onSkeletonProjectileHit(ProjectileHitEvent e) {
		if (!(e.getEntity().getShooter() instanceof Player) && e.getEntityType().equals(EntityType.ARROW)) {
			Location loc = null;
			try {
				if(e.getHitBlock() != null) {
					loc = e.getEntity().getLocation();
				}
				if(e.getHitEntity() != null) {
					loc = e.getHitEntity().getLocation();
				}
			}
			catch(NullPointerException ec) {
				
			}
			
			int random = randomt.nextInt(20, 40 + 1);
			
			if(loc != null) {
				World w = loc.getWorld();
				switch(random) {
					default:
						fb = w.spawnFallingBlock(loc, Bukkit.createBlockData(Material.FIRE));
						fb.setDropItem(false);
						break;
					case 20:
						fb = w.spawnFallingBlock(loc, Bukkit.createBlockData(Material.TNT));
						fb.setDropItem(false);
						break;
					case 30:
						float yaw = loc.getYaw();
						
						double x = loc.getX();
						double z = loc.getZ();

						if(yaw < -90) {
							// -90 bis -180
							x = loc.getX() + 0.5;
						}
						else if(yaw < 0) {
							// 0 bis -90
							z = loc.getZ() - 0.5;
						}
						else if(yaw < 90) {
							// 90 bis 0
							x = loc.getX() - 0.5;
						}
						else{
							// 180 bis 90
							z = loc.getZ() + 0.5;
						}
						
						
						
						Location loc1 = new Location(w, x, loc.getY(), z);
						
						
						w.createExplosion(loc1, 3, false, true);
						
						break;
						
					case 40:
						w.spawnEntity(loc, EntityType.LIGHTNING);
						break;
				}
				e.getEntity().remove();
			}
		}
	}
}
