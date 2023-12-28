// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.util.RealmsTextureManager;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import org.lwjgl.opengl.GL11;
import net.minecraft.realms.Tezzelator;
import net.minecraft.realms.RealmsSharedConstants;
import org.lwjgl.input.Mouse;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.gui.RealmsConstants;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.Collection;
import java.util.ArrayList;
import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.realms.RealmsButton;
import java.util.List;
import com.mojang.realmsclient.dto.WorldTemplate;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsSelectWorldTemplateScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static final String LINK_ICON = "realms:textures/gui/realms/link_icons.png";
    private static final String TRAILER_ICON = "realms:textures/gui/realms/trailer_icons.png";
    private static final String SLOT_FRAME_LOCATION = "realms:textures/gui/realms/slot_frame.png";
    private final RealmsScreenWithCallback<WorldTemplate> lastScreen;
    private WorldTemplate selectedWorldTemplate;
    private final List<WorldTemplate> templates;
    private WorldTemplateSelectionList worldTemplateSelectionList;
    private int selectedTemplate;
    private String title;
    private static final int BUTTON_BACK_ID = 0;
    private static final int BUTTON_SELECT_ID = 1;
    private RealmsButton selectButton;
    private String toolTip;
    private String currentLink;
    private final RealmsServer.WorldType worldType;
    private int clicks;
    private String warning;
    private String warningURL;
    private boolean displayWarning;
    private boolean hoverWarning;
    private boolean prepopulated;
    private WorldTemplatePaginatedList paginatedList;
    private boolean loading;
    private boolean stopLoadingTemplates;
    
    public RealmsSelectWorldTemplateScreen(final RealmsScreenWithCallback<WorldTemplate> lastScreen, final WorldTemplate selectedWorldTemplate, final RealmsServer.WorldType worldType) {
        this.templates = new ArrayList<WorldTemplate>();
        this.selectedTemplate = -1;
        this.lastScreen = lastScreen;
        this.selectedWorldTemplate = selectedWorldTemplate;
        this.worldType = worldType;
        this.title = RealmsScreen.getLocalizedString((this.worldType == RealmsServer.WorldType.MINIGAME) ? "mco.template.title.minigame" : "mco.template.title");
        if (this.paginatedList == null) {
            this.paginatedList = new WorldTemplatePaginatedList();
            this.paginatedList.size = 10;
        }
        if (this.paginatedList.size == 0) {
            this.paginatedList.size = 10;
        }
    }
    
    public RealmsSelectWorldTemplateScreen(final RealmsScreenWithCallback<WorldTemplate> lastScreen, final WorldTemplate selectedWorldTemplate, final RealmsServer.WorldType worldType, final WorldTemplatePaginatedList list) {
        this(lastScreen, selectedWorldTemplate, worldType);
        this.prepopulated = true;
        this.templates.addAll((list == null) ? new ArrayList<WorldTemplate>() : list.templates);
        this.paginatedList = list;
        if (this.paginatedList.size == 0) {
            this.paginatedList.size = 10;
        }
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setWarning(final String string) {
        this.warning = string;
        this.displayWarning = true;
    }
    
    public void setWarningURL(final String string) {
        this.warningURL = string;
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        if (this.hoverWarning && this.warningURL != null) {
            RealmsUtil.browseTo("https://beta.minecraft.net/realms/adventure-maps-in-1-9");
        }
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.worldTemplateSelectionList.mouseEvent();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.worldTemplateSelectionList = new WorldTemplateSelectionList();
        if (!this.prepopulated && this.templates.isEmpty()) {
            this.paginatedList.page = 0;
            this.paginatedList.size = 10;
            this.fetchMoreTemplatesAsync();
        }
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 + 6, this.height() - 32, 153, 20, RealmsScreen.getLocalizedString((this.worldType == RealmsServer.WorldType.MINIGAME) ? "gui.cancel" : "gui.back")));
        this.buttonsAdd(this.selectButton = RealmsScreen.newButton(1, this.width() / 2 - 154, this.height() - 32, 153, 20, RealmsScreen.getLocalizedString("mco.template.button.select")));
        this.selectButton.active(false);
    }
    
    @Override
    public void tick() {
        super.tick();
        --this.clicks;
        if (this.clicks < 0) {
            this.clicks = 0;
        }
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 0: {
                this.backButtonClicked();
                break;
            }
            case 1: {
                this.selectTemplate();
                break;
            }
            default: {}
        }
    }
    
    @Override
    public void keyPressed(final char eventCharacter, final int eventKey) {
        switch (eventKey) {
            case 1: {
                this.backButtonClicked();
                break;
            }
            case 200: {
                if (this.selectedTemplate != -1) {
                    final int theIndex = this.selectedTemplate;
                    if (theIndex == 0) {
                        this.worldTemplateSelectionList.scroll(0 - this.worldTemplateSelectionList.getScroll());
                        return;
                    }
                    final int newIndex = theIndex - 1;
                    if (newIndex > -1) {
                        this.selectedTemplate = newIndex;
                        final int maxScroll = Math.max(0, this.worldTemplateSelectionList.getMaxPosition() - (this.height() - 40 - (this.displayWarning ? RealmsConstants.row(1) : 32) - 4));
                        final int maxItemsInView = (int)Math.floor((this.height() - 40 - (this.displayWarning ? RealmsConstants.row(1) : 32)) / 46);
                        final int scroll = this.worldTemplateSelectionList.getScroll();
                        final int hiddenItems = (int)Math.ceil(scroll / 46.0f);
                        final int scrollPerItem = maxScroll / this.templates.size();
                        final int positionNeeded = scrollPerItem * newIndex;
                        final int proposedScroll = positionNeeded - this.worldTemplateSelectionList.getScroll();
                        if (newIndex < hiddenItems || newIndex > hiddenItems + maxItemsInView) {
                            this.worldTemplateSelectionList.scroll(proposedScroll);
                        }
                        return;
                    }
                }
                this.selectedTemplate = 0;
                this.worldTemplateSelectionList.scroll(0 - this.worldTemplateSelectionList.getScroll());
                break;
            }
            case 208: {
                if (this.selectedTemplate != -1) {
                    final int theIndex = this.selectedTemplate;
                    final int maxScroll2 = Math.max(0, this.worldTemplateSelectionList.getMaxPosition() - (this.height() - 40 - (this.displayWarning ? RealmsConstants.row(1) : 32)));
                    if (theIndex == this.templates.size() - 1) {
                        this.worldTemplateSelectionList.scroll(maxScroll2 - this.worldTemplateSelectionList.getScroll() + 46);
                        return;
                    }
                    final int newIndex2 = theIndex + 1;
                    if (newIndex2 == this.templates.size() - 1) {
                        this.selectedTemplate = newIndex2;
                        this.worldTemplateSelectionList.scroll(maxScroll2 - this.worldTemplateSelectionList.getScroll() + 46);
                        return;
                    }
                    if (newIndex2 < this.templates.size()) {
                        this.selectedTemplate = newIndex2;
                        final int maxItemsInView = (int)Math.floor((this.height() - 40 - (this.displayWarning ? RealmsConstants.row(1) : 32)) / 46);
                        final int scroll = this.worldTemplateSelectionList.getScroll();
                        final int hiddenItems = (int)Math.ceil(scroll / 46.0f);
                        final int scrollPerItem = maxScroll2 / this.templates.size();
                        final int positionNeeded = scrollPerItem * newIndex2;
                        int proposedScroll = positionNeeded - this.worldTemplateSelectionList.getScroll();
                        if (proposedScroll > 0) {
                            proposedScroll += scrollPerItem;
                        }
                        if (newIndex2 < hiddenItems || newIndex2 >= hiddenItems + maxItemsInView) {
                            this.worldTemplateSelectionList.scroll(proposedScroll);
                        }
                        return;
                    }
                }
                this.selectedTemplate = 0;
                this.worldTemplateSelectionList.scroll(-(this.worldTemplateSelectionList.getItemCount() * 46));
                break;
            }
            case 28:
            case 156: {
                this.selectTemplate();
                break;
            }
        }
    }
    
    private void backButtonClicked() {
        this.lastScreen.callback(null);
        Realms.setScreen(this.lastScreen);
    }
    
    private void selectTemplate() {
        if (this.selectedTemplate >= 0 && this.selectedTemplate < this.templates.size()) {
            final WorldTemplate template = this.templates.get(this.selectedTemplate);
            this.lastScreen.callback(template);
        }
    }
    
    private void fetchMoreTemplatesAsync() {
        if (!this.loading && !this.stopLoadingTemplates) {
            this.loading = true;
            new Thread("realms-template-fetcher") {
                @Override
                public void run() {
                    try {
                        final RealmsClient client = RealmsClient.createRealmsClient();
                        RealmsSelectWorldTemplateScreen.this.paginatedList = client.fetchWorldTemplates(RealmsSelectWorldTemplateScreen.this.paginatedList.page + 1, RealmsSelectWorldTemplateScreen.this.paginatedList.size, RealmsSelectWorldTemplateScreen.this.worldType);
                        RealmsSelectWorldTemplateScreen.this.templates.addAll(RealmsSelectWorldTemplateScreen.this.paginatedList.templates);
                        if (RealmsSelectWorldTemplateScreen.this.paginatedList.templates.size() == 0) {
                            RealmsSelectWorldTemplateScreen.this.stopLoadingTemplates = true;
                        }
                    }
                    catch (final RealmsServiceException ignored) {
                        RealmsSelectWorldTemplateScreen.LOGGER.error("Couldn't fetch templates");
                        RealmsSelectWorldTemplateScreen.this.stopLoadingTemplates = true;
                    }
                    finally {
                        RealmsSelectWorldTemplateScreen.this.loading = false;
                    }
                }
            }.start();
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.toolTip = null;
        this.currentLink = null;
        this.hoverWarning = false;
        if (!this.paginatedList.isLastPage()) {
            this.fetchMoreTemplatesAsync();
        }
        this.renderBackground();
        this.worldTemplateSelectionList.render(xm, ym, a);
        this.drawCenteredString(this.title, this.width() / 2, 13, 16777215);
        if (this.displayWarning) {
            final String[] lines = this.warning.split("\\\\n");
            for (int index = 0; index < lines.length; ++index) {
                final int fontWidth = this.fontWidth(lines[index]);
                final int offsetX = this.width() / 2 - fontWidth / 2;
                final int offsetY = RealmsConstants.row(-1 + index);
                if (xm >= offsetX && xm <= offsetX + fontWidth && ym >= offsetY && ym <= offsetY + this.fontLineHeight()) {
                    this.hoverWarning = true;
                }
            }
            for (int index = 0; index < lines.length; ++index) {
                String line = lines[index];
                int warningColor = 10526880;
                if (this.warningURL != null) {
                    if (this.hoverWarning) {
                        warningColor = 7107012;
                        line = "§n" + line;
                    }
                    else {
                        warningColor = 3368635;
                    }
                }
                this.drawCenteredString(line, this.width() / 2, RealmsConstants.row(-1 + index), warningColor);
            }
        }
        super.render(xm, ym, a);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
    }
    
    protected void renderMousehoverTooltip(final String msg, final int x, final int y) {
        if (msg == null) {
            return;
        }
        final int rx = x + 12;
        final int ry = y - 12;
        final int width = this.fontWidth(msg);
        this.fillGradient(rx - 3, ry - 3, rx + width + 3, ry + 8 + 3, -1073741824, -1073741824);
        this.fontDrawShadow(msg, rx, ry, 16777215);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    private class WorldTemplateSelectionList extends RealmsClickableScrolledSelectionList
    {
        public WorldTemplateSelectionList() {
            super(RealmsSelectWorldTemplateScreen.this.width(), RealmsSelectWorldTemplateScreen.this.height(), RealmsSelectWorldTemplateScreen.this.displayWarning ? RealmsConstants.row(1) : 32, RealmsSelectWorldTemplateScreen.this.height() - 40, 46);
        }
        
        @Override
        public int getItemCount() {
            return RealmsSelectWorldTemplateScreen.this.templates.size();
        }
        
        @Override
        public void customMouseEvent(final int y0, final int y1, final int headerHeight, final float yo, final int itemHeight) {
            if (Mouse.isButtonDown(0) && this.ym() >= y0 && this.ym() <= y1) {
                final int x0 = this.width() / 2 - 150;
                final int x2 = this.width();
                final int clickSlotPos = this.ym() - y0 - headerHeight + (int)yo - 4;
                final int slot = clickSlotPos / itemHeight;
                if (this.xm() >= x0 && this.xm() <= x2 && slot >= 0 && clickSlotPos >= 0 && slot < this.getItemCount()) {
                    this.itemClicked(clickSlotPos, slot, this.xm(), this.ym(), this.width());
                    if (slot >= RealmsSelectWorldTemplateScreen.this.templates.size()) {
                        return;
                    }
                    RealmsSelectWorldTemplateScreen.this.selectButton.active(true);
                    RealmsSelectWorldTemplateScreen.this.selectedTemplate = slot;
                    RealmsSelectWorldTemplateScreen.this.selectedWorldTemplate = null;
                    RealmsSelectWorldTemplateScreen.this.clicks += RealmsSharedConstants.TICKS_PER_SECOND / 3 + 1;
                    if (RealmsSelectWorldTemplateScreen.this.clicks >= RealmsSharedConstants.TICKS_PER_SECOND / 2) {
                        RealmsSelectWorldTemplateScreen.this.selectTemplate();
                    }
                }
            }
        }
        
        @Override
        public boolean isSelectedItem(final int item) {
            if (RealmsSelectWorldTemplateScreen.this.templates.size() == 0) {
                return false;
            }
            if (item >= RealmsSelectWorldTemplateScreen.this.templates.size()) {
                return false;
            }
            if (RealmsSelectWorldTemplateScreen.this.selectedWorldTemplate != null) {
                final boolean same = RealmsSelectWorldTemplateScreen.this.selectedWorldTemplate.name.equals(RealmsSelectWorldTemplateScreen.this.templates.get(item).name);
                if (same) {
                    RealmsSelectWorldTemplateScreen.this.selectedTemplate = item;
                }
                return same;
            }
            return item == RealmsSelectWorldTemplateScreen.this.selectedTemplate;
        }
        
        @Override
        public void itemClicked(final int clickSlotPos, final int slot, final int xm, final int ym, final int width) {
            if (slot >= RealmsSelectWorldTemplateScreen.this.templates.size()) {
                return;
            }
            if (RealmsSelectWorldTemplateScreen.this.currentLink != null) {
                RealmsUtil.browseTo(RealmsSelectWorldTemplateScreen.this.currentLink);
            }
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 46;
        }
        
        @Override
        public void renderBackground() {
            RealmsSelectWorldTemplateScreen.this.renderBackground();
        }
        
        @Override
        public void renderItem(final int i, final int x, final int y, final int h, final int mouseX, final int mouseY) {
            if (i < RealmsSelectWorldTemplateScreen.this.templates.size()) {
                this.renderWorldTemplateItem(i, x, y, h);
            }
        }
        
        @Override
        public int getScrollbarPosition() {
            return super.getScrollbarPosition() + 30;
        }
        
        @Override
        public void renderSelected(final int width, final int y, final int h, final Tezzelator t) {
            final int x0 = this.getScrollbarPosition() - 290;
            final int x2 = this.getScrollbarPosition() - 10;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(3553);
            t.begin(7, RealmsDefaultVertexFormat.POSITION_TEX_COLOR);
            t.vertex(x0, y + h + 2, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x2, y + h + 2, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x2, y - 2, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x0, y - 2, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x0 + 1, y + h + 1, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x2 - 1, y + h + 1, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x2 - 1, y - 1, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x0 + 1, y - 1, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            t.end();
            GL11.glEnable(3553);
        }
        
        private void renderWorldTemplateItem(final int i, final int x, final int y, final int h) {
            final WorldTemplate worldTemplate = RealmsSelectWorldTemplateScreen.this.templates.get(i);
            final int textStart = x + 20;
            RealmsSelectWorldTemplateScreen.this.drawString(worldTemplate.name, textStart, y + 2, 16777215);
            RealmsSelectWorldTemplateScreen.this.drawString(worldTemplate.author, textStart, y + 15, 7105644);
            RealmsSelectWorldTemplateScreen.this.drawString(worldTemplate.version, textStart + 227 - RealmsSelectWorldTemplateScreen.this.fontWidth(worldTemplate.version), y + 1, 7105644);
            if (!"".equals(worldTemplate.link) || !"".equals(worldTemplate.trailer) || !"".equals(worldTemplate.recommendedPlayers)) {
                this.drawIcons(textStart - 1, y + 25, this.xm(), this.ym(), worldTemplate.link, worldTemplate.trailer, worldTemplate.recommendedPlayers);
            }
            this.drawImage(x - 25, y + 1, this.xm(), this.ym(), worldTemplate);
        }
        
        private void drawImage(final int x, final int y, final int xm, final int ym, final WorldTemplate worldTemplate) {
            RealmsTextureManager.bindWorldTemplate(worldTemplate.id, worldTemplate.image);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RealmsScreen.blit(x + 1, y + 1, 0.0f, 0.0f, 38, 38, 38.0f, 38.0f);
            RealmsScreen.bind("realms:textures/gui/realms/slot_frame.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RealmsScreen.blit(x, y, 0.0f, 0.0f, 40, 40, 40.0f, 40.0f);
        }
        
        private void drawIcons(final int x, final int y, final int xm, final int ym, final String link, final String trailerLink, final String recommendedPlayers) {
            if (!"".equals(recommendedPlayers)) {
                RealmsSelectWorldTemplateScreen.this.drawString(recommendedPlayers, x, y + 4, 5000268);
            }
            final int offset = "".equals(recommendedPlayers) ? 0 : (RealmsSelectWorldTemplateScreen.this.fontWidth(recommendedPlayers) + 2);
            boolean linkHovered = false;
            boolean trailerHovered = false;
            if (xm >= x + offset && xm <= x + offset + 32 && ym >= y && ym <= y + 15 && ym < RealmsSelectWorldTemplateScreen.this.height() - 15 && ym > 32) {
                if (xm <= x + 15 + offset && xm > offset) {
                    if ("".equals(link)) {
                        trailerHovered = true;
                    }
                    else {
                        linkHovered = true;
                    }
                }
                else if (!"".equals(link)) {
                    trailerHovered = true;
                }
            }
            if (!"".equals(link)) {
                RealmsScreen.bind("realms:textures/gui/realms/link_icons.png");
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPushMatrix();
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                RealmsScreen.blit(x + offset, y, linkHovered ? 15.0f : 0.0f, 0.0f, 15, 15, 30.0f, 15.0f);
                GL11.glPopMatrix();
            }
            if (!"".equals(trailerLink)) {
                RealmsScreen.bind("realms:textures/gui/realms/trailer_icons.png");
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPushMatrix();
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                RealmsScreen.blit(x + offset + ("".equals(link) ? 0 : 17), y, trailerHovered ? 15.0f : 0.0f, 0.0f, 15, 15, 30.0f, 15.0f);
                GL11.glPopMatrix();
            }
            if (linkHovered && !"".equals(link)) {
                RealmsSelectWorldTemplateScreen.this.toolTip = RealmsScreen.getLocalizedString("mco.template.info.tooltip");
                RealmsSelectWorldTemplateScreen.this.currentLink = link;
            }
            else if (trailerHovered && !"".equals(trailerLink)) {
                RealmsSelectWorldTemplateScreen.this.toolTip = RealmsScreen.getLocalizedString("mco.template.trailer.tooltip");
                RealmsSelectWorldTemplateScreen.this.currentLink = trailerLink;
            }
        }
    }
}
