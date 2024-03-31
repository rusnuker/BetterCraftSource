// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.gui;

import java.io.IOException;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.user.cosmetic.custom.UserTextureContainer;
import net.labymod.user.cosmetic.custom.handler.StickerImageHandler;
import net.labymod.user.cosmetic.custom.CosmeticImageHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.labymod.user.UserManager;
import net.labymod.main.ModTextures;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.utils.ModColor;
import net.labymod.utils.DrawUtils;
import net.labymod.user.sticker.data.StickerPack;
import net.labymod.user.sticker.StickerRegistry;
import net.labymod.user.User;
import net.labymod.main.lang.LanguageManager;
import java.util.ArrayList;
import net.minecraft.client.settings.GameSettings;
import net.labymod.main.ModSettings;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.labymod.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.labymod.main.Source;
import net.labymod.user.sticker.data.Sticker;
import java.util.List;
import net.labymod.gui.elements.ModTextField;
import java.lang.reflect.Field;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class StickerSelectorGui extends Gui
{
    private static final ResourceLocation POP_SOUND;
    private static final int ANIMATION_SPEED = 100;
    private static Field fieldPressTime;
    private static Field fieldLeftClickCounter;
    private boolean open;
    private long lastOpened;
    private int selectedItemIndex;
    private ModTextField fieldSearch;
    private boolean hoverSearchBar;
    private float lockedYaw;
    private float lockedPitch;
    private boolean prevCrosshairState;
    private short lastHoveredStickerId;
    private int hotkeySelectedSticker;
    private short lastAcceptedHoveredStickerId;
    private long lastHoveredStickerChanged;
    private int hoverStickerIndex;
    private double lastSelectorWidth;
    private double lastSelectorHeight;
    private List<Sticker> filteredStickers;
    private boolean searchOpened;
    private List<String> packTitles;
    private int searchMouseX;
    private int searchMouseY;
    private int page;
    private int acceptedPage;
    private int animationState;
    private long pageAnimation;
    
    static {
        POP_SOUND = new ResourceLocation(Source.ABOUT_MC_VERSION.startsWith("1.8") ? "random.pop" : "entity.chicken.egg");
    }
    
    public StickerSelectorGui() {
        this.open = false;
        this.lockedYaw = 0.0f;
        this.lockedPitch = 0.0f;
        this.lastHoveredStickerId = -1;
        this.hotkeySelectedSticker = -1;
        this.lastAcceptedHoveredStickerId = -1;
        this.hoverStickerIndex = -1;
        this.searchOpened = false;
        this.page = 0;
        this.acceptedPage = 0;
        this.animationState = 0;
        this.pageAnimation = 0L;
    }
    
    public void open() {
        if (this.open) {
            return;
        }
        final EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        if (player == null) {
            return;
        }
        this.open = true;
        this.page = 0;
        this.selectedItemIndex = LabyModCore.getMinecraft().getPlayer().inventory.currentItem;
        this.hotkeySelectedSticker = -1;
        this.lastAcceptedHoveredStickerId = -1;
        this.lockedYaw = player.rotationYaw;
        this.lockedPitch = player.rotationPitch;
        (this.fieldSearch = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, 0, 20)).setBlackBox(false);
        this.fieldSearch.setEnableBackgroundDrawing(false);
        this.searchOpened = false;
        this.prevCrosshairState = LabyMod.getInstance().getLabyModAPI().isCrosshairHidden();
        LabyMod.getInstance().getLabyModAPI().setCrosshairHidden(true);
        if (StickerSelectorGui.fieldLeftClickCounter == null) {
            try {
                (StickerSelectorGui.fieldLeftClickCounter = ReflectionHelper.findField(Minecraft.class, LabyModCore.getMappingAdapter().getLeftClickCounterMappings())).setAccessible(true);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (StickerSelectorGui.fieldPressTime == null) {
            try {
                (StickerSelectorGui.fieldPressTime = ReflectionHelper.findField(KeyBinding.class, LabyModCore.getMappingAdapter().getPressTimeMappings())).setAccessible(true);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        this.filter("");
        if (System.currentTimeMillis() - this.lastOpened < 300L) {
            Minecraft.getMinecraft().displayGuiScreen(new SearchGui(this, this.fieldSearch));
            this.searchOpened = true;
            this.animationState = 0;
        }
        else {
            this.searchOpened = false;
        }
        this.lastOpened = System.currentTimeMillis();
    }
    
    public void close() {
        if (!this.open) {
            return;
        }
        if (!this.searchOpened && this.hoverSearchBar) {
            this.searchOpened = true;
            Minecraft.getMinecraft().displayGuiScreen(new SearchGui(this, this.fieldSearch));
            return;
        }
        this.searchOpened = false;
        this.open = false;
        LabyMod.getInstance().getLabyModAPI().setCrosshairHidden(this.prevCrosshairState);
        this.updateScrollLock(false);
        final EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        if (player == null) {
            return;
        }
        player.rotationYaw = this.lockedYaw;
        player.rotationPitch = this.lockedPitch;
        if (this.lastHoveredStickerId != -1) {
            LabyModCore.getMinecraft().playSound(StickerSelectorGui.POP_SOUND, 1.0f);
            LabyMod.getInstance().getStickerRegistry().playSticker(LabyMod.getInstance().getUserManager().getUser(player.getUniqueID()), this.lastHoveredStickerId);
            final ModSettings settings = LabyMod.getSettings();
            try {
                boolean swap = false;
                for (int i = 0; i < settings.stickerHistory.length; ++i) {
                    if (settings.stickerHistory[i] == this.lastHoveredStickerId) {
                        swap = true;
                    }
                }
                if (!swap) {
                    settings.stickerHistory[settings.stickerHistory.length - 1] = -1;
                    for (int i = settings.stickerHistory.length - 2; i >= 0; --i) {
                        if (settings.stickerHistory[i + 1] == -1) {
                            settings.stickerHistory[i + 1] = settings.stickerHistory[i];
                            settings.stickerHistory[i] = -1;
                        }
                    }
                    settings.stickerHistory[0] = this.lastHoveredStickerId;
                    LabyMod.getMainConfig().save();
                }
            }
            catch (final Exception error) {
                error.printStackTrace();
                settings.stickerHistory = new short[] { -1, -1, -1, -1, -1 };
                LabyMod.getMainConfig().save();
            }
        }
    }
    
    private void updateScrollLock(final boolean locked) {
        final GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        if (gameSettings == null) {
            return;
        }
        final KeyBinding keyBinding = gameSettings.keyBindsHotbar[this.selectedItemIndex];
        if (keyBinding == null) {
            return;
        }
        final int keyCode = keyBinding.getKeyCode();
        KeyBinding.setKeyBindState(keyCode, locked);
        if (locked) {
            KeyBinding.onTick(keyCode);
        }
        else {
            try {
                if (StickerSelectorGui.fieldPressTime != null) {
                    StickerSelectorGui.fieldPressTime.setInt(keyBinding, 0);
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void filter(String searchString) {
        final EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        final User user = LabyMod.getInstance().getUserManager().getUser(player.getUniqueID());
        final List<Short> stickerPacks = user.getStickerPacks();
        final List<Sticker> filteredStickers = new ArrayList<Sticker>();
        final StickerRegistry stickerRegistry = LabyMod.getInstance().getStickerRegistry();
        if (stickerRegistry.getStickerData() != null && stickerRegistry.getStickerData().getPacks() != null) {
            if (searchString.isEmpty()) {
                final List<String> packTitles = new ArrayList<String>();
                final ModSettings settings = LabyMod.getSettings();
                for (int i = 0; i < settings.stickerHistory.length; ++i) {
                    final short id = settings.stickerHistory[i];
                    if (id != -1 || !packTitles.isEmpty()) {
                        filteredStickers.add((id == -1) ? null : stickerRegistry.getSticker(id));
                        if (packTitles.isEmpty()) {
                            packTitles.add(LanguageManager.translate("sticker_history"));
                        }
                    }
                }
                StickerPack[] packs;
                for (int length = (packs = stickerRegistry.getStickerData().getPacks()).length, j = 0; j < length; ++j) {
                    final StickerPack stickerPack = packs[j];
                    if (stickerPacks.contains(stickerPack.getId())) {
                        Sticker[] stickers;
                        for (int length2 = (stickers = stickerPack.getStickers()).length, k = 0; k < length2; ++k) {
                            final Sticker sticker = stickers[k];
                            filteredStickers.add(sticker);
                        }
                        packTitles.add(stickerPack.getName());
                    }
                }
                this.packTitles = packTitles;
                this.filteredStickers = filteredStickers;
            }
            else {
                searchString = searchString.toLowerCase();
                StickerPack[] packs2;
                for (int length3 = (packs2 = stickerRegistry.getStickerData().getPacks()).length, l = 0; l < length3; ++l) {
                    final StickerPack stickerPack2 = packs2[l];
                    if (stickerPacks.contains(stickerPack2.getId())) {
                        if (stickerPack2.getName().toLowerCase().contains(searchString)) {
                            Sticker[] stickers2;
                            for (int length4 = (stickers2 = stickerPack2.getStickers()).length, n = 0; n < length4; ++n) {
                                final Sticker sticker2 = stickers2[n];
                                filteredStickers.add(sticker2);
                            }
                        }
                        else {
                            Sticker[] stickers3;
                            for (int length5 = (stickers3 = stickerPack2.getStickers()).length, n2 = 0; n2 < length5; ++n2) {
                                final Sticker sticker2 = stickers3[n2];
                                if (sticker2.getName().toLowerCase().contains(searchString)) {
                                    filteredStickers.add(sticker2);
                                }
                                else {
                                    String[] tags;
                                    for (int length6 = (tags = sticker2.getTags()).length, n3 = 0; n3 < length6; ++n3) {
                                        final String tag = tags[n3];
                                        if (tag.toLowerCase().contains(searchString)) {
                                            filteredStickers.add(sticker2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.filteredStickers = filteredStickers;
    }
    
    public void render() {
        if (!this.open) {
            return;
        }
        final EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        final UserManager userManager = LabyMod.getInstance().getUserManager();
        final User user = userManager.getUser(player.getUniqueID());
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        try {
            if (StickerSelectorGui.fieldLeftClickCounter != null) {
                StickerSelectorGui.fieldLeftClickCounter.setInt(Minecraft.getMinecraft(), 2);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        long timePassed = System.currentTimeMillis() - this.lastOpened;
        if (timePassed > 2000L) {
            timePassed = 2000L;
        }
        final double stickerSize = draw.getHeight() / 8.0 - Math.exp(-timePassed / 100.0) * 10.0;
        final double padding = 2.0;
        double midX = draw.getWidth() / 2.0;
        double midY = draw.getHeight() / 2.0 - (stickerSize / 2.0 + 2.0);
        final double lockedX = this.lockedYaw;
        double lockedY = this.lockedPitch;
        final double radiusY = this.lastSelectorHeight / 3.0;
        if (lockedY + radiusY > 90.0) {
            lockedY = (float)(90.0 - radiusY);
        }
        if (lockedY - radiusY < -90.0) {
            lockedY = (float)(-90.0 + radiusY);
        }
        double offsetX = lockedX - player.rotationYaw;
        double offsetY = lockedY - player.rotationPitch;
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            midX += offsetX;
            midY += offsetY;
        }
        final int stickerPerPage = 5;
        final int totalSticker = this.filteredStickers.size();
        final int maxPages = (int)Math.ceil(totalSticker / 5.0);
        final int stickerIndex = this.page * 5;
        final int acceptedStickerIndex = this.acceptedPage * 5;
        final double selectorWidth = 5.0 * (stickerSize + 1.0) - 1.0;
        DrawUtils.drawRect(midX - selectorWidth / 2.0 - 2.0, midY - stickerSize / 2.0 - 2.0, midX + selectorWidth / 2.0 + 2.0, midY + stickerSize / 2.0 + 2.0, Integer.MIN_VALUE);
        double cursorX = midX - offsetX * 1.5;
        double cursorY = midY - offsetY * 1.5 + stickerSize / 2.0 + 2.0;
        if (this.searchOpened) {
            cursorX = this.searchMouseX;
            cursorY = this.searchMouseY;
            offsetX = this.searchMouseX - draw.getWidth() / 2;
            offsetY = this.searchMouseY - draw.getHeight() / 2;
        }
        final int searchFieldHeight = 10;
        final int searchAreaGap = 1;
        DrawUtils.drawRect(midX - selectorWidth / 2.0 - 2.0, midY + stickerSize / 2.0 + 2.0 + 1.0, midX + selectorWidth / 2.0 + 2.0, midY + stickerSize / 2.0 + 2.0 + 10.0 + 4.0 + 1.0 + 2.0, Integer.MIN_VALUE);
        final double fieldX = midX - selectorWidth / 2.0 + 1.0;
        final double fieldY = midY + stickerSize / 2.0 + 2.0 + 1.0 + 2.0 + 1.0;
        this.fieldSearch.width = (int)selectorWidth - 2;
        this.fieldSearch.height = 10;
        this.fieldSearch.xPosition = (int)fieldX + 1;
        this.fieldSearch.yPosition = (int)fieldY + 2;
        this.hoverSearchBar = (cursorX > fieldX - 1.0 && cursorX < fieldX + selectorWidth - 1.0 && cursorY > fieldY - 1.0 && cursorY < fieldY + this.fieldSearch.height + 1.0);
        DrawUtils.drawRect(fieldX - 1.0, fieldY - 1.0, fieldX + selectorWidth - 1.0, fieldY + this.fieldSearch.height + 1.0, this.hoverSearchBar ? ModColor.toRGB(255, 255, 170, 100) : Integer.MAX_VALUE);
        if (!this.fieldSearch.isFocused()) {
            draw.drawString(LanguageManager.translate("search_textbox_placeholder"), fieldX + 1.0, fieldY + 1.0);
        }
        this.drawStickerRow(midX, midY, cursorX, cursorY, 5, totalSticker, stickerIndex, stickerSize, selectorWidth, user, this.filteredStickers, 0);
        this.drawStickerRow(midX, midY, cursorX, cursorY, 5, totalSticker, stickerIndex, stickerSize, selectorWidth, user, this.filteredStickers, -1);
        this.drawStickerRow(midX, midY, cursorX, cursorY, 5, totalSticker, acceptedStickerIndex, stickerSize, selectorWidth, user, this.filteredStickers, 1);
        double pageDisplayX = midX + selectorWidth / 2.0 + 2.0 + 1.0;
        double pageDisplayY = midY - stickerSize / 2.0 - 2.0;
        for (int i = 0; i < maxPages; ++i) {
            final boolean selected = this.page == i;
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_MENU_POINT);
            draw.drawTexture(pageDisplayX, pageDisplayY, 255.0, 255.0, 3.0, 3.0, selected ? 1.1f : 0.2f);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
            pageDisplayY += 4.0;
            if (pageDisplayY > midY + stickerSize / 2.0 + 2.0) {
                pageDisplayY = midY - stickerSize / 2.0 - 2.0;
                pageDisplayX += 4.0;
            }
        }
        final String title = (this.packTitles == null || this.packTitles.isEmpty()) ? LanguageManager.translate("sticker_title") : this.packTitles.get(this.page);
        final double titleWidth = draw.getStringWidth(title);
        final double titleOffsetY = -15.0;
        final double titlePadding = 3.0;
        DrawUtils.drawRect(midX - titleWidth / 2.0 - 3.0, midY - stickerSize / 2.0 - 15.0 - 3.0, midX + titleWidth / 2.0 + 3.0, midY - stickerSize / 2.0 - 15.0 + 7.0 + 3.0, Integer.MIN_VALUE);
        draw.drawCenteredString(title, midX, midY - stickerSize / 2.0 - 15.0);
        if (this.filteredStickers.isEmpty() && !this.searchOpened) {
            final double statusY = midY + stickerSize / 2.0 + 10.0 + 1.0 + 10.0;
            draw.drawCenteredString(LanguageManager.translate("sticker_status_no_stickers"), midX, statusY, 0.8);
        }
        else if (maxPages > 1 && this.page == 0) {
            final double statusY = midY + stickerSize / 2.0 + 10.0 + 1.0 + 10.0;
            draw.drawCenteredString(String.valueOf(ModColor.cl('7')) + LanguageManager.translate("sticker_info_scroll"), midX, statusY, 0.8);
        }
        if (offsetX == 0.0 && offsetY == 0.0) {
            cursorX = (int)cursorX;
            cursorY = (int)cursorY;
        }
        if (!this.searchOpened) {
            DrawUtils.drawRect(cursorX, cursorY - 4.0, cursorX + 1.0, cursorY + 5.0, Integer.MAX_VALUE);
            DrawUtils.drawRect(cursorX - 4.0, cursorY, cursorX + 5.0, cursorY + 1.0, Integer.MAX_VALUE);
        }
        this.lastSelectorWidth = selectorWidth + 4.0;
        this.lastSelectorHeight = 2.0 + stickerSize + 2.0 + 1.0 + 2.0 + 10.0 + 2.0;
        this.handleMouseInput(maxPages - 1);
    }
    
    private void handleMouseInput(final int maxPages) {
        final double scroll = Mouse.getDWheel();
        boolean moveUp = scroll > 0.0;
        boolean moveDown = scroll < 0.0;
        if (this.hoverStickerIndex == -1 && (moveUp || moveDown) && (!moveDown || this.acceptedPage != maxPages || this.hotkeySelectedSticker != 0) && (!moveUp || this.acceptedPage != 0 || this.hotkeySelectedSticker != 4)) {
            final int value = this.hotkeySelectedSticker + (moveUp ? 1 : -1);
            this.hotkeySelectedSticker = ((value < 0) ? 4 : (value % 5));
            if ((moveUp || value != -1) && (!moveUp || value != 5)) {
                moveDown = false;
                moveUp = false;
            }
        }
        try {
            for (int stickerPerPage = 5, i = 0; i < stickerPerPage; ++i) {
                final int code = Minecraft.getMinecraft().gameSettings.keyBindsHotbar[i].getKeyCode();
                if (code >= 0 && Keyboard.isKeyDown(code)) {
                    this.hotkeySelectedSticker = stickerPerPage - 1 - i;
                }
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
        if (Mouse.isButtonDown(0)) {
            moveUp = true;
        }
        if (Mouse.isButtonDown(1)) {
            moveDown = true;
        }
        if (this.acceptedPage == this.page && this.animationState == 0) {
            if (moveUp && this.page > 0) {
                --this.page;
                this.animationState = -1;
                this.pageAnimation = System.currentTimeMillis();
            }
            if (moveDown && this.page < maxPages) {
                ++this.page;
                this.animationState = 1;
                this.pageAnimation = System.currentTimeMillis();
            }
        }
        else if (!moveUp && !moveDown && this.animationState != -1 && this.animationState != 1) {
            this.acceptedPage = this.page;
        }
        if ((this.animationState == -1 || this.animationState == 1) && this.pageAnimation + 100L < System.currentTimeMillis()) {
            this.animationState *= 2;
        }
        if ((this.animationState == -2 || this.animationState == 2) && this.pageAnimation + 100L < System.currentTimeMillis()) {
            this.animationState = 0;
        }
    }
    
    private void drawStickerRow(final double midX, final double midY, final double cursorX, final double cursorY, final int stickerPerPage, final int totalSticker, int stickerIndex, final double stickerSize, final double selectorWidth, final User user, final List<Sticker> stickers, final int animationChannel) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final long timePassed = System.currentTimeMillis() - this.pageAnimation;
        final double percentage = 0.01 * timePassed;
        double animation = (this.animationState == 0) ? 1.0 : Math.min(percentage, 1.0);
        final boolean increased = this.page < this.acceptedPage;
        final boolean lowBound = animationChannel == (increased ? 1 : -1);
        this.lastHoveredStickerId = -1;
        this.hoverStickerIndex = -1;
        if (animationChannel == 1) {
            animation = 1.0 - animation;
        }
        final double lowOffset = (stickerSize - 2.0) * (1.0 - animation);
        for (int index = stickerPerPage - 1; index >= 0; --index) {
            final double x = midX - selectorWidth / 2.0 + (stickerPerPage - index - 1) * (stickerSize + 1.0);
            final double y = midY - stickerSize / 2.0;
            boolean hover = cursorX > x && cursorX < x + stickerSize && cursorY > y && cursorY < y + stickerSize;
            final Sticker sticker = (stickerIndex >= totalSticker) ? null : stickers.get(stickerIndex);
            final Short id = (sticker == null) ? null : Short.valueOf(sticker.getId());
            if (hover) {
                this.hoverStickerIndex = index;
            }
            if (hover && id != null) {
                this.hotkeySelectedSticker = -1;
            }
            if (this.hotkeySelectedSticker != -1 && this.hotkeySelectedSticker == index) {
                hover = true;
            }
            double brightness = 0.0;
            if (hover && id != null) {
                brightness = 185.0 + 185.0 * -Math.exp(-(System.currentTimeMillis() - this.lastHoveredStickerChanged) / 500.0);
            }
            if (animationChannel == 0) {
                DrawUtils.drawRect(x, y, x + stickerSize, y + stickerSize, ModColor.toRGB((int)brightness, (int)brightness, (int)brightness, 50));
            }
            else {
                double currentStickerSize = stickerSize - 2.0;
                double hoverOffset = 0.0;
                if (hover && id != null) {
                    this.lastHoveredStickerId = id;
                    if (this.lastAcceptedHoveredStickerId != id) {
                        this.lastAcceptedHoveredStickerId = id;
                        this.lastHoveredStickerChanged = System.currentTimeMillis();
                    }
                }
                if (hover) {
                    final double popUp = 10.0 + 10.0 * -Math.exp(-(System.currentTimeMillis() - this.lastHoveredStickerChanged) / 100.0);
                    hoverOffset = popUp / 2.0;
                    currentStickerSize += popUp;
                }
                GlStateManager.pushMatrix();
                GlStateManager.scale(1.0, animation, 1.0);
                this.drawSticker(x + 1.0 - hoverOffset, (y + 1.0 - hoverOffset + (lowBound ? lowOffset : 0.0)) / animation, currentStickerSize, id, user);
                GlStateManager.popMatrix();
                ++stickerIndex;
            }
        }
    }
    
    private void drawSticker(final double x, final double y, final double size, final Short stickerId, final User user) {
        final UserManager userManager = LabyMod.getInstance().getUserManager();
        final StickerImageHandler stickerHandler = userManager.getCosmeticImageManager().getStickerImageHandler();
        if (stickerHandler != null && stickerId != null && stickerId != -1) {
            final UserTextureContainer container = stickerHandler.getContainer(user, stickerId);
            if (container != null) {
                container.validateTexture(stickerHandler);
                final ResourceLocation resourceLocation = stickerHandler.getResourceLocations().get(container.getFileName());
                if (resourceLocation != null) {
                    final double scale = size / 20.0;
                    final double textureSizeX = 115.90909090909092;
                    final double textureSizeY = 231.81818181818184;
                    final double textureOffsetX = 11.590909090909092;
                    final double textureOffsetY = 23.181818181818183;
                    GlStateManager.pushMatrix();
                    GlStateManager.color(1.0f, 1.1f, 1.0f, 1.0f);
                    GlStateManager.scale(scale, scale, 1.0);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
                    LabyMod.getInstance().getDrawUtils().drawTexture(x / scale, y / scale, 11.590909090909092, 23.181818181818183, 115.90909090909092, 231.81818181818184, 20.0, 20.0);
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    
    public void pointSearchMouse(final int mouseX, final int mouseY) {
        this.searchMouseX = mouseX;
        this.searchMouseY = mouseY;
        if (this.lastHoveredStickerId != -1) {
            final Sticker sticker = LabyMod.getInstance().getStickerRegistry().getSticker(this.lastHoveredStickerId);
            if (sticker != null) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, sticker.getName());
            }
        }
    }
    
    public void lockMouseMovementInCircle() {
        if (!this.open) {
            return;
        }
        final EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        if (player == null) {
            return;
        }
        final double radiusX = this.lastSelectorWidth / 3.0;
        final double radiusY = this.lastSelectorHeight / 3.0;
        float centerX = this.lockedYaw;
        float centerY = this.lockedPitch + 5.0f - 16.0f;
        if (centerY + radiusY > 90.0) {
            centerY = (float)(90.0 - radiusY);
        }
        if (centerY - radiusY < -90.0) {
            centerY = (float)(-90.0 + radiusY);
        }
        final float newX = player.rotationYaw;
        final float newY = player.rotationPitch;
        final double distanceX = Math.abs(centerX - newX);
        final double distanceY = Math.abs(centerY - newY);
        if (distanceX > radiusX) {
            double fromOriginToObjectX = newX - centerX;
            final double multiplier = radiusX / distanceX;
            fromOriginToObjectX *= multiplier;
            centerX += (float)fromOriginToObjectX;
            player.rotationYaw = centerX;
            player.prevRotationYaw = centerX;
        }
        if (distanceY > radiusY) {
            double fromOriginToObjectY = newY - centerY;
            final double multiplier = radiusY / distanceY;
            fromOriginToObjectY *= multiplier;
            centerY += (float)fromOriginToObjectY;
            player.rotationPitch = centerY;
            player.prevRotationPitch = centerY;
        }
        this.updateScrollLock(true);
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public static class SearchGui extends GuiScreen
    {
        private StickerSelectorGui stickerSelectorGui;
        private ModTextField textField;
        
        public SearchGui(final StickerSelectorGui stickerSelectorGui, final ModTextField textField) {
            this.stickerSelectorGui = stickerSelectorGui;
            this.textField = textField;
        }
        
        @Override
        public void initGui() {
            super.initGui();
            this.textField.setFocused(true);
        }
        
        @Override
        public void onGuiClosed() {
            super.onGuiClosed();
            this.stickerSelectorGui.close();
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.stickerSelectorGui.pointSearchMouse(mouseX, mouseY);
            this.textField.drawTextBox();
        }
        
        @Override
        public void updateScreen() {
            super.updateScreen();
            this.textField.updateCursorCounter();
        }
        
        @Override
        protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
            super.keyTyped(typedChar, keyCode);
            if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
                this.stickerSelectorGui.filter(this.textField.getText());
            }
        }
        
        public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            if (this.textField.mouseClicked(mouseX, mouseY, mouseButton)) {
                return;
            }
            this.stickerSelectorGui.pointSearchMouse(mouseX, mouseY);
            Minecraft.getMinecraft().displayGuiScreen(null);
            this.stickerSelectorGui.close();
        }
        
        @Override
        protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
            super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
            this.stickerSelectorGui.pointSearchMouse(mouseX, mouseY);
        }
    }
}
