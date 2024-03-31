// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.nzxtercode.bettercraft.client.Config;
import java.net.URI;
import net.minecraft.client.gui.GuiMultiplayer;
import java.io.IOException;
import java.util.Objects;
import me.nzxtercode.bettercraft.client.misc.altmanager.thealtening.AlteningAlt;
import me.nzxtercode.bettercraft.client.misc.altmanager.thealtening.TheAltening;
import org.lwjgl.input.Keyboard;
import me.nzxtercode.bettercraft.client.misc.altmanager.impl.AccountManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import me.nzxtercode.bettercraft.client.misc.altmanager.impl.AccountLoginThread;
import net.minecraft.client.gui.GuiScreen;

public class GuiAlteningLogin extends GuiScreen
{
    private GuiScreen parent;
    public static AccountLoginThread thread;
    public static GuiTextField token;
    public static GuiTextField key;
    
    public GuiAlteningLogin(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(1, GuiAlteningLogin.width / 2 + 2, GuiAlteningLogin.height / 4 + 145, 98, 20, "Back"));
        this.buttonList.add(new GuiButton(-1, GuiAlteningLogin.width / 2 - 100, GuiAlteningLogin.height / 4 + 70, 98, 20, "Login"));
        this.buttonList.add(new GuiButton(4, GuiAlteningLogin.width / 2 - 100, GuiAlteningLogin.height / 4 + 95, "Generate (Web)"));
        this.buttonList.add(new GuiButton(0, GuiAlteningLogin.width / 2 - 100, GuiAlteningLogin.height / 4 + 120, "Generate and Login (Key)"));
        this.buttonList.add(new GuiButton(3, GuiAlteningLogin.width / 2 + 2, GuiAlteningLogin.height / 4 + 70, 98, 20, "Last Alt"));
        this.buttonList.add(new GuiButton(2, GuiAlteningLogin.width / 2 - 100, GuiAlteningLogin.height / 4 + 145, 98, 20, "Multiplayer"));
        (GuiAlteningLogin.token = new GuiTextField(GuiAlteningLogin.height / 4 + 24, this.mc.fontRendererObj, GuiAlteningLogin.width / 2 - 98, GuiAlteningLogin.height / 4 - 8, 196, 20)).setMaxStringLength(Integer.MAX_VALUE);
        (GuiAlteningLogin.key = new GuiTextField(GuiAlteningLogin.height / 4 + 22, this.mc.fontRendererObj, GuiAlteningLogin.width / 2 - 98, GuiAlteningLogin.height / 4 + 38, 196, 20)).setMaxStringLength(Integer.MAX_VALUE);
        if (AccountManager.getInstance().getAlteningKey() != null) {
            GuiAlteningLogin.key.setText(AccountManager.getInstance().getAlteningKey());
        }
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case -1: {
                if (GuiAlteningLogin.token.getText().isEmpty()) {
                    return;
                }
                AccountManager.getInstance().setAlteningKey(GuiAlteningLogin.key.getText());
                AccountManager.getInstance().setLastAlteningAlt(GuiAlteningLogin.token.getText());
                (GuiAlteningLogin.thread = new AccountLoginThread(GuiAlteningLogin.token.getText().replaceAll(" ", ""), "gaymer")).run();
                AccountManager.getInstance().save();
                break;
            }
            case 0: {
                if (GuiAlteningLogin.key.getText().isEmpty()) {
                    return;
                }
                try {
                    final TheAltening theAltening = new TheAltening(GuiAlteningLogin.key.getText());
                    final AlteningAlt account = theAltening.generateAccount(theAltening.getUser());
                    GuiAlteningLogin.token.setText(Objects.requireNonNull(account).getToken());
                    AccountManager.getInstance().save();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
                if (!GuiAlteningLogin.token.getText().isEmpty()) {
                    AccountManager.getInstance().setAlteningKey(GuiAlteningLogin.key.getText());
                    AccountManager.getInstance().setLastAlteningAlt(GuiAlteningLogin.token.getText());
                    (GuiAlteningLogin.thread = new AccountLoginThread(GuiAlteningLogin.token.getText().replaceAll(" ", ""), "gaymer")).run();
                    AccountManager.getInstance().save();
                    break;
                }
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 3: {
                if (GuiAlteningLogin.key.getText().isEmpty() || AccountManager.getInstance().getLastAlteningAlt() == null) {
                    return;
                }
                AccountManager.getInstance().setAlteningKey(GuiAlteningLogin.key.getText());
                (GuiAlteningLogin.thread = new AccountLoginThread(AccountManager.getInstance().getLastAlteningAlt().replaceAll(" ", ""), "gaymer")).run();
                AccountManager.getInstance().save();
                break;
            }
            case 4: {
                try {
                    final Class<?> oclass = Class.forName("java.awt.Desktop");
                    final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI("https://fastalts.net/"));
                }
                catch (final Exception ex) {}
                break;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char character, final int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        if (character == '\t' && GuiAlteningLogin.token.isFocused()) {
            GuiAlteningLogin.token.setFocused(GuiAlteningLogin.token.isFocused());
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        if (character == '\t' && GuiAlteningLogin.key.isFocused()) {
            GuiAlteningLogin.key.setFocused(GuiAlteningLogin.key.isFocused());
        }
        GuiAlteningLogin.token.textboxKeyTyped(character, key);
        GuiAlteningLogin.key.textboxKeyTyped(character, key);
    }
    
    @Override
    public void updateScreen() {
        GuiAlteningLogin.token.updateCursorCounter();
        GuiAlteningLogin.key.updateCursorCounter();
    }
    
    @Override
    protected void mouseClicked(final int x, final int y, final int button) {
        try {
            super.mouseClicked(x, y, button);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        GuiAlteningLogin.token.mouseClicked(x, y, button);
        GuiAlteningLogin.key.mouseClicked(x, y, button);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        Config.getInstance().editConfig("Alterning", json -> json.add("apikey", new JsonPrimitive(GuiAlteningLogin.key.getText())));
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float z) {
        this.drawDefaultBackground();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        GuiAlteningLogin.token.drawTextBox();
        GuiAlteningLogin.key.drawTextBox();
        Gui.drawCenteredString(this.mc.fontRendererObj, "The Altening Login", GuiAlteningLogin.width / 2, sr.getScaledHeight() / 4 - 50, 16777215);
        Gui.drawCenteredString(this.fontRendererObj, String.valueOf(EnumChatFormatting.RED.toString()) + Minecraft.session.getUsername(), GuiAlteningLogin.width / 2, sr.getScaledHeight() / 4 - 35, 16777215);
        Gui.drawCenteredString(this.fontRendererObj, "Token", GuiAlteningLogin.width / 2 - 85, GuiAlteningLogin.height / 4 - 20, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, "Apikey", GuiAlteningLogin.width / 2 - 85, GuiAlteningLogin.height / 4 + 25, 10526880);
        super.drawScreen(x, y, z);
    }
}
