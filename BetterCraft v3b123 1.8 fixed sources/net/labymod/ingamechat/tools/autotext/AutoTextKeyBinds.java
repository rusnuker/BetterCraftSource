// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tools.autotext;

import net.minecraft.client.multiplayer.ServerData;
import net.labymod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.List;

public class AutoTextKeyBinds
{
    private List<AutoText> autoTextKeyBinds;
    
    public AutoTextKeyBinds() {
        this.autoTextKeyBinds = new ArrayList<AutoText>();
    }
    
    public List<AutoText> getAutoTextKeyBinds() {
        return this.autoTextKeyBinds;
    }
    
    public static class AutoText
    {
        private String message;
        private boolean keyShift;
        private boolean keyCtrl;
        private boolean keyAlt;
        private int keyCode;
        private boolean sendNotInstantly;
        private boolean serverBound;
        private String serverAddress;
        private boolean available;
        
        public AutoText(final String message, final boolean keyShift, final boolean keyCtrl, final boolean keyAlt, final int keyCode, final boolean instantSend, final boolean serverBound, final String serverAddress) {
            this.available = true;
            this.message = message;
            this.keyShift = keyShift;
            this.keyCtrl = keyCtrl;
            this.keyAlt = keyAlt;
            this.keyCode = keyCode;
            this.sendNotInstantly = instantSend;
            this.serverBound = serverBound;
            this.serverAddress = ((serverAddress == null) ? "" : serverAddress);
        }
        
        public AutoText(final AutoText component) {
            this(component.getMessage(), component.isKeyShift(), component.isKeyCtrl(), component.isKeyAlt(), component.getKeyCode(), component.isSendNotInstantly(), component.isServerBound(), component.getServerAddress());
        }
        
        public boolean isPressed() {
            final boolean pressed = Keyboard.isKeyDown(this.keyCode) && (!this.keyCtrl || (this.keyCtrl && Keyboard.isKeyDown(29))) && (!this.keyAlt || (this.keyAlt && Keyboard.isKeyDown(56))) && (!this.keyShift || (this.keyShift && Keyboard.isKeyDown(42)));
            if (!this.serverBound) {
                return pressed;
            }
            if (!pressed) {
                return false;
            }
            final ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
            if (serverData == null) {
                return Minecraft.getMinecraft().isSingleplayer() && this.serverAddress.equals("singleplayer");
            }
            return this.serverAddress == null || this.serverAddress.isEmpty() || ModUtils.getProfileNameByIp(serverData.serverIP).equalsIgnoreCase(this.serverAddress);
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public boolean isKeyShift() {
            return this.keyShift;
        }
        
        public boolean isKeyCtrl() {
            return this.keyCtrl;
        }
        
        public boolean isKeyAlt() {
            return this.keyAlt;
        }
        
        public int getKeyCode() {
            return this.keyCode;
        }
        
        public boolean isSendNotInstantly() {
            return this.sendNotInstantly;
        }
        
        public boolean isServerBound() {
            return this.serverBound;
        }
        
        public String getServerAddress() {
            return this.serverAddress;
        }
        
        public boolean isAvailable() {
            return this.available;
        }
        
        public void setMessage(final String message) {
            this.message = message;
        }
        
        public void setKeyShift(final boolean keyShift) {
            this.keyShift = keyShift;
        }
        
        public void setKeyCtrl(final boolean keyCtrl) {
            this.keyCtrl = keyCtrl;
        }
        
        public void setKeyAlt(final boolean keyAlt) {
            this.keyAlt = keyAlt;
        }
        
        public void setKeyCode(final int keyCode) {
            this.keyCode = keyCode;
        }
        
        public void setSendNotInstantly(final boolean sendNotInstantly) {
            this.sendNotInstantly = sendNotInstantly;
        }
        
        public void setServerBound(final boolean serverBound) {
            this.serverBound = serverBound;
        }
        
        public void setServerAddress(final String serverAddress) {
            this.serverAddress = serverAddress;
        }
        
        public void setAvailable(final boolean available) {
            this.available = available;
        }
    }
}
