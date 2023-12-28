// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.api;

public interface IBrowser
{
    void close();
    
    void resize(final int p0, final int p1);
    
    void draw(final double p0, final double p1, final double p2, final double p3);
    
    int getTextureID();
    
    void injectMouseMove(final int p0, final int p1, final int p2, final boolean p3);
    
    void injectMouseButton(final int p0, final int p1, final int p2, final int p3, final boolean p4, final int p5);
    
    void injectKeyTyped(final char p0, final int p1);
    
    void injectKeyPressedByKeyCode(final int p0, final char p1, final int p2);
    
    void injectKeyReleasedByKeyCode(final int p0, final char p1, final int p2);
    
    void injectMouseWheel(final int p0, final int p1, final int p2, final int p3, final int p4);
    
    void runJS(final String p0, final String p1);
    
    void loadURL(final String p0);
    
    void goBack();
    
    void goForward();
    
    String getURL();
    
    void visitSource(final IStringVisitor p0);
    
    boolean isPageLoading();
}
