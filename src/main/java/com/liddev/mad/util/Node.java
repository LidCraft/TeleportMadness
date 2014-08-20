package com.liddev.mad.util;

import java.util.Collection;

abstract public class Node<C extends Comparable, T> implements Cloneable {

  private Node<C, T> parent = null;
  private C key = null;
  private T value = null;

  public Node() {
  }

  public Node(C key, T value) {
    this.key = key;
    this.value = value;
  }

  public Node<C, T> getParent() {
    return parent;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public C getKey() {
    return key;
  }

  public void setKey(C key) {
    this.key = key;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  protected void setParent(Node<C, T> parent) {
    this.parent = parent;
  }

  /**
   *
   * @param child
   * @return the previous child associated with value if one exists null otherwise
   */
  public abstract Node<C, T> addChild(Node<C, T> child);

  public abstract void removeChild(final C value);

  public abstract void replace(Node<C, T> node);

  public abstract boolean hasChild(final C value);

  public abstract boolean hasChildren();

  /**
   * Gets the child associated with a particular value.
   *
   * @param value the value corresponding to the desired node.
   * @return the Node corresponding to value if it exists null otherwise.
   */
  public abstract Node<C, T> getChild(final C value);

  public abstract Collection<Node<C, T>> getChildren(final C fromValue, final C toValue);

  public abstract Collection<Node<C, T>> getChildren();

  public abstract Node<C, T> fillNode();
}
