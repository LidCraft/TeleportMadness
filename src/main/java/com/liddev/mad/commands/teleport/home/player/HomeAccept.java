package com.liddev.mad.commands.teleport.home.player;

import com.liddev.mad.teleport.JumpPoint;
import com.liddev.mad.teleport.LocationHandler;
import com.liddev.mad.core.MadCommand;
import com.liddev.mad.teleport.PlayerData;
import com.liddev.mad.teleport.TeleportMadness;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeAccept extends MadCommand {

  public HomeAccept(ConfigurationSection config) {
    super(config);
  }

  @Override
  public boolean run(CommandSender sender, String alias, String[] args) {
    Player p = (Player) sender;
    Location l = p.getLocation();  //TODO: check if player can teleport from here.
    PlayerData data = TeleportMadness.getDataManager().getPlayerData(p);
    JumpPoint jump = null;
    if (args.length == 0) {
      jump = data.getInvite().getJumpPoint();
      if (jump == null) {
        p.sendMessage("Unable to teleport: You have no active invitations.");
        return false;
      }
      data.removeInvite();
    } else {
      data.getInvite(args[0]).getJumpPoint();
      if (jump == null) {
        p.sendMessage("Unable to teleport:" + args[0] + "has not invited you recently or does not exist.  Make sure you typed the name correctly and check if " + args[0] + " has invited you with /home invite.");
        return false;
      }
      data.removeInvite(args[0]);
    }
    return LocationHandler.teleport(p, jump);
  }
}
