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
						for(Entity entity : p.getNearbyEntities(r + 2, r + 2, r + 2)) {
							if(entity.getType().isAlive() && !entity.equals(p.getShooter())) {
								((LivingEntity) entity).setFireTicks(200);
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
						for(Entity entity : p.getNearbyEntities(r+2, r+2, r+2)) {
							if(entity.getFireTicks()>0) {
								fire=true;
								entity.setFireTicks(0);
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
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		if (e.getEntity().getShooter() instanceof Player || e.getEntity().getShooter() instanceof LivingEntity) {
			if(	e.getEntityType().equals(EntityType.ARROW) 
				|| e.getEntityType().equals(EntityType.FIREWORK) 
				|| e.getEntityType().equals(EntityType.EGG) 
				|| e.getEntityType().equals(EntityType.SNOWBALL) 
				|| e.getEntityType().equals(EntityType.SPLASH_POTION)) {
				
				LivingEntity p = (LivingEntity) e.getEntity().getShooter();
				ItemStack mhi = p.getEquipment().getItemInMainHand();
				ItemStack ohi = p.getEquipment().getItemInOffHand();
				String namespace = null;
				
				EntityType entitytype = e.getEntityType();
				
				// Wenn die SpecialItems mit der RightHand geworfen werden oder der Superbow genutzt wird
				if(mhi.hasItemMeta()) {
					String displayname = mhi.getItemMeta().getLocalizedName();
					
					if(displayname.equals("§cExplosiverBogen")) {
						if(ohi.equals(GetItem.bow_bomb)) 
							namespace = "xbow_bomb";
						else if(ohi.equals(GetItem.bow_rocket))
							namespace = "xbow_rocket";
						else if(ohi.equals(GetItem.bow_nuklear)) 
							namespace = "xbow_nuklear";
						else
							namespace = "xbow";
					}
					else if(entitytype.equals(EntityType.FIREWORK)) {
						if(mhi.getType().equals(Material.CROSSBOW)) {
							namespace = "firework";
						}
					}
					else if(e.getEntityType().equals(EntityType.EGG)) {
						if(displayname.equals("§c§lFeuerbombe")) {
							namespace = "firebomb";
						}
 						else if(displayname.equals("§7§lMobfalle")) {
							namespace = "mobtrap";
						}
						else if(displayname.equals("§6§lImplosion")) {
							namespace = "imploding";
						}
						else if(displayname.equals("§e§lDruckwelle")) {
							namespace = "exploding";
						}
						else if(displayname.equals("§5§lSpinnenfalle")) {
							namespace = "spidertrap";
						}
					}
					else if(entitytype.equals(EntityType.SNOWBALL)) {
						if(displayname.equals("§b§lEisbombe"))
								namespace = "icebomb";
					}
					else if(entitytype.equals(EntityType.SPLASH_POTION)) {
						if(displayname.equals("§c§lMolotowcocktail")) {
							Vector vec = e.getEntity().getVelocity();
							vec.multiply(3);
							e.getEntity().setVelocity(vec);
							namespace = "firebomb";
						}
						else if(displayname.equals("§3§lWasserbombe")) {
							if(p.getWorld().getEnvironment().equals(Environment.NETHER)) {
								e.setCancelled(true);
								p.sendMessage("§cDu kannst die Wasserbombe im Nether nicht benutzen!");
							}
							Vector vec = e.getEntity().getVelocity();
							vec.multiply(3);
							e.getEntity().setVelocity(vec);
							namespace = "waterbomb";
						}
					}
					
					
					
				}
				// Wenn keine ItemMeta notwendig
				if(ohi.getType().equals(Material.TNT) && !ohi.equals(GetItem.bow_bomb) && mhi.getType().equals(Material.BOW)) {
					namespace = "tnt";
					ohi.setAmount(ohi.getAmount() - 1);
				}
				// Wenn die SPecialItems mit der Lefthand geworfen werden
				if(ohi.hasItemMeta()) {
					String displayname = ohi.getItemMeta().getLocalizedName();
					
					if(e.getEntityType().equals(EntityType.EGG)) {
						if(displayname.equals("§c§lFeuerbombe")) {
							namespace = "firebomb";
						}
 						else if(displayname.equals("§7§lMobfalle")) {
							namespace = "mobtrap";
						}
						else if(displayname.equals("§6§lImplosion")) {
							namespace = "imploding";
						}
						else if(displayname.equals("§e§lDruckwelle")) {
							namespace = "exploding";
						}
						else if(displayname.equals("§5§lSpinnenfalle")) {
							namespace = "spidertrap";
						}
					}
					else if(entitytype.equals(EntityType.SNOWBALL)) {
						if(displayname.equals("§b§lEisbombe"))
								namespace = "icebomb";
					}
					else if(entitytype.equals(EntityType.SPLASH_POTION)) {
						if(displayname.equals("§c§lMolotowcocktail")) {
							Vector vec = e.getEntity().getVelocity();
							vec.multiply(3);
							e.getEntity().setVelocity(vec);
							namespace = "firebomb";
						}
						else if(displayname.equals("§3§lWasserbombe")) {
							if(p.getWorld().getEnvironment().equals(Environment.NETHER)) {
								e.setCancelled(true);
								p.sendMessage("§cDu kannst die Wasserbombe im Nether nicht benutzen!");
							}
							Vector vec = e.getEntity().getVelocity();
							vec.multiply(3);
							e.getEntity().setVelocity(vec);
							namespace = "waterbomb";
						}
					}
				}
				
				if(namespace != null)
					e.getEntity().setMetadata("projectile_data", new FixedMetadataValue(Main.main, namespace));
			}
		}
	}
}
