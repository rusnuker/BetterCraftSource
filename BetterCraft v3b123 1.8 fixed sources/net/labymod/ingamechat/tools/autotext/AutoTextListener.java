// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tools.autotext;

import net.lenni0451.eventapi.events.EventTarget;
import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiChat;
import net.labymod.core.LabyModCore;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.api.permissions.Permissions;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.events.ClientTickEvent;

public class AutoTextListener
{
    @EventTarget
    public void handleEvent(final ClientTickEvent event) {
        if (Minecraft.getMinecraft().currentScreen != null) {
            return;
        }
        if (!LabyMod.getInstance().getServerManager().isAllowed(Permissions.Permission.CHAT)) {
            return;
        }
        for (final AutoTextKeyBinds.AutoText keybind : LabyMod.getInstance().getChatToolManager().getAutoTextKeyBinds()) {
            if (keybind.isAvailable() && keybind.isPressed()) {
                keybind.setAvailable(false);
                if (!keybind.isSendNotInstantly()) {
                    boolean cancelled = false;
                    for (final MessageSendEvent messageSend : LabyMod.getInstance().getEventManager().getMessageSend()) {
                        if (messageSend.onSend(keybind.getMessage()) && !cancelled) {
                            cancelled = true;
                        }
                    }
                    if (!cancelled) {
                        LabyModCore.getMinecraft().getPlayer().sendChatMessage(keybind.getMessage());
                    }
                }
                else {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiChat(keybind.getMessage()));
                }
            }
            if (!keybind.isPressed() && !keybind.isAvailable()) {
                keybind.setAvailable(true);
            }
        }
    }
}
