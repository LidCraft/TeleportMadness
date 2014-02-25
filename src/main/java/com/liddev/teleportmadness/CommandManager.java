package com.liddev.teleportmadness;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Renlar <liddev.com>
 */
public class CommandManager implements CommandExecutor {

    private final TeleportMadness mad;
    private CommandTree commandTree;
    private SimpleCommandMap cmap;

    public CommandManager(TeleportMadness mad) {
        this.mad = mad;
        loadCMap();
        
        mad.getLogger().log(Level.INFO, "Building command structure.");
        commandTree = new CommandTree(TeleportMadness.getFileManager().getCommands());

        mad.getLogger().log(Level.INFO, "Registering commands with bukkit.");
        registerRootCommands(commandTree.getRootCommands());
    }

    private void registerRootCommands(List<MadCommand> commandList) {
        for (MadCommand mc : commandList) {
            mad.getLogger().log(Level.INFO, "Registering root command: {0}", mc.getName());
            register((Command) mc);
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String rootCommand, String[] args) {
        String[] command = new String[args.length + 1]; //TODO: validate sender.
        command[0] = rootCommand;

        for (int i = 1; i < command.length; i++) {
            command[i] = args[i - 1];
        }

        return commandTree.findAndRunCommand(cs, command);
    }

    /**
     *
     * @param commands the list of commands to register with bukkit.
     */
    public void register(List<Command> commands) {
        for (Command command : commands) {
            register(command);
        }
    }

    /**
     *
     * @param command the command to register with bukkit.
     * @return true if registration was successful.
     */
    public boolean register(Command command) {
        return cmap.register(TeleportMadness.getProp().getPrefix(), (Command) command);
    }

    //Allows access to protected command hash in bukkit.  Uses refection similar to:
    //https://forums.bukkit.org/threads/register-command-without-plugin-yml.112932/#post-1430463
    //https://forums.bukkit.org/threads/dynamic-registration-of-commands.65786/#post-1033134
    private void loadCMap() {  //Review. Possibly use CraftBukkit.getCommandMap()?
        try {
            final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            cmap = (SimpleCommandMap) f.get(Bukkit.getServer());
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TeleportMadness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TeleportMadness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(TeleportMadness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TeleportMadness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return the PluginCommand instance for name.
     */
    protected MadCommand customCommandFactory(ConfigurationSection data) {
        MadCommand madCommand;

        String className = data.getString("class");
        if (!(className == null) && !className.isEmpty()) {
            try {
                madCommand = (MadCommand) Class.forName(className).newInstance();

                madCommand.setDescription(data.getString("desc"));
                madCommand.setUsage(data.getString("use"));
                madCommand.setConsole(data.getBoolean("console"));
                madCommand.setPattern(new CommandPattern(data.getString("pattern"), data));
                madCommand.setAliases(data.getStringList("alias"));
                madCommand.setPermission(data.getString("perm"));
                madCommand.setExecutor(this);
                return madCommand;
            } catch (ClassNotFoundException ex) {
                mad.getLogger().log(Level.SEVERE, "Error: Command class " + data.getString("class") + " was not found, unable to initiate command.  May cause undesired results.", ex);
            } catch (InstantiationException ex) {
                mad.getLogger().log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                mad.getLogger().log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}