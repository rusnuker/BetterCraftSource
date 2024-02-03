// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import java.io.InputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.net.Socket;

public class TeamSpeakController implements Runnable
{
    private static TeamSpeakController instance;
    private Socket socket;
    private BufferedWriter writer;
    private List<ControlListener> listeners;
    public String serverIP;
    public int serverPort;
    TeamSpeakUser me;
    private boolean connectionEstablished;
    private boolean tested;
    
    public TeamSpeakController() {
        this.listeners = new ArrayList<ControlListener>();
        this.serverIP = "";
        this.serverPort = 0;
        this.tested = false;
        TeamSpeakController.instance = this;
        new Thread(this, "TeamSpeak").start();
    }
    
    public TeamSpeakController(final ControlListener listener) {
        this();
        this.listeners.add(listener);
    }
    
    public static TeamSpeakController getInstance() {
        return TeamSpeakController.instance;
    }
    
    public void addControlListener(final ControlListener listener) {
        this.listeners.add(listener);
    }
    
    @Override
    public void run() {
        try {
            TeamSpeak.print("Connect to TeamSpeak..");
            this.socket = new Socket("localhost", 25639);
            final OutputStream outputstream = this.socket.getOutputStream();
            final InputStream inputstream = this.socket.getInputStream();
            this.writer = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(outputstream), Charset.forName("utf-8")));
            new InputStreamReaderThread(inputstream);
            new OutputStreamWriterThread(outputstream);
            TeamspeakAuth.auth(outputstream);
            this.onEnable();
        }
        catch (final UnknownHostException unknownhostexception) {
            TeamSpeak.print("TeamSpeak Connection Error: " + unknownhostexception.getMessage());
            unknownhostexception.printStackTrace();
        }
        catch (final IOException ioexception) {
            TeamSpeak.print("Can't connect to TeamSpeak");
            ioexception.printStackTrace();
        }
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    protected void sendMessage(final String message) {
        if (this.getSocket() != null && !this.getSocket().isClosed()) {
            try {
                this.writer.write(String.valueOf(message) + "\n");
                this.writer.flush();
            }
            catch (final IOException var3) {
                TeamSpeak.print("Can't send message");
                this.reset();
            }
        }
        else {
            TeamSpeak.print("Trying to send message via closed socket");
        }
    }
    
    public static void main(final String[] args) {
        new TeamSpeakController();
    }
    
    protected void onMessageRecieved(final String message) {
        if (!message.isEmpty()) {}
        if (message.startsWith("notify")) {
            this.handleNotifyMessage(message);
        }
        else if (message.startsWith("clid") && message.contains("|")) {
            final ArrayList<Integer> arraylist1 = new ArrayList<Integer>();
            final String[] astring4 = message.split("[|]");
            String[] array;
            for (int length = (array = astring4).length, n = 0; n < length; ++n) {
                final String s1 = array[n];
                final String[] astring5 = s1.split(" ");
                final Argument[] ateamspeakcontroller$argument3 = new Argument[astring5.length];
                for (int l = 0; l < ateamspeakcontroller$argument3.length; ++l) {
                    ateamspeakcontroller$argument3[l] = new Argument(astring5[l]);
                }
                TeamSpeakUser teamspeakuser3 = null;
                if (TeamSpeakUser.contains(ateamspeakcontroller$argument3[0].getAsInt())) {
                    teamspeakuser3 = getInstance().getUser(ateamspeakcontroller$argument3[0].getAsInt());
                }
                else {
                    teamspeakuser3 = new TeamSpeakUser(ateamspeakcontroller$argument3[0].getAsInt());
                }
                if (teamspeakuser3 != null) {
                    teamspeakuser3.updateChannelId(ateamspeakcontroller$argument3[1].getAsInt());
                    teamspeakuser3.updateDatabaseId(ateamspeakcontroller$argument3[2].getAsInt());
                    teamspeakuser3.updateNickname(TeamSpeak.fix(ateamspeakcontroller$argument3[3].getValue()));
                    teamspeakuser3.updateTyping(ateamspeakcontroller$argument3[4].getAsBoolean());
                    teamspeakuser3.updateAway(ateamspeakcontroller$argument3[5].getAsBoolean(), TeamSpeak.fix(ateamspeakcontroller$argument3[6].getValue()));
                    teamspeakuser3.updateTalkStatus(ateamspeakcontroller$argument3[7].getAsBoolean());
                    teamspeakuser3.updateClientInput(ateamspeakcontroller$argument3[8].getAsBoolean());
                    teamspeakuser3.updateClientOutput(ateamspeakcontroller$argument3[9].getAsBoolean());
                    teamspeakuser3.updateClientInputHardware(ateamspeakcontroller$argument3[10].getAsBoolean());
                    teamspeakuser3.updateClientOutputHardware(ateamspeakcontroller$argument3[11].getAsBoolean());
                    teamspeakuser3.updateTalkPower(ateamspeakcontroller$argument3[12].getAsInt());
                    teamspeakuser3.updateTalker(ateamspeakcontroller$argument3[13].getAsBoolean());
                    teamspeakuser3.updatePrioritySpeaker(ateamspeakcontroller$argument3[14].getAsBoolean());
                    teamspeakuser3.updateRecording(ateamspeakcontroller$argument3[15].getAsBoolean());
                    teamspeakuser3.updateChannelCommander(ateamspeakcontroller$argument3[16].getAsBoolean());
                    teamspeakuser3.updateMuted(ateamspeakcontroller$argument3[17].getAsBoolean());
                    teamspeakuser3.updateUid(ateamspeakcontroller$argument3[18].getValue());
                    teamspeakuser3.updateServerGroups(ateamspeakcontroller$argument3[19].getAsIntArray());
                    teamspeakuser3.updateChannelGroupId(ateamspeakcontroller$argument3[20].getAsInt());
                    teamspeakuser3.updateIconId(ateamspeakcontroller$argument3[21].getAsInt());
                    teamspeakuser3.updateCountry(ateamspeakcontroller$argument3[22].getValue());
                    arraylist1.add(teamspeakuser3.getClientId());
                }
            }
            final List<TeamSpeakUser> list1 = TeamSpeakUser.getUsers();
            Collections.synchronizedList(list1);
            final ArrayList<TeamSpeakUser> arraylist2 = new ArrayList<TeamSpeakUser>();
            for (final TeamSpeakUser teamspeakuser4 : list1) {
                if (!arraylist1.contains(teamspeakuser4.getClientId())) {
                    arraylist2.add(teamspeakuser4);
                }
            }
            for (final TeamSpeakUser teamspeakuser5 : arraylist2) {
                TeamSpeakUser.unregisterUser(teamspeakuser5);
            }
        }
        else if (message.startsWith("cid") && message.contains("|")) {
            final ArrayList<Integer> arraylist3 = new ArrayList<Integer>();
            final String[] astring6 = message.split("[|]");
            String[] array2;
            for (int length2 = (array2 = astring6).length, n2 = 0; n2 < length2; ++n2) {
                final String s2 = array2[n2];
                final String[] astring7 = s2.split(" ");
                final Argument[] ateamspeakcontroller$argument4 = new Argument[astring7.length];
                for (int j = 0; j < ateamspeakcontroller$argument4.length; ++j) {
                    ateamspeakcontroller$argument4[j] = new Argument(astring7[j]);
                }
                TeamSpeakChannel teamspeakchannel2 = null;
                if (TeamSpeakChannel.contains(ateamspeakcontroller$argument4[0].getAsInt())) {
                    teamspeakchannel2 = getInstance().getChannel(ateamspeakcontroller$argument4[0].getAsInt());
                }
                else {
                    teamspeakchannel2 = new TeamSpeakChannel(ateamspeakcontroller$argument4[0].getAsInt());
                }
                teamspeakchannel2.updatePID(ateamspeakcontroller$argument4[1].getAsInt());
                teamspeakchannel2.updateChannelOrder(ateamspeakcontroller$argument4[2].getAsInt());
                teamspeakchannel2.updateChannelName(TeamSpeak.fix(ateamspeakcontroller$argument4[3].getValue()));
                teamspeakchannel2.updateTopic(ateamspeakcontroller$argument4[4].getValue());
                teamspeakchannel2.updateFlagDefault(ateamspeakcontroller$argument4[5].getAsBoolean());
                teamspeakchannel2.updateIsPassword(ateamspeakcontroller$argument4[6].getAsBoolean());
                teamspeakchannel2.updatePermanent(ateamspeakcontroller$argument4[7].getAsBoolean());
                teamspeakchannel2.updateSemiPermanent(ateamspeakcontroller$argument4[8].getAsBoolean());
                teamspeakchannel2.updateChannelCodec(ateamspeakcontroller$argument4[9].getAsInt());
                teamspeakchannel2.updateChannelCodecQuality(ateamspeakcontroller$argument4[10].getAsInt());
                teamspeakchannel2.updateTalkPower(ateamspeakcontroller$argument4[11].getAsInt());
                teamspeakchannel2.updateIconID(ateamspeakcontroller$argument4[12].getAsInt());
                teamspeakchannel2.updateMaxClients(ateamspeakcontroller$argument4[13].getAsInt());
                teamspeakchannel2.updateMaxFamilyClients(ateamspeakcontroller$argument4[14].getAsInt());
                teamspeakchannel2.updateFlagAreSubscribed(ateamspeakcontroller$argument4[15].getAsBoolean());
                if (ateamspeakcontroller$argument4.length == 17) {
                    teamspeakchannel2.updateTotalClients(ateamspeakcontroller$argument4[16].getAsInt());
                }
                arraylist3.add(teamspeakchannel2.getChannelId());
            }
            final List<TeamSpeakChannel> list2 = TeamSpeakChannel.getChannels();
            Collections.synchronizedList(list2);
            final ArrayList<TeamSpeakChannel> arraylist4 = new ArrayList<TeamSpeakChannel>();
            for (final TeamSpeakChannel teamspeakchannel3 : list2) {
                if (!arraylist3.contains(teamspeakchannel3.getChannelId())) {
                    arraylist4.add(teamspeakchannel3);
                }
            }
            for (final TeamSpeakChannel teamspeakchannel4 : arraylist4) {
                TeamSpeakChannel.deleteChannel(teamspeakchannel4);
            }
        }
        else if (message.startsWith("clid") && !message.contains("|") && message.contains("cid")) {
            final String[] astring8 = message.split(" ");
            final Argument[] ateamspeakcontroller$argument5 = new Argument[astring8.length];
            for (int k = 0; k < ateamspeakcontroller$argument5.length; ++k) {
                ateamspeakcontroller$argument5[k] = new Argument(astring8[k]);
            }
            if (ateamspeakcontroller$argument5.length == 2) {
                final TeamSpeakUser teamspeakuser6 = this.getUser(ateamspeakcontroller$argument5[0].getAsInt());
                if (teamspeakuser6 != null) {
                    teamspeakuser6.updateChannelId(ateamspeakcontroller$argument5[1].getAsInt());
                    this.me = teamspeakuser6;
                }
            }
        }
        else if (message.startsWith("ip=") && message.contains("port=")) {
            final String[] astring9 = message.split(" ");
            final Argument[] ateamspeakcontroller$argument6 = new Argument[astring9.length];
            for (int i = 0; i < ateamspeakcontroller$argument6.length; ++i) {
                ateamspeakcontroller$argument6[i] = new Argument(astring9[i]);
            }
            if (ateamspeakcontroller$argument6.length >= 2) {
                this.serverIP = ateamspeakcontroller$argument6[0].getValue();
                this.serverPort = ateamspeakcontroller$argument6[1].getAsInt();
                TeamSpeak.print("Connected to " + this.serverIP + ":" + this.serverPort);
                TeamSpeak.setupChat();
                for (final ControlListener controllistener : this.listeners) {
                    controllistener.onConnect();
                }
            }
        }
        else {
            this.handleOther(message);
        }
    }
    
    private void onEnable() {
        TeamSpeak.print("Successfully connected to TeamSpeak!");
        this.updateInformation(EnumUpdateType.ALL);
        this.sendMessage("clientnotifyregister schandlerid=1 event=any");
        final String s = "Hallo lieber Decompiler, wie geht's dir heute? Macht das Code-Lesen denn auch Spa\u00df? Gr\u00fc\u00dfe, die Internet-Polizei";
        s.length();
    }
    
    private void handleOther(final String message) {
        final String[] astring = message.split(" ");
        final Argument[] ateamspeakcontroller$argument = new Argument[astring.length - 1];
        for (int i = 0; i < ateamspeakcontroller$argument.length; ++i) {
            ateamspeakcontroller$argument[i] = new Argument(astring[i + 1]);
        }
        final String s1 = astring[0];
        if (s1.equalsIgnoreCase("error")) {
            final int j = ateamspeakcontroller$argument[0].getAsInt();
            if (j != 0 && j != 1794) {
                final String s2 = ateamspeakcontroller$argument[1].getValue();
                for (final ControlListener controllistener : this.listeners) {
                    controllistener.onError(j, s2);
                }
            }
        }
    }
    
    private void handleNotifyMessage(String message) {
        message = message.substring(6, message.length());
        final String[] astring = message.split(" ");
        Argument[] ateamspeakcontroller$argument = new Argument[astring.length - 1];
        for (int i = 0; i < ateamspeakcontroller$argument.length; ++i) {
            ateamspeakcontroller$argument[i] = new Argument(astring[i + 1]);
        }
        final String s = astring[0];
        if (s.equalsIgnoreCase("talkstatuschange")) {
            final TeamSpeakUser teamspeakuser = this.getUser(ateamspeakcontroller$argument[3].getAsInt());
            if (teamspeakuser != null) {
                teamspeakuser.updateTalkStatus(ateamspeakcontroller$argument[1].getAsBoolean());
            }
            else {
                this.updateInformation(EnumUpdateType.CLIENTS);
            }
        }
        else if (s.equalsIgnoreCase("cliententerview")) {
            if (this.getUser(ateamspeakcontroller$argument[4].getAsInt()) != null) {
                return;
            }
            final TeamSpeakUser teamspeakuser2 = new TeamSpeakUser(ateamspeakcontroller$argument[4].getAsInt());
            teamspeakuser2.updateChannelId(ateamspeakcontroller$argument[14].getAsInt());
            teamspeakuser2.updateNickname(ateamspeakcontroller$argument[6].getValue().replace("\\s", " "));
            teamspeakuser2.updateClientInput(ateamspeakcontroller$argument[7].getAsBoolean());
            teamspeakuser2.updateClientOutput(ateamspeakcontroller$argument[8].getAsBoolean());
            for (final ControlListener controllistener : this.listeners) {
                controllistener.onClientConnect(teamspeakuser2);
            }
            this.updateInformation(EnumUpdateType.CLIENTS);
            TeamSpeak.updateScroll(teamspeakuser2.getChannelId(), true);
        }
        else if (s.equalsIgnoreCase("clientleftview")) {
            if (ateamspeakcontroller$argument.length > 5 && ateamspeakcontroller$argument[5].isInt()) {
                final TeamSpeakUser teamspeakuser3 = this.getUser(ateamspeakcontroller$argument[5].getAsInt());
                if (ateamspeakcontroller$argument[3].getAsInt() == 3) {
                    for (final ControlListener controllistener2 : this.listeners) {
                        controllistener2.onClientTimout(teamspeakuser3);
                        TeamSpeakUser.unregisterUser(teamspeakuser3);
                    }
                }
                else {
                    for (final ControlListener controllistener3 : this.listeners) {
                        controllistener3.onClientDisconnected(teamspeakuser3, ateamspeakcontroller$argument[4].getValue());
                        TeamSpeakUser.unregisterUser(teamspeakuser3);
                    }
                }
            }
            else {
                final TeamSpeakUser teamspeakuser4 = this.getUser(ateamspeakcontroller$argument[3].getAsInt());
                if (ateamspeakcontroller$argument[2].getAsInt() == 3) {
                    for (final ControlListener controllistener4 : this.listeners) {
                        controllistener4.onClientTimout(teamspeakuser4);
                        TeamSpeakUser.unregisterUser(teamspeakuser4);
                    }
                }
                else {
                    for (final ControlListener controllistener5 : this.listeners) {
                        controllistener5.onClientDisconnected(teamspeakuser4, "Disconnected");
                        TeamSpeakUser.unregisterUser(teamspeakuser4);
                    }
                }
            }
            this.updateInformation(EnumUpdateType.CLIENTS);
            final TeamSpeakUser teamspeakuser5 = this.getUser(ateamspeakcontroller$argument[3].getAsInt());
            if (teamspeakuser5 != null) {
                TeamSpeak.updateScroll(teamspeakuser5.getChannelId(), false);
            }
        }
        else if (s.equalsIgnoreCase("clientupdated")) {
            final TeamSpeakUser teamspeakuser6 = this.getUser(ateamspeakcontroller$argument[1].getAsInt());
            if (teamspeakuser6 == null) {
                return;
            }
            if (ateamspeakcontroller$argument[2].getKey().equalsIgnoreCase("client_input_muted")) {
                teamspeakuser6.updateClientInput(ateamspeakcontroller$argument[2].getAsBoolean());
            }
            else if (ateamspeakcontroller$argument[2].getKey().equalsIgnoreCase("client_output_muted")) {
                teamspeakuser6.updateClientOutput(ateamspeakcontroller$argument[2].getAsBoolean());
            }
            else if (ateamspeakcontroller$argument[2].getKey().equalsIgnoreCase("client_input_hardware")) {
                teamspeakuser6.updateClientInputHardware(ateamspeakcontroller$argument[2].getAsBoolean());
            }
            else if (ateamspeakcontroller$argument[2].getKey().equalsIgnoreCase("client_output_hardware")) {
                teamspeakuser6.updateClientOutputHardware(ateamspeakcontroller$argument[2].getAsBoolean());
            }
            else if (ateamspeakcontroller$argument[2].getKey().equalsIgnoreCase("client_away")) {
                if (ateamspeakcontroller$argument.length == 4) {
                    teamspeakuser6.updateAway(ateamspeakcontroller$argument[2].getAsBoolean(), ateamspeakcontroller$argument[3].getValue());
                }
                else {
                    teamspeakuser6.updateAway(ateamspeakcontroller$argument[2].getAsBoolean());
                }
            }
            this.updateInformation(EnumUpdateType.CLIENTS);
        }
        else if (!s.equalsIgnoreCase("channeldeleted") && !s.equalsIgnoreCase("channelcreated") && !s.equalsIgnoreCase("channeledited")) {
            if (s.equalsIgnoreCase("connectstatuschange")) {
                if (ateamspeakcontroller$argument[1].getValue().equalsIgnoreCase("disconnected")) {
                    for (final ControlListener controllistener6 : this.listeners) {
                        this.reset();
                        controllistener6.onDisconnect();
                    }
                }
                else if (ateamspeakcontroller$argument[1].getValue().equalsIgnoreCase("connection_established")) {
                    this.updateInformation(EnumUpdateType.ALL);
                }
            }
            else if (s.equalsIgnoreCase("clientpoke")) {
                final TeamSpeakUser teamspeakuser7 = this.getUser(ateamspeakcontroller$argument[1].getAsInt());
                final String s2 = TeamSpeak.fix(ateamspeakcontroller$argument[4].getValue());
                for (final ControlListener controllistener7 : this.listeners) {
                    controllistener7.onPokeRecieved(teamspeakuser7, s2);
                }
            }
            else if (s.equalsIgnoreCase("textmessage")) {
                final int k = ateamspeakcontroller$argument[1].getAsInt();
                if (k == 1) {
                    final TeamSpeakUser teamspeakuser8 = this.getUser(ateamspeakcontroller$argument[3].getAsInt());
                    final TeamSpeakUser teamspeakuser9 = this.getUser(ateamspeakcontroller$argument[4].getAsInt());
                    if (teamspeakuser9 == null) {
                        return;
                    }
                    for (final ControlListener controllistener8 : this.listeners) {
                        if (teamspeakuser9.isTyping()) {
                            teamspeakuser9.updateTyping(false);
                        }
                        controllistener8.onMessageRecieved(teamspeakuser8, teamspeakuser9, TeamSpeak.fix(ateamspeakcontroller$argument[2].getValue()));
                    }
                }
                if (k == 2) {
                    final TeamSpeakUser teamspeakuser10 = this.getUser(ateamspeakcontroller$argument[3].getAsInt());
                    if (teamspeakuser10 == null) {
                        return;
                    }
                    for (final ControlListener controllistener9 : this.listeners) {
                        if (teamspeakuser10.isTyping()) {
                            teamspeakuser10.updateTyping(false);
                        }
                        controllistener9.onChannelMessageRecieved(teamspeakuser10, TeamSpeak.fix(ateamspeakcontroller$argument[2].getValue()));
                    }
                }
                if (k == 3) {
                    final TeamSpeakUser teamspeakuser11 = this.getUser(ateamspeakcontroller$argument[3].getAsInt());
                    if (teamspeakuser11 == null) {
                        return;
                    }
                    for (final ControlListener controllistener10 : this.listeners) {
                        if (teamspeakuser11.isTyping()) {
                            teamspeakuser11.updateTyping(false);
                        }
                        controllistener10.onServerMessageRecieved(teamspeakuser11, TeamSpeak.fix(ateamspeakcontroller$argument[2].getValue()));
                    }
                }
            }
            else if (s.equalsIgnoreCase("clientchatcomposing")) {
                final TeamSpeakUser teamspeakuser12 = this.getUser(ateamspeakcontroller$argument[1].getAsInt());
                if (teamspeakuser12 == null) {
                    return;
                }
                for (final ControlListener controllistener11 : this.listeners) {
                    controllistener11.onClientStartTyping(teamspeakuser12);
                    teamspeakuser12.updateTyping(true);
                }
            }
            else if (s.equalsIgnoreCase("clientmoved")) {
                final int l = ateamspeakcontroller$argument[1].getAsInt();
                final TeamSpeakUser teamspeakuser13 = this.getUser(ateamspeakcontroller$argument[2].getAsInt());
                if (teamspeakuser13 != null) {
                    TeamSpeak.updateScroll(teamspeakuser13.getChannelId(), false);
                }
                this.updateInformation(EnumUpdateType.CLIENTS);
                TeamSpeak.updateScroll(l, true);
            }
            else if (s.equalsIgnoreCase("currentserverconnectionchanged")) {
                this.updateInformation(EnumUpdateType.ALL);
            }
            else if (s.equalsIgnoreCase("servergrouplist")) {
                TeamSpeakServerGroup.getGroups().clear();
                final String[] astring2 = message.replace("servergrouplist", "").replace(" schandlerid=1 ", "").replace(" schandlerid=0 ", "").split("[|]");
                String[] array;
                for (int length = (array = astring2).length, n = 0; n < length; ++n) {
                    final String s3 = array[n];
                    final String[] astring3 = s3.split(" ");
                    ateamspeakcontroller$argument = new Argument[astring3.length];
                    for (int j = 0; j < ateamspeakcontroller$argument.length; ++j) {
                        ateamspeakcontroller$argument[j] = new Argument(astring3[j]);
                    }
                    final TeamSpeakServerGroup teamspeakservergroup = new TeamSpeakServerGroup(ateamspeakcontroller$argument[0].getAsInt());
                    teamspeakservergroup.setGroupName(ateamspeakcontroller$argument[1].getValue());
                    teamspeakservergroup.setType(ateamspeakcontroller$argument[2].getAsInt());
                    teamspeakservergroup.setIconId(ateamspeakcontroller$argument[3].getAsInt());
                    teamspeakservergroup.setSavebd(ateamspeakcontroller$argument[4].getAsInt());
                    TeamSpeakServerGroup.addGroup(teamspeakservergroup);
                }
            }
            else if (s.equalsIgnoreCase("channelgrouplist")) {
                TeamSpeakChannelGroup.getGroups().clear();
                final String[] astring4 = message.replace("channelgrouplist", "").replace(" schandlerid=1 ", "").replace(" schandlerid=0 ", "").split("[|]");
                String[] array2;
                for (int length2 = (array2 = astring4).length, n2 = 0; n2 < length2; ++n2) {
                    final String s4 = array2[n2];
                    final String[] astring5 = s4.split(" ");
                    ateamspeakcontroller$argument = new Argument[astring5.length];
                    for (int i2 = 0; i2 < ateamspeakcontroller$argument.length; ++i2) {
                        ateamspeakcontroller$argument[i2] = new Argument(astring5[i2]);
                    }
                    final TeamSpeakChannelGroup teamspeakchannelgroup = new TeamSpeakChannelGroup(ateamspeakcontroller$argument[0].getAsInt());
                    teamspeakchannelgroup.setGroupName(ateamspeakcontroller$argument[1].getValue());
                    teamspeakchannelgroup.setType(ateamspeakcontroller$argument[2].getAsInt());
                    teamspeakchannelgroup.setIconId(ateamspeakcontroller$argument[3].getAsInt());
                    teamspeakchannelgroup.setSavebd(ateamspeakcontroller$argument[4].getAsInt());
                    teamspeakchannelgroup.setNamemode(ateamspeakcontroller$argument[5].getAsInt());
                    teamspeakchannelgroup.setNameModifyPower(ateamspeakcontroller$argument[6].getAsInt());
                    teamspeakchannelgroup.setNameMemberAddPower(ateamspeakcontroller$argument[7].getAsInt());
                    teamspeakchannelgroup.setNameMemberRemovePower(ateamspeakcontroller$argument[8].getAsInt());
                    TeamSpeakChannelGroup.addGroup(teamspeakchannelgroup);
                }
            }
            else {
                this.updateInformation(EnumUpdateType.ALL);
            }
        }
        else {
            this.updateInformation(EnumUpdateType.CHANNELS);
        }
        if (TeamSpeakChannel.getChannels().size() == 0 || TeamSpeakUser.getUsers().size() == 0) {
            this.updateInformation(EnumUpdateType.ALL);
        }
        if ((TeamSpeakServerGroup.getGroups().isEmpty() || TeamSpeakChannelGroup.getGroups().isEmpty()) && !s.contains("grouplist")) {
            this.updateInformation(EnumUpdateType.GROUPS);
        }
    }
    
    private void updateInformation(final EnumUpdateType type) {
        if (type == EnumUpdateType.ALL || type == EnumUpdateType.CHANNELS) {
            this.sendMessage("channellist -topic -flags -voice -icon -limits");
        }
        if (type == EnumUpdateType.ALL || type == EnumUpdateType.CLIENTS) {
            this.sendMessage("clientlist -uid -away -voice -groups -icon -country");
        }
        if (type == EnumUpdateType.ALL || type == EnumUpdateType.ME) {
            this.sendMessage("whoami");
        }
        if (type == EnumUpdateType.ALL || type == EnumUpdateType.GROUPS) {
            this.sendMessage("servergrouplist");
            this.sendMessage("channelgrouplist");
        }
        if (this.serverIP.isEmpty() || this.serverPort == 0 || !this.isConnectionEstablished()) {
            this.sendMessage("serverconnectinfo");
        }
        int i = 0;
        for (final Chat chat : TeamSpeak.chats) {
            if (chat.getSlotId() < 0) {
                ++i;
            }
        }
        if (i != 2) {
            TeamSpeak.setupChat();
        }
    }
    
    public void tick() {
        this.testForConnectionEstablished();
        if (this.isConnectionEstablished() && this.serverIP.isEmpty() && this.serverPort == 0) {
            this.updateInformation(EnumUpdateType.ALL);
        }
    }
    
    public TeamSpeakUser getUser(final int clientId) {
        final List<TeamSpeakUser> list = TeamSpeakUser.getUsers();
        Collections.synchronizedList(list);
        for (final TeamSpeakUser teamspeakuser : list) {
            if (teamspeakuser.getClientId() == clientId) {
                return teamspeakuser;
            }
        }
        return null;
    }
    
    public TeamSpeakUser getUser(final String clientName) {
        for (final TeamSpeakUser teamspeakuser : TeamSpeakUser.getUsers()) {
            if (teamspeakuser.getNickName() == clientName) {
                return teamspeakuser;
            }
        }
        return null;
    }
    
    public TeamSpeakChannel getChannel(final int channelId) {
        for (final TeamSpeakChannel teamspeakchannel : TeamSpeakChannel.getChannels()) {
            if (teamspeakchannel.getChannelId() == channelId) {
                return teamspeakchannel;
            }
        }
        return null;
    }
    
    public TeamSpeakServerGroup getServerGroup(final int id) {
        final List<TeamSpeakServerGroup> list = new ArrayList<TeamSpeakServerGroup>();
        list.addAll(TeamSpeakServerGroup.getGroups());
        for (final TeamSpeakServerGroup teamspeakservergroup : list) {
            if (teamspeakservergroup.getSgid() == id) {
                return teamspeakservergroup;
            }
        }
        return null;
    }
    
    public TeamSpeakChannelGroup getChannelGroup(final int id) {
        final List<TeamSpeakChannelGroup> list = new ArrayList<TeamSpeakChannelGroup>();
        list.addAll(TeamSpeakChannelGroup.getGroups());
        for (final TeamSpeakChannelGroup teamspeakchannelgroup : list) {
            if (teamspeakchannelgroup != null && teamspeakchannelgroup.getSgid() == id) {
                return teamspeakchannelgroup;
            }
        }
        return null;
    }
    
    public void diconnectUser(final TeamSpeakUser user) {
        TeamSpeakUser.unregisterUser(user);
    }
    
    public TeamSpeakUser me() {
        return this.me;
    }
    
    private void reset() {
        TeamSpeak.chats.clear();
        TeamSpeakUser.reset();
        TeamSpeakChannel.reset();
        this.serverIP = "";
        this.serverPort = 0;
    }
    
    public void connect() {
        this.listeners.clear();
        this.reset();
        TeamSpeak.init();
    }
    
    public boolean isConnectionEstablished() {
        if (!this.tested) {
            this.tested = true;
            this.testForConnectionEstablished();
        }
        return this.connectionEstablished;
    }
    
    public void testForConnectionEstablished() {
        try {
            this.writer.write("whoami\n");
            this.writer.flush();
            this.connectionEstablished = true;
        }
        catch (final Exception var2) {
            this.connectionEstablished = false;
        }
    }
    
    public void quit() {
        this.sendMessage("quit");
    }
    
    public static class Argument
    {
        private String key;
        private String value;
        
        public Argument(final String input) {
            if (input.contains("=") && input.split("=").length >= 1) {
                final String[] astring = input.split("=", 2);
                this.key = astring[0];
                this.value = astring[1];
            }
            else {
                this.key = input;
                this.value = "";
            }
        }
        
        public String getKey() {
            return this.key;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public boolean isInt() {
            try {
                Integer.parseInt(this.value);
                return true;
            }
            catch (final Exception var2) {
                return false;
            }
        }
        
        public int getAsInt() {
            return Integer.parseInt(this.value);
        }
        
        public boolean getAsBoolean() {
            final int i = Integer.parseInt(this.value);
            return i == 1;
        }
        
        public ArrayList<Integer> getAsIntArray() {
            final ArrayList<Integer> arraylist = new ArrayList<Integer>();
            if (this.value.contains(",")) {
                final String[] astring = this.value.split(",");
                String[] array;
                for (int length = (array = astring).length, i = 0; i < length; ++i) {
                    final String s = array[i];
                    arraylist.add(Integer.parseInt(s));
                }
            }
            else {
                arraylist.add(Integer.parseInt(this.value));
            }
            return arraylist;
        }
    }
    
    private class InputStreamReaderThread extends Thread
    {
        private InputStream input;
        
        public InputStreamReaderThread(final InputStream input) {
            super("InputStreamReadThread");
            this.input = input;
            this.start();
        }
        
        @Override
        public void run() {
            TeamSpeakController.this.testForConnectionEstablished();
            final InputStreamReader inputstreamreader = new InputStreamReader(new BufferedInputStream(this.getInput()), Charset.forName("utf-8"));
            final BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            while (TeamSpeakController.this.isConnectionEstablished()) {
                try {
                    final String s = bufferedreader.readLine();
                    if (s == null) {
                        continue;
                    }
                    TeamSpeakController.this.onMessageRecieved(s);
                }
                catch (final IOException ioexception) {
                    ioexception.printStackTrace();
                }
            }
        }
        
        public InputStream getInput() {
            return this.input;
        }
    }
    
    protected class OutputStreamWriterThread extends Thread
    {
        private OutputStream output;
        
        public OutputStreamWriterThread(final OutputStream output) {
            super("OutputStreamWriteThread");
            this.output = output;
            this.start();
        }
        
        @Override
        public void run() {
            TeamSpeakController.this.testForConnectionEstablished();
            final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("utf-8")));
            while (TeamSpeakController.this.isConnectionEstablished()) {
                try {
                    final String s = bufferedreader.readLine();
                    if (s == null) {
                        continue;
                    }
                    if (s.equalsIgnoreCase("usage")) {
                        TeamSpeak.print("Free: " + Runtime.getRuntime().freeMemory() / 1024L / 1024L);
                        TeamSpeak.print("Max: " + Runtime.getRuntime().maxMemory() / 1024L / 1024L);
                        TeamSpeak.print("Total: " + Runtime.getRuntime().totalMemory() / 1024L / 1024L);
                    }
                    else {
                        TeamSpeakController.this.sendMessage(s);
                    }
                }
                catch (final IOException ioexception) {
                    ioexception.printStackTrace();
                }
            }
        }
        
        public OutputStream getOutput() {
            return this.output;
        }
    }
}
