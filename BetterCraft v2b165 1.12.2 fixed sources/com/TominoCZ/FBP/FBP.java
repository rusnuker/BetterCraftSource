// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP;

import me.amkgre.bettercraft.client.utils.ClientSettingsUtils;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import com.google.common.base.Throwables;
import com.TominoCZ.FBP.util.FBPReflectionHelper;
import net.minecraft.client.particle.ParticleDigging;
import java.lang.invoke.MethodHandles;
import me.amkgre.bettercraft.client.gui.GuiMods;
import java.util.Collections;
import java.util.ArrayList;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import com.TominoCZ.FBP.handler.FBPEventHandler;
import net.minecraft.client.particle.ParticleManager;
import com.TominoCZ.FBP.particle.FBPParticleManager;
import com.TominoCZ.FBP.block.FBPAnimationDummyBlock;
import java.lang.invoke.MethodHandle;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.Vec3d;
import java.util.SplittableRandom;
import net.minecraft.block.material.Material;
import java.util.List;
import java.io.File;
import net.minecraft.util.ResourceLocation;

public class FBP
{
    public static FBP INSTANCE;
    public static final String MODID = "fbp";
    public static final ResourceLocation LOCATION_PARTICLE_TEXTURE;
    public static File animBlacklistFile;
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
    public static boolean fancyPlaceAnim;
    public static boolean animSmoothLighting;
    public static boolean spawnPlaceParticles;
    public static boolean fancyRain;
    public static boolean fancySnow;
    public static boolean fancyFlame;
    public static boolean fancySmoke;
    public static boolean waterPhysics;
    public static boolean restOnFloor;
    public static boolean frozen;
    public List<String> blockParticleBlacklist;
    public List<String> blockAnimBlacklist;
    public List<Material> floatingMaterials;
    public static SplittableRandom random;
    public static final Vec3d[] CUBE;
    public static final Vec3d[] CUBE_NORMALS;
    public static VertexFormat POSITION_TEX_COLOR_LMAP_NORMAL;
    public static MethodHandle setSourcePos;
    public static FBPAnimationDummyBlock FBPBlock;
    public static FBPParticleManager fancyEffectRenderer;
    public static ParticleManager originalEffectRenderer;
    public FBPEventHandler eventHandler;
    
    static {
        LOCATION_PARTICLE_TEXTURE = new ResourceLocation("textures/particle/particles.png");
        FBP.animBlacklistFile = null;
        FBP.particleBlacklistFile = null;
        FBP.floatingMaterialsFile = null;
        FBP.config = null;
        FBP.enabled = false;
        FBP.showInMillis = false;
        FBP.infiniteDuration = false;
        FBP.random = new SplittableRandom();
        CUBE = new Vec3d[] { new Vec3d(1.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(-1.0, -1.0, -1.0), new Vec3d(-1.0, -1.0, 1.0), new Vec3d(1.0, -1.0, 1.0), new Vec3d(1.0, -1.0, -1.0), new Vec3d(-1.0, -1.0, 1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(1.0, -1.0, 1.0), new Vec3d(1.0, -1.0, -1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(-1.0, -1.0, -1.0), new Vec3d(-1.0, -1.0, -1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(-1.0, -1.0, 1.0), new Vec3d(1.0, -1.0, 1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(1.0, -1.0, -1.0) };
        CUBE_NORMALS = new Vec3d[] { new Vec3d(0.0, 1.0, 0.0), new Vec3d(0.0, -1.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0) };
        FBP.FBPBlock = new FBPAnimationDummyBlock();
    }
    
    public FBP() {
        this.eventHandler = new FBPEventHandler();
        FBP.INSTANCE = this;
        (FBP.POSITION_TEX_COLOR_LMAP_NORMAL = new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.addElement(DefaultVertexFormats.TEX_2F);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.addElement(DefaultVertexFormats.COLOR_4UB);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.addElement(DefaultVertexFormats.TEX_2S);
        FBP.POSITION_TEX_COLOR_LMAP_NORMAL.addElement(DefaultVertexFormats.NORMAL_3B);
        this.blockParticleBlacklist = Collections.synchronizedList(new ArrayList<String>());
        this.blockAnimBlacklist = Collections.synchronizedList(new ArrayList<String>());
        this.floatingMaterials = Collections.synchronizedList(new ArrayList<Material>());
    }
    
    public void initialize() {
        if (GuiMods.fbp) {
            this.syncWithModule();
            final MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            try {
                FBP.setSourcePos = lookup.unreflectSetter(FBPReflectionHelper.findField(ParticleDigging.class, "field_181019_az", "sourcePos"));
            }
            catch (final Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }
    
    public static boolean isEnabled() {
        final boolean result = FBP.enabled;
        if (!result) {
            FBP.frozen = false;
        }
        return result;
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
    
    public boolean isBlacklisted(final Block b, final boolean particle) {
        return b == null || (particle ? this.blockParticleBlacklist : this.blockAnimBlacklist).contains(b.getUnlocalizedName());
    }
    
    public boolean doesMaterialFloat(final Material mat) {
        return this.floatingMaterials.contains(mat);
    }
    
    public void addToBlacklist(final Block b, final boolean particle) {
        if (b == null) {
            return;
        }
        final String name = b.getUnlocalizedName().toString();
        if (!(particle ? this.blockParticleBlacklist : this.blockAnimBlacklist).contains(name)) {
            (particle ? this.blockParticleBlacklist : this.blockAnimBlacklist).add(name);
        }
    }
    
    public void removeFromBlacklist(final Block b, final boolean particle) {
        if (b == null) {
            return;
        }
        final String name = b.getUnlocalizedName().toString();
        if ((particle ? this.blockParticleBlacklist : this.blockAnimBlacklist).contains(name)) {
            (particle ? this.blockParticleBlacklist : this.blockAnimBlacklist).remove(name);
        }
    }
    
    public void addToBlacklist(final String name, final boolean particle) {
        if (StringUtils.isEmpty(name)) {
            return;
        }
        for (final ResourceLocation rl : Block.REGISTRY.getKeys()) {
            final String s = rl.toString();
            if (s.equals(name)) {
                final Block b = Block.REGISTRY.getObject(rl);
                if (b == Blocks.REDSTONE_BLOCK) {
                    break;
                }
                this.addToBlacklist(b, particle);
                break;
            }
        }
    }
    
    public void resetBlacklist(final boolean particle) {
        if (particle) {
            this.blockParticleBlacklist.clear();
        }
        else {
            this.blockAnimBlacklist.clear();
        }
    }
    
    public void syncWithModule() {
        if (ClientSettingsUtils.fbp) {
            FBP.enabled = true;
            FBP.minAge = 10;
            FBP.maxAge = 55;
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
    }
}
