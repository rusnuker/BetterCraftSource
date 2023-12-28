// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;

public class CapeImageBuffer extends ImageBufferDownload
{
    private AbstractClientPlayer player;
    private ResourceLocation resourceLocation;
    
    public CapeImageBuffer(final AbstractClientPlayer p_i21_1_, final ResourceLocation p_i21_2_) {
        this.player = p_i21_1_;
        this.resourceLocation = p_i21_2_;
    }
    
    @Override
    public BufferedImage parseUserSkin(final BufferedImage image) {
        return CapeUtils.parseCape(image);
    }
    
    @Override
    public void skinAvailable() {
        if (this.player != null) {
            this.player.setLocationOfCape(this.resourceLocation);
        }
    }
    
    public void cleanup() {
        this.player = null;
    }
}
