// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

public enum EnumTokenType
{
    IDENTIFIER("IDENTIFIER", 0, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", "0123456789_:."), 
    CONSTANT("CONSTANT", 1, "0123456789", "."), 
    OPERATOR("OPERATOR", 2, "+-*/%", 1), 
    COMMA("COMMA", 3, ",", 1), 
    BRACKET_OPEN("BRACKET_OPEN", 4, "(", 1), 
    BRACKET_CLOSE("BRACKET_CLOSE", 5, ")", 1);
    
    private String charsFirst;
    private String charsExt;
    private int maxLen;
    public static final EnumTokenType[] VALUES;
    
    static {
        VALUES = values();
    }
    
    private EnumTokenType(final String s, final int n, final String charsFirst) {
        this.charsFirst = charsFirst;
        this.charsExt = "";
    }
    
    private EnumTokenType(final String s, final int n, final String charsFirst, final int maxLen) {
        this.charsFirst = charsFirst;
        this.charsExt = "";
        this.maxLen = maxLen;
    }
    
    private EnumTokenType(final String s, final int n, final String charsFirst, final String charsExt) {
        this.charsFirst = charsFirst;
        this.charsExt = charsExt;
    }
    
    public String getCharsFirst() {
        return this.charsFirst;
    }
    
    public String getCharsExt() {
        return this.charsExt;
    }
    
    public static EnumTokenType getTypeByFirstChar(final char ch) {
        for (int i = 0; i < EnumTokenType.VALUES.length; ++i) {
            final EnumTokenType enumtokentype = EnumTokenType.VALUES[i];
            if (enumtokentype.getCharsFirst().indexOf(ch) >= 0) {
                return enumtokentype;
            }
        }
        return null;
    }
    
    public boolean hasChar(final char ch) {
        return this.getCharsFirst().indexOf(ch) >= 0 || this.getCharsExt().indexOf(ch) >= 0;
    }
    
    public int getMaxLen() {
        return this.maxLen;
    }
}
