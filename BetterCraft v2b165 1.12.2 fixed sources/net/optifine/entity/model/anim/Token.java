// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

public class Token
{
    private EnumTokenType type;
    private String text;
    
    public Token(final EnumTokenType type, final String text) {
        this.type = type;
        this.text = text;
    }
    
    public EnumTokenType getType() {
        return this.type;
    }
    
    public String getText() {
        return this.text;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
}
