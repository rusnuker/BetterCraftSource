// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.toasts;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.ResourceLocation;

public interface IToast
{
    public static final ResourceLocation field_193654_a = new ResourceLocation("textures/gui/toasts.png");
    public static final Object field_193655_b = new Object();
    
    Visibility func_193653_a(final GuiToast p0, final long p1);
    
    default Object func_193652_b() {
        return IToast.field_193655_b;
    }
    
    public enum Visibility
    {
        SHOW("SHOW", 0, SoundEvents.field_194226_id), 
        HIDE("HIDE", 1, SoundEvents.field_194227_ie);
        
        private final SoundEvent field_194170_c;
        
        private Visibility(final String s, final int n, final SoundEvent p_i47607_3_) {
            this.field_194170_c = p_i47607_3_;
        }
        
        public void func_194169_a(final SoundHandler p_194169_1_) {
            p_194169_1_.playSound(PositionedSoundRecord.func_194007_a(this.field_194170_c, 1.0f, 1.0f));
        }
    }
}
