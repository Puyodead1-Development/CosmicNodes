package me.puyodead1.cosmicnodes.classes;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import me.puyodead1.cosmicnodes.CosmicNodes;
import me.puyodead1.cosmicnodes.utils.UMaterial;
import me.puyodead1.cosmicnodes.utils.Utils;

@Getter
public class ActiveResourceNode {

	private final CosmicNodes cn = CosmicNodes.getPlugin();

	private final Node node;
	private final Location location;
	private final UUID uuid;
	@Setter
	private long cooldown;
	private final Player player;
	private final Holograms hologram;

	public int task;

	// private static List<ActiveResourceNode> activeResourceNodes = new
	// ArrayList<ActiveResourceNode>();
	private static HashMap<Location, ActiveResourceNode> activeResourceNodes = new HashMap<Location, ActiveResourceNode>();

	public ActiveResourceNode(Player player, Node node, Location location) {
		this(player, UUID.randomUUID(), node, location, System.currentTimeMillis() + node.getRespawnTime() * 1000,
				true);
	}

	public ActiveResourceNode(Player player, UUID uuid, Node node, Location location, long cooldown, boolean place) {
		this.player = player;
		this.uuid = uuid;
		this.node = node;
		this.location = location;
		this.cooldown = cooldown;

		if (place)
			place();
		else
			run();

		hologram = cn.useHolographicDisplays ? new Holograms(this.location) : null;
		activeResourceNodes.put(location, this);
		if (hologram != null)
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
		task = Bukkit.getScheduler().scheduleSyncDelayedTask(cn, () -> {
			final Block b = location.getWorld().getBlockAt(location);
			final UMaterial u = UMaterial.match(node.getHarvest_block().toString());
			final ItemStack is = u.getItemStack();
			b.setType(is.getType());
			final BlockState bs = b.getState();
			bs.setRawData(is.getData().getData());
			bs.update();
			location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location.clone().add(0.5, 1, 0.5), 50,
					b.getBlockData());
			if (b.getType() == Material.OAK_LOG)
				location.getWorld().playSound(location, Sound.BLOCK_WOOD_BREAK, 50, 1);
			else if (b.getType() == Material.COBBLESTONE)
				location.getWorld().playSound(location, Sound.BLOCK_STONE_BREAK, 50, 1);
			else if (b.getType() == Material.COAL_ORE)
				location.getWorld().playSound(location, Sound.BLOCK_STONE_BREAK, 50, 1);
			else if (b.getType() == Material.IRON_ORE)
				location.getWorld().playSound(location, Sound.BLOCK_METAL_BREAK, 50, 1);
			else if (b.getType() == Material.GOLD_ORE)
				location.getWorld().playSound(location, Sound.BLOCK_METAL_BREAK, 50, 1);
			else if (b.getType() == Material.DIAMOND_ORE)
				location.getWorld().playSound(location, Sound.BLOCK_METAL_BREAK, 50, 1);

			if (hologram != null)
				Bukkit.getScheduler().cancelTask(hologram.getTask());
			hologram.getLine().setText(Utils.formatText(cn.getConfig().getString("messages.harvestable hologram")
					.replace("{TYPE}", Utils.capitalizeFirstLetter(node.getType()))));
		}, 20 * node.getRespawnTime());
	}

	public void harvest(Location location) {
		final ActiveResourceNode arn = ActiveResourceNode.valueOf(location);
		arn.setCooldown(System.currentTimeMillis() + arn.getNode().getRespawnTime() * 1000);
		final Block b = arn.getLocation().getWorld().getBlockAt(arn.getLocation());
		final Material m = arn.getNode().getNode_block();
		final ItemStack is = new ItemStack(m);
		b.setType(is.getType());
		final BlockState bs = b.getState();
		bs.setRawData(is.getData().getData());
		bs.update();
		arn.run();
		if (cn.useHolographicDisplays)
			hologram.run();
	}

	public void delete() {
		Bukkit.getScheduler().cancelTask(task);
		getLocation().getWorld().getBlockAt(getLocation()).setType(Material.AIR);
		activeResourceNodes.remove(location);
		if (hologram != null)
			hologram.getHologram().delete();
	}

	public static ActiveResourceNode valueOf(Location loc) {
		return getActiveResourceNodes().get(loc);
	}

	private static HashMap<Location, ActiveResourceNode> getActiveResourceNodes() {
		return activeResourceNodes;
	}

	public static void unload() {
		activeResourceNodes = null;
	}
}
