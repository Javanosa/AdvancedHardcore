package main.listener;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class SpiderController implements Listener{
	@EventHandler 
	public void onTarget(EntityTargetLivingEntityEvent e) {
		if(e.getEntityType().equals(EntityType.SPIDER))
			if(e.getEntity().hasMetadata("spidertrapspawned"))
				if(e.getTarget() instanceof Player)
					e.setCancelled(true);
	}
	
	@EventHandler 
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntityType().equals(EntityType.SPIDER))
			if(e.getEntity().hasMetadata("spidertrapspawned")) {
				e.getDrops().clear();
				e.setDroppedExp(0);
			}
	}
	
	@EventHandler 
	public void onBreak(BlockBreakEvent e) {
		if(e.getBlock().getType().equals(Material.COBWEB))
			if(e.getBlock().hasMetadata("spidertrapspawned"))
				e.setDropItems(false);
	}
}
