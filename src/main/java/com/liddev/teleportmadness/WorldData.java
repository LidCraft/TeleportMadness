package com.liddev.teleportmadness;

import java.io.Serializable;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 *
 * @author Renlar < liddev.com >
 */
public class WorldData implements Serializable {

    private Long id;

    private String name;

    private PermissionGroup permissionGroup;

    private ArrayList<JumpPoint> homes;
    
    private JumpPoint defaultHome;
    
    private int defaultPlayerHomeLimit;

    public JumpPoint getDefaultHome(){
        return defaultHome;
    }
    
    public void setDefaultHome(JumpPoint defaultHome){
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
    
    public void setHomes(ArrayList<JumpPoint> homes){
        this.homes = homes;
    }
    
    public ArrayList<JumpPoint> getHomes(){
        return homes;
    }
    
    public void addHome(JumpPoint home){
        homes.add(home);
    }
    
    public JumpPoint getHome(String name){
        JumpPoint home = null;
        for(JumpPoint h : homes){
            if(h.getName().equalsIgnoreCase(name)){
                home = h;
            }
        }
        return home;
    }
    
    public void removeHome(String name){
        for(JumpPoint h : homes){
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
