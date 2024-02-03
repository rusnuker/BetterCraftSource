// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.main.lang.LanguageManager;

public enum EnumConnectionState
{
    HELLO("HELLO", 0, -1, "d"), 
    LOGIN("LOGIN", 1, 0, "b"), 
    PLAY("PLAY", 2, 1, "a"), 
    ALL("ALL", 3, 2, "f", "ALL"), 
    OFFLINE("OFFLINE", 4, 3, "c");
    
    private int number;
    private String displayColor;
    private String buttonState;
    
    private EnumConnectionState(final String s, final int n, final int number, final String displayColor) {
        this.number = number;
        this.displayColor = displayColor;
        this.buttonState = LanguageManager.translate("chat_button_state_" + this.name().toLowerCase());
    }
    
    private EnumConnectionState(final String s, final int n, final int number, final String displayColor, final String buttonState) {
        this.number = number;
        this.displayColor = displayColor;
        this.buttonState = buttonState;
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public String getDisplayColor() {
        return this.displayColor;
    }
    
    public String getButtonState() {
        return this.buttonState;
    }
}
