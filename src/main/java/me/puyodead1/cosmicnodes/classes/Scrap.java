package me.puyodead1.cosmicnodes.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import me.puyodead1.cosmicnodes.CosmicNodes;
import me.puyodead1.cosmicnodes.utils.UMaterial;
import me.puyodead1.cosmicnodes.utils.Utils;

@Getter
public class Scrap {

	public CosmicNodes cn = CosmicNodes.getPlugin();

	private static HashMap<String, Scrap> scraps = new HashMap<String, Scrap>();

	private final String type, name;
	private final Node sourceNode;
	private final Material material;
	private final List<String> lore;
	private final ItemStack item;

	public Scrap(String type, String name, Node sourceNode, String material, List<String> lore) {
		this.type = type;
		this.name = name;
		this.sourceNode = sourceNode;
		this.material = UMaterial.valueOf(material).getMaterial();
		this.lore = lore;

		final ItemStack is = new ItemStack(this.material);
		final ItemMeta im = is.getItemMeta();
		im.setDisplayName(Utils.formatText(this.name));
		final ArrayList<String> lores = new ArrayList<String>();
		for (final String s : this.lore)
			lores.add(Utils.formatText(s));
		im.setLore(lores);
		is.setItemMeta(im);

		item = is;

		scraps.put(type, this);
	}

	public static Scrap valueOf(String type) {
		return getScraps().get(type);
	}

	public static HashMap<String, Scrap> getScraps() {
		return scraps;
	}

	public static void unload() {
		scraps = null;
	}
}
