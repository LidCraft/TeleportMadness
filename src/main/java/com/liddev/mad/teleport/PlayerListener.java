package com.liddev.mad.teleport;

import java.util.logging.Level;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Renlar < liddev.com >
 */
class PlayerListener implements Listener {

  TeleportMadness plugin;

  public PlayerListener(TeleportMadness aThis) {
    plugin = aThis;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerLogin(PlayerJoinEvent event) {
    plugin.getLogger().log(Level.FINEST, "Player {0} joined, registering...", event.getPlayer().getName());
    plugin.getDataManager().loadPlayer(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerKick(PlayerKickEvent event) {
    plugin.getLogger().log(Level.FINEST, "Player {0} was kicked, unloading player data...", event.getPlayer().getName());
    plugin.getDataManager().unloadPlayer(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    plugin.getLogger().log(Level.FINEST, "Player {0} quit, unloading player data...", event.getPlayer().getName());
    plugin.getDataManager().unloadPlayer(event.getPlayer());
  }
}
