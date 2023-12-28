// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit;

import java.util.Iterator;
import com.mcf.davidee.nbtedit.nbt.NBTTree;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Level;
import com.mcf.davidee.nbtedit.nbt.SaveStates;
import com.mcf.davidee.nbtedit.nbt.NamedNBT;
import org.apache.logging.log4j.Logger;
import com.mcf.davidee.nbtedit.nbt.NBTNodeSorter;

public class NBTEdit
{
    public static final String MODID = "nbtedit";
    public static final String NAME = "In-game NBTEdit";
    public static final String VERSION = "1.11.2-2.0.2";
    public static final NBTNodeSorter SORTER;
    public static Logger logger;
    public static NamedNBT clipboard;
    public static boolean opOnly;
    public static boolean editOtherPlayers;
    public static NBTEdit instance;
    private SaveStates saves;
    static final String SEP;
    
    static {
        SORTER = new NBTNodeSorter();
        NBTEdit.clipboard = null;
        NBTEdit.opOnly = true;
        NBTEdit.editOtherPlayers = false;
        NBTEdit.instance = new NBTEdit();
        SEP = System.getProperty("line.separator");
    }
    
    public static void log(final Level l, final String s) {
        NBTEdit.logger.log(l, s);
    }
    
    public static void throwing(final String cls, final String mthd, final Throwable thr) {
        NBTEdit.logger.warn("class: " + cls + " method: " + mthd, thr);
    }
    
    public static void logTag(final NBTTagCompound tag) {
        final NBTTree tree = new NBTTree(tag);
        String sb = "";
        for (final String s : tree.toStrings()) {
            sb = String.valueOf(sb) + NBTEdit.SEP + "\t\t\t" + s;
        }
        log(Level.TRACE, sb);
    }
    
    public static SaveStates getSaveStates() {
        return NBTEdit.instance.saves;
    }
}
