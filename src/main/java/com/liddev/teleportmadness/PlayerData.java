package com.liddev.teleportmadness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar < liddev.com >
 */
public class PlayerData implements Serializable {
    
    private long id;

    private UUID playerUUID;
 
    private Map<UUID, Integer> worldLimits;
    
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
        return Bukkit.getServer().getPlayer(playerUUID);
    }

    public void setPlayer(Player player) {
        this.playerUUID = player.getUniqueId();
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Map<UUID, Integer> getWorldLimits() {
        return worldLimits;
    }

    public void setWorldLimits(Map<UUID, Integer> worldLimits) {
        this.worldLimits = worldLimits;
    }

    public void setWorldLimit(UUID world, Integer limit) {
        worldLimits.put(world, limit);
    }

    public int getWorldLimit(String world) {
        return worldLimits.get(world);
    }

    public int getWorldLimit(World world) {
        return worldLimits.get(world.getUID());
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

    public Invite getInvite(String playerName) {
        for (Invite i : invites) {
            if (i.getInvitee().getPlayer().getName().equals(playerName)) {
                return i;
            }
        }
        return null;
    }
    
    public Invite getInvite(UUID player){
        for(Invite i : invites){
            if(i.getInvitee().getPlayer().getUniqueId().equals(player)){
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
        removeInvite(inviter.getPlayerUUID());
    }

    public void removeInvite(String player) {
        Invite i = getInvite(player);
        invites.remove(i);
    }

    public void removeInvite(UUID player){
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
