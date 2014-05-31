package com.liddev.mad.teleport;

import java.io.Serializable;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar < liddev.com >
 */
public class JumpPoint implements Serializable {

    private long id;

    private UUID worldUUID;

    private JumpType type;

    private PermissionGroup permissionGroup;

    private String name;

    private String tpDenyMessage;

    private String tpAcceptMessage;

    private double x;

    private double y;

    private double z;

    private float pitch;

    private float yaw;

    public JumpPoint(Location loc, String name) {
        setLocation(loc);
        this.name = name;
    }

    public JumpPoint(Location loc, String name, JumpType type) {
        this(loc, name);
        this.type = type;
    }

    public void setType(JumpType type) {
        this.type = type;
    }

    public JumpType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTpDenyMessage() {
        return tpDenyMessage;
    }

    public void setTpDenyMessage(String tpDenyMessage) {
        this.tpDenyMessage = tpDenyMessage;
    }

    public String getTpAcceptMessage() {
        return tpAcceptMessage;
    }

    public void setTpAcceptMessage(String tpAcceptMessage) {
        this.tpAcceptMessage = tpAcceptMessage;
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }

    public void setWorldUUID(UUID worldName) {
        this.worldUUID = worldName;
    }
    
    public void setWorld(World world){
        this.worldUUID = world.getUID();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public final void setLocation(Location location) {
        this.worldUUID = location.getWorld().getUID();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public Location getLocation() {
        World world = Bukkit.getServer().getWorld(worldUUID);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }
    
    @Override
    public String toString(){
        return name + ": " + " X: " + x + " Y: " + y + " Z: " + z;
    }

    public String debugString() {
        return worldUUID + "::" + Bukkit.getServer().getWorld(worldUUID).getName() + ": " + toString() + " Pitch: " + pitch + " Yaw: " + yaw;
    }
}
