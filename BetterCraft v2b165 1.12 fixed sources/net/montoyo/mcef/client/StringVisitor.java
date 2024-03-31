// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import net.montoyo.mcef.api.IStringVisitor;
import org.cef.callback.CefStringVisitor;

public class StringVisitor implements CefStringVisitor
{
    IStringVisitor isv;
    
    public StringVisitor(final IStringVisitor isv) {
        this.isv = isv;
    }
    
    @Override
    public void visit(final String string) {
        this.isv.visit(string);
    }
}
