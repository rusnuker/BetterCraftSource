// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.nodes;

import java.util.Date;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.util.UriEncoder;
import java.util.Map;
import java.util.Set;

public final class Tag
{
    public static final String PREFIX = "tag:yaml.org,2002:";
    public static final Tag YAML;
    public static final Tag MERGE;
    public static final Tag SET;
    public static final Tag PAIRS;
    public static final Tag OMAP;
    public static final Tag BINARY;
    public static final Tag INT;
    public static final Tag FLOAT;
    public static final Tag TIMESTAMP;
    public static final Tag BOOL;
    public static final Tag NULL;
    public static final Tag STR;
    public static final Tag SEQ;
    public static final Tag MAP;
    public static final Set<Tag> standardTags;
    public static final Tag COMMENT;
    private static final Map<Tag, Set<Class<?>>> COMPATIBILITY_MAP;
    private final String value;
    private boolean secondary;
    
    public Tag(final String tag) {
        this.secondary = false;
        if (tag == null) {
            throw new NullPointerException("Tag must be provided.");
        }
        if (tag.length() == 0) {
            throw new IllegalArgumentException("Tag must not be empty.");
        }
        if (tag.trim().length() != tag.length()) {
            throw new IllegalArgumentException("Tag must not contain leading or trailing spaces.");
        }
        this.value = UriEncoder.encode(tag);
        this.secondary = !tag.startsWith("tag:yaml.org,2002:");
    }
    
    public Tag(final Class<?> clazz) {
        this.secondary = false;
        if (clazz == null) {
            throw new NullPointerException("Class for tag must be provided.");
        }
        this.value = "tag:yaml.org,2002:" + UriEncoder.encode(clazz.getName());
    }
    
    public boolean isSecondary() {
        return this.secondary;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public boolean startsWith(final String prefix) {
        return this.value.startsWith(prefix);
    }
    
    public String getClassName() {
        if (this.secondary) {
            throw new YAMLException("Invalid tag: " + this.value);
        }
        return UriEncoder.decode(this.value.substring("tag:yaml.org,2002:".length()));
    }
    
    @Override
    public String toString() {
        return this.value;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Tag && this.value.equals(((Tag)obj).getValue());
    }
    
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
    
    public boolean isCompatible(final Class<?> clazz) {
        final Set<Class<?>> set = Tag.COMPATIBILITY_MAP.get(this);
        return set != null && set.contains(clazz);
    }
    
    public boolean matches(final Class<?> clazz) {
        return this.value.equals("tag:yaml.org,2002:" + clazz.getName());
    }
    
    public boolean isCustomGlobal() {
        return !this.secondary && !Tag.standardTags.contains(this);
    }
    
    static {
        YAML = new Tag("tag:yaml.org,2002:yaml");
        MERGE = new Tag("tag:yaml.org,2002:merge");
        SET = new Tag("tag:yaml.org,2002:set");
        PAIRS = new Tag("tag:yaml.org,2002:pairs");
        OMAP = new Tag("tag:yaml.org,2002:omap");
        BINARY = new Tag("tag:yaml.org,2002:binary");
        INT = new Tag("tag:yaml.org,2002:int");
        FLOAT = new Tag("tag:yaml.org,2002:float");
        TIMESTAMP = new Tag("tag:yaml.org,2002:timestamp");
        BOOL = new Tag("tag:yaml.org,2002:bool");
        NULL = new Tag("tag:yaml.org,2002:null");
        STR = new Tag("tag:yaml.org,2002:str");
        SEQ = new Tag("tag:yaml.org,2002:seq");
        MAP = new Tag("tag:yaml.org,2002:map");
        (standardTags = new HashSet<Tag>(15)).add(Tag.YAML);
        Tag.standardTags.add(Tag.MERGE);
        Tag.standardTags.add(Tag.SET);
        Tag.standardTags.add(Tag.PAIRS);
        Tag.standardTags.add(Tag.OMAP);
        Tag.standardTags.add(Tag.BINARY);
        Tag.standardTags.add(Tag.INT);
        Tag.standardTags.add(Tag.FLOAT);
        Tag.standardTags.add(Tag.TIMESTAMP);
        Tag.standardTags.add(Tag.BOOL);
        Tag.standardTags.add(Tag.NULL);
        Tag.standardTags.add(Tag.STR);
        Tag.standardTags.add(Tag.SEQ);
        Tag.standardTags.add(Tag.MAP);
        COMMENT = new Tag("tag:yaml.org,2002:comment");
        COMPATIBILITY_MAP = new HashMap<Tag, Set<Class<?>>>();
        final Set<Class<?>> floatSet = new HashSet<Class<?>>();
        floatSet.add(Double.class);
        floatSet.add(Float.class);
        floatSet.add(BigDecimal.class);
        Tag.COMPATIBILITY_MAP.put(Tag.FLOAT, floatSet);
        final Set<Class<?>> intSet = new HashSet<Class<?>>();
        intSet.add(Integer.class);
        intSet.add(Long.class);
        intSet.add(BigInteger.class);
        Tag.COMPATIBILITY_MAP.put(Tag.INT, intSet);
        final Set<Class<?>> timestampSet = new HashSet<Class<?>>();
        timestampSet.add(Date.class);
        try {
            timestampSet.add(Class.forName("java.sql.Date"));
            timestampSet.add(Class.forName("java.sql.Timestamp"));
        }
        catch (final ClassNotFoundException ex) {}
        Tag.COMPATIBILITY_MAP.put(Tag.TIMESTAMP, timestampSet);
    }
}
