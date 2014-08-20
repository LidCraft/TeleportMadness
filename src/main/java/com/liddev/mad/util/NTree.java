package com.liddev.mad.util;

public class NTree<C extends Comparable, T> implements Tree<C, T> {

  private final Node<C, T> root;

  public NTree(Node<C, T> root) {
    this.root = root;
  }

  @Override
  public Node<C, T> getRoot() {
    return root;
  }

  @Override
  public Node<C, T> find(C[] path) {
    Node<C, T> found = root;
    for (C comp : path) {
      if (found.hasChild(comp)) {
        found = found.getChild(comp);
      } else {
        return null;
      }
    }
    return found;
  }

  @Override
  public Node<C, T> findPartial(C[] path) {
    Node<C, T> found = root;
    for (C comp : path) {
      if (found.hasChild(comp)) {
        found = found.getChild(comp);
      } else {
        break;
      }
    }
    return found;
  }

  @Override
  public Node<C, T> add(C[] path, Node<C, T> node) {
    Node<C, T> current = createPathIfNotExists(path);
    return current.addChild(node);
  }

  @Override
  public boolean addIfNotExists(C[] path, Node<C, T> node) {
    Node<C, T> current = createPathIfNotExists(path);
    Node<C, T> exists = current.getChild(node.getKey());
    if (exists == null) {
      current.addChild(node);
      return true;
    } else if (exists.getValue() == null) {
      exists.replace(node);
    }
    return false;
  }

  @Override
  public void remove(C[] path, C name) {
    Node<C, T> parent = find(path);
    if (parent != null) {
      parent.removeChild(name);
    }
  }

  private Node<C, T> createPathIfNotExists(C[] path) {
    Node<C, T> current = root;
    for (int i = 0; i < path.length; ++i) {
      C comp = path[i];
      Node<C, T> temp = null;
      if (current.hasChild(comp)) {
        temp = current.getChild(comp);
      }
      if (temp != null) {
        current = temp;
      } else {
        Node<C, T> fillNode = root.fillNode();
        fillNode.setParent(current);
        current.addChild(fillNode);
        current = current.getChild(comp);
      }
    }
    return current;
  }
}
