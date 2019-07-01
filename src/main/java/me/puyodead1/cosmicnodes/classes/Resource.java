package me.puyodead1.cosmicnodes.classes;

import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import me.puyodead1.cosmicnodes.CosmicNodes;
import me.puyodead1.cosmicnodes.utils.UMaterial;
import me.puyodead1.cosmicnodes.utils.Utils;

@Getter
public class Resource {

	private final CosmicNodes cn = CosmicNodes.getPlugin();

	public static HashMap<String, Resource> resources = new HashMap<String, Resource>();

	private final String name;
	private final List<String> lore;
	private final UMaterial material;
	private final ItemStack item;

	public Resource(String name, UMaterial material, List<String> lore) {
		this.name = name;
		this.material = material;
		this.lore = lore;

		final ItemStack is = new ItemStack(material.getMaterial());
		final ItemMeta im = is.getItemMeta();
		im.setLore(Utils.formatText(lore));
		is.setItemMeta(im);
		item = is;

		resources.put(name, this);
	}

	public static HashMap<String, Resource> getResources() {
		return resources;
	}

	public static Resource valueOf(String name) {
		return getResources().get(name);
	}

	public static void unload() {
		resources = null;
	}
}
