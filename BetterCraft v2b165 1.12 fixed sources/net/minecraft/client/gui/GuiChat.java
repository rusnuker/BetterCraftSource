// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import javax.annotation.Nullable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import me.amkgre.bettercraft.client.utils.ClipboardUtils;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import net.labymod.gui.GuiLabyModChat;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.gui.GuiNameHistory;
import org.lwjgl.input.Keyboard;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.TabCompleter;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ITabCompleter;

public class GuiChat extends GuiScreen implements ITabCompleter
{
    private static final Logger LOGGER;
    private String historyBuffer;
    public static boolean isInChat;
    private int sentHistoryCursor;
    private TabCompleter tabCompleter;
    protected static GuiTextField inputField;
    private String defaultInputFieldText;
    
    static {
        LOGGER = LogManager.getLogger();
        GuiChat.isInChat = false;
    }
    
    public GuiChat() {
        this.historyBuffer = "";
        this.sentHistoryCursor = -1;
        this.defaultInputFieldText = "";
    }
    
    public GuiChat(final String defaultText) {
        this.historyBuffer = "";
        this.sentHistoryCursor = -1;
        this.defaultInputFieldText = "";
        this.defaultInputFieldText = defaultText;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(GuiChat.isInChat = true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        (GuiChat.inputField = new GuiTextField(0, this.fontRendererObj, 4, GuiChat.height - 15, GuiChat.width - 4, 12)).setMaxStringLength(150);
        this.buttonList.add(new GuiButton(9, GuiChat.width - 105, 5, 100, 20, "Name History"));
        this.buttonList.add(new GuiButton(10, GuiChat.width - 210, 5, 100, 20, "LabyMod"));
        GuiChat.inputField.setEnableBackgroundDrawing(false);
        GuiChat.inputField.setFocused(true);
        GuiChat.inputField.setText(this.defaultInputFieldText);
        GuiChat.inputField.setCanLoseFocus(false);
        this.tabCompleter = new ChatTabCompleter(GuiChat.inputField);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(GuiChat.isInChat = false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 9: {
                this.mc.displayGuiScreen(new GuiNameHistory());
                break;
            }
            case 10: {
                this.mc.displayGuiScreen(new GuiLabyModChat(Client.getInstance().getLabyMod().getLabyConnect().getFriends()));
                break;
            }
        }
        super.actionPerformed(button);
    }
    
    @Override
    public void updateScreen() {
        GuiChat.inputField.updateCursorCounter();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.tabCompleter.resetRequested();
        if (keyCode == 15) {
            this.tabCompleter.complete();
        }
        else {
            this.tabCompleter.resetDidComplete();
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
        else if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 200) {
                this.getSentHistory(-1);
            }
            else if (keyCode == 208) {
                this.getSentHistory(1);
            }
            else if (keyCode == 201) {
                this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
            }
            else if (keyCode == 209) {
                this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
            }
            else {
                GuiChat.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        }
        else {
            final String s = GuiChat.inputField.getText().trim();
            if (!s.isEmpty()) {
                this.sendChatMessage(s);
            }
            this.mc.displayGuiScreen(null);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            if (i > 1) {
                i = 1;
            }
            if (i < -1) {
                i = -1;
            }
            if (!GuiScreen.isShiftKeyDown()) {
                i *= 7;
            }
            this.mc.ingameGUI.getChatGUI().scroll(i);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        final ITextComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (mouseButton == 0) {
            if (this.handleComponentClick(ichatcomponent)) {
                return;
            }
        }
        else if (mouseButton == 1 && ichatcomponent != null) {
            ClipboardUtils.setClipboard(ichatcomponent.getFormattedText());
        }
        GuiChat.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void setText(final String newChatText, final boolean shouldOverwrite) {
        if (shouldOverwrite) {
            GuiChat.inputField.setText(newChatText);
        }
        else {
            GuiChat.inputField.writeText(newChatText);
        }
    }
    
    public void getSentHistory(final int msgPos) {
        int i = this.sentHistoryCursor + msgPos;
        final int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        i = MathHelper.clamp(i, 0, j);
        if (i != this.sentHistoryCursor) {
            if (i == j) {
                this.sentHistoryCursor = j;
                GuiChat.inputField.setText(this.historyBuffer);
            }
            else {
                if (this.sentHistoryCursor == j) {
                    this.historyBuffer = GuiChat.inputField.getText();
                }
                GuiChat.inputField.setText(this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
                this.sentHistoryCursor = i;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Gui.drawRect(2, GuiChat.height - 17, GuiChat.width - 2, GuiChat.height - 5, Integer.MIN_VALUE);
        GuiChat.inputField.drawTextBox();
        ColorUtils.drawChromaString(String.valueOf(GuiChat.inputField.getText().length()) + "/150", 5, GuiChat.height - 27, true);
        final ITextComponent itextcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null) {
            this.handleComponentHover(itextcomponent, mouseX, mouseY);
        }
        if (GuiChat.inputField.text.length() == 0) {
            this.mc.fontRendererObj.drawStringWithShadow("   §7Type the command §d+help §7for all Commands and then hold a Item with NBT Data press §dN §7for NBTEdit and than you config the UI press the §dRShift §7Button", (float)GuiChat.inputField.xPosition, (float)GuiChat.inputField.yPosition, -1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void setCompletions(final String... newCompletions) {
        this.tabCompleter.setCompletions(newCompletions);
    }
    
    public static class ChatTabCompleter extends TabCompleter
    {
        private final Minecraft clientInstance;
        
        public ChatTabCompleter(final GuiTextField p_i46749_1_) {
            super(p_i46749_1_, false);
            this.clientInstance = Minecraft.getMinecraft();
        }
        
        @Override
        public void complete() {
            super.complete();
            if (this.completions.size() > 1) {
                final StringBuilder stringbuilder = new StringBuilder();
                for (final String s : this.completions) {
                    if (stringbuilder.length() > 0) {
                        stringbuilder.append(", ");
                    }
                    stringbuilder.append(s);
                }
                this.clientInstance.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(stringbuilder.toString()), 1);
            }
        }
        
        @Nullable
        @Override
        public BlockPos getTargetBlockPos() {
            BlockPos blockpos = null;
            if (this.clientInstance.objectMouseOver != null && this.clientInstance.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                blockpos = this.clientInstance.objectMouseOver.getBlockPos();
            }
            return blockpos;
        }
    }
}
