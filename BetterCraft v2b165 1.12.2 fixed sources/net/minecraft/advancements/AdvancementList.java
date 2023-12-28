// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import javax.annotation.Nullable;
import java.util.function.Function;
import com.google.common.base.Functions;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import java.util.Set;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class AdvancementList
{
    private static final Logger field_192091_a;
    private final Map<ResourceLocation, Advancement> field_192092_b;
    private final Set<Advancement> field_192093_c;
    private final Set<Advancement> field_192094_d;
    private Listener field_192095_e;
    
    static {
        field_192091_a = LogManager.getLogger();
    }
    
    public AdvancementList() {
        this.field_192092_b = (Map<ResourceLocation, Advancement>)Maps.newHashMap();
        this.field_192093_c = (Set<Advancement>)Sets.newLinkedHashSet();
        this.field_192094_d = (Set<Advancement>)Sets.newLinkedHashSet();
    }
    
    private void func_192090_a(final Advancement p_192090_1_) {
        for (final Advancement advancement : p_192090_1_.func_192069_e()) {
            this.func_192090_a(advancement);
        }
        AdvancementList.field_192091_a.info("Forgot about advancement " + p_192090_1_.func_192067_g());
        this.field_192092_b.remove(p_192090_1_.func_192067_g());
        if (p_192090_1_.func_192070_b() == null) {
            this.field_192093_c.remove(p_192090_1_);
            if (this.field_192095_e != null) {
                this.field_192095_e.func_191928_b(p_192090_1_);
            }
        }
        else {
            this.field_192094_d.remove(p_192090_1_);
            if (this.field_192095_e != null) {
                this.field_192095_e.func_191929_d(p_192090_1_);
            }
        }
    }
    
    public void func_192085_a(final Set<ResourceLocation> p_192085_1_) {
        for (final ResourceLocation resourcelocation : p_192085_1_) {
            final Advancement advancement = this.field_192092_b.get(resourcelocation);
            if (advancement == null) {
                AdvancementList.field_192091_a.warn("Told to remove advancement " + resourcelocation + " but I don't know what that is");
            }
            else {
                this.func_192090_a(advancement);
            }
        }
    }
    
    public void func_192083_a(final Map<ResourceLocation, Advancement.Builder> p_192083_1_) {
        final Function<ResourceLocation, Advancement> function = (Function<ResourceLocation, Advancement>)Functions.forMap(this.field_192092_b, (Object)null);
        while (!p_192083_1_.isEmpty()) {
            boolean flag = false;
            Iterator<Map.Entry<ResourceLocation, Advancement.Builder>> iterator = p_192083_1_.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<ResourceLocation, Advancement.Builder> entry = iterator.next();
                final ResourceLocation resourcelocation = entry.getKey();
                final Advancement.Builder advancement$builder = entry.getValue();
                if (advancement$builder.func_192058_a(function)) {
                    final Advancement advancement = advancement$builder.func_192056_a(resourcelocation);
                    this.field_192092_b.put(resourcelocation, advancement);
                    flag = true;
                    iterator.remove();
                    if (advancement.func_192070_b() == null) {
                        this.field_192093_c.add(advancement);
                        if (this.field_192095_e == null) {
                            continue;
                        }
                        this.field_192095_e.func_191931_a(advancement);
                    }
                    else {
                        this.field_192094_d.add(advancement);
                        if (this.field_192095_e == null) {
                            continue;
                        }
                        this.field_192095_e.func_191932_c(advancement);
                    }
                }
            }
            if (!flag) {
                iterator = p_192083_1_.entrySet().iterator();
                while (iterator.hasNext()) {
                    final Map.Entry<ResourceLocation, Advancement.Builder> entry2 = iterator.next();
                    AdvancementList.field_192091_a.error("Couldn't load advancement " + entry2.getKey() + ": " + entry2.getValue());
                }
                break;
            }
        }
        AdvancementList.field_192091_a.info("Loaded " + this.field_192092_b.size() + " advancements");
    }
    
    public void func_192087_a() {
        this.field_192092_b.clear();
        this.field_192093_c.clear();
        this.field_192094_d.clear();
        if (this.field_192095_e != null) {
            this.field_192095_e.func_191930_a();
        }
    }
    
    public Iterable<Advancement> func_192088_b() {
        return this.field_192093_c;
    }
    
    public Iterable<Advancement> func_192089_c() {
        return this.field_192092_b.values();
    }
    
    @Nullable
    public Advancement func_192084_a(final ResourceLocation p_192084_1_) {
        return this.field_192092_b.get(p_192084_1_);
    }
    
    public void func_192086_a(@Nullable final Listener p_192086_1_) {
        this.field_192095_e = p_192086_1_;
        if (p_192086_1_ != null) {
            for (final Advancement advancement : this.field_192093_c) {
                p_192086_1_.func_191931_a(advancement);
            }
            for (final Advancement advancement2 : this.field_192094_d) {
                p_192086_1_.func_191932_c(advancement2);
            }
        }
    }
    
    public interface Listener
    {
        void func_191931_a(final Advancement p0);
        
        void func_191928_b(final Advancement p0);
        
        void func_191932_c(final Advancement p0);
        
        void func_191929_d(final Advancement p0);
        
        void func_191930_a();
    }
}
