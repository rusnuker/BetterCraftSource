// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import me.amkgre.bettercraft.client.utils.ItemStackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiItemCreator extends GuiScreen
{
    private GuiScreen previous;
    private GuiTextField nameOrId;
    private GuiTextField name;
    
    public GuiItemCreator(final GuiScreen previous) {
        this.previous = previous;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiItemCreator.width / 2 - 100, GuiItemCreator.height / 5 + 100, "Give Item"));
        (this.nameOrId = new GuiTextField(0, this.fontRendererObj, GuiItemCreator.width / 2 - 100, GuiItemCreator.height / 5 + 30, 200, 20)).setText("command_block 1 0");
        this.nameOrId.setMaxStringLength(Integer.MAX_VALUE);
    }
    
    @Override
    public void updateScreen() {
        this.nameOrId.updateCursorCounter();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, ItemStackUtils.stringtostack(this.nameOrId.getText())));
            this.mc.displayGuiScreen(this.previous);
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.previous);
        }
    }
    
    public static String withColors(final String identifier, final String input) {
        String output = input;
        int index = output.indexOf(identifier);
        while (output.indexOf(identifier) != -1) {
            output = output.replace(identifier, "§");
            index = output.indexOf(identifier);
        }
        return output;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.nameOrId.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.nameOrId.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.nameOrId.drawTextBox();
        Gui.drawCenteredString(this.fontRendererObj, "Name/ID", GuiItemCreator.width / 2, this.nameOrId.yPosition - 15, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
