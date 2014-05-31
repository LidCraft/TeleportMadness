package com.liddev.mad.teleport;

import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimResizeEvent;
import me.ryanhamshire.GriefPrevention.events.NewClaimCreated;
import me.ryanhamshire.GriefPrevention.events.ClaimModifiedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author Renlar < liddev.com >
 */
public class ClaimListener implements Listener{
    private TeleportMadness plugin;
    
    public ClaimListener(TeleportMadness plugin){
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onClaimCreate(NewClaimCreated event){
        plugin.getDataManager().createClaim(event.getClaim());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onClaimResize(ClaimResizeEvent event){
        plugin.getDataManager().updateClaim(event.getClaim());
        
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onClaimDelete(ClaimDeletedEvent event){
        plugin.getDataManager().onDeleteClaim(event.getClaim());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onClaimModified(ClaimModifiedEvent event){
        plugin.getDataManager().modifyClaim(event.getClaim());
    }
}
