// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import wdl.WDL;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLAbout extends GuiScreen
{
    private final GuiScreen parent;
    private static final String FORUMS_THREAD = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2520465";
    private static final String COREMOD_GITHUB = "https://github.com/Pokechu22/WorldDownloader";
    private static final String LITEMOD_GITHUB = "https://github.com/uyjulian/LiteModWDL/";
    private TextList list;
    
    public GuiWDLAbout(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, GuiWDLAbout.width / 2 - 155, 18, 150, 20, I18n.format("wdl.gui.about.extensions", new Object[0])));
        this.buttonList.add(new GuiButton(1, GuiWDLAbout.width / 2 + 5, 18, 150, 20, I18n.format("wdl.gui.about.debugInfo", new Object[0])));
        this.buttonList.add(new GuiButton(2, GuiWDLAbout.width / 2 - 100, GuiWDLAbout.height - 29, I18n.format("gui.done", new Object[0])));
        final String wdlVersion = "1.8.9a-beta2";
        final String mcVersion = WDL.getMinecraftVersionInfo();
        (this.list = new TextList(this.mc, GuiWDLAbout.width, GuiWDLAbout.height, 39, 32)).addLine(I18n.format("wdl.gui.about.blurb", new Object[0]));
        this.list.addBlankLine();
        this.list.addLine(I18n.format("wdl.gui.about.version", wdlVersion, mcVersion));
        this.list.addBlankLine();
        final String currentLanguage = WDL.minecraft.getLanguageManager().getCurrentLanguage().toString();
        final String translatorCredit = I18n.format("wdl.translatorCredit", currentLanguage);
        if (translatorCredit != null && !translatorCredit.isEmpty()) {
            this.list.addLine(translatorCredit);
            this.list.addBlankLine();
        }
        this.list.addLinkLine(I18n.format("wdl.gui.about.forumThread", new Object[0]), "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2520465");
        this.list.addBlankLine();
        this.list.addLinkLine(I18n.format("wdl.gui.about.coremodSrc", new Object[0]), "https://github.com/Pokechu22/WorldDownloader");
        this.list.addBlankLine();
        this.list.addLinkLine(I18n.format("wdl.gui.about.litemodSrc", new Object[0]), "https://github.com/uyjulian/LiteModWDL/");
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiWDLExtensions(this));
        }
        else if (button.id == 1) {
            GuiScreen.setClipboardString(WDL.getDebugInfo());
            button.displayString = I18n.format("wdl.gui.about.debugInfo.copied", new Object[0]);
        }
        else if (button.id == 2) {
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
        super.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.about.title", new Object[0]), GuiWDLAbout.width / 2, 2, 16777215);
    }
}
