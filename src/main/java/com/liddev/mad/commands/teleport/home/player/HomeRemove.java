package com.liddev.mad.commands.teleport.home.player;

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
public class HomeRemove extends MadCommand {

  public HomeRemove(ConfigurationSection config) {
    super(config);
  }

  @Override
  public boolean run(CommandSender sender, String alias, String[] args) {
    String player;
    String home;
    PlayerData data;
    if (args.length == 2) {
      player = args[0];
      home = args[1];
      data = TeleportMadness.getDataManager().getPlayerData(((Player) sender).getName());
      if (data == null) {
        sender.sendMessage(player + " is not online, attempting to look up in database.");
        data = TeleportMadness.getDataManager().getPlayer(player);
      }
      if (data == null) {
        sender.sendMessage(player + " was not found in the database, name must be exact for database lookup to succeed.");
        return false;
      }
    } else {
      home = args[0];
      data = TeleportMadness.getDataManager().getPlayerData(((Player) sender).getName());
    }
    if (!data.removeHome(args[1])) {
      sender.sendMessage("Home, " + home + " was not found.");
      return false;
    }
    data.removeHome(home);
    return true;
  }

}
//TODO: continue from here.
