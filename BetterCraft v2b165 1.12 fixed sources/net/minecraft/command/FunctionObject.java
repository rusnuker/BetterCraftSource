// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

import java.util.ArrayDeque;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.advancements.FunctionManager;

public class FunctionObject
{
    private final Entry[] field_193530_b;
    
    public FunctionObject(final Entry[] p_i47600_1_) {
        this.field_193530_b = p_i47600_1_;
    }
    
    public Entry[] func_193528_a() {
        return this.field_193530_b;
    }
    
    public static FunctionObject func_193527_a(final FunctionManager p_193527_0_, final List<String> p_193527_1_) {
        final List<Entry> list = (List<Entry>)Lists.newArrayListWithCapacity(p_193527_1_.size());
        for (String s : p_193527_1_) {
            s = s.trim();
            if (!s.startsWith("#") && !s.isEmpty()) {
                final String[] astring = s.split(" ", 2);
                final String s2 = astring[0];
                if (!p_193527_0_.func_193062_a().getCommands().containsKey(s2)) {
                    if (s2.startsWith("//")) {
                        throw new IllegalArgumentException("Unknown or invalid command '" + s2 + "' (if you intended to make a comment, use '#' not '//')");
                    }
                    if (s2.startsWith("/") && s2.length() > 1) {
                        throw new IllegalArgumentException("Unknown or invalid command '" + s2 + "' (did you mean '" + s2.substring(1) + "'? Do not use a preceding forwards slash.)");
                    }
                    throw new IllegalArgumentException("Unknown or invalid command '" + s2 + "'");
                }
                else {
                    list.add(new CommandEntry(s));
                }
            }
        }
        return new FunctionObject(list.toArray(new Entry[list.size()]));
    }
    
    public static class CacheableFunction
    {
        public static final CacheableFunction field_193519_a;
        @Nullable
        private final ResourceLocation field_193520_b;
        private boolean field_193521_c;
        private FunctionObject field_193522_d;
        
        static {
            field_193519_a = new CacheableFunction((ResourceLocation)null);
        }
        
        public CacheableFunction(@Nullable final ResourceLocation p_i47537_1_) {
            this.field_193520_b = p_i47537_1_;
        }
        
        public CacheableFunction(final FunctionObject p_i47602_1_) {
            this.field_193520_b = null;
            this.field_193522_d = p_i47602_1_;
        }
        
        @Nullable
        public FunctionObject func_193518_a(final FunctionManager p_193518_1_) {
            if (!this.field_193521_c) {
                if (this.field_193520_b != null) {
                    this.field_193522_d = p_193518_1_.func_193058_a(this.field_193520_b);
                }
                this.field_193521_c = true;
            }
            return this.field_193522_d;
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.field_193520_b);
        }
    }
    
    public static class CommandEntry implements Entry
    {
        private final String field_193525_a;
        
        public CommandEntry(final String p_i47534_1_) {
            this.field_193525_a = p_i47534_1_;
        }
        
        @Override
        public void func_194145_a(final FunctionManager p_194145_1_, final ICommandSender p_194145_2_, final ArrayDeque<FunctionManager.QueuedCommand> p_194145_3_, final int p_194145_4_) {
            p_194145_1_.func_193062_a().executeCommand(p_194145_2_, this.field_193525_a);
        }
        
        @Override
        public String toString() {
            return "/" + this.field_193525_a;
        }
    }
    
    public static class FunctionEntry implements Entry
    {
        private final CacheableFunction field_193524_a;
        
        public FunctionEntry(final FunctionObject p_i47601_1_) {
            this.field_193524_a = new CacheableFunction(p_i47601_1_);
        }
        
        @Override
        public void func_194145_a(final FunctionManager p_194145_1_, final ICommandSender p_194145_2_, final ArrayDeque<FunctionManager.QueuedCommand> p_194145_3_, final int p_194145_4_) {
            final FunctionObject functionobject = this.field_193524_a.func_193518_a(p_194145_1_);
            if (functionobject != null) {
                final Entry[] afunctionobject$entry = functionobject.func_193528_a();
                final int i = p_194145_4_ - p_194145_3_.size();
                final int j = Math.min(afunctionobject$entry.length, i);
                for (int k = j - 1; k >= 0; --k) {
                    p_194145_3_.addFirst(new FunctionManager.QueuedCommand(p_194145_1_, p_194145_2_, afunctionobject$entry[k]));
                }
            }
        }
        
        @Override
        public String toString() {
            return "/function " + this.field_193524_a;
        }
    }
    
    public interface Entry
    {
        void func_194145_a(final FunctionManager p0, final ICommandSender p1, final ArrayDeque<FunctionManager.QueuedCommand> p2, final int p3);
    }
}
