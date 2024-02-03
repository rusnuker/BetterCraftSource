// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main.listeners;

import net.lenni0451.eventapi.events.EventTarget;
import net.labymod.utils.DrawUtils;
import net.labymod.support.MemoryUpgradeGui;
import net.minecraft.client.Minecraft;
import java.util.Objects;
import net.labymod.main.ModSettings;
import net.labymod.api.permissions.Permissions;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.main.LabyMod;
import me.nzxtercode.bettercraft.client.events.types.TypePrePost;
import me.nzxtercode.bettercraft.client.events.RenderEvents;

public class RenderGameOverlayListener
{
    @EventTarget
    public void handleRenderEvent(final RenderEvents.ToolTip event) {
        if (event.getType() != TypePrePost.PRE) {
            return;
        }
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        LabyMod.getInstance().getServerManager().draw();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        LabyMod.getInstance().getGuiCustomAchievement().updateAchievementWindow();
        Permissions.getPermissionNotifyRenderer().render(draw.getWidth());
        LabyMod.getInstance().getEmoteRegistry().getEmoteSelectorGui().render();
        LabyMod.getInstance().getStickerRegistry().getStickerSelectorGui().render();
        LabyMod.getInstance().getUserManager().getUserActionGui().render();
        if (Objects.requireNonNull(LabyMod.getSettings()).outOfMemoryWarning && Minecraft.getMinecraft().currentScreen == null) {
            MemoryUpgradeGui.renderTickOutOfMemoryDetector();
        }
    }
}
