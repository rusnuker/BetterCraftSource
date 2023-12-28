// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import java.awt.image.BufferedImage;
import java.net.URL;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerImage;

public class ServerImageImpl implements ServerImage
{
    private URL pointingURL;
    private BufferedImage image;
    
    public ServerImageImpl(final URL pointingURL, final BufferedImage image) {
        this.pointingURL = pointingURL;
        this.image = image;
    }
    
    @Override
    public URL getPointingURL() {
        return this.pointingURL;
    }
    
    @Override
    public BufferedImage getImage() {
        return this.image;
    }
}
