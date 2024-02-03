// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.labymod.core.SoundAdapter;

public class SoundImplementation implements SoundAdapter
{
    @Override
    public void playSignSearchSign(final int x, final int y, final int z) {
        Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation("fireworks.twinkle_far"), 10.0f, 2.0f, (float)x, (float)y, (float)z));
    }
}
