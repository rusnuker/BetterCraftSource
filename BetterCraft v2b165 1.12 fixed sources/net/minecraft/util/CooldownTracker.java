// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import java.util.Map;

public class CooldownTracker
{
    private final Map<Item, Cooldown> cooldowns;
    private int ticks;
    
    public CooldownTracker() {
        this.cooldowns = (Map<Item, Cooldown>)Maps.newHashMap();
    }
    
    public boolean hasCooldown(final Item itemIn) {
        return this.getCooldown(itemIn, 0.0f) > 0.0f;
    }
    
    public float getCooldown(final Item itemIn, final float partialTicks) {
        final Cooldown cooldowntracker$cooldown = this.cooldowns.get(itemIn);
        if (cooldowntracker$cooldown != null) {
            final float f = (float)(cooldowntracker$cooldown.expireTicks - cooldowntracker$cooldown.createTicks);
            final float f2 = cooldowntracker$cooldown.expireTicks - (this.ticks + partialTicks);
            return MathHelper.clamp(f2 / f, 0.0f, 1.0f);
        }
        return 0.0f;
    }
    
    public void tick() {
        ++this.ticks;
        if (!this.cooldowns.isEmpty()) {
            final Iterator<Map.Entry<Item, Cooldown>> iterator = this.cooldowns.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<Item, Cooldown> entry = iterator.next();
                if (entry.getValue().expireTicks <= this.ticks) {
                    iterator.remove();
                    this.notifyOnRemove(entry.getKey());
                }
            }
        }
    }
    
    public void setCooldown(final Item itemIn, final int ticksIn) {
        this.cooldowns.put(itemIn, new Cooldown(this.ticks, this.ticks + ticksIn, (Cooldown)null));
        this.notifyOnSet(itemIn, ticksIn);
    }
    
    public void removeCooldown(final Item itemIn) {
        this.cooldowns.remove(itemIn);
        this.notifyOnRemove(itemIn);
    }
    
    protected void notifyOnSet(final Item itemIn, final int ticksIn) {
    }
    
    protected void notifyOnRemove(final Item itemIn) {
    }
    
    class Cooldown
    {
        final int createTicks;
        final int expireTicks;
        
        private Cooldown(final int createTicksIn, final int expireTicksIn) {
            this.createTicks = createTicksIn;
            this.expireTicks = expireTicksIn;
        }
    }
}
