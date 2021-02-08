package main.commands;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gate implements CommandExecutor{
	
	public void changeItemBlockdata(ItemStack itemstack, String blockdata) {
		if(itemstack != null && itemstack.getType().isBlock() && !itemstack.getType().isAir()) {
			ItemMeta itemmeta = itemstack.getItemMeta();
			itemmeta.setDisplayName(blockdata);
			itemstack.setItemMeta(itemmeta);
		}
	}
	
	private ItemStack itemstack = null;
	private String message = null;
	private String blockdata = null;
	private BlockData bd = null;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				itemstack = p.getInventory().getItemInMainHand();
				
				if(args.length > 0) {
					switch(args[0]) {
						case "clearblockdata":
							changeItemBlockdata(itemstack, null);
							break;
						case "blockdata":
							if(args.length == 2) {
								blockdata = args[1];
								try {
									bd = Bukkit.createBlockData(itemstack.getType(), blockdata);
								}
								catch(IllegalArgumentException ec) {
									message = "§cFehler beim erstellen der Blockdata";
								}
								if(bd != null) {
									message = "§7Blockdaten: §b"+blockdata;
									changeItemBlockdata(itemstack, blockdata);
								}
								
							}
							else {
								Block block = p.getTargetBlockExact(8);
								if(block!=null) {
									blockdata = block.getBlockData().getAsString();
									int index = blockdata.indexOf("[");
									if(index > 0) {
										blockdata = blockdata.substring(index);
										changeItemBlockdata(itemstack, blockdata);
										message = "§7Blockdaten: §b"+blockdata;
									}
									else {
										message = "§cDieser Block besitzt keine Blockdaten";
									}
								}
								else
									message = "§cKein Block gefunden";
							}
							break;
					}
					
				}
				else
					message = "§7/gate <blockdata|clearblockdata> <customblockdata>";
					//message = cmd.getUsage();
						
				
					
					
			p.sendMessage(message);
				
		}
		
		return false;
	}
}
