package com.liddev.mad.commands.teleport.home.player;

import com.liddev.mad.teleport.JumpPoint;
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
public class HomeDefaultSet extends MadCommand {

  public HomeDefaultSet(ConfigurationSection config) {
    super(config);
  }

  @Override
  public boolean run(CommandSender sender, String alias, String[] args) {
    Player player = (Player) sender;
    PlayerData data = TeleportMadness.getDataManager().getPlayerData(player);
    JumpPoint home = data.getHome(args[0]);
    if (home.equals(data.getDefaultHome())) {
      player.sendMessage("Home is already set to" + args[0]);
    } else if (home == null) {
      player.sendMessage("You do not have a home called " + args[0]);
    } else {
      data.setDefaultHome(home);
    }
    return true;
  }

}
