package me.puyodead1.cosmicnodes.classes;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import lombok.Getter;
import me.puyodead1.cosmicnodes.CosmicNodes;
import me.puyodead1.cosmicnodes.utils.Utils;

@Getter
public class Holograms {

	private final CosmicNodes cn = CosmicNodes.getPlugin();

	private final Location location, originalLocation;
	private List<String> text;
	private final TextLine line;
	private static HashMap<Location, Holograms> holograms = new HashMap<Location, Holograms>();
	private final Hologram hologram;
	private ActiveResourceNode arn;
	private int task;

	public Holograms(Location location) {
		originalLocation = location;
		this.location = location.clone().add(0.5, 1.5, 0.5);
		hologram = HologramsAPI.createHologram(cn, this.location);
		line = hologram.appendTextLine("test");

		holograms.put(location, this);
	}

	public void run() {
		arn = ActiveResourceNode.valueOf(originalLocation);
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(cn, () -> {
			final long c = System.currentTimeMillis();
			final long d = arn.getCooldown();
			final String type = Utils.capitalizeFirstLetter(arn.getNode().getType());
			final String timeRemaining = Utils.getRemainingTime(d - c);

			line.setText(Utils.formatText(cn.getConfig().getString("messages.respawn in hologram")
					.replace("{TYPE}", type).replace("{TIME}", timeRemaining)));
		}, 0, 20);
	}

	public static Holograms valueof(Location location) {
		return getHolograms().get(location);
	}

	public static HashMap<Location, Holograms> getHolograms() {
		return holograms;
	}

	public void destroy() {
		hologram.delete();
	}

	public static void unload() {
		if (holograms != null)
			for (final Holograms a : holograms.values())
				a.getHologram().delete();
		holograms = null;
	}
}
