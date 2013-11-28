package com.liddev.teleportmadness;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 *
 * @author Renlar < liddev.com >
 */
public class ClaimData implements Serializable {
    public final static int defaultPermissionLevel = 5;

    private long id;
    
    private PermissionGroup permissionGroup;

    private String worldName;

    //possible values -1 no-one, 0 owner, 1 permissions trust, 2 trust, 3 container trust, 4 access trust, 5 everyone, default = 5
    private int permissionLevel; //TODO: use grief prevention permissions instead of an integer equivalence.
    
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

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(int level) {
        if (level < -1) {
            level = -1;
        } else if (level > 5) {
            level = 5;
        }
        this.permissionLevel = level;
    }
}
