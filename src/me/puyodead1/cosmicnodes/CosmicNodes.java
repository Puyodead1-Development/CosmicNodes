package me.puyodead1.cosmicnodes;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.puyodead1.cosmicnodes.events.BlockBreak;
import me.puyodead1.cosmicnodes.events.BlockPlace;
import me.puyodead1.cosmicnodes.events.PlayerJoin;
import me.puyodead1.cosmicnodes.events.PlayerQuit;
import me.puyodead1.cosmicnodes.utils.Holograms;
import me.puyodead1.cosmicnodes.utils.Node;
import me.puyodead1.cosmicnodes.utils.PSPlayer;
import me.puyodead1.cosmicnodes.utils.UMaterial;
import me.puyodead1.cosmicnodes.utils.Utils;

public class CosmicNodes extends JavaPlugin {

	public static CosmicNodes plugin;
	public boolean useHolographicDisplays = false;

	public static CosmicNodes getPlugin() {
		return plugin;
	}

	public void onEnable() {
		final long started = System.currentTimeMillis();
		
		plugin = this;
		useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");

		if(!useHolographicDisplays) {
			Utils.sendConsole("&cHolographicDisplays is not installed or not enabled! Holograms will be disabled!", true);
		} else {
			Utils.sendConsole("&aHolographicDisplays found, holograms will be enabled!", true);
		}
		
		InitEvents();
		InitCommands();
		InitConfig();
		LoadNodes();
		InitOnlinePlayers();

		Utils.sendConsole("&dEnable Finished.", started, true);
	}

	public void onDisable() {
		final long started = System.currentTimeMillis();
		UnloadNodes();
	}

	public void InitEvents() {
		final long started = System.currentTimeMillis();

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new PlayerQuit(), this);
		pm.registerEvents(new BlockPlace(), this);

		Utils.sendConsole("&dLoaded Events.", started, true);
	}

	public void InitCommands() {
		final long started = System.currentTimeMillis();

		getCommand("cosmicnodes").setExecutor(new CosmicNodesCommand());

		Utils.sendConsole("&dLoaded Commands.", started, true);
	}

	public void InitConfig() {
		final long started = System.currentTimeMillis();

		getConfig().options().copyDefaults(true);
		saveDefaultConfig();

		Utils.sendConsole("&dLoaded Config.", started, true);
	}

	public void LoadNodes() {
		final long started = System.currentTimeMillis();
		int LOADED = 0;

		for (String s : getConfig().getConfigurationSection("nodes").getKeys(false)) {
			final String a = "nodes." + s + ".";
			final String name = getConfig().getString(a + "name");
			final List<String> lore = getConfig().getStringList(a + "lore");
			final Material loot = UMaterial.valueOf(getConfig().getString(a + "loot")).getMaterial();
			final Material harvest_block = UMaterial.valueOf(getConfig().getString(a + "harvest block")).getMaterial();
			final Material node_block = UMaterial.valueOf(getConfig().getString(a + "node block")).getMaterial();
			final int respawnTime = getConfig().getInt(a + "respawn time");

			new Node(s, name, harvest_block, node_block, loot, respawnTime, lore);
			LOADED++;
		}
		Utils.sendConsole("&dLoaded " + LOADED + " Nodes.", started, true);
	}

	public void InitOnlinePlayers() {
		final long started = System.currentTimeMillis();
		int LOADED = 0, startedNodes = 0;

		for (Player p : Bukkit.getOnlinePlayers()) {
			PSPlayer pdata = new PSPlayer(p.getUniqueId());
			ConfigurationSection section = pdata.config.getConfigurationSection("nodes");
			if (section != null && section.getKeys(false).size() != 0) {
				for (String s : section.getKeys(false)) {
					String path = s + ".";
					Location location = new Location(Bukkit.getWorld(section.getString(path + "location.world")),
							section.getDouble(path + "location.x"), section.getDouble(path + "location.y"),
							section.getDouble(path + "location.z"));

					if (location.getWorld().getBlockAt(location) != null) {
						new ActiveResourceNode(p, Node.valueOf(section.getString(path + "type")), location);
						startedNodes++;
					}
				}
				LOADED++;
			}
		}

		Utils.sendConsole("&dLoaded " + LOADED + " Online players, started " + startedNodes + " player nodes and started " + Holograms.getHolograms().size() +  " holograms.", started,
				true);
	}

	public void UnloadNodes() {
		final long started = System.currentTimeMillis();
		int UNLOADED = 0, PUNLOADED = 0, HUNLOADED = 0;

		for (Node node : Node.getNodes().values()) {
			node = null;

			UNLOADED++;
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			PSPlayer pdata = PSPlayer.getPlayer(p.getUniqueId());
			
			ConfigurationSection section = pdata.config.getConfigurationSection("nodes");
			if (section != null && section.getKeys(false).size() != 0) {
				for (String s : section.getKeys(false)) {
					String path = s + ".";
					Location location = new Location(Bukkit.getWorld(section.getString(path + "location.world")),
							section.getDouble(path + "location.x"), section.getDouble(path + "location.y"),
							section.getDouble(path + "location.z"));

					if (location.getWorld().getBlockAt(location) != null) {
						ActiveResourceNode arn = ActiveResourceNode.valueOf(location);
						arn.delete();
						Holograms.unload();
						PUNLOADED++;
					}
				}
			}
		}

		Utils.sendConsole("&dUnloaded " + UNLOADED + " nodes,  " + PUNLOADED + " active player nodes and unloaded " + HUNLOADED + " holograms.", started, true);
	}
}
