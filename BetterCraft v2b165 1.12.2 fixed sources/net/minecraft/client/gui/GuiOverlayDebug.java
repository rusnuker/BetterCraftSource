// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.block.properties.IProperty;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import java.util.Collection;
import optifine.Reflector;
import org.lwjgl.opengl.Display;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import com.google.common.base.Strings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class GuiOverlayDebug extends Gui
{
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    
    public GuiOverlayDebug(final Minecraft mc) {
        this.mc = mc;
        this.fontRenderer = mc.fontRendererObj;
    }
    
    public void renderDebugInfo(final ScaledResolution scaledResolutionIn) {
        this.mc.mcProfiler.startSection("debug");
        GlStateManager.pushMatrix();
        this.renderDebugInfoLeft();
        this.renderDebugInfoRight(scaledResolutionIn);
        GlStateManager.popMatrix();
        if (this.mc.gameSettings.showLagometer) {
            this.renderLagometer();
        }
        this.mc.mcProfiler.endSection();
    }
    
    protected void renderDebugInfoLeft() {
        final List<String> list = this.call();
        list.add("");
        list.add("Debug: Pie [shift]: " + (this.mc.gameSettings.showDebugProfilerChart ? "visible" : "hidden") + " FPS [alt]: " + (this.mc.gameSettings.showLagometer ? "visible" : "hidden"));
        list.add("For help: press F3 + Q");
        for (int i = 0; i < list.size(); ++i) {
            final String s = list.get(i);
            if (!Strings.isNullOrEmpty(s)) {
                final int j = this.fontRenderer.FONT_HEIGHT;
                final int k = this.fontRenderer.getStringWidth(s);
                final int l = 2;
                final int i2 = 2 + j * i;
                this.fontRenderer.drawString(s, 662, i2, 14737632);
            }
        }
    }
    
    protected void renderDebugInfoRight(final ScaledResolution scaledRes) {
        final List<String> list = this.getDebugInfoRight();
        for (int i = 0; i < list.size(); ++i) {
            final String s = list.get(i);
            if (!Strings.isNullOrEmpty(s)) {
                final int j = this.fontRenderer.FONT_HEIGHT;
                final int k = this.fontRenderer.getStringWidth(s);
                final int l = ScaledResolution.getScaledWidth() - 2 - k;
                final int i2 = 2 + j * i;
                this.fontRenderer.drawString(s, l, i2, 14737632);
            }
        }
    }
    
    protected List<String> call() {
        final BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
        if (this.mc.isReducedDebug()) {
            return Lists.newArrayList("Minecraft 1.12.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(), this.mc.world.getProviderName(), "", String.format("-relative: %d %d %d", blockpos.getX() & 0xF, blockpos.getY() & 0xF, blockpos.getZ() & 0xF));
        }
        final Entity entity = this.mc.getRenderViewEntity();
        final EnumFacing enumfacing = entity.getHorizontalFacing();
        String s = "Invalid";
        switch (enumfacing) {
            case NORTH: {
                s = "Towards negative Z";
                break;
            }
            case SOUTH: {
                s = "Towards positive Z";
                break;
            }
            case WEST: {
                s = "Towards negative X";
                break;
            }
            case EAST: {
                s = "Towards positive X";
                break;
            }
        }
        final List<String> list = Lists.newArrayList("Minecraft 1.12.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : ("/" + this.mc.getVersionType())) + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(), this.mc.world.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ), String.format("Block: %d %d %d", blockpos.getX(), blockpos.getY(), blockpos.getZ()), String.format("Chunk: %d %d %d in %d %d %d", blockpos.getX() & 0xF, blockpos.getY() & 0xF, blockpos.getZ() & 0xF, blockpos.getX() >> 4, blockpos.getY() >> 4, blockpos.getZ() >> 4), String.format("Facing: %s (%s) (%.1f / %.1f)", enumfacing, s, MathHelper.wrapDegrees(entity.rotationYaw), MathHelper.wrapDegrees(entity.rotationPitch)));
        if (this.mc.world != null) {
            final Chunk chunk = this.mc.world.getChunkFromBlockCoords(blockpos);
            if (this.mc.world.isBlockLoaded(blockpos) && blockpos.getY() >= 0 && blockpos.getY() < 256) {
                if (!chunk.isEmpty()) {
                    list.add("Biome: " + chunk.getBiome(blockpos, this.mc.world.getBiomeProvider()).getBiomeName());
                    list.add("Light: " + chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos) + " block)");
                    DifficultyInstance difficultyinstance = this.mc.world.getDifficultyForLocation(blockpos);
                    if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
                        final EntityPlayerMP entityplayermp = this.mc.getIntegratedServer().getPlayerList().getPlayerByUUID(this.mc.player.getUniqueID());
                        if (entityplayermp != null) {
                            difficultyinstance = entityplayermp.world.getDifficultyForLocation(new BlockPos(entityplayermp));
                        }
                    }
                    list.add(String.format("Local Difficulty: %.2f // %.2f (Day %d)", difficultyinstance.getAdditionalDifficulty(), difficultyinstance.getClampedAdditionalDifficulty(), this.mc.world.getWorldTime() / 24000L));
                }
                else {
                    list.add("Waiting for chunk...");
                }
            }
            else {
                list.add("Outside of world...");
            }
        }
        if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            list.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            final BlockPos blockpos2 = this.mc.objectMouseOver.getBlockPos();
            list.add(String.format("Looking at: %d %d %d", blockpos2.getX(), blockpos2.getY(), blockpos2.getZ()));
        }
        return list;
    }
    
    protected <T extends Comparable<T>> List<String> getDebugInfoRight() {
        final long i = Runtime.getRuntime().maxMemory();
        final long j = Runtime.getRuntime().totalMemory();
        final long k = Runtime.getRuntime().freeMemory();
        final long l = j - k;
        final List<String> list = Lists.newArrayList(String.format("Java: %s %dbit", System.getProperty("java.version"), this.mc.isJava64bit() ? 64 : 32), String.format("Mem: % 2d%% %03d/%03dMB", l * 100L / i, bytesToMb(l), bytesToMb(i)), String.format("Allocated: % 2d%% %03dMB", j * 100L / i, bytesToMb(j)), "", String.format("CPU: %s", OpenGlHelper.getCpu()), "", String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GlStateManager.glGetString(7936)), GlStateManager.glGetString(7937), GlStateManager.glGetString(7938));
        if (Reflector.FMLCommonHandler_getBrandings.exists()) {
            final Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            list.add("");
            list.addAll((Collection<? extends String>)Reflector.call(object, Reflector.FMLCommonHandler_getBrandings, false));
        }
        if (this.mc.isReducedDebug()) {
            return list;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            final BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
            IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
            if (this.mc.world.getWorldType() != WorldType.DEBUG_WORLD) {
                iblockstate = iblockstate.getActualState(this.mc.world, blockpos);
            }
            list.add("");
            list.add(String.valueOf(Block.REGISTRY.getNameForObject(iblockstate.getBlock())));
            for (final Map.Entry<IProperty<?>, Comparable<?>> entry : iblockstate.getProperties().entrySet()) {
                final IProperty<T> iproperty = (IProperty<T>)entry.getKey();
                final T t = (T)entry.getValue();
                String s = iproperty.getName(t);
                if (Boolean.TRUE.equals(t)) {
                    s = TextFormatting.GREEN + s;
                }
                else if (Boolean.FALSE.equals(t)) {
                    s = TextFormatting.RED + s;
                }
                list.add(String.valueOf(iproperty.getName()) + ": " + s);
            }
        }
        return list;
    }
    
    private void renderLagometer() {
    }
    
    private int getFrameColor(final int p_181552_1_, final int p_181552_2_, final int p_181552_3_, final int p_181552_4_) {
        return (p_181552_1_ < p_181552_3_) ? this.blendColors(-16711936, -256, p_181552_1_ / (float)p_181552_3_) : this.blendColors(-256, -65536, (p_181552_1_ - p_181552_3_) / (float)(p_181552_4_ - p_181552_3_));
    }
    
    private int blendColors(final int p_181553_1_, final int p_181553_2_, final float p_181553_3_) {
        final int i = p_181553_1_ >> 24 & 0xFF;
        final int j = p_181553_1_ >> 16 & 0xFF;
        final int k = p_181553_1_ >> 8 & 0xFF;
        final int l = p_181553_1_ & 0xFF;
        final int i2 = p_181553_2_ >> 24 & 0xFF;
        final int j2 = p_181553_2_ >> 16 & 0xFF;
        final int k2 = p_181553_2_ >> 8 & 0xFF;
        final int l2 = p_181553_2_ & 0xFF;
        final int i3 = MathHelper.clamp((int)(i + (i2 - i) * p_181553_3_), 0, 255);
        final int j3 = MathHelper.clamp((int)(j + (j2 - j) * p_181553_3_), 0, 255);
        final int k3 = MathHelper.clamp((int)(k + (k2 - k) * p_181553_3_), 0, 255);
        final int l3 = MathHelper.clamp((int)(l + (l2 - l) * p_181553_3_), 0, 255);
        return i3 << 24 | j3 << 16 | k3 << 8 | l3;
    }
    
    private static long bytesToMb(final long bytes) {
        return bytes / 1024L / 1024L;
    }
}
