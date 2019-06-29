package me.puyodead1.cosmicnodes.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Node {

	public static HashMap<String, Node> nodes = new HashMap<String, Node>();

	private String name, type;
	private Material harvest_block, node_block, loot;
	private int respawnTime;
	private List<String> lore;
	private ItemStack item;
	private Node node;

	public Node(String type, String name, Material harvest_block, Material node_block, Material loot, int respawnTime,
			List<String> lore) {
		this.type = type;
		this.name = name;
		this.harvest_block = harvest_block;
		this.node_block = node_block;
		this.respawnTime = respawnTime;
		this.lore = lore;
		this.loot = loot;

		ItemStack is = new ItemStack(harvest_block);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(Utils.formatText(name));
		ArrayList<String> lores = new ArrayList<String>();
		
		for (String s : lore) {
			lores.add(Utils.formatText(s).replace("{RESPAWN}", Utils.getRemainingTime(respawnTime * 1000)));
		}
		
		im.setLore(lores);
		is.setItemMeta(im);

		this.item = is;
		this.node = this;
		nodes.put(type, this);
	}

	public static Node valueOf(String type) {
		return getNodes().get(type) != null ? getNodes().get(type) : null;
	}

	public static Node valueOf(ItemStack itemstack) {
		for(Node n : getNodes().values()) {
			if(n.getItem().equals(itemstack)) {
				return n;
			}
		}
		return null;
	}
	
	public static HashMap<String, Node> getNodes() {
		return nodes;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Material getHarvest_block() {
		return harvest_block;
	}

	public Material getNode_block() {
		return node_block;
	}

	public Material getLoot() {
		return loot;
	}

	public int getRespawnTime() {
		return respawnTime;
	}

	public List<String> getLore() {
		return lore;
	}

	public ItemStack getItem() {
		return item;
	}

	public Node getNode() {
		return node;
	}
}
