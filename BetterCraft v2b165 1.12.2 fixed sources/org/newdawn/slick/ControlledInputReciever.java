// 
// Decompiled by Procyon v0.6.0
// 

package org.newdawn.slick;

public interface ControlledInputReciever
{
    void setInput(final Input p0);
    
    boolean isAcceptingInput();
    
    void inputEnded();
    
    void inputStarted();
}
