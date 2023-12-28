// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.nbt;

import java.util.ArrayList;
import java.util.List;
import com.mcf.davidee.nbtedit.NBTStringHelper;
import net.minecraft.nbt.NBTTagList;
import java.util.Map;
import com.mcf.davidee.nbtedit.NBTHelper;
import java.util.Comparator;
import java.util.Collections;
import com.mcf.davidee.nbtedit.NBTEdit;
import net.minecraft.nbt.NBTBase;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;

public class NBTTree
{
    private NBTTagCompound baseTag;
    private Node<NamedNBT> root;
    
    public NBTTree(final NBTTagCompound tag) {
        this.baseTag = tag;
        this.construct();
    }
    
    public Node<NamedNBT> getRoot() {
        return this.root;
    }
    
    public boolean canDelete(final Node<NamedNBT> node) {
        return node != this.root;
    }
    
    public boolean delete(final Node<NamedNBT> node) {
        return node != null && node != this.root && this.deleteNode(node, this.root);
    }
    
    private boolean deleteNode(final Node<NamedNBT> toDelete, final Node<NamedNBT> cur) {
        final Iterator<Node<NamedNBT>> it = cur.getChildren().iterator();
        while (it.hasNext()) {
            final Node<NamedNBT> child = it.next();
            if (child == toDelete) {
                it.remove();
                return true;
            }
            final boolean flag = this.deleteNode(toDelete, child);
            if (flag) {
                return true;
            }
        }
        return false;
    }
    
    private void construct() {
        (this.root = new Node<NamedNBT>(new NamedNBT("ROOT", this.baseTag.copy()))).setDrawChildren(true);
        this.addChildrenToTree(this.root);
        this.sort(this.root);
    }
    
    public void sort(final Node<NamedNBT> node) {
        Collections.sort(node.getChildren(), NBTEdit.SORTER);
        for (final Node<NamedNBT> c : node.getChildren()) {
            this.sort(c);
        }
    }
    
    public void addChildrenToTree(final Node<NamedNBT> parent) {
        final NBTBase tag = parent.getObject().getNBT();
        if (tag instanceof NBTTagCompound) {
            final Map<String, NBTBase> map = NBTHelper.getMap((NBTTagCompound)tag);
            for (final Map.Entry<String, NBTBase> entry : map.entrySet()) {
                final NBTBase base = entry.getValue();
                final Node<NamedNBT> child = new Node<NamedNBT>(parent, new NamedNBT(entry.getKey(), base));
                parent.addChild(child);
                this.addChildrenToTree(child);
            }
        }
        else if (tag instanceof NBTTagList) {
            final NBTTagList list = (NBTTagList)tag;
            for (int i = 0; i < list.tagCount(); ++i) {
                final NBTBase base2 = NBTHelper.getTagAt(list, i);
                final Node<NamedNBT> child2 = new Node<NamedNBT>(parent, new NamedNBT(base2));
                parent.addChild(child2);
                this.addChildrenToTree(child2);
            }
        }
    }
    
    public NBTTagCompound toNBTTagCompound() {
        final NBTTagCompound tag = new NBTTagCompound();
        this.addChildrenToTag(this.root, tag);
        return tag;
    }
    
    public void addChildrenToTag(final Node<NamedNBT> parent, final NBTTagCompound tag) {
        for (final Node<NamedNBT> child : parent.getChildren()) {
            final NBTBase base = child.getObject().getNBT();
            final String name = child.getObject().getName();
            if (base instanceof NBTTagCompound) {
                final NBTTagCompound newTag = new NBTTagCompound();
                this.addChildrenToTag(child, newTag);
                tag.setTag(name, newTag);
            }
            else if (base instanceof NBTTagList) {
                final NBTTagList list = new NBTTagList();
                this.addChildrenToList(child, list);
                tag.setTag(name, list);
            }
            else {
                tag.setTag(name, base.copy());
            }
        }
    }
    
    public void addChildrenToList(final Node<NamedNBT> parent, final NBTTagList list) {
        for (final Node<NamedNBT> child : parent.getChildren()) {
            final NBTBase base = child.getObject().getNBT();
            if (base instanceof NBTTagCompound) {
                final NBTTagCompound newTag = new NBTTagCompound();
                this.addChildrenToTag(child, newTag);
                list.appendTag(newTag);
            }
            else if (base instanceof NBTTagList) {
                final NBTTagList newList = new NBTTagList();
                this.addChildrenToList(child, newList);
                list.appendTag(newList);
            }
            else {
                list.appendTag(base.copy());
            }
        }
    }
    
    public void print() {
        this.print(this.root, 0);
    }
    
    private void print(final Node<NamedNBT> n, final int i) {
        System.out.println(String.valueOf(repeat("\t", i)) + NBTStringHelper.getNBTName(n.getObject()));
        for (final Node<NamedNBT> child : n.getChildren()) {
            this.print(child, i + 1);
        }
    }
    
    public List<String> toStrings() {
        final List<String> s = new ArrayList<String>();
        this.toStrings(s, this.root, 0);
        return s;
    }
    
    private void toStrings(final List<String> s, final Node<NamedNBT> n, final int i) {
        s.add(String.valueOf(repeat("   ", i)) + NBTStringHelper.getNBTName(n.getObject()));
        for (final Node<NamedNBT> child : n.getChildren()) {
            this.toStrings(s, child, i + 1);
        }
    }
    
    public static String repeat(final String c, final int i) {
        final StringBuilder b = new StringBuilder(i + 1);
        for (int j = 0; j < i; ++j) {
            b.append(c);
        }
        return b.toString();
    }
}
