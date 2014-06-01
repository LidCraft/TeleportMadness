package com.liddev.mad.core;

import com.liddev.mad.exceptions.InvalidPathException;
import com.liddev.mad.teleport.TeleportMadness;
import com.liddev.mad.util.DynamicClassHelpers;
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
    }
    
    public void setup(ConfigurationSection commands){
        loadCommands(commands);
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
                madCommand = DynamicClassHelpers.instantiate(className, MadCommand.class, 
                        new Class<?>[]{ConfigurationSection.class},  new Object[]{data});

                madCommand.setExecutor(this);
                return madCommand;
            } catch (IllegalStateException ex) {
                mad.getLogger().log(Level.SEVERE, "Error setting up command class " + className + ".  May cause undesired results."
                        + "\nPossible Causes: " 
                        + "\n\t1) " + className + " does not exist. "
                        + "\n\t2) " + className + " is not a sub-class of MadCommand."
                        + "\n\t3) There was a security error while attempting to access the class."
                        + "\n\t4) One ore more dependencies for " + className + " are missing."
                        + "\n\t5) One ore more classes required for " + className + " to operate have not been loaded yet."
                        + "\n\t6) The MadCommand constructor or the CommandManager.commandFactory() have changed independently of eachother."
                        + "\n\t7) Unknown Java VM error.  The version of java you are running might be incompatable with this program.  Are you running java 6?"
                        + "\nError Dump:\n"
                        , ex);
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
     * Takes configuration section as argument and loads command in current level
     * configured in the commands.yml file by adding them to a CommandTree.
     *
     * @param commands the yaml configFile containing the command structure.
     */
    
    public void loadCommands(ConfigurationSection commands) {
        Set<String> keys = commands.getKeys(false);

        for (String key : keys) {
            ConfigurationSection commandSection = commands.getConfigurationSection(key);
            
            MadCommand command = commandFactory(commandSection);
            if(command == null){
                continue;
            }
            
            String[] path = getCommandPath(commands);
            mad.getLogger().log(Level.INFO, "Registering command, /{0}{1} with class {2}", 
                                new Object[]{toCommandString(path), command.getAliases().get(1), command.toString()});
            
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
    
    public static String toCommandString(String[] path){
        String command = "";
        for(String s : path){
            command += s + " ";
        }
        return command;
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
        mad.getLogger().log(Level.SEVERE, "InvalidPathException: Unable to add command, {3}."
            + "\nThis likely means a serious error has occured in the Mad Core."
            + "\nPossible causes:"
            + "\n\t1) A Mad Plugin is misconfigured.  Usualy in its commands.yml file."
            + "\n\t2) The MadCore.jar file is corrupt."
            + "\n\t3) Another plugin has accessed or modified this plugin incorrectly."
            + "\nError Dump:\n{0}\n{1}\n\n{2}\n\n{4}"
            , new Object[]{e.getMessage(), e.getStackTrace(), Arrays.toString(path), command.toString(), commands.toString()});
    }
}