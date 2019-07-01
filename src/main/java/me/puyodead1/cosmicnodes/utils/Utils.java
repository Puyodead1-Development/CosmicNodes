package me.puyodead1.cosmicnodes.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import lombok.Getter;

public class Utils {

	@Getter
	private final static String prefix = "&7[&bCosmicNodes&7] ";

	public static String formatText(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static List<String> formatText(List<String> text) {
		final List<String> formattedText = new ArrayList<String>();
		for (final String s : text)
			formattedText.add(formatText(s));
		return formattedText;
	}

	public static void sendMessage(Player player, String text, boolean prefix) {
		if (prefix)
			player.sendMessage(formatText(getPrefix() + text));
		else
			player.sendMessage(formatText(text));
	}

	public static void sendMessage(Player player, List<String> text, boolean prefix) {
		if (prefix)
			for (final String s : text)
				player.sendMessage(formatText(getPrefix() + s));
		else
			for (final String s : text)
				player.sendMessage(formatText(s));
	}

	public static void sendConsole(String text, boolean prefix) {
		if (prefix)
			Bukkit.getConsoleSender().sendMessage(formatText(getPrefix() + text));
		else
			Bukkit.getConsoleSender().sendMessage(formatText(text));
	}

	public static void sendConsole(String text, long time, boolean prefix) {
		if (prefix)
			Bukkit.getConsoleSender().sendMessage(
					formatText(getPrefix() + text + " &e(took " + (System.currentTimeMillis() - time) + "ms)"));
		else
			Bukkit.getConsoleSender()
					.sendMessage(formatText(text + " &e(took " + (System.currentTimeMillis() - time) + "ms)"));
	}

	public static String capitalizeFirstLetter(String text) {
		return text.substring(0, 1).toUpperCase() + text.toLowerCase().substring(1);
	}

	/*
	 * From RandomSky by RandomHashTags
	 */
	public static String getRemainingTime(long time) {
		int sec = (int) TimeUnit.MILLISECONDS.toSeconds(time), min = sec / 60, hr = min / 60;
		final int d = hr / 24;
		hr -= d * 24;
		min -= hr * 60 + d * 60 * 24;
		sec -= min * 60 + hr * 60 * 60 + d * 60 * 60 * 24;
		final String dys = d > 0 ? d + "d " : "";
		final String hrs = hr > 0 ? hr + "h" + " " : "";
		final String mins = min != 0 ? min + "m " : "";
		final String secs = sec != 0 ? sec + "s" : "";
		return dys + hrs + mins + secs;
	}

	public long getDelay(String input) {
		input = input.toLowerCase();
		long l = 0;
		if (input.contains("d")) {
			final String[] s = input.split("d");
			l += getRemainingDouble(s[0]) * 1000 * 60 * 60 * 24;
			input = s.length > 1 ? s[1] : input;
		}
		if (input.contains("h")) {
			final String[] s = input.split("h");
			l += getRemainingDouble(s[0]) * 1000 * 60 * 60;
			input = s.length > 1 ? s[1] : input;
		}
		if (input.contains("m")) {
			final String[] s = input.split("m");
			l += getRemainingDouble(s[0]) * 1000 * 60;
			input = s.length > 1 ? s[1] : input;
		}
		if (input.contains("s"))
			l += getRemainingDouble(input.split("s")[0]) * 1000;
		return l;
	}

	public Double getRemainingDouble(String string) {
		string = ChatColor.stripColor(
				ChatColor.translateAlternateColorCodes('&', string).replaceAll("\\p{L}", "").replaceAll("\\p{Z}", "")
						.replaceAll("\\.", "d").replaceAll("\\p{P}", "").replaceAll("\\p{S}", "").replace("d", "."));
		return string == null || string.equals("") ? -1.00
				: Double.parseDouble(
						string.contains(".") && string.split("\\.").length > 1 && string.split("\\.")[1].length() > 2
								? string.substring(0, string.split("\\.")[0].length() + 3)
								: string);
	}
}
