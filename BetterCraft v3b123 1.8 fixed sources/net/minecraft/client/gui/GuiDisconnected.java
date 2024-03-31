// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import me.nzxtercode.bettercraft.client.gui.section.GuiUISettings;
import java.util.ArrayList;
import viamcp.gui.GuiProtocolSelector;
import me.nzxtercode.bettercraft.client.utils.ProxyUtils;
import me.nzxtercode.bettercraft.client.misc.altmanager.GuiAlteningLogin;
import java.util.Objects;
import me.nzxtercode.bettercraft.client.misc.altmanager.thealtening.AlteningAlt;
import me.nzxtercode.bettercraft.client.misc.altmanager.thealtening.TheAltening;
import net.minecraft.client.multiplayer.GuiConnecting;
import me.nzxtercode.bettercraft.client.misc.altmanager.impl.AccountLoginThread;
import java.util.Random;
import me.nzxtercode.bettercraft.client.misc.altmanager.impl.Account;
import me.nzxtercode.bettercraft.client.misc.altmanager.impl.AccountManager;
import me.nzxtercode.bettercraft.client.misc.altmanager.GuiAltManager;
import net.minecraft.client.Minecraft;
import java.util.function.Consumer;
import me.nzxtercode.bettercraft.client.utils.TextAnimationUtils;
import viamcp.vialoadingbase.ViaLoadingBase;
import java.util.Arrays;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import java.util.List;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.multiplayer.ServerData;

public class GuiDisconnected extends GuiScreen
{
    public static ServerData serverData;
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private int field_175353_i;
    private String[] animationText;
    public static boolean useTheAltening;
    private String status;
    private final GuiScreen parent;
    
    public GuiDisconnected(final GuiScreen screen, final String reasonLocalizationKey, final IChatComponent chatComp) {
        this.status = "What do you want to do?";
        this.parent = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), GuiDisconnected.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        if (this.field_175353_i > GuiDisconnected.height || GuiDisconnected.height / 2 + this.field_175353_i / 2 + 101 > GuiDisconnected.height) {
            this.multilineMessage = Arrays.asList("To large message");
            this.field_175353_i = this.fontRendererObj.FONT_HEIGHT;
        }
        this.buttonList.add(new GuiButton(0, GuiDisconnected.width / 2 - 100, GuiDisconnected.height / 2 + this.field_175353_i / 2 + 81, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(new GuiButton(1, GuiDisconnected.width / 2 + 103, GuiDisconnected.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, 90, 20, "Altmanager"));
        this.buttonList.add(new GuiButton(2, GuiDisconnected.width / 2 + 103, GuiDisconnected.height / 2 + this.field_175353_i / 2 + 33, 90, 20, "Set Banned"));
        this.buttonList.add(new GuiButton(4, GuiDisconnected.width / 2 + 103, GuiDisconnected.height / 2 + this.field_175353_i / 2 + 57, 90, 20, "Remove Alt"));
        this.buttonList.add(new GuiButton(5, GuiDisconnected.width / 2 - 100, GuiDisconnected.height / 2 + this.field_175353_i / 2 + 33, 98, 20, "Relog not banned"));
        this.buttonList.add(new GuiButton(6, GuiDisconnected.width / 2 + 2, GuiDisconnected.height / 2 + this.field_175353_i / 2 + 33, 98, 20, "Relog banned"));
        this.buttonList.add(new GuiButton(8, GuiDisconnected.width / 2 - 100, GuiDisconnected.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, "Relog"));
        this.buttonList.add(new GuiButton(7, GuiDisconnected.width / 2 + 2, GuiDisconnected.height / 2 + this.field_175353_i / 2 + 57, 98, 20, "Relog The Altening"));
        this.buttonList.add(new GuiButton(9, GuiDisconnected.width / 2 - 100, GuiDisconnected.height / 2 + this.field_175353_i / 2 + 57, 98, 20, "Relog with Proxy"));
        this.buttonList.add(new GuiButton(10, GuiDisconnected.width / 2 + 103, GuiDisconnected.height / 2 + this.field_175353_i / 2 + 81, 90, 20, ViaLoadingBase.getInstance().getTargetVersion().getName()));
        this.animationText = new String[this.multilineMessage.size()];
        this.multilineMessage.forEach(message -> TextAnimationUtils.writeTextAnimation(message, 20L, value -> this.animationText[this.multilineMessage.indexOf(s)] = value, null));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parent);
        }
        if (button.id == 0 || button.id == 1) {
            Minecraft.getMinecraft().setServerData(null);
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(GuiAltManager.getInstance());
        }
        if (button.id == 2) {
            if (GuiAltManager.getInstance().currentAccount == null) {
                this.status = "You are not using any alt...";
                return;
            }
            GuiAltManager.getInstance().currentAccount.setBanned(true);
            this.status = "The account has been set to &obanned.";
        }
        if (button.id == 4) {
            if (GuiAltManager.getInstance().currentAccount == null) {
                this.status = "You are not using any alt...";
                return;
            }
            if (GuiAltManager.getInstance().loginThread != null) {
                GuiAltManager.getInstance().loginThread = null;
            }
            AccountManager.getInstance().getAccounts().remove(GuiAltManager.getInstance().currentAccount);
            AccountManager.getInstance().save();
            AccountManager.getInstance().setLastAlt(null);
            GuiAltManager.getInstance().currentAccount = null;
            this.status = "The alt has been removed succesfully.";
        }
        if (button.id == 5 || button.id == 6) {
            final ArrayList<Account> registry = (button.id == 5) ? AccountManager.getInstance().getNotBannedAccounts() : AccountManager.getInstance().getAccounts();
            final Random random = new Random();
            if (registry.size() == 0) {
                this.status = "You don't have any account eligible for this.";
                return;
            }
            final Account randomAlt = registry.get(random.nextInt(registry.size()));
            final String user2 = randomAlt.getEmail();
            final String pass2 = randomAlt.getPassword();
            GuiAltManager.getInstance().currentAccount = randomAlt;
            try {
                (GuiAltManager.getInstance().loginThread = new AccountLoginThread(user2, pass2)).start();
                AccountManager.getInstance().save();
                if (GuiDisconnected.serverData != null) {
                    this.mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this.mc, GuiDisconnected.serverData));
                }
            }
            catch (final Exception ex) {}
        }
        if (button.id == 7) {
            if (AccountManager.getInstance().getAlteningKey() == null) {
                this.status = "No TheAltening key...";
                return;
            }
            GuiDisconnected.useTheAltening = true;
            try {
                final TheAltening theAltening = new TheAltening(AccountManager.getInstance().getAlteningKey());
                final AlteningAlt account = theAltening.generateAccount(theAltening.getUser());
                if (!Objects.requireNonNull(account).getToken().isEmpty()) {
                    AccountManager.getInstance().setAlteningKey(AccountManager.getInstance().getAlteningKey());
                    AccountManager.getInstance().setLastAlteningAlt(Objects.requireNonNull(account).getToken());
                    (GuiAlteningLogin.thread = new AccountLoginThread(Objects.requireNonNull(account).getToken().replaceAll(" ", ""), "nig")).start();
                    AccountManager.getInstance().save();
                }
                if (GuiDisconnected.serverData != null) {
                    this.mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this.mc, GuiDisconnected.serverData));
                }
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        if (button.id == 8 && GuiDisconnected.serverData != null) {
            this.mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this.mc, GuiDisconnected.serverData));
        }
        if (button.id == 9 && GuiDisconnected.serverData != null && !GuiScreenServerList.proxy.getText().isEmpty()) {
            ProxyUtils.setProxy(GuiScreenServerList.isEnabled ? null : ProxyUtils.getProxyFromString(GuiScreenServerList.proxy.getText()));
            GuiScreenServerList.isEnabled = !GuiScreenServerList.isEnabled;
            this.mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this.mc, GuiDisconnected.serverData));
        }
        if (button.id == 10) {
            this.mc.displayGuiScreen(new GuiProtocolSelector(this));
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, this.status, GuiDisconnected.width / 2, 10, 16777215);
        Gui.drawCenteredString(this.fontRendererObj, this.reason, GuiDisconnected.width / 2, GuiDisconnected.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = GuiDisconnected.height / 2 - this.field_175353_i / 2;
        if (this.multilineMessage != null) {
            if (GuiUISettings.enabledAnimations[2]) {
                String[] animationText;
                for (int length = (animationText = this.animationText).length, j = 0; j < length; ++j) {
                    final String s = animationText[j];
                    Gui.drawCenteredString(this.fontRendererObj, s, GuiDisconnected.width / 2, i, 16777215);
                    i += this.fontRendererObj.FONT_HEIGHT;
                }
            }
            else {
                for (final String s : this.multilineMessage) {
                    Gui.drawCenteredString(this.fontRendererObj, s, GuiDisconnected.width / 2, i, 16777215);
                    i += this.fontRendererObj.FONT_HEIGHT;
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
