// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.texture;

import net.minecraft.client.resources.IResource;
import java.util.Iterator;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class LayeredTexture extends AbstractTexture
{
    private static final Logger LOGGER;
    public final List<String> layeredTextureNames;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public LayeredTexture(final String... textureNames) {
        this.layeredTextureNames = Lists.newArrayList(textureNames);
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        BufferedImage bufferedimage = null;
        for (final String s : this.layeredTextureNames) {
            IResource iresource = null;
            try {
                if (s == null) {
                    continue;
                }
                iresource = resourceManager.getResource(new ResourceLocation(s));
                final BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(iresource.getInputStream());
                if (bufferedimage == null) {
                    bufferedimage = new BufferedImage(bufferedimage2.getWidth(), bufferedimage2.getHeight(), 2);
                }
                bufferedimage.getGraphics().drawImage(bufferedimage2, 0, 0, null);
                continue;
            }
            catch (final IOException ioexception) {
                LayeredTexture.LOGGER.error("Couldn't load layered image", ioexception);
            }
            finally {
                IOUtils.closeQuietly(iresource);
            }
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
    }
}
