// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import java.io.InputStream;
import net.minecraft.client.resources.IResource;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import java.util.Random;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.util.text.TextFormatting;
import com.google.common.collect.Lists;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class GuiWinGame extends GuiScreen
{
    private static final Logger LOGGER;
    private static final ResourceLocation MINECRAFT_LOGO;
    private static final ResourceLocation field_194401_g;
    private static final ResourceLocation VIGNETTE_TEXTURE;
    private final boolean field_193980_h;
    private final Runnable field_193981_i;
    private float time;
    private List<String> lines;
    private int totalScrollLength;
    private float scrollSpeed;
    
    static {
        LOGGER = LogManager.getLogger();
        MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
        field_194401_g = new ResourceLocation("textures/gui/title/edition.png");
        VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    }
    
    public GuiWinGame(final boolean p_i47590_1_, final Runnable p_i47590_2_) {
        this.scrollSpeed = 0.5f;
        this.field_193980_h = p_i47590_1_;
        this.field_193981_i = p_i47590_2_;
        if (!p_i47590_1_) {
            this.scrollSpeed = 0.75f;
        }
    }
    
    @Override
    public void updateScreen() {
        this.mc.getMusicTicker().update();
        this.mc.getSoundHandler().update();
        final float f = (this.totalScrollLength + GuiWinGame.height + GuiWinGame.height + 24) / this.scrollSpeed;
        if (this.time > f) {
            this.sendRespawnPacket();
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.sendRespawnPacket();
        }
    }
    
    private void sendRespawnPacket() {
        this.field_193981_i.run();
        this.mc.displayGuiScreen(null);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    @Override
    public void initGui() {
        if (this.lines == null) {
            this.lines = (List<String>)Lists.newArrayList();
            IResource iresource = null;
            try {
                final String s = new StringBuilder().append(TextFormatting.WHITE).append(TextFormatting.OBFUSCATED).append(TextFormatting.GREEN).append(TextFormatting.AQUA).toString();
                final int i = 274;
                if (this.field_193980_h) {
                    iresource = this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt"));
                    final InputStream inputstream = iresource.getInputStream();
                    final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                    final Random random = new Random(8124371L);
                    String s2;
                    while ((s2 = bufferedreader.readLine()) != null) {
                        String s3;
                        String s4;
                        for (s2 = s2.replaceAll("PLAYERNAME", Minecraft.getSession().getUsername()); s2.contains(s); s2 = String.valueOf(s3) + TextFormatting.WHITE + TextFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s4) {
                            final int j = s2.indexOf(s);
                            s3 = s2.substring(0, j);
                            s4 = s2.substring(j + s.length());
                        }
                        this.lines.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s2, 274));
                        this.lines.add("");
                    }
                    inputstream.close();
                    for (int k = 0; k < 8; ++k) {
                        this.lines.add("");
                    }
                }
                final InputStream inputstream2 = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
                final BufferedReader bufferedreader2 = new BufferedReader(new InputStreamReader(inputstream2, StandardCharsets.UTF_8));
                String s5;
                while ((s5 = bufferedreader2.readLine()) != null) {
                    s5 = s5.replaceAll("PLAYERNAME", Minecraft.getSession().getUsername());
                    s5 = s5.replaceAll("\t", "    ");
                    this.lines.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s5, 274));
                    this.lines.add("");
                }
                inputstream2.close();
                this.totalScrollLength = this.lines.size() * 12;
            }
            catch (final Exception exception) {
                GuiWinGame.LOGGER.error("Couldn't load credits", exception);
                return;
            }
            finally {
                IOUtils.closeQuietly(iresource);
            }
            IOUtils.closeQuietly(iresource);
        }
    }
    
    private void drawWinGameScreen(final int p_146575_1_, final int p_146575_2_, final float p_146575_3_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        final int i = GuiWinGame.width;
        final float f = -this.time * 0.5f * this.scrollSpeed;
        final float f2 = GuiWinGame.height - this.time * 0.5f * this.scrollSpeed;
        final float f3 = 0.015625f;
        float f4 = this.time * 0.02f;
        final float f5 = (this.totalScrollLength + GuiWinGame.height + GuiWinGame.height + 24) / this.scrollSpeed;
        final float f6 = (f5 - 20.0f - this.time) * 0.005f;
        if (f6 < f4) {
            f4 = f6;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        f4 *= f4;
        f4 = f4 * 96.0f / 255.0f;
        bufferbuilder.pos(0.0, GuiWinGame.height, GuiWinGame.zLevel).tex(0.0, f * 0.015625f).color(f4, f4, f4, 1.0f).endVertex();
        bufferbuilder.pos(i, GuiWinGame.height, GuiWinGame.zLevel).tex(i * 0.015625f, f * 0.015625f).color(f4, f4, f4, 1.0f).endVertex();
        bufferbuilder.pos(i, 0.0, GuiWinGame.zLevel).tex(i * 0.015625f, f2 * 0.015625f).color(f4, f4, f4, 1.0f).endVertex();
        bufferbuilder.pos(0.0, 0.0, GuiWinGame.zLevel).tex(0.0, f2 * 0.015625f).color(f4, f4, f4, 1.0f).endVertex();
        tessellator.draw();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawWinGameScreen(mouseX, mouseY, partialTicks);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        final int i = 274;
        final int j = GuiWinGame.width / 2 - 137;
        final int k = GuiWinGame.height + 50;
        this.time += partialTicks;
        final float f = -this.time * this.scrollSpeed;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, f, 0.0f);
        this.mc.getTextureManager().bindTexture(GuiWinGame.MINECRAFT_LOGO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        this.drawTexturedModalRect(j, k, 0, 0, 155, 44);
        this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        this.mc.getTextureManager().bindTexture(GuiWinGame.field_194401_g);
        Gui.drawModalRectWithCustomSizedTexture(j + 88, k + 37, 0.0f, 0.0f, 98, 14, 128.0f, 16.0f);
        GlStateManager.disableAlpha();
        int l = k + 100;
        for (int i2 = 0; i2 < this.lines.size(); ++i2) {
            if (i2 == this.lines.size() - 1) {
                final float f2 = l + f - (GuiWinGame.height / 2 - 6);
                if (f2 < 0.0f) {
                    GlStateManager.translate(0.0f, -f2, 0.0f);
                }
            }
            if (l + f + 12.0f + 8.0f > 0.0f && l + f < GuiWinGame.height) {
                final String s = this.lines.get(i2);
                if (s.startsWith("[C]")) {
                    this.fontRendererObj.drawStringWithShadow(s.substring(3), (float)(j + (274 - this.fontRendererObj.getStringWidth(s.substring(3))) / 2), (float)l, 16777215);
                }
                else {
                    this.fontRendererObj.fontRandom.setSeed((long)(i2 * 4238972211L + this.time / 4.0f));
                    this.fontRendererObj.drawStringWithShadow(s, (float)j, (float)l, 16777215);
                }
            }
            l += 12;
        }
        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(GuiWinGame.VIGNETTE_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        final int j2 = GuiWinGame.width;
        final int k2 = GuiWinGame.height;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0, k2, GuiWinGame.zLevel).tex(0.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(j2, k2, GuiWinGame.zLevel).tex(1.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(j2, 0.0, GuiWinGame.zLevel).tex(1.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(0.0, 0.0, GuiWinGame.zLevel).tex(0.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
