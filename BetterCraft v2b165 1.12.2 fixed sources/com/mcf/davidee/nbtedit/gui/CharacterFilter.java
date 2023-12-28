// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.gui;

import net.minecraft.util.ChatAllowedCharacters;

public class CharacterFilter
{
    public static String filerAllowedCharacters(final String str, final boolean section) {
        final StringBuilder sb = new StringBuilder();
        final char[] arr = str.toCharArray();
        char[] array;
        for (int length = (array = arr).length, i = 0; i < length; ++i) {
            final char c = array[i];
            if (ChatAllowedCharacters.isAllowedCharacter(c) || (section && (c == '§' || c == '\n'))) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
