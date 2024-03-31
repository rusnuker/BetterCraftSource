// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import java.util.Arrays;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import java.util.List;
import me.amkgre.bettercraft.client.mods.cosmetics.CosmeticBase;

public class CosmeticHeadPet extends CosmeticBase
{
    private static final List<String> folderLess;
    private Render entityRenderer;
    private ModelBase model;
    
    static {
        folderLess = Arrays.asList("bat", "witch", "phantom", "blaze");
    }
    
    public CosmeticHeadPet(final RenderPlayer player) {
        super(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
    }
}
