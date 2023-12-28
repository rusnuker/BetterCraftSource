// 
// Decompiled by Procyon v0.6.0
// 

package org.json;

public class XMLParserConfiguration
{
    public static final XMLParserConfiguration ORIGINAL;
    public static final XMLParserConfiguration KEEP_STRINGS;
    public final boolean keepStrings;
    public final String cDataTagName;
    public final boolean convertNilAttributeToNull;
    
    static {
        ORIGINAL = new XMLParserConfiguration();
        KEEP_STRINGS = new XMLParserConfiguration(true);
    }
    
    public XMLParserConfiguration() {
        this(false, "content", false);
    }
    
    public XMLParserConfiguration(final boolean keepStrings) {
        this(keepStrings, "content", false);
    }
    
    public XMLParserConfiguration(final String cDataTagName) {
        this(false, cDataTagName, false);
    }
    
    public XMLParserConfiguration(final boolean keepStrings, final String cDataTagName) {
        this.keepStrings = keepStrings;
        this.cDataTagName = cDataTagName;
        this.convertNilAttributeToNull = false;
    }
    
    public XMLParserConfiguration(final boolean keepStrings, final String cDataTagName, final boolean convertNilAttributeToNull) {
        this.keepStrings = keepStrings;
        this.cDataTagName = cDataTagName;
        this.convertNilAttributeToNull = convertNilAttributeToNull;
    }
}
