// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.misc.BoolRef;
import org.cef.browser.CefBrowser;

public interface CefKeyboardHandler
{
    boolean onPreKeyEvent(final CefBrowser p0, final CefKeyEvent p1, final BoolRef p2);
    
    boolean onKeyEvent(final CefBrowser p0, final CefKeyEvent p1);
    
    public static final class CefKeyEvent
    {
        public final EventType type;
        public final int modifiers;
        public final int windows_key_code;
        public final int native_key_code;
        public final boolean is_system_key;
        public final char character;
        public final char unmodified_character;
        public final boolean focus_on_editable_field;
        
        CefKeyEvent(final EventType typeAttr, final int modifiersAttr, final int windows_key_codeAttr, final int native_key_codeAttr, final boolean is_system_keyAttr, final char characterAttr, final char unmodified_characterAttr, final boolean focus_on_editable_fieldAttr) {
            this.type = typeAttr;
            this.modifiers = modifiersAttr;
            this.windows_key_code = windows_key_codeAttr;
            this.native_key_code = native_key_codeAttr;
            this.is_system_key = is_system_keyAttr;
            this.character = characterAttr;
            this.unmodified_character = unmodified_characterAttr;
            this.focus_on_editable_field = focus_on_editable_fieldAttr;
        }
        
        @Override
        public String toString() {
            return "CefKeyEvent [type=" + this.type + ", modifiers=" + this.modifiers + ", windows_key_code=" + this.windows_key_code + ", native_key_code=" + this.native_key_code + ", is_system_key=" + this.is_system_key + ", character=" + this.character + ", unmodified_character=" + this.unmodified_character + ", focus_on_editable_field=" + this.focus_on_editable_field + "]";
        }
        
        public enum EventType
        {
            KEYEVENT_RAWKEYDOWN("KEYEVENT_RAWKEYDOWN", 0), 
            KEYEVENT_KEYDOWN("KEYEVENT_KEYDOWN", 1), 
            KEYEVENT_KEYUP("KEYEVENT_KEYUP", 2), 
            KEYEVENT_CHAR("KEYEVENT_CHAR", 3);
            
            private EventType(final String s, final int n) {
            }
        }
    }
}
