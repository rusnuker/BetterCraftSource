// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class GuiLockIconButton extends GuiButton
{
    private boolean locked;
    
    public GuiLockIconButton(final int p_i45538_1_, final int p_i45538_2_, final int p_i45538_3_) {
        super(p_i45538_1_, p_i45538_2_, p_i45538_3_, 20, 20, "");
    }
    
    public boolean isLocked() {
        return this.locked;
    }
    
    public void setLocked(final boolean lockedIn) {
        this.locked = lockedIn;
    }
    
    @Override
    public void drawButton(final Minecraft p_191745_1_, final int p_191745_2_, final int p_191745_3_, final float p_191745_4_) {
        if (this.visible) {
            p_191745_1_.getTextureManager().bindTexture(GuiButton.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final boolean flag = p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height;
            Icon guilockiconbutton$icon;
            if (this.locked) {
                if (!this.enabled) {
                    guilockiconbutton$icon = Icon.LOCKED_DISABLED;
                }
                else if (flag) {
                    guilockiconbutton$icon = Icon.LOCKED_HOVER;
                }
                else {
                    guilockiconbutton$icon = Icon.LOCKED;
                }
            }
            else if (!this.enabled) {
                guilockiconbutton$icon = Icon.UNLOCKED_DISABLED;
            }
            else if (flag) {
                guilockiconbutton$icon = Icon.UNLOCKED_HOVER;
            }
            else {
                guilockiconbutton$icon = Icon.UNLOCKED;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, guilockiconbutton$icon.getX(), guilockiconbutton$icon.getY(), this.width, this.height);
        }
    }
    
    enum Icon
    {
        LOCKED("LOCKED", 0, 0, 146), 
        LOCKED_HOVER("LOCKED_HOVER", 1, 0, 166), 
        LOCKED_DISABLED("LOCKED_DISABLED", 2, 0, 186), 
        UNLOCKED("UNLOCKED", 3, 20, 146), 
        UNLOCKED_HOVER("UNLOCKED_HOVER", 4, 20, 166), 
        UNLOCKED_DISABLED("UNLOCKED_DISABLED", 5, 20, 186);
        
        private final int x;
        private final int y;
        
        private Icon(final String s, final int n, final int p_i45537_3_, final int p_i45537_4_) {
            this.x = p_i45537_3_;
            this.y = p_i45537_4_;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
    }
}
