package com.liddev.mad.core;

import com.liddev.mad.util.MapNode;
import com.liddev.mad.util.NTree;
import com.liddev.mad.util.Node;
import com.liddev.mad.util.Tree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubCommandMap {

  private final Map<Class, TreeSet<MapNode<String, MadCommand>>> classNodeMap;
  private final Tree<String, MadCommand> commandTree;

  public SubCommandMap() {
//TODO: base initial capacity off of expected number of commands.
    classNodeMap = new HashMap<Class, TreeSet<MapNode<String, MadCommand>>>(64);
    commandTree = new NTree<String, MadCommand>(new MapNode<String, MadCommand>());
  }

  public void register(MadCommand command) {
    register(new String[]{}, command);
  }

  @SuppressWarnings("unchecked")
  public void register(String[] path, MadCommand command) {
    for (String alias : command.getAliases()) {
      MapNode<String, MadCommand> commandNode;
      TreeSet<MapNode<String, MadCommand>> nodes = classNodeMap.get(command.getClass());
      if (nodes != null) {
        if (nodes.size() > 0) {
          try {
            commandNode = (MapNode<String, MadCommand>) nodes.first().clone();
          }
          catch (CloneNotSupportedException ex) {
            Logger.getLogger(SubCommandMap.class.getName()).log(Level.SEVERE, null, ex);
            continue;
          }
          commandNode.setKey(alias);
        } else {
          commandNode = new MapNode<String, MadCommand>(alias, command);
        }
        nodes.add(commandNode);
      } else {
        commandNode = new MapNode<String, MadCommand>(alias, command);
        nodes = new TreeSet<MapNode<String, MadCommand>>();
        nodes.add(commandNode);
        classNodeMap.put(command.getClass(), nodes);
      }
      boolean prefixUsed = false;
      while (!addCommandIfNotExists(path, commandNode)) {
        prefixUsed = true;
        alias = commandNode.getValue().getPrefix() + alias;
        commandNode.setKey(alias);
      }

      if (!prefixUsed) {
        alias = commandNode.getValue().getPrefix() + alias;
        try {
          commandNode = (MapNode<String, MadCommand>) nodes.first().clone();
        }
        catch (CloneNotSupportedException ex) {
          Logger.getLogger(SubCommandMap.class.getName()).log(Level.SEVERE, null, ex);
          continue;
        }
        commandNode.setKey(alias);
        nodes.add(commandNode);
        addCommandIfNotExists(path, commandNode);
      }
    }
  }

  public List<MadCommand> getRootCommands() {
    Collection<Node<String, MadCommand>> commandNodes = commandTree.getRoot().getChildren();
    List<MadCommand> commands = new ArrayList<MadCommand>(commandNodes.size());
    for (Node<String, MadCommand> node : commandNodes) {
      commands.add(node.getValue());
    }
    return commands;
  }

  public int getDepth(Node<String, MadCommand> currentNode) {
    int depth = 0;
    Node<String, MadCommand> parent = currentNode.getParent();

    while (parent != commandTree.getRoot()) {
      ++depth;
      currentNode = parent;
      parent = currentNode.getParent();
    }

    return depth;
  }

  public boolean isRootLevel(Node<String, MadCommand> currentNode) {
    return currentNode.getParent() == commandTree.getRoot();
  }

  public Node<String, MadCommand> match(String[] commandArray) {
    return commandTree.findPartial(commandArray);  //TODO: find best command for arguments here? (regex arg. matching)
    //TODO: Give user list of potential matches if multiple exact matchs or multiple close partial matches are found
  }

  /**
   *
   * @param path,    the path through the tree to add the command at, the ordered list of all parent commands
   * @param command, the command instance to add to the tree.
   */
//TODO: Deal with commands which have the same name.  If they are within the same plugin report an error.
//      Overwrite existing if it is an alias and the current is the main name? force prefix on new command?
  private void addCommand(String[] path, MapNode<String, MadCommand> command) {
    Node<String, MadCommand> previous = commandTree.add(path, command);
    if (previous != null) {
      Logger.getLogger(SubCommandMap.class.getName())
          .log(Level.WARNING, "Insert for {0}, {1}, at {2} replaced {3}, {4}.",
               new Object[]{command.getKey(), command.getValue().getClass().toString(), Arrays.toString(path),
                            previous.getKey(), previous.getValue().getClass().toString()});
    }
  }

  private boolean addCommandIfNotExists(MapNode<String, MadCommand> command) {
    Node<String, MadCommand> exists = commandTree.getRoot().getChild(command.getKey());
    if (exists == null) {
      commandTree.getRoot().addChild(command);
    } else if (exists.getValue() == null) {
      exists.replace(command);
    } else {
      return false;
    }
    return true;
  }

  private boolean addCommandIfNotExists(String[] path, MapNode<String, MadCommand> command) {
    return commandTree.addIfNotExists(path, command);
  }

  private void removeCommand(String[] path, String alias) {
    commandTree.remove(path, alias);
  }

  private void removeCommandAliases(String[] path, String name) {
    Node<String, MadCommand> parent = commandTree.find(path);
    if (parent == null) {
      return;
    }

    Node<String, MadCommand> child = parent.getChild(name);
    if (child == null) {
      return;
    }
    MadCommand command = child.getValue();

    if (command == null) {
      return;
    }
    for (String alias : command.getAliases()) {
      parent.removeChild(alias);
    }
  }

}
