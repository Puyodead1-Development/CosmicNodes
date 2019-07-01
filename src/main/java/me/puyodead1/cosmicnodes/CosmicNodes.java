package me.puyodead1.cosmicnodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.puyodead1.cosmicnodes.classes.ActiveResourceNode;
import me.puyodead1.cosmicnodes.classes.CustomRecipe;
import me.puyodead1.cosmicnodes.classes.Holograms;
import me.puyodead1.cosmicnodes.classes.Node;
import me.puyodead1.cosmicnodes.classes.Resource;
import me.puyodead1.cosmicnodes.classes.Scrap;
import me.puyodead1.cosmicnodes.events.BlockBreak;
import me.puyodead1.cosmicnodes.events.BlockPlace;
import me.puyodead1.cosmicnodes.events.Craft;
import me.puyodead1.cosmicnodes.events.PlayerJoin;
import me.puyodead1.cosmicnodes.events.PlayerQuit;
import me.puyodead1.cosmicnodes.utils.PSPlayer;
import me.puyodead1.cosmicnodes.utils.UMaterial;
import me.puyodead1.cosmicnodes.utils.Utils;

public class CosmicNodes extends JavaPlugin {

	public static CosmicNodes plugin;
	public boolean useHolographicDisplays = false;

	public static CosmicNodes getPlugin() {
		return plugin;
	}

	@Override
	public void onEnable() {
		final long started = System.currentTimeMillis();

		plugin = this;
		if (getConfig().getBoolean("settings.use holograms")
				&& Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
			useHolographicDisplays = true;
		else
			useHolographicDisplays = false;

		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
			Utils.sendConsole("&cHolographicDisplays is not installed or not enabled! Holograms support disabled!",
					true);
		else
			Utils.sendConsole("&aHolographicDisplays found, hologram support enabled!", true);

		InitEvents();
		InitCommands();
		InitConfig();
		LoadScraps();
		LoadNodes();
		LoadResources();
		InitCrafting();
		InitOnlinePlayers();

		Utils.sendConsole("&dEnable Finished.", started, true);
	}

	@Override
	public void onDisable() {
		System.currentTimeMillis();
		UnloadNodes();
		UnloadHolograms();
		UnloadResources();
		UnloadScraps();
	}

	public void InitEvents() {
		final long started = System.currentTimeMillis();

		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new PlayerQuit(), this);
		pm.registerEvents(new BlockPlace(), this);
		pm.registerEvents(new Craft(), this);

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

		for (final String s : getConfig().getConfigurationSection("nodes").getKeys(false)) {
			final String a = "nodes." + s + ".", name = getConfig().getString(a + "name");
			final List<String> lore = getConfig().getStringList(a + "lore");
			final Scrap loot = Scrap.valueOf(getConfig().getString(a + "loot"));
			final Material harvest_block = UMaterial.valueOf(getConfig().getString(a + "harvest block")).getMaterial(),
					node_block = UMaterial.valueOf(getConfig().getString(a + "node block")).getMaterial();
			final int respawnTime = getConfig().getInt(a + "respawn time");

			new Node(s, name, harvest_block, node_block, loot, respawnTime, lore);
			LOADED++;
		}
		Utils.sendConsole("&dLoaded " + LOADED + " Nodes.", started, true);
	}

	public void LoadScraps() {
		final long started = System.currentTimeMillis();
		int LOADED = 0;

		for (final String s : getConfig().getConfigurationSection("scraps").getKeys(false)) {
			final String path = "scraps." + s + ".", name = getConfig().getString(path + "name"),
					material = getConfig().getString(path + "item");
			final List<String> lore = getConfig().getStringList(path + "lore");
			final Node sourceNode = Node.valueOf(getConfig().getString(path + "source node"));

			new Scrap(s + "_SCRAP", name, sourceNode, material, lore);
			LOADED++;
		}

		Utils.sendConsole("&dLoaded " + LOADED + " scraps.", started, true);
	}

	public void InitOnlinePlayers() {
		final long started = System.currentTimeMillis();
		int LOADED = 0, startedNodes = 0;

		for (final Player p : Bukkit.getOnlinePlayers()) {
			final PSPlayer pdata = new PSPlayer(p.getUniqueId());
			final ConfigurationSection section = pdata.config.getConfigurationSection("nodes");
			if (section != null && section.getKeys(false).size() != 0) {
				for (final String s : section.getKeys(false)) {
					final String path = s + ".";
					final Location location = new Location(Bukkit.getWorld(section.getString(path + "location.world")),
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

		Utils.sendConsole("&dLoaded " + LOADED + " Online players, started " + startedNodes
				+ " player nodes and started " + Holograms.getHolograms().size() + " holograms.", started, true);
	}

	public void LoadResources() {
		final long started = System.currentTimeMillis();
		int LOADED = 0;

		final ConfigurationSection section = getConfig().getConfigurationSection("resources");
		if (section != null)
			for (final String s : section.getKeys(false)) {
				final String path = s + ".";
				final UMaterial material = UMaterial.valueOf(section.getString(path + "item"));
				final List<String> lore = section.getStringList(path + "lore");

				new Resource(s, material, lore);
				LOADED++;
			}

		Utils.sendConsole("&dLoaded " + LOADED + " resources.", started, true);
	}

	public void InitCrafting() {
		final long started = System.currentTimeMillis();
		int LOADED = 0;

		ConfigurationSection section = getConfig().getConfigurationSection("custom crafting");
		final ArrayList<String> recipesToRemove = new ArrayList<String>();

		if (section != null) {
			// Remove recipes
			for (final String s : section.getStringList("remove"))
				recipesToRemove.add(s);

			final Iterator<Recipe> a = getServer().recipeIterator();
			final List<Recipe> b = new ArrayList<>();
			while (a.hasNext()) {
				final Recipe r = a.next();
				final boolean one = r instanceof ShapedRecipe, two = r instanceof ShapelessRecipe;

				if (!one && !two || one && !((ShapedRecipe) r).getKey().getNamespace().equals("cosmicnodes")
						|| two && !((ShapelessRecipe) r).getKey().getNamespace().equals("cosmicnodes")) {
					final ItemStack result = r.getResult();
					final Material u = Material.valueOf(r.getResult().getType().name());
					if (!result.getType().equals(Material.AIR) && !recipesToRemove.contains(u.name().toLowerCase()))
						b.add(r);
				}
			}
			getServer().clearRecipes();
			for (final Recipe r : b)
				getServer().addRecipe(r);

			// Add new
			section = getConfig().getConfigurationSection("custom crafting.recipes");
			for (final String s : section.getKeys(false)) {
				final String path = s + ".";
				final String type = section.getString(path + "type");
				final Resource resource = Resource.valueOf(section.getString(path + "result"));
				final Scrap scrap = Scrap.valueOf(section.getString(path + "recipe"));
				final ItemStack result = resource.getItem(), recipe = scrap.getItem();
				final NamespacedKey key = new NamespacedKey(this, s + "_shapeless");
				final ShapelessRecipe r = new ShapelessRecipe(key, result);
				final int amount = section.getInt(path + "amount");
				r.addIngredient(1, recipe.getType());
				// add the recipe
				new CustomRecipe(s, type, amount, resource, scrap);
				Bukkit.addRecipe(r);
				LOADED++;
			}
		}
		Utils.sendConsole("&dLoaded " + LOADED + " Custom recipes.", started, true);
	}

	public void UnloadNodes() {
		final long started = System.currentTimeMillis();
		int UNLOADED = 0, PUNLOADED = 0;
		final int HUNLOADED = 0;

		for (Node node : Node.getNodes().values()) {
			node = null;

			UNLOADED++;
		}

		for (final Player p : Bukkit.getOnlinePlayers()) {
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
						if (useHolographicDisplays)
							Holograms.unload();
						PUNLOADED++;
					}
				}
		}

		Node.unload();

		Utils.sendConsole("&dUnloaded " + UNLOADED + " nodes, " + PUNLOADED + " active player nodes and unloaded "
				+ HUNLOADED + " holograms.", started, true);
	}

	public void UnloadScraps() {
		final long started = System.currentTimeMillis();
		Scrap.unload();

		Utils.sendConsole("&dUnloaded scraps.", started, true);
	}

	public void UnloadResources() {
		final long started = System.currentTimeMillis();
		Resource.unload();

		Utils.sendConsole("&dUnloaded resources.", started, true);
	}

	public void UnloadHolograms() {
		final long started = System.currentTimeMillis();
		Holograms.unload();

		Utils.sendConsole("&dUnloaded holograms.", started, true);
	}
}
