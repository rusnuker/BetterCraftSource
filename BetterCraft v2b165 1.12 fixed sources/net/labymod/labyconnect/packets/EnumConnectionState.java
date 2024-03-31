// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

public enum EnumConnectionState
{
    HELLO("HELLO", 0, "HELLO", 0, -1, "d"), 
    LOGIN("LOGIN", 1, "LOGIN", 1, 0, "b"), 
    PLAY("PLAY", 2, "PLAY", 2, 1, "a"), 
    ALL("ALL", 3, "ALL", 3, 2, "f", "ALL"), 
    OFFLINE("OFFLINE", 4, "OFFLINE", 4, 3, "c");
    
    private int number;
    private String displayColor;
    private String buttonState;
    
    private EnumConnectionState(final String s2, final int n3, final String s, final int n2, final int number, final String displayColor) {
        this.number = number;
        this.displayColor = displayColor;
        this.buttonState = "chat_button_state_" + this.name().toLowerCase();
    }
    
    private EnumConnectionState(final String s2, final int n3, final String s, final int n2, final int number, final String displayColor, final String buttonState) {
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
