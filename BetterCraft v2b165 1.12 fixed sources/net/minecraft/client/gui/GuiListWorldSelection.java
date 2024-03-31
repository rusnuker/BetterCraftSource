// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import java.util.Collections;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.resources.I18n;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class GuiListWorldSelection extends GuiListExtended
{
    private static final Logger LOGGER;
    private final GuiWorldSelection worldSelectionObj;
    private final List<GuiListWorldSelectionEntry> entries;
    private int selectedIdx;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public GuiListWorldSelection(final GuiWorldSelection p_i46590_1_, final Minecraft clientIn, final int p_i46590_3_, final int p_i46590_4_, final int p_i46590_5_, final int p_i46590_6_, final int p_i46590_7_) {
        super(clientIn, p_i46590_3_, p_i46590_4_, p_i46590_5_, p_i46590_6_, p_i46590_7_);
        this.entries = (List<GuiListWorldSelectionEntry>)Lists.newArrayList();
        this.selectedIdx = -1;
        this.worldSelectionObj = p_i46590_1_;
        this.refreshList();
    }
    
    public void refreshList() {
        final ISaveFormat isaveformat = this.mc.getSaveLoader();
        List<WorldSummary> list;
        try {
            list = isaveformat.getSaveList();
        }
        catch (final AnvilConverterException anvilconverterexception) {
            GuiListWorldSelection.LOGGER.error("Couldn't load level list", anvilconverterexception);
            this.mc.displayGuiScreen(new GuiErrorScreen(I18n.format("selectWorld.unable_to_load", new Object[0]), anvilconverterexception.getMessage()));
            return;
        }
        Collections.sort(list);
        for (final WorldSummary worldsummary : list) {
            this.entries.add(new GuiListWorldSelectionEntry(this, worldsummary, this.mc.getSaveLoader()));
        }
    }
    
    @Override
    public GuiListWorldSelectionEntry getListEntry(final int index) {
        return this.entries.get(index);
    }
    
    @Override
    protected int getSize() {
        return this.entries.size();
    }
    
    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 20;
    }
    
    @Override
    public int getListWidth() {
        return super.getListWidth() + 50;
    }
    
    public void selectWorld(final int idx) {
        this.selectedIdx = idx;
        this.worldSelectionObj.selectWorld(this.getSelectedWorld());
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return slotIndex == this.selectedIdx;
    }
    
    @Nullable
    public GuiListWorldSelectionEntry getSelectedWorld() {
        return (this.selectedIdx >= 0 && this.selectedIdx < this.getSize()) ? this.getListEntry(this.selectedIdx) : null;
    }
    
    public GuiWorldSelection getGuiWorldSelection() {
        return this.worldSelectionObj;
    }
}
