// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.labymod.core.WorldRendererAdapter;
import net.labymod.core.LabyModCore;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.labymod.user.group.LabyGroup;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.labymod.user.User;
import net.labymod.utils.ModColor;
import net.labymod.user.group.EnumGroupDisplayType;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.labymod.utils.manager.TagManager;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.main.LabyMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.mojang.RenderPlayerHook;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.labymod.user.cosmetic.layers.LayerCustomCape;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.labymod.core_implementation.mc18.layer.LayerArrowCustom;
import net.labymod.core_implementation.mc18.layer.LayerHeldItemCustom;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.labymod.core_implementation.mc18.layer.LayerBipedArmorCustom;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.labymod.core.RenderPlayerAdapter;

public class RenderPlayerImplementation implements RenderPlayerAdapter
{
    @Override
    public String[] getSkinMapNames() {
        return new String[] { "skinMap", "l", "field_178636_l" };
    }
    
    @Override
    public LayerRenderer[] getLayerRenderers(final RenderPlayer renderPlayer) {
        return new LayerRenderer[] { new LayerBipedArmorCustom(renderPlayer), new LayerHeldItemCustom(renderPlayer), new LayerArrowCustom(renderPlayer), new LayerDeadmau5Head(renderPlayer), new LayerCustomCape(renderPlayer), new LayerCustomHead(renderPlayer.getMainModel().bipedHead) };
    }
    
    @Override
    public void renderName(final RenderPlayerHook.RenderPlayerCustom renderPlayer, final AbstractClientPlayer entity, final double x, double y, final double z) {
        final boolean canRender = Minecraft.isGuiEnabled() && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) && entity.riddenByEntity == null;
        if (renderPlayer.canRenderTheName(entity) || (entity == renderPlayer.getRenderManager().livingPlayer && LabyMod.getSettings().showMyName && canRender)) {
            final double distance = entity.getDistanceSqToEntity(renderPlayer.getRenderManager().livingPlayer);
            final float f = entity.isSneaking() ? 32.0f : 64.0f;
            if (distance < f * f) {
                final User user = (entity instanceof EntityPlayer) ? LabyMod.getInstance().getUserManager().getUser(entity.getUniqueID()) : null;
                final float maxNameTagHeight = (user == null || !LabyMod.getSettings().cosmetics) ? 0.0f : user.getMaxNameTagHeight();
                String username = entity.getDisplayName().getFormattedText();
                GlStateManager.alphaFunc(516, 0.1f);
                final String tagName = TagManager.getTaggedMessage(username);
                if (tagName != null) {
                    username = tagName;
                }
                final float fixedPlayerViewX = renderPlayer.getRenderManager().playerViewX * ((Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) ? -1 : 1);
                y += maxNameTagHeight;
                final FontRenderer fontrenderer = renderPlayer.getFontRendererFromRenderManager();
                if (entity.isSneaking()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f), (float)z);
                    GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(-renderPlayer.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(fixedPlayerViewX, 1.0f, 0.0f, 0.0f);
                    GlStateManager.scale(-0.02666667f, -0.02666667f, 0.02666667f);
                    GlStateManager.translate(0.0f, 9.374999f, 0.0f);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.disableTexture2D();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    final int i = fontrenderer.getStringWidth(username) / 2;
                    final Tessellator tessellator = Tessellator.getInstance();
                    final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos(-i - 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldrenderer.pos(-i - 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldrenderer.pos(i + 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldrenderer.pos(i + 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                    GlStateManager.depthMask(true);
                    fontrenderer.drawString(username, -fontrenderer.getStringWidth(username) / 2, 0, 553648127);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                }
                else {
                    final LabyGroup labyGroup = user.getGroup();
                    if (user.getSubTitle() != null) {
                        GlStateManager.pushMatrix();
                        final double size = user.getSubTitleSize();
                        GlStateManager.translate(0.0, -0.2 + size / 8.0, 0.0);
                        this.renderLivingLabelCustom(renderPlayer, entity, user.getSubTitle(), x, y, z, 64, (float)size);
                        y += size / 6.0;
                        GlStateManager.popMatrix();
                    }
                    if (labyGroup != null && labyGroup.getDisplayType() == EnumGroupDisplayType.BESIDE_NAME) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f), (float)z);
                        GlStateManager.rotate(-renderPlayer.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.rotate(fixedPlayerViewX, 1.0f, 0.0f, 0.0f);
                        GlStateManager.scale(-0.02666667f, -0.02666667f, 0.02666667f);
                        GlStateManager.disableLighting();
                        GlStateManager.disableBlend();
                        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                        final double pos = -fontrenderer.getStringWidth(username) / 2 - 2 - 8;
                        labyGroup.renderBadge(pos, -0.5, 8.0, 8.0, false);
                        GlStateManager.enableLighting();
                        GlStateManager.disableBlend();
                        GlStateManager.resetColor();
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                        GlStateManager.popMatrix();
                    }
                    if (distance < 100.0) {
                        final Scoreboard scoreboard = entity.getWorldScoreboard();
                        final ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
                        if (scoreobjective != null) {
                            final Score score = scoreboard.getValueFromObjective(entity.getName(), scoreobjective);
                            this.renderLivingLabelCustom(renderPlayer, entity, String.valueOf(score.getScorePoints()) + " " + scoreobjective.getDisplayName(), x, y, z, 64);
                            y += LabyMod.getInstance().getDrawUtils().getFontRenderer().FONT_HEIGHT * 1.15f * 0.02666667f;
                        }
                    }
                    this.renderLivingLabelCustom(renderPlayer, entity, username, x, y - (entity.isChild() ? (entity.height / 2.0f) : 0.0), z, 64);
                    if (tagName != null) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f), (float)z);
                        GlStateManager.rotate(-renderPlayer.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.rotate(fixedPlayerViewX, 1.0f, 0.0f, 0.0f);
                        GlStateManager.scale(-0.01666667f, -0.01666667f, 0.01666667f);
                        GlStateManager.translate(0.0f, entity.isSneaking() ? 17.0f : 2.0f, 0.0f);
                        GlStateManager.disableLighting();
                        GlStateManager.enableBlend();
                        fontrenderer.drawString("\u270e", 5 + (int)(fontrenderer.getStringWidth(username) * 0.8), 0, ModColor.toRGB(255, 255, 0, 255));
                        GlStateManager.disableBlend();
                        GlStateManager.enableLighting();
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                        GlStateManager.popMatrix();
                    }
                    if (labyGroup != null && labyGroup.getDisplayType() == EnumGroupDisplayType.ABOVE_HEAD) {
                        GlStateManager.pushMatrix();
                        final double size = 0.5;
                        GlStateManager.scale(0.5, 0.5, 0.5);
                        GlStateManager.translate(0.0, 2.0, 0.0);
                        this.renderLivingLabelCustom(renderPlayer, entity, labyGroup.getDisplayTag(), x / 0.5, (y - (entity.isChild() ? (entity.height / 2.0f) : 0.0) + 0.3) / 0.5, z / 0.5, 10);
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }
    
    protected void renderLivingLabelCustom(final RenderPlayerHook.RenderPlayerCustom renderPlayer, final Entity entityIn, final String str, final double x, final double y, final double z, final int maxDistance) {
        this.renderLivingLabelCustom(renderPlayer, entityIn, str, x, y, z, maxDistance, 1.6f);
    }
    
    protected void renderLivingLabelCustom(final RenderPlayerHook.RenderPlayerCustom renderPlayer, final Entity entityIn, final String str, final double x, final double y, final double z, final int maxDistance, final float scale) {
        final double d0 = entityIn.getDistanceSqToEntity(renderPlayer.getRenderManager().livingPlayer);
        if (d0 <= maxDistance * maxDistance) {
            final float fixedPlayerViewX = renderPlayer.getRenderManager().playerViewX * ((Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) ? -1 : 1);
            final FontRenderer fontrenderer = renderPlayer.getFontRendererFromRenderManager();
            final float f1 = 0.016666668f * scale;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0f, (float)y + entityIn.height + 0.5f, (float)z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-renderPlayer.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(fixedPlayerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
            int i = 0;
            if (str.equals("deadmau5")) {
                i = -10;
            }
            final int j = fontrenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(-j - 1, -1 + i, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(-j - 1, 8 + i, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(j + 1, 8 + i, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(j + 1, -1 + i, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public RenderPlayerHook.RenderPlayerCustom getRenderPlayer(final RenderManager renderManager, final boolean slim) {
        return new RenderPlayerHook.RenderPlayerCustom(renderManager, slim) {
            @Override
            public boolean canRenderTheName(final AbstractClientPlayer entity) {
                return super.canRenderName(entity);
            }
            
            @Override
            public void renderLabel(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String string, final float height, final double distance) {
                super.renderOffsetLivingLabel(entityIn, x, y, z, string, height, distance);
            }
            
            @Override
            public void renderName(final AbstractClientPlayer entity, final double x, final double y, final double z) {
                LabyModCore.getRenderPlayerImplementation().renderName(this, entity, x, y, z);
            }
        };
    }
}
