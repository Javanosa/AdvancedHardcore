package main.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Stray;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.LightningStrikeEvent.Cause;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.metadata.FixedMetadataValue;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;

import main.Explosion;
import main.Main;
import net.minecraft.server.v1_16_R3.ArgumentBlockPredicate.b;

public class ExplosionListener implements Listener {
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		e.setYield(0);
		//e.setCancelled(true);
		if(e.getEntity().hasMetadata("radius") && e.getEntity().hasMetadata("setfire")) {
			Explosion.moveBlocks(e.getLocation(), e.blockList(), e.getEntity().getMetadata("radius").get(0).asFloat(), e.getEntity().getMetadata("setfire").get(0).asBoolean());
		}
		else{
			Explosion.moveBlocks(e.getLocation(), e.blockList(), null, null);
		}
		
	}
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent e) {
		e.setYield(0);
		//e.setCancelled(true);
		Explosion.moveBlocks(e.getBlock().getLocation(), e.blockList(), null, null);
	}
	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent e) {
		e.getEntity().setMetadata("radius", new FixedMetadataValue(Main.main, e.getRadius()));
		e.getEntity().setMetadata("setfire", new FixedMetadataValue(Main.main, e.getFire()));
	}
	/*@EventHandler
	public void onLightning(EntitySpawnEvent e) {
		if(e.getEntityType().equals(EntityType.LIGHTNING)) {
			Location loc = e.getEntity().getLocation();
			loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 3, true, true);
		}
	}*/
	
	@EventHandler
	public void onLightning(LightningStrikeEvent e) {
		if(!e.getCause().equals(Cause.TRAP)) {
			Location loc = e.getLightning().getLocation();
			loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 3, true, true);
		}
	}
	@EventHandler
	public void onPlayerExplosionDamage(EntityDamageEvent e) {
		DamageCause cause = e.getCause();
		Entity et = e.getEntity();
		/*Entity et = e.getEntity();
		if(!(et instanceof Player) && et instanceof Damageable) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
				@Override
				public void run() {
					double health =  Math.round(((Damageable) et).getHealth()*10)/10.0;
					if(health==0.0) {
						Location l = et.getLocation();
						Location pl = Bukkit.getPlayer("Jonosa").getLocation();
						
						if(l.distance(pl) < 32) {
							Bukkit.broadcastMessage(e.getCause().name());
						}
						
						
					}
					et.setCustomName("§c"+health);
					et.setCustomNameVisible(true);
				}
				
			}, 0);
			
			
		}*/
		if(cause.equals(DamageCause.BLOCK_EXPLOSION) 
		|| cause.equals(DamageCause.ENTITY_EXPLOSION)) {
			if(et instanceof Player) {
				if(e.getDamage() > 38)
					e.setDamage(e.getDamage()/2.5);
				else
					e.setDamage(e.getDamage()/2);
			}
		}
		else if(cause.equals(DamageCause.FALLING_BLOCK)) {
				double damage;
				if(e.getDamage() > 5)
					damage = 5;
				else 
					damage = e.getDamage()/2;
				
				e.setDamage(damage);
		}
		else if(cause.equals(DamageCause.FIRE)
		/*|| cause.equals(DamageCause.FIRE_TICK)*/) {
			if(et instanceof Skeleton || et instanceof Stray || et instanceof Phantom || et instanceof Pillager || et instanceof Piglin) {
				//long time = et.getWorld().getTime();
				//if(time < 23500 && time > 13000 || et.getLocation().getBlock().getLightFromSky() < 15) {
					e.setCancelled(true);
					et.setFireTicks(0);
				//}
			}
		}
	}
	
	/*@EventHandler
	public void onRemoveFallingblock(EntityRemoveFromWorldEvent e) {
		if(e.getEntityType().equals(EntityType.FALLING_BLOCK)) {
			
			Location loc = e.getEntity().getLocation();
			
			Bukkit.broadcastMessage((loc.getBlockX() - loc.getX()) + " " + (loc.getY() - loc.getBlockY()) + " "+ (loc.getBlockZ() - loc.getZ()));
			
			if(loc.getY() - loc.getBlockY() > 0) {
				loc.setY(loc.getBlockY()+1);
			}
			if(loc.getBlock().getType().isAir()) {
				loc.getBlock().setType(Material.BRICKS);
			}
			//e.getHandlers().getRegisteredListeners();
		}
	}*/
}
