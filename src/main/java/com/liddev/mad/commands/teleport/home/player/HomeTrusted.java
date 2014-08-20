package com.liddev.mad.commands.teleport.home.player;

import com.liddev.mad.core.MadCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Renlar <liddev.com>
 */
public class HomeTrusted extends MadCommand {

  public HomeTrusted(ConfigurationSection config) {
    super(config);
  }

  @Override
  public boolean run(CommandSender sender, String alias, String[] args) {
    //TODO: implement home trusted
    return false;
  }

}
