package com.liddev.teleportmadness;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Renlar <liddev.com>
 * @param <T>
 */
public class Node<T> {

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
        this.parent = parent;
    }
    
    public void connectParent(Node<T> parent){
        parent.addChild(this);
    }

    public void addChild(Node<T> child) {
        this.children.add(child);
    }

    public void connectChild(Node<T> child) {
        if (children == null) {
            this.children = new ArrayList<Node<T>>();
        }
        addChild(child);
        child.parent = this;
    }

    public void removeChild(Node<T> child) {
        if (children != null) {
            children.remove(child);
        }
    }

    public void remove() {
        this.getParent().removeChild(this);
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

    public boolean hasChildren() {
        return !(children == null || children.isEmpty());
    }
}
