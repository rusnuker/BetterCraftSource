// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.serverfinder;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.multiplayer.ServerData;
import java.net.UnknownHostException;
import me.amkgre.bettercraft.client.utils.NewServerPingerUtils;
import java.util.ArrayList;
import java.net.InetAddress;
import org.lwjgl.input.Keyboard;
import me.amkgre.bettercraft.client.utils.MathUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiServerFinder extends GuiScreen
{
    private static final String[] stateStrings;
    private GuiTextField ipBox;
    private GuiTextField maxThreadsBox;
    private int checked;
    private int working;
    public static String renderText;
    private ServerFinderState state;
    private GuiMultiplayer before;
    
    static {
        stateStrings = new String[] { "", "§dSearching...", "§dResolving...", "§4Unknown Host!", "§4Cancelled!", "§aDone!", "§4An error occurred!" };
        GuiServerFinder.renderText = "";
    }
    
    public GuiServerFinder(final GuiMultiplayer before) {
        this.before = before;
    }
    
    @Override
    public void updateScreen() {
        this.ipBox.updateCursorCounter();
        this.buttonList.get(0).displayString = (this.state.isRunning() ? "§cCancel" : "§aSearch");
        this.ipBox.setEnabled(!this.state.isRunning());
        this.maxThreadsBox.setEnabled(!this.state.isRunning());
        this.buttonList.get(0).enabled = (MathUtils.isInteger(this.maxThreadsBox.getText()) && !this.ipBox.getText().isEmpty());
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiServerFinder.width / 2 - 100, GuiServerFinder.height / 4 + 100, "Search"));
        this.buttonList.add(new GuiButton(1, GuiServerFinder.width / 2 - 100, GuiServerFinder.height / 4 + 125, "Delete"));
        this.buttonList.add(new GuiButton(2, GuiServerFinder.width / 2 - 100, GuiServerFinder.height / 4 + 150, "Back"));
        (this.ipBox = new GuiTextField(0, this.fontRendererObj, GuiServerFinder.width / 2 - 100, GuiServerFinder.height / 4 + 25, 200, 20)).setMaxStringLength(200);
        this.ipBox.setFocused(true);
        (this.maxThreadsBox = new GuiTextField(1, this.fontRendererObj, GuiServerFinder.width / 2 - 32, GuiServerFinder.height / 4 + 50, 26, 12)).setMaxStringLength(3);
        this.maxThreadsBox.setFocused(false);
        this.maxThreadsBox.setText(Integer.toString(1));
        this.state = ServerFinderState.NOT_RUNNING;
        GuiServerFinder.renderText = "";
    }
    
    @Override
    public void onGuiClosed() {
        this.state = ServerFinderState.CANCELLED;
        MathUtils.isInteger(this.maxThreadsBox.getText());
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton clickedButton) {
        if (clickedButton.enabled) {
            if (clickedButton.id == 0) {
                if (this.state.isRunning()) {
                    this.state = ServerFinderState.CANCELLED;
                }
                else {
                    MathUtils.isInteger(this.maxThreadsBox.getText());
                    this.state = ServerFinderState.RESOLVING;
                    this.checked = 0;
                    this.working = 0;
                    new Thread("Server Finder") {
                        @Override
                        public void run() {
                            try {
                                final InetAddress addr = InetAddress.getByName(GuiServerFinder.this.ipBox.getText().split(":")[0].trim());
                                final int[] ipParts = new int[4];
                                for (int i2 = 0; i2 < 4; ++i2) {
                                    ipParts[i2] = (addr.getAddress()[i2] & 0xFF);
                                }
                                GuiServerFinder.access(GuiServerFinder.this, ServerFinderState.SEARCHING);
                                final ArrayList<NewServerPingerUtils> pingers = new ArrayList<NewServerPingerUtils>();
                                final int[] arrn = { 0, 1, -1, 2, -2, 3, -3 };
                                final int[] array;
                                final int[] changes = array = arrn;
                                for (int length = changes.length, j2 = 0; j2 < length; ++j2) {
                                    final int change = array[j2];
                                    for (int i3 = 0; i3 <= 255; ++i3) {
                                        if (GuiServerFinder.this.state == ServerFinderState.CANCELLED) {
                                            return;
                                        }
                                        final int[] ipParts2 = ipParts.clone();
                                        ipParts2[2] = (ipParts[2] + change & 0xFF);
                                        ipParts2[3] = i3;
                                        final String ip2 = String.valueOf(String.valueOf(String.valueOf(ipParts2[0]))) + "." + ipParts2[1] + "." + ipParts2[2] + "." + ipParts2[3];
                                        final NewServerPingerUtils pinger = new NewServerPingerUtils();
                                        pinger.ping(ip2);
                                        pingers.add(pinger);
                                        while (pingers.size() >= 100) {
                                            if (GuiServerFinder.this.state == ServerFinderState.CANCELLED) {
                                                return;
                                            }
                                            GuiServerFinder.this.updatePingers(pingers);
                                        }
                                    }
                                }
                                while (pingers.size() > 0) {
                                    if (GuiServerFinder.this.state == ServerFinderState.CANCELLED) {
                                        return;
                                    }
                                    GuiServerFinder.this.updatePingers(pingers);
                                }
                                GuiServerFinder.access(GuiServerFinder.this, ServerFinderState.DONE);
                            }
                            catch (final UnknownHostException e2) {
                                GuiServerFinder.access(GuiServerFinder.this, ServerFinderState.UNKNOWN_HOST);
                            }
                            catch (final Exception e3) {
                                e3.printStackTrace();
                                GuiServerFinder.access(GuiServerFinder.this, ServerFinderState.ERROR);
                            }
                        }
                    }.start();
                }
            }
            else if (clickedButton.id != 1 && clickedButton.id == 2) {
                this.mc.displayGuiScreen(this.before);
            }
            else if (clickedButton.id == 1) {
                this.serverOutList("Grief me #");
                GuiServerFinder.renderText = "§aSuccessful";
            }
        }
    }
    
    private void serverOutList(final String ip2) {
        for (int i2 = 0; i2 < this.before.savedServerList.countServers(); ++i2) {
            if (this.before.savedServerList.getServerData(i2).serverName.startsWith(ip2)) {
                this.before.savedServerList.removeServerData(i2);
                this.before.savedServerList.saveServerList();
                this.before.serverListSelector.setSelectedSlotIndex(-1);
                this.before.serverListSelector.updateOnlineServers(this.before.savedServerList);
            }
        }
    }
    
    private boolean serverInList(final String ip2) {
        for (int i2 = 0; i2 < this.before.savedServerList.countServers(); ++i2) {
            if (this.before.savedServerList.getServerData(i2).serverIP.equals(ip2)) {
                return true;
            }
        }
        return false;
    }
    
    private void updatePingers(final ArrayList<NewServerPingerUtils> pingers) {
        for (int i2 = 0; i2 < pingers.size(); ++i2) {
            if (!pingers.get(i2).isStillPinging()) {
                ++this.checked;
                if (pingers.get(i2).isWorking()) {
                    ++this.working;
                    if (!this.serverInList(pingers.get(i2).serverData.serverIP)) {
                        this.before.savedServerList.addServerData(new ServerData("Grief me #" + this.working, pingers.get(i2).serverData.serverIP, false));
                        this.before.savedServerList.saveServerList();
                        this.before.serverListSelector.setSelectedSlotIndex(-1);
                        this.before.serverListSelector.updateOnlineServers(this.before.savedServerList);
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
        Gui.drawCenteredString(this.fontRendererObj, "This will search for servers with similar IPs", GuiServerFinder.width / 2, 20, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, "to the IP you type into the field below.", GuiServerFinder.width / 2, 30, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, "The servers it finds will be added to your server list.", GuiServerFinder.width / 2, 40, 10526880);
        Gui.drawCenteredString(this.fontRendererObj, "MCHost24 Server Scan §d45.81.233.1", GuiServerFinder.width / 2, 60, 10526880);
        Gui.drawString(this.fontRendererObj, "Server address:", GuiServerFinder.width / 2 - 100, GuiServerFinder.height / 4 + 15, 10526880);
        this.ipBox.drawTextBox();
        Gui.drawString(this.fontRendererObj, "Max. threads:", GuiServerFinder.width / 2 - 100, GuiServerFinder.height / 4 + 50, 10526880);
        this.maxThreadsBox.drawTextBox();
        Gui.drawCenteredString(this.fontRendererObj, this.state.toString(), GuiServerFinder.width / 2, GuiServerFinder.height / 4 + 65, 10526880);
        Gui.drawString(this.fontRendererObj, "Checked: §f" + this.checked + " §8/ §c1792", GuiServerFinder.width / 2 - 100, GuiServerFinder.height / 4 + 75, 10526880);
        Gui.drawString(this.fontRendererObj, "Working: §a" + this.working, GuiServerFinder.width / 2 - 100, GuiServerFinder.height / 4 + 85, 10526880);
        Gui.drawCenteredString(this.mc.fontRendererObj, GuiServerFinder.renderText, GuiServerFinder.width / 2, GuiServerFinder.height / 2 - 50, 0);
        super.drawScreen(par1, par2, par3);
    }
    
    static void access(final GuiServerFinder guiServerFinder, final ServerFinderState state) {
        guiServerFinder.state = state;
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
            return GuiServerFinder.stateStrings[this.ordinal()];
        }
    }
}
