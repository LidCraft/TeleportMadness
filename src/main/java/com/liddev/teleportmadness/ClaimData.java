package com.liddev.teleportmadness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

    public void setWorld(World world) {
        this.worldUUID = world.getUID();
    }

    public World getWorld() {
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

    public boolean isClaimManager(Player p) {
        return TeleportMadness.getDataManager().getClaim(id).isManager(p);
    }

    public boolean isClaimManager(String p) {
        return TeleportMadness.getDataManager().getClaim(id).isManager(p);
    }

    public Player getClaimOwner() {
        return Bukkit.getPlayer(TeleportMadness.getDataManager().getClaim(id).getOwnerName());
    }

    public boolean hasPermission(Player p) {
        if (p.hasPermission("teleportMadness.home.bypass")
				|| permissionGroup.isWhitelisted(p) || permissionLevel == PermissionLevel.EVERYONE
				|| TeleportMadness.getDataManager().getClaim(id).getOwnerName().equals(p.getName())) {
            return true;
        }
        List<String> builder = new ArrayList<String>();
        List<String> container = new ArrayList<String>();
        List<String> access = new ArrayList<String>();
        List<String> manager = new ArrayList<String>();
        TeleportMadness.getDataManager().getClaim(id).getPermissions(builder, container, access, manager);
        if (permissionLevel.level <= PermissionLevel.ACCESSTRUST.level) {
            if (access.contains("public") || container.contains("everyone") || access.contains(p.getName())) {
                return true;
            }
        }
        if (permissionLevel.level <= PermissionLevel.CONTAINERTRUST.level) {
            if (container.contains("public") || container.contains("everyone") || container.contains(p.getName())) {
                return true;
            }

        }
        if (permissionLevel.level <= PermissionLevel.TRUST.level) {
            if (builder.contains("public") || builder.contains("everyone") || builder.contains(p.getName())) {
                return true;
            }

        }
        if (permissionLevel.level <= PermissionLevel.PERMISSIONTRUST.level) {
            if (manager.contains("public") || container.contains("everyone") || manager.contains(p.getName())) {
                return true;
            }

        }
        return false;
    }
}
