// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import wdl.WDL;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiTurningCameraBase extends GuiScreen
{
    private float yaw;
    private float yawNextTick;
    private int oldCameraMode;
    private boolean oldHideHud;
    private boolean oldShowDebug;
    private EntityPlayer.EnumChatVisibility oldChatVisibility;
    private EntityPlayerSP cam;
    private Entity oldRenderViewEntity;
    private boolean initializedCamera;
    private static final float ROTATION_SPEED = 1.0f;
    private static final float ROTATION_VARIANCE = 0.7f;
    
    public GuiTurningCameraBase() {
        this.initializedCamera = false;
    }
    
    @Override
    public void initGui() {
        if (!this.initializedCamera) {
            (this.cam = new EntityPlayerSP(WDL.minecraft, WDL.worldClient, WDL.thePlayer.sendQueue, WDL.thePlayer.getStatFileWriter())).setLocationAndAngles(WDL.thePlayer.posX, WDL.thePlayer.posY - WDL.thePlayer.getYOffset(), WDL.thePlayer.posZ, WDL.thePlayer.rotationYaw, 0.0f);
            final float rotationYaw = WDL.thePlayer.rotationYaw;
            this.yawNextTick = rotationYaw;
            this.yaw = rotationYaw;
            this.oldShowDebug = WDL.minecraft.gameSettings.showDebugProfilerChart;
            this.oldChatVisibility = WDL.minecraft.gameSettings.chatVisibility;
            WDL.minecraft.gameSettings.showDebugProfilerChart = false;
            WDL.minecraft.gameSettings.chatVisibility = EntityPlayer.EnumChatVisibility.HIDDEN;
            this.oldRenderViewEntity = WDL.minecraft.getRenderViewEntity();
            this.initializedCamera = true;
        }
        WDL.minecraft.setRenderViewEntity(this.cam);
    }
    
    @Override
    public void updateScreen() {
        this.yaw = this.yawNextTick;
        this.yawNextTick = this.yaw + 1.0f * (float)(1.0 + 0.699999988079071 * Math.cos((this.yaw + 45.0f) / 45.0 * 3.141592653589793));
    }
    
    private double truncateDistanceIfBlockInWay(final double camX, final double camZ, double currentDistance) {
        final Vec3 playerPos = WDL.thePlayer.getPositionVector().addVector(0.0, WDL.thePlayer.getEyeHeight(), 0.0);
        final Vec3 offsetPos = new Vec3(WDL.thePlayer.posX - currentDistance * camX, WDL.thePlayer.posY + WDL.thePlayer.getEyeHeight(), WDL.thePlayer.posZ + camZ);
        for (int i = 0; i < 9; ++i) {
            float offsetX = ((i & 0x1) != 0x0) ? -0.1f : 0.1f;
            float offsetY = ((i & 0x2) != 0x0) ? -0.1f : 0.1f;
            float offsetZ = ((i & 0x4) != 0x0) ? -0.1f : 0.1f;
            if (i == 8) {
                offsetX = 0.0f;
                offsetY = 0.0f;
                offsetZ = 0.0f;
            }
            final Vec3 from = playerPos.addVector(offsetX, offsetY, offsetZ);
            final Vec3 to = offsetPos.addVector(offsetX, offsetY, offsetZ);
            final MovingObjectPosition pos = this.mc.theWorld.rayTraceBlocks(from, to);
            if (pos != null) {
                final double distance = pos.hitVec.distanceTo(playerPos);
                if (distance < currentDistance && distance > 0.0) {
                    currentDistance = distance;
                }
            }
        }
        return currentDistance - 0.25;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.cam != null) {
            final float yaw = this.yaw + (this.yawNextTick - this.yaw) * partialTicks;
            final EntityPlayerSP cam = this.cam;
            final EntityPlayerSP cam2 = this.cam;
            final float n = 0.0f;
            cam2.rotationPitch = n;
            cam.prevRotationPitch = n;
            final EntityPlayerSP cam3 = this.cam;
            final EntityPlayerSP cam4 = this.cam;
            final float n2 = yaw;
            cam4.rotationYaw = n2;
            cam3.prevRotationYaw = n2;
            final double x = Math.cos(yaw / 180.0 * 3.141592653589793);
            final double z = Math.sin((yaw - 90.0f) / 180.0 * 3.141592653589793);
            final double distance = this.truncateDistanceIfBlockInWay(x, z, 0.5);
            final EntityPlayerSP cam5 = this.cam;
            final EntityPlayerSP cam6 = this.cam;
            final EntityPlayerSP cam7 = this.cam;
            final double posY = WDL.thePlayer.posY;
            cam7.posY = posY;
            cam6.prevPosY = posY;
            cam5.lastTickPosY = posY;
            final EntityPlayerSP cam8 = this.cam;
            final EntityPlayerSP cam9 = this.cam;
            final EntityPlayerSP cam10 = this.cam;
            final double lastTickPosX = WDL.thePlayer.posX - distance * x;
            cam10.posX = lastTickPosX;
            cam9.prevPosX = lastTickPosX;
            cam8.lastTickPosX = lastTickPosX;
            final EntityPlayerSP cam11 = this.cam;
            final EntityPlayerSP cam12 = this.cam;
            final EntityPlayerSP cam13 = this.cam;
            final double lastTickPosZ = WDL.thePlayer.posZ + distance * z;
            cam13.posZ = lastTickPosZ;
            cam12.prevPosZ = lastTickPosZ;
            cam11.lastTickPosZ = lastTickPosZ;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        WDL.minecraft.gameSettings.showDebugProfilerChart = this.oldShowDebug;
        WDL.minecraft.gameSettings.chatVisibility = this.oldChatVisibility;
        WDL.minecraft.setRenderViewEntity(this.oldRenderViewEntity);
    }
}
