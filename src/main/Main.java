package main;

import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Spider;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import main.commands.Biomelist;
import main.commands.Fly;
import main.commands.GetItem;
import main.commands.Heal;
import main.commands.ReplaceBiome;
import main.commands.Speed;
import main.listener.EggThrowListener;
import main.listener.Events;
import main.listener.ExplosionListener;
import main.listener.ProjectileEvents;
import main.listener.SpiderController;


public class Main extends JavaPlugin{
	
	public static Main main;
	
	public PluginManager pm = Bukkit.getPluginManager();
	
	@Override
	public void onEnable() {
	    
	    pm.registerEvents(new Events(), this);
	    pm.registerEvents(new ExplosionListener(), this);
	    pm.registerEvents(new EggThrowListener(), this);
	    pm.registerEvents(new ProjectileEvents(), this);
	    pm.registerEvents(new SpiderController(), this);
	    
	    this.getCommand("replacebiome").setExecutor(new ReplaceBiome());
		this.getCommand("biomeliste").setExecutor(new Biomelist());
		this.getCommand("getitem").setExecutor(new GetItem());
		this.getCommand("speed").setExecutor(new Speed());
		this.getCommand("fly").setExecutor(new Fly());
		this.getCommand("heal").setExecutor(new Heal());
		
		Recipes.register_recipes();
		
		getLogger().info("§cAdvancedHardcore wurde erfolgreich geladen");
	}
	@Override
	public void onLoad() {
		main = this;
	}
	
	@Override
	public void onDisable() {
		ProjectileEvents.ice_blocks.forEach((UUID uuid, List list) -> {
			List<Block> rlist = list;
			for(Block b : rlist) {
				if(b.getType().equals(Material.ICE) || b.getType().equals(Material.WATER)) 
				b.setType(Material.AIR);
			}
			//ProjectileEvents.ice_blocks.remove(uuid);
		});
		
		EggThrowListener.spider_blocks.forEach((UUID uuid, List list) -> {
			List<Block> rlist = list;
			for(Block b : rlist) {
				if(b.getType().equals(Material.COBWEB)) 
				b.setType(Material.AIR);
			}
			//EggThrowListener.spider_blocks.remove(uuid);
		});
		
		EggThrowListener.spider_entitys.forEach((UUID uuid, List list) -> {
			List<Spider> rlist = list;
			for(Spider s : rlist) {
				s.remove();
			}
			//EggThrowListener.spider_entitys.remove(uuid);
		});
	}
}