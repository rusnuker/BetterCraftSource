// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.client.gui.GuiListExtended;

class TextList extends GuiListExtended
{
    public final int topMargin;
    public final int bottomMargin;
    private List<IGuiListEntry> entries;
    
    public TextList(final Minecraft mc, final int width, final int height, final int topMargin, final int bottomMargin) {
        super(mc, width, height, topMargin, height - bottomMargin, mc.fontRendererObj.FONT_HEIGHT + 1);
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
        this.entries = new ArrayList<IGuiListEntry>();
    }
    
    @Override
    public IGuiListEntry getListEntry(final int index) {
        return this.entries.get(index);
    }
    
    @Override
    protected int getSize() {
        return this.entries.size();
    }
    
    @Override
    protected int getScrollBarX() {
        return this.width - 10;
    }
    
    @Override
    public int getListWidth() {
        return this.width - 18;
    }
    
    public void addLine(final String text) {
        final List<String> lines = Utils.wordWrap(text, this.getListWidth());
        for (final String line : lines) {
            this.entries.add(new TextEntry(this.mc, line, 16777215));
        }
    }
    
    public void addBlankLine() {
        this.entries.add(new TextEntry(this.mc, "", 16777215));
    }
    
    public void addLinkLine(final String text, final String URL) {
        final List<String> lines = Utils.wordWrap(text, this.getListWidth());
        for (final String line : lines) {
            this.entries.add(new LinkEntry(this.mc, line, URL));
        }
    }
    
    public void clearLines() {
        this.entries.clear();
    }
}
