package com.liddev.mad.teleport;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class LocationHandler {

	public static boolean isSafe(Location l) {
		//TODO: implement smart checks for surrounding blocks
		return true;
	}

	public static boolean isSafe(JumpPoint j) {
		//TODO: implement smart checks for surrounding blocks
		return true;
	}

	public static boolean teleport(Player p, JumpPoint j) {
		//TODO: implement teleport function preferably using multiverse
		p.teleport(j.getLocation());
		return true;
	}

	public static JumpPoint findNearestHome(Player player) {
		JumpPoint nearest = null;
		double distance = Double.MAX_VALUE;
		List<JumpPoint> homes = TeleportMadness.getDataManager().getPlayerData(player).getHomes();
		for (JumpPoint h : homes) {
			double dist = player.getLocation().distanceSquared(h.getLocation());
			if (dist < distance) {
				nearest = h;
				distance = dist;
			}
		}
		return nearest;
	}
}
