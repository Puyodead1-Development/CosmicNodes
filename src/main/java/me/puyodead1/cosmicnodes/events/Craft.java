package me.puyodead1.cosmicnodes.events;

import org.bukkit.event.Listener;

public class Craft implements Listener {

//	private final CosmicNodes cn = CosmicNodes.getPlugin();
//
//	@EventHandler
//	public void craftItemEvent(CraftItemEvent e) {
//		final CraftingInventory ci = e.getInventory();
//		e.setCancelled(true);
//
//		if (cn.getConfig().getBoolean("settings.resource only crafting")) {
//			final ItemStack[] matrix = ci.getMatrix();
//			if (matrix != null)
//				for (final ItemStack is : matrix)
//					if (is != null)
//						if (is.getItemMeta().getLore() == null || is.getItemMeta().getLore().contains("Cosmetic"))
//							return;
//						else if (is.getItemMeta().getLore().get(0).contains("&7Resource")
//								&& !is.getItemMeta().getLore().contains("Cosmetic"))
//							e.setCancelled(false);
//		}
//	}
}
