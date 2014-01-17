package com.liddev.teleportmadness;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Renlar <liddev.com>
 */
public class CommandManager {

    private TeleportMadness mad;
    private Node commandTree;
    private YamlConfiguration commandFile;

    public CommandManager(TeleportMadness mad) {
        this.mad = mad;
        this.commandFile = mad.getFileManager().getCommands();
        commandTree = new Node<CommandData>();
        generateTree(mad.getFileManager().getCommands(), commandTree);
    }

    public boolean run(CommandSender Sender, Command command, String[] args) {
        YamlConfiguration commands = mad.getFileManager().getCommands();
        for (String s : args) {
            for (String p : commands.getKeys(false)) {//load the first level of keys
                if (s.equalsIgnoreCase(p)) {

                }
            }
        }
        return false;
    }

    public MadCommand getCommand(String s) {
        throw new UnsupportedOperationException();
    }

    public boolean hasPermission(CommandSender sender) {
        throw new UnsupportedOperationException();
    }

    public boolean validateArguemnts(String[] args) {
        throw new UnsupportedOperationException();
    }

    private Node generateTree(YamlConfiguration commands, Node<CommandData> commandTree) {
        throw new UnsupportedOperationException();
    }

    private class CommandData {

        private String[] aliases;
        private ParseTree pattern;
        private Class madCommand;
        private String permission;
        private boolean console;
        private String description;
        private String help;

        public CommandData(ConfigurationSection yam) {
            aliases = (String[]) yam.getList("alias").toArray();
            pattern = new ParseTree(yam.getString("pattern"), yam);
            try {
                madCommand = Class.forName(yam.getString("class"));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, "Error: Command class " + yam.getString("class") + " was not found, unable to initiate command.  May cause undesired results.", ex);
            }
            permission = yam.getString("perm");
            console = yam.getBoolean("console");
            description = yam.getString("desc");
            help = yam.getString("help");
        }

    }

    private static class ParseTree {

        public static final String self = "this";
        public static final String parent = "super";
        public static final String string = "str";
        public static final String integer = "int";
        public static final String floatingPoint = "float";
        public static final String bool = "bool";
        public static final String tag = "tag";
        public static final String or = "|";
        public static final String and = "&";
        public static final String xor = "^";
        public static final String not = "!";
        public static final String exclude = "-";
        public static final String include = "+";
        public static final String comma = ",";
        public static final String required = "<>";
        public static final String optional = "[]";
        public static final String[] groups = {required, optional};
        public static final String[] operators = {or, and, xor, not, exclude, include};
        public static final String[] types = {self, parent, string, integer, floatingPoint, bool, tag};
        private final Node<String> root;
        private final ConfigurationSection yaml;
        private final String pattern;

        public ParseTree(String string, ConfigurationSection yaml) {
            this.yaml = yaml;
            this.pattern = string.replaceAll("\\s", "");
            this.root = new Node();
            expandNodeGroups();
        }

        public String getPattern() {
            return pattern;
        }

        public ConfigurationSection getYaml() {
            return yaml;
        }

        /**
         * Takes a pattern string and expands it into a parse tree around <>, and [].
         *
         * @param pattern the pattern string to be turned into a parse tree
         * @return
         */
        private Node<String> expandNodeGroups() {
            Node next, current = root;
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < pattern.length(); i++) {
                if (pattern.charAt(i) == required.charAt(0)) {
                    addComplexNode(builder, current);
                    next = new Node(required);
                    current.addChild(next);
                    current = next;
                } else if (pattern.charAt(i) == optional.charAt(0)) {
                    addComplexNode(builder, current);
                    next = new Node(optional);
                    current.addChild(next);
                    current = next;
                } else if (pattern.charAt(i) == required.charAt(1)) {
                    addComplexNode(builder, current);
                    current = current.getParent();
                } else if (pattern.charAt(i) == optional.charAt(1)) {
                    addComplexNode(builder, current);
                    current = current.getParent();
                } else {
                    builder.append(pattern.charAt(i));
                }
            }
            return root;
        }

        private void addComplexNode(StringBuilder builder, Node<String> current) {
            if (builder.length() > 0) {
                Node next = new Node(builder.toString());
                builder.delete(0, builder.length());
                current.addChild(next);
                expandComplexNode(next);
            }
        }

        /**
         * Expands a complex nodes into simple nodes. Complex nodes: contains one or more types, references, and
         * operators. Simple node: contains only a single type, operator, or reference.
         *
         * @param node a node in the parse tree which contains 2 or more of the following operators, types, and
         * references
         */
        private void expandComplexNode(Node<String> node) {//TODO: review, check that a valid node expansion is produced here.
            String data = node.getData();
            Node next, current = node.getParent();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < data.length(); i++) {
                boolean isOperator = false;

                for (String operator : operators) {
                    if (data.charAt(i) == operator.charAt(0)) {
                        isOperator = true;
                        next = new Node(operator);

                        if ((current.getData().equals(exclude) && next.getData().equals(exclude)) || (current.getData().equals(include) && next.getData().equals(include))) {
                            //do nothing
                        } else if ((current.getData().equals(include) && next.getData().equals(exclude)) || (current.getData().equals(exclude) && next.getData().equals(include))) {
                            throw new PatternSyntaxException("Can not use inclusion, +, and exclusion, -, together in the same option.", pattern, pattern.indexOf(node.getData()) + i);
                        } else {
                            current.addChild(next);
                            current = next;
                        }

                        for (String type : types) {
                            if (builder.toString().equals(type)) {
                                current.addChild(new Node(type));
                                builder.delete(0, builder.length());
                                break;
                            }
                        }

                        if (builder.length() > 0) {
                            next = new Node(builder.toString());
                            builder.delete(0, builder.length());
                            current.addChild(next);
                            expandReferenceNode(next);
                        }
                        break;
                    }
                }
                if (isOperator == false) {
                    builder.append(data.charAt(i));
                }
            }

            node.remove();
        }

        /**
         * Expands reference type nodes such as mad.sub.home.sub.*.alias, this will replace the with the aliases of all
         * sub commands of home all of these must reference nodes in the yaml file passed into the constructor;
         *
         * @param node a reference node in the parse tree to be replaced with the values it references.
         * @param yaml the yaml file which contains the data for the references.
         */
        private void expandReferenceNode(Node<String> node) {//TODO: write expand reference node method.
            List<String> references = new ArrayList<String>();
            YamlConfiguration rootConfig = (YamlConfiguration) yaml.getRoot();
            //TODO: check node for validity before charging ahead into lookup.
            for (int i = 0; i < node.getData().length(); i++) {
                if (node.getData().charAt(i) == '*') {
                    if (i > 0 && node.getData().charAt(i - 1) == '.') {

                        if (!references.isEmpty()) {
                            for (String e : references) {
                                references.remove(e);
                                for (String s : rootConfig.getKeys(false)) {
                                    references.add(e.replaceFirst(Pattern.quote("*"), s));
                                }
                            }

                        } else {
                            for (String s : rootConfig.getConfigurationSection(node.getData().substring(0, i)).getKeys(false)) {
                                references.add(node.getData().replaceFirst(Pattern.quote("*"), s));
                            }
                        }
                    }
                }
            }
            resolveReferences(node, references);

            node.remove();
        }

        private void resolveReferences(Node<String> node, List<String> references) {
            YamlConfiguration rootConfig = (YamlConfiguration) yaml.getRoot();

            for (String reference : references) {
                Object o = rootConfig.get(reference);
                interpretResults(o, node);
            }
        }

        private void interpretResults(Object o, Node<String> node) {
            Class c = o.getClass();
            if (c.equals(String.class)) {
                String s = (String) o;
                node.addChild(new Node(s));
            } else if (c.equals(Integer.class)) {
                Integer i = (Integer) o;
                node.addChild(new Node(i.toString()));
            } else if (c.equals(Double.class)) {
                Double d = (Double) o;
                node.addChild(new Node(d.toString()));
            } else if (c.equals(Float.class)) {
                Float f = (Float) o;
                node.addChild(new Node(f.toString()));
            } else if (c.equals(Boolean.class)) {
                Boolean b = (Boolean) o;
                if (b) {
                    node.addChild(new Node("true"));
                    node.addChild(new Node("t"));
                } else {
                    node.addChild(new Node("false"));
                    node.addChild(new Node("f"));
                }
            } else if (c.equals(List.class)) {
                List l = (List) o;
                for (Object item : l) {
                    interpretResults(item, node);
                }
            }
        }
    }

    private static class Node<T> {

        private Node<T> parent;
        private List<Node<T>> children;
        private final T data;

        public Node() {
            this.parent = null;
            this.data = null;
        }

        public Node(T data) {
            this.data = data;
        }

        public void setParent(Node<T> parent) {
            parent.addChild(this);
        }

        public void addChild(Node<T> child) {
            if (children == null) {
                this.children = new ArrayList<Node<T>>();
            }
            this.children.add(child);
            child.parent = this;
        }

        public void removeChild(Node<T> child) {
            if (children != null) {
                children.remove(child);
            }
        }

        public void remove() {
            this.getParent().removeChild(this);
            this.parent = null;
        }

        public Node<T> getParent() {
            return parent;
        }

        public List<Node<T>> getChildren() {
            return children;
        }

        public T getData() {
            return data;
        }
    }
}
