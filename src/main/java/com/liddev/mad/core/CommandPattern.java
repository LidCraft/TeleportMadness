package com.liddev.mad.core;

import com.liddev.mad.util.ListNode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Renlar <liddev.com>
 */
//TODO: replace with regex?  Use finite state atomata?
public class CommandPattern {

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
  public static final String required = "<>";
  public static final String optional = "[]";
  public static final String[] groups = {required, optional};
  public static final String[] operators = {or, and, xor, not, exclude, include};
  public static final String[] types = {string, integer, floatingPoint, bool, tag};
  public static final String T = "t";
  public static final String TRUE = "true";
  public static final String F = "f";
  public static final String FALSE = "false";
  private final ListNode<String> root;
  private final ConfigurationSection yaml;
  private final String pattern;

  public CommandPattern(String string, ConfigurationSection yaml) {
    this.yaml = yaml;
    this.pattern = string.replaceAll("\\s", "");
    this.root = new ListNode<String>();
    expandNodeGroups();
  }

  public String getPattern() {
    return pattern;
  }

  protected ListNode<String> getTreeRoot() {
    return root;
  }

  public ConfigurationSection getYaml() {
    return yaml;
  }
//TODO: update to a more inteligent matcher.  (Use CFG to describe and analize?  Report possible corrections?)
//TODO: Add support for all operators.

  public boolean matches(String[] args) {
    List<ListNode<String>> patternTree = root.getChildren();
    int req = 0, opt = 0;
    for (ListNode<String> n : patternTree) {
      if (n.data().equals(required)) {
        req++;
      } else if (n.data().equals(optional)) {
        opt++;
      }
    }
    if (args.length < req || args.length > (req + opt)) {
      return false;
    }
    for (int i = 0, j = 0; i < args.length && j < patternTree.size();) {
      String arg = args[i];

      if (typeMatches(arg, patternTree.get(i).getChildren().get(0).data())) {
        i++;
        j++;
      } else if (patternTree.get(i).data().equals(optional)) {
        j++;
      } else {
        return false;
      }
    }
    return true;
  }

  public boolean typeMatches(String arg, String type) {
    if (type.equals(string)) {
      return true;
    } else if (type.equals(integer)) {
      if (arg.matches("^[-+]?\\d+$")) {
        return true;
      }
    } else if (type.equals(floatingPoint)) {
      if (arg.matches("^[-+]?\\d+(\\.\\d+)?$")) {
        return true;
      }
    } else if (type.equals(bool)) {
      if (arg.matches("^(1|0|t|f|\\btrue\\b|\\bfalse\\b)$")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Takes a pattern string and expands it into a parse tree around <>, and [].
   *
   * @param pattern the pattern string to be turned into a parse tree
   * @return
   */
  private ListNode<String> expandNodeGroups() {
    ListNode<String> next;
    Node<String> current = root;
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < pattern.length(); i++) {
      if (pattern.charAt(i) == required.charAt(0)) {
        addComplexNode(builder, current);
        next = new ListNode<String>(required);
        current.connectChild(next);
        current = next;
      } else if (pattern.charAt(i) == optional.charAt(0)) {
        addComplexNode(builder, current);
        next = new ListNode<String>(optional);
        current.connectChild(next);
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

  private void addComplexNode(StringBuilder builder, ListNode<String> current) {
    if (builder.length() > 0) {
      ListNode<String> next = new ListNode<String>(builder.toString());
      builder.delete(0, builder.length());
      current.connectChild(next);
      expandComplexNode(next);
      if (!next.getParent().hasChildren()) {
        next.getParent().remove();
      }
    }
  }

  /**
   * Expands a complex nodes into simple nodes. Complex node: contains one or more types, references, and operators.
   * Simple node: contains only a single type, operator, or reference.
   *
   * @param node a node in the parse tree which contains 2 or more of the following operators, types, and references
   */
  private void expandComplexNode(ListNode<String> node) {//TODO: review, check that a valid node expansion is produced here.
    String data = node.data();
    ListNode<String> next;
    Node<String> current = node.getParent();
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < data.length(); i++) {
      boolean isOperator = false;

      for (String operator : operators) {
        if (data.charAt(i) == operator.charAt(0)) {
          isOperator = true;
          next = new ListNode<String>(operator);

          if ((current.data().equals(exclude) && next.data().equals(exclude)) || (current.data().equals(include) && next.data().equals(include))) {
            //do nothing
          } else if ((current.data().equals(include) && next.data().equals(exclude)) || (current.data().equals(exclude) && next.data().equals(include))) {
            throw new PatternSyntaxException("Can not use inclusion, +, and exclusion, -, together in the same option.", pattern, pattern.indexOf(node.data()) + i);
          } else {
            current.connectChild(next);
            current = next;
          }

          for (String type : types) {
            if (builder.toString().equals(type)) {
              ListNode<String> typeNode = new ListNode<String>(type);
              builder.delete(0, builder.length());
              current.connectChild(typeNode);
              break;
            }
          }

          if (builder.length() > 0) {
            next = new ListNode<String>(builder.toString());
            builder.delete(0, builder.length());
            current.connectChild(next);
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
   * Expands reference type nodes such as mad.sub.home.sub.*.alias, this will replace the with the aliases of all sub
   * commands of home. All of these must reference nodes in the yaml file passed into the constructor;
   *
   * @param node a reference node in the parse tree to be replaced with the values it references.
   * @param yaml the yaml file which contains the data for the references.
   */
  private void expandReferenceNode(ListNode<String> node) {
    List<String> references = new ArrayList<String>();
    YamlConfiguration rootConfig = (YamlConfiguration) yaml.getRoot();
    //TODO: check node for validity before charging into lookup.
    for (int i = 0; i < node.data().length(); i++) {
      if (node.data().charAt(i) == '*') {
        if (i > 0 && node.data().charAt(i - 1) == '.') {

          if (!references.isEmpty()) {
            for (String e : references) {
              references.remove(e);
              for (String s : rootConfig.getKeys(false)) {
                references.add(e.replaceFirst(Pattern.quote("*"), s));
              }
            }

          } else {
            for (String s : rootConfig.getConfigurationSection(node.data().substring(0, i)).getKeys(false)) {
              references.add(node.data().replaceFirst(Pattern.quote("*"), s));
            }
          }
        }
      }
    }
    resolveReferences(node, references);

    node.remove();
  }

  private void resolveReferences(ListNode<String> node, List<String> references) {
    YamlConfiguration rootConfig = (YamlConfiguration) yaml.getRoot();

    for (String reference : references) {
      Object o = rootConfig.get(reference);
      interpretResults(o, node);
    }
  }

  private void interpretResults(Object o, ListNode<String> node) {
    Class c = o.getClass();
    if (c.equals(String.class)) {
      String s = (String) o;
      node.connectChild(new ListNode<String>(s));
    } else if (c.equals(Integer.class)) {
      Integer i = (Integer) o;
      node.connectChild(new ListNode<String>(i.toString()));
    } else if (c.equals(Double.class)) {
      Double d = (Double) o;
      node.connectChild(new ListNode<String>(d.toString()));
    } else if (c.equals(Float.class)) {
      Float f = (Float) o;
      node.connectChild(new ListNode<String>(f.toString()));
    } else if (c.equals(Boolean.class)) {
      Boolean b = (Boolean) o;
      if (b) {
        node.connectChild(new ListNode<String>(TRUE));
        node.connectChild(new ListNode<String>(T));
      } else {
        node.connectChild(new ListNode<String>(FALSE));
        node.connectChild(new ListNode<String>(F));
      }
    } else if (c.equals(List.class)) {
      List l = (List) o;
      for (Object item : l) {
        interpretResults(item, node);
      }
    }
  }
}
