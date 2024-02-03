// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP;

import net.minecraft.init.Blocks;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import com.google.common.base.Throwables;
import com.TominoCZ.FBP.util.FBPReflectionHelper;
import net.minecraft.client.particle.EntityDiggingFX;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.ArrayList;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import com.TominoCZ.FBP.handler.FBPEventHandler;
import net.minecraft.block.material.Material;
import java.util.List;
import net.minecraft.client.particle.EffectRenderer;
import com.TominoCZ.FBP.particle.FBPParticleManager;
import java.lang.invoke.MethodHandle;
import net.minecraft.client.renderer.vertex.VertexFormat;
import java.util.SplittableRandom;
import java.io.File;
import net.minecraft.util.Vec3;
import net.minecraft.util.ResourceLocation;

public class FBP
{
    private static final FBP INSTANCE;
    public static final String MODID = "fbp";
    public static final ResourceLocation LOCATION_PARTICLE_TEXTURE;
    public static final ResourceLocation FBP_BUG;
    public static final ResourceLocation FBP_FBP;
    public static final ResourceLocation FBP_WIDGETS;
    public static final Vec3[] CUBE;
    public static final Vec3[] CUBE_NORMALS;
    public static File particleBlacklistFile;
    public static File floatingMaterialsFile;
    public static File config;
    public static int minAge;
    public static int maxAge;
    public static int particlesPerAxis;
    public static double scaleMult;
    public static double gravityMult;
    public static double rotationMult;
    public static double weatherParticleDensity;
    public static boolean enabled;
    public static boolean showInMillis;
    public static boolean infiniteDuration;
    public static boolean randomRotation;
    public static boolean cartoonMode;
    public static boolean spawnWhileFrozen;
    public static boolean spawnRedstoneBlockParticles;
    public static boolean randomizedScale;
    public static boolean randomFadingSpeed;
    public static boolean entityCollision;
    public static boolean bounceOffWalls;
    public static boolean lowTraction;
    public static boolean smartBreaking;
    public static boolean fancyRain;
    public static boolean fancySnow;
    public static boolean fancyFlame;
    public static boolean fancySmoke;
    public static boolean waterPhysics;
    public static boolean restOnFloor;
    public static boolean frozen;
    public static SplittableRandom random;
    public static VertexFormat POSITION_TEX_COLOR_LMAP_NORMAL;
    public static MethodHandle setSourcePos;
    public static FBPParticleManager fancyEffectRenderer;
    public static EffectRenderer originalEffectRenderer;
    public List<String> blockParticleBlacklist;
    public List<Material> floatingMaterials;
    public FBPEventHandler eventHandler;
    
    static {
        INSTANCE = new FBP();
        LOCATION_PARTICLE_TEXTURE = new ResourceLocation("textures/particle/particles.png");
        FBP_BUG = new ResourceLocation("fbp:textures/gui/bug.png");
        FBP_FBP = new ResourceLocation("fbp:textures/gui/fbp.png");
        FBP_WIDGETS = new ResourceLocation("fbp:textures/gui/widgets.png");
        CUBE = new Vec3[] { new Vec3(1.0, 1.0, -1.0), new Vec3(1.0, 1.0, 1.0), new Vec3(-1.0, 1.0, 1.0), new Vec3(-1.0, 1.0, -1.0), new Vec3(-1.0, -1.0, -1.0), new Vec3(-1.0, -1.0, 1.0), new Vec3(1.0, -1.0, 1.0), new Vec3(1.0, -1.0, -1.0), new Vec3(-1.0, -1.0, 1.0), new Vec3(-1.0, 1.0, 1.0), new Vec3(1.0, 1.0, 1.0), new Vec3(1.0, -1.0, 1.0), new Vec3(1.0, -1.0, -1.0), new Vec3(1.0, 1.0, -1.0), new Vec3(-1.0, 1.0, -1.0), new Vec3(-1.0, -1.0, -1.0), new Vec3(-1.0, -1.0, -1.0), new Vec3(-1.0, 1.0, -1.0), new Vec3(-1.0, 1.0, 1.0), new Vec3(-1.0, -1.0, 1.0), new Vec3(1.0, -1.0, 1.0), new Vec3(1.0, 1.0, 1.0), new Vec3(1.0, 1.0, -1.0), new Vec3(1.0, -1.0, -1.0) };
        CUBE_NORMALS = new Vec3[] { new Vec3(0.0, 1.0, 0.0), new Vec3(0.0, -1.0, 0.0), new Vec3(0.0, 0.0, 1.0), new Vec3(0.0, 0.0, -1.0), new Vec3(-1.0, 0.0, 0.0), new Vec3(1.0, 0.0, 0.0) };
        FBP.particleBlacklistFile = null;
        FBP.floatingMaterialsFile = null;
        FBP.config = null;
        FBP.enabled = true;
        FBP.showInMillis = false;
        FBP.infiniteDuration = false;
        FBP.random = new SplittableRandom();
    }
    
    public static final FBP getInstance() {
        return FBP.INSTANCE;
    }
    
    public FBP() {
        this.eventHandler = new FBPEventHandler();
        (FBP.POSITION_TEX_COLOR_LMAP_NORMAL = new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.addElement(DefaultVertexFormats.TEX_2F);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.addElement(DefaultVertexFormats.COLOR_4UB);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.addElement(DefaultVertexFormats.TEX_2S);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.addElement(DefaultVertexFormats.NORMAL_3B);
        this.blockParticleBlacklist = Collections.synchronizedList(new ArrayList<String>());
        this.floatingMaterials = Collections.synchronizedList(new ArrayList<Material>());
    }
    
    public void init() {
        final MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        try {
            FBP.setSourcePos = lookup.unreflectSetter(FBPReflectionHelper.findField(EntityDiggingFX.class, "field_181019_az", "sourcePos"));
        }
        catch (final Exception e) {
            throw Throwables.propagate(e);
        }
        this.syncWithModule();
    }
    
    public static boolean isEnabled() {
        if (!FBP.enabled) {
            FBP.frozen = false;
        }
        return FBP.enabled;
    }
    
    public static void setEnabled(final boolean enabled) {
        if (FBP.enabled != enabled) {
            if (enabled) {
                FBP.fancyEffectRenderer.carryOver();
                Minecraft.getMinecraft().effectRenderer = FBP.fancyEffectRenderer;
            }
            else {
                Minecraft.getMinecraft().effectRenderer = FBP.originalEffectRenderer;
            }
        }
        FBP.enabled = enabled;
    }
    
    public boolean isBlacklisted(final Block b) {
        return b == null || this.blockParticleBlacklist.contains(b.getLocalizedName().toString());
    }
    
    public boolean doesMaterialFloat(final Material mat) {
        return this.floatingMaterials.contains(mat);
    }
    
    public void addToBlacklist(final String name) {
        if (StringUtils.isEmpty(name)) {
            return;
        }
        final Block b = Block.getBlockFromName(name);
        if (b == null || b == Blocks.redstone_block) {
            return;
        }
        this.addToBlacklist(b);
    }
    
    public void addToBlacklist(final Block b) {
        if (b == null) {
            return;
        }
        final String name = b.getLocalizedName().toString();
        if (!this.blockParticleBlacklist.contains(name)) {
            this.blockParticleBlacklist.add(name);
        }
    }
    
    public void removeFromBlacklist(final Block b) {
        if (b == null) {
            return;
        }
        final String name = b.getLocalizedName().toString();
        if (this.blockParticleBlacklist.contains(name)) {
            this.blockParticleBlacklist.remove(name);
        }
    }
    
    public void syncWithModule() {
        FBP.minAge = 10;
        FBP.maxAge = 50;
        FBP.infiniteDuration = false;
        FBP.particlesPerAxis = 4;
        FBP.scaleMult = 0.7;
        FBP.gravityMult = 1.0;
        FBP.rotationMult = 1.0;
        FBP.randomRotation = true;
        FBP.cartoonMode = false;
        FBP.randomizedScale = true;
        FBP.randomFadingSpeed = true;
        FBP.spawnRedstoneBlockParticles = true;
        FBP.entityCollision = false;
        FBP.bounceOffWalls = true;
        FBP.lowTraction = false;
        FBP.smartBreaking = true;
        FBP.fancyFlame = true;
        FBP.fancySmoke = true;
        FBP.waterPhysics = true;
        FBP.restOnFloor = true;
    }
    
    public void resetBlacklist() {
        this.blockParticleBlacklist.clear();
    }
}
