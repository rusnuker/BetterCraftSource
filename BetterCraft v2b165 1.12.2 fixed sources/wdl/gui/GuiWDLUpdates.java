// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import wdl.update.Release;
import net.minecraft.client.gui.Gui;
import wdl.update.WDLUpdateChecker;
import wdl.WDL;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLUpdates extends GuiScreen
{
    private final GuiScreen parent;
    private static final int TOP_MARGIN = 39;
    private static final int BOTTOM_MARGIN = 32;
    private UpdateList list;
    private GuiButton updateMinecraftVersionButton;
    private GuiButton updateAllowBetasButton;
    
    public GuiWDLUpdates(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        this.list = new UpdateList();
        this.updateMinecraftVersionButton = new GuiButton(0, GuiWDLUpdates.width / 2 - 155, 18, 150, 20, this.getUpdateMinecraftVersionText());
        this.buttonList.add(this.updateMinecraftVersionButton);
        this.updateAllowBetasButton = new GuiButton(1, GuiWDLUpdates.width / 2 + 5, 18, 150, 20, this.getAllowBetasText());
        this.buttonList.add(this.updateAllowBetasButton);
        this.buttonList.add(new GuiButton(100, GuiWDLUpdates.width / 2 - 100, GuiWDLUpdates.height - 29, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.cycleUpdateMinecraftVersion();
        }
        if (button.id == 1) {
            this.cycleAllowBetas();
        }
        if (button.id == 100) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void onGuiClosed() {
        WDL.saveGlobalProps();
    }
    
    private void cycleUpdateMinecraftVersion() {
        final String prop = WDL.globalProps.getProperty("UpdateMinecraftVersion");
        if (prop.equals("client")) {
            WDL.globalProps.setProperty("UpdateMinecraftVersion", "server");
        }
        else if (prop.equals("server")) {
            WDL.globalProps.setProperty("UpdateMinecraftVersion", "any");
        }
        else {
            WDL.globalProps.setProperty("UpdateMinecraftVersion", "client");
        }
        this.updateMinecraftVersionButton.displayString = this.getUpdateMinecraftVersionText();
    }
    
    private void cycleAllowBetas() {
        if (WDL.globalProps.getProperty("UpdateAllowBetas").equals("true")) {
            WDL.globalProps.setProperty("UpdateAllowBetas", "false");
        }
        else {
            WDL.globalProps.setProperty("UpdateAllowBetas", "true");
        }
        this.updateAllowBetasButton.displayString = this.getAllowBetasText();
    }
    
    private String getUpdateMinecraftVersionText() {
        return I18n.format("wdl.gui.updates.updateMinecraftVersion." + WDL.globalProps.getProperty("UpdateMinecraftVersion"), WDL.getMinecraftVersion());
    }
    
    private String getAllowBetasText() {
        return I18n.format("wdl.gui.updates.updateAllowBetas." + WDL.globalProps.getProperty("UpdateAllowBetas"), new Object[0]);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.list.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.list.mouseReleased(mouseX, mouseY, state)) {
            return;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.list == null) {
            return;
        }
        this.list.regenerateVersionList();
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        if (!WDLUpdateChecker.hasFinishedUpdateCheck()) {
            Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.updates.pleaseWait", new Object[0]), GuiWDLUpdates.width / 2, GuiWDLUpdates.height / 2, 16777215);
        }
        else if (WDLUpdateChecker.hasUpdateCheckFailed()) {
            final String reason = WDLUpdateChecker.getUpdateCheckFailReason();
            Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.updates.checkFailed", new Object[0]), GuiWDLUpdates.width / 2, GuiWDLUpdates.height / 2 - this.fontRendererObj.FONT_HEIGHT / 2, 16733525);
            Gui.drawCenteredString(this.fontRendererObj, I18n.format(reason, new Object[0]), GuiWDLUpdates.width / 2, GuiWDLUpdates.height / 2 + this.fontRendererObj.FONT_HEIGHT / 2, 16733525);
        }
        Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.updates.title", new Object[0]), GuiWDLUpdates.width / 2, 8, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.updateMinecraftVersionButton.isMouseOver()) {
            Utils.drawGuiInfoBox(I18n.format("wdl.gui.updates.updateMinecraftVersion.description", WDL.getMinecraftVersion()), GuiWDLUpdates.width, GuiWDLUpdates.height, 32);
        }
        else if (this.updateAllowBetasButton.isMouseOver()) {
            Utils.drawGuiInfoBox(I18n.format("wdl.gui.updates.updateAllowBetas.description", new Object[0]), GuiWDLUpdates.width, GuiWDLUpdates.height, 32);
        }
    }
    
    private String buildVersionInfo(final Release release) {
        String type = "?";
        String mainVersion = "?";
        String supportedVersions = "?";
        if (release.hiddenInfo != null) {
            type = release.hiddenInfo.loader;
            mainVersion = release.hiddenInfo.mainMinecraftVersion;
            final String[] versions = release.hiddenInfo.supportedMinecraftVersions;
            if (versions.length == 1) {
                supportedVersions = I18n.format("wdl.gui.updates.update.version.listSingle", versions[0]);
            }
            else if (versions.length == 2) {
                supportedVersions = I18n.format("wdl.gui.updates.update.version.listDouble", versions[0], versions[1]);
            }
            else {
                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < versions.length; ++i) {
                    if (i == 0) {
                        builder.append(I18n.format("wdl.gui.updates.update.version.listStart", versions[i]));
                    }
                    else if (i == versions.length - 1) {
                        builder.append(I18n.format("wdl.gui.updates.update.version.listEnd", versions[i]));
                    }
                    else {
                        builder.append(I18n.format("wdl.gui.updates.update.version.listMiddle", versions[i]));
                    }
                }
                supportedVersions = builder.toString();
            }
        }
        return I18n.format("wdl.gui.updates.update.version", type, mainVersion, supportedVersions);
    }
    
    private String buildReleaseTitle(final Release release) {
        final String version = release.tag;
        String mcVersion = "?";
        if (release.hiddenInfo != null) {
            mcVersion = release.hiddenInfo.mainMinecraftVersion;
        }
        if (release.prerelease) {
            return I18n.format("wdl.gui.updates.update.title.prerelease", version, mcVersion);
        }
        return I18n.format("wdl.gui.updates.update.title.release", version, mcVersion);
    }
    
    private class UpdateList extends GuiListExtended
    {
        private List<VersionEntry> displayedVersions;
        private Release recomendedRelease;
        final /* synthetic */ GuiWDLUpdates this$0;
        
        public UpdateList() {
            super(GuiWDLUpdates.this.mc, GuiWDLUpdates.width, GuiWDLUpdates.height, 39, GuiWDLUpdates.height - 32, (GuiWDLUpdates.this.fontRendererObj.FONT_HEIGHT + 1) * 6 + 2);
            this.showSelectionBox = true;
        }
        
        private void regenerateVersionList() {
            this.displayedVersions = new ArrayList<VersionEntry>();
            if (WDLUpdateChecker.hasNewVersion()) {
                this.recomendedRelease = WDLUpdateChecker.getRecomendedRelease();
            }
            else {
                this.recomendedRelease = null;
            }
            final List<Release> releases = WDLUpdateChecker.getReleases();
            if (releases == null) {
                return;
            }
            for (final Release release : releases) {
                this.displayedVersions.add(new VersionEntry(release));
            }
        }
        
        @Override
        public VersionEntry getListEntry(final int index) {
            return this.displayedVersions.get(index);
        }
        
        @Override
        protected int getSize() {
            return this.displayedVersions.size();
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            final VersionEntry entry = this.getListEntry(slotIndex);
            return "1.11a-beta1".equals(entry.release.tag);
        }
        
        @Override
        public int getListWidth() {
            return this.width - 30;
        }
        
        @Override
        protected int getScrollBarX() {
            return this.width - 10;
        }
        
        private class VersionEntry implements IGuiListEntry
        {
            private final Release release;
            private String title;
            private String caption;
            private String body1;
            private String body2;
            private String body3;
            private String time;
            private final int fontHeight;
            
            public VersionEntry(final Release release) {
                this.release = release;
                this.fontHeight = UpdateList.this.this$0.fontRendererObj.FONT_HEIGHT + 1;
                this.title = GuiWDLUpdates.this.buildReleaseTitle(release);
                this.caption = GuiWDLUpdates.this.buildVersionInfo(release);
                final List<String> body = Utils.wordWrap(release.textOnlyBody, UpdateList.this.getListWidth());
                this.body1 = ((body.size() >= 1) ? body.get(0) : "");
                this.body2 = ((body.size() >= 2) ? body.get(1) : "");
                this.body3 = ((body.size() >= 3) ? body.get(2) : "");
                this.time = I18n.format("wdl.gui.updates.update.releaseDate", release.date);
            }
            
            @Override
            public void func_192634_a(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected, final float t) {
                String title;
                if (UpdateList.this.isSelected(slotIndex)) {
                    title = I18n.format("wdl.gui.updates.currentVersion", this.title);
                }
                else if (this.release == UpdateList.this.recomendedRelease) {
                    title = I18n.format("wdl.gui.updates.recomendedVersion", this.title);
                }
                else {
                    title = this.title;
                }
                GuiWDLUpdates.this.fontRendererObj.drawString(title, x, y + this.fontHeight * 0, 16777215);
                GuiWDLUpdates.this.fontRendererObj.drawString(this.caption, x, y + this.fontHeight * 1, 8421504);
                GuiWDLUpdates.this.fontRendererObj.drawString(this.body1, x, y + this.fontHeight * 2, 16777215);
                GuiWDLUpdates.this.fontRendererObj.drawString(this.body2, x, y + this.fontHeight * 3, 16777215);
                GuiWDLUpdates.this.fontRendererObj.drawString(this.body3, x, y + this.fontHeight * 4, 16777215);
                GuiWDLUpdates.this.fontRendererObj.drawString(this.time, x, y + this.fontHeight * 5, 8421504);
                if (mouseX > x && mouseX < x + listWidth && mouseY > y && mouseY < y + slotHeight) {
                    Gui.drawRect(x - 2, y - 2, x + listWidth - 3, y + slotHeight + 2, 536870911);
                }
            }
            
            @Override
            public boolean mousePressed(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
                if (relativeY > 0 && relativeY < UpdateList.this.slotHeight) {
                    UpdateList.this.mc.displayGuiScreen(new GuiWDLSingleUpdate(GuiWDLUpdates.this, this.release));
                    UpdateList.this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                    return true;
                }
                return false;
            }
            
            @Override
            public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            }
            
            @Override
            public void func_192633_a(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_, final float p_192633_4_) {
            }
        }
    }
    
    private class GuiWDLSingleUpdate extends GuiScreen
    {
        private final GuiWDLUpdates parent;
        private final Release release;
        private TextList list;
        
        public GuiWDLSingleUpdate(final GuiWDLUpdates parent, final Release releaseToShow) {
            this.parent = parent;
            this.release = releaseToShow;
        }
        
        @Override
        public void initGui() {
            this.buttonList.add(new GuiButton(0, GuiWDLSingleUpdate.width / 2 - 155, 18, 150, 20, I18n.format("wdl.gui.updates.update.viewOnline", new Object[0])));
            if (this.release.hiddenInfo != null) {
                this.buttonList.add(new GuiButton(1, GuiWDLSingleUpdate.width / 2 + 5, 18, 150, 20, I18n.format("wdl.gui.updates.update.viewForumPost", new Object[0])));
            }
            this.buttonList.add(new GuiButton(100, GuiWDLSingleUpdate.width / 2 - 100, GuiWDLSingleUpdate.height - 29, I18n.format("gui.done", new Object[0])));
            (this.list = new TextList(this.mc, GuiWDLSingleUpdate.width, GuiWDLSingleUpdate.height, 39, 32)).addLine(GuiWDLUpdates.this.buildReleaseTitle(this.release));
            this.list.addLine(I18n.format("wdl.gui.updates.update.releaseDate", this.release.date));
            this.list.addLine(GuiWDLUpdates.this.buildVersionInfo(this.release));
            this.list.addBlankLine();
            this.list.addLine(this.release.textOnlyBody);
        }
        
        @Override
        protected void actionPerformed(final GuiButton button) throws IOException {
            if (button.id == 0) {
                Utils.openLink(this.release.URL);
            }
            if (button.id == 1) {
                Utils.openLink(this.release.hiddenInfo.post);
            }
            if (button.id == 100) {
                this.mc.displayGuiScreen(this.parent);
            }
        }
        
        @Override
        protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
            this.list.mouseClicked(mouseX, mouseY, mouseButton);
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        
        @Override
        public void handleMouseInput() throws IOException {
            super.handleMouseInput();
            this.list.handleMouseInput();
        }
        
        @Override
        protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
            if (this.list.mouseReleased(mouseX, mouseY, state)) {
                return;
            }
            super.mouseReleased(mouseX, mouseY, state);
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            if (this.list == null) {
                return;
            }
            this.list.drawScreen(mouseX, mouseY, partialTicks);
            Gui.drawCenteredString(this.fontRendererObj, GuiWDLUpdates.this.buildReleaseTitle(this.release), GuiWDLSingleUpdate.width / 2, 8, 16777215);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
