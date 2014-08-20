package com.liddev.mad.util;

import java.util.Collection;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MapNode<C extends Comparable, T> extends Node<C, T> {

  private TreeMap<C, Node<C, T>> children = null;

  public MapNode() {
    super();
  }

  public MapNode(C key, T value) {
    super(key, value);
  }

  @Override
  public Node<C, T> addChild(Node<C, T> child) {
    if (children == null) {
      this.children = new TreeMap<C, Node<C, T>>();
    }
    child.setParent(this);
    return this.children.put(child.getKey(), child);
  }

  @Override
  public void removeChild(C value) {
    if (children != null) {
      children.remove(value);

      Node<C, T> child = children.get(value);
      if (child != null) {
        child.setParent(null);
      }
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void replace(Node<C, T> node) {
    for (Node<C, T> child : this.children.values()) {
      node.addChild(child);
    }
    node.setParent(this.getParent());
  }

  @Override
  public boolean hasChild(C value) {
    return children.containsKey(value);
  }

  @Override
  public boolean hasChildren() {
    return !(children == null || children.isEmpty());
  }

  @Override
  public Node<C, T> getChild(C value) {
    return children.get(value);
  }

  @Override
  public Collection<Node<C, T>> getChildren(C fromValue, C toValue) {
    return children.subMap(fromValue, toValue).values();
  }

  @Override
  public Collection<Node<C, T>> getChildren() {
    return children.values();
  }

  @Override
  public Node<C, T> fillNode() {
    return new MapNode<C, T>();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
