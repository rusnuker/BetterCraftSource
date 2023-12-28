// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;

public class NameGenerator
{
    private static final Random random;
    
    static {
        random = new Random();
    }
    
    public static String generateRandomName(final int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
