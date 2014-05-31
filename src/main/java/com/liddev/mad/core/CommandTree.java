package com.liddev.mad.core;

import com.liddev.mad.exceptions.InvalidPathException;
import com.liddev.mad.util.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Renlar <liddev.com>
 */
//TODO: make more compatable.  Extend org.bukkit.command.commandMap?
public class CommandTree {

    private final Node<MadCommand> root;

    public CommandTree() {
        root = new Node<MadCommand>();
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
            //TODO: deal with nodes which have the same name as existing one making them inaccessable.
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
