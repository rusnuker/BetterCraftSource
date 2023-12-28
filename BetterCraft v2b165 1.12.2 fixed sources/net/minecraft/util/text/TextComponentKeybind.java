// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.text;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.function.Function;

public class TextComponentKeybind extends TextComponentBase
{
    public static Function<String, Supplier<String>> field_193637_b;
    private final String field_193638_c;
    private Supplier<String> field_193639_d;
    
    static {
        TextComponentKeybind.field_193637_b = (Function<String, Supplier<String>>)(p_193635_0_ -> () -> s2);
    }
    
    public TextComponentKeybind(final String p_i47521_1_) {
        this.field_193638_c = p_i47521_1_;
    }
    
    @Override
    public String getUnformattedComponentText() {
        if (this.field_193639_d == null) {
            this.field_193639_d = TextComponentKeybind.field_193637_b.apply(this.field_193638_c);
        }
        return this.field_193639_d.get();
    }
    
    @Override
    public TextComponentKeybind createCopy() {
        final TextComponentKeybind textcomponentkeybind = new TextComponentKeybind(this.field_193638_c);
        textcomponentkeybind.setStyle(this.getStyle().createShallowCopy());
        for (final ITextComponent itextcomponent : this.getSiblings()) {
            textcomponentkeybind.appendSibling(itextcomponent.createCopy());
        }
        return textcomponentkeybind;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof TextComponentKeybind)) {
            return false;
        }
        final TextComponentKeybind textcomponentkeybind = (TextComponentKeybind)p_equals_1_;
        return this.field_193638_c.equals(textcomponentkeybind.field_193638_c) && super.equals(p_equals_1_);
    }
    
    @Override
    public String toString() {
        return "KeybindComponent{keybind='" + this.field_193638_c + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
    
    public String func_193633_h() {
        return this.field_193638_c;
    }
}
