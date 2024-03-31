// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.util;

import net.labymod.utils.ModColor;

public enum EnumUserRank
{
    ADMIN("ADMIN", 0, "ADMIN", 0, 25, '4', "ADMIN", true, true, true), 
    DEVELOPER("DEVELOPER", 1, "DEVELOPER", 1, 24, 'b', "DEV", true, true, true), 
    MODERATOR("MODERATOR", 2, "MODERATOR", 2, 23, '6', "MOD", true, true, true), 
    SUPPORTER("SUPPORTER", 3, "SUPPORTER", 3, 22, 'e', "SUP", true, true, true), 
    CONTENT("CONTENT", 4, "CONTENT", 4, 21, '2', "CONTENT", true, true, true), 
    TEST_SUPPORTER("TEST_SUPPORTER", 5, "TEST_SUPPORTER", 5, 20, 'e', "TSUP", false, false, true), 
    YOUTUBER("YOUTUBER", 6, "YOUTUBER", 6, 2, '5', "YOUTUBER", false, false, true), 
    PREMIUM("PREMIUM", 7, "PREMIUM", 7, 1, '6', "PREMIUM", false, false, true), 
    USER("USER", 8, "USER", 8, 0, 'f', "USER", false, false, false);
    
    private int id;
    private char color;
    private boolean render;
    private String tagName;
    private boolean staff;
    private boolean premium;
    
    private EnumUserRank(final String s2, final int n3, final String s, final int n2, final int id, final char color, final String tagName, final boolean render, final boolean staff, final boolean premium) {
        this.id = id;
        this.color = color;
        this.tagName = tagName;
        this.render = render;
        this.staff = staff;
        this.premium = premium;
    }
    
    public static EnumUserRank getById(final int id) {
        EnumUserRank[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumUserRank enumuserrank = values[i];
            if (enumuserrank.getId() == id) {
                return enumuserrank;
            }
        }
        return null;
    }
    
    public String buildTag() {
        return String.valueOf(String.valueOf(ModColor.cl("f"))) + ModColor.cl("l") + "LABYMOD " + '§' + this.color + this.tagName;
    }
    
    public int getId() {
        return this.id;
    }
    
    public char getColor() {
        return this.color;
    }
    
    public boolean isRender() {
        return this.render;
    }
    
    public String getTagName() {
        return this.tagName;
    }
    
    public boolean isStaff() {
        return this.staff;
    }
    
    public boolean isPremium() {
        return this.premium;
    }
}
