// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.misc.BoolRef;
import org.cef.browser.CefBrowser;

public abstract class CefKeyboardHandlerAdapter implements CefKeyboardHandler
{
    @Override
    public boolean onPreKeyEvent(final CefBrowser browser, final CefKeyEvent event, final BoolRef is_keyboard_shortcut) {
        return false;
    }
    
    @Override
    public boolean onKeyEvent(final CefBrowser browser, final CefKeyEvent event) {
        return false;
    }
}
