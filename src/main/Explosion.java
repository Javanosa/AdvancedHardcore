package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import main.listener.Events;

public class Explosion {
	
	public static int useold = 1;
	
	public static List<Block> changeBlocks(Location l, int r, int type) {
		List<Block> blocks = new ArrayList<Block>();
		World w = l.getWorld();
		// Centerblocks
		int bx=l.getBlockX();
		int by=l.getBlockY();
		int bz=l.getBlockZ();
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
		
		for(int x=ix; x <= mx; x++) {
			for(int y=iy; y <= my; y++) {
				for(int z=iz; z <= mz; z++) {
					d = ((bx-x) * (bx-x) + (bz-z) * (bz-z) + (by-y) * (by-y));
					if(d < r2 - Events.randomt.nextInt(1, 10 + 1)) {
						blocks.add(w.getBlockAt(x, y, z));
					}
				}
			}
		}
		Material m;
		Material nm;
		
		if(type == 0) {
			for(Block b : blocks) {
				nm = null;
				m = b.getType();
				
				switch(m) {
					case AIR:
					case CAVE_AIR:
					case VOID_AIR:
					case SNOW:
					case GRASS:
						Location l1 = b.getLocation();
						Material m1 = l1.getWorld().getBlockAt(l1.getBlockX(), l1.getBlockY() - 1, l1.getBlockZ()).getType();
						if(m1.isSolid() && !m1.equals(Material.ICE)) {
							int random = Events.randomt.nextInt(1, 3 + 1);
							if(random == 1)
							nm = Material.FIRE;	
						}
						break;
					case ICE:
					case PACKED_ICE:
					case BLUE_ICE:
						nm = Material.WATER;
						break;
				}
				if(nm!=null)
				b.setType(nm);
			}
		}
		else if(type == 1) {
			for(Block b : blocks) {
				nm = null;
				m = b.getType();
				
				switch(m) {
					case AIR:
					case CAVE_AIR:
					case VOID_AIR:
						Location l1 = b.getLocation();
						Material m1 = w.getBlockAt(l1.getBlockX(), l1.getBlockY() - 1, l1.getBlockZ()).getType();
						if(m1.isSolid() && !m1.equals(Material.ICE)) {
							nm = Material.SNOW;
						}
						break;
					case FIRE:
					/*case OAK_LEAVES:
					case DARK_OAK_LEAVES:
					case ACACIA_LEAVES:
					case JUNGLE_LEAVES:
					case BIRCH_LEAVES:
					case SPRUCE_LEAVES:*/
						nm = Material.ICE;
						break;
					case WATER:
						Location l2 = b.getLocation();
						Material m2 = w.getBlockAt(l2.getBlockX(), l2.getBlockY() + 1, l2.getBlockZ()).getType();
						if(m2.isAir()) {
							nm = Material.ICE;
						}
						break;
				}
				if(nm!=null)
				b.setType(nm);
			}
		}
		else if(type == 2) {
			boolean fire = false;
			for(Block b : blocks) {
				nm = null;
				m = b.getType();
				
				
				switch(m) {
					
					case FIRE:
						nm = Material.AIR;
						fire = true;
						break;
					
				}
				if(nm!=null)
				b.setType(nm);
			}
			if(fire)
			l.getWorld().playSound(l, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
		}
		
		return blocks;
	}
	
	public static void moveBlocks(Location loc, List<Block> blocks, Float r, Boolean fire) {
		
		
		Material i;
		Byte j;
		Location p;
		BlockData bd;
		
		World w = loc.getWorld();
		
		
		
		Float radius = 2 + (float) Math.sqrt(blocks.size()/10);
		boolean setfire = false;
		
		if(r != null) {
			radius = r;
		}
		if(fire != null) {
			setfire = fire;
		}
		
		int s = blocks.size();
		
		int ver = 1;
		
		if(s < 100) {
			radius = 2f;
		}
		else if(s < 300) {
			radius = 4f;
		}
		else if(s < 500) {
			ver = 2;
			radius = 5f;
		}
		else if(s < 1000) {
			ver = 3;
			radius = 8f;
		}
		else {
			ver = 7;
			radius = 10f;
		}
		
		//ver = 1;
		
		int dummy = 0;
		
		Inventory inv = null;
		
		//long starttime = System.nanoTime();
		for(int v=0; v < blocks.size(); v+=ver) {
			//v = v+5;
			
			Block b = blocks.get(v);
			
			/*if(b.getType().equals(Material.SPAWNER)) {
				blocks.remove(v);
			}
			else*/ if(b.getType().isSolid() || b.getType().equals(Material.FIRE)) {
				p = b.getLocation();
				if(!b.getType().equals(Material.TNT) && !b.getType().equals(Material.SPAWNER)) {
					i = b.getType();
					bd = b.getBlockData();
					
					
					if(i.equals(Material.GRASS_BLOCK) || i.equals(Material.GRASS_PATH)) {
						bd = Bukkit.createBlockData(Material.DIRT);
						
					}
					if(i.equals(Material.CHEST)) {
						bd = Bukkit.createBlockData(Material.CHEST);
					}
					
					if(useold == 1) {
						p.setY(p.getY()+0.99);
						FallingBlock fb = w.spawnFallingBlock(p, bd);
						fb.setDropItem(false);
						fb.setHurtEntities(true);
						Location l = fb.getLocation();
						
						double random = Events.randomt.nextDouble(0.5, 1);
						double x = (l.getX() - loc.getX());
						double y = (l.getY() - loc.getY() + 1.5); 
						double z = (l.getZ() - loc.getZ());
						
						Vector vector = new Vector(x, y, z);
						
						if(s < 30)
							vector.normalize().multiply(0.5);
						if(s < 200)
							vector.normalize().multiply(0.6);
						else
							vector.normalize().multiply(0.7);
						vector.setX(vector.getX()*random);
						vector.setZ(vector.getZ()*random);
						//vector.setY(vector.getY()+0.3);

						fb.setVelocity(vector);
					}
					else if(useold == 2){
						FallingBlock fb = w.spawnFallingBlock(p, bd);
						fb.setDropItem(false);
						fb.setHurtEntities(true);
						Location l = fb.getLocation();
						
						double random = Events.randomt.nextDouble(0.5, 1);
						double x = (l.getX() - loc.getX());
						double y = (l.getY() - loc.getY() + 1.5); 
						double z = (l.getZ() - loc.getZ());
						
						Vector vector = new Vector(x, y, z);
						
						if(s < 30)
							vector.normalize().multiply(0.5);
						else
							vector.normalize().multiply(0.6);

						vector.setX(vector.getX()*random);
						vector.setZ(vector.getZ()*random);
						vector.setY(vector.getY()+0.3);

						fb.setVelocity(vector);
					}
					
					
					//p.setZ(p.getZ()+1);
				
							
							
							//p.getBlock().setType(Material.BRICK);
							//Bukkit.broadcastMessage(p.getBlock().toString());
							//p.getWorld().getBlockAt(p).setBlockData(Bukkit.createBlockData(Material.BRICK));
							
							
							
						
					
					
					//fb.teleport(new Location(p.getWorld(), p.getX(),p.getY()+1,p.getZ()));
					
					
					/*double x = (Math.random() - Math.random()) / 1.5;
                    double y = Math.random() / 1.5;
                    double z = (Math.random() - Math.random()) / 1.5;
                    
                    fb.setVelocity(new Vector(x, y, z));*/
					
					
							
					
					
					
					dummy++;
				}
				//else {
					// Notwendig, falls das Event cancelled ist
					/*TNTPrimed fb = (TNTPrimed) w.spawnEntity(p, EntityType.PRIMED_TNT);
					fb.setFuseTicks(20);*/
				//}
			}
		}
		
		
		//long endtime = System.nanoTime();
		//Bukkit.broadcastMessage(endtime-starttime+"");
		
		//Material m;
		
		/*Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
			@Override
			public void run() {
				for(Block b : blocks) {
					if(b.getType().equals(Material.STONE))
					Bukkit.getPlayer("Jonosa").sendBlockChange(b.getLocation(), Bukkit.createBlockData(Material.STONE));
				}
			}
			
		}, 20);*/
		/*for(Block b : blocks) {
			m = b.getType();*/
			// Notwendig, falls das Event cancelled ist.
			/*if(m.equals(Material.CHEST) || m.equals(Material.HOPPER) || m.equals(Material.FURNACE) || m.equals(Material.BLAST_FURNACE) 
				|| m.equals(Material.DISPENSER) || m.equals(Material.BARREL) || m.equals(Material.TRAPPED_CHEST) || m.equals(Material.SMOKER) || m.equals(Material.DROPPER)) {
				BlockState state = b.getState();
				
				Container cont = (Container) state;
                inv = cont.getSnapshotInventory();
                
                for(ItemStack item : inv.getContents()) {
                	if(item!=null) {
	                	Item itemob = (Item) w.spawnEntity(b.getLocation(), EntityType.DROPPED_ITEM);
	                	itemob.setItemStack(item);
	                	itemob.setInvulnerable(true);
                	}
                }
			}*/
			//b.setType(Material.AIR);
		//}
		
		Main.main.getLogger().info("§e Size > " + blocks.size()
				+ " §6 FinalSize > " + dummy
				+ " §e Reduce > " + ver
				+ " §6 NewRadius > " + radius);
		
		// Notwendig, falls das Event cancelled ist.
		//w.createExplosion(loc.getX(), loc.getY(), loc.getZ(), radius, setfire, false);
	}
	
}
