package me.puyodead1.cosmicnodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.puyodead1.cosmicnodes.utils.Holograms;
import me.puyodead1.cosmicnodes.utils.Node;
import me.puyodead1.cosmicnodes.utils.UMaterial;
import me.puyodead1.cosmicnodes.utils.Utils;

public class ActiveResourceNode {

	private CosmicNodes cn = CosmicNodes.getPlugin();

	private Node node;
	private Location location;
	private UUID uuid;
	private long cooldown;
	private Player player;
	private Holograms hologram;

	public int task;

	private static List<ActiveResourceNode> activeResourceNodes = new ArrayList<ActiveResourceNode>();

	public ActiveResourceNode(Player player, Node node, Location location) {
		this(player, UUID.randomUUID(), node, location, System.currentTimeMillis() + node.getRespawnTime() * 1000, true);
	}

	public ActiveResourceNode(Player player, UUID uuid, Node node, Location location, long cooldown, boolean place) {
		this.player = player;
		this.uuid = uuid;
		this.node = node;
		this.location = location;
		this.cooldown = cooldown;
		
		if(place) {
			place();
		} else {
			run();
		}

		this.hologram = new Holograms(this.location);
		activeResourceNodes.add(this);
		hologram.run();
	}
	
	public void place() {
		final Block b = location.getWorld().getBlockAt(location);
		final Material m = node.getNode_block();
		final ItemStack is = new ItemStack(m);
		b.setType(is.getType());
		final BlockState bs = b.getState();
		bs.setRawData(is.getData().getData());
		bs.update();
		
		run();
	}

	public void run() {
		this.task = Bukkit.getScheduler().scheduleSyncDelayedTask(cn, () -> {
			final Block b = location.getWorld().getBlockAt(location);
			final UMaterial u = UMaterial.match(node.getHarvest_block().toString());
			final ItemStack is = u.getItemStack();
			b.setType(is.getType());
			final BlockState bs = b.getState();
			bs.setRawData(is.getData().getData());
			bs.update();
			location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location.clone().add(0.5, 1, 0.5), 50, b.getBlockData());
			Bukkit.getScheduler().cancelTask(hologram.getTask());
			hologram.getLine().setText(Utils.formatText(cn.getConfig().getString("messages.harvestable hologram").replace("{TYPE}", Utils.capitalizeFirstLetter(node.getType()))));
		}, 20 * node.getRespawnTime());
	}

	public void harvest(Location location) {
		ActiveResourceNode arn = ActiveResourceNode.valueOf(location);
		arn.setCooldown(System.currentTimeMillis() + arn.getNode().getRespawnTime() * 1000);
		final Block b = arn.getLocation().getWorld().getBlockAt(arn.getLocation());
		final Material m = arn.getNode().getNode_block();
		final ItemStack is = new ItemStack(m);
		b.setType(is.getType());
		final BlockState bs = b.getState();
		bs.setRawData(is.getData().getData());
		bs.update();
		arn.run();
		hologram.run();
	}

	public void delete() {
		Bukkit.getScheduler().cancelTask(task);
		getLocation().getWorld().getBlockAt(getLocation()).setType(Material.AIR);
//		Bukkit.getConsoleSender().sendMessage("holograms: " + Objects.isNull(hologram) + "");
//		Bukkit.getConsoleSender().sendMessage("hologram: " + Objects.isNull(hologram.getHologram()) + "");
//		Bukkit.getConsoleSender().sendMessage("hologram valueo: " + Objects.isNull(Holograms.valueof(hologramLocation)) + "");
//		Bukkit.getConsoleSender().sendMessage("hologram valueOF: " + Objects.isNull(Holograms.valueOf(hologramLocation)) + "");
		activeResourceNodes.remove(this);
		hologram.getHologram().delete();
	}

	public Node getNode() {
		return node;
	}

	public Location getLocation() {
		return location;
	}

	public UUID getUuid() {
		return uuid;
	}

	public long getCooldown() {
		return cooldown;
	}

	public Player getPlayer() {
		return player;
	}

	public int getTask() {
		return task;
	}

	public static List<ActiveResourceNode> getActiveResourceNodes() {
		return activeResourceNodes;
	}

	public static ActiveResourceNode valueOf(Location loc) {
		for (ActiveResourceNode arn : getActiveResourceNodes()) {
			if (arn.getLocation().equals(loc))
				return arn;
		}
		return null;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}
}
