package me.puyodead1.cosmicnodes.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.puyodead1.cosmicnodes.CosmicNodes;
import me.puyodead1.cosmicnodes.classes.ActiveResourceNode;
import me.puyodead1.cosmicnodes.utils.PSPlayer;
import me.puyodead1.cosmicnodes.utils.Utils;

public class PlayerQuit implements Listener {

	private final CosmicNodes cn = CosmicNodes.getPlugin();

	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		final long started = System.currentTimeMillis();
		final Player p = e.getPlayer();
		int UNLOADED = 0;

		final PSPlayer pdata = PSPlayer.getPlayer(p.getUniqueId());

		final ConfigurationSection section = pdata.config.getConfigurationSection("nodes");
		if (section != null && section.getKeys(false).size() != 0)
			for (final String s : section.getKeys(false)) {
				final String path = s + ".";
				final Location location = new Location(Bukkit.getWorld(section.getString(path + "location.world")),
						section.getDouble(path + "location.x"), section.getDouble(path + "location.y"),
						section.getDouble(path + "location.z"));

				if (location.getWorld().getBlockAt(location) != null) {
					final ActiveResourceNode arn = ActiveResourceNode.valueOf(location);
					arn.delete();
					UNLOADED++;
				}
			}
		pdata.save();
		Utils.sendConsole("&dUnloaded " + UNLOADED + " nodes and saved data for player: " + p.getName() + ".", started,
				true);
	}
}
