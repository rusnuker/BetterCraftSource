// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.renderer.types;

import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.minecraft.client.Minecraft;
import net.labymod.ingamechat.IngameChatManager;
import net.minecraft.client.settings.GameSettings;
import net.labymod.ingamechat.renderer.ChatRenderer;

public class ChatRendererMain extends ChatRenderer
{
    private GameSettings gameSettings;
    
    public ChatRendererMain(final IngameChatManager manager) {
        super(manager, true, false);
        this.gameSettings = Minecraft.getMinecraft().gameSettings;
    }
    
    @Override
    public String getLogPrefix() {
        return "CHAT";
    }
    
    @Override
    public float getChatWidth() {
        final float max = 320.0f;
        final float min = 40.0f;
        return (float)LabyModCore.getMath().floor_float(this.gameSettings.chatWidth * 280.0f + 40.0f);
    }
    
    @Override
    public int getChatHeight() {
        final float height = this.isChatOpen() ? this.gameSettings.chatHeightFocused : this.gameSettings.chatHeightUnfocused;
        final float max = 180.0f;
        final float min = 20.0f;
        return LabyModCore.getMath().floor_float(height * 160.0f + 20.0f);
    }
    
    @Override
    public float getChatScale() {
        return this.gameSettings.chatScale;
    }
    
    @Override
    public float getChatOpacity() {
        return Minecraft.getMinecraft().gameSettings.chatOpacity;
    }
    
    @Override
    public float getChatPositionX() {
        final double screenWidth = LabyMod.getInstance().getDrawUtils().getWidth();
        final float percent = this.getChatPercentX();
        final double total = screenWidth / 2.0 + this.getChatWidth();
        if (percent < 1.0f) {
            return 2.0f;
        }
        double pos = total / 100.0 * percent;
        if (pos > screenWidth - this.getChatWidth()) {
            pos = screenWidth - this.getChatWidth();
        }
        return (float)pos;
    }
    
    @Override
    public float getChatPositionY() {
        final float height = this.lastRenderedLinesCount * LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT * this.getChatScale();
        final double screenHeight = LabyMod.getInstance().getDrawUtils().getHeight() - 28;
        final float percent = this.getChatPercentY();
        if (percent > 99.0f) {
            return (float)screenHeight;
        }
        if (percent < 50.0f) {
            return (float)(height + 2.0f + screenHeight / 100.0 * percent);
        }
        return (float)(screenHeight / 100.0 * percent);
    }
    
    @Override
    public float getChatPercentX() {
        return LabyMod.getSettings().mainChatPercentX;
    }
    
    @Override
    public float getChatPercentY() {
        return LabyMod.getSettings().mainChatPercentY;
    }
    
    @Override
    public void updateChatSetting(final ChatSettingType type, final float value) {
        switch (type) {
            case WIDTH: {
                this.gameSettings.chatWidth = value;
                break;
            }
            case HEIGHT: {
                this.gameSettings.chatHeightFocused = value;
                this.gameSettings.chatHeightUnfocused = value / 2.0f;
                break;
            }
            case X: {
                LabyMod.getSettings().mainChatPercentX = value;
                break;
            }
            case Y: {
                LabyMod.getSettings().mainChatPercentY = value;
                break;
            }
        }
    }
    
    @Override
    public void save() {
        Minecraft.getMinecraft().gameSettings.saveOptions();
    }
}
