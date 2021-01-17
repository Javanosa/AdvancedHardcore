package main.listener;

import java.util.ArrayList;
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
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import main.Explosion;
import main.Main;
import main.commands.GetItem;

public class ProjectileEvents implements Listener{
	
	PotionEffect potioneffect_freeze = new PotionEffect(PotionEffectType.SLOW, 600, 9);
	
	public static Map<UUID, List<Block>> ice_blocks = new HashMap<>();
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		Projectile p = e.getEntity();
		if(p.hasMetadata("projectile_data")) {
				Location loc = e.getEntity().getLocation();
				World w = loc.getWorld();
				int r = 0;
				boolean blowup = false;
				String namespace = p.getMetadata("projectile_data").get(0).asString();
				switch(namespace) {
					case "xbow_bomb":
					case "tnt":
						r = 4;
						blowup = true;
						break;
					case "xbow_rocket":
						r = 7;
						blowup = true;
						break;
					case "xbow_nuklear":
						r = 10;
						blowup = true;
						break;
					case "firework":
						r = 3;
						blowup = true;
						break;
					case "xbow":
						r = 2;
						blowup = true;
						break;
					case "firebomb":
						r = 4;
						for(Entity et : p.getNearbyEntities(r + 2, r + 2, r + 2)) {
							if(et.getType().isAlive() && !et.equals(p.getShooter())) {
								((LivingEntity) et).setFireTicks(200);
							}
						}
						p.teleport(new Location(w,loc.getX(),loc.getY()-1000,loc.getZ()));
						
						Explosion.changeBlocks(loc, r, 0);
						
						w.spawnParticle(Particle.FLAME, loc, 40, 0, 0, 0, 0.25);
						w.playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1, 1);
						break;
					case "icebomb":
						r = 8;
						List<Block> blocks = new ArrayList<Block>();
						
						int frozen = 0;
						
						for(Entity entity : p.getNearbyEntities(r, r, r)) {
							if(entity instanceof Monster && !entity.isInWater()) {
								((Monster) entity).addPotionEffect(potioneffect_freeze);
								BoundingBox bb = entity.getBoundingBox();
								Location l = entity.getLocation();
								for(int x=l.getBlockX() - 1; x <= bb.getMaxX() + 0.5; x++) {
									for(int z=l.getBlockZ() - 1; z <= bb.getMaxZ() + 0.5; z++) {
										for(int y=l.getBlockY() - 1; y <= bb.getMaxY() + 0.5; y++) {
											Block b = loc.getWorld().getBlockAt(x,y,z);
											if(b.getType().isAir() || b.getType().equals(Material.GRASS) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.TALL_GRASS)) {
												b.setType(Material.ICE);
												blocks.add(b);
												
											}
											
										}
									}
								}
								frozen++;
							}
						}
						

						if(frozen != 0 && p.getShooter() instanceof Player)
							((Player) p.getShooter()).sendMessage("§b" + frozen + " §7Entities §beingefroren.");
						
						ice_blocks.put(p.getUniqueId(), blocks);
						
						Explosion.changeBlocks(loc, r, 1);
						
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
							@Override
							public void run() {
								for(Block b : blocks) {
									if(b.getType().equals(Material.ICE) || b.getType().equals(Material.WATER)) {
										Location l1 = b.getLocation();
										Material m1 = w.getBlockAt(l1.getBlockX(), l1.getBlockY() - 1, l1.getBlockZ()).getType();
										if(m1.isSolid() && !m1.equals(Material.ICE)) {
											b.setType(Material.SNOW);
										}
										else 
											b.setType(Material.AIR);
									}
								}
								// Wichtig, sonst wird das beim onDisable() nochmal durchgearbeitet!
								ice_blocks.remove(p.getUniqueId());
							}
							
						}, 200);
						w.spawnParticle(Particle.SPIT, loc, 60, 0, 0, 0, 0.5);
						w.playSound(loc, Sound.BLOCK_GLASS_BREAK, 1, 1);
						
						break;
					case "waterbomb":
						r = 6;
						boolean fire = false;
						for(Entity et : p.getNearbyEntities(r+2, r+2, r+2)) {
							if(et.getFireTicks()>0) {
								fire=true;
								et.setFireTicks(0);
							}
						}
						p.teleport(new Location(w,loc.getX(),loc.getY()-1000,loc.getZ()));
						
						
						if(fire)
						w.playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
						
						Explosion.changeBlocks(loc, r, 2);
						w.spawnParticle(Particle.WATER_SPLASH, loc, 20, 0, 0, 0);
						w.spawnParticle(Particle.WATER_WAKE, loc, 60, 0, 0, 0, 0.3);
						w.spawnParticle(Particle.ITEM_CRACK, loc, 20, 0, 0, 0, 0.1, new ItemStack(Material.SPLASH_POTION));
						w.playSound(loc, Sound.ENTITY_GENERIC_SPLASH, 2f, 1f);
						break;
				}
				p.remove();
				
				if(blowup)
				w.createExplosion(loc.getX(), loc.getY(), loc.getZ(), r, false, true);
		}
	}
	
	private String getNamespace(ItemStack item) {
		if(item.equals(GetItem.bow_bomb)) {
			return "xbow_bomb";
		} else if(item.equals(GetItem.bow_rocket)) {
			return "xbow_rocket";
		} else if(item.equals(GetItem.bow_nuklear)) {
			return "xbow_nuklear";
		} else {
			return "xbow";
		}
	}
	
	private String getEggNamespace(String displayname) {
		if(displayname.equals("§c§lFeuerbombe")) {
			return "firebomb";
		}
		else if(displayname.equals("§7§lMobfalle")) {
				return "mobtrap";
		}
		else if(displayname.equals("§6§lImplosion")) {
			return "imploding";
		}
		else if(displayname.equals("§e§lDruckwelle")) {
			return "exploding";
		}
		else if(displayname.equals("§5§lSpinnenfalle")) {
			return "spidertrap";
		}
		return "";
	}
	
	private boolean isCorrectEntityType(EntityType entityType) {
		return entityType.equals(EntityType.ARROW) || entityType.equals(EntityType.FIREWORK) 
		|| entityType.equals(EntityType.EGG) || entityType.equals(EntityType.SNOWBALL) 
		|| entityType.equals(EntityType.SPLASH_POTION);
	}

	private String doTheWork(ItemStack item, EntityType entityType, ProjectileLaunchEvent event, LivingEntity player) {
		String displayname = item.getItemMeta().getLocalizedName();
		String namespace = null;
		
		if(displayname.equals("§cExplosiverBogen")){
			namespace = getNamespace(item);
		}
		else if(entityType.equals(EntityType.FIREWORK)) {
			if(item.getType().equals(Material.CROSSBOW)) {
				namespace = "firework";
			}
		}
		else if(event.getEntityType().equals(EntityType.EGG)) {
			namespace = getEggNamespace(displayname);
		}
		else if(entityType.equals(EntityType.SNOWBALL)) {
			if(displayname.equals("§b§lEisbombe")) {
					namespace = "icebomb";
			}
		}
		else if(entityType.equals(EntityType.SPLASH_POTION)) {
			if(displayname.equals("§c§lMolotowcocktail")) {
				Vector vec = event.getEntity().getVelocity();
				vec.multiply(3);
				event.getEntity().setVelocity(vec);
				namespace = "firebomb";
			}
			else if(displayname.equals("§3§lWasserbombe")) {
				if(player.getWorld().getEnvironment().equals(Environment.NETHER)) {
					//event.setCancelled();
					// No cancel other plugins might wanna receive that event
					player.sendMessage("§cDu kannst die Wasserbombe im Nether nicht benutzen!");
					return "";
				}
				Vector vec = event.getEntity().getVelocity();
				vec.multiply(3);
				event.getEntity().setVelocity(vec);
				namespace = "waterbomb";
			}
		}
		else if(item.getType().equals(Material.TNT) && item.getType().equals(Material.BOW)) {
			namespace = "tnt";
			item.setAmount(item.getAmount() - 1);
		}
		return namespace;
	}
	
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player || event.getEntity().getShooter() instanceof LivingEntity) {
			if(isCorrectEntityType(event.getEntityType())) {
				
				LivingEntity player = (LivingEntity) event.getEntity().getShooter();
				ItemStack itemInMainHand = player.getEquipment().getItemInMainHand();
				ItemStack itemInOffHand = player.getEquipment().getItemInOffHand();
				String namespace = null;
				
				EntityType entitytype = event.getEntityType();
				
				if(itemInMainHand.hasItemMeta()) {
					namespace = doTheWork(itemInMainHand, entitytype, event, player);
				}
				if(itemInOffHand.hasItemMeta()) {
					namespace = doTheWork(itemInOffHand, entitytype, event, player);
				}
				else if(itemInOffHand.getType().equals(Material.TNT) && !itemInMainHand.equals(GetItem.superbow) && itemInMainHand.getType().equals(Material.BOW)) {
					namespace = "tnt";
					itemInOffHand.setAmount(itemInOffHand.getAmount() - 1);
				}
				
				if(namespace != null) {
					event.getEntity().setMetadata("projectile_data", new FixedMetadataValue(Main.main, namespace));
				}
			}
		}
	}
}
