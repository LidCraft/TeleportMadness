package com.liddev.teleportmadness;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.event.world.WorldInitEvent;
import com.onarandombox.MultiverseCore.event.MVWorldDeleteEvent;

/**
 *
 * @author Renlar < liddev.com >
 */
public class WorldListener implements Listener {

    
    TeleportMadness plugin;
    public WorldListener(TeleportMadness aThis) {
        plugin = aThis;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldDelete(MVWorldDeleteEvent e) {
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldInit(WorldInitEvent e){
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldLoad(WorldLoadEvent e){
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldUnload(WorldUnloadEvent e){
        
    }
}
