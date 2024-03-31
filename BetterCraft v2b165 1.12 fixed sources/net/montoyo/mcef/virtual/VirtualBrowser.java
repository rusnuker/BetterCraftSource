// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.virtual;

import net.montoyo.mcef.api.IStringVisitor;
import net.montoyo.mcef.api.IBrowser;

public class VirtualBrowser implements IBrowser
{
    @Override
    public void close() {
    }
    
    @Override
    public void resize(final int width, final int height) {
    }
    
    @Override
    public void draw(final double x1, final double y1, final double x2, final double y2) {
    }
    
    @Override
    public int getTextureID() {
        return 0;
    }
    
    @Override
    public void injectMouseMove(final int x, final int y, final int mods, final boolean left) {
    }
    
    @Override
    public void injectMouseButton(final int x, final int y, final int mods, final int btn, final boolean pressed, final int ccnt) {
    }
    
    @Override
    public void injectKeyTyped(final char c, final int mods) {
    }
    
    @Override
    public void injectKeyPressedByKeyCode(final int keyCode, final char c, final int mods) {
    }
    
    @Override
    public void injectKeyReleasedByKeyCode(final int keyCode, final char c, final int mods) {
    }
    
    @Override
    public void injectMouseWheel(final int x, final int y, final int mods, final int amount, final int rot) {
    }
    
    @Override
    public void runJS(final String script, final String frame) {
    }
    
    @Override
    public void loadURL(final String url) {
    }
    
    @Override
    public void goBack() {
    }
    
    @Override
    public void goForward() {
    }
    
    @Override
    public String getURL() {
        return "about:blank";
    }
    
    @Override
    public void visitSource(final IStringVisitor isv) {
        isv.visit("https://www.youtube.com/watch?v=VX5gXHcbJAk");
    }
    
    @Override
    public boolean isPageLoading() {
        return true;
    }
}
