package main.listener;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftSkeletonHorse;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.event.entity.WitchThrowPotionEvent;

import io.netty.util.internal.ThreadLocalRandom;
import main.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.EntityHorseSkeleton;

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
	
	@EventHandler
	public void onWitchThrowPotion(WitchThrowPotionEvent e) {
		e.setPotion(new ItemStack(Material.BARREL));
		e.getEntity().getEquipment().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
		e.getEntity().getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
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
				Main.log.info("playerdeath "+zyklos);
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
				Main.log.info("§aPlaced " + itemstack.name() + " §7in " + loc.getWorld().getName()
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
	
	@EventHandler
	public void onBlockBurnBlock(BlockBurnEvent e) {
		int random = Main.random.nextInt(1, 2 + 1);
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
	
	
	@EventHandler
	public void onEntityDamageBE(EntityDamageByEntityEvent e) {
		if(e.getEntityType().equals(EntityType.SKELETON)) {
			if(e.getDamager() instanceof Player) {
				if((((Damageable) e.getEntity()).getHealth() - e.getFinalDamage()) < 0.1) {
					int random = Main.random.nextInt(1, 5 + 1);
			
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
	public void onFallingBlock(EntityChangeBlockEvent e) {
		if(e.getEntityType().equals(EntityType.FALLING_BLOCK)) {
			String st = e.getEntity().getPersistentDataContainer().get(new NamespacedKey(Main.main,"containerdata"), PersistentDataType.STRING);
			Bukkit.broadcastMessage(st);
		}
	}*/
	
	//PotionEffect potioneffect_fire = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 1, false, true);
	
	//public int enderdragons = 0;
	
	@EventHandler
	public void onSkeletonSpawn(CreatureSpawnEvent e) {
		
		EntityType type = e.getEntityType();
		SpawnReason sr = e.getSpawnReason();
		Location loc = e.getLocation();
		if(type.equals(EntityType.SKELETON) || type.equals(EntityType.PHANTOM)) {
			if(sr != SpawnReason.CUSTOM && sr != SpawnReason.SPAWNER) {
				World w = loc.getWorld();
				
				int random = Main.random.nextInt(1, 4 + 1);
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
			new ItemStack(Material.LIGHT_GRAY_BANNER)
		};
		
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
		Main.log.info("§cPlayer died §7in " + l.getWorld().getName()
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
			
			int random = Main.random.nextInt(20, 40 + 1); // 20, 40 + 1
			
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
						
						//float yaw = loc.getYaw();
						
						double xa = 0;
						double za = 0;
						double ya = 0;
						
						
						
						switch(e.getEntity().getFacing()) {
							case WEST:
								xa = -0.5;
								break;
							case SOUTH:
								za = -0.5;
								break;
							case EAST:
								xa = 0.5;
								break;
							case NORTH:
								za = 0.5;
								break;
							case UP:
								ya = 0.5;
								break;
							case DOWN:
								ya = -0.5;
								break;
						}
						
						

						/*if(yaw < -90) {
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
						}*/
						
						
						
						Location loc1 = new Location(w, loc.getX()+xa, loc.getY()+ya, loc.getZ()+za);
						
						
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
