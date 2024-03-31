// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.util.Iterator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.List;

public class SpriteDependencies
{
    public static TextureAtlasSprite resolveDependencies(final List<TextureAtlasSprite> p_resolveDependencies_0_, final int p_resolveDependencies_1_, final TextureMap p_resolveDependencies_2_) {
        TextureAtlasSprite textureatlassprite;
        for (textureatlassprite = p_resolveDependencies_0_.get(p_resolveDependencies_1_); resolveOne(p_resolveDependencies_0_, p_resolveDependencies_1_, textureatlassprite, p_resolveDependencies_2_); textureatlassprite = p_resolveDependencies_0_.get(p_resolveDependencies_1_)) {}
        textureatlassprite.isDependencyParent = false;
        return textureatlassprite;
    }
    
    private static boolean resolveOne(final List<TextureAtlasSprite> p_resolveOne_0_, final int p_resolveOne_1_, final TextureAtlasSprite p_resolveOne_2_, final TextureMap p_resolveOne_3_) {
        int i = 0;
        for (final ResourceLocation resourcelocation : p_resolveOne_2_.getDependencies()) {
            Config.dbg("Sprite dependency: " + p_resolveOne_2_.getIconName() + " <- " + resourcelocation);
            TextureAtlasSprite textureatlassprite = p_resolveOne_3_.getRegisteredSprite(resourcelocation);
            if (textureatlassprite == null) {
                textureatlassprite = p_resolveOne_3_.registerSprite(resourcelocation);
            }
            else {
                final int j = p_resolveOne_0_.indexOf(textureatlassprite);
                if (j <= p_resolveOne_1_ + i) {
                    continue;
                }
                if (textureatlassprite.isDependencyParent) {
                    final String s = "circular dependency: " + p_resolveOne_2_.getIconName() + " -> " + textureatlassprite.getIconName();
                    final ResourceLocation resourcelocation2 = p_resolveOne_3_.getResourceLocation(p_resolveOne_2_);
                    ReflectorForge.FMLClientHandler_trackBrokenTexture(resourcelocation2, s);
                    break;
                }
                p_resolveOne_0_.remove(j);
            }
            p_resolveOne_2_.isDependencyParent = true;
            p_resolveOne_0_.add(p_resolveOne_1_ + i, textureatlassprite);
            ++i;
        }
        return i > 0;
    }
}
