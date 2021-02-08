package main;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Spider;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import io.netty.util.internal.ThreadLocalRandom;
import main.commands.Biomelist;
import main.commands.Fly;
import main.commands.Gate;
import main.commands.GetItem;
import main.commands.Heal;
import main.commands.ReplaceBiome;
import main.commands.Speed;
import main.commands.Trash;
import main.commands.toggleMethod;
import main.listener.EggThrowListener;
import main.listener.Events;
import main.listener.ExplosionListener;
import main.listener.GateListener;
import main.listener.ItemSaver;
import main.listener.ItemSaverNew;
import main.listener.ProjectileEvents;
import main.listener.SpiderController;

public class Main extends JavaPlugin{
	
	public static Main main;
	
	public static Logger log;
	
	public static ThreadLocalRandom random = ThreadLocalRandom.current();
	
	//FileConfiguration config = getConfig();
	
	public PluginManager pm = Bukkit.getPluginManager();
	
	@Override
	public void onEnable() {
		/*this.getConfig().options().copyDefaults(true);
	    this.saveConfig();*/
	    
	    saveDefaultConfig();
	    
	    main = this;
		log = Main.main.getLogger();
		
		
	    pm.registerEvents(new Events(), this);
	    pm.registerEvents(new ExplosionListener(), this);
	    pm.registerEvents(new EggThrowListener(), this);
	    pm.registerEvents(new ProjectileEvents(), this);
	    pm.registerEvents(new SpiderController(), this);
	    pm.registerEvents(new ItemSaver(), this);
	    pm.registerEvents(new GateListener(), this);
	    
	    this.getCommand("replacebiome").setExecutor(new ReplaceBiome());
	    this.getCommand("biomeliste").setExecutor(new Biomelist());
		this.getCommand("getitem").setExecutor(new GetItem());
		this.getCommand("speed").setExecutor(new Speed());
		this.getCommand("fly").setExecutor(new Fly());
		this.getCommand("heal").setExecutor(new Heal());
		
		this.getCommand("tglmethod").setExecutor(new toggleMethod());
		this.getCommand("trash").setExecutor(new Trash());
		this.getCommand("gate").setExecutor(new Gate());
		
		//Recipes.register_recipes();
		
		log.info("§cAdvancedHardcore alpha 0.93 wurde erfolgreich geladen");
	}
	/*@Override
	public void onLoad() {
		main = this;
		log = Main.main.getLogger();
	}*/
	
	@Override
	public void onDisable() {
		ItemSaver.saveTrashItems();
		//this.saveConfig();
		
		
		ProjectileEvents.ice_blocks.forEach((UUID uuid, List<Block> list) -> {
			for(Block b : list) {
				if(b.getType().equals(Material.ICE) || b.getType().equals(Material.WATER))
					b.setType(Material.AIR);
			}
			//ProjectileEvents.ice_blocks.remove(uuid);
		});
		
		EggThrowListener.spider_blocks.forEach((UUID uuid, List<Block> list) -> {
			for(Block b : list) {
				if(b.getType().equals(Material.COBWEB)) 
				b.setType(Material.AIR);
			}
			//EggThrowListener.spider_blocks.remove(uuid);
		});
		
		EggThrowListener.spider_entitys.forEach((UUID uuid, List<Spider> list) -> {
			for(Spider s : list) {
				s.remove();
			}
			//EggThrowListener.spider_entitys.remove(uuid);
		});
	}
}