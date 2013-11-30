package com.liddev.teleportmadness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
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
    
    private JumpPoint defaultHome;
    
    private List<JumpPoint> homes;

    private int homeLimit;
    
    private List<Invite> invites;

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
    
    public JumpPoint getHome(String home){
        for(JumpPoint p: homes){
            if(p.getName().equals(home)){
                return p;
            }
        }
        return null;
    }
    
    public boolean hasHome(String home){
        if(getHome(home) != null){
            return true;
        }
        return false;
    }

    public void addHome(JumpPoint home) {
        homes.add(home);
    }
    
    public void setDefaultHome(JumpPoint defaultHome) {
        this.defaultHome = defaultHome;
    }

    public JumpPoint getDefaultHome() {
        return defaultHome;
    }
    
    public Invite getInvite(String player){
        for(Invite i : invites){
            if(i.getInvitee().getPlayer().getName().equals(player)){
                return i;
            }
        }
        return null;
    }
    
    public void setInvites(List<Invite> invites){
        this.invites = invites;
    }
    
    public void addInvite(Invite invite){
        invites.add(invite);
    }
    
    public void removeInvite(PlayerData inviter){
        removeInvite(inviter.getPlayerName());
    }
    public void removeInvite(String player){
        Invite i = getInvite(player);
        invites.remove(i);
    }
    
    public void removeInvite(){
        invites.remove(invites.size() - 1);
    }
    
    public void clearInvites(){
        invites = new ArrayList<Invite>();
    }
    
    public Invite getInvite(){
        if(!invites.isEmpty()){
            return invites.get(invites.size());
        }
        return null;
    }
}
