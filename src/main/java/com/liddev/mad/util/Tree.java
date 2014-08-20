package com.liddev.mad.util;

/**
 *
 * @author Renlar <liddev.com>
 * @param <C> The key type to build the tree with. Used for navigating the tree.
 * @param <T> The value type the tree will contain.
 */
public interface Tree<C extends Comparable, T> {

  public Node<C, T> getRoot();

  public Node<C, T> find(C[] path);

  public Node<C, T> findPartial(C[] path);

  public Node<C, T> add(C[] path, Node<C, T> node);

  public boolean addIfNotExists(C[] path, Node<C, T> node);

  public void remove(C[] path, C name);
}
