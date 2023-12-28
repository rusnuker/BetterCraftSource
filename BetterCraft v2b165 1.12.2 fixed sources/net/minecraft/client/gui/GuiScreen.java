// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import me.amkgre.bettercraft.client.mods.shader.ShaderRenderer;
import net.minecraft.util.ResourceLocation;
import me.amkgre.bettercraft.client.utils.ClientSettingsUtils;
import me.amkgre.bettercraft.client.mods.shader.old.GuiShaderOld;
import me.amkgre.bettercraft.client.mods.shader.browser.GuiShaderBrowser;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.TextureUtil;
import java.io.FileInputStream;
import me.amkgre.bettercraft.client.utils.FileManagerUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.io.File;
import java.util.Locale;
import java.net.URISyntaxException;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.ITextComponent;
import java.util.Iterator;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Arrays;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import org.apache.commons.lang3.StringUtils;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import java.io.IOException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.net.URI;
import java.util.List;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.Minecraft;
import com.google.common.base.Splitter;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public abstract class GuiScreen extends Gui implements GuiYesNoCallback
{
    protected static final Logger LOGGER;
    private static final Set<String> PROTOCOLS;
    private static final Splitter NEWLINE_SPLITTER;
    protected Minecraft mc;
    protected RenderItem itemRender;
    public static int width;
    public static int height;
    protected List<GuiButton> buttonList;
    protected List<GuiLabel> labelList;
    public boolean allowUserInput;
    protected FontRenderer fontRendererObj;
    protected GuiButton selectedButton;
    private int eventButton;
    private long lastMouseEvent;
    private int touchValue;
    private URI clickedLinkURI;
    private boolean field_193977_u;
    int max;
    int w;
    int speed;
    int curren;
    private int MouseXBackGround;
    private int MouseYBackGround;
    public static DynamicTexture dynamicTexture;
    
    static {
        LOGGER = LogManager.getLogger();
        PROTOCOLS = Sets.newHashSet("http", "https");
        NEWLINE_SPLITTER = Splitter.on('\n');
    }
    
    public GuiScreen() {
        this.buttonList = (List<GuiButton>)Lists.newArrayList();
        this.labelList = (List<GuiLabel>)Lists.newArrayList();
        this.max = 100;
        this.w = 2;
        this.speed = 10;
        this.curren = 0;
        this.MouseXBackGround = 0;
        this.MouseYBackGround = 0;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.MouseXBackGround = mouseX;
        this.MouseYBackGround = mouseY;
        for (int i = 0; i < this.buttonList.size(); ++i) {
            this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
        for (int j = 0; j < this.labelList.size(); ++j) {
            this.labelList.get(j).drawLabel(this.mc, mouseX, mouseY);
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
            if (Minecraft.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }
    
    protected <T extends GuiButton> T addButton(final T p_189646_1_) {
        this.buttonList.add(p_189646_1_);
        return p_189646_1_;
    }
    
    public static String getClipboardString() {
        try {
            final Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String)transferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (final Exception ex) {}
        return "";
    }
    
    public static void setClipboardString(final String copyText) {
        if (!StringUtils.isEmpty(copyText)) {
            try {
                final StringSelection stringselection = new StringSelection(copyText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
            }
            catch (final Exception ex) {}
        }
    }
    
    protected void renderToolTip(final ItemStack stack, final int x, final int y) {
        this.drawHoveringText(this.func_191927_a(stack), x, y);
    }
    
    public List<String> func_191927_a(final ItemStack p_191927_1_) {
        final List<String> list = p_191927_1_.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, p_191927_1_.getRarity().rarityColor + list.get(i));
            }
            else {
                list.set(i, TextFormatting.GRAY + list.get(i));
            }
        }
        return list;
    }
    
    public void drawCreativeTabHoveringText(final String tabName, final int mouseX, final int mouseY) {
        this.drawHoveringText(Arrays.asList(tabName), mouseX, mouseY);
    }
    
    public void func_193975_a(final boolean p_193975_1_) {
        this.field_193977_u = p_193975_1_;
    }
    
    public boolean func_193976_p() {
        return this.field_193977_u;
    }
    
    public void drawHoveringText(final List<String> textLines, final int x, final int y) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;
            for (final String s : textLines) {
                final int j = this.fontRendererObj.getStringWidth(s);
                if (j > i) {
                    i = j;
                }
            }
            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;
            if (textLines.size() > 1) {
                k += 2 + (textLines.size() - 1) * 10;
            }
            if (l1 + i > GuiScreen.width) {
                l1 -= 28 + i;
            }
            if (i2 + k + 6 > GuiScreen.height) {
                i2 = GuiScreen.height - k - 6;
            }
            GuiScreen.zLevel = 300.0f;
            this.itemRender.zLevel = 300.0f;
            final int m = -267386864;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
            this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
            final int i3 = 1347420415;
            final int j2 = 1344798847;
            for (int k2 = 0; k2 < textLines.size(); ++k2) {
                final String s2 = textLines.get(k2);
                this.fontRendererObj.drawStringWithShadow(s2, (float)l1, (float)i2, -1);
                if (k2 == 0) {
                    i2 += 2;
                }
                i2 += 10;
            }
            GuiScreen.zLevel = 0.0f;
            this.itemRender.zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
    
    protected void handleComponentHover(final ITextComponent component, final int x, final int y) {
        if (component != null && component.getStyle().getHoverEvent() != null) {
            final HoverEvent hoverevent = component.getStyle().getHoverEvent();
            if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
                ItemStack itemstack = ItemStack.field_190927_a;
                try {
                    final NBTBase nbtbase = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
                    if (nbtbase instanceof NBTTagCompound) {
                        itemstack = new ItemStack((NBTTagCompound)nbtbase);
                    }
                }
                catch (final NBTException ex) {}
                if (itemstack.func_190926_b()) {
                    this.drawCreativeTabHoveringText(TextFormatting.RED + "Invalid Item!", x, y);
                }
                else {
                    this.renderToolTip(itemstack, x, y);
                }
            }
            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
                if (this.mc.gameSettings.advancedItemTooltips) {
                    try {
                        final NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
                        final List<String> list = (List<String>)Lists.newArrayList();
                        list.add(nbttagcompound.getString("name"));
                        if (nbttagcompound.hasKey("type", 8)) {
                            final String s = nbttagcompound.getString("type");
                            list.add("Type: " + s);
                        }
                        list.add(nbttagcompound.getString("id"));
                        this.drawHoveringText(list, x, y);
                    }
                    catch (final NBTException var8) {
                        this.drawCreativeTabHoveringText(TextFormatting.RED + "Invalid Entity!", x, y);
                    }
                }
            }
            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
                this.drawHoveringText(this.mc.fontRendererObj.listFormattedStringToWidth(hoverevent.getValue().getFormattedText(), Math.max(GuiScreen.width / 2, 200)), x, y);
            }
            GlStateManager.disableLighting();
        }
    }
    
    protected void setText(final String newChatText, final boolean shouldOverwrite) {
    }
    
    public boolean handleComponentClick(final ITextComponent component) {
        if (component == null) {
            return false;
        }
        final ClickEvent clickevent = component.getStyle().getClickEvent();
        if (isShiftKeyDown()) {
            if (component.getStyle().getInsertion() != null) {
                this.setText(component.getStyle().getInsertion(), false);
            }
        }
        else if (clickevent != null) {
            if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
                if (!this.mc.gameSettings.chatLinks) {
                    return false;
                }
                try {
                    final URI uri = new URI(clickevent.getValue());
                    final String s = uri.getScheme();
                    if (s == null) {
                        throw new URISyntaxException(clickevent.getValue(), "Missing protocol");
                    }
                    if (!GuiScreen.PROTOCOLS.contains(s.toLowerCase(Locale.ROOT))) {
                        throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s.toLowerCase(Locale.ROOT));
                    }
                    if (this.mc.gameSettings.chatLinksPrompt) {
                        this.clickedLinkURI = uri;
                        this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, clickevent.getValue(), 31102009, false));
                    }
                    else {
                        this.openWebLink(uri);
                    }
                }
                catch (final URISyntaxException urisyntaxexception) {
                    GuiScreen.LOGGER.error("Can't open url for {}", clickevent, urisyntaxexception);
                }
            }
            else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
                final URI uri2 = new File(clickevent.getValue()).toURI();
                this.openWebLink(uri2);
            }
            else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                this.setText(clickevent.getValue(), true);
            }
            else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.sendChatMessage(clickevent.getValue(), false);
            }
            else {
                GuiScreen.LOGGER.error("Don't know how to handle {}", clickevent);
            }
            return true;
        }
        return false;
    }
    
    public void sendChatMessage(final String msg) {
        this.sendChatMessage(msg, true);
    }
    
    public void sendChatMessage(final String msg, final boolean addToChat) {
        if (addToChat) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
        this.mc.player.sendChatMessage(msg);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (int i = 0; i < this.buttonList.size(); ++i) {
                final GuiButton guibutton = this.buttonList.get(i);
                if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                    (this.selectedButton = guibutton).playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(guibutton);
                }
            }
        }
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.selectedButton != null && state == 0) {
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
    }
    
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
    }
    
    protected void actionPerformed(final GuiButton button) throws IOException {
    }
    
    public void setWorldAndResolution(final Minecraft mc, final int width, final int height) {
        this.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRendererObj = mc.fontRendererObj;
        GuiScreen.width = width;
        GuiScreen.height = height;
        this.buttonList.clear();
        this.initGui();
    }
    
    public void setGuiSize(final int w, final int h) {
        GuiScreen.width = w;
        GuiScreen.height = h;
    }
    
    public void initGui() {
    }
    
    public void handleInput() throws IOException {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                this.handleMouseInput();
            }
        }
        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                this.handleKeyboardInput();
            }
        }
    }
    
    public void handleMouseInput() throws IOException {
        final int i = Mouse.getEventX() * GuiScreen.width / this.mc.displayWidth;
        final int j = GuiScreen.height - Mouse.getEventY() * GuiScreen.height / this.mc.displayHeight - 1;
        final int k = Mouse.getEventButton();
        if (Mouse.getEventButtonState()) {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }
            this.eventButton = k;
            this.lastMouseEvent = Minecraft.getSystemTime();
            this.mouseClicked(i, j, this.eventButton);
        }
        else if (k != -1) {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }
            this.eventButton = -1;
            this.mouseReleased(i, j, k);
        }
        else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            final long l = Minecraft.getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(i, j, this.eventButton, l);
        }
    }
    
    public void handleKeyboardInput() throws IOException {
        final char c0 = Keyboard.getEventCharacter();
        if ((Keyboard.getEventKey() == 0 && c0 >= ' ') || Keyboard.getEventKeyState()) {
            this.keyTyped(c0, Keyboard.getEventKey());
        }
        this.mc.dispatchKeypresses();
    }
    
    public void updateScreen() {
    }
    
    public void onGuiClosed() {
    }
    
    public void drawDefaultBackground() {
        if (GuiScreen.dynamicTexture == null) {
            try {
                GuiScreen.dynamicTexture = new DynamicTexture(TextureUtil.readBufferedImage(new FileInputStream(new File(FileManagerUtils.clientDir + "/customBG.bc"))));
            }
            catch (final IOException ex) {}
        }
        if (this.mc.player != null) {
            this.drawWorldBackground(0);
            return;
        }
        if (!GuiShaderBrowser.shader && !GuiShaderOld.shader) {
            final ScaledResolution s1 = new ScaledResolution(this.mc);
            try {
                if (!ClientSettingsUtils.isCurrentBackgroundImageCustom) {
                    this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/backgrounds/" + ClientSettingsUtils.fieldToStr(ClientSettingsUtils.class.getDeclaredField("currentBackgroundImage")).split("=")[1] + ".png"));
                }
                else {
                    try {
                        GlStateManager.bindTexture(GuiScreen.dynamicTexture.getGlTextureId());
                    }
                    catch (final Exception exception) {
                        exception.printStackTrace();
                        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/backgrounds/" + ClientSettingsUtils.fieldToStr(ClientSettingsUtils.class.getDeclaredField("currentBackgroundImage")).split("=")[1] + ".png"));
                    }
                }
            }
            catch (final NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), (float)ScaledResolution.getScaledWidth(), (float)ScaledResolution.getScaledHeight());
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            return;
        }
        ShaderRenderer.doShaderStuff();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public void drawWorldBackground(final int tint) {
        if (this.mc.world != null) {
            this.drawGradientRect(0, 0, GuiScreen.width, GuiScreen.height, -1072689136, -804253680);
        }
        else {
            this.drawBackground(tint);
        }
    }
    
    public void drawBackground(final int tint) {
        this.drawDefaultBackground();
    }
    
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (id == 31102009) {
            if (result) {
                this.openWebLink(this.clickedLinkURI);
            }
            this.clickedLinkURI = null;
            this.mc.displayGuiScreen(this);
        }
    }
    
    protected void openWebLink(final URI url) {
        try {
            final Class<?> oclass = Class.forName("java.awt.Desktop");
            final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            oclass.getMethod("browse", URI.class).invoke(object, url);
        }
        catch (final Throwable throwable1) {
            final Throwable throwable2 = throwable1.getCause();
            GuiScreen.LOGGER.error("Couldn't open link: {}", (throwable2 == null) ? "<UNKNOWN>" : throwable2.getMessage());
        }
    }
    
    public static boolean isCtrlKeyDown() {
        if (Minecraft.IS_RUNNING_ON_MAC) {
            return Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220);
        }
        return Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }
    
    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }
    
    public static boolean isAltKeyDown() {
        return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
    }
    
    public static boolean isKeyComboCtrlX(final int keyID) {
        return keyID == 45 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }
    
    public static boolean isKeyComboCtrlV(final int keyID) {
        return keyID == 47 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }
    
    public static boolean isKeyComboCtrlC(final int keyID) {
        return keyID == 46 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }
    
    public static boolean isKeyComboCtrlA(final int keyID) {
        return keyID == 30 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }
    
    public void onResize(final Minecraft mcIn, final int w, final int h) {
        this.setWorldAndResolution(mcIn, w, h);
    }
}
