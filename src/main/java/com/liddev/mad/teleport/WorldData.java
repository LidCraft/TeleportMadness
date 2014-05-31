package com.liddev.mad.teleport;

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

    private ArrayList<JumpPoint> jumpPoints;
    
    private JumpPoint worldHome;
    
    private int defaultPlayerHomeLimit;

    public JumpPoint getHome(){
        return worldHome;
    }
    
    public void setHome(JumpPoint defaultHome){
        this.worldHome = defaultHome;
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
    
    public void setJumpPoints(ArrayList<JumpPoint> jumpPoints){
        this.jumpPoints = jumpPoints;
    }
    
    public ArrayList<JumpPoint> getJumpPoints(){
        return jumpPoints;
    }
    
    public void addJumpPoint(JumpPoint home){
        jumpPoints.add(home);
    }
    
    public JumpPoint getJumpPoint(String name){
        JumpPoint home = null;
        for(JumpPoint h : jumpPoints){
            if(h.getName().equalsIgnoreCase(name)){
                home = h;
            }
        }
        return home;
    }
    
    public void removeJumpPoint(String name){
        for(JumpPoint h : jumpPoints){
            if(h.getName().equalsIgnoreCase(name)){
                jumpPoints.remove(h);
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
