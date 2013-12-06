package com.liddev.teleportmadness;

import com.liddev.teleportmadness.Managers.Data;
import java.io.Serializable;
import java.util.UUID;
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

    private UUID worldUUID;

    //possible values 0 owner, 1 permissions trust, 2 trust, 3 container trust, 4 access trust, 5 everyone, default = 5
    private PermissionLevel permissionLevel;
    
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setWorldUUID(UUID world) {
        this.worldUUID = world;
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }
    
    public void setWorld(World world){
        this.worldUUID = world.getUID();
    }
    
    public World getWorld(){
        return Bukkit.getServer().getWorld(worldUUID);
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
        return Data.get().getClaim(id).isManager(p);
    }
    
    public Player getClaimOwner(){
        return Bukkit.getPlayer(Data.get().getClaim(id).getOwnerName());
    }
}
