// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client;

import java.security.SecureRandom;

public class ClientBrandRetriever
{
    private static SecureRandom random;
    
    static {
        ClientBrandRetriever.random = new SecureRandom();
    }
    
    public static String getClientModName() {
        return "B" + ClientBrandRetriever.random.nextInt(9) + "e" + ClientBrandRetriever.random.nextInt(9) + "t" + ClientBrandRetriever.random.nextInt(9) + "t" + ClientBrandRetriever.random.nextInt(9) + "e" + ClientBrandRetriever.random.nextInt(9) + "r" + ClientBrandRetriever.random.nextInt(9) + "C" + ClientBrandRetriever.random.nextInt(9) + "r" + ClientBrandRetriever.random.nextInt(9) + "a" + ClientBrandRetriever.random.nextInt(9) + "f" + ClientBrandRetriever.random.nextInt(9) + "t";
    }
}
