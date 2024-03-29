// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.mods.impl;

import java.util.Iterator;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.INpc;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import me.nzxtercode.bettercraft.client.hud.ScreenPosition;
import me.nzxtercode.bettercraft.client.mods.ModRender;

public class ModRadar extends ModRender
{
    @Override
    public int getWidth() {
        return 80;
    }
    
    @Override
    public int getHeight() {
        return 80;
    }
    
    @Override
    public void render(final ScreenPosition pos) {
        GL11.glPushMatrix();
        final int x1 = pos.getAbsoluteX();
        final int x2 = pos.getAbsoluteX() + 80;
        final int y1 = pos.getAbsoluteY();
        final int y2 = pos.getAbsoluteY() + 80;
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(2929);
        GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.5f);
        GL11.glBegin(7);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y2);
        GL11.glEnd();
        GL11.glLineWidth(2.0f);
        GL11.glBegin(2);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y2);
        GL11.glEnd();
        GL11.glColor4f(0.1f, 1.0f, 0.1f, 1.0f);
        GL11.glLineWidth(1.0f);
        final float prevRotationYawHead = Minecraft.getMinecraft().thePlayer.prevRotationYawHead;
        final float rotationYawHead = Minecraft.getMinecraft().thePlayer.rotationYawHead;
        final float n = rotationYawHead - Minecraft.getMinecraft().thePlayer.prevRotationYawHead;
        final float rotation = -(prevRotationYawHead + n * Minecraft.getMinecraft().timer.renderPartialTicks);
        for (final Entity en : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (en.isInvisible()) {
                GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
            }
            else {
                if (!(en instanceof EntityLivingBase)) {
                    continue;
                }
                if (en instanceof EntityPlayer) {
                    if (en != Minecraft.getMinecraft().thePlayer) {
                        if (Minecraft.getMinecraft().thePlayer.isOnSameTeam((EntityLivingBase)en)) {
                            GL11.glColor4f(0.5f, 1.0f, 0.5f, 1.0f);
                        }
                        else {
                            GL11.glColor4f(1.0f, 1.0f, 0.4f, 1.0f);
                        }
                    }
                    else {
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    }
                }
                if (en instanceof IMob) {
                    GL11.glColor4f(1.0f, 0.1f, 0.5f, 1.0f);
                }
                if (en instanceof EntityAnimal) {
                    GL11.glColor4f(1.0f, 1.0f, 0.5f, 1.0f);
                }
                if (en instanceof INpc || en instanceof EntityIronGolem) {
                    GL11.glColor4f(1.0f, 0.5f, 1.0f, 1.0f);
                }
            }
            GL11.glTranslated((x1 + x2) / 2, y1 + y2 / 2 - (double)(y1 / 2), 0.0);
            GL11.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            final double posX2 = Minecraft.getMinecraft().thePlayer.posX;
            final double lastTickPosX = en.lastTickPosX;
            final double n2 = en.posX - en.lastTickPosX;
            final double posX3 = -(posX2 - (lastTickPosX + n2 * Minecraft.getMinecraft().timer.renderPartialTicks)) / 3.1;
            final double posZ2 = Minecraft.getMinecraft().thePlayer.posZ;
            final double lastTickPosZ = en.lastTickPosZ;
            final double n3 = en.posZ - en.lastTickPosZ;
            final double posZ3 = -(posZ2 - (lastTickPosZ + n3 * Minecraft.getMinecraft().timer.renderPartialTicks)) / 3.1;
            final double posY2 = Minecraft.getMinecraft().thePlayer.posY;
            final double lastTickPosY = en.lastTickPosY;
            final double n4 = en.posY - en.lastTickPosY;
            final double posY3 = -(posY2 - (lastTickPosY + n4 * Minecraft.getMinecraft().timer.renderPartialTicks)) / 500.0;
            GL11.glPushMatrix();
            GL11.glScaled(1.4 + posY3, 1.4 + posY3, 1.0);
            GL11.glBegin(6);
            for (int i = 0; i <= 10; ++i) {
                final double angle = 6.283185307179586 * i / 10.0;
                final double x3 = Math.cos(angle);
                final double y3 = Math.sin(angle);
                GL11.glVertex3d(x3 + posX3, y3 + posZ3, 365.0 + posY3);
            }
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef(-rotation, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(-180.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslated(-((x1 + x2) / 2), -((y1 + y2) / 2), 0.0);
        }
        GL11.glPopMatrix();
        GL11.glDisable(3042);
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(2929);
    }
}
