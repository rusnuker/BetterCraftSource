// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.hologram.impl;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class PictureHologramLoader
{
    public int pictures;
    private int i;
    private String[] lines;
    private final Color[] colors;
    private static final char TRANSPARENT_CHAR = ' ';
    
    public PictureHologramLoader(final BufferedImage image, final int height, final char imgChar) {
        this.i = 0;
        this.colors = new Color[] { new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 255, 85), new Color(255, 255, 255) };
        this.pictures = height - 1;
        final PictureHologramColor[][] chatColors = this.toChatColorArray(image, height);
        this.lines = this.toImgMessage(chatColors, imgChar);
    }
    
    public PictureHologramLoader(final PictureHologramColor[][] chatColors, final char imgChar) {
        this.i = 0;
        this.colors = new Color[] { new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 255, 85), new Color(255, 255, 255) };
        this.lines = this.toImgMessage(chatColors, imgChar);
    }
    
    private PictureHologramColor[][] toChatColorArray(final BufferedImage image, final int height) {
        final double ratio = image.getHeight() / image.getWidth();
        int width = (int)(height / ratio);
        if (width > 10) {
            width = 10;
        }
        final BufferedImage resized = this.resizeImage(image, (int)(height / ratio), height);
        final PictureHologramColor[][] chatImg = new PictureHologramColor[resized.getWidth()][resized.getHeight()];
        for (int x = 0; x < resized.getWidth(); ++x) {
            for (int y = 0; y < resized.getHeight(); ++y) {
                final int rgb = resized.getRGB(x, y);
                final PictureHologramColor closest = chatImg[x][y] = this.getClosestChatColor(new Color(rgb, true));
            }
        }
        return chatImg;
    }
    
    private String[] toImgMessage(final PictureHologramColor[][] colors, final char imgchar) {
        final String[] lines2 = new String[colors[0].length];
        for (int y = 0; y < colors[0].length; ++y) {
            String line = "";
            for (int x = 0; x < colors.length; ++x) {
                final PictureHologramColor color;
                line = String.valueOf(String.valueOf(line)) + (((color = colors[x][y]) != null) ? (String.valueOf(String.valueOf(colors[x][y].toString())) + imgchar) : Character.valueOf(' '));
            }
            lines2[y] = String.valueOf(String.valueOf(line)) + PictureHologramColor.RESET;
        }
        return lines2;
    }
    
    private BufferedImage resizeImage(final BufferedImage src, final int width, final int height) {
        int finalw = width;
        int finalh = height;
        double factor = 1.0;
        if (src.getWidth() > src.getHeight()) {
            factor = src.getHeight() / (double)src.getWidth();
            finalh = (int)(finalw * factor);
        }
        else {
            factor = src.getWidth() / (double)src.getHeight();
            finalw = (int)(finalh * factor);
        }
        final BufferedImage resizedImg = new BufferedImage(finalw, finalh, 3);
        final Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, finalw, finalh, null);
        g2.dispose();
        return resizedImg;
    }
    
    private double getDistance(final Color c1, final Color c2) {
        final double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        final double r = c1.getRed() - c2.getRed();
        final double g = c1.getGreen() - c2.getGreen();
        final int b = c1.getBlue() - c2.getBlue();
        final double weightR = 2.0 + rmean / 256.0;
        final double weightG = 4.0;
        final double weightB = 2.0 + (255.0 - rmean) / 256.0;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }
    
    private boolean areIdentical(final Color c1, final Color c2) {
        return Math.abs(c1.getRed() - c2.getRed()) <= 5 && Math.abs(c1.getGreen() - c2.getGreen()) <= 5 && Math.abs(c1.getBlue() - c2.getBlue()) <= 5;
    }
    
    private PictureHologramColor getClosestChatColor(final Color color) {
        if (color.getAlpha() < 128) {
            return null;
        }
        int index2 = 0;
        double best = -1.0;
        for (int i = 0; i < this.colors.length; ++i) {
            if (this.areIdentical(this.colors[i], color)) {
                return PictureHologramColor.values()[i];
            }
        }
        for (int i = 0; i < this.colors.length; ++i) {
            final double distance = this.getDistance(color, this.colors[i]);
            if (distance < best || best == -1.0) {
                best = distance;
                index2 = i;
            }
        }
        return PictureHologramColor.values()[index2];
    }
    
    private String center(final String s2, final int length) {
        if (s2.length() > length) {
            return s2.substring(0, length);
        }
        if (s2.length() == length) {
            return s2;
        }
        final int leftPadding = (length - s2.length()) / 2;
        final StringBuilder leftBuilder = new StringBuilder();
        for (int i = 0; i < leftPadding; ++i) {
            leftBuilder.append(" ");
        }
        return String.valueOf(String.valueOf(leftBuilder.toString())) + s2;
    }
    
    public String[] getLines() {
        return this.lines;
    }
    
    public ItemStack[] getArmorStands(final double x, final double y, final double z) {
        final ItemStack[] itemStacks = new ItemStack[this.getLines().length];
        final List<String> lines2 = Arrays.asList(this.getLines());
        Collections.reverse(lines2);
        for (int i = 0; i < this.getLines().length; ++i) {
            final ItemStack item = new ItemStack(Items.ARMOR_STAND);
            final NBTTagCompound base = new NBTTagCompound();
            final NBTTagCompound entityTag = new NBTTagCompound();
            final NBTTagList pos = new NBTTagList();
            pos.appendTag(new NBTTagDouble(x));
            pos.appendTag(new NBTTagDouble(y + 0.2 * i));
            pos.appendTag(new NBTTagDouble(z));
            entityTag.setTag("Pos", pos);
            entityTag.setString("CustomName", this.getLines()[i]);
            entityTag.setInteger("CustomNameVisible", 1);
            entityTag.setInteger("Invisible", 1);
            entityTag.setInteger("NoGravity", 1);
            base.setTag("EntityTag", entityTag);
            item.setTagCompound(base);
            item.setStackDisplayName("§4Hologram: #" + i);
            itemStacks[i] = item;
        }
        return itemStacks;
    }
}
