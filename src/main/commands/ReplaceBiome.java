package main.commands;

import org.bukkit.Chunk;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplaceBiome implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if(p.isOp()) {
					Chunk c = p.getLocation().getChunk();
					Biome r=null;
					Biome s=null;
					if(args.length>=2) {
						try {
						r = Biome.valueOf(args[0].toUpperCase());
						s = Biome.valueOf(args[1].toUpperCase());
						
						}
						catch(IllegalArgumentException e){
							p.sendMessage(e.toString()+"\n§cUngültiges Biom!");
						}
						int rd=0;
						if(args.length==3)
							rd = Integer.parseInt(args[2]);
						if(rd>24)
							rd=0;
						
						if(r!=null && s!=null) {
							
							int cxrd = c.getX() + rd + 1;
							int czrd = c.getZ() + rd + 1;
							
							int cxst = c.getX();
							int czst = c.getZ();
							int pow = rd*2+1;
							String one = "§7Im Chunk x§3"+c.getX()+"§7 z§3"+c.getZ()+"§7 wurde das Biom §c"+r+"§7 durch §a"+s+"§7 ersetzt";
							String more = "§7In §3"+pow*pow+" Chunks §7wurde das Biom §c"+r+"§7 durch §a"+s+"§7 ersetzt";
							
							if(rd==0)
								p.sendMessage(one);
							else
								p.sendMessage(more);
							String debug = " §7";
							for(int cx=cxst-rd ; cx < cxrd; cx++ ) {
								
								for(int cz=czst-rd; cz < czrd; cz++ ) {
									Chunk cc = p.getLocation().getWorld().getChunkAt(cx, cz);
									if(cx==cxst && cz==czst)
										debug = debug+ "§a■ §7";
									else
										debug = debug+ "■ §7";
									for(int x=0;x<16;x++) {
										for(int z=0;z<16;z++) {
											if(cc.getBlock(x,70,z).getBiome().equals(r))
												for(int y=0;y<256;y++) {
													cc.getBlock(x,y,z).setBiome(s);
												}
										}
									}
								}
								debug = debug + "\n";
							}
							if(rd<17)
							p.sendMessage(debug);
						}
					}
					else {
						p.sendMessage("§7/replacebiome <BiomeReplace> <BiomeSetTo> <chunkradius>");
					}
				}
			}
		
		return false;
	}
}
