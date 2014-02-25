package com.liddev.teleportmadness;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.command.CommandSender;
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

    public void addRoot(MadCommand command) {
        Node<MadCommand> node = new Node<MadCommand>(command);
        root.addChild(node);
    }

    public List<MadCommand> getRootCommands() {
        List<Node<MadCommand>> commandNodes = root.getChildren();
        List<MadCommand> commands = new ArrayList<MadCommand>();
        for (Node<MadCommand> node : commandNodes) {
            commands.add(node.getData());
        }
        return commands;
    }

    public void addCommand(String[] path, MadCommand command) {
        Node<MadCommand> currentLevel = root;
        Node<MadCommand> previousLevel = currentLevel;
        for (String step : path) {
            for (Node<MadCommand> n : currentLevel.getChildren()) {
                if (n.getData().getName().equals(step)) {
                    currentLevel = n;
                    break;
                }
            }
        }
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
            Node<MadCommand> command = new Node<MadCommand>(TeleportMadness.getCommandManager().customCommandFactory(commandSection));

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
    

    protected boolean findAndRunCommand(CommandSender sender, String[] statements) {
        //TODO: reimplement command search and execute.
        MadCommand currentChoice;
        int aliasPosition = 0;
        int currentStatement = 0;
        Node<MadCommand> preNode = current;
        Node<MadCommand> node;
        for (int i = 0; i < current.getChildren().size(); i++) {
            node = current.getChildren().get(i);
            MadCommand cData = node.getData();
            List<String> aliases = cData.getAliases();
            for (String alias : aliases) {
                if (alias.equalsIgnoreCase(statements[currentStatement])) {
                    current = node;
                    i = 0;
                    currentChoice = cData;
                    aliasPosition = currentStatement;
                    currentStatement++;
                    break;
                }
            }
            if (current == preNode) {
                break;
            } else {
                preNode = current;
            }
        }
        String[] args = new String[statements.length - aliasPosition - 1];
        for (int i = statements.length - 1, j = args.length; j > 0; i--, j--) {
            args[j - 1] = statements[i];
        }
        return currentChoice.run(sender, statements[aliasPosition], args);
    }
}
