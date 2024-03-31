// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc;

import net.minecraft.client.gui.GuiButton;
import java.util.concurrent.CompletableFuture;
import me.amkgre.bettercraft.client.mods.discord.rpc.slot.MouseClickType;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.util.List;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.image.BufferedImage;
import me.amkgre.bettercraft.client.mods.discord.rpc.slot.Slot;
import java.util.HashMap;
import net.minecraft.client.gui.GuiScreen;
import me.amkgre.bettercraft.client.mods.discord.rpc.slot.SlotRenderer;

public class GuiDiscordRPC extends SlotRenderer<DiscordRP>
{
    private final GuiScreen before;
    private int avatarIconID;
    private static HashMap<Slot<DiscordRP>, BufferedImage> bigImageTextures;
    private static HashMap<Slot<DiscordRP>, BufferedImage> smallImageTextures;
    private static HashMap<BufferedImage, DynamicTexture> dynamicTextures;
    
    static {
        GuiDiscordRPC.bigImageTextures = new HashMap<Slot<DiscordRP>, BufferedImage>();
        GuiDiscordRPC.smallImageTextures = new HashMap<Slot<DiscordRP>, BufferedImage>();
        GuiDiscordRPC.dynamicTextures = new HashMap<BufferedImage, DynamicTexture>();
    }
    
    public GuiDiscordRPC(final GuiScreen screen) throws IOException {
        this.avatarIconID = 0;
        this.before = screen;
        try {
            final String uri = "https://cdn.discordapp.com/avatars/" + DiscordMain.getInstance().getDiscordRP().getDiscordRPUser().getUserId() + "/" + DiscordMain.getInstance().getDiscordRP().getDiscordRPUser().getAvatar() + ".png?size=128";
            final URL url = new URL(uri);
            final HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            final DynamicTexture dynamicTex = new DynamicTexture(ImageIO.read(connection.getInputStream()));
            this.avatarIconID = dynamicTex.getGlTextureId();
        }
        catch (final Exception ex) {}
    }
    
    @Override
    public List<DiscordRP> getList() {
        return DiscordMain.getInstance().getDiscordRP().getObjects();
    }
    
    @Override
    public int getXPosition() {
        return 20;
    }
    
    @Override
    public int getYPosition() {
        return 20;
    }
    
    @Override
    public int getSlotWidth() {
        return 200;
    }
    
    @Override
    public int getSlotHeight() {
        return 12;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int rpcXPos = this.getXPosition() + this.getSlotWidth() + 155 + 20 + ((GuiDiscordRPC.width - 427 == 0) ? 0 : ((GuiDiscordRPC.width - 427) / 2));
        final Color blue = new Color(-9270822);
        final Color dark_blue = new Color(-9665841);
        this.drawDefaultBackground();
        if (this.getSelectedSlot() != null) {
            Gui.drawRect(rpcXPos - 155, GuiDiscordRPC.height / 2 - 100, rpcXPos - 5, GuiDiscordRPC.height / 2 + 50, blue.getRGB());
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            try {
                final int middle = rpcXPos - (rpcXPos - 155);
                final int size = 20;
                final int pictureX = rpcXPos - middle / 2 - (size + 5);
                final int pictureY = GuiDiscordRPC.height / 2 - 90;
                final int pictureWidth = rpcXPos - middle / 2 - pictureX + size;
                final int pictureHeight = GuiDiscordRPC.height / 2 - (size * 2 + 5) - pictureY;
                GlStateManager.bindTexture(this.avatarIconID);
                Gui.drawModalRectWithCustomSizedTexture(pictureX, pictureY, 0.0f, 0.0f, pictureWidth, pictureHeight, (float)pictureWidth, (float)pictureHeight);
                GlStateManager.color(blue.getRed() / 255.0f, blue.getGreen() / 255.0f, blue.getBlue() / 255.0f);
                RenderUtils.drawTextureAt(pictureX, pictureY, new ResourceLocation("textures/gui/circle.png"), size * 2 + 5);
                try {
                    final String headline = "§l" + DiscordMain.getInstance().getDiscordRP().getDiscordRPUser().getUsername() + "§r#" + DiscordMain.getInstance().getDiscordRP().getDiscordRPUser().getDiscriminator();
                    Gui.drawCenteredString(this.mc.fontRendererObj, headline, rpcXPos - middle / 2, pictureY + size * 2 + 10, -1);
                }
                catch (final Exception ex) {}
                Gui.drawRect(rpcXPos - 155, pictureY + size * 2 + 30, rpcXPos - 5, GuiDiscordRPC.height / 2 + 50, dark_blue.getRGB());
                final int x = rpcXPos - 155 + 10;
                final int y = GuiDiscordRPC.height / 2 + 50;
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                if (GuiDiscordRPC.bigImageTextures.containsKey(this.getSelectedSlot())) {
                    DynamicTexture dynamicTex;
                    if (GuiDiscordRPC.dynamicTextures.containsKey(GuiDiscordRPC.bigImageTextures.get(this.getSelectedSlot()))) {
                        dynamicTex = GuiDiscordRPC.dynamicTextures.get(GuiDiscordRPC.bigImageTextures.get(this.getSelectedSlot()));
                    }
                    else {
                        dynamicTex = new DynamicTexture(GuiDiscordRPC.bigImageTextures.get(this.getSelectedSlot()));
                        GuiDiscordRPC.dynamicTextures.put(GuiDiscordRPC.bigImageTextures.get(this.getSelectedSlot()), dynamicTex);
                    }
                    GlStateManager.bindTexture(dynamicTex.getGlTextureId());
                    Gui.drawModalRectWithCustomSizedTexture(x, y - 10, 0.0f, 0.0f, size * 2, -size * 2, (float)(size * 2), (float)(size * 2));
                }
                GlStateManager.color(dark_blue.getRed() / 255.0f, dark_blue.getGreen() / 255.0f, dark_blue.getBlue() / 255.0f);
                RenderUtils.drawTextureAt(x, y - 10, new ResourceLocation("textures/gui/roundet.png"), size * 2, -size * 2);
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                if (GuiDiscordRPC.smallImageTextures.containsKey(this.getSelectedSlot())) {
                    DynamicTexture dynamicTex;
                    if (GuiDiscordRPC.dynamicTextures.containsKey(GuiDiscordRPC.smallImageTextures.get(this.getSelectedSlot()))) {
                        dynamicTex = GuiDiscordRPC.dynamicTextures.get(GuiDiscordRPC.smallImageTextures.get(this.getSelectedSlot()));
                    }
                    else {
                        dynamicTex = new DynamicTexture(GuiDiscordRPC.smallImageTextures.get(this.getSelectedSlot()));
                        GuiDiscordRPC.dynamicTextures.put(GuiDiscordRPC.smallImageTextures.get(this.getSelectedSlot()), dynamicTex);
                    }
                    GlStateManager.bindTexture(dynamicTex.getGlTextureId());
                    Gui.drawModalRectWithCustomSizedTexture(x + size * 2 - 8, y - 10 + 2, 0.0f, 0.0f, size / 2, -size / 2, (float)(size / 2), (float)(size / 2));
                    GlStateManager.color(dark_blue.getRed() / 255.0f, dark_blue.getGreen() / 255.0f, dark_blue.getBlue() / 255.0f);
                    RenderUtils.drawTextureAt(x + size * 2 - 8, y - 10 + 2, new ResourceLocation("textures/gui/roundet.png"), size / 2, -size / 2);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                GlStateManager.pushMatrix();
                final double scale = 0.699999988079071;
                GlStateManager.scale(scale, scale, scale);
                this.mc.fontRendererObj.drawString("§l" + this.getSelectedSlot().getObject().getType().getName(), (int)((x + size * 2 + 5) / scale), (int)((y - 10 + -size * 2 + size - this.mc.fontRendererObj.FONT_HEIGHT / 2 - (this.getSelectedSlot().getObject().getLeaveServerContent().getFirstLine().isEmpty() ? 0 : 5)) / scale), -1);
                GlStateManager.popMatrix();
                this.mc.fontRendererObj.drawString(this.getSelectedSlot().getObject().getLeaveServerContent().getFirstLine(), x + size * 2 + 5, y - 10 + -size * 2 + size - this.mc.fontRendererObj.FONT_HEIGHT / 2 + 5, -1);
                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, scale);
                this.mc.fontRendererObj.drawString("§lSPIELT EIN SPIEL", (int)(x / scale), (int)((y + -size * 2 - 22) / scale), -1);
                GlStateManager.popMatrix();
            }
            catch (final Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void onClickSlot(final Slot<DiscordRP> slot, final MouseClickType clickType) {
        if (!GuiDiscordRPC.bigImageTextures.containsKey(slot)) {
            CompletableFuture.runAsync(() -> {
                try {
                    if (!slot2.getObject().getType().getBigImageAssetsKey().isEmpty()) {
                        try {
                            final String uri = "https://cdn.discordapp.com/app-assets/" + slot2.getObject().getApplicationID() + "/" + slot2.getObject().getType().getBigImageAssetsKey() + ".png";
                            final URL url = new URL(uri);
                            final HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                            GuiDiscordRPC.bigImageTextures.put(slot2, ImageIO.read(connection.getInputStream()));
                        }
                        catch (final Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
                catch (final Throwable t) {}
                return;
            });
        }
        if (!GuiDiscordRPC.smallImageTextures.containsKey(slot)) {
            CompletableFuture.runAsync(() -> {
                try {
                    if (!slot3.getObject().getType().getSmallImageAssetsKey().isEmpty()) {
                        try {
                            final String uri2 = "https://cdn.discordapp.com/app-assets/" + slot3.getObject().getApplicationID() + "/" + slot3.getObject().getType().getSmallImageAssetsKey() + ".png";
                            final URL url2 = new URL(uri2);
                            final HttpsURLConnection connection2 = (HttpsURLConnection)url2.openConnection();
                            connection2.setRequestProperty("User-Agent", "Mozilla/5.0");
                            GuiDiscordRPC.smallImageTextures.put(slot3, ImageIO.read(connection2.getInputStream()));
                        }
                        catch (final Throwable throwable2) {
                            throwable2.printStackTrace();
                        }
                    }
                }
                catch (final Throwable t2) {}
                return;
            });
        }
        super.onClickSlot(slot, clickType);
    }
    
    @Override
    public void onDoubleClickSlot(final Slot<DiscordRP> slot) {
        CompletableFuture.runAsync(() -> {
            DiscordMain.getInstance().getDiscordRP().shutdownRPC();
            DiscordMain.getInstance().getDiscordRP().startRPC(slot2.getObject().getType());
            return;
        });
        super.onDoubleClickSlot(slot);
    }
    
    @Override
    public void drawSlot(final int xPosition, final int yPosition, final Slot<DiscordRP> slot) {
        super.drawSlot(xPosition, yPosition, slot);
        Gui.drawCenteredString(this.mc.fontRendererObj, String.valueOf((DiscordMain.getInstance().getDiscordRP().getRunningRP() == slot.getObject()) ? "§2" : "§7") + slot.getObject().getType().getName(), xPosition + (xPosition + this.getSlotWidth()) / 2 - 10, yPosition + this.getSlotHeight() / 2 - 4, -1);
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(1, GuiDiscordRPC.width - 70, GuiDiscordRPC.height - 30, 60, 20, "Back"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.before);
        }
    }
}
