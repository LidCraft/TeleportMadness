package com.liddev.mad.core;

import com.liddev.mad.util.Node;
import com.liddev.mad.core.exceptions.InvalidPathException;
import com.liddev.mad.teleport.TeleportMadness;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Renlar <liddev.com>
 */
public class CommandTree {

    private final Node<MadCommand> root;

    public CommandTree(ConfigurationSection commands) {
        root = new Node<MadCommand>();
        build(commands, root);
    }

    /**
     * Takes an initialized command tree as arguments and loads all mad commands
     * configured in the commands.yml file by attaching them to the tree.
     *
     * @param commands the yaml configFile containing the command structure.
     * @param commandTree an Initialized Node to attach all subcommands to.
     */
    private void build(ConfigurationSection commands, Node<MadCommand> current) {
        Set<String> keys = commands.getKeys(false);

        for (String key : keys) {
            ConfigurationSection commandSection = commands.getConfigurationSection(key);
            Node<MadCommand> command = new Node<MadCommand>(TeleportMadness.getCommandManager().commandFactory(commandSection));

            current.connectChild(command);

            if (commandSection.getBoolean("root")) {
                root.connectChild(command);
            }

            if (commandSection.contains("sub")) {
                ConfigurationSection subCommand = commandSection.getConfigurationSection("sub");
                build(subCommand, command);
            }
        }
    }

    public List<MadCommand> getRootCommands() {
        List<Node<MadCommand>> commandNodes = root.getChildren();
        List<MadCommand> commands = new ArrayList<MadCommand>();
        for (Node<MadCommand> node : commandNodes) {
            commands.add(node.data());
        }
        return commands;
    }

    /**
     * 
     * @param path, the path through the tree to add the command at, the ordered list of all parent commands
     * @param command, the command instance to add to the tree.
     * @throws InvalidPathException when no matching path is found.
     */
    //TODO: add a closest match option to prevent unaccessable commands or build parent nodes as necessary.
    //TODO: deal with commands which have the same name. Overwrite existing? Force prefix?  Currently if a command with the same name as an existing command is added it will be inaccessable.
    public void addCommand(String[] path, MadCommand command) throws InvalidPathException {
        Node node = getNode(path, false);
        if(node == null){
            throw new InvalidPathException("Path: " + Arrays.toString(path) + " was not found unable to initialize command.");
        }else{
            node.connectChild(new Node(command));
            //TODO: deal with nodes which have the same name as one will be inaccessable.
        }
    }
    
    /**
     * 
     * @param path the path through the tree to search for.
     * @param partial return deepest match if full match not found
     * @return return found Node.  Will either be the deepest match found to path if partial=true or null if not found and partial=false
     */
    protected Node getNode(String[] path, boolean partial){
        Node<MadCommand> currentNode = root;
        boolean found = true;
        for (String step : path) {
            for (Node<MadCommand> n : currentNode.getChildren()) {
                if (n.data().getName().equals(step)) {//Ignore case????
                    currentNode = n;
                    found = true;
                    break;
                } else {
                    found = false;
                }
            }
        }
        
        if(found || partial){
            return currentNode;
        } else {
            return null;
        }
    }
    
    protected int getDepth(Node node){
        Node current = node;
        int depth = 0;
        while(current != root){
            current = current.getParent();
            depth++;
        }
        return depth;
    }
    
    protected boolean isRootLevel(Node node){
        return  node.getParent() == root;
    }
}
