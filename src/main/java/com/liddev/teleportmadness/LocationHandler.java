package com.liddev.teleportmadness;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class LocationHandler {
    public static boolean isSafe(Location l){
        //TODO: implement smart checks for surrounding blocks
        return true;
    }
    public static boolean isSafe(JumpPoint j){
        //TODO: implement smart checks for surrounding blocks
        return true;
    }

    public static boolean teleport(Player p, JumpPoint j) {
        //TODO: implement teleport function preferably using multiverse
        return true;
    }

    public static JumpPoint findNearestHome(Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
