package com.liddev.teleportmadness;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Renlar < liddev.com >
 */
@Entity()
@Table(name = "mad_home")
public class Home implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    private String worldName;

    @NotNull
    private String type; //global, world, personal, gift

    @OneToOne
    @JoinColumn(name="mad_home_permission_group")
    private PermissionGroup permissionGroup;

    @Length(max = 30)
    @NotEmpty
    private String name;

    @Length(max = 300)
    private String tpDenyMessage;

    @Length(max = 300)
    private String tpAcceptMessage;

    private double x;

    private double y;

    private double z;

    private float pitch;

    private float yaw;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
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

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
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

    public void setLocation(Location location) {
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public Location getLocation() {
        World world = Bukkit.getServer().getWorld(worldName);
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
    /*public Claim getClaim(){
     return claim;
     }
    
     public void setClaim(Claim claim){
     this.claim = claim;
     }*/
}
