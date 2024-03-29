// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.resolver;

import java.util.Iterator;
import org.yaml.snakeyaml.nodes.NodeId;
import java.util.ArrayList;
import java.util.HashMap;
import org.yaml.snakeyaml.nodes.Tag;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Resolver
{
    public static final Pattern BOOL;
    public static final Pattern FLOAT;
    public static final Pattern INT;
    public static final Pattern MERGE;
    public static final Pattern NULL;
    public static final Pattern EMPTY;
    public static final Pattern TIMESTAMP;
    public static final Pattern VALUE;
    public static final Pattern YAML;
    protected Map<Character, List<ResolverTuple>> yamlImplicitResolvers;
    
    protected void addImplicitResolvers() {
        this.addImplicitResolver(Tag.BOOL, Resolver.BOOL, "yYnNtTfFoO", 10);
        this.addImplicitResolver(Tag.INT, Resolver.INT, "-+0123456789");
        this.addImplicitResolver(Tag.FLOAT, Resolver.FLOAT, "-+0123456789.");
        this.addImplicitResolver(Tag.MERGE, Resolver.MERGE, "<", 10);
        this.addImplicitResolver(Tag.NULL, Resolver.NULL, "~nN\u0000", 10);
        this.addImplicitResolver(Tag.NULL, Resolver.EMPTY, null, 10);
        this.addImplicitResolver(Tag.TIMESTAMP, Resolver.TIMESTAMP, "0123456789", 50);
        this.addImplicitResolver(Tag.YAML, Resolver.YAML, "!&*", 10);
    }
    
    public Resolver() {
        this.yamlImplicitResolvers = new HashMap<Character, List<ResolverTuple>>();
        this.addImplicitResolvers();
    }
    
    public void addImplicitResolver(final Tag tag, final Pattern regexp, final String first) {
        this.addImplicitResolver(tag, regexp, first, 1024);
    }
    
    public void addImplicitResolver(final Tag tag, final Pattern regexp, final String first, final int limit) {
        if (first == null) {
            List<ResolverTuple> curr = this.yamlImplicitResolvers.get(null);
            if (curr == null) {
                curr = new ArrayList<ResolverTuple>();
                this.yamlImplicitResolvers.put(null, curr);
            }
            curr.add(new ResolverTuple(tag, regexp, limit));
        }
        else {
            final char[] chrs = first.toCharArray();
            for (int i = 0, j = chrs.length; i < j; ++i) {
                Character theC = chrs[i];
                if (theC == '\0') {
                    theC = null;
                }
                List<ResolverTuple> curr2 = this.yamlImplicitResolvers.get(theC);
                if (curr2 == null) {
                    curr2 = new ArrayList<ResolverTuple>();
                    this.yamlImplicitResolvers.put(theC, curr2);
                }
                curr2.add(new ResolverTuple(tag, regexp, limit));
            }
        }
    }
    
    public Tag resolve(final NodeId kind, final String value, final boolean implicit) {
        if (kind == NodeId.scalar && implicit) {
            List<ResolverTuple> resolvers;
            if (value.length() == 0) {
                resolvers = this.yamlImplicitResolvers.get('\0');
            }
            else {
                resolvers = this.yamlImplicitResolvers.get(value.charAt(0));
            }
            if (resolvers != null) {
                for (final ResolverTuple v : resolvers) {
                    final Tag tag = v.getTag();
                    final Pattern regexp = v.getRegexp();
                    if (value.length() <= v.getLimit() && regexp.matcher(value).matches()) {
                        return tag;
                    }
                }
            }
            if (this.yamlImplicitResolvers.containsKey(null)) {
                for (final ResolverTuple v : this.yamlImplicitResolvers.get(null)) {
                    final Tag tag = v.getTag();
                    final Pattern regexp = v.getRegexp();
                    if (value.length() <= v.getLimit() && regexp.matcher(value).matches()) {
                        return tag;
                    }
                }
            }
        }
        switch (kind) {
            case scalar: {
                return Tag.STR;
            }
            case sequence: {
                return Tag.SEQ;
            }
            default: {
                return Tag.MAP;
            }
        }
    }
    
    static {
        BOOL = Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");
        FLOAT = Pattern.compile("^([-+]?(?:[0-9][0-9_]*)\\.[0-9_]*(?:[eE][-+]?[0-9]+)?|[-+]?(?:[0-9][0-9_]*)(?:[eE][-+]?[0-9]+)|[-+]?\\.[0-9_]+(?:[eE][-+]?[0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
        INT = Pattern.compile("^(?:[-+]?0b_*[0-1][0-1_]*|[-+]?0_*[0-7][0-7_]*|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x_*[0-9a-fA-F][0-9a-fA-F_]*|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
        MERGE = Pattern.compile("^(?:<<)$");
        NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
        EMPTY = Pattern.compile("^$");
        TIMESTAMP = Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
        VALUE = Pattern.compile("^(?:=)$");
        YAML = Pattern.compile("^(?:!|&|\\*)$");
    }
}
