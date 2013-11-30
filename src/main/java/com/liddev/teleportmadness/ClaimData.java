package com.liddev.teleportmadness;

import com.liddev.teleportmadness.Managers.Data;
import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar < liddev.com >
 */
public class ClaimData implements Serializable {
    private long id;
    
    private PermissionGroup permissionGroup;

    private String worldName;

    //possible values 0 owner, 1 permissions trust, 2 trust, 3 container trust, 4 access trust, 5 everyone, default = 5
    private PermissionLevel permissionLevel;     
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setWorldName(String world) {
        this.worldName = world;
    }

    public String getWorldName() {
        return worldName;
    }
    
    public void setWorld(World world){
        this.worldName = world.getName();
    }
    
    public World getWorld(){
        return Bukkit.getServer().getWorld(worldName);
    }

    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevel level) {
        this.permissionLevel = level;
    }
    
    public boolean isClaimManager(Player p){
        return Data.get().getClaim(id).isManager(p.getName());
    }
    
    public Player getClaimOwner(){
        return Bukkit.getPlayer(Data.get().getClaim(id).getOwnerName());
    }
}
