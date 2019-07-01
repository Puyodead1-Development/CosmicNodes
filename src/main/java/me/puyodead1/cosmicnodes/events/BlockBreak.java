package me.puyodead1.cosmicnodes.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.puyodead1.cosmicnodes.CosmicNodes;
import me.puyodead1.cosmicnodes.classes.ActiveResourceNode;
import me.puyodead1.cosmicnodes.classes.Node;
import me.puyodead1.cosmicnodes.utils.PSPlayer;
import me.puyodead1.cosmicnodes.utils.Utils;

public class BlockBreak implements Listener {

	private final CosmicNodes cn = CosmicNodes.getPlugin();

	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		final Player player = e.getPlayer();
		final Block b = e.getBlock();
		final Location bLoc = b.getLocation();
		player.getLocation();

		final PSPlayer pdata = PSPlayer.getPlayer(player.getUniqueId());
		final YamlConfiguration pc = pdata.config;
		final ConfigurationSection section = pc.getConfigurationSection("nodes");

		if (section != null)
			for (final String nodeName : section.getKeys(false)) {
				final String p = nodeName + ".";
				final Location location = new Location(Bukkit.getWorld(section.getString(p + "location.world")),
						section.getDouble(p + "location.x"), section.getDouble(p + "location.y"),
						section.getDouble(p + "location.z"));

				if (location != null && bLoc.equals(location)) {
					final ActiveResourceNode arn = ActiveResourceNode.valueOf(bLoc);

					if (arn != null) {
						final Node node = arn.getNode();
						if (b.getType().equals(node.getHarvest_block()) && !player.isSneaking()) {
							e.setCancelled(true);

							// Does this need to be UMaterial?
							arn.harvest(bLoc);
							if (cn.getConfig().getBoolean("settings.auto pickup scrap"))
								player.getInventory().addItem(node.getLoot().getItem());
							else
								arn.getLocation().getWorld().dropItemNaturally(bLoc.add(0, 1, 0),
										node.getLoot().getItem());

						} else if (player.isSneaking()) {
							// Player is destroying the node.
							e.setCancelled(true);
							if (cn.getConfig().getBoolean("settings.auto pickup nodes"))
								player.getInventory().addItem(node.getItem());
							else
								bLoc.getWorld().dropItemNaturally(bLoc, node.getItem());
							arn.delete();
							b.setType(Material.AIR);
							Utils.sendMessage(player, cn.getConfig().getString("messages.node destroyed")
									.replace("{TYPE}", Utils.capitalizeFirstLetter(node.getType())), false);

							section.set(nodeName, null);
							pdata.save();
						} else if (b.getType().equals(node.getNode_block()) && !player.isSneaking()) {
							e.setCancelled(true);
							// tell how many seconds left
							final long c = System.currentTimeMillis();
							final long d = arn.getCooldown();
							final String type = Utils.capitalizeFirstLetter(node.getType());
							final String timeRemaining = Utils.getRemainingTime(d - c);

							Utils.sendMessage(player, cn.getConfig().getString("messages.not harvestable")
									.replace("{TYPE}", type).replace("{TIME}", timeRemaining), false);
						}
					}
				}
			}
	}
}
