// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.nbt;

import com.mcf.davidee.nbtedit.NBTEdit;
import org.apache.logging.log4j.Level;
import net.minecraft.nbt.NBTBase;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.File;

public class SaveStates
{
    private File file;
    private SaveState[] tags;
    
    public SaveStates(final File file) {
        this.file = file;
        this.tags = new SaveState[7];
        for (int i = 0; i < 7; ++i) {
            this.tags[i] = new SaveState("Slot " + (i + 1));
        }
    }
    
    public void read() throws IOException {
        if (this.file.exists() && this.file.canRead()) {
            final NBTTagCompound root = CompressedStreamTools.read(this.file);
            for (int i = 0; i < 7; ++i) {
                final String name = "slot" + (i + 1);
                if (root.hasKey(name)) {
                    this.tags[i].tag = root.getCompoundTag(name);
                }
                if (root.hasKey(String.valueOf(name) + "Name")) {
                    this.tags[i].name = root.getString(String.valueOf(name) + "Name");
                }
            }
        }
    }
    
    public void write() throws IOException {
        final NBTTagCompound root = new NBTTagCompound();
        for (int i = 0; i < 7; ++i) {
            root.setTag("slot" + (i + 1), this.tags[i].tag);
            root.setString("slot" + (i + 1) + "Name", this.tags[i].name);
        }
        CompressedStreamTools.write(root, this.file);
    }
    
    public void save() {
        try {
            this.write();
            NBTEdit.log(Level.TRACE, "NBTEdit saved successfully.");
        }
        catch (final IOException e) {
            NBTEdit.log(Level.WARN, "Unable to write NBTEdit save.");
            NBTEdit.throwing("SaveStates", "save", e);
        }
    }
    
    public void load() {
        try {
            this.read();
            NBTEdit.log(Level.TRACE, "NBTEdit save loaded successfully.");
        }
        catch (final IOException e) {
            NBTEdit.log(Level.WARN, "Unable to read NBTEdit save.");
            NBTEdit.throwing("SaveStates", "load", e);
        }
    }
    
    public SaveState getSaveState(final int index) {
        return this.tags[index];
    }
    
    public static final class SaveState
    {
        public String name;
        public NBTTagCompound tag;
        
        public SaveState(final String name) {
            this.name = name;
            this.tag = new NBTTagCompound();
        }
    }
}
