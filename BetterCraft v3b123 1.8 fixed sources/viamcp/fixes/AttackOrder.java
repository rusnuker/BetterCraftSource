// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.fixes;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import viamcp.vialoadingbase.ViaLoadingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.Minecraft;

public class AttackOrder
{
    private static final Minecraft mc;
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public static void sendConditionalSwing(final MovingObjectPosition mop) {
        if (mop != null && mop.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
            AttackOrder.mc.thePlayer.swingItem();
        }
    }
    
    public static void sendFixedAttack(final EntityPlayer entityIn, final Entity target) {
        if (ViaLoadingBase.getInstance().getTargetVersion().isOlderThanOrEqualTo(ProtocolVersion.v1_8)) {
            AttackOrder.mc.thePlayer.swingItem();
            AttackOrder.mc.playerController.attackEntity(entityIn, target);
        }
        else {
            AttackOrder.mc.playerController.attackEntity(entityIn, target);
            AttackOrder.mc.thePlayer.swingItem();
        }
    }
}
