package com.liddev.teleportmadness;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar < liddev.com >
 */
public class PlayerData implements Serializable {
    
    private long id;

    private String playerName;
 
    private Map<String, Integer> worldLimits;

    private List<JumpPoint> homes;

    private int homeLimit;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Player getPlayer() {
        return Bukkit.getServer().getPlayer(playerName);
    }

    public void setPlayer(Player player) {
        this.playerName = player.getName();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Map<String, Integer> getWorldLimits() {
        return worldLimits;
    }

    public void setWorldLimits(Map<String, Integer> worldLimits) {
        this.worldLimits = worldLimits;
    }

    public void setWorldLimit(String world, Integer limit) {
        worldLimits.put(world, limit);
    }

    public int getWorldLimit(String world) {
        return worldLimits.get(world);
    }

    public int getWorldLimit(World world) {
        return worldLimits.get(world.getName());
    }

    public boolean isPrivateAllowed() {
        return homeLimit != 0;
    }

    public int getHomeLimit() {
        return homeLimit;
    }

    public void setHomeLimit(int homeLimit) {
        this.homeLimit = homeLimit;
    }

    public void setHomes(List<JumpPoint> homes) {
        this.homes = homes;
    }

    public List<JumpPoint> getHomes() {
        return homes;
    }

    public void addHome(JumpPoint home) {
        homes.add(home);
    }
}
