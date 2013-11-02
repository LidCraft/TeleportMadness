package com.liddev.teleportmadness;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 *
 * @author Renlar < liddev.com >
 */
@Entity()
@Table(name = "mad_world")
public class WorldData implements Serializable {

    @Id
    private Long id;

    @NotEmpty
    private String name;

    @OneToOne
    private PermissionGroup permissionGroup;

    @OneToMany
    private ArrayList<Home> homes;
    
    @OneToOne
    private Home defaultHome;
    
    @NotNull
    private int defaultPlayerHomeLimit;

    public Home getDefaultHome(){
        return defaultHome;
    }
    
    public void setDefaultHome(Home defaultHome){
        this.defaultHome = defaultHome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorld(World world) {
        this.name = world.getName();
    }

    public World getWorld() {
        return Bukkit.getWorld(name);
    }

    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
    }
    
    public void setHomes(ArrayList<Home> homes){
        this.homes = homes;
    }
    
    public ArrayList<Home> getHomes(){
        return homes;
    }
    
    public void addHome(Home home){
        homes.add(home);
    }
    
    public Home getHome(String name){
        Home home = null;
        for(Home h : homes){
            if(h.getName().equalsIgnoreCase(name)){
                home = h;
            }
        }
        return home;
    }
    
    public void removeHome(String name){
        for(Home h : homes){
            if(h.getName().equalsIgnoreCase(name)){
                homes.remove(h);
            }
        }
    }

    public int getDefaultPlayerHomeLimit() {
        return defaultPlayerHomeLimit;
    }

    public void setDefaultPlayerHomeLimit(int defaultPlayerHomeLimit) {
        this.defaultPlayerHomeLimit = defaultPlayerHomeLimit;
    }
}
