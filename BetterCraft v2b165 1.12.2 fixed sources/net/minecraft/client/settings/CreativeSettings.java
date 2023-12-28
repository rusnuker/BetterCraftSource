// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.settings;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class CreativeSettings
{
    private static final Logger field_192566_b;
    protected Minecraft field_192565_a;
    private final File field_192567_c;
    private final HotbarSnapshot[] field_192568_d;
    
    static {
        field_192566_b = LogManager.getLogger();
    }
    
    public CreativeSettings(final Minecraft p_i47395_1_, final File p_i47395_2_) {
        this.field_192568_d = new HotbarSnapshot[9];
        this.field_192565_a = p_i47395_1_;
        this.field_192567_c = new File(p_i47395_2_, "hotbar.nbt");
        for (int i = 0; i < 9; ++i) {
            this.field_192568_d[i] = new HotbarSnapshot();
        }
        this.func_192562_a();
    }
    
    public void func_192562_a() {
        try {
            final NBTTagCompound nbttagcompound = CompressedStreamTools.read(this.field_192567_c);
            if (nbttagcompound == null) {
                return;
            }
            for (int i = 0; i < 9; ++i) {
                this.field_192568_d[i].func_192833_a(nbttagcompound.getTagList(String.valueOf(i), 10));
            }
        }
        catch (final Exception exception) {
            CreativeSettings.field_192566_b.error("Failed to load creative mode options", exception);
        }
    }
    
    public void func_192564_b() {
        try {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            for (int i = 0; i < 9; ++i) {
                nbttagcompound.setTag(String.valueOf(i), this.field_192568_d[i].func_192834_a());
            }
            CompressedStreamTools.write(nbttagcompound, this.field_192567_c);
        }
        catch (final Exception exception) {
            CreativeSettings.field_192566_b.error("Failed to save creative mode options", exception);
        }
    }
    
    public HotbarSnapshot func_192563_a(final int p_192563_1_) {
        return this.field_192568_d[p_192563_1_];
    }
}
