// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.patchy;

import java.util.Hashtable;
import javax.naming.NameParser;
import javax.naming.Context;
import javax.naming.Binding;
import javax.naming.NameClassPair;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.NamingEnumeration;
import javax.naming.directory.ModificationItem;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.Attributes;
import java.util.function.Predicate;
import javax.naming.directory.DirContext;

public class BlockingDC implements DirContext
{
    private final Predicate<String> blockList;
    private final DirContext parent;
    
    public BlockingDC(final Predicate<String> blockList, final DirContext parent) {
        this.blockList = blockList;
        this.parent = parent;
    }
    
    @Override
    public Attributes getAttributes(final String name, final String[] attrIds) throws NamingException {
        if (this.blockList.test(name)) {
            return new BasicAttributes();
        }
        return this.parent.getAttributes(name, attrIds);
    }
    
    @Override
    public Attributes getAttributes(final String name) throws NamingException {
        if (this.blockList.test(name)) {
            return new BasicAttributes();
        }
        return this.parent.getAttributes(name);
    }
    
    @Override
    public Attributes getAttributes(final Name name) throws NamingException {
        return this.parent.getAttributes(name);
    }
    
    @Override
    public Attributes getAttributes(final Name name, final String[] attrIds) throws NamingException {
        return this.parent.getAttributes(name, attrIds);
    }
    
    @Override
    public void modifyAttributes(final Name name, final int mod_op, final Attributes attrs) throws NamingException {
        this.parent.modifyAttributes(name, mod_op, attrs);
    }
    
    @Override
    public void modifyAttributes(final String name, final int mod_op, final Attributes attrs) throws NamingException {
        this.parent.modifyAttributes(name, mod_op, attrs);
    }
    
    @Override
    public void modifyAttributes(final Name name, final ModificationItem[] mods) throws NamingException {
        this.parent.modifyAttributes(name, mods);
    }
    
    @Override
    public void modifyAttributes(final String name, final ModificationItem[] mods) throws NamingException {
        this.parent.modifyAttributes(name, mods);
    }
    
    @Override
    public void bind(final Name name, final Object obj, final Attributes attrs) throws NamingException {
        this.parent.bind(name, obj, attrs);
    }
    
    @Override
    public void bind(final String name, final Object obj, final Attributes attrs) throws NamingException {
        this.parent.bind(name, obj, attrs);
    }
    
    @Override
    public void rebind(final Name name, final Object obj, final Attributes attrs) throws NamingException {
        this.parent.rebind(name, obj, attrs);
    }
    
    @Override
    public void rebind(final String name, final Object obj, final Attributes attrs) throws NamingException {
        this.parent.rebind(name, obj, attrs);
    }
    
    @Override
    public DirContext createSubcontext(final Name name, final Attributes attrs) throws NamingException {
        return this.parent.createSubcontext(name, attrs);
    }
    
    @Override
    public DirContext createSubcontext(final String name, final Attributes attrs) throws NamingException {
        return this.parent.createSubcontext(name, attrs);
    }
    
    @Override
    public DirContext getSchema(final Name name) throws NamingException {
        return this.parent.getSchema(name);
    }
    
    @Override
    public DirContext getSchema(final String name) throws NamingException {
        return this.parent.getSchema(name);
    }
    
    @Override
    public DirContext getSchemaClassDefinition(final Name name) throws NamingException {
        return this.parent.getSchemaClassDefinition(name);
    }
    
    @Override
    public DirContext getSchemaClassDefinition(final String name) throws NamingException {
        return this.parent.getSchemaClassDefinition(name);
    }
    
    @Override
    public NamingEnumeration<SearchResult> search(final Name name, final Attributes matchingAttributes, final String[] attributesToReturn) throws NamingException {
        return this.parent.search(name, matchingAttributes, attributesToReturn);
    }
    
    @Override
    public NamingEnumeration<SearchResult> search(final String name, final Attributes matchingAttributes, final String[] attributesToReturn) throws NamingException {
        return this.parent.search(name, matchingAttributes, attributesToReturn);
    }
    
    @Override
    public NamingEnumeration<SearchResult> search(final Name name, final Attributes matchingAttributes) throws NamingException {
        return this.parent.search(name, matchingAttributes);
    }
    
    @Override
    public NamingEnumeration<SearchResult> search(final String name, final Attributes matchingAttributes) throws NamingException {
        return this.parent.search(name, matchingAttributes);
    }
    
    @Override
    public NamingEnumeration<SearchResult> search(final Name name, final String filter, final SearchControls cons) throws NamingException {
        return this.parent.search(name, filter, cons);
    }
    
    @Override
    public NamingEnumeration<SearchResult> search(final String name, final String filter, final SearchControls cons) throws NamingException {
        return this.parent.search(name, filter, cons);
    }
    
    @Override
    public NamingEnumeration<SearchResult> search(final Name name, final String filterExpr, final Object[] filterArgs, final SearchControls cons) throws NamingException {
        return this.parent.search(name, filterExpr, filterArgs, cons);
    }
    
    @Override
    public NamingEnumeration<SearchResult> search(final String name, final String filterExpr, final Object[] filterArgs, final SearchControls cons) throws NamingException {
        return this.parent.search(name, filterExpr, filterArgs, cons);
    }
    
    @Override
    public Object lookup(final Name name) throws NamingException {
        return this.parent.lookup(name);
    }
    
    @Override
    public Object lookup(final String name) throws NamingException {
        return this.parent.lookup(name);
    }
    
    @Override
    public void bind(final Name name, final Object obj) throws NamingException {
        this.parent.bind(name, obj);
    }
    
    @Override
    public void bind(final String name, final Object obj) throws NamingException {
        this.parent.bind(name, obj);
    }
    
    @Override
    public void rebind(final Name name, final Object obj) throws NamingException {
        this.parent.rebind(name, obj);
    }
    
    @Override
    public void rebind(final String name, final Object obj) throws NamingException {
        this.parent.rebind(name, obj);
    }
    
    @Override
    public void unbind(final Name name) throws NamingException {
        this.parent.unbind(name);
    }
    
    @Override
    public void unbind(final String name) throws NamingException {
        this.parent.unbind(name);
    }
    
    @Override
    public void rename(final Name oldName, final Name newName) throws NamingException {
        this.parent.rename(oldName, newName);
    }
    
    @Override
    public void rename(final String oldName, final String newName) throws NamingException {
        this.parent.rename(oldName, newName);
    }
    
    @Override
    public NamingEnumeration<NameClassPair> list(final Name name) throws NamingException {
        return this.parent.list(name);
    }
    
    @Override
    public NamingEnumeration<NameClassPair> list(final String name) throws NamingException {
        return this.parent.list(name);
    }
    
    @Override
    public NamingEnumeration<Binding> listBindings(final Name name) throws NamingException {
        return this.parent.listBindings(name);
    }
    
    @Override
    public NamingEnumeration<Binding> listBindings(final String name) throws NamingException {
        return this.parent.listBindings(name);
    }
    
    @Override
    public void destroySubcontext(final Name name) throws NamingException {
        this.parent.destroySubcontext(name);
    }
    
    @Override
    public void destroySubcontext(final String name) throws NamingException {
        this.parent.destroySubcontext(name);
    }
    
    @Override
    public Context createSubcontext(final Name name) throws NamingException {
        return this.parent.createSubcontext(name);
    }
    
    @Override
    public Context createSubcontext(final String name) throws NamingException {
        return this.parent.createSubcontext(name);
    }
    
    @Override
    public Object lookupLink(final Name name) throws NamingException {
        return this.parent.lookupLink(name);
    }
    
    @Override
    public Object lookupLink(final String name) throws NamingException {
        return this.parent.lookupLink(name);
    }
    
    @Override
    public NameParser getNameParser(final Name name) throws NamingException {
        return this.parent.getNameParser(name);
    }
    
    @Override
    public NameParser getNameParser(final String name) throws NamingException {
        return this.parent.getNameParser(name);
    }
    
    @Override
    public Name composeName(final Name name, final Name prefix) throws NamingException {
        return this.parent.composeName(name, prefix);
    }
    
    @Override
    public String composeName(final String name, final String prefix) throws NamingException {
        return this.parent.composeName(name, prefix);
    }
    
    @Override
    public Object addToEnvironment(final String propName, final Object propVal) throws NamingException {
        return this.parent.addToEnvironment(propName, propVal);
    }
    
    @Override
    public Object removeFromEnvironment(final String propName) throws NamingException {
        return this.parent.removeFromEnvironment(propName);
    }
    
    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return this.parent.getEnvironment();
    }
    
    @Override
    public void close() throws NamingException {
        this.parent.close();
    }
    
    @Override
    public String getNameInNamespace() throws NamingException {
        return this.parent.getNameInNamespace();
    }
}
