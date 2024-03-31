// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.splash;

import net.labymod.splash.dailyemotes.DailyEmote;
import net.labymod.splash.splashdates.SplashDate;
import net.labymod.splash.advertisement.Advertisement;

public class SplashEntries
{
    private Advertisement[] left;
    private Advertisement[] right;
    private SplashDate[] splashDates;
    private DailyEmote[] dailyEmotes;
    
    public Advertisement[] getLeft() {
        return this.left;
    }
    
    public Advertisement[] getRight() {
        return this.right;
    }
    
    public SplashDate[] getSplashDates() {
        return this.splashDates;
    }
    
    public DailyEmote[] getDailyEmotes() {
        return this.dailyEmotes;
    }
}
