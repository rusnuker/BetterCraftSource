// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.views;

import net.minecraft.client.gui.GuiScreen;
import net.labymod.gui.elements.GuiTextboxPrompt;
import net.labymod.utils.Consumer;
import net.minecraft.client.Minecraft;
import net.labymod.utils.DrawUtils;
import net.labymod.main.LabyMod;
import net.labymod.addons.resourcepacks24.gui.elements.basement.PackElement;
import net.labymod.addons.resourcepacks24.api.Resourcepacks24Api;
import net.labymod.addons.resourcepacks24.api.util.enums.EnumFeedType;
import net.labymod.addons.resourcepacks24.api.util.enums.EnumResourcepackType;
import net.labymod.main.ModTextures;
import net.labymod.utils.texture.DynamicTextureManager;
import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;
import net.labymod.addons.resourcepacks24.api.model.Pack;
import net.labymod.addons.resourcepacks24.api.util.interfaces.ActionResponse;
import net.labymod.addons.resourcepacks24.gui.elements.OnlinePackElement;
import net.labymod.gui.elements.GuiImageButton;
import net.minecraft.util.ResourceLocation;

public class OnlineView extends View
{
    public static final ResourceLocation TEXTURE_ONLINE;
    private GuiImageButton buttonSearch;
    private GuiImageButton buttonSearchRemove;
    private GuiImageButton buttonNextPage;
    private GuiImageButton buttonPreviousPage;
    private GuiImageButton buttonLibrary;
    private AvailableLibrary selectedLibrary;
    private int page;
    private boolean paginator;
    private String query;
    private OnlinePackElement[] onlineRepository;
    private final ActionResponse<Pack[]> actionResponse;
    
    static {
        TEXTURE_ONLINE = new ResourceLocation("resourcepacks24/textures/online.png");
    }
    
    public OnlineView(final GuiResourcepacks24 gui) {
        super(gui, "Online library");
        this.selectedLibrary = AvailableLibrary.TRENDING;
        this.onlineRepository = new OnlinePackElement[0];
        final DynamicTextureManager dynamicIconManager = gui.getResourcepacks24().getRp24Api().getDynamicIconManager();
        this.actionResponse = new ActionResponse<Pack[]>() {
            @Override
            public void success(final Pack[] packs) {
                final OnlinePackElement[] array = new OnlinePackElement[packs.length];
                for (int i = 0; i < packs.length; ++i) {
                    array[i] = new OnlinePackElement(packs[i], dynamicIconManager, OnlineView.this);
                }
                OnlineView.access$0(OnlineView.this, array);
                gui.initGui();
                OnlineView.this.onLoaded(OnlineView.class);
            }
            
            @Override
            public void failed(final String message) {
                OnlineView.this.throwError(message);
            }
        };
        this.buttonSearch = new GuiImageButton(OnlineView.TEXTURE_ONLINE, 0, 0, 127, 127);
        this.buttonSearchRemove = new GuiImageButton(OnlineView.TEXTURE_ONLINE, 0, 127, 127, 127);
        this.buttonNextPage = new GuiImageButton(ModTextures.MISC_ARROW, 127, 0, 127, 255);
        this.buttonPreviousPage = new GuiImageButton(ModTextures.MISC_ARROW, 0, 0, 127, 255);
        this.buttonLibrary = new GuiImageButton(OnlineView.TEXTURE_ONLINE, 127, 0, 127, 127);
        this.loadPacks(1);
    }
    
    private void loadPacks(final int page) {
        final Resourcepacks24Api api = this.gui.getResourcepacks24().getRp24Api();
        this.paginator = this.selectedLibrary.isPaginator();
        this.page = page;
        this.query = null;
        this.loaded = false;
        this.scrollbar.setScrollY(0.0);
        api.getDynamicIconManager().unloadAll();
        switch (this.selectedLibrary) {
            case NEW: {
                api.resourcepacks(EnumResourcepackType.NEW, this.actionResponse);
                break;
            }
            case PACKS_OF_THE_WEEK: {
                api.feed(EnumFeedType.RESOURCEPACK_OF_THE_WEEK, this.page, this.actionResponse);
                break;
            }
            case PROMOTED: {
                api.feed(EnumFeedType.PROMOTION, this.page, this.actionResponse);
                break;
            }
            default: {
                api.feed(EnumFeedType.TRENDING, this.page, this.actionResponse);
                break;
            }
        }
    }
    
    @Override
    public void init(final double x, final double y, final double width, final double height, final double margin) {
        super.init(x, y, width, height, margin);
        this.buttonSearch.init(x + 2.0, y - 12.0, 10.0);
        this.buttonSearchRemove.init(x + 2.0 + 14.0, y - 12.0, 10.0);
    }
    
    @Override
    public void renderPre(final int mouseX, final int mouseY) {
        super.renderPre(mouseX, mouseY);
        if (!this.loaded) {
            return;
        }
        final double entryWidth = this.width - (this.scrollbar.isHidden() ? 0 : 5);
        final double entryHeight = entryWidth / 7.0;
        double listY = this.y + 3.0 + this.scrollbar.getScrollY();
        final OnlinePackElement[] array = this.onlineRepository;
        for (int index = 0; index < array.length; ++index) {
            this.drawPack(array[index], this.x, listY, entryWidth, entryHeight, mouseX, mouseY);
            listY += entryHeight + 3.0;
        }
        this.postRenderCursorY = listY - this.scrollbar.getScrollY();
    }
    
    @Override
    public void renderPost(final int mouseX, final int mouseY) {
        super.renderPost(mouseX, mouseY);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        this.buttonSearch.draw(mouseX, mouseY);
        if (this.paginator) {
            double paginatorX = this.x + this.width - 10.0;
            final double paginatorY = this.y + this.height + 4.0;
            final String pageString = String.valueOf(this.page);
            final int pageStringWidth = draw.getStringWidth(pageString);
            final int padding = 5;
            this.buttonPreviousPage.setImageAlpha((this.page <= 1) ? 0.2f : 1.0f);
            this.buttonPreviousPage.setEnabled(this.page > 1);
            this.buttonNextPage.setImageAlpha((this.onlineRepository.length == 0) ? 0.2f : 1.0f);
            this.buttonNextPage.setEnabled(this.onlineRepository.length != 0);
            this.buttonNextPage.draw(paginatorX, paginatorY, 10.0, 8.0, mouseX, mouseY);
            draw.drawString(pageString, paginatorX -= pageStringWidth + 5, paginatorY);
            this.buttonPreviousPage.draw(paginatorX -= 15.0, paginatorY, 10.0, 8.0, mouseX, mouseY);
        }
        if (this.query == null) {
            this.buttonLibrary.draw(this.x + 2.0, this.y + this.height + 4.0, 10.0, mouseX, mouseY);
            draw.drawString(this.selectedLibrary.getDisplayName(), this.x + 2.0 + 13.0, this.y + this.height + 5.0);
        }
        else {
            final int amount = this.onlineRepository.length;
            final String queryTrimmed = draw.trimStringToWidth(this.query, (int)(this.width / 2.0));
            draw.drawString(String.valueOf(amount) + " result" + ((amount == 1) ? "" : "s") + " for " + queryTrimmed + (queryTrimmed.equals(this.query) ? "" : "..."), this.x + 2.0, this.y + this.height + 4.0);
            this.buttonSearchRemove.draw(mouseX, mouseY);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.buttonSearch.isMouseOver(mouseX, mouseY)) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTextboxPrompt(this.gui, "Search resourcepack online", "Search", "Cancel", "", new Consumer<String>() {
                @Override
                public void accept(final String query) {
                    if (query.replaceAll(" ", "").isEmpty()) {
                        return;
                    }
                    OnlineView.access$2(OnlineView.this, query);
                    OnlineView.this.gui.getResourcepacks24().getRp24Api().search(query, OnlineView.this.actionResponse);
                    OnlineView.access$4(OnlineView.this, false);
                    OnlineView.access$5(OnlineView.this, 1);
                    OnlineView.this.scrollbar.setScrollY(0.0);
                }
            }));
        }
        if (this.buttonPreviousPage.isMouseOver(mouseX, mouseY)) {
            this.loadPacks(this.page - 1);
        }
        if (this.buttonNextPage.isMouseOver(mouseX, mouseY)) {
            this.loadPacks(this.page + 1);
        }
        if (this.buttonLibrary.isMouseOver(mouseX, mouseY)) {
            this.selectedLibrary = this.selectedLibrary.next();
            this.loadPacks(1);
        }
        if (this.buttonSearchRemove.isMouseOver(mouseX, mouseY)) {
            this.loadPacks(1);
        }
    }
    
    static /* synthetic */ void access$0(final OnlineView onlineView, final OnlinePackElement[] onlineRepository) {
        onlineView.onlineRepository = onlineRepository;
    }
    
    static /* synthetic */ void access$2(final OnlineView onlineView, final String query) {
        onlineView.query = query;
    }
    
    static /* synthetic */ void access$4(final OnlineView onlineView, final boolean paginator) {
        onlineView.paginator = paginator;
    }
    
    static /* synthetic */ void access$5(final OnlineView onlineView, final int page) {
        onlineView.page = page;
    }
    
    private enum AvailableLibrary
    {
        TRENDING("TRENDING", 0, "Trending", true), 
        PROMOTED("PROMOTED", 1, "Featured", false), 
        PACKS_OF_THE_WEEK("PACKS_OF_THE_WEEK", 2, "Packs of the week", true), 
        NEW("NEW", 3, "New packs", false);
        
        private String displayName;
        private boolean paginator;
        
        public AvailableLibrary next() {
            return values()[(this.ordinal() + 1) % values().length];
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
        
        public boolean isPaginator() {
            return this.paginator;
        }
        
        private AvailableLibrary(final String s, final int n, final String displayName, final boolean paginator) {
            this.displayName = displayName;
            this.paginator = paginator;
        }
    }
}
