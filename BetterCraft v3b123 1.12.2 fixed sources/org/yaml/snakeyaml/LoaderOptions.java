// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml;

import org.yaml.snakeyaml.inspector.UnTrustedTagInspector;
import org.yaml.snakeyaml.inspector.TagInspector;

public class LoaderOptions
{
    private boolean allowDuplicateKeys;
    private boolean wrappedToRootException;
    private int maxAliasesForCollections;
    private boolean allowRecursiveKeys;
    private boolean processComments;
    private boolean enumCaseSensitive;
    private int nestingDepthLimit;
    private int codePointLimit;
    private TagInspector tagInspector;
    
    public LoaderOptions() {
        this.allowDuplicateKeys = true;
        this.wrappedToRootException = false;
        this.maxAliasesForCollections = 50;
        this.allowRecursiveKeys = false;
        this.processComments = false;
        this.enumCaseSensitive = true;
        this.nestingDepthLimit = 50;
        this.codePointLimit = 3145728;
        this.tagInspector = new UnTrustedTagInspector();
    }
    
    public final boolean isAllowDuplicateKeys() {
        return this.allowDuplicateKeys;
    }
    
    public void setAllowDuplicateKeys(final boolean allowDuplicateKeys) {
        this.allowDuplicateKeys = allowDuplicateKeys;
    }
    
    public final boolean isWrappedToRootException() {
        return this.wrappedToRootException;
    }
    
    public void setWrappedToRootException(final boolean wrappedToRootException) {
        this.wrappedToRootException = wrappedToRootException;
    }
    
    public final int getMaxAliasesForCollections() {
        return this.maxAliasesForCollections;
    }
    
    public void setMaxAliasesForCollections(final int maxAliasesForCollections) {
        this.maxAliasesForCollections = maxAliasesForCollections;
    }
    
    public final boolean getAllowRecursiveKeys() {
        return this.allowRecursiveKeys;
    }
    
    public void setAllowRecursiveKeys(final boolean allowRecursiveKeys) {
        this.allowRecursiveKeys = allowRecursiveKeys;
    }
    
    public final boolean isProcessComments() {
        return this.processComments;
    }
    
    public LoaderOptions setProcessComments(final boolean processComments) {
        this.processComments = processComments;
        return this;
    }
    
    public final boolean isEnumCaseSensitive() {
        return this.enumCaseSensitive;
    }
    
    public void setEnumCaseSensitive(final boolean enumCaseSensitive) {
        this.enumCaseSensitive = enumCaseSensitive;
    }
    
    public final int getNestingDepthLimit() {
        return this.nestingDepthLimit;
    }
    
    public void setNestingDepthLimit(final int nestingDepthLimit) {
        this.nestingDepthLimit = nestingDepthLimit;
    }
    
    public final int getCodePointLimit() {
        return this.codePointLimit;
    }
    
    public void setCodePointLimit(final int codePointLimit) {
        this.codePointLimit = codePointLimit;
    }
    
    public TagInspector getTagInspector() {
        return this.tagInspector;
    }
    
    public void setTagInspector(final TagInspector tagInspector) {
        this.tagInspector = tagInspector;
    }
}
