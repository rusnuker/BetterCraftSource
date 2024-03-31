// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.manager;

import javax.xml.bind.DatatypeConverter;

public class Base64Manager
{
    public static String encode(final String string) {
        final String encoded = DatatypeConverter.printBase64Binary(string.getBytes());
        return encoded;
    }
    
    public static String decode(final String base64String) {
        final String decoded = new String(DatatypeConverter.parseBase64Binary(base64String));
        return decoded;
    }
}
