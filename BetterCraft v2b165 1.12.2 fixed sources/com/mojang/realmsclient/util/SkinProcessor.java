// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.util;

import javax.annotation.Nullable;
import java.awt.Graphics;
import java.awt.image.DataBufferInt;
import java.awt.Color;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class SkinProcessor
{
    private int[] pixels;
    private int width;
    private int height;
    
    @Nullable
    public BufferedImage process(final BufferedImage image) {
        if (image == null) {
            return null;
        }
        this.width = 64;
        this.height = 64;
        final BufferedImage out = new BufferedImage(this.width, this.height, 2);
        final Graphics outGraphics = out.getGraphics();
        outGraphics.drawImage(image, 0, 0, null);
        final boolean isLegacy = image.getHeight() == 32;
        if (isLegacy) {
            outGraphics.setColor(new Color(0, 0, 0, 0));
            outGraphics.fillRect(0, 32, 64, 32);
            outGraphics.drawImage(out, 24, 48, 20, 52, 4, 16, 8, 20, null);
            outGraphics.drawImage(out, 28, 48, 24, 52, 8, 16, 12, 20, null);
            outGraphics.drawImage(out, 20, 52, 16, 64, 8, 20, 12, 32, null);
            outGraphics.drawImage(out, 24, 52, 20, 64, 4, 20, 8, 32, null);
            outGraphics.drawImage(out, 28, 52, 24, 64, 0, 20, 4, 32, null);
            outGraphics.drawImage(out, 32, 52, 28, 64, 12, 20, 16, 32, null);
            outGraphics.drawImage(out, 40, 48, 36, 52, 44, 16, 48, 20, null);
            outGraphics.drawImage(out, 44, 48, 40, 52, 48, 16, 52, 20, null);
            outGraphics.drawImage(out, 36, 52, 32, 64, 48, 20, 52, 32, null);
            outGraphics.drawImage(out, 40, 52, 36, 64, 44, 20, 48, 32, null);
            outGraphics.drawImage(out, 44, 52, 40, 64, 40, 20, 44, 32, null);
            outGraphics.drawImage(out, 48, 52, 44, 64, 52, 20, 56, 32, null);
        }
        outGraphics.dispose();
        this.pixels = ((DataBufferInt)out.getRaster().getDataBuffer()).getData();
        this.setNoAlpha(0, 0, 32, 16);
        if (isLegacy) {
            this.doNotchTransparencyHack(32, 0, 64, 32);
        }
        this.setNoAlpha(0, 16, 64, 32);
        this.setNoAlpha(16, 48, 48, 64);
        return out;
    }
    
    private void doNotchTransparencyHack(final int x0, final int y0, final int x1, final int y1) {
        for (int x2 = x0; x2 < x1; ++x2) {
            for (int y2 = y0; y2 < y1; ++y2) {
                final int pix = this.pixels[x2 + y2 * this.width];
                if ((pix >> 24 & 0xFF) < 128) {
                    return;
                }
            }
        }
        for (int x2 = x0; x2 < x1; ++x2) {
            for (int y2 = y0; y2 < y1; ++y2) {
                final int[] pixels = this.pixels;
                final int n = x2 + y2 * this.width;
                pixels[n] &= 0xFFFFFF;
            }
        }
    }
    
    private void setNoAlpha(final int x0, final int y0, final int x1, final int y1) {
        for (int x2 = x0; x2 < x1; ++x2) {
            for (int y2 = y0; y2 < y1; ++y2) {
                final int[] pixels = this.pixels;
                final int n = x2 + y2 * this.width;
                pixels[n] |= 0xFF000000;
            }
        }
    }
}
