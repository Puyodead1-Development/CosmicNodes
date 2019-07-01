package me.puyodead1.cosmicnodes.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import me.puyodead1.cosmicnodes.utils.Utils;

@Getter
public class Node {

	public static HashMap<String, Node> nodes = new HashMap<String, Node>();

	private final String name, type;
	private final Material harvest_block, node_block;
	private final Scrap loot;
	private final int respawnTime;
	private final List<String> lore;
	private final ItemStack item;
	private final Node node;

	public Node(String type, String name, Material harvest_block, Material node_block, Scrap loot, int respawnTime,
			List<String> lore) {
		this.type = type;
		this.name = name;
		this.harvest_block = harvest_block;
		this.node_block = node_block;
		this.respawnTime = respawnTime;
		this.lore = lore;
		this.loot = loot;

		final ItemStack is = new ItemStack(harvest_block);
		final ItemMeta im = is.getItemMeta();
		im.setDisplayName(Utils.formatText(name));
		final ArrayList<String> lores = new ArrayList<String>();

		for (final String s : lore)
			lores.add(Utils.formatText(s).replace("{RESPAWN}", Utils.getRemainingTime(respawnTime * 1000)));

		im.setLore(lores);
		is.setItemMeta(im);

		item = is;
		node = this;
		nodes.put(type, this);
	}

	public static Node valueOf(String type) {
		return getNodes().get(type);
	}

	public static Node valueOf(ItemStack itemstack) {
		for (final Node n : getNodes().values())
			if (n.getItem().equals(itemstack))
				return n;
		return null;
	}

	public static HashMap<String, Node> getNodes() {
		return nodes;
	}

	public static void unload() {
		nodes = null;
	}
}
