package me.puyodead1.cosmicnodes.classes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.puyodead1.cosmicnodes.CosmicNodes;
import me.puyodead1.cosmicnodes.utils.Utils;

@Getter
public class CustomRecipe implements Listener {

	private final CosmicNodes cn = CosmicNodes.getPlugin();
	private static HashMap<String, CustomRecipe> customRecipes = new HashMap<String, CustomRecipe>();

	private final String name, type;
	private final int amount;
	private final Resource result;
	private final Scrap recipe;

	public CustomRecipe(String name, String type, int amount, Resource result, Scrap recipe) {
		this.name = name;
		this.type = type;
		this.amount = amount;
		this.result = result;
		this.recipe = recipe;

		Bukkit.getPluginManager().registerEvents(this, cn);
		customRecipes.put(name, this);
	}

	public static HashMap<String, CustomRecipe> getCustomRecipes() {
		return customRecipes;
	}

	@EventHandler
	public void craftItemEvent(CraftItemEvent e) {
		final Player p = (Player) e.getWhoClicked();
		final CraftingInventory ci = e.getInventory();
		final ItemStack r = e.getRecipe().getResult();
		if (r.isSimilar(result.getItem())) {
			int A = amount;
			final ItemStack[] m = ci.getMatrix();
			final boolean shapeless = true;
			int amount = 0;
			for (final ItemStack is : m) {
				if (is != null)
					if (shapeless)
						if (is.isSimilar(recipe.getItem()))
							amount += is.getAmount();
						else {
							e.setCancelled(true);
							p.updateInventory();
							return;
						}
			}
			if (shapeless && amount < A) {
				e.setCancelled(true);
				p.updateInventory();
				recipe.getItem().hasItemMeta();
				recipe.getItem().getItemMeta().hasDisplayName();
				recipe.getItem().getItemMeta().getDisplayName();
				recipe.getMaterial().name();
				p.sendMessage(Utils.formatText(cn.getConfig().getString("messages.not enough resources")));
			} else {
				int index = 0, removed = 0;
				if (e.isShiftClick()) {
					e.setCancelled(true);
					final int am = amount / this.amount;
					final ItemStack item = r.clone();
					item.setAmount(am);
					p.getInventory().addItem(item);
					A = am * this.amount + 1;
				}
				final ItemStack[] mat = m.clone();
				for (final ItemStack is : mat) {
					if (removed != A)
						for (int z = 1; z < A; z++)
							if (is != null && removed != A) {
								final int am = is.getAmount() - 1;
								if (am == 0)
									mat[index] = null;
								else
									is.setAmount(am);
								removed++;
							}
					index++;
				}
				ci.setMatrix(mat);
				p.updateInventory();
			}
		}
	}
}
