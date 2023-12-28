// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import java.util.Iterator;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import net.minecraft.command.ICommandManager;
import net.minecraft.world.World;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import net.minecraft.command.ICommandSender;
import java.util.ArrayDeque;
import net.minecraft.command.FunctionObject;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import java.io.File;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ITickable;

public class FunctionManager implements ITickable
{
    private static final Logger field_193067_a;
    private final File field_193068_b;
    private final MinecraftServer field_193069_c;
    private final Map<ResourceLocation, FunctionObject> field_193070_d;
    private String field_193071_e;
    private FunctionObject field_193072_f;
    private final ArrayDeque<QueuedCommand> field_194020_g;
    private boolean field_194021_h;
    private final ICommandSender field_193073_g;
    
    static {
        field_193067_a = LogManager.getLogger();
    }
    
    public FunctionManager(@Nullable final File p_i47517_1_, final MinecraftServer p_i47517_2_) {
        this.field_193070_d = (Map<ResourceLocation, FunctionObject>)Maps.newHashMap();
        this.field_193071_e = "-";
        this.field_194020_g = new ArrayDeque<QueuedCommand>();
        this.field_194021_h = false;
        this.field_193073_g = new ICommandSender() {
            @Override
            public String getName() {
                return FunctionManager.this.field_193071_e;
            }
            
            @Override
            public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
                return permLevel <= 2;
            }
            
            @Override
            public World getEntityWorld() {
                return FunctionManager.this.field_193069_c.worldServers[0];
            }
            
            @Override
            public MinecraftServer getServer() {
                return FunctionManager.this.field_193069_c;
            }
        };
        this.field_193068_b = p_i47517_1_;
        this.field_193069_c = p_i47517_2_;
        this.func_193059_f();
    }
    
    @Nullable
    public FunctionObject func_193058_a(final ResourceLocation p_193058_1_) {
        return this.field_193070_d.get(p_193058_1_);
    }
    
    public ICommandManager func_193062_a() {
        return this.field_193069_c.getCommandManager();
    }
    
    public int func_193065_c() {
        return this.field_193069_c.worldServers[0].getGameRules().getInt("maxCommandChainLength");
    }
    
    public Map<ResourceLocation, FunctionObject> func_193066_d() {
        return this.field_193070_d;
    }
    
    @Override
    public void update() {
        final String s = this.field_193069_c.worldServers[0].getGameRules().getString("gameLoopFunction");
        if (!s.equals(this.field_193071_e)) {
            this.field_193071_e = s;
            this.field_193072_f = this.func_193058_a(new ResourceLocation(s));
        }
        if (this.field_193072_f != null) {
            this.func_194019_a(this.field_193072_f, this.field_193073_g);
        }
    }
    
    public int func_194019_a(final FunctionObject p_194019_1_, final ICommandSender p_194019_2_) {
        final int i = this.func_193065_c();
        if (this.field_194021_h) {
            if (this.field_194020_g.size() < i) {
                this.field_194020_g.addFirst(new QueuedCommand(this, p_194019_2_, new FunctionObject.FunctionEntry(p_194019_1_)));
            }
            return 0;
        }
        int l = 0;
        Label_0186: {
            try {
                this.field_194021_h = true;
                int j = 0;
                final FunctionObject.Entry[] afunctionobject$entry = p_194019_1_.func_193528_a();
                for (int k = afunctionobject$entry.length - 1; k >= 0; --k) {
                    this.field_194020_g.push(new QueuedCommand(this, p_194019_2_, afunctionobject$entry[k]));
                }
                while (!this.field_194020_g.isEmpty()) {
                    this.field_194020_g.removeFirst().func_194222_a(this.field_194020_g, i);
                    if (++j >= i) {
                        l = j;
                        break Label_0186;
                    }
                }
                l = j;
                return l;
            }
            finally {
                this.field_194020_g.clear();
                this.field_194021_h = false;
            }
        }
        this.field_194020_g.clear();
        this.field_194021_h = false;
        return l;
    }
    
    public void func_193059_f() {
        this.field_193070_d.clear();
        this.field_193072_f = null;
        this.field_193071_e = "-";
        this.func_193061_h();
    }
    
    private void func_193061_h() {
        if (this.field_193068_b != null) {
            this.field_193068_b.mkdirs();
            for (final File file1 : FileUtils.listFiles(this.field_193068_b, new String[] { "mcfunction" }, true)) {
                final String s = FilenameUtils.removeExtension(this.field_193068_b.toURI().relativize(file1.toURI()).toString());
                final String[] astring = s.split("/", 2);
                if (astring.length == 2) {
                    final ResourceLocation resourcelocation = new ResourceLocation(astring[0], astring[1]);
                    try {
                        this.field_193070_d.put(resourcelocation, FunctionObject.func_193527_a(this, Files.readLines(file1, StandardCharsets.UTF_8)));
                    }
                    catch (final Throwable throwable) {
                        FunctionManager.field_193067_a.error("Couldn't read custom function " + resourcelocation + " from " + file1, throwable);
                    }
                }
            }
            if (!this.field_193070_d.isEmpty()) {
                FunctionManager.field_193067_a.info("Loaded " + this.field_193070_d.size() + " custom command functions");
            }
        }
    }
    
    public static class QueuedCommand
    {
        private final FunctionManager field_194223_a;
        private final ICommandSender field_194224_b;
        private final FunctionObject.Entry field_194225_c;
        
        public QueuedCommand(final FunctionManager p_i47603_1_, final ICommandSender p_i47603_2_, final FunctionObject.Entry p_i47603_3_) {
            this.field_194223_a = p_i47603_1_;
            this.field_194224_b = p_i47603_2_;
            this.field_194225_c = p_i47603_3_;
        }
        
        public void func_194222_a(final ArrayDeque<QueuedCommand> p_194222_1_, final int p_194222_2_) {
            this.field_194225_c.func_194145_a(this.field_194223_a, this.field_194224_b, p_194222_1_, p_194222_2_);
        }
        
        @Override
        public String toString() {
            return this.field_194225_c.toString();
        }
    }
}
