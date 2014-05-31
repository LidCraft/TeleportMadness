package com.liddev.mad.core;

import com.liddev.mad.exceptions.InvalidPathException;
import com.liddev.mad.teleport.TeleportMadness;
import com.liddev.mad.util.Node;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.util.org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 *
 * @author Renlar <liddev.com>
 */
public class CommandManager implements CommandExecutor {

    private final TeleportMadness mad;
    private final CommandTree commandTree;
    private SimpleCommandMap cmap;

    public CommandManager(TeleportMadness mad) {
        this.mad = mad;
        loadCMap();
        
        mad.getLogger().log(Level.INFO, "Building command structure.");
        commandTree = new CommandTree();

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

        return findAndRunCommand(cs, command);
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
        return cmap.register(mad.getProp().getPrefix(), (Command) command);
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
     * Creates a MadCommand subclass instance from the information in a
     * commands.yml section.
     * 
     * @param data a commands.yml section
     * @return the PluginCommand instance for name.
     */
    protected MadCommand commandFactory(ConfigurationSection data) {
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

    protected boolean findAndRunCommand(CommandSender sender, String[] commandArray) {
        //TODO: reimplement command search and execute.
        
        //Get the deepest command match from the tree.
        Node currentNode = commandTree.getNode(commandArray, true);
        MadCommand command = (MadCommand)currentNode.data();
        StringBuilder usage = new StringBuilder(command.getUsage());
        
        if(currentNode.getParent() == null){
            if (sender instanceof Player) {
                sender.sendMessage("Unknown command. Type \"/help\" for help.");
            } else {
                sender.sendMessage("Unknown command. Type \"?\" for help.");
            }
            return false;
        }
        
        //
        int depth = commandTree.getDepth(currentNode);
        String[] arguments = ArrayUtils.subarray(commandArray, depth + 1, commandArray.length);
        
        while(!command.pattern().matches(arguments)){
            if(commandTree.isRootLevel(currentNode)){
                sender.sendMessage(usage.toString());
                return false;
            }
            currentNode = currentNode.getParent();
            depth = commandTree.getDepth(currentNode);
            arguments = ArrayUtils.subarray(commandArray, depth + 1, commandArray.length);
            command = (MadCommand)currentNode.data();
            usage.append(command.getUsage());
        }
        
        return command.run(sender, commandArray[depth], arguments);
    }
    
    

    /**
     * Takes an initialized command tree as arguments and loads all mad commands
     * configured in the commands.yml file by attaching them to the tree.
     *
     * @param commands the yaml configFile containing the command structure.
     */
    
    //TODO: fix failure with more than one root level yaml node.
    public void loadCommands(ConfigurationSection commands) {
        Set<String> keys = commands.getKeys(false);

        for (String key : keys) {
            ConfigurationSection commandSection = commands.getConfigurationSection(key);
            
            MadCommand command = commandFactory(commandSection);
            String[] path = getCommandPath(commands);
            try{
                commandTree.addCommand(path, command);
            }catch(InvalidPathException e){
                logInvalidPathException(e, path, command, commands);
            }
            try{
                if (commandSection.getBoolean("root")) {
                    commandTree.addCommand(new String[0], command);
                }
            }catch(InvalidPathException e){
                logInvalidPathException(e, path, command, commands);
            }

            if (commandSection.contains("sub")) {
                ConfigurationSection subCommand = commandSection.getConfigurationSection("sub");
                loadCommands(subCommand);
            }
        }
    }
    
    public static String[] getCommandPath(ConfigurationSection commandSection){
        ArrayList<String> path = new ArrayList<String>();
        String[] sectionPath = commandSection.getCurrentPath().split(".");
        for (String str : sectionPath) {
            if (!str.equals("sub")) {
                path.add(str);
            }
        }
        String[] pathArray = new String[path.size()];
        return path.toArray(pathArray);
    }

    private void logInvalidPathException(InvalidPathException e, String[] path, MadCommand command, ConfigurationSection commands) {
        mad.getLogger().log(Level.SEVERE, "InvalidPathException: Unable to add a command."
            + "\nThis likely means a serious error has occured in the Mad Core."
            + "\nPossible causes:"
            + "\n\t1) A Mad Plugin is misconfigured.  Usualy in its commands.yml file."
            + "\n\t2) The MadCore.jar file is corrupt."
            + "\n\t3) Another plugin has to accessed or modified this plugin incorrectly."
            + "\nError Dump:\n{0}\n{1}\n\n{2}\n\n{3}\n\n{4}"
            , new Object[]{e.getMessage(), e.getStackTrace(), Arrays.toString(path), command.toString(), commands.toString()});
    }
}