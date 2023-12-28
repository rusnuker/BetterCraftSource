// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import net.minecraft.advancements.critereon.NetherTravelTrigger;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.PlacedBlockTrigger;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.LevitationTrigger;
import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
import net.minecraft.advancements.critereon.VillagerTradeTrigger;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.advancements.critereon.PositionTrigger;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.SummonedEntityTrigger;
import net.minecraft.advancements.critereon.UsedEnderEyeTrigger;
import net.minecraft.advancements.critereon.ConstructBeaconTrigger;
import net.minecraft.advancements.critereon.BrewedPotionTrigger;
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class CriteriaTriggers
{
    private static final Map<ResourceLocation, ICriterionTrigger<?>> field_192139_s;
    public static final ImpossibleTrigger field_192121_a;
    public static final KilledTrigger field_192122_b;
    public static final KilledTrigger field_192123_c;
    public static final EnterBlockTrigger field_192124_d;
    public static final InventoryChangeTrigger field_192125_e;
    public static final RecipeUnlockedTrigger field_192126_f;
    public static final PlayerHurtEntityTrigger field_192127_g;
    public static final EntityHurtPlayerTrigger field_192128_h;
    public static final EnchantedItemTrigger field_192129_i;
    public static final BrewedPotionTrigger field_192130_j;
    public static final ConstructBeaconTrigger field_192131_k;
    public static final UsedEnderEyeTrigger field_192132_l;
    public static final SummonedEntityTrigger field_192133_m;
    public static final BredAnimalsTrigger field_192134_n;
    public static final PositionTrigger field_192135_o;
    public static final PositionTrigger field_192136_p;
    public static final CuredZombieVillagerTrigger field_192137_q;
    public static final VillagerTradeTrigger field_192138_r;
    public static final ItemDurabilityTrigger field_193132_s;
    public static final LevitationTrigger field_193133_t;
    public static final ChangeDimensionTrigger field_193134_u;
    public static final TickTrigger field_193135_v;
    public static final TameAnimalTrigger field_193136_w;
    public static final PlacedBlockTrigger field_193137_x;
    public static final ConsumeItemTrigger field_193138_y;
    public static final EffectsChangedTrigger field_193139_z;
    public static final UsedTotemTrigger field_193130_A;
    public static final NetherTravelTrigger field_193131_B;
    
    static {
        field_192139_s = Maps.newHashMap();
        field_192121_a = func_192118_a(new ImpossibleTrigger());
        field_192122_b = func_192118_a(new KilledTrigger(new ResourceLocation("player_killed_entity")));
        field_192123_c = func_192118_a(new KilledTrigger(new ResourceLocation("entity_killed_player")));
        field_192124_d = func_192118_a(new EnterBlockTrigger());
        field_192125_e = func_192118_a(new InventoryChangeTrigger());
        field_192126_f = func_192118_a(new RecipeUnlockedTrigger());
        field_192127_g = func_192118_a(new PlayerHurtEntityTrigger());
        field_192128_h = func_192118_a(new EntityHurtPlayerTrigger());
        field_192129_i = func_192118_a(new EnchantedItemTrigger());
        field_192130_j = func_192118_a(new BrewedPotionTrigger());
        field_192131_k = func_192118_a(new ConstructBeaconTrigger());
        field_192132_l = func_192118_a(new UsedEnderEyeTrigger());
        field_192133_m = func_192118_a(new SummonedEntityTrigger());
        field_192134_n = func_192118_a(new BredAnimalsTrigger());
        field_192135_o = func_192118_a(new PositionTrigger(new ResourceLocation("location")));
        field_192136_p = func_192118_a(new PositionTrigger(new ResourceLocation("slept_in_bed")));
        field_192137_q = func_192118_a(new CuredZombieVillagerTrigger());
        field_192138_r = func_192118_a(new VillagerTradeTrigger());
        field_193132_s = func_192118_a(new ItemDurabilityTrigger());
        field_193133_t = func_192118_a(new LevitationTrigger());
        field_193134_u = func_192118_a(new ChangeDimensionTrigger());
        field_193135_v = func_192118_a(new TickTrigger());
        field_193136_w = func_192118_a(new TameAnimalTrigger());
        field_193137_x = func_192118_a(new PlacedBlockTrigger());
        field_193138_y = func_192118_a(new ConsumeItemTrigger());
        field_193139_z = func_192118_a(new EffectsChangedTrigger());
        field_193130_A = func_192118_a(new UsedTotemTrigger());
        field_193131_B = func_192118_a(new NetherTravelTrigger());
    }
    
    private static <T extends ICriterionTrigger> T func_192118_a(final T p_192118_0_) {
        if (CriteriaTriggers.field_192139_s.containsKey(p_192118_0_.func_192163_a())) {
            throw new IllegalArgumentException("Duplicate criterion id " + p_192118_0_.func_192163_a());
        }
        CriteriaTriggers.field_192139_s.put(p_192118_0_.func_192163_a(), p_192118_0_);
        return p_192118_0_;
    }
    
    @Nullable
    public static <T extends ICriterionInstance> ICriterionTrigger<T> func_192119_a(final ResourceLocation p_192119_0_) {
        return (ICriterionTrigger)CriteriaTriggers.field_192139_s.get(p_192119_0_);
    }
    
    public static Iterable<? extends ICriterionTrigger<?>> func_192120_a() {
        return CriteriaTriggers.field_192139_s.values();
    }
}
