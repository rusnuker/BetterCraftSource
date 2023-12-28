// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import wdl.WDLPluginChannels;
import wdl.WDL;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLChunkOverrides extends GuiScreen
{
    private static final int TOP_MARGIN = 61;
    private static final int BOTTOM_MARGIN = 32;
    private static final ResourceLocation WIDGET_TEXTURES;
    private final GuiScreen parent;
    private GuiButton startDownloadButton;
    private float scrollX;
    private float scrollZ;
    private static final int SCALE = 8;
    private Mode mode;
    private boolean partiallyRequested;
    private int requestStartX;
    private int requestStartZ;
    private int requestEndX;
    private int requestEndZ;
    private boolean dragging;
    private int lastTickX;
    private int lastTickY;
    private static final int RNG_SEED = 769532;
    
    static {
        WIDGET_TEXTURES = new ResourceLocation("wdl:textures/permission_widgets.png");
    }
    
    public GuiWDLChunkOverrides(final GuiScreen parent) {
        this.mode = Mode.PANNING;
        this.parent = parent;
        if (WDL.thePlayer != null) {
            this.scrollX = (float)WDL.thePlayer.chunkCoordX;
            this.scrollZ = (float)WDL.thePlayer.chunkCoordZ;
        }
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new RequestModeButton(0, GuiWDLChunkOverrides.width / 2 - 155, 18, Mode.PANNING));
        this.buttonList.add(new RequestModeButton(1, GuiWDLChunkOverrides.width / 2 - 130, 18, Mode.REQUESTING));
        this.buttonList.add(new RequestModeButton(this, 2, GuiWDLChunkOverrides.width / 2 - 105, 18, Mode.ERASING) {
            {
                this.enabled = false;
            }
        });
        this.buttonList.add(new RequestModeButton(this, 3, GuiWDLChunkOverrides.width / 2 - 80, 18, Mode.MOVING) {
            {
                this.enabled = false;
            }
        });
        this.buttonList.add(new GuiButton(4, GuiWDLChunkOverrides.width / 2 - 80, 18, 80, 20, "Send request"));
        this.startDownloadButton = new GuiButton(6, GuiWDLChunkOverrides.width / 2 + 5, 18, 150, 20, "Start download in these ranges");
        this.startDownloadButton.enabled = WDLPluginChannels.canDownloadAtAll();
        this.buttonList.add(this.startDownloadButton);
        this.buttonList.add(new GuiButton(100, GuiWDLChunkOverrides.width / 2 - 100, GuiWDLChunkOverrides.height - 29, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(200, GuiWDLChunkOverrides.width / 2 - 155, 39, 100, 20, I18n.format("wdl.gui.permissions.current", new Object[0])));
        this.buttonList.add(new GuiButton(201, GuiWDLChunkOverrides.width / 2 - 50, 39, 100, 20, I18n.format("wdl.gui.permissions.request", new Object[0])));
        this.buttonList.add(new GuiButton(202, GuiWDLChunkOverrides.width / 2 + 55, 39, 100, 20, I18n.format("wdl.gui.permissions.overrides", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mode = Mode.PANNING;
        }
        if (button.id == 1) {
            this.mode = Mode.REQUESTING;
            this.partiallyRequested = false;
        }
        if (button.id == 4) {
            WDLPluginChannels.sendRequests();
        }
        if (button.id == 6) {
            if (!WDLPluginChannels.canDownloadAtAll()) {
                button.enabled = false;
                return;
            }
            WDL.startDownload();
        }
        if (button.id == 100) {
            this.mc.displayGuiScreen(this.parent);
        }
        if (button.id == 200) {
            this.mc.displayGuiScreen(new GuiWDLPermissions(this.parent));
        }
        if (button.id == 201) {
            this.mc.displayGuiScreen(new GuiWDLPermissionRequest(this.parent));
        }
        final int id = button.id;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseY > 61 && mouseY < GuiWDLChunkOverrides.height - 32 && mouseButton == 0) {
            switch (this.mode) {
                case PANNING: {
                    this.dragging = true;
                    this.lastTickX = mouseX;
                    this.lastTickY = mouseY;
                    break;
                }
                case REQUESTING: {
                    if (this.partiallyRequested) {
                        this.requestEndX = this.displayXToChunkX(mouseX);
                        this.requestEndZ = this.displayZToChunkZ(mouseY);
                        final WDLPluginChannels.ChunkRange requestRange = new WDLPluginChannels.ChunkRange("", this.requestStartX, this.requestStartZ, this.requestEndX, this.requestEndZ);
                        WDLPluginChannels.addChunkOverrideRequest(requestRange);
                        this.partiallyRequested = false;
                    }
                    else {
                        this.requestStartX = this.displayXToChunkX(mouseX);
                        this.requestStartZ = this.displayZToChunkZ(mouseY);
                        this.partiallyRequested = true;
                    }
                    this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                    break;
                }
            }
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            this.dragging = false;
        }
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        if (this.dragging) {
            final int deltaX = this.lastTickX - mouseX;
            final int deltaY = this.lastTickY - mouseY;
            this.lastTickX = mouseX;
            this.lastTickY = mouseY;
            this.scrollX += deltaX / 8.0f;
            this.scrollZ += deltaY / 8.0f;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Utils.drawDarkBackground(0, 0, GuiWDLChunkOverrides.height, GuiWDLChunkOverrides.width);
        if (this.mode == Mode.REQUESTING) {
            final int x1 = this.partiallyRequested ? this.requestStartX : this.displayXToChunkX(mouseX);
            final int z1 = this.partiallyRequested ? this.requestStartZ : this.displayZToChunkZ(mouseY);
            final int x2 = this.displayXToChunkX(mouseX);
            final int z2 = this.displayZToChunkZ(mouseY);
            final WDLPluginChannels.ChunkRange requestRange = new WDLPluginChannels.ChunkRange("", x1, z1, x2, z2);
            final int alpha = 127 + (int)(Math.sin(Minecraft.getSystemTime() * 3.141592653589793 / 5000.0) * 64.0);
            this.drawRange(requestRange, 16777215, alpha);
        }
        for (final Multimap<String, WDLPluginChannels.ChunkRange> group : WDLPluginChannels.getChunkOverrides().values()) {
            for (final WDLPluginChannels.ChunkRange range : group.values()) {
                this.drawRange(range, 769532, 255);
            }
        }
        for (final WDLPluginChannels.ChunkRange range2 : WDLPluginChannels.getChunkOverrideRequests()) {
            final int alpha2 = 127 + (int)(Math.sin(Minecraft.getSystemTime() * 3.141592653589793 / 5000.0) * 64.0);
            this.drawRange(range2, 8421504, alpha2);
        }
        final int playerPosX = (int)((WDL.thePlayer.posX / 16.0 - this.scrollX) * 8.0 + GuiWDLChunkOverrides.width / 2);
        final int playerPosZ = (int)((WDL.thePlayer.posZ / 16.0 - this.scrollZ) * 8.0 + GuiWDLChunkOverrides.height / 2);
        this.drawHorizontalLine(playerPosX - 3, playerPosX + 3, playerPosZ, -1);
        this.drawVerticalLine(playerPosX, playerPosZ - 4, playerPosZ + 4, -1);
        Utils.drawBorder(61, 32, 0, 0, GuiWDLChunkOverrides.height, GuiWDLChunkOverrides.width);
        Gui.drawCenteredString(this.fontRendererObj, "Chunk overrides", GuiWDLChunkOverrides.width / 2, 8, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, "§c§lThis is a work in progress.", GuiWDLChunkOverrides.width / 2, GuiWDLChunkOverrides.height / 2, 16777215);
    }
    
    private void drawRange(final WDLPluginChannels.ChunkRange range, final int seed, final int alpha) {
        final int color = (range.tag.hashCode() ^ seed) & 0xFFFFFF;
        final int x1 = this.chunkXToDisplayX(range.x1);
        final int z1 = this.chunkZToDisplayZ(range.z1);
        final int x2 = this.chunkXToDisplayX(range.x2) + 8 - 1;
        final int z2 = this.chunkZToDisplayZ(range.z2) + 8 - 1;
        Gui.drawRect(x1, z1, x2, z2, color + (alpha << 24));
        final int colorDark = this.darken(color);
        this.drawVerticalLine(x1, z1, z2, colorDark + (alpha << 24));
        this.drawVerticalLine(x2, z1, z2, colorDark + (alpha << 24));
        this.drawHorizontalLine(x1, x2, z1, colorDark + (alpha << 24));
        this.drawHorizontalLine(x1, x2, z2, colorDark + (alpha << 24));
    }
    
    private int chunkXToDisplayX(final int chunkX) {
        return (int)((chunkX - this.scrollX) * 8.0f + GuiWDLChunkOverrides.width / 2);
    }
    
    private int chunkZToDisplayZ(final int chunkZ) {
        return (int)((chunkZ - this.scrollZ) * 8.0f + GuiWDLChunkOverrides.height / 2);
    }
    
    private int displayXToChunkX(final int displayX) {
        return MathHelper.floor((displayX - (float)(GuiWDLChunkOverrides.width / 2)) / 8.0f + this.scrollX);
    }
    
    private int displayZToChunkZ(final int displayZ) {
        return MathHelper.floor((displayZ - (float)(GuiWDLChunkOverrides.height / 2)) / 8.0f + this.scrollZ);
    }
    
    private int darken(final int color) {
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        r /= 2;
        g /= 2;
        b /= 2;
        return (r << 16) + (g << 8) + b;
    }
    
    private enum Mode
    {
        PANNING("PANNING", 0, 0, 128), 
        REQUESTING("REQUESTING", 1, 16, 128), 
        ERASING("ERASING", 2, 32, 128), 
        MOVING("MOVING", 3, 48, 128);
        
        public final int overlayU;
        public final int overlayV;
        
        private Mode(final String s, final int n, final int overlayU, final int overlayV) {
            this.overlayU = overlayU;
            this.overlayV = overlayV;
        }
    }
    
    private class RequestModeButton extends GuiButton
    {
        public final Mode mode;
        
        public RequestModeButton(final int buttonId, final int x, final int y, final Mode mode) {
            super(buttonId, x, y, 20, 20, "");
            this.mode = mode;
        }
        
        @Override
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float p_191745_4_) {
            if (GuiWDLChunkOverrides.this.mode == this.mode) {
                Gui.drawRect(this.xPosition - 2, this.yPosition - 2, this.xPosition + this.width + 2, this.yPosition + this.height + 2, -16744704);
            }
            super.drawButton(mc, mouseX, mouseY, p_191745_4_);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            mc.getTextureManager().bindTexture(GuiWDLChunkOverrides.WIDGET_TEXTURES);
            this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.mode.overlayU, this.mode.overlayV, 16, 16);
        }
    }
}
