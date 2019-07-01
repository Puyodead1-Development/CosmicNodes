package me.puyodead1.cosmicnodes.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.puyodead1.cosmicnodes.CosmicNodes;
import me.puyodead1.cosmicnodes.classes.ActiveResourceNode;
import me.puyodead1.cosmicnodes.classes.Node;
import me.puyodead1.cosmicnodes.utils.PSPlayer;
import me.puyodead1.cosmicnodes.utils.Utils;

public class BlockPlace implements Listener {

	private final CosmicNodes cn = CosmicNodes.getPlugin();

	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		final Player player = e.getPlayer();
		final Block b = e.getBlock();
		final Location bLoc = b.getLocation();
		final ItemStack is = e.getItemInHand();
		final Node node = Node.valueOf(is);

		if (node != null) {
			final PSPlayer pdata = PSPlayer.getPlayer(player.getUniqueId());
			final YamlConfiguration pc = pdata.config;
			final ConfigurationSection section = pc.getConfigurationSection("nodes");

			if (section == null || section.getKeys(false).size() == 0) {
				final String p = "nodes.0.";
				pc.set(p + "type", node.getType());
				pc.set(p + "location.world", bLoc.getWorld().getName());
				pc.set(p + "location.x", bLoc.getX());
				pc.set(p + "location.y", bLoc.getY());
				pc.set(p + "location.z", bLoc.getZ());
			} else {
				final String p = "nodes." + (section.getKeys(false).size() + 1) + ".";
				pc.set(p + "type", node.getType());
				pc.set(p + "location.world", bLoc.getWorld().getName());
				pc.set(p + "location.x", bLoc.getX());
				pc.set(p + "location.y", bLoc.getY());
				pc.set(p + "location.z", bLoc.getZ());
			}

			pdata.save();
			b.setType(node.getNode_block());
			new ActiveResourceNode(player, node, bLoc);

			Utils.sendMessage(player, cn.getConfig().getString("messages.node placed").replace("{TYPE}",
					Utils.capitalizeFirstLetter(node.getType())), false);
		}
	}
}
