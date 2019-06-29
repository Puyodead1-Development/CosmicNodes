package me.puyodead1.cosmicnodes.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import me.puyodead1.cosmicnodes.CosmicNodes;

public class PSPlayer {

	private static CosmicNodes cn = CosmicNodes.getPlugin();

	public UUID uuid;
	public File file;
	public YamlConfiguration config;

	public static HashMap<UUID, PSPlayer> players = new HashMap<>();
	private static final String dataFolder = cn.getDataFolder() + File.separator + "data";

	public PSPlayer(UUID uuid) {
		this.uuid = uuid;
		final File pf = new File(dataFolder, uuid.toString() + ".yml");

		if (!players.containsKey(uuid)) {
			if (!pf.exists()) {
				try {
					final File folder = new File(PSPlayer.dataFolder);
					if (!folder.exists()) {
						folder.mkdir();
					}
					pf.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					Utils.sendConsole("&cFailed to create player data file for "
							+ Bukkit.getPlayer(uuid).getName(), false);
				}
			}

			file = new File(dataFolder, uuid.toString() + ".yml");
			config = YamlConfiguration.loadConfiguration(file);
			players.put(uuid, this);
		}
	}

	public static PSPlayer getPlayer(UUID uuid) {
		return players.getOrDefault(uuid, new PSPlayer(uuid));
	}

	public void save() {
		try {
			config.save(file);
			config = YamlConfiguration.loadConfiguration(file);
		} catch (IOException e) {
			Utils.sendConsole("&cFailed to save player data for " + Bukkit.getPlayer(uuid).getName(), true);
			e.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}

	public YamlConfiguration getConfig() {
		return config;
	}
	
}
