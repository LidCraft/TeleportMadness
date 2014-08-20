package com.liddev.mad.commands.teleport.home.player;

import com.liddev.mad.teleport.Invite;
import com.liddev.mad.teleport.JumpPoint;
import com.liddev.mad.teleport.LocationHandler;
import com.liddev.mad.core.MadCommand;
import com.liddev.mad.teleport.PlayerData;
import com.liddev.mad.teleport.TeleportMadness;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeInvite extends MadCommand {

  public HomeInvite(ConfigurationSection config) {
    super(config);
  }

  @Override
  public boolean run(CommandSender sender, String alias, String[] args) {
    Player player = (Player) sender;
    PlayerData invitee = TeleportMadness.getDataManager().getPlayerData(args[0]);
    PlayerData inviter = TeleportMadness.getDataManager().getPlayerData(player);
    JumpPoint jump = null;
    if (args.length == 1) {
      jump = LocationHandler.findNearestHome(player);
      if (jump == null) {
        sender.sendMessage("Unable to send invite.  You have no homes in this world.");
      }
    } else {
      jump = inviter.getHome(args[1]);
    }
    if (jump != null) {
      Invite invite = new Invite(invitee, inviter, jump);
      invitee.addInvite(invite); //TODO: tell invitee about invite.
      return true;
    }
    return false;
  }
}
