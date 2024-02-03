// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.background;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.nzxtercode.bettercraft.client.Config;
import java.util.Objects;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import java.awt.Color;
import java.util.function.Consumer;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.util.ResourceLocation;
import javax.vecmath.Vector2f;
import net.minecraft.client.gui.GuiScreen;

public class GuiColorPicker extends GuiScreen
{
    private int glTexture;
    private Vector2f position;
    private final ResourceLocation location;
    private final AtomicReference<BufferedImage> image;
    private final Consumer<Color> consumer;
    private GuiScreen parent;
    private GuiTextField hexTextField;
    private boolean buttonEnabled;
    
    public GuiColorPicker(final GuiScreen parent, final Consumer<Color> consumer) {
        this.location = new ResourceLocation("client/gui/colorgradient.png");
        this.image = new AtomicReference<BufferedImage>();
        this.consumer = consumer;
        this.parent = parent;
        try {
            this.image.set(TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(this.location).getInputStream()));
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, GuiColorPicker.width - 107, GuiColorPicker.height - 27, 100, 20, "Back"));
        this.buttonList.add(new GuiButton(1, GuiColorPicker.width / 2 + 45, GuiColorPicker.height / 2 + 10, 50, 10, "Background"));
        this.buttonList.add(new GuiButton(2, GuiColorPicker.width / 2 - 20, GuiColorPicker.height / 2 + 10, 50, 10, "Particel"));
        this.buttonList.add(new GuiButton(3, GuiColorPicker.width / 2 - 90, GuiColorPicker.height / 2 + 10, 50, 10, "Button-String"));
        (this.hexTextField = new GuiTextField(0, this.fontRendererObj, GuiColorPicker.width / 2 + 30 + 1, GuiColorPicker.height / 2 - 5, 59, this.mc.fontRendererObj.FONT_HEIGHT)).setEnableBackgroundDrawing(false);
        this.hexTextField.setMaxStringLength(6);
        this.image.set(this.resize(this.image.get(), 100, 100));
        if (Objects.nonNull(this.position)) {
            this.hexTextField.setText(Integer.toHexString(this.getColor((int)this.position.getX(), (int)this.position.getY()).getRGB()).substring(2));
        }
        super.initGui();
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parent);
        }
        if (Objects.nonNull(this.position)) {
            switch (button.id) {
                case 1: {
                    Config.getInstance().editBackground("Background", json -> json.add("color", new JsonPrimitive(this.getColor((int)this.position.getX(), (int)this.position.getY()).getRGB())));
                    break;
                }
                case 2: {
                    Config.getInstance().editColor("Particel", json -> json.add("color", new JsonPrimitive(this.getColor((int)this.position.getX(), (int)this.position.getY()).getRGB())));
                    break;
                }
                case 3: {
                    Config.getInstance().editColor("Buttons", json -> json.add("string", new JsonPrimitive(this.getColor((int)this.position.getX(), (int)this.position.getY()).getRGB())));
                    break;
                }
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        try {
            this.drawDefaultBackground();
            Gui.drawRect(GuiColorPicker.width / 2 - 100, GuiColorPicker.height / 2 - 100, GuiColorPicker.width / 2 + 100, GuiColorPicker.height / 2 + 30, Integer.MIN_VALUE);
            this.drawTextureAt(GuiColorPicker.width / 2 - 100, GuiColorPicker.height / 2 - 100, 100, 100);
            if (Objects.nonNull(this.position)) {
                final Color color = this.getColor((int)this.position.getX(), (int)this.position.getY());
                this.mc.fontRendererObj.drawString("RGB", GuiColorPicker.width / 2 + 10, GuiColorPicker.height / 2 - 95, -1);
                this.mc.fontRendererObj.drawString("R:", GuiColorPicker.width / 2 + 10, GuiColorPicker.height / 2 - 85, -1);
                Gui.drawRect(GuiColorPicker.width / 2 + 30, GuiColorPicker.height / 2 - 85, GuiColorPicker.width / 2 + 90, GuiColorPicker.height / 2 - 85 + this.mc.fontRendererObj.FONT_HEIGHT - 1, new Color(color.getRed(), 1, 1).getRGB());
                this.mc.fontRendererObj.drawString("G:", GuiColorPicker.width / 2 + 10, GuiColorPicker.height / 2 - 75, -1);
                Gui.drawRect(GuiColorPicker.width / 2 + 30, GuiColorPicker.height / 2 - 75, GuiColorPicker.width / 2 + 90, GuiColorPicker.height / 2 - 75 + this.mc.fontRendererObj.FONT_HEIGHT - 1, new Color(1, color.getGreen(), 1).getRGB());
                this.mc.fontRendererObj.drawString("B:", GuiColorPicker.width / 2 + 10, GuiColorPicker.height / 2 - 65, -1);
                Gui.drawRect(GuiColorPicker.width / 2 + 30, GuiColorPicker.height / 2 - 65, GuiColorPicker.width / 2 + 90, GuiColorPicker.height / 2 - 65 + this.mc.fontRendererObj.FONT_HEIGHT - 1, new Color(1, 1, color.getBlue()).getRGB());
                final float[] hsb = new float[3];
                Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
                this.mc.fontRendererObj.drawString("HSB", GuiColorPicker.width / 2 + 10, GuiColorPicker.height / 2 - 50, -1);
                this.mc.fontRendererObj.drawString("H:", GuiColorPicker.width / 2 + 10, GuiColorPicker.height / 2 - 40, -1);
                Gui.drawRect(GuiColorPicker.width / 2 + 30, GuiColorPicker.height / 2 - 40, GuiColorPicker.width / 2 + 90, GuiColorPicker.height / 2 - 40 + this.mc.fontRendererObj.FONT_HEIGHT - 1, color.getRGB());
                final int lol2 = Math.min((int)(-((hsb[2] * 10000.0f - 9999.0f) / 500.0f) * 255.0f), 255);
                this.mc.fontRendererObj.drawString("S:", GuiColorPicker.width / 2 + 10, GuiColorPicker.height / 2 - 30, -1);
                Gui.drawRect(GuiColorPicker.width / 2 + 30, GuiColorPicker.height / 2 - 30, GuiColorPicker.width / 2 + 90, GuiColorPicker.height / 2 - 30 + this.mc.fontRendererObj.FONT_HEIGHT - 1, new Color(lol2, lol2, lol2).getRGB());
                final int lol3 = 255 - Math.min((int)(-((hsb[2] * 10000.0f - 9999.0f) / 500.0f) * 255.0f), 255);
                this.mc.fontRendererObj.drawString("B:", GuiColorPicker.width / 2 + 10, GuiColorPicker.height / 2 - 20, -1);
                Gui.drawRect(GuiColorPicker.width / 2 + 30, GuiColorPicker.height / 2 - 20, GuiColorPicker.width / 2 + 90, GuiColorPicker.height / 2 - 20 + this.mc.fontRendererObj.FONT_HEIGHT - 1, new Color(lol3, lol3, lol3).getRGB());
                this.mc.fontRendererObj.drawString("Hex:", GuiColorPicker.width / 2 + 10, GuiColorPicker.height / 2 - 5, -1);
                Gui.drawRect(GuiColorPicker.width / 2 + 30, GuiColorPicker.height / 2 - 5, GuiColorPicker.width / 2 + 90, GuiColorPicker.height / 2 - 5 + this.mc.fontRendererObj.FONT_HEIGHT - 1, color.getRGB());
                this.hexTextField.drawTextBox();
                this.drawBorder(GuiColorPicker.width / 2 - 100 + (int)this.position.x - 2, GuiColorPicker.height / 2 - 100 + (int)this.position.y - 2, GuiColorPicker.width / 2 - 100 + (int)this.position.x + 2, GuiColorPicker.height / 2 - 100 + (int)this.position.y + 2, Color.WHITE.getRGB());
            }
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.update(mouseX, mouseY);
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.update(mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void update(final int mouseX, final int mouseY) {
        if (mouseX >= GuiColorPicker.width / 2 - 100 && mouseX <= GuiColorPicker.width / 2 - 100 + 100 && mouseY >= GuiColorPicker.height / 2 - 100 && mouseY <= GuiColorPicker.height / 2 - 100 + 100) {
            try {
                final Color color = this.getColor(Math.max(Math.min(mouseX - (GuiColorPicker.width / 2 - 100), 99), 1), Math.max(Math.min(mouseY - (GuiColorPicker.height / 2 - 100), 99), 0));
                if (color.getRed() != 0 && color.getGreen() != 0 && color.getBlue() != 0) {
                    this.position = new Vector2f((float)Math.max(Math.min(mouseX - (GuiColorPicker.width / 2 - 100), 99), 1), (float)Math.max(Math.min(mouseY - (GuiColorPicker.height / 2 - 100), 99), 0));
                    this.hexTextField.setText(Integer.toHexString(color.getRGB()).substring(2));
                }
            }
            catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    
    public Color getColor(final int x, final int y) {
        return new Color(this.image.get().getRGB(x, y));
    }
    
    public BufferedImage resize(final BufferedImage img, final int newW, final int newH) {
        final Image tmp = img.getScaledInstance(newW, newH, 4);
        final BufferedImage dimg = new BufferedImage(newW, newH, 2);
        final Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }
    
    public void drawTextureAt(final int x, final int y, final int width, final int height) throws Exception {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.location);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GlStateManager.popMatrix();
    }
    
    public void drawBorder(final int left, final int top, final int right, final int bottom, final int border) {
        Gui.drawRect(left, top + 1, right, top, border);
        Gui.drawRect(right - 1, top, right, bottom, border);
        Gui.drawRect(left, bottom, right, bottom - 1, border);
        Gui.drawRect(left, top, left + 1, bottom, border);
    }
}
