package me.puyodead1.cosmicnodes.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import me.puyodead1.cosmicnodes.ActiveResourceNode;
import me.puyodead1.cosmicnodes.CosmicNodes;

public class Holograms {

	private CosmicNodes cn = CosmicNodes.getPlugin();

	private Location location, originalLocation;
	private List<String> text;
	private TextLine line;
	private static List<Holograms> holograms = new ArrayList<Holograms>();
	private Hologram hologram;
	private ActiveResourceNode arn;
	private int task;

	public Holograms(Location location) {
		this.originalLocation = location;
		this.location = location.clone().add(0.5, 1.5, 0.5);
		this.hologram = HologramsAPI.createHologram(cn, this.location);
		this.line = hologram.appendTextLine("test");

		holograms.add(this);
	}

	public void run() {
		this.arn = ActiveResourceNode.valueOf(originalLocation);
		this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(cn, () -> {
			final long c = System.currentTimeMillis();
			final long d = arn.getCooldown();
			final String type = Utils.capitalizeFirstLetter(arn.getNode().getType());
			final String timeRemaining = Utils.getRemainingTime(d - c);

			line.setText(Utils.formatText(cn.getConfig().getString("messages.respawn in hologram").replace("{TYPE}", type).replace("{TIME}", timeRemaining)));
		}, 0, 20);
	}

	public static Hologram valueOf(Location location) {
		for (int i = 0; i < holograms.size(); i++) {
			Hologram h = holograms.get(i).getHologram();
			if (h.getLocation().equals(location)) {
				return h;
			}
		}
		return null;
	}

	public static Holograms valueof(Location location) {
		for (int i = 0; i < holograms.size(); i++) {
			Holograms h = holograms.get(i);
			if (h.getLocation().equals(location)) {
				return h;
			}
		}
		return null;
	}

	public Location getLocation() {
		return location;
	}

	public List<String> getText() {
		return text;
	}

	public TextLine getLine() {
		return line;
	}

	public static List<Holograms> getHolograms() {
		return holograms;
	}

	public Hologram getHologram() {
		return hologram;
	}

	public void destroy() {
		hologram.delete();
	}

	public static void unload() {
		if (holograms != null) {
			for (Holograms a : holograms) {
				a.getHologram().delete();
			}
		}
		holograms = null;
	}

	public int getTask() {
		return task;
	}
	
}
