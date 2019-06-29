package me.puyodead1.cosmicnodes.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.puyodead1.cosmicnodes.ActiveResourceNode;
import me.puyodead1.cosmicnodes.CosmicNodes;
import me.puyodead1.cosmicnodes.utils.PSPlayer;
import me.puyodead1.cosmicnodes.utils.Utils;

public class PlayerQuit implements Listener {

	private CosmicNodes cn = CosmicNodes.getPlugin();
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		final long started = System.currentTimeMillis();
		final Player p = e.getPlayer();
		int UNLOADED = 0;
		
		PSPlayer pdata = PSPlayer.getPlayer(p.getUniqueId());
		
		ConfigurationSection section = pdata.config.getConfigurationSection("nodes");
		if(section != null && section.getKeys(false).size() != 0) {
			for(String s : section.getKeys(false)) {
				String path = s + ".";
				Location location = new Location(Bukkit.getWorld(section.getString(path + "location.world")),
						section.getDouble(path + "location.x"), section.getDouble(path + "location.y"),
						section.getDouble(path + "location.z"));
				
				if(location.getWorld().getBlockAt(location) != null) {
					ActiveResourceNode arn = ActiveResourceNode.valueOf(location);
					arn.delete();
					UNLOADED++;
				}
			}
		}
		pdata.save();
		Utils.sendConsole("&dUnloaded " + UNLOADED + " nodes and saved data for player: " + p.getName() + ".", started, true);
	}
}
