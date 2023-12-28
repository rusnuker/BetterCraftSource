// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

import java.awt.image.BufferedImage;

public interface Group extends Comparable<Group>
{
    int getId();
    
    String getName();
    
    boolean isShowPrefix();
    
    int getType();
    
    BufferedImage getIcon();
    
    int getIconId();
    
    boolean isPersistent();
    
    int getSortId();
    
    int getModifyPower();
    
    int getMemberAddPower();
    
    int getMemberRemovePower();
}
