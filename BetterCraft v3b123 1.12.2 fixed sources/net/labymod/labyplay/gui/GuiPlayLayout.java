// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.labymod.utils.DrawUtils;
import net.labymod.labyplay.gui.elements.WinPartyInvites;
import net.labymod.labyplay.gui.elements.WinPartyCreator;
import net.labymod.labyplay.gui.elements.WinCurrentParty;
import net.labymod.gui.elements.Tabs;
import net.labymod.gui.layout.WindowElement;
import java.util.List;
import net.labymod.main.LabyMod;
import net.labymod.labyplay.party.PartySystem;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.gui.layout.WindowLayout;

public class GuiPlayLayout extends WindowLayout
{
    private GuiScreen parentScreen;
    private PartySystem partySystem;
    
    public GuiPlayLayout(final GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.partySystem = LabyMod.getInstance().getLabyPlay().getPartySystem();
    }
    
    @Override
    public void initGui() {
        super.initGui();
    }
    
    @Override
    protected void initLayout(final List<WindowElement<?>> windowElements) {
        this.buttonList.clear();
        Tabs.initMultiplayerTabs(2);
        Tabs.initGuiScreen(this.buttonList, this);
        final int marginWindowX = GuiPlayLayout.width / 4;
        final int marginWindowY = 10;
        final int windowLeft = marginWindowX;
        final int windowRight = GuiPlayLayout.width - marginWindowX;
        final int windowBottom = GuiPlayLayout.height - 10;
        final boolean hasParty = this.partySystem.hasParty();
        final boolean hasInvites = !hasParty && this.partySystem.getPartyInvites().size() != 0;
        final int partyPlaySplit = GuiPlayLayout.height - 80 + 10;
        final int partyInvitesSplit = hasInvites ? (GuiPlayLayout.width / 2) : windowRight;
        if (hasParty) {
            windowElements.add(new WinCurrentParty(this).construct(windowLeft, partyPlaySplit, partyInvitesSplit, windowBottom));
        }
        else {
            windowElements.add(new WinPartyCreator(this).construct(windowLeft, partyPlaySplit, partyInvitesSplit, windowBottom));
        }
        if (hasInvites) {
            windowElements.add(new WinPartyInvites(this).construct(partyInvitesSplit, partyPlaySplit, windowRight, windowBottom));
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawAutoDimmedBackground(0, 41, GuiPlayLayout.width, GuiPlayLayout.height - 80);
        draw.drawOverlayBackground(0, 41);
        draw.drawOverlayBackground(0, GuiPlayLayout.height - 80, GuiPlayLayout.width, GuiPlayLayout.height);
        draw.drawGradientShadowTop(41.0, 0.0, GuiPlayLayout.width);
        draw.drawGradientShadowBottom(GuiPlayLayout.height - 80, 0.0, GuiPlayLayout.width);
        super.drawScreen(mouseX, mouseY, partialTicks);
        Tabs.drawMultiplayerTabs(2, mouseX, mouseY, true, false);
        Tabs.drawParty(mouseX, mouseY, GuiPlayLayout.width);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        Tabs.actionPerformedButton(button);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        Tabs.mouseClickedMultiplayerTabs(2, mouseX, mouseY);
    }
    
    public GuiScreen getParentScreen() {
        return this.parentScreen;
    }
    
    public PartySystem getPartySystem() {
        return this.partySystem;
    }
}
