// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui.serverlists.serverfinder;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.multiplayer.ServerData;
import java.net.UnknownHostException;
import me.nzxtercode.bettercraft.client.gui.serverlists.serverfinder.impl.NewServerPinger;
import java.util.ArrayList;
import java.net.InetAddress;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiServerFinderAddServer extends GuiScreen
{
    private static final String[] stateStrings;
    private GuiTextField ipBox;
    private GuiTextField maxThreadsBox;
    private int checked;
    private int working;
    private ServerFinderState state;
    private GuiServerFinderMultiplayer parent;
    
    static {
        stateStrings = new String[] { "", "§dSearching...", "§dResolving...", "§4Unknown Host!", "§4Cancelled!", "§aDone!", "§4An error occurred!" };
    }
    
    public GuiServerFinderAddServer(final GuiServerFinderMultiplayer parent) {
        this.parent = parent;
    }
    
    @Override
    public void updateScreen() {
        this.ipBox.updateCursorCounter();
        this.buttonList.get(0).displayString = (this.state.isRunning() ? "§cCancel" : "§aSearch");
        this.ipBox.setEnabled(!this.state.isRunning());
        this.maxThreadsBox.setEnabled(!this.state.isRunning());
        this.buttonList.get(0).enabled = (!this.ipBox.getText().isEmpty() && !this.maxThreadsBox.getText().isEmpty());
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiServerFinderAddServer.width / 2 - 100, GuiServerFinderAddServer.height / 4 + 100, "Search"));
        this.buttonList.add(new GuiButton(2, GuiServerFinderAddServer.width / 2 - 100, GuiServerFinderAddServer.height / 4 + 125, "Back"));
        (this.ipBox = new GuiTextField(0, this.fontRendererObj, GuiServerFinderAddServer.width / 2 - 100, GuiServerFinderAddServer.height / 4 + 25, 200, 20)).setMaxStringLength(200);
        this.ipBox.setFocused(true);
        (this.maxThreadsBox = new GuiTextField(1, this.fontRendererObj, GuiServerFinderAddServer.width / 2 - 32, GuiServerFinderAddServer.height / 4 + 50, 26, 12)).setMaxStringLength(3);
        this.maxThreadsBox.setFocused(false);
        this.maxThreadsBox.setText(Integer.toString(1));
        this.state = ServerFinderState.NOT_RUNNING;
    }
    
    @Override
    public void onGuiClosed() {
        this.state = ServerFinderState.CANCELLED;
        Boolean.parseBoolean(String.valueOf(Integer.parseInt(this.maxThreadsBox.getText())));
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.enabled) {
            if (button.id == 0) {
                if (this.state.isRunning()) {
                    this.state = ServerFinderState.CANCELLED;
                }
                else {
                    this.state = ServerFinderState.RESOLVING;
                    this.checked = 0;
                    this.working = 0;
                    new Thread("Server Finder") {
                        @Override
                        public void run() {
                            try {
                                final InetAddress addr = InetAddress.getByName(GuiServerFinderAddServer.this.ipBox.getText().split(":")[0].trim());
                                final int[] ipParts = new int[4];
                                for (int i = 0; i < 4; ++i) {
                                    ipParts[i] = (addr.getAddress()[i] & 0xFF);
                                }
                                GuiServerFinderAddServer.access$2(GuiServerFinderAddServer.this, ServerFinderState.SEARCHING);
                                final ArrayList<NewServerPinger> pingers = new ArrayList<NewServerPinger>();
                                final int[] changes = { 0, 1, -1, 2, -2, 3, -3 };
                                int[] array;
                                for (int length = (array = changes).length, j = 0; j < length; ++j) {
                                    final int change = array[j];
                                    for (int i2 = 0; i2 <= 255; ++i2) {
                                        if (GuiServerFinderAddServer.this.state == ServerFinderState.CANCELLED) {
                                            return;
                                        }
                                        final int[] ipParts2 = ipParts.clone();
                                        ipParts2[2] = (ipParts[2] + change & 0xFF);
                                        ipParts2[3] = i2;
                                        final String ip = String.valueOf(ipParts2[0]) + "." + ipParts2[1] + "." + ipParts2[2] + "." + ipParts2[3];
                                        final NewServerPinger pinger = new NewServerPinger();
                                        pinger.ping(ip);
                                        pingers.add(pinger);
                                        while (pingers.size() >= Integer.parseInt(GuiServerFinderAddServer.this.maxThreadsBox.getText())) {
                                            if (GuiServerFinderAddServer.this.state == ServerFinderState.CANCELLED) {
                                                return;
                                            }
                                            GuiServerFinderAddServer.this.updatePingers(pingers);
                                        }
                                    }
                                }
                                while (pingers.size() > 0) {
                                    if (GuiServerFinderAddServer.this.state == ServerFinderState.CANCELLED) {
                                        return;
                                    }
                                    GuiServerFinderAddServer.this.updatePingers(pingers);
                                }
                                GuiServerFinderAddServer.access$2(GuiServerFinderAddServer.this, ServerFinderState.DONE);
                            }
                            catch (final UnknownHostException e) {
                                GuiServerFinderAddServer.access$2(GuiServerFinderAddServer.this, ServerFinderState.UNKNOWN_HOST);
                            }
                            catch (final Exception e2) {
                                e2.printStackTrace();
                                GuiServerFinderAddServer.access$2(GuiServerFinderAddServer.this, ServerFinderState.ERROR);
                            }
                        }
                    }.start();
                }
            }
            else if (button.id == 2) {
                this.mc.displayGuiScreen(this.parent);
            }
        }
    }
    
    private boolean serverInList(final String ip2) {
        for (int i2 = 0; i2 < GuiServerFinderMultiplayer.savedServerList.countServers(); ++i2) {
            if (GuiServerFinderMultiplayer.savedServerList.getServerData(i2).serverIP.equals(ip2)) {
                return true;
            }
        }
        return false;
    }
    
    private void updatePingers(final ArrayList<NewServerPinger> pingers) {
        for (int i2 = 0; i2 < pingers.size(); ++i2) {
            if (!pingers.get(i2).isStillPinging()) {
                ++this.checked;
                if (pingers.get(i2).isWorking()) {
                    ++this.working;
                    if (!this.serverInList(pingers.get(i2).serverData.serverIP)) {
                        GuiServerFinderMultiplayer.savedServerList.addServerData(new ServerData("Grief me #" + this.working, pingers.get(i2).serverData.serverIP, false));
                        GuiServerFinderMultiplayer.savedServerList.saveServerList();
                        this.parent.serverListSelector.setSelectedSlotIndex(-1);
                        this.parent.serverListSelector.func_148195_a(GuiServerFinderMultiplayer.savedServerList);
                    }
                }
                pingers.remove(i2);
            }
        }
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.ipBox.textboxKeyTyped(par1, par2);
        this.maxThreadsBox.textboxKeyTyped(par1, par2);
        if (par2 == 28 || par2 == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        this.ipBox.mouseClicked(par1, par2, par3);
        this.maxThreadsBox.mouseClicked(par1, par2, par3);
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, "This will search for servers with similar IPs", GuiServerFinderAddServer.width / 2, 20, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, "to the IP you type into the field below.", GuiServerFinderAddServer.width / 2, 30, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, "The servers it finds will be added to your server list.", GuiServerFinderAddServer.width / 2, 40, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, "LivingBots §c45.88.108.0 - 45.88.111.255 §rMCHost24 §c45.81.233.0 - 45.81.235.255", GuiServerFinderAddServer.width / 2, 60, 10526880);
        this.drawString(this.fontRendererObj, "Server address:", GuiServerFinderAddServer.width / 2 - 100, GuiServerFinderAddServer.height / 4 + 15, 10526880);
        this.ipBox.drawTextBox();
        this.drawString(this.fontRendererObj, "Max. threads:", GuiServerFinderAddServer.width / 2 - 100, GuiServerFinderAddServer.height / 4 + 50, 10526880);
        this.maxThreadsBox.drawTextBox();
        Gui.drawCenteredString(this.fontRendererObj, this.state.toString(), GuiServerFinderAddServer.width / 2, GuiServerFinderAddServer.height / 4 + 65, 10526880);
        this.drawString(this.fontRendererObj, "Checked: §f" + this.checked + " §8/ §c1792", GuiServerFinderAddServer.width / 2 - 100, GuiServerFinderAddServer.height / 4 + 75, 10526880);
        this.drawString(this.fontRendererObj, "Working: §a" + this.working, GuiServerFinderAddServer.width / 2 - 100, GuiServerFinderAddServer.height / 4 + 85, 10526880);
        super.drawScreen(par1, par2, par3);
    }
    
    static void access(final GuiServerFinderAddServer guiServerFinder, final ServerFinderState state) {
        guiServerFinder.state = state;
    }
    
    static /* synthetic */ void access$2(final GuiServerFinderAddServer guiServerFinderAddServer, final ServerFinderState state) {
        guiServerFinderAddServer.state = state;
    }
    
    enum ServerFinderState
    {
        NOT_RUNNING("NOT_RUNNING", 0, "NOT_RUNNING", 0), 
        SEARCHING("SEARCHING", 1, "SEARCHING", 1), 
        RESOLVING("RESOLVING", 2, "RESOLVING", 2), 
        UNKNOWN_HOST("UNKNOWN_HOST", 3, "UNKNOWN_HOST", 3), 
        CANCELLED("CANCELLED", 4, "CANCELLED", 4), 
        DONE("DONE", 5, "DONE", 5), 
        ERROR("ERROR", 6, "ERROR", 6);
        
        private ServerFinderState(final String s3, final int n4, final String s2, final int n3) {
        }
        
        public boolean isRunning() {
            return this == ServerFinderState.SEARCHING || this == ServerFinderState.RESOLVING;
        }
        
        @Override
        public String toString() {
            return GuiServerFinderAddServer.stateStrings[this.ordinal()];
        }
    }
}
