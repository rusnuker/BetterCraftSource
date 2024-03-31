// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.nbt;

import java.util.ArrayList;
import java.util.List;

public class Node<T>
{
    private List<Node<T>> children;
    private Node<T> parent;
    private T obj;
    private boolean drawChildren;
    
    public Node() {
        this((Object)null);
    }
    
    public Node(final T obj) {
        this.children = new ArrayList<Node<T>>();
        this.obj = obj;
    }
    
    public boolean shouldDrawChildren() {
        return this.drawChildren;
    }
    
    public void setDrawChildren(final boolean draw) {
        this.drawChildren = draw;
    }
    
    public Node(final Node<T> parent) {
        this((Node<Object>)parent, null);
    }
    
    public Node(final Node<T> parent, final T obj) {
        this.parent = parent;
        this.children = new ArrayList<Node<T>>();
        this.obj = obj;
    }
    
    public void addChild(final Node<T> n) {
        this.children.add(n);
    }
    
    public boolean removeChild(final Node<T> n) {
        return this.children.remove(n);
    }
    
    public List<Node<T>> getChildren() {
        return this.children;
    }
    
    public Node<T> getParent() {
        return this.parent;
    }
    
    public T getObject() {
        return this.obj;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(this.obj).toString();
    }
    
    public boolean hasChildren() {
        return this.children.size() > 0;
    }
    
    public boolean hasParent() {
        return this.parent != null;
    }
}
