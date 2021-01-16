package main.listener;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.metadata.FixedMetadataValue;

import main.Explosion;
import main.Main;

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
	@EventHandler
	public void onLightning(EntitySpawnEvent e) {
		if(e.getEntityType().equals(EntityType.LIGHTNING)) {
			Location loc = e.getEntity().getLocation();
			loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 3, true, true);
		}
	}
	@EventHandler
	public void onPlayerExplosionDamage(EntityDamageEvent e) {
		if(e.getCause().equals(DamageCause.BLOCK_EXPLOSION) 
		|| e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
			if(e.getEntity() instanceof Player) {
				e.setDamage(e.getDamage()/2);
			}
		}
		else if(e.getCause().equals(DamageCause.FALLING_BLOCK)) {
				double damage;
				if(e.getDamage() > 5)
					damage = 5;
				else 
					damage = e.getDamage()/2;
				
				e.setDamage(damage);
		}
	}
}
