package me.puyodead1.cosmicnodes;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.puyodead1.cosmicnodes.classes.Node;
import me.puyodead1.cosmicnodes.utils.Utils;

public class CosmicNodesCommand implements CommandExecutor {

	private final CosmicNodes cn = CosmicNodes.getPlugin();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player player = sender instanceof Player ? (Player) sender : null;
		final String c = cmd.getName();
		final int l = args.length;

		if (player != null)
			if (c.equalsIgnoreCase("cosmicnodes"))
				if (l == 0) {
					if (player.hasPermission("cosmicnodes.cosmicnodes") || player.getName().equals("Puyodead1")) {
						Utils.sendMessage(player,
								Arrays.asList("------------------------", "&dAuthor: &ePuyodead1",
										"&dCosmicNodes Version: &e"
												+ CosmicNodes.getPlugin().getDescription().getVersion(),
										"&dServer Version: &e" + Bukkit.getServer().getVersion(),
										"&dHolographicDisplays Version: &e" + Bukkit.getPluginManager()
												.getPlugin("HolographicDisplays").getDescription().getVersion(),
										"", "------------------------"),
								true);
						return true;
					} else {
						Utils.sendMessage(player, cn.getConfig().getString("messages.no permission"), false);
						return false;
					}
				} else if (l == 1) {

				} else if (l == 3) {
					final String arg = args[0];
					Bukkit.getPlayer(args[1]);
					Bukkit.getPlayer(args[1]);
					final Node nodeType = Node.valueOf(args[2].toUpperCase());
					if (arg.equalsIgnoreCase("givenode"))
						if (player.hasPermission("cosmicnodes.givenode")) {
							if (nodeType != null) {
								player.getInventory().addItem(nodeType.getItem());
								return true;
							}
						} else {
							Utils.sendMessage(player, cn.getConfig().getString("messages.no permission"), false);
							return false;
						}
				}
		return false;
	}

}
