// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import me.nzxtercode.bettercraft.client.misc.altmanager.impl.Account;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import me.nzxtercode.bettercraft.client.misc.altmanager.impl.AccountManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public class GuiAccountList extends GuiSlot
{
    public int selected;
    private GuiAltManager parent;
    
    public GuiAccountList(final GuiAltManager parent) {
        super(Minecraft.getMinecraft(), GuiAltManager.width, GuiAltManager.height, 36, GuiAltManager.height - 56, 40);
        this.selected = -1;
        this.parent = parent;
    }
    
    public int getSize() {
        return AccountManager.getInstance().getAccounts().size();
    }
    
    public void elementClicked(final int i, final boolean b, final int i1, final int i2) {
        this.selected = i;
        if (b) {
            this.parent.login(this.getAccount(i));
        }
    }
    
    @Override
    protected boolean isSelected(final int i) {
        return i == this.selected;
    }
    
    @Override
    protected void drawBackground() {
    }
    
    @Override
    protected void drawSlot(final int i, final int i1, final int i2, final int i3, final int i4, final int i5) {
        final Account account = this.getAccount(i);
        final Minecraft minecraft = Minecraft.getMinecraft();
        final ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        final FontRenderer fontRenderer = minecraft.fontRendererObj;
        final int x = i1 + 2;
        final int y = i2;
        if (y >= scaledResolution.getScaledHeight() || y < 0) {
            return;
        }
        GL11.glTranslated(x, y, 0.0);
        this.drawFace(account.getName(), 0, 6, 24, 24);
        fontRenderer.drawStringWithShadow(account.getName(), 30.0f, 6.0f, -1);
        fontRenderer.drawStringWithShadow(account.getEmail(), 30.0f, (float)(6 + fontRenderer.FONT_HEIGHT + 2), -1);
        GL11.glTranslated(-x, -y, 0.0);
    }
    
    public Account getAccount(final int i) {
        return AccountManager.getInstance().getAccounts().get(i);
    }
    
    private void drawFace(final String name, final int x, final int y, final int w, final int h) {
        try {
            AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Minecraft.getMinecraft().getResourceManager());
            Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
            GL11.glEnable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 24.0f, 24.0f, w, h, 192.0f, 192.0f);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 120.0f, 24.0f, w, h, 192.0f, 192.0f);
            GL11.glDisable(3042);
        }
        catch (final Exception ex) {}
    }
    
    public void removeSelected() {
        if (this.selected == -1) {
            return;
        }
        AccountManager.getInstance().getAccounts().remove(this.getAccount(this.selected));
        AccountManager.getInstance().save();
    }
    
    public Account getSelectedAccount() {
        return this.getAccount(this.selected);
    }
}
