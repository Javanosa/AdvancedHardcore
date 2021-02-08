package main.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import main.Main;

public class EggThrowListener implements Listener{
	
	public static Map<UUID, List<Block>> spider_blocks = new HashMap<>();
	public static Map<UUID, List<Spider>> spider_entitys = new HashMap<>();
	
	List<EntityType> friendlymobs = Arrays.asList(
		EntityType.SHEEP, EntityType.COW, EntityType.PIG, EntityType.CAT, EntityType.CHICKEN, 
		EntityType.WOLF, EntityType.DONKEY, EntityType.FOX, EntityType.HORSE, EntityType.IRON_GOLEM, 
		EntityType.SNOWMAN, EntityType.LLAMA, EntityType.RABBIT, EntityType.PLAYER, EntityType.MULE, 
		EntityType.VILLAGER, EntityType.POLAR_BEAR, EntityType.PARROT, EntityType.SKELETON_HORSE, EntityType.SQUID);
	
								
	@EventHandler
	public void onEggThrow(PlayerEggThrowEvent e) {
		Egg p = e.getEgg();
		
		if(p.hasMetadata("projectile_data")) {
			e.setHatching(false);
			Location loc = e.getEgg().getLocation();
			World w = loc.getWorld();
			String namespace = p.getMetadata("projectile_data").get(0).asString();
			int r = 0;
			switch(namespace) {
				/*case "firebomb":
					r = 4;
					Explosion.changeBlocks(loc, r, 0);
					for(Entity et : p.getNearbyEntities(r, r, r)) {
						if(et.getType().isAlive() && !et.equals(e.getPlayer())) {
							((LivingEntity) et).setFireTicks(200);
						}
					}
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 40, 0, 0, 0, 0.25);
					loc.getWorld().playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1, 1);
					break;*/
				case "mobtrap":
					r = 10;		
					List<Block> blocks2 = new ArrayList<Block>();
					for(Entity entity : p.getNearbyEntities(r, r, r)) {
						if(entity.getType().isAlive() && !entity.equals(e.getPlayer()) && !entity.isInWater() && !entity.isInsideVehicle()) {
							BoundingBox bb = entity.getBoundingBox();
							Location l = entity.getLocation();
							
							double maxy = Math.floor(bb.getMaxY());
							
							if(maxy < l.getBlockY() + 2)
								maxy=l.getBlockY()+2;
							
							for(int x=l.getBlockX(); x <= bb.getMaxX(); x++) {
								for(int z=l.getBlockZ(); z <= bb.getMaxZ(); z++) {
									for(int y=l.getBlockY(); y <= maxy; y++) {
										Block b = w.getBlockAt(x,y,z);
										blocks2.add(b);
										
									}
								}
							}
						}
					}
					int prisoned = 0;
					
					for(Entity entity : p.getNearbyEntities(r, r, r)) {
						if(entity.getType().isAlive() && !entity.equals(e.getPlayer()) && !entity.isInWater() && !entity.isInsideVehicle()) {
							
							BoundingBox bb = entity.getBoundingBox();
							Location l = entity.getLocation();
							
							double maxy = Math.floor(bb.getMaxY()) + 1;
							
							if(maxy < l.getBlockY() + 3)
								maxy=l.getBlockY()+3;
								
							for(int x=l.getBlockX() - 1; x <= bb.getMaxX() + 1; x++) {
								for(int z=l.getBlockZ() - 1; z <= bb.getMaxZ() + 1; z++) {
									for(int y=l.getBlockY() - 1; y <= maxy; y++) {
										Block b = w.getBlockAt(x,y,z);
										if(!blocks2.contains(b))
										if(b.getType().isAir() || b.getType().equals(Material.GRASS) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.TALL_GRASS) || b.getType().equals(Material.WATER)) {
											if(y == maxy)
												b.setType(Material.ANDESITE_SLAB);
											else if(y == l.getBlockY() - 1)
												b.setType(Material.COBBLESTONE);
											else
												b.setType(Material.SPRUCE_FENCE);
										}
									}
								}
							}
							prisoned++;
						}
					}
					w.spawnParticle(Particle.BLOCK_CRACK, loc, 20, Bukkit.createBlockData(Material.IRON_BLOCK));
					w.spawnParticle(Particle.SMOKE_NORMAL, loc, 20, 0, 0, 0, 0.1);
					w.playSound(loc, Sound.BLOCK_ANVIL_PLACE, 0.7f, 0.8f);
					if(prisoned!=0)
						e.getPlayer().sendMessage("§b" + prisoned + " §7Entities §aeingesperrt§7.");
					
					break;
				case "imploding":
					r = 8;
					for(Entity entity : p.getNearbyEntities(r, r, r)) {
						if(entity.getType().isAlive() && !entity.equals(e.getPlayer())) {
							Location l = entity.getLocation();
							Vector vec = new Vector(l.getX(),l.getY(),l.getZ());
							Vector vec2 = new Vector(loc.getX(),loc.getY(),loc.getZ()).subtract(vec);
							vec2.multiply(0.35);
							entity.setVelocity(vec2);
						}
					}
					w.spawnParticle(Particle.CRIT, loc, 60, 0, 0, 0, 1);
					w.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
					break;
				case "exploding":
					r = 8;
					for(Entity entity : p.getNearbyEntities(r, r, r)) {
						if(entity.getType().isAlive() && !entity.equals(e.getPlayer())) {
							Location l = entity.getLocation();
							Vector vec = new Vector(loc.getX(),loc.getY(),loc.getZ());
							Vector vec2 = new Vector(l.getX(),l.getY()+2,l.getZ());
							double d = vec.distance(vec2);
							vec2.subtract(vec);
							double mu = 1;
							if(d>6) {
								mu = 0.2;
							}
							else if(d>4) {
								mu = 0.5;
							}
							else if(d>2) {
								mu = 0.8;
							}
							vec2.multiply(mu);
							entity.setVelocity(vec2);
						}
					}
					w.spawnParticle(Particle.CRIT, loc, 60, 0, 0, 0, 1);
					w.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
					break;
				/*case "waterbomb":
					p.teleport(new Location(w,loc.getX(),loc.getY()-1000,loc.getZ()));
					r = 6;
					boolean fire = false;
					for(Entity et : p.getNearbyEntities(r+2, r+2, r+2)) {
						if(et.getFireTicks()>0) {
							fire=true;
							et.setFireTicks(0);
						}
					}
					
					if(fire)
					w.playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
					
					Explosion.changeBlocks(loc, r, 2);
					w.spawnParticle(Particle.WATER_SPLASH, loc, 20, 0, 0, 0);
					w.spawnParticle(Particle.WATER_WAKE, loc, 60, 0, 0, 0, 0.3);
					w.playSound(loc, Sound.ENTITY_GENERIC_SPLASH, 1, 1);
					break;*/
				case "spidertrap":
					r = 8;
					List<Block> blocks3 = new ArrayList<Block>();
					List<Entity> targetlist = new ArrayList<>();
					
					for(Entity entity : p.getNearbyEntities(r, r, r)) {
						if(entity instanceof Monster && !entity.isInWater()) {
							BoundingBox bb = entity.getBoundingBox();
							Location l = entity.getLocation();
							for(int x=l.getBlockX(); x <= bb.getMaxX(); x++) {
								for(int z=l.getBlockZ(); z <= bb.getMaxZ(); z++) {
									for(int y=l.getBlockY(); y <= bb.getMaxY(); y++) {
										Block b = w.getBlockAt(x,y,z);
										if(b.getType().isAir() || b.getType().equals(Material.GRASS) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.TALL_GRASS)) {
											b.setType(Material.COBWEB);
											b.setMetadata("spidertrapspawned", new FixedMetadataValue(Main.main, 1));
											blocks3.add(b);
										}
									}
								}	
							}
							targetlist.add(entity);
						}
					}
					spider_blocks.put(p.getUniqueId(), blocks3);
					
					// Centerblocks
					int bx=loc.getBlockX();
					int by=loc.getBlockY();
					int bz=loc.getBlockZ();
					// Startblocks
					int ix=bx - r;
					int iy=by - r;
					int iz=bz - r;
					// Maxblocks
					int mx=bx + r;
					int my=by + r;
					int mz=bz + r;
					
					int r2 = r*r;
					double d;
					
					int current_spiders = 0;
					
					List<Spider> spiderlist = new ArrayList<>();
					
					for(int x=ix; x <= mx; x++) {
						for(int y=iy; y <= my; y++) {
							for(int z=iz; z <= mz; z++) {
								d = ((bx-x) * (bx-x) + (bz-z) * (bz-z) + (by-y) * (by-y));
								if(d < r2 - Main.random.nextInt(1, 10 + 1)) {
									Material m = w.getBlockAt(x, y, z).getType();
									Material m1 = w.getBlockAt(x, y - 1, z).getType();
									if(m.equals(Material.AIR) || m.equals(Material.GRASS) || m.equals(Material.TALL_GRASS) || m.equals(Material.SNOW))
										if(m1.isSolid()) {
											int random1 = Main.random.nextInt(0, 20);
											if(random1 == 10 && current_spiders <= 6) {
												Spider spider = (Spider) w.spawnEntity(new Location(w, x, y,z), EntityType.SPIDER);
												if(targetlist.size()!=0) {
													int random = Main.random.nextInt(0, targetlist.size());
													spider.setTarget((Monster) targetlist.get(random));
												}
												current_spiders++;
												spider.setGlowing(true);
												spiderlist.add(spider);
												
												if(!spider.getPassengers().isEmpty())
													spider.removePassenger(spider.getPassengers().get(0));
												
												spider.setMetadata("spidertrapspawned", new FixedMetadataValue(Main.main, 1));
												
											}
										}
								}
							}
						}
					}
					
					spider_entitys.put(p.getUniqueId(), spiderlist);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
						@Override
						public void run() {
							for(Block b : blocks3) {
								if(b.getType().equals(Material.COBWEB)) {
									b.setType(Material.AIR);
								}
							}
							for(Spider s : spiderlist) {
								s.remove();
							}
							// Wichtig, sonst wird das beim onDisable() nochmal durchgearbeitet!
							spider_blocks.remove(p.getUniqueId());
							spider_entitys.remove(p.getUniqueId());
						}
						
					}, 300);
					
					w.spawnParticle(Particle.SPIT, loc, 60, 0, 0, 0, 0.5);
					w.playSound(loc, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
					break;
			}
		}
	}
}
