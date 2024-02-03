// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tabs;

import net.labymod.ingamechat.namehistory.NameHistoryUtil;
import net.labymod.utils.Consumer;
import java.io.IOException;
import net.labymod.utils.UUIDFetcher;
import net.labymod.utils.ModUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.gui.FontRenderer;
import net.labymod.utils.DrawUtils;
import net.labymod.core.LabyModCore;
import net.labymod.main.lang.LanguageManager;
import net.labymod.main.LabyMod;
import net.labymod.ingamechat.namehistory.NameHistory;
import net.labymod.gui.elements.ModTextField;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.ingamechat.GuiChatCustom;

public class GuiChatNameHistory extends GuiChatCustom
{
    private final int windowWidth = 160;
    private final int windowHeight = 16;
    private final int entryHeight = 11;
    private final int maxEntries = 10;
    private Scrollbar scrollbar;
    private ModTextField fieldSearch;
    private boolean hoverSearchButton;
    private NameHistory nameHistory;
    private boolean lastSearchNotFound;
    private String forceNameSearch;
    
    public GuiChatNameHistory(final String defaultText) {
        super(defaultText);
        this.getClass();
        this.scrollbar = new Scrollbar(11);
        this.hoverSearchButton = false;
        this.lastSearchNotFound = false;
        this.forceNameSearch = null;
    }
    
    public GuiChatNameHistory(final String defaultText, final String username) {
        super(defaultText);
        this.getClass();
        this.scrollbar = new Scrollbar(11);
        this.hoverSearchButton = false;
        this.lastSearchNotFound = false;
        this.forceNameSearch = null;
        this.forceNameSearch = username;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final double buttonWidth = draw.getStringWidth(LanguageManager.translate("button_search")) * 0.7 + 4.0;
        final int componentId = 0;
        final FontRenderer fontRenderer = LabyModCore.getMinecraft().getFontRenderer();
        final int x = 0;
        final int y = 0;
        this.getClass();
        final int par5Width = (int)(160.0 - buttonWidth);
        this.getClass();
        (this.fieldSearch = new ModTextField(0, fontRenderer, 0, 0, par5Width, 16)).setEnableBackgroundDrawing(false);
        this.fieldSearch.setMaxStringLength(16);
        this.fieldSearch.setFocused(true);
        final Scrollbar scrollbar = this.scrollbar;
        this.getClass();
        scrollbar.setSpeed(11);
        this.scrollbar.init();
        if (this.forceNameSearch != null) {
            this.fieldSearch.setText(this.forceNameSearch);
            this.fieldSearch.setFocused(true);
            this.fieldSearch.setCursorPositionEnd();
            this.doNameSearch(this.forceNameSearch);
            this.forceNameSearch = null;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final int n = GuiChatNameHistory.width - 2;
        this.getClass();
        final int x = n - 160;
        final int n2 = GuiChatNameHistory.height - 24;
        this.getClass();
        double y = n2 - 24;
        y -= this.pushSearchBarUp();
        final DrawUtils drawUtils = draw;
        final double left = x;
        final double top = y;
        final int n3 = x;
        this.getClass();
        final double right = n3 + 160;
        final double n4 = y;
        this.getClass();
        DrawUtils.drawRect(left, top, right, n4 + 16.0, Integer.MIN_VALUE);
        final int paddingField = 1;
        final int fieldColor = this.fieldSearch.isFocused() ? Integer.MIN_VALUE : ModColor.toRGB(12, 12, 12, 180);
        final DrawUtils drawUtils2 = draw;
        final double left2 = x + 1;
        final double top2 = y + 1.0;
        final double right2 = x + this.fieldSearch.getWidth() - 1;
        final double n5 = y;
        this.getClass();
        DrawUtils.drawRect(left2, top2, right2, n5 + 16.0 - 1.0, fieldColor);
        this.fieldSearch.xPosition = x + 3;
        this.fieldSearch.yPosition = (int)(y + 4.0);
        this.fieldSearch.drawTextBox();
        final int searchX = x + this.fieldSearch.getWidth();
        final double searchY = y;
        final int searchWidth = GuiChatNameHistory.width - 2 - searchX;
        this.hoverSearchButton = (mouseX > searchX && searchX < searchX + searchWidth && mouseY > searchY && mouseY < searchY + 16.0);
        final int buttonColor = this.fieldSearch.getText().isEmpty() ? ModColor.toRGB(100, 100, 100, 200) : (this.hoverSearchButton ? ModColor.toRGB(100, 200, 100, 200) : Integer.MAX_VALUE);
        final int paddingButton = 1;
        final DrawUtils drawUtils3 = draw;
        final double left3 = searchX + 1;
        final double top3 = searchY + 1.0;
        final double right3 = searchX + searchWidth - 1;
        final double n6 = searchY;
        this.getClass();
        DrawUtils.drawRect(left3, top3, right3, n6 + 16.0 - 1.0, buttonColor);
        draw.drawCenteredString(LanguageManager.translate("button_search"), searchX + searchWidth / 2.0, searchY + 5.0, 0.7);
        final double topY = y;
        if (this.nameHistory == null && this.lastSearchNotFound) {
            final double n7 = y;
            this.getClass();
            y = n7 + 16.0;
            final DrawUtils drawUtils4 = draw;
            final double left4 = x;
            final double top4 = y + 1.0;
            final int n8 = x;
            this.getClass();
            DrawUtils.drawRect(left4, top4, n8 + 160, y + 14.0, Integer.MIN_VALUE);
            draw.drawString(String.valueOf(ModColor.cl('c')) + LanguageManager.translate("search_not_found"), x + 3, y + 4.0);
        }
        else if (this.nameHistory != null && this.nameHistory.getChanges() != null) {
            final double n9 = y;
            this.getClass();
            y = n9 + 16.0;
            y += this.scrollbar.getScrollY();
            int index = 0;
            UUIDFetcher[] changes;
            for (int length = (changes = this.nameHistory.getChanges()).length, i = 0; i < length; ++i) {
                final UUIDFetcher fetchedName = changes[i];
                final double n10 = y;
                final double n11 = topY;
                this.getClass();
                if (n10 >= n11 + 16.0 && y < GuiChatNameHistory.height - 24 && this.nameHistory != null && this.nameHistory.getChanges() != null && fetchedName != null) {
                    final boolean latestName = index == 0;
                    final boolean originalName = index == this.nameHistory.getChanges().length - 1;
                    final DrawUtils drawUtils5 = draw;
                    final double left5 = x;
                    final double top5 = y + 1.0;
                    final int n12 = x;
                    this.getClass();
                    final double right4 = n12 + 160;
                    final double n13 = y;
                    this.getClass();
                    DrawUtils.drawRect(left5, top5, right4, n13 + 11.0, Integer.MIN_VALUE);
                    final String nameColor = ModColor.cl(latestName ? 'a' : (originalName ? 'b' : '7'));
                    draw.drawString(String.valueOf(nameColor) + fetchedName.name, x + 2, y + ((latestName || originalName) ? 2 : 3), (latestName || originalName) ? 1.0 : 0.7);
                    final String timeDiff = (fetchedName.changedToAt == 0L) ? LanguageManager.translate("original_name") : ModUtils.getTimeDiff(fetchedName.changedToAt);
                    final DrawUtils drawUtils6 = draw;
                    final String string = String.valueOf(ModColor.cl('e')) + timeDiff;
                    final int n14 = x;
                    this.getClass();
                    drawUtils6.drawRightString(string, n14 + 160 - 4, y + 4.0, 0.6);
                    if (this.nameHistory != null && this.nameHistory.getChanges() != null && this.nameHistory.getChanges().length > 1) {
                        final int color = originalName ? ModColor.toRGB(100, 200, 200, 250) : (latestName ? ModColor.toRGB(100, 200, 100, 250) : ModColor.toRGB(180, 180, 180, 100));
                        final DrawUtils drawUtils7 = draw;
                        final double left6 = x - 3;
                        final double n15 = y;
                        int n16;
                        if (latestName) {
                            this.getClass();
                            n16 = 5;
                        }
                        else {
                            n16 = 0;
                        }
                        final double top6 = n15 + n16;
                        final double right5 = x - 3 + 1;
                        final double n17 = y;
                        int n18;
                        if (originalName) {
                            this.getClass();
                            n18 = 6;
                        }
                        else {
                            this.getClass();
                            n18 = 11;
                        }
                        DrawUtils.drawRect(left6, top6, right5, n17 + n18, color);
                        final DrawUtils drawUtils8 = draw;
                        final double left7 = x - 3 + 1;
                        final double n19 = y;
                        this.getClass();
                        final double top7 = n19 + 5.0;
                        final double right6 = x;
                        final double n20 = y;
                        this.getClass();
                        DrawUtils.drawRect(left7, top7, right6, n20 + 5.0 + 1.0, color);
                    }
                }
                final double n21 = y;
                this.getClass();
                y = n21 + 11.0;
                ++index;
            }
        }
        final Scrollbar scrollbar = this.scrollbar;
        final int n22 = x;
        this.getClass();
        final double left8 = n22 + 160 - 3;
        final double n23 = topY;
        this.getClass();
        final double top8 = n23 + 16.0;
        final int n24 = x;
        this.getClass();
        scrollbar.setPosition(left8, top8, n24 + 160, GuiChatNameHistory.height - 24);
        this.scrollbar.draw();
        draw.drawString(LanguageManager.translate("ingame_chat_tab_namehistory"), x, topY - 10.0);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final int n = GuiChatNameHistory.width - 2;
        this.getClass();
        final int x = n - 160;
        final int n2 = GuiChatNameHistory.height - 24;
        this.getClass();
        int y = n2 - 24;
        y -= this.pushSearchBarUp();
        this.fieldSearch.mouseClicked(mouseX, mouseY, mouseButton);
        Label_0130: {
            if (mouseX > x && mouseX < x + this.fieldSearch.getWidth() && mouseY > y) {
                final int n3 = y;
                this.getClass();
                if (mouseY < n3 + 16) {
                    this.fieldSearch.setFocused(true);
                    break Label_0130;
                }
            }
            this.fieldSearch.setFocused(false);
        }
        if (this.hoverSearchButton && !this.fieldSearch.getText().isEmpty()) {
            this.doNameSearch(this.fieldSearch.getText());
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.fieldSearch.isFocused()) {
            this.fieldSearch.textboxKeyTyped(typedChar, keyCode);
            if (keyCode == 28) {
                this.doNameSearch(this.fieldSearch.getText());
            }
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        final Scrollbar scrollbar = this.scrollbar;
        final double scrollY = this.scrollbar.getScrollY();
        this.getClass();
        final int n = (int)(scrollY / 11.0);
        this.getClass();
        scrollbar.setScrollY(n * 11);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
    
    @Override
    public void updateScreen() {
        if (this.fieldSearch.isFocused()) {
            this.fieldSearch.updateCursorCounter();
        }
        else {
            super.updateScreen();
        }
    }
    
    private int pushSearchBarUp() {
        if (this.nameHistory != null) {
            final int length = this.nameHistory.getChanges().length;
            this.getClass();
            int n2;
            if (length > 10) {
                this.getClass();
                final int n = 10;
                this.getClass();
                n2 = 110;
            }
            else {
                final int length2 = this.nameHistory.getChanges().length;
                this.getClass();
                n2 = length2 * 11;
            }
            return n2;
        }
        if (this.lastSearchNotFound) {
            return 14;
        }
        return 0;
    }
    
    private void doNameSearch(final String username) {
        NameHistoryUtil.getNameHistory(username, new Consumer<NameHistory>() {
            @Override
            public void accept(final NameHistory nameHistory) {
                GuiChatNameHistory.access$0(GuiChatNameHistory.this, nameHistory);
                GuiChatNameHistory.access$1(GuiChatNameHistory.this, nameHistory == null);
                if (nameHistory == null) {
                    GuiChatNameHistory.this.scrollbar.update(0);
                }
                else {
                    GuiChatNameHistory.this.scrollbar.update(nameHistory.getChanges().length);
                    GuiChatNameHistory.this.fieldSearch.setFocused(false);
                }
            }
        });
    }
    
    static /* synthetic */ void access$0(final GuiChatNameHistory guiChatNameHistory, final NameHistory nameHistory) {
        guiChatNameHistory.nameHistory = nameHistory;
    }
    
    static /* synthetic */ void access$1(final GuiChatNameHistory guiChatNameHistory, final boolean lastSearchNotFound) {
        guiChatNameHistory.lastSearchNotFound = lastSearchNotFound;
    }
}
