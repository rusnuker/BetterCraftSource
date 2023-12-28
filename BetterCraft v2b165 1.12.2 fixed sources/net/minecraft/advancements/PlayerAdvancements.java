// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.Packet;
import java.util.Collection;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Set;
import java.io.File;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class PlayerAdvancements
{
    private static final Logger field_192753_a;
    private static final Gson field_192754_b;
    private static final TypeToken<Map<ResourceLocation, AdvancementProgress>> field_192755_c;
    private final MinecraftServer field_192756_d;
    private final File field_192757_e;
    private final Map<Advancement, AdvancementProgress> field_192758_f;
    private final Set<Advancement> field_192759_g;
    private final Set<Advancement> field_192760_h;
    private final Set<Advancement> field_192761_i;
    private EntityPlayerMP field_192762_j;
    @Nullable
    private Advancement field_194221_k;
    private boolean field_192763_k;
    
    static {
        field_192753_a = LogManager.getLogger();
        field_192754_b = new GsonBuilder().registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.Serializer()).registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).setPrettyPrinting().create();
        field_192755_c = new TypeToken<Map<ResourceLocation, AdvancementProgress>>() {};
    }
    
    public PlayerAdvancements(final MinecraftServer p_i47422_1_, final File p_i47422_2_, final EntityPlayerMP p_i47422_3_) {
        this.field_192758_f = (Map<Advancement, AdvancementProgress>)Maps.newLinkedHashMap();
        this.field_192759_g = (Set<Advancement>)Sets.newLinkedHashSet();
        this.field_192760_h = (Set<Advancement>)Sets.newLinkedHashSet();
        this.field_192761_i = (Set<Advancement>)Sets.newLinkedHashSet();
        this.field_192763_k = true;
        this.field_192756_d = p_i47422_1_;
        this.field_192757_e = p_i47422_2_;
        this.field_192762_j = p_i47422_3_;
        this.func_192740_f();
    }
    
    public void func_192739_a(final EntityPlayerMP p_192739_1_) {
        this.field_192762_j = p_192739_1_;
    }
    
    public void func_192745_a() {
        for (final ICriterionTrigger<?> icriteriontrigger : CriteriaTriggers.func_192120_a()) {
            icriteriontrigger.func_192167_a(this);
        }
    }
    
    public void func_193766_b() {
        this.func_192745_a();
        this.field_192758_f.clear();
        this.field_192759_g.clear();
        this.field_192760_h.clear();
        this.field_192761_i.clear();
        this.field_192763_k = true;
        this.field_194221_k = null;
        this.func_192740_f();
    }
    
    private void func_192751_c() {
        for (final Advancement advancement : this.field_192756_d.func_191949_aK().func_192780_b()) {
            this.func_193764_b(advancement);
        }
    }
    
    private void func_192752_d() {
        final List<Advancement> list = (List<Advancement>)Lists.newArrayList();
        for (final Map.Entry<Advancement, AdvancementProgress> entry : this.field_192758_f.entrySet()) {
            if (entry.getValue().func_192105_a()) {
                list.add(entry.getKey());
                this.field_192761_i.add(entry.getKey());
            }
        }
        for (final Advancement advancement : list) {
            this.func_192742_b(advancement);
        }
    }
    
    private void func_192748_e() {
        for (final Advancement advancement : this.field_192756_d.func_191949_aK().func_192780_b()) {
            if (advancement.func_192073_f().isEmpty()) {
                this.func_192750_a(advancement, "");
                advancement.func_192072_d().func_192113_a(this.field_192762_j);
            }
        }
    }
    
    private void func_192740_f() {
        if (this.field_192757_e.isFile()) {
            try {
                final String s = Files.toString(this.field_192757_e, StandardCharsets.UTF_8);
                final Map<ResourceLocation, AdvancementProgress> map = JsonUtils.func_193840_a(PlayerAdvancements.field_192754_b, s, PlayerAdvancements.field_192755_c.getType());
                if (map == null) {
                    throw new JsonParseException("Found null for advancements");
                }
                final Stream<Map.Entry<ResourceLocation, AdvancementProgress>> stream = map.entrySet().stream().sorted(Comparator.comparing((Function<? super Map.Entry<ResourceLocation, AdvancementProgress>, ? extends Comparable>)Map.Entry::getValue));
                for (final Map.Entry<ResourceLocation, AdvancementProgress> entry : stream.collect((Collector<? super Map.Entry<ResourceLocation, AdvancementProgress>, ?, List<? super Map.Entry<ResourceLocation, AdvancementProgress>>>)Collectors.toList())) {
                    final Advancement advancement = this.field_192756_d.func_191949_aK().func_192778_a(entry.getKey());
                    if (advancement == null) {
                        PlayerAdvancements.field_192753_a.warn("Ignored advancement '" + entry.getKey() + "' in progress file " + this.field_192757_e + " - it doesn't exist anymore?");
                    }
                    else {
                        this.func_192743_a(advancement, entry.getValue());
                    }
                }
            }
            catch (final JsonParseException jsonparseexception) {
                PlayerAdvancements.field_192753_a.error("Couldn't parse player advancements in " + this.field_192757_e, jsonparseexception);
            }
            catch (final IOException ioexception) {
                PlayerAdvancements.field_192753_a.error("Couldn't access player advancements in " + this.field_192757_e, ioexception);
            }
        }
        this.func_192748_e();
        this.func_192752_d();
        this.func_192751_c();
    }
    
    public void func_192749_b() {
        final Map<ResourceLocation, AdvancementProgress> map = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap();
        for (final Map.Entry<Advancement, AdvancementProgress> entry : this.field_192758_f.entrySet()) {
            final AdvancementProgress advancementprogress = entry.getValue();
            if (advancementprogress.func_192108_b()) {
                map.put(entry.getKey().func_192067_g(), advancementprogress);
            }
        }
        if (this.field_192757_e.getParentFile() != null) {
            this.field_192757_e.getParentFile().mkdirs();
        }
        try {
            Files.write(PlayerAdvancements.field_192754_b.toJson(map), this.field_192757_e, StandardCharsets.UTF_8);
        }
        catch (final IOException ioexception) {
            PlayerAdvancements.field_192753_a.error("Couldn't save player advancements to " + this.field_192757_e, ioexception);
        }
    }
    
    public boolean func_192750_a(final Advancement p_192750_1_, final String p_192750_2_) {
        boolean flag = false;
        final AdvancementProgress advancementprogress = this.func_192747_a(p_192750_1_);
        final boolean flag2 = advancementprogress.func_192105_a();
        if (advancementprogress.func_192109_a(p_192750_2_)) {
            this.func_193765_c(p_192750_1_);
            this.field_192761_i.add(p_192750_1_);
            flag = true;
            if (!flag2 && advancementprogress.func_192105_a()) {
                p_192750_1_.func_192072_d().func_192113_a(this.field_192762_j);
                if (p_192750_1_.func_192068_c() != null && p_192750_1_.func_192068_c().func_193220_i() && this.field_192762_j.world.getGameRules().getBoolean("announceAdvancements")) {
                    this.field_192756_d.getPlayerList().sendChatMsg(new TextComponentTranslation("chat.type.advancement." + p_192750_1_.func_192068_c().func_192291_d().func_192307_a(), new Object[] { this.field_192762_j.getDisplayName(), p_192750_1_.func_193123_j() }));
                }
            }
        }
        if (advancementprogress.func_192105_a()) {
            this.func_192742_b(p_192750_1_);
        }
        return flag;
    }
    
    public boolean func_192744_b(final Advancement p_192744_1_, final String p_192744_2_) {
        boolean flag = false;
        final AdvancementProgress advancementprogress = this.func_192747_a(p_192744_1_);
        if (advancementprogress.func_192101_b(p_192744_2_)) {
            this.func_193764_b(p_192744_1_);
            this.field_192761_i.add(p_192744_1_);
            flag = true;
        }
        if (!advancementprogress.func_192108_b()) {
            this.func_192742_b(p_192744_1_);
        }
        return flag;
    }
    
    private void func_193764_b(final Advancement p_193764_1_) {
        final AdvancementProgress advancementprogress = this.func_192747_a(p_193764_1_);
        if (!advancementprogress.func_192105_a()) {
            for (final Map.Entry<String, Criterion> entry : p_193764_1_.func_192073_f().entrySet()) {
                final CriterionProgress criterionprogress = advancementprogress.func_192106_c(entry.getKey());
                if (criterionprogress != null && !criterionprogress.func_192151_a()) {
                    final ICriterionInstance icriterioninstance = entry.getValue().func_192143_a();
                    if (icriterioninstance == null) {
                        continue;
                    }
                    final ICriterionTrigger<ICriterionInstance> icriteriontrigger = CriteriaTriggers.func_192119_a(icriterioninstance.func_192244_a());
                    if (icriteriontrigger == null) {
                        continue;
                    }
                    icriteriontrigger.func_192165_a(this, new ICriterionTrigger.Listener<ICriterionInstance>(icriterioninstance, p_193764_1_, entry.getKey()));
                }
            }
        }
    }
    
    private void func_193765_c(final Advancement p_193765_1_) {
        final AdvancementProgress advancementprogress = this.func_192747_a(p_193765_1_);
        for (final Map.Entry<String, Criterion> entry : p_193765_1_.func_192073_f().entrySet()) {
            final CriterionProgress criterionprogress = advancementprogress.func_192106_c(entry.getKey());
            if (criterionprogress != null && (criterionprogress.func_192151_a() || advancementprogress.func_192105_a())) {
                final ICriterionInstance icriterioninstance = entry.getValue().func_192143_a();
                if (icriterioninstance == null) {
                    continue;
                }
                final ICriterionTrigger<ICriterionInstance> icriteriontrigger = CriteriaTriggers.func_192119_a(icriterioninstance.func_192244_a());
                if (icriteriontrigger == null) {
                    continue;
                }
                icriteriontrigger.func_192164_b(this, new ICriterionTrigger.Listener<ICriterionInstance>(icriterioninstance, p_193765_1_, entry.getKey()));
            }
        }
    }
    
    public void func_192741_b(final EntityPlayerMP p_192741_1_) {
        if (!this.field_192760_h.isEmpty() || !this.field_192761_i.isEmpty()) {
            final Map<ResourceLocation, AdvancementProgress> map = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap();
            final Set<Advancement> set = (Set<Advancement>)Sets.newLinkedHashSet();
            final Set<ResourceLocation> set2 = (Set<ResourceLocation>)Sets.newLinkedHashSet();
            for (final Advancement advancement : this.field_192761_i) {
                if (this.field_192759_g.contains(advancement)) {
                    map.put(advancement.func_192067_g(), this.field_192758_f.get(advancement));
                }
            }
            for (final Advancement advancement2 : this.field_192760_h) {
                if (this.field_192759_g.contains(advancement2)) {
                    set.add(advancement2);
                }
                else {
                    set2.add(advancement2.func_192067_g());
                }
            }
            if (!map.isEmpty() || !set.isEmpty() || !set2.isEmpty()) {
                p_192741_1_.connection.sendPacket(new SPacketAdvancementInfo(this.field_192763_k, set, set2, map));
                this.field_192760_h.clear();
                this.field_192761_i.clear();
            }
        }
        this.field_192763_k = false;
    }
    
    public void func_194220_a(@Nullable final Advancement p_194220_1_) {
        final Advancement advancement = this.field_194221_k;
        if (p_194220_1_ != null && p_194220_1_.func_192070_b() == null && p_194220_1_.func_192068_c() != null) {
            this.field_194221_k = p_194220_1_;
        }
        else {
            this.field_194221_k = null;
        }
        if (advancement != this.field_194221_k) {
            this.field_192762_j.connection.sendPacket(new SPacketSelectAdvancementsTab((this.field_194221_k == null) ? null : this.field_194221_k.func_192067_g()));
        }
    }
    
    public AdvancementProgress func_192747_a(final Advancement p_192747_1_) {
        AdvancementProgress advancementprogress = this.field_192758_f.get(p_192747_1_);
        if (advancementprogress == null) {
            advancementprogress = new AdvancementProgress();
            this.func_192743_a(p_192747_1_, advancementprogress);
        }
        return advancementprogress;
    }
    
    private void func_192743_a(final Advancement p_192743_1_, final AdvancementProgress p_192743_2_) {
        p_192743_2_.func_192099_a(p_192743_1_.func_192073_f(), p_192743_1_.func_192074_h());
        this.field_192758_f.put(p_192743_1_, p_192743_2_);
    }
    
    private void func_192742_b(final Advancement p_192742_1_) {
        final boolean flag = this.func_192738_c(p_192742_1_);
        final boolean flag2 = this.field_192759_g.contains(p_192742_1_);
        if (flag && !flag2) {
            this.field_192759_g.add(p_192742_1_);
            this.field_192760_h.add(p_192742_1_);
            if (this.field_192758_f.containsKey(p_192742_1_)) {
                this.field_192761_i.add(p_192742_1_);
            }
        }
        else if (!flag && flag2) {
            this.field_192759_g.remove(p_192742_1_);
            this.field_192760_h.add(p_192742_1_);
        }
        if (flag != flag2 && p_192742_1_.func_192070_b() != null) {
            this.func_192742_b(p_192742_1_.func_192070_b());
        }
        for (final Advancement advancement : p_192742_1_.func_192069_e()) {
            this.func_192742_b(advancement);
        }
    }
    
    private boolean func_192738_c(Advancement p_192738_1_) {
        for (int i = 0; p_192738_1_ != null && i <= 2; p_192738_1_ = p_192738_1_.func_192070_b(), ++i) {
            if (i == 0 && this.func_192746_d(p_192738_1_)) {
                return true;
            }
            if (p_192738_1_.func_192068_c() == null) {
                return false;
            }
            final AdvancementProgress advancementprogress = this.func_192747_a(p_192738_1_);
            if (advancementprogress.func_192105_a()) {
                return true;
            }
            if (p_192738_1_.func_192068_c().func_193224_j()) {
                return false;
            }
        }
        return false;
    }
    
    private boolean func_192746_d(final Advancement p_192746_1_) {
        final AdvancementProgress advancementprogress = this.func_192747_a(p_192746_1_);
        if (advancementprogress.func_192105_a()) {
            return true;
        }
        for (final Advancement advancement : p_192746_1_.func_192069_e()) {
            if (this.func_192746_d(advancement)) {
                return true;
            }
        }
        return false;
    }
}
