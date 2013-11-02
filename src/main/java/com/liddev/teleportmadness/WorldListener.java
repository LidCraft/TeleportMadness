package com.liddev.teleportmadness;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldEvent;

/**
 *
 * @author Renlar < liddev.com >
 */

public class WorldListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldCreate(WorldEvent event){
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldDelete(){
        
    }
}
