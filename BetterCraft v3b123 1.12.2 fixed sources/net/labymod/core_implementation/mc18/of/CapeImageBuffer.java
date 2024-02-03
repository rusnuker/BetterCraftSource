// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.of;

import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.lang.ref.WeakReference;
import net.minecraft.client.renderer.ImageBufferDownload;

public class CapeImageBuffer extends ImageBufferDownload
{
    private WeakReference<AbstractClientPlayer> playerReference;
    private ResourceLocation resourceLocation;
    public ImageBufferDownload imageBufferDownload;
    
    public CapeImageBuffer(final AbstractClientPlayer player, final ResourceLocation resourceLocation) {
        this.playerReference = new WeakReference<AbstractClientPlayer>(player);
        this.resourceLocation = resourceLocation;
        this.imageBufferDownload = new ImageBufferDownload();
    }
    
    @Override
    public BufferedImage parseUserSkin(final BufferedImage var1) {
        return parseCape(var1);
    }
    
    @Override
    public void skinAvailable() {
        final AbstractClientPlayer player = (this.playerReference == null) ? null : this.playerReference.get();
        if (player != null) {
            setLocationOfCape(player, this.resourceLocation);
        }
    }
    
    public void cleanup() {
        this.playerReference = null;
    }
    
    private static void setLocationOfCape(final AbstractClientPlayer player2, final ResourceLocation resourceLocation2) {
    }
    
    private static BufferedImage parseCape(final BufferedImage var1) {
        return null;
    }
}
