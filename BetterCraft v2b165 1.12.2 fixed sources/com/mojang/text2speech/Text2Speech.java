// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.text2speech;

public class Text2Speech
{
    public static void main(final String[] args) {
        System.setProperty("jna.library.path", "./src/natives/resources/");
        final Narrator narrator = Narrator.getNarrator();
        narrator.say("This is a test");
    Label_0020_Outer:
        while (true) {
            while (true) {
                try {
                    while (true) {
                        Thread.sleep(100L);
                    }
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                    continue Label_0020_Outer;
                }
                continue;
            }
        }
    }
}
