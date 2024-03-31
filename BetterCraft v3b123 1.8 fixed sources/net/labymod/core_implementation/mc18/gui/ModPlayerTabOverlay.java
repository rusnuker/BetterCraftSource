// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.gui;

import com.google.common.collect.ComparisonChain;
import net.labymod.utils.DrawUtils;
import net.minecraft.scoreboard.Score;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.labymod.user.group.LabyGroup;
import net.labymod.user.User;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.labymod.user.FamiliarManager;
import net.labymod.user.UserManager;
import net.labymod.utils.ModColor;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.labymod.core.LabyModCore;
import net.labymod.api.permissions.Permissions;
import net.labymod.main.LabyMod;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.labymod.utils.manager.TagManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import java.util.Comparator;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.GuiPlayerTabOverlay;

public class ModPlayerTabOverlay extends GuiPlayerTabOverlay
{
    private static final Ordering<NetworkPlayerInfo> field_175252_a;
    private final Minecraft mc;
    private final GuiIngame guiIngame;
    private IChatComponent footer;
    private IChatComponent header;
    private long lastTimeOpened;
    private boolean isBeingRendered;
    
    static {
        field_175252_a = Ordering.from((Comparator<NetworkPlayerInfo>)new PlayerComparator(null));
    }
    
    public ModPlayerTabOverlay(final Minecraft mcIn, final GuiIngame guiIngameIn) {
        super(mcIn, guiIngameIn);
        this.mc = mcIn;
        this.guiIngame = guiIngameIn;
    }
    
    @Override
    public String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String name = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        final String tagName = TagManager.getTaggedMessage(name);
        if (tagName != null) {
            return tagName;
        }
        return name;
    }
    
    @Override
    public void updatePlayerList(final boolean willBeRendered) {
        if (willBeRendered && !this.isBeingRendered) {
            this.lastTimeOpened = Minecraft.getSystemTime();
        }
        this.isBeingRendered = willBeRendered;
    }
    
    @Override
    public void renderPlayerlist(final int width, final Scoreboard scoreboardIn, final ScoreObjective scoreObjectiveIn) {
        if (LabyMod.getSettings().oldTablist && Permissions.isAllowed(Permissions.Permission.ANIMATIONS)) {
            this.oldTabOverlay(width, scoreboardIn, scoreObjectiveIn);
        }
        else {
            this.newTabOverlay(width, scoreboardIn, scoreObjectiveIn);
        }
    }
    
    public void newTabOverlay(final int width, final Scoreboard scoreboardIn, final ScoreObjective scoreObjectiveIn) {
        final UserManager userManager = LabyMod.getInstance().getUserManager();
        final FamiliarManager familiarManager = userManager.getFamiliarManager();
        int familiarCount = 0;
        int totalCount = 0;
        final NetHandlerPlayClient nethandlerplayclient = LabyModCore.getMinecraft().getPlayer().sendQueue;
        List<NetworkPlayerInfo> var5 = ModPlayerTabOverlay.field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
        int var6 = 0;
        int var7 = 0;
        for (final NetworkPlayerInfo networkplayerinfo : var5) {
            int k = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(this.getPlayerName(networkplayerinfo));
            if (LabyMod.getSettings().revealFamiliarUsers) {
                final UUID uuid = networkplayerinfo.getGameProfile().getId();
                if (familiarManager.isFamiliar(uuid)) {
                    k += 10;
                    ++familiarCount;
                }
                ++totalCount;
            }
            var6 = Math.max(var6, k);
            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                k = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                var7 = Math.max(var7, k);
            }
        }
        var5 = var5.subList(0, Math.min(var5.size(), 80));
        int var9;
        int var8;
        int j4;
        for (var8 = (var9 = var5.size()), j4 = 1; var8 > 20; var8 = (var9 + j4 - 1) / j4) {
            ++j4;
        }
        final boolean var10 = this.mc.isIntegratedServerRunning() || LabyModCore.getMinecraft().getConnection().getNetworkManager().getIsencrypted();
        int var11;
        if (scoreObjectiveIn != null) {
            if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                var11 = 90;
            }
            else {
                var11 = var7;
            }
        }
        else {
            var11 = 0;
        }
        final int var12 = Math.min(j4 * ((var10 ? 9 : 0) + var6 + var11 + 13), width - 50) / j4;
        final int var13 = width / 2 - (var12 * j4 + (j4 - 1) * 5) / 2;
        int var14 = 10;
        int var15 = var12 * j4 + (j4 - 1) * 5;
        List<String> var16 = null;
        List<String> var17 = null;
        if (this.header != null) {
            var16 = LabyModCore.getMinecraft().getFontRenderer().listFormattedStringToWidth(this.header.getFormattedText(), width - 50);
            for (final String s : var16) {
                var15 = Math.max(var15, LabyModCore.getMinecraft().getFontRenderer().getStringWidth(s));
            }
        }
        if (this.footer != null) {
            var17 = LabyModCore.getMinecraft().getFontRenderer().listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);
            for (final String s2 : var17) {
                var15 = Math.max(var15, LabyModCore.getMinecraft().getFontRenderer().getStringWidth(s2));
            }
        }
        if (var16 != null) {
            Gui.drawRect(width / 2 - var15 / 2 - 1, var14 - 1, width / 2 + var15 / 2 + 2, var14 + var16.size() * LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT, Integer.MIN_VALUE);
            for (final String s3 : var16) {
                final int i2 = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(s3);
                LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(s3, (float)(width / 2 - i2 / 2), (float)var14, -1);
                var14 += LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT;
            }
            ++var14;
        }
        Gui.drawRect(width / 2 - var15 / 2 - 1, var14 - 1, width / 2 + var15 / 2 + 2, var14 + var8 * 9, Integer.MIN_VALUE);
        for (int var18 = 0; var18 < var9; ++var18) {
            final int var19 = var18 / var8;
            final int var20 = var18 % var8;
            int var21 = var13 + var19 * var12 + var19 * 5;
            final int var22 = var14 + var20 * 9;
            Gui.drawRect(var21, var22, var21 + var12, var22 + 8, 553648127);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            if (var18 < var5.size()) {
                final NetworkPlayerInfo networkplayerinfo2 = var5.get(var18);
                String s4 = this.getPlayerName(networkplayerinfo2);
                final GameProfile gameprofile = networkplayerinfo2.getGameProfile();
                if (var10) {
                    final EntityPlayer entityplayer = LabyModCore.getMinecraft().getWorld().getPlayerEntityByUUID(gameprofile.getId());
                    final boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
                    this.mc.getTextureManager().bindTexture(networkplayerinfo2.getLocationSkin());
                    final int l2 = 8 + (flag1 ? 8 : 0);
                    final int i3 = 8 * (flag1 ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect(var21, var22, 8.0f, (float)l2, 8, i3, 8, 8, 64.0f, 64.0f);
                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                        final int j5 = 8 + (flag1 ? 8 : 0);
                        final int k2 = 8 * (flag1 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(var21, var22, 40.0f, (float)j5, 8, k2, 8, 8, 64.0f, 64.0f);
                    }
                    var21 += 9;
                }
                if (networkplayerinfo2.getGameType() == WorldSettings.GameType.SPECTATOR) {
                    s4 = EnumChatFormatting.ITALIC + s4;
                    LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(s4, (float)var21, (float)var22, -1862270977);
                }
                else {
                    boolean badgeVisible = false;
                    if (LabyMod.getSettings().revealFamiliarUsers) {
                        final User user = userManager.getUser(networkplayerinfo2.getGameProfile().getId());
                        if (user.isFamiliar()) {
                            final LabyGroup group = user.getGroup();
                            if (group != null) {
                                group.renderBadge(var21 - 1, var22, 8.0, 8.0, true);
                            }
                            badgeVisible = true;
                        }
                    }
                    LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(s4, (float)(var21 + (badgeVisible ? 8 : 0)), (float)var22, -1);
                }
                if (scoreObjectiveIn != null && networkplayerinfo2.getGameType() != WorldSettings.GameType.SPECTATOR) {
                    final int var23 = var21 + var6 + 1;
                    final int var24 = var23 + var11;
                    if (var24 - var23 > 5) {
                        this.drawScoreboardValues(scoreObjectiveIn, var22, gameprofile.getName(), var23, var24, networkplayerinfo2);
                    }
                }
                this.drawPing(var12, var21 - (var10 ? 9 : 0), var22, networkplayerinfo2);
            }
        }
        if (var17 != null) {
            var14 = var14 + var8 * 9 + 1;
            Gui.drawRect(width / 2 - var15 / 2 - 1, var14 - 1, width / 2 + var15 / 2 + 2, var14 + var17.size() * LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT, Integer.MIN_VALUE);
            for (final String s5 : var17) {
                final int j6 = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(s5);
                LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(s5, (float)(width / 2 - j6 / 2), (float)var14, -1);
                var14 += LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT;
            }
        }
        if (LabyMod.getSettings().revealFamiliarUsers && LabyMod.getSettings().revealFamiliarUsersPercentage) {
            final int percent = (int)((totalCount == 0) ? 0L : Math.round(100.0 / totalCount * familiarCount));
            final String displayString = String.valueOf(ModColor.cl('7')) + familiarCount + ModColor.cl('8') + "/" + ModColor.cl('7') + totalCount + " " + ModColor.cl('a') + percent + "%";
            LabyMod.getInstance().getDrawUtils().drawRightString(displayString, width / 2 + var15 / 2, 3.0, 0.7);
        }
    }
    
    public void oldTabOverlay(final int width, final Scoreboard scoreboardIn, final ScoreObjective scoreObjectiveIn) {
        final UserManager userManager = LabyMod.getInstance().getUserManager();
        final FamiliarManager familiarManager = userManager.getFamiliarManager();
        int familiarCount = 0;
        int totalCount = 0;
        try {
            final NetHandlerPlayClient var4 = LabyModCore.getMinecraft().getPlayer().sendQueue;
            final List<?> var5 = ModPlayerTabOverlay.field_175252_a.sortedCopy((Iterable<?>)var4.getPlayerInfoMap());
            final int var7;
            int var6 = var7 = LabyModCore.getMinecraft().getPlayer().sendQueue.currentServerMaxPlayers;
            final ScaledResolution var8 = new ScaledResolution(Minecraft.getMinecraft());
            int var9 = 0;
            final int var10 = var8.getScaledWidth();
            int var11 = 0;
            int var12 = 0;
            int var13 = 0;
            for (var9 = 1; var6 > 20; var6 = (var7 + var9 - 1) / var9) {
                ++var9;
            }
            int var14 = 300 / var9;
            if (var14 > 150) {
                var14 = 150;
            }
            final int var15 = (var10 - var9 * var14) / 2;
            final byte var16 = 10;
            Gui.drawRect(var15 - 1, 9, var15 + var14 * var9, 10 + 9 * var6, Integer.MIN_VALUE);
            for (var11 = 0; var11 < var7; ++var11) {
                var12 = var15 + var11 % var9 * var14;
                var13 = 10 + var11 / var9 * 9;
                Gui.drawRect(var12, var13, var12 + var14 - 1, var13 + 8, 553648127);
                GlStateManager.enableAlpha();
                if (var11 < var5.size()) {
                    final NetworkPlayerInfo var17 = (NetworkPlayerInfo)var5.get(var11);
                    final String name = var17.getGameProfile().getName();
                    final ScorePlayerTeam var18 = LabyModCore.getMinecraft().getWorld().getScoreboard().getPlayersTeam(name);
                    final String var19 = this.getPlayerName(var17);
                    final boolean badgeVisible = false;
                    if (LabyMod.getSettings().revealFamiliarUsers) {
                        final User user = userManager.getUser(var17.getGameProfile().getId());
                        if (user.isFamiliar()) {
                            final LabyGroup group = user.getGroup();
                            if (group != null) {
                                group.renderBadge(var12, var13, 8.0, 8.0, true);
                            }
                            ++familiarCount;
                        }
                    }
                    ++totalCount;
                    LabyMod.getInstance().getDrawUtils().drawString(var19, var12 + 0, var13);
                    if (scoreObjectiveIn != null) {
                        final int var20 = var12 + LabyMod.getInstance().getDrawUtils().getStringWidth(var19) + 5;
                        final int var21 = var12 + var14 - 12 - 5;
                        if (var21 - var20 > 5) {
                            final Score var22 = scoreboardIn.getValueFromObjective(name, scoreObjectiveIn);
                            final String var23 = new StringBuilder().append(EnumChatFormatting.YELLOW).append(var22.getScorePoints()).toString();
                            LabyMod.getInstance().getDrawUtils().drawString(var23, var21 - LabyMod.getInstance().getDrawUtils().getStringWidth(var23), var13, 1.6777215E7);
                        }
                    }
                    this.drawPing(50, var12 + var14 - 52, var13, var17);
                }
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.enableAlpha();
            if (LabyMod.getSettings().revealFamiliarUsers && LabyMod.getSettings().revealFamiliarUsersPercentage) {
                final int percent = (int)((totalCount == 0) ? 0L : Math.round(100.0 / totalCount * familiarCount));
                final String displayString = String.valueOf(ModColor.cl('7')) + familiarCount + ModColor.cl('8') + "/" + ModColor.cl('7') + totalCount + " " + ModColor.cl('a') + percent + "%";
                LabyMod.getInstance().getDrawUtils().drawRightString(displayString, var15 + var14 * var9, 3.0, 0.7);
            }
        }
        catch (final Exception ex) {}
    }
    
    @Override
    protected void drawPing(final int p_175245_1_, final int p_175245_2_, final int p_175245_3_, final NetworkPlayerInfo networkPlayerInfoIn) {
        if (!LabyMod.getSettings().tabPing) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(ModPlayerTabOverlay.icons);
            final byte var5 = 0;
            byte var6;
            if (networkPlayerInfoIn.getResponseTime() < 0) {
                var6 = 5;
            }
            else if (networkPlayerInfoIn.getResponseTime() < 150) {
                var6 = 0;
            }
            else if (networkPlayerInfoIn.getResponseTime() < 300) {
                var6 = 1;
            }
            else if (networkPlayerInfoIn.getResponseTime() < 600) {
                var6 = 2;
            }
            else if (networkPlayerInfoIn.getResponseTime() < 1000) {
                var6 = 3;
            }
            else {
                var6 = 4;
            }
            this.zLevel += 100.0f;
            this.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0, 176 + var6 * 8, 10, 8);
        }
        else {
            this.zLevel += 100.0f;
        }
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        int ping = networkPlayerInfoIn.getResponseTime();
        if (ping >= 1000) {
            ping = 999;
        }
        if (ping < 0) {
            ping = 0;
        }
        final boolean useColors = LabyMod.getSettings().tabPing_colored;
        String c = useColors ? "a" : "f";
        if (useColors) {
            if (ping > 150) {
                c = "2";
            }
            if (ping > 300) {
                c = "c";
            }
            if (ping > 600) {
                c = "4";
            }
        }
        if (LabyMod.getSettings().tabPing) {
            draw.drawCenteredString(String.valueOf(ModColor.cl(c)) + ((ping == 0) ? "?" : Integer.valueOf(ping)), (p_175245_2_ + p_175245_1_) * 2 - 12, p_175245_3_ * 2 + 5);
        }
        GL11.glPopMatrix();
        this.zLevel -= 100.0f;
    }
    
    private void drawScoreboardValues(final ScoreObjective p_175247_1_, final int p_175247_2_, final String p_175247_3_, final int p_175247_4_, final int p_175247_5_, final NetworkPlayerInfo p_175247_6_) {
        final int i = p_175247_1_.getScoreboard().getValueFromObjective(p_175247_3_, p_175247_1_).getScorePoints();
        if (p_175247_1_.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
            this.mc.getTextureManager().bindTexture(ModPlayerTabOverlay.icons);
            if (this.lastTimeOpened == p_175247_6_.func_178855_p()) {
                if (i < p_175247_6_.func_178835_l()) {
                    p_175247_6_.func_178846_a(Minecraft.getSystemTime());
                    p_175247_6_.func_178844_b(this.guiIngame.getUpdateCounter() + 20);
                }
                else if (i > p_175247_6_.func_178835_l()) {
                    p_175247_6_.func_178846_a(Minecraft.getSystemTime());
                    p_175247_6_.func_178844_b(this.guiIngame.getUpdateCounter() + 10);
                }
            }
            if (Minecraft.getSystemTime() - p_175247_6_.func_178847_n() > 1000L || this.lastTimeOpened != p_175247_6_.func_178855_p()) {
                p_175247_6_.func_178836_b(i);
                p_175247_6_.func_178857_c(i);
                p_175247_6_.func_178846_a(Minecraft.getSystemTime());
            }
            p_175247_6_.func_178843_c(this.lastTimeOpened);
            p_175247_6_.func_178836_b(i);
            final int j = LabyModCore.getMath().ceiling_float_int(Math.max(i, p_175247_6_.func_178860_m()) / 2.0f);
            final int k = Math.max(LabyModCore.getMath().ceiling_float_int((float)(i / 2)), Math.max(LabyModCore.getMath().ceiling_float_int((float)(p_175247_6_.func_178860_m() / 2)), 10));
            final boolean flag = p_175247_6_.func_178858_o() > this.guiIngame.getUpdateCounter() && (p_175247_6_.func_178858_o() - this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L;
            if (j > 0) {
                final float f = Math.min((p_175247_5_ - p_175247_4_ - 4) / (float)k, 9.0f);
                if (f > 3.0f) {
                    for (int l = j; l < k; ++l) {
                        this.drawTexturedModalRect(p_175247_4_ + l * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                    }
                    for (int j2 = 0; j2 < j; ++j2) {
                        this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                        if (flag) {
                            if (j2 * 2 + 1 < p_175247_6_.func_178860_m()) {
                                this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, 70, 0, 9, 9);
                            }
                            if (j2 * 2 + 1 == p_175247_6_.func_178860_m()) {
                                this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, 79, 0, 9, 9);
                            }
                        }
                        if (j2 * 2 + 1 < i) {
                            this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, (j2 >= 10) ? 160 : 52, 0, 9, 9);
                        }
                        if (j2 * 2 + 1 == i) {
                            this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, (j2 >= 10) ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else {
                    final float f2 = LabyModCore.getMath().clamp_float(i / 20.0f, 0.0f, 1.0f);
                    final int i2 = (int)((1.0f - f2) * 255.0f) << 16 | (int)(f2 * 255.0f) << 8;
                    String s = new StringBuilder().append(i / 2.0f).toString();
                    if (p_175247_5_ - LabyModCore.getMinecraft().getFontRenderer().getStringWidth(String.valueOf(s) + "hp") >= p_175247_4_) {
                        s = String.valueOf(s) + "hp";
                    }
                    LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(s, (float)((p_175247_5_ + p_175247_4_) / 2 - LabyModCore.getMinecraft().getFontRenderer().getStringWidth(s) / 2), (float)p_175247_2_, i2);
                }
            }
        }
        else {
            final String s2 = new StringBuilder().append(EnumChatFormatting.YELLOW).append(i).toString();
            LabyModCore.getMinecraft().getFontRenderer().drawStringWithShadow(s2, (float)(p_175247_5_ - LabyModCore.getMinecraft().getFontRenderer().getStringWidth(s2)), (float)p_175247_2_, 16777215);
        }
    }
    
    @Override
    public void setFooter(final IChatComponent footerIn) {
        this.footer = footerIn;
        LabyMod.getInstance().getEventManager().callAllFooter(LabyModCore.getMinecraft().getChatComponent(footerIn));
    }
    
    @Override
    public void setHeader(final IChatComponent headerIn) {
        this.header = headerIn;
        LabyMod.getInstance().getEventManager().callAllHeader(LabyModCore.getMinecraft().getChatComponent(headerIn));
    }
    
    public void func_181030_a() {
        this.header = null;
        this.footer = null;
    }
    
    static class PlayerComparator implements Comparator<NetworkPlayerInfo>
    {
        private PlayerComparator() {
        }
        
        @Override
        public int compare(final NetworkPlayerInfo p_compare_1_, final NetworkPlayerInfo p_compare_2_) {
            final ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            final ScorePlayerTeam scoreplayerteam2 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare((scoreplayerteam != null) ? scoreplayerteam.getRegisteredName() : "", (scoreplayerteam2 != null) ? scoreplayerteam2.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
        }
    }
}
