// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.text2speech;

public class NarratorDummy implements Narrator
{
    @Override
    public void say(final String msg) {
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public boolean active() {
        return false;
    }
}
