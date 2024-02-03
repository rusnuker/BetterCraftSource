// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.gui;

import net.minecraft.util.IChatComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.FoodStats;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.labymod.api.permissions.Permissions;
import net.minecraft.entity.SharedMonsterAttributes;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import net.minecraft.scoreboard.Team;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import net.minecraft.scoreboard.Score;
import com.google.common.base.Predicate;
import net.minecraft.util.BlockPos;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.client.gui.Gui;
import net.labymod.main.LabyMod;
import net.minecraft.potion.Potion;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.labymod.core.LabyModCore;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiIngame;

public class GuiIngameCustom extends GuiIngame
{
    private static final ResourceLocation vignetteTexPath;
    private static final ResourceLocation widgetsTexPath;
    private static final ResourceLocation pumpkinBlurTexPath;
    private final Random rand;
    private final Minecraft mc;
    private final RenderItem itemRenderer;
    private final GuiNewChat persistantChatGUI;
    private final GuiOverlayDebug overlayDebug;
    private final GuiSpectator spectatorGui;
    private final GuiPlayerTabOverlay overlayPlayerList;
    public float prevVignetteBrightness;
    private int updateCounter;
    private String recordPlaying;
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack;
    private int field_175195_w;
    private String field_175201_x;
    private String field_175200_y;
    private int field_175199_z;
    private int field_175192_A;
    private int field_175193_B;
    private int playerHealth;
    private int lastPlayerHealth;
    private long lastSystemTime;
    private long healthUpdateCounter;
    
    static {
        vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
        widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
        pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    }
    
    public GuiIngameCustom(final Minecraft mcIn) {
        super(mcIn);
        this.rand = new Random();
        this.recordPlaying = "";
        this.prevVignetteBrightness = 1.0f;
        this.field_175201_x = "";
        this.field_175200_y = "";
        this.playerHealth = 0;
        this.lastPlayerHealth = 0;
        this.lastSystemTime = 0L;
        this.healthUpdateCounter = 0L;
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.spectatorGui = new GuiSpectator(mcIn);
        this.func_175177_a();
        this.persistantChatGUI = new GuiChatAdapter(mcIn);
        this.overlayPlayerList = new ModPlayerTabOverlay(mcIn, this);
    }
    
    @Override
    public FontRenderer getFontRenderer() {
        return LabyModCore.getMinecraft().getFontRenderer();
    }
    
    public void func_175177_a() {
        this.field_175199_z = 10;
        this.field_175192_A = 70;
        this.field_175193_B = 20;
    }
    
    @Override
    public void renderGameOverlay(final float partialTicks) {
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        final int i = scaledresolution.getScaledWidth();
        final int j = scaledresolution.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        if (Minecraft.isFancyGraphicsEnabled()) {
            this.renderVignetteNew(LabyModCore.getMinecraft().getPlayer().getBrightness(partialTicks), scaledresolution);
        }
        else {
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        final ItemStack itemstack = LabyModCore.getMinecraft().getPlayer().inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            this.renderPumpkinOverlayNew(scaledresolution);
        }
        if (!LabyModCore.getMinecraft().getPlayer().isPotionActive(Potion.confusion)) {
            final float f = LabyModCore.getMinecraft().getPlayer().prevTimeInPortal + (LabyModCore.getMinecraft().getPlayer().timeInPortal - LabyModCore.getMinecraft().getPlayer().prevTimeInPortal) * partialTicks;
            if (f > 0.0f) {
                this.renderPortalNew(f, scaledresolution);
            }
        }
        if (this.mc.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        }
        else {
            this.renderTooltip(scaledresolution, partialTicks);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiIngameCustom.icons);
        GlStateManager.enableBlend();
        if (this.showCrosshair() && !LabyMod.getInstance().getLabyModAPI().isCrosshairHidden()) {
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
        }
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealthNew();
        this.mc.mcProfiler.endSection();
        if (this.mc.playerController.shouldDrawHUD()) {
            this.renderPlayerStatsNew(scaledresolution);
        }
        GlStateManager.disableBlend();
        if (LabyModCore.getMinecraft().getPlayer().getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            final int j2 = LabyModCore.getMinecraft().getPlayer().getSleepTimer();
            float f2 = j2 / 100.0f;
            if (f2 > 1.0f) {
                f2 = 1.0f - (j2 - 100) / 10.0f;
            }
            final int k = (int)(220.0f * f2) << 24 | 0x101020;
            Gui.drawRect(0, 0, i, j, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int k2 = i / 2 - 91;
        if (LabyModCore.getMinecraft().getPlayer().isRidingHorse()) {
            this.renderHorseJumpBar(scaledresolution, k2);
        }
        else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledresolution, k2);
        }
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.renderSelectedItem(scaledresolution);
        }
        else if (LabyModCore.getMinecraft().getPlayer().isSpectator()) {
            this.spectatorGui.renderSelectedItem(scaledresolution);
        }
        if (this.mc.isDemo()) {
            this.renderDemo(scaledresolution);
        }
        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.renderDebugInfo(scaledresolution);
        }
        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            final float f3 = this.recordPlayingUpFor - partialTicks;
            int l1 = (int)(f3 * 255.0f / 20.0f);
            if (l1 > 255) {
                l1 = 255;
            }
            if (l1 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j - 68), 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                int m = 16777215;
                if (this.recordIsPlaying) {
                    m = (LabyModCore.getMath().hsvToRGB(f3 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF);
                }
                this.getFontRenderer().drawString(this.recordPlaying, -this.getFontRenderer().getStringWidth(this.recordPlaying) / 2, -4, m + (l1 << 24 & 0xFF000000));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        if (this.field_175195_w > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            final float f4 = this.field_175195_w - partialTicks;
            int i2 = 255;
            if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
                final float f5 = this.field_175199_z + this.field_175192_A + this.field_175193_B - f4;
                i2 = (int)(f5 * 255.0f / this.field_175199_z);
            }
            if (this.field_175195_w <= this.field_175193_B) {
                i2 = (int)(f4 * 255.0f / this.field_175193_B);
            }
            i2 = LabyModCore.getMath().clamp_int(i2, 0, 255);
            if (i2 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j / 2), 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0f, 4.0f, 4.0f);
                final int j3 = i2 << 24 & 0xFF000000;
                this.getFontRenderer().drawString(this.field_175201_x, (float)(-this.getFontRenderer().getStringWidth(this.field_175201_x) / 2), -10.0f, 0xFFFFFF | j3, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                this.getFontRenderer().drawString(this.field_175200_y, (float)(-this.getFontRenderer().getStringWidth(this.field_175200_y) / 2), 5.0f, 0xFFFFFF | j3, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        final Scoreboard scoreboard = LabyModCore.getMinecraft().getWorld().getScoreboard();
        ScoreObjective scoreobjective = null;
        final ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(LabyModCore.getMinecraft().getPlayer().getName());
        if (scoreplayerteam != null) {
            final int i3 = scoreplayerteam.getChatFormat().getColorIndex();
            if (i3 >= 0) {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i3);
            }
        }
        ScoreObjective scoreobjective2 = (scoreobjective != null) ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, (float)(j - 48), 0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective2 = scoreboard.getObjectiveInDisplaySlot(0);
        if (!this.mc.gameSettings.keyBindPlayerList.isKeyDown() || (this.mc.isIntegratedServerRunning() && LabyModCore.getMinecraft().getPlayer().sendQueue.getPlayerInfoMap().size() <= 1 && scoreobjective2 == null)) {
            this.overlayPlayerList.updatePlayerList(false);
        }
        else {
            this.overlayPlayerList.updatePlayerList(true);
            this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective2);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }
    
    @Override
    protected void renderTooltip(final ScaledResolution sr, final float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(GuiIngameCustom.widgetsTexPath);
            final EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            final int i = sr.getScaledWidth() / 2;
            final float f = this.zLevel;
            this.zLevel = -90.0f;
            this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            this.zLevel = f;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            for (int j = 0; j < 9; ++j) {
                final int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                final int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItemNew(j, k, l, partialTicks, entityplayer);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }
    
    @Override
    public void renderHorseJumpBar(final ScaledResolution p_175186_1_, final int p_175186_2_) {
        this.mc.mcProfiler.startSection("jumpBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        final float f = LabyModCore.getMinecraft().getPlayer().getHorseJumpPower();
        final int i = 182;
        final int j = (int)(f * 183.0f);
        final int k = p_175186_1_.getScaledHeight() - 32 + 3;
        this.drawTexturedModalRect(p_175186_2_, k, 0, 84, 182, 5);
        if (j > 0) {
            this.drawTexturedModalRect(p_175186_2_, k, 0, 89, j, 5);
        }
        this.mc.mcProfiler.endSection();
    }
    
    @Override
    public void renderExpBar(final ScaledResolution p_175176_1_, final int p_175176_2_) {
        this.mc.mcProfiler.startSection("expBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        final int i = LabyModCore.getMinecraft().getPlayer().xpBarCap();
        if (i > 0) {
            final int j = 182;
            final int k = (int)(LabyModCore.getMinecraft().getPlayer().experience * 183.0f);
            final int l = p_175176_1_.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(p_175176_2_, l, 0, 64, 182, 5);
            if (k > 0) {
                this.drawTexturedModalRect(p_175176_2_, l, 0, 69, k, 5);
            }
        }
        this.mc.mcProfiler.endSection();
        if (LabyModCore.getMinecraft().getPlayer().experienceLevel > 0) {
            this.mc.mcProfiler.startSection("expLevel");
            final int k2 = 8453920;
            final String s = new StringBuilder().append(LabyModCore.getMinecraft().getPlayer().experienceLevel).toString();
            final int l2 = (p_175176_1_.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            final int i2 = p_175176_1_.getScaledHeight() - 31 - 4;
            this.getFontRenderer().drawString(s, l2 + 1, i2, 0);
            this.getFontRenderer().drawString(s, l2 - 1, i2, 0);
            this.getFontRenderer().drawString(s, l2, i2 + 1, 0);
            this.getFontRenderer().drawString(s, l2, i2 - 1, 0);
            this.getFontRenderer().drawString(s, l2, i2, 8453920);
            this.mc.mcProfiler.endSection();
        }
    }
    
    @Override
    public void renderSelectedItem(final ScaledResolution p_181551_1_) {
        this.mc.mcProfiler.startSection("selectedItemName");
        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            String s = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                s = EnumChatFormatting.ITALIC + s;
            }
            final int i = (p_181551_1_.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int j = p_181551_1_.getScaledHeight() - 59;
            if (!this.mc.playerController.shouldDrawHUD()) {
                j += 14;
            }
            int k = (int)(this.remainingHighlightTicks * 256.0f / 10.0f);
            if (k > 255) {
                k = 255;
            }
            if (k > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.getFontRenderer().drawStringWithShadow(s, (float)i, (float)j, 16777215 + (k << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
        this.mc.mcProfiler.endSection();
    }
    
    @Override
    public void renderDemo(final ScaledResolution p_175185_1_) {
        this.mc.mcProfiler.startSection("demo");
        String s = "";
        if (LabyModCore.getMinecraft().getWorld().getTotalWorldTime() >= 120500L) {
            s = I18n.format("demo.demoExpired", new Object[0]);
        }
        else {
            s = I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - LabyModCore.getMinecraft().getWorld().getTotalWorldTime())));
        }
        final int i = this.getFontRenderer().getStringWidth(s);
        this.getFontRenderer().drawStringWithShadow(s, (float)(p_175185_1_.getScaledWidth() - i - 10), 5.0f, 16777215);
        this.mc.mcProfiler.endSection();
    }
    
    @Override
    protected boolean showCrosshair() {
        if (this.mc.gameSettings.showDebugInfo && !LabyModCore.getMinecraft().getPlayer().hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
            return false;
        }
        if (!this.mc.playerController.isSpectator()) {
            return true;
        }
        if (this.mc.pointedEntity != null) {
            return true;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
            return LabyModCore.getMinecraft().getWorld().getTileEntity(blockpos) instanceof IInventory;
        }
        return false;
    }
    
    @Override
    public void renderStreamIndicator(final ScaledResolution p_180478_1_) {
    }
    
    private void renderScoreboardNew(final ScoreObjective p_180475_1_, final ScaledResolution p_180475_2_) {
        final Scoreboard scoreboard = p_180475_1_.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(p_180475_1_);
        final List<Score> list = (List<Score>)Lists.newArrayList((Iterable<?>)Iterables.filter((Iterable<? extends E>)collection, (Predicate<? super E>)new Predicate<Score>() {
            @Override
            public boolean apply(final Score p_apply_1_) {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }
        }));
        if (list.size() > 15) {
            collection = (Collection<Score>)Lists.newArrayList((Iterable<?>)Iterables.skip((Iterable<? extends E>)list, collection.size() - 15));
        }
        else {
            collection = list;
        }
        int i = this.getFontRenderer().getStringWidth(p_180475_1_.getDisplayName());
        for (final Score score : collection) {
            final ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            final String s = String.valueOf(ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName())) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            i = Math.max(i, this.getFontRenderer().getStringWidth(s));
        }
        final int i2 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
        final int j1 = p_180475_2_.getScaledHeight() / 2 + i2 / 3;
        final int k1 = 3;
        final int l1 = p_180475_2_.getScaledWidth() - i - 3;
        int m = 0;
        for (final Score score2 : collection) {
            ++m;
            final ScorePlayerTeam scoreplayerteam2 = scoreboard.getPlayersTeam(score2.getPlayerName());
            final String s2 = ScorePlayerTeam.formatPlayerName(scoreplayerteam2, score2.getPlayerName());
            final String s3 = new StringBuilder().append(EnumChatFormatting.RED).append(score2.getScorePoints()).toString();
            final int k2 = j1 - m * this.getFontRenderer().FONT_HEIGHT;
            final int l2 = p_180475_2_.getScaledWidth() - 3 + 2;
            Gui.drawRect(l1 - 2, k2, l2, k2 + this.getFontRenderer().FONT_HEIGHT, 1342177280);
            this.getFontRenderer().drawString(s2, l1, k2, 553648127);
            this.getFontRenderer().drawString(s3, l2 - this.getFontRenderer().getStringWidth(s3), k2, 553648127);
            if (m == collection.size()) {
                final String s4 = p_180475_1_.getDisplayName();
                Gui.drawRect(l1 - 2, k2 - this.getFontRenderer().FONT_HEIGHT - 1, l2, k2 - 1, 1610612736);
                Gui.drawRect(l1 - 2, k2 - 1, l2, k2, 1342177280);
                this.getFontRenderer().drawString(s4, l1 + i / 2 - this.getFontRenderer().getStringWidth(s4) / 2, k2 - this.getFontRenderer().FONT_HEIGHT, 553648127);
            }
        }
    }
    
    private void renderPlayerStatsNew(final ScaledResolution p_180477_1_) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            final int i = LabyModCore.getMath().ceiling_float_int(entityplayer.getHealth());
            final boolean flag = this.healthUpdateCounter > this.updateCounter && (this.healthUpdateCounter - this.updateCounter) / 3L % 2L == 1L;
            if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 20;
            }
            else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 10;
            }
            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = i;
                this.lastPlayerHealth = i;
                this.lastSystemTime = Minecraft.getSystemTime();
            }
            this.playerHealth = i;
            final int j = this.lastPlayerHealth;
            this.rand.setSeed(this.updateCounter * 312871);
            final boolean flag2 = false;
            final FoodStats foodstats = entityplayer.getFoodStats();
            final int k = foodstats.getFoodLevel();
            final int l = foodstats.getPrevFoodLevel();
            final float saturation = foodstats.getSaturationLevel();
            boolean hasSaturation = false;
            final IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            final int i2 = p_180477_1_.getScaledWidth() / 2 - 91;
            final int j2 = p_180477_1_.getScaledWidth() / 2 + 91;
            int k2 = p_180477_1_.getScaledHeight() - 39;
            final float f = (float)iattributeinstance.getAttributeValue();
            final float f2 = entityplayer.getAbsorptionAmount();
            final int l2 = LabyModCore.getMath().ceiling_float_int((f + f2) / 2.0f / 10.0f);
            final int i3 = Math.max(10 - (l2 - 2), 3);
            final int j3 = k2 - (l2 - 1) * i3 - 10;
            float f3 = f2;
            final int k3 = entityplayer.getTotalArmorValue();
            int l3 = -1;
            if (entityplayer.isPotionActive(Potion.regeneration)) {
                l3 = this.updateCounter % LabyModCore.getMath().ceiling_float_int(f + 5.0f);
            }
            this.mc.mcProfiler.startSection("armor");
            this.mc.getTextureManager().bindTexture(GuiIngameCustom.icons);
            for (int i4 = 0; i4 < 10; ++i4) {
                if (k3 > 0) {
                    final int j4 = i2 + i4 * 8;
                    if (i4 * 2 + 1 < k3) {
                        this.drawTexturedModalRect(j4, j3, 34, 9, 9, 9);
                    }
                    if (i4 * 2 + 1 == k3) {
                        this.drawTexturedModalRect(j4, j3, 25, 9, 9, 9);
                    }
                    if (i4 * 2 + 1 > k3) {
                        this.drawTexturedModalRect(j4, j3, 16, 9, 9, 9);
                    }
                }
            }
            this.mc.mcProfiler.endStartSection("health");
            for (int i5 = LabyModCore.getMath().ceiling_float_int((f + f2) / 2.0f) - 1; i5 >= 0; --i5) {
                int j5 = 16;
                if (entityplayer.isPotionActive(Potion.poison)) {
                    j5 += 36;
                }
                else if (entityplayer.isPotionActive(Potion.wither)) {
                    j5 += 72;
                }
                int k4 = 0;
                if (flag) {
                    k4 = 1;
                }
                final int l4 = LabyModCore.getMath().ceiling_float_int((i5 + 1) / 10.0f) - 1;
                final int i6 = i2 + i5 % 10 * 8;
                int j6 = k2 - l4 * i3;
                if (i <= 4) {
                    j6 += this.rand.nextInt(2);
                }
                if (i5 == l3) {
                    j6 -= 2;
                }
                int k5 = 0;
                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    k5 = 5;
                }
                this.drawTexturedModalRect(i6, j6, 16 + k4 * 9, 9 * k5, 9, 9);
                if ((!LabyMod.getSettings().oldHearts || !Permissions.isAllowed(Permissions.Permission.ANIMATIONS)) && flag) {
                    if (i5 * 2 + 1 < j) {
                        this.drawTexturedModalRect(i6, j6, j5 + 54, 9 * k5, 9, 9);
                    }
                    if (i5 * 2 + 1 == j) {
                        this.drawTexturedModalRect(i6, j6, j5 + 63, 9 * k5, 9, 9);
                    }
                }
                if (f3 > 0.0f) {
                    if (f3 == f2 && f2 % 2.0f == 1.0f) {
                        this.drawTexturedModalRect(i6, j6, j5 + 153, 9 * k5, 9, 9);
                    }
                    else {
                        this.drawTexturedModalRect(i6, j6, j5 + 144, 9 * k5, 9, 9);
                    }
                    f3 -= 2.0f;
                }
                else {
                    if (i5 * 2 + 1 < i) {
                        this.drawTexturedModalRect(i6, j6, j5 + 36, 9 * k5, 9, 9);
                    }
                    if (i5 * 2 + 1 == i) {
                        this.drawTexturedModalRect(i6, j6, j5 + 45, 9 * k5, 9, 9);
                    }
                }
            }
            final Entity entity = entityplayer.ridingEntity;
            this.mc.getTextureManager().bindTexture(GuiIngameCustom.icons);
            if (entity == null) {
                this.mc.mcProfiler.endStartSection("food");
                for (int k6 = 0; k6 < 10; ++k6) {
                    int i7 = k2;
                    int l5 = 16;
                    int j7 = 0;
                    if (entityplayer.isPotionActive(Potion.hunger)) {
                        l5 += 36;
                        j7 = 13;
                    }
                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0f && this.updateCounter % (k * 3 + 1) == 0) {
                        i7 = k2 + (this.rand.nextInt(3) - 1);
                    }
                    final int i8 = j2 - k6 * 8 - 9;
                    this.drawTexturedModalRect(i8, i7, 16 + j7 * 9, 27, 9, 9);
                    if (k6 * 2 + 1 < k) {
                        this.drawTexturedModalRect(i8, i7, l5 + 36, 27, 9, 9);
                    }
                    if (k6 * 2 + 1 == k) {
                        this.drawTexturedModalRect(i8, i7, l5 + 45, 27, 9, 9);
                    }
                }
                if (LabyMod.getSettings().showSaturation && Permissions.isAllowed(Permissions.Permission.SATURATION_BAR)) {
                    for (int k6 = 0; k6 < saturation / 2.0f; ++k6) {
                        int i7 = k2 - 10;
                        int l5 = 16;
                        int j7 = 0;
                        if (entityplayer.isPotionActive(Potion.hunger)) {
                            l5 += 36;
                            j7 = 13;
                        }
                        if (saturation <= 0.0f && this.updateCounter % (saturation * 3.0f + 1.0f) == 0.0f) {
                            i7 = k2 + (this.rand.nextInt(3) - 1);
                        }
                        hasSaturation = true;
                        final int i8 = j2 - k6 * 8 - 9;
                        this.drawTexturedModalRect(i8, i7, 16 + j7 * 9, 27, 9, 9);
                        if (k6 * 2 + 1 < saturation) {
                            this.drawTexturedModalRect(i8, i7, l5 + 36, 27, 9, 9);
                        }
                        if (k6 * 2 + 1 == saturation) {
                            this.drawTexturedModalRect(i8, i7, l5 + 45, 27, 9, 9);
                        }
                    }
                }
            }
            else if (entity instanceof EntityLivingBase) {
                this.mc.mcProfiler.endStartSection("mountHealth");
                final EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                final int j8 = (int)Math.ceil(entitylivingbase.getHealth());
                final float f4 = entitylivingbase.getMaxHealth();
                int k7 = (int)(f4 + 0.5f) / 2;
                if (k7 > 30) {
                    k7 = 30;
                }
                int j9 = k2;
                int k8 = 0;
                while (k7 > 0) {
                    final int l6 = Math.min(k7, 10);
                    k7 -= l6;
                    for (int i9 = 0; i9 < l6; ++i9) {
                        final int j10 = 52;
                        final int k9 = 0;
                        final int l7 = j2 - i9 * 8 - 9;
                        this.drawTexturedModalRect(l7, j9, 52 + k9 * 9, 9, 9, 9);
                        if (i9 * 2 + 1 + k8 < j8) {
                            this.drawTexturedModalRect(l7, j9, 88, 9, 9, 9);
                        }
                        if (i9 * 2 + 1 + k8 == j8) {
                            this.drawTexturedModalRect(l7, j9, 97, 9, 9, 9);
                        }
                    }
                    j9 -= 10;
                    k8 += 20;
                }
            }
            this.mc.mcProfiler.endStartSection("air");
            if (entityplayer.isInsideOfMaterial(Material.water)) {
                final int l8 = LabyModCore.getMinecraft().getPlayer().getAir();
                final int k10 = LabyModCore.getMath().ceiling_double_int((l8 - 2) * 10.0 / 300.0);
                final int i10 = LabyModCore.getMath().ceiling_double_int(l8 * 10.0 / 300.0) - k10;
                k2 -= 10;
                if (hasSaturation) {
                    k2 -= 10;
                }
                for (int l9 = 0; l9 < k10 + i10; ++l9) {
                    if (l9 < k10) {
                        this.drawTexturedModalRect(j2 - l9 * 8 - 9, k2, 16, 18, 9, 9);
                    }
                    else {
                        this.drawTexturedModalRect(j2 - l9 * 8 - 9, k2, 25, 18, 9, 9);
                    }
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }
    
    private void renderBossHealthNew() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            final int i = scaledresolution.getScaledWidth();
            final int j = 182;
            final int k = i / 2 - 91;
            final int l = (int)(BossStatus.healthScale * 183.0f);
            final int i2 = 12;
            if (LabyMod.getSettings().showBossBar) {
                this.drawTexturedModalRect(k, 12, 0, 74, 182, 5);
                this.drawTexturedModalRect(k, 12, 0, 74, 182, 5);
                this.drawTexturedModalRect(k, 12, 0, 74, 182, 5);
                this.drawTexturedModalRect(k, 12, 0, 74, 182, 5);
                if (l > 0) {
                    this.drawTexturedModalRect(k, 12, 0, 79, l, 5);
                }
            }
            final String s = BossStatus.bossName;
            this.getFontRenderer().drawStringWithShadow(s, (float)(i / 2 - this.getFontRenderer().getStringWidth(s) / 2), 2.0f, 16777215);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(GuiIngameCustom.icons);
        }
    }
    
    private void renderPumpkinOverlayNew(final ScaledResolution p_180476_1_) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(GuiIngameCustom.pumpkinBlurTexPath);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, p_180476_1_.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(p_180476_1_.getScaledWidth(), p_180476_1_.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(p_180476_1_.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderVignetteNew(float p_180480_1_, final ScaledResolution p_180480_2_) {
        p_180480_1_ = 1.0f - p_180480_1_;
        p_180480_1_ = LabyModCore.getMath().clamp_float(p_180480_1_, 0.0f, 1.0f);
        final WorldBorder worldborder = LabyModCore.getMinecraft().getWorld().getWorldBorder();
        float f = (float)worldborder.getClosestDistance(LabyModCore.getMinecraft().getPlayer());
        final double d0 = Math.min(worldborder.getResizeSpeed() * worldborder.getWarningTime() * 1000.0, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
        final double d2 = Math.max(worldborder.getWarningDistance(), d0);
        if (f < d2) {
            f = 1.0f - (float)(f / d2);
        }
        else {
            f = 0.0f;
        }
        this.prevVignetteBrightness += (float)((p_180480_1_ - this.prevVignetteBrightness) * 0.01);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
        if (f > 0.0f) {
            GlStateManager.color(0.0f, f, f, 1.0f);
        }
        else {
            GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0f);
        }
        this.mc.getTextureManager().bindTexture(GuiIngameCustom.vignetteTexPath);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, p_180480_2_.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(p_180480_2_.getScaledWidth(), p_180480_2_.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(p_180480_2_.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }
    
    private void renderPortalNew(float p_180474_1_, final ScaledResolution p_180474_2_) {
        if (p_180474_1_ < 1.0f) {
            p_180474_1_ *= p_180474_1_;
            p_180474_1_ *= p_180474_1_;
            p_180474_1_ = p_180474_1_ * 0.8f + 0.2f;
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, p_180474_1_);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        final TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        final float f = textureatlassprite.getMinU();
        final float f2 = textureatlassprite.getMinV();
        final float f3 = textureatlassprite.getMaxU();
        final float f4 = textureatlassprite.getMaxV();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, p_180474_2_.getScaledHeight(), -90.0).tex(f, f4).endVertex();
        worldrenderer.pos(p_180474_2_.getScaledWidth(), p_180474_2_.getScaledHeight(), -90.0).tex(f3, f4).endVertex();
        worldrenderer.pos(p_180474_2_.getScaledWidth(), 0.0, -90.0).tex(f3, f2).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(f, f2).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderHotbarItemNew(final int index, final int xPos, final int yPos, final float partialTicks, final EntityPlayer p_175184_5_) {
        final ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];
        if (itemstack != null) {
            final float f = itemstack.animationsToGo - partialTicks;
            if (f > 0.0f) {
                GlStateManager.pushMatrix();
                final float f2 = 1.0f + f / 5.0f;
                GlStateManager.translate((float)(xPos + 8), (float)(yPos + 12), 0.0f);
                GlStateManager.scale(1.0f / f2, (f2 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate((float)(-(xPos + 8)), (float)(-(yPos + 12)), 0.0f);
            }
            this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
            if (f > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.itemRenderer.renderItemOverlays(LabyModCore.getMinecraft().getFontRenderer(), itemstack, xPos, yPos);
        }
    }
    
    @Override
    public void updateTick() {
        if (this.recordPlayingUpFor > 0) {
            --this.recordPlayingUpFor;
        }
        if (this.field_175195_w > 0) {
            --this.field_175195_w;
            if (this.field_175195_w <= 0) {
                this.field_175201_x = "";
                this.field_175200_y = "";
            }
        }
        ++this.updateCounter;
        if (LabyModCore.getMinecraft().getPlayer() != null) {
            final ItemStack itemstack = LabyModCore.getMinecraft().getPlayer().inventory.getCurrentItem();
            if (itemstack == null) {
                this.remainingHighlightTicks = 0;
            }
            else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            }
            else {
                this.remainingHighlightTicks = 40;
            }
            this.highlightingItemStack = itemstack;
        }
    }
    
    @Override
    public void setRecordPlayingMessage(final String p_73833_1_) {
        this.setRecordPlaying(I18n.format("record.nowPlaying", p_73833_1_), true);
    }
    
    @Override
    public void setRecordPlaying(final String p_110326_1_, final boolean p_110326_2_) {
        this.recordPlaying = p_110326_1_;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = p_110326_2_;
    }
    
    @Override
    public void displayTitle(final String p_175178_1_, final String p_175178_2_, final int p_175178_3_, final int p_175178_4_, final int p_175178_5_) {
        if (p_175178_1_ == null && p_175178_2_ == null && p_175178_3_ < 0 && p_175178_4_ < 0 && p_175178_5_ < 0) {
            this.field_175201_x = "";
            this.field_175200_y = "";
            this.field_175195_w = 0;
        }
        else if (p_175178_1_ != null) {
            this.field_175201_x = p_175178_1_;
            this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
        }
        else if (p_175178_2_ != null) {
            this.field_175200_y = p_175178_2_;
        }
        else {
            if (p_175178_3_ >= 0) {
                this.field_175199_z = p_175178_3_;
            }
            if (p_175178_4_ >= 0) {
                this.field_175192_A = p_175178_4_;
            }
            if (p_175178_5_ >= 0) {
                this.field_175193_B = p_175178_5_;
            }
            if (this.field_175195_w > 0) {
                this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
            }
        }
    }
    
    @Override
    public void setRecordPlaying(final IChatComponent p_175188_1_, final boolean p_175188_2_) {
        this.setRecordPlaying(p_175188_1_.getUnformattedText(), p_175188_2_);
    }
    
    @Override
    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }
    
    @Override
    public int getUpdateCounter() {
        return this.updateCounter;
    }
    
    @Override
    public GuiSpectator getSpectatorGui() {
        return this.spectatorGui;
    }
    
    @Override
    public GuiPlayerTabOverlay getTabList() {
        return this.overlayPlayerList;
    }
    
    public void func_181029_i() {
    }
}
