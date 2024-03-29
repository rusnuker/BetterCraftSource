// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml;

import java.lang.reflect.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Collection;
import org.yaml.snakeyaml.error.YAMLException;
import java.util.Collections;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertySubstitute;
import java.util.Map;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.introspector.Property;
import java.util.Set;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.internal.Logger;

public class TypeDescription
{
    private static final Logger log;
    private final Class<?> type;
    private Class<?> impl;
    private final Tag tag;
    private transient Set<Property> dumpProperties;
    private transient PropertyUtils propertyUtils;
    private transient boolean delegatesChecked;
    private Map<String, PropertySubstitute> properties;
    protected Set<String> excludes;
    protected String[] includes;
    protected BeanAccess beanAccess;
    
    public TypeDescription(final Class<?> clazz, final Tag tag) {
        this(clazz, tag, null);
    }
    
    public TypeDescription(final Class<?> clazz, final Tag tag, final Class<?> impl) {
        this.properties = Collections.emptyMap();
        this.excludes = Collections.emptySet();
        this.includes = null;
        this.type = clazz;
        this.tag = tag;
        this.impl = impl;
        this.beanAccess = null;
    }
    
    public TypeDescription(final Class<?> clazz, final String tag) {
        this(clazz, new Tag(tag), null);
    }
    
    public TypeDescription(final Class<?> clazz) {
        this(clazz, new Tag(clazz), null);
    }
    
    public TypeDescription(final Class<?> clazz, final Class<?> impl) {
        this(clazz, new Tag(clazz), impl);
    }
    
    public Tag getTag() {
        return this.tag;
    }
    
    public Class<?> getType() {
        return this.type;
    }
    
    @Deprecated
    public void putListPropertyType(final String property, final Class<?> type) {
        this.addPropertyParameters(property, type);
    }
    
    @Deprecated
    public void putMapPropertyType(final String property, final Class<?> key, final Class<?> value) {
        this.addPropertyParameters(property, key, value);
    }
    
    public void addPropertyParameters(final String pName, final Class<?>... classes) {
        if (!this.properties.containsKey(pName)) {
            this.substituteProperty(pName, null, null, null, classes);
        }
        else {
            final PropertySubstitute pr = this.properties.get(pName);
            pr.setActualTypeArguments(classes);
        }
    }
    
    @Override
    public String toString() {
        return "TypeDescription for " + this.getType() + " (tag='" + this.getTag() + "')";
    }
    
    private void checkDelegates() {
        final Collection<PropertySubstitute> values = this.properties.values();
        for (final PropertySubstitute p : values) {
            try {
                p.setDelegate(this.discoverProperty(p.getName()));
            }
            catch (final YAMLException ex) {}
        }
        this.delegatesChecked = true;
    }
    
    private Property discoverProperty(final String name) {
        if (this.propertyUtils == null) {
            return null;
        }
        if (this.beanAccess == null) {
            return this.propertyUtils.getProperty(this.type, name);
        }
        return this.propertyUtils.getProperty(this.type, name, this.beanAccess);
    }
    
    public Property getProperty(final String name) {
        if (!this.delegatesChecked) {
            this.checkDelegates();
        }
        return this.properties.containsKey(name) ? this.properties.get(name) : this.discoverProperty(name);
    }
    
    public void substituteProperty(final String pName, final Class<?> pType, final String getter, final String setter, final Class<?>... argParams) {
        this.substituteProperty(new PropertySubstitute(pName, pType, getter, setter, argParams));
    }
    
    public void substituteProperty(final PropertySubstitute substitute) {
        if (Collections.EMPTY_MAP == this.properties) {
            this.properties = new LinkedHashMap<String, PropertySubstitute>();
        }
        substitute.setTargetType(this.type);
        this.properties.put(substitute.getName(), substitute);
    }
    
    public void setPropertyUtils(final PropertyUtils propertyUtils) {
        this.propertyUtils = propertyUtils;
    }
    
    public void setIncludes(final String... propNames) {
        this.includes = (String[])((propNames != null && propNames.length > 0) ? propNames : null);
    }
    
    public void setExcludes(final String... propNames) {
        if (propNames != null && propNames.length > 0) {
            Collections.addAll(this.excludes = new HashSet<String>(), propNames);
        }
        else {
            this.excludes = Collections.emptySet();
        }
    }
    
    public Set<Property> getProperties() {
        if (this.dumpProperties != null) {
            return this.dumpProperties;
        }
        if (this.propertyUtils == null) {
            return null;
        }
        if (this.includes != null) {
            this.dumpProperties = new LinkedHashSet<Property>();
            for (final String propertyName : this.includes) {
                if (!this.excludes.contains(propertyName)) {
                    this.dumpProperties.add(this.getProperty(propertyName));
                }
            }
            return this.dumpProperties;
        }
        final Set<Property> readableProps = (this.beanAccess == null) ? this.propertyUtils.getProperties(this.type) : this.propertyUtils.getProperties(this.type, this.beanAccess);
        if (!this.properties.isEmpty()) {
            if (!this.delegatesChecked) {
                this.checkDelegates();
            }
            this.dumpProperties = new LinkedHashSet<Property>();
            for (final Property property : this.properties.values()) {
                if (!this.excludes.contains(property.getName()) && property.isReadable()) {
                    this.dumpProperties.add(property);
                }
            }
            for (final Property property : readableProps) {
                if (!this.excludes.contains(property.getName())) {
                    this.dumpProperties.add(property);
                }
            }
            return this.dumpProperties;
        }
        if (this.excludes.isEmpty()) {
            return this.dumpProperties = readableProps;
        }
        this.dumpProperties = new LinkedHashSet<Property>();
        for (final Property property : readableProps) {
            if (!this.excludes.contains(property.getName())) {
                this.dumpProperties.add(property);
            }
        }
        return this.dumpProperties;
    }
    
    public boolean setupPropertyType(final String key, final Node valueNode) {
        return false;
    }
    
    public boolean setProperty(final Object targetBean, final String propertyName, final Object value) throws Exception {
        return false;
    }
    
    public Object newInstance(final Node node) {
        if (this.impl != null) {
            try {
                final Constructor<?> c = this.impl.getDeclaredConstructor((Class<?>[])new Class[0]);
                c.setAccessible(true);
                return c.newInstance(new Object[0]);
            }
            catch (final Exception e) {
                TypeDescription.log.warn(e.getLocalizedMessage());
                this.impl = null;
            }
        }
        return null;
    }
    
    public Object newInstance(final String propertyName, final Node node) {
        return null;
    }
    
    public Object finalizeConstruction(final Object obj) {
        return obj;
    }
    
    static {
        log = Logger.getLogger(TypeDescription.class.getPackage().getName());
    }
}
