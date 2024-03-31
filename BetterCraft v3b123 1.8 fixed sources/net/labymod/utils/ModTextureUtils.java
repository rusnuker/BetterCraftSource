// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import net.labymod.main.LabyMod;
import net.minecraft.util.ResourceLocation;
import com.mojang.authlib.GameProfile;

@Deprecated
public class ModTextureUtils
{
    @Deprecated
    public static ModTextureUtils INSTANCE;
    
    static {
        ModTextureUtils.INSTANCE = new ModTextureUtils();
    }
    
    @Deprecated
    public ResourceLocation getSkinTexture(final GameProfile gameProfile) {
        return LabyMod.getInstance().getDynamicTextureManager().getHeadTexture(gameProfile);
    }
    
    @Deprecated
    public void drawImageURL(final String username, final String url, final double x, final double y, final double imageWidth, final double imageHeight, final double maxWidth, final double maxHeight) {
        LabyMod.getInstance().getDrawUtils().drawImageUrl(url, x, y, imageWidth, imageHeight, maxWidth, maxHeight);
    }
}
