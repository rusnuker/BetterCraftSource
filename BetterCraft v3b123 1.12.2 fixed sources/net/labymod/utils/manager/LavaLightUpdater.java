// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.manager;

import java.lang.reflect.Field;
import net.labymod.utils.ReflectionHelper;
import net.labymod.core.LabyModCore;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import net.labymod.main.LabyMod;

public class LavaLightUpdater
{
    public static void update() {
        final boolean value = LabyMod.getSettings().improvedLavaNoLight;
        final Block blockFlowingLava = Block.getBlockById(10);
        final Block blockLava = Block.getBlockById(11);
        try {
            updateLightField(blockLava, value);
            updateLightField(blockFlowingLava, value);
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
        if (LabyMod.getInstance().isInGame()) {
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        }
    }
    
    private static void updateLightField(final Block block, final boolean value) throws Exception {
        final Field lightValueField = ReflectionHelper.findField(Block.class, LabyModCore.getMappingAdapter().getLightValueMappings());
        lightValueField.setAccessible(true);
        lightValueField.set(block, value ? 0 : 15);
    }
}
