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
import org.bukkit.inventory.ItemStack;

import me.puyodead1.cosmicnodes.ActiveResourceNode;
import me.puyodead1.cosmicnodes.CosmicNodes;
import me.puyodead1.cosmicnodes.utils.Node;
import me.puyodead1.cosmicnodes.utils.PSPlayer;
import me.puyodead1.cosmicnodes.utils.UMaterial;
import me.puyodead1.cosmicnodes.utils.Utils;

public class BlockBreak implements Listener {

	private CosmicNodes cn = CosmicNodes.getPlugin();

	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		final Player player = e.getPlayer();
		final Block b = e.getBlock();
		final Location bLoc = b.getLocation();
		final Location pLoc = player.getLocation();

		PSPlayer pdata = PSPlayer.getPlayer(player.getUniqueId());
		YamlConfiguration pc = pdata.config;
		ConfigurationSection section = pc.getConfigurationSection("nodes");

		if (section != null) {
			for (String nodeName : section.getKeys(false)) {
				String p = nodeName + ".";
				Location location = new Location(Bukkit.getWorld(section.getString(p + "location.world")),
						section.getDouble(p + "location.x"), section.getDouble(p + "location.y"),
						section.getDouble(p + "location.z"));

				if (location != null && bLoc.equals(location)) {
					final ActiveResourceNode arn = ActiveResourceNode.valueOf(bLoc);
					
					if(arn != null) {
						Node node = arn.getNode();
						if (b.getType().equals(UMaterial.valueOf(node.getHarvest_block().name()).getMaterial()) && !player.isSneaking()) {
							e.setCancelled(true);

							// Does this need to be UMaterial?
							arn.harvest(bLoc);
							arn.getLocation().getWorld().dropItemNaturally(bLoc.add(0, 1, 0), UMaterial.valueOf(node.getLoot().name()).getItemStack());
						} else if (player.isSneaking()) {
							// Player is destroying the node.
							e.setCancelled(true);
							bLoc.getWorld().dropItemNaturally(bLoc, node.getItem());
							arn.delete();
							b.setType(Material.AIR);
							Utils.sendMessage(player, cn.getConfig().getString("messages.node destroyed").replace("{TYPE}",
									Utils.capitalizeFirstLetter(node.getType())), false);

							section.set(nodeName, null);
							pdata.save();
						} else if(b.getType().equals(UMaterial.valueOf(node.getNode_block().name()).getMaterial()) && !player.isSneaking()) {
							e.setCancelled(true);
							// tell how many seconds left
							final long c = System.currentTimeMillis();
							final long d = arn.getCooldown();
							final String type = Utils.capitalizeFirstLetter(node.getType());
							final String timeRemaining = Utils.getRemainingTime(d-c);
							
							Utils.sendMessage(player, cn.getConfig().getString("messages.not harvestable").replace("{TYPE}", type).replace("{TIME}", timeRemaining), false);
						}
					}
				}
			}
		}

		// change this to check if the broken block is a node and wheather the player is
		// sneaking
//		if(b.getType() == UMaterial.valueOf("OAK_LOG").getMaterial() && !player.isSneaking()) {
//			e.setCancelled(true);
//			b.setType(Material.OAK_PLANKS);
//		} else if(b.getType() == UMaterial.valueOf("OAK_PLANKS").getMaterial() && player.isSneaking()) {
//			e.setCancelled(true);
//			bLoc.getWorld().dropItemNaturally(bLoc, Node.valueOf("LOG").getItem());
//			b.setType(Material.AIR);
//		} else if(b.getType() == UMaterial.valueOf("OAK_PLANKS").getMaterial() && !player.isSneaking()) {
//			e.setCancelled(true);
//		}
	}
}
