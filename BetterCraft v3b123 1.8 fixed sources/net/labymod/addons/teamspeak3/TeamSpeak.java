// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import net.labymod.utils.ModUtils;
import java.util.Iterator;
import net.labymod.utils.ModColor;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import net.labymod.utils.DrawUtils;

public class TeamSpeak
{
    private static DrawUtils draw;
    private static final Logger logger;
    public static String chatPrefix;
    public static TeamSpeakOverlayWindow overlayWindows;
    public static String inputString;
    public static ArrayList<Chat> chats;
    public static int selectedChat;
    public static int selectedChannel;
    public static int selectedUser;
    public static boolean defaultScreen;
    public static int ySplit;
    public static int xSplit;
    public static int scrollChat;
    public static int scrollChannel;
    public static boolean callBack;
    public static int callBackClient;
    public static boolean teamSpeakGroupPrefix;
    public static ArrayList<Integer> outOfView;
    
    static {
        TeamSpeak.draw = new DrawUtils();
        logger = LogManager.getLogger("TeamSpeak");
        TeamSpeak.chatPrefix = String.valueOf(ModColor.cl("8")) + "[" + ModColor.cl("5") + ModColor.cl("l") + "TeamSpeak" + ModColor.cl("8") + "] " + ModColor.cl("7");
        TeamSpeak.inputString = "";
        TeamSpeak.chats = new ArrayList<Chat>();
        TeamSpeak.selectedChat = -1;
        TeamSpeak.selectedChannel = -1;
        TeamSpeak.selectedUser = -1;
        TeamSpeak.defaultScreen = false;
        TeamSpeak.scrollChat = 0;
        TeamSpeak.scrollChannel = 0;
        TeamSpeak.callBack = false;
        TeamSpeak.callBackClient = 0;
        TeamSpeak.teamSpeakGroupPrefix = false;
        TeamSpeak.outOfView = new ArrayList<Integer>();
    }
    
    public static void init() {
        setupChat();
        new TeamSpeakController(new TeamSpeakListener());
        TeamSpeak.overlayWindows = new TeamSpeakOverlayWindow();
    }
    
    public static void setupChat() {
        TeamSpeak.chats.clear();
        TeamSpeak.chats.add(new Chat(-2, EnumTargetMode.SERVER));
        TeamSpeak.chats.add(new Chat(-1, EnumTargetMode.CHANNEL));
    }
    
    public static void addChat(final TeamSpeakUser target, final TeamSpeakUser sender, String message, final EnumTargetMode mode) {
        if (message != null) {
            message = fix(message);
            message = colors(message);
            message = url(message);
        }
        if (mode == EnumTargetMode.USER) {
            TeamSpeakUser teamspeakuser;
            if ((teamspeakuser = sender) == null) {
                teamspeakuser = target;
            }
            else if (sender.getClientId() == TeamSpeakController.getInstance().me().getClientId()) {
                teamspeakuser = target;
            }
            if (teamspeakuser == null) {
                error("User is offline");
                return;
            }
            Chat chat = null;
            for (final Chat chat2 : TeamSpeak.chats) {
                if (chat2.getSlotId() == teamspeakuser.getClientId()) {
                    chat = chat2;
                    break;
                }
            }
            if (sender == null) {
                if (chat != null) {
                    chat.addMessage(null, message);
                }
            }
            else if (chat == null) {
                if (message == null) {
                    TeamSpeak.chats.add(new Chat(teamspeakuser, sender, mode));
                }
                else {
                    TeamSpeak.chats.add(new Chat(teamspeakuser, sender, mode, message));
                }
            }
            else if (message == null) {
                TeamSpeak.selectedChat = teamspeakuser.getClientId();
            }
            else {
                chat.addMessage(sender, message);
            }
        }
        if (mode == EnumTargetMode.CHANNEL) {
            Chat chat3 = null;
            for (final Chat chat4 : TeamSpeak.chats) {
                if (chat4.getSlotId() == -1) {
                    chat3 = chat4;
                    break;
                }
            }
            if (chat3 != null) {
                chat3.addMessage(sender, message);
            }
        }
        if (mode == EnumTargetMode.SERVER) {
            Chat chat5 = null;
            for (final Chat chat6 : TeamSpeak.chats) {
                if (chat6.getSlotId() == -2) {
                    chat5 = chat6;
                    break;
                }
            }
            if (chat5 != null) {
                chat5.addMessage(sender, message);
            }
        }
    }
    
    public static String replaceColor(final String message, final String name, final String code) {
        return message.replaceAll("(?i)Color=" + name, ModColor.cl(code)).replace("[" + ModColor.cl(code) + "]", ModColor.cl(code));
    }
    
    public static String replaceHtml(final String message, final String tag, final String toPrefix, final String toSuffix) {
        return message.replaceAll("(?i)" + tag, toPrefix).replace("[" + toPrefix + "]", toPrefix).replace("[/" + toPrefix + "]", toSuffix);
    }
    
    public static String colors(String message) {
        message = replaceColor(message, "RED", "c");
        message = replaceColor(message, "BLUE", "9");
        message = replaceColor(message, "GREEN", "2");
        message = replaceColor(message, "YELLOW", "e");
        message = replaceColor(message, "GOLD", "6");
        message = replaceColor(message, "AQUA", "3");
        message = replaceColor(message, "WHITE", "f");
        message = replaceColor(message, "BLACK", "0");
        message = message.replaceAll("(?i)/Color", ModColor.cl("7")).replace("[" + ModColor.cl("7") + "]", ModColor.cl("7"));
        return message;
    }
    
    public static String url(String message) {
        message = replaceHtml(message, "URL", String.valueOf(ModColor.cl("9")) + ModColor.cl("n"), ModColor.cl("7"));
        return message;
    }
    
    public static String toUrl(String message) {
        final ArrayList<String> arraylist = ModUtils.extractDomains(message);
        if (!arraylist.isEmpty()) {
            for (final String s : arraylist) {
                message = message.replace(s, "[URL]" + s + "[/URL]");
            }
        }
        return message;
    }
    
    public static String fix(String message) {
        message = message.replace("\\/", "/");
        message = message.replace("\\p", "|");
        message = message.replace("\\s", " ");
        message = message.replace("\\\\", "\\");
        message = message.replace("\\n", " ");
        return message;
    }
    
    public static String unFix(String message) {
        message = message.replace("\\", "\\\\");
        message = message.replace("/", "\\/");
        message = message.replace("|", "\\p");
        message = message.replace(" ", "\\s");
        return message;
    }
    
    public static String toStarSpacer(final String channelName, final int xSplit) {
        final String s = channelName.split("]", 2)[1];
        String s2 = "";
        final int i = xSplit / 10;
        for (int j = 0; j <= i; ++j) {
            s2 = String.valueOf(s2) + s;
        }
        if (s2.length() > i) {
            s2 = s2.substring(0, i);
        }
        return s2;
    }
    
    public static String toCenterSpacer(final String channelName) {
        return channelName.split("]", 2)[1];
    }
    
    public static boolean isSpacer(final String channelName) {
        return channelName.toLowerCase().startsWith("[spacer") && channelName.contains("]");
    }
    
    public static boolean isStarSpacer(final String channelName) {
        return channelName.toLowerCase().startsWith("[*spacer") && channelName.contains("]");
    }
    
    public static boolean isCenterSpacer(final String channelName) {
        return channelName.toLowerCase().startsWith("[cspacer") && channelName.contains("]");
    }
    
    public static String country(final String country) {
        return (country == null) ? "Unknown" : (country.equalsIgnoreCase("DE") ? "Germany" : (country.equalsIgnoreCase("AT") ? "Austria" : (country.equalsIgnoreCase("TR") ? "Turkey" : country)));
    }
    
    public static String status(final boolean status, final String on, final String off) {
        return status ? on : off;
    }
    
    public static TeamSpeakChannel getFromOrder(final int channelOrder) {
        for (final TeamSpeakChannel teamspeakchannel : TeamSpeakBridge.getChannels()) {
            if (teamspeakchannel.getChannelOrder() == channelOrder) {
                return teamspeakchannel;
            }
        }
        return null;
    }
    
    public static boolean isChannelNotInView(final int channelId) {
        for (final int i : TeamSpeak.outOfView) {
            if (i == channelId) {
                return true;
            }
        }
        return false;
    }
    
    public static void updateScroll(final int channelId, final boolean extend) {
        if (isChannelNotInView(channelId)) {
            if (extend) {
                TeamSpeak.scrollChannel -= 10;
            }
            else {
                TeamSpeak.scrollChannel += 10;
            }
        }
    }
    
    public static void setDefaultScreen() {
        if (!TeamSpeak.defaultScreen) {
            TeamSpeak.defaultScreen = true;
            TeamSpeak.xSplit = TeamSpeak.draw.getWidth() / 3 * 2;
            TeamSpeak.ySplit = TeamSpeak.draw.getHeight() / 4 * 3;
        }
    }
    
    public static int booleanToInteger(final boolean input) {
        return input ? 1 : 0;
    }
    
    public static void print(final String msg) {
        TeamSpeak.logger.info(msg);
    }
    
    public static void error(final String errorMessage) {
        final String s = String.valueOf(ModColor.cl("c")) + errorMessage;
        if (TeamSpeak.selectedChat == -1) {
            addChat(null, null, s, EnumTargetMode.CHANNEL);
        }
        else if (TeamSpeak.selectedChat == -2) {
            addChat(null, null, s, EnumTargetMode.SERVER);
        }
        else {
            final TeamSpeakUser teamspeakuser = TeamSpeakController.getInstance().getUser(TeamSpeak.selectedChat);
            if (teamspeakuser != null) {
                addChat(teamspeakuser, null, s, EnumTargetMode.USER);
            }
            else {
                addChat(null, null, s, EnumTargetMode.SERVER);
            }
        }
    }
    
    public static void info(final String message) {
        final String s = String.valueOf(ModColor.cl("c")) + message;
        if (TeamSpeak.selectedChat == -1) {
            addChat(null, null, s, EnumTargetMode.CHANNEL);
        }
        else if (TeamSpeak.selectedChat == -2) {
            addChat(null, null, s, EnumTargetMode.SERVER);
        }
        else {
            final TeamSpeakUser teamspeakuser = TeamSpeakController.getInstance().getUser(TeamSpeak.selectedChat);
            if (teamspeakuser != null) {
                addChat(teamspeakuser, null, s, EnumTargetMode.USER);
            }
            else {
                addChat(null, null, s, EnumTargetMode.SERVER);
            }
        }
    }
    
    public static void infoAll(final String message) {
        final String s = String.valueOf(ModColor.cl("c")) + message;
        addChat(null, null, s, EnumTargetMode.CHANNEL);
        addChat(null, null, s, EnumTargetMode.SERVER);
        if (TeamSpeak.selectedChat >= 0) {
            final TeamSpeakUser teamspeakuser = TeamSpeakController.getInstance().getUser(TeamSpeak.selectedChat);
            if (teamspeakuser != null) {
                addChat(teamspeakuser, null, s, EnumTargetMode.USER);
            }
            else {
                addChat(null, null, s, EnumTargetMode.SERVER);
            }
        }
    }
    
    public static String getTalkColor(final TeamSpeakUser user) {
        String s = ModColor.cl("9");
        if (user == null) {
            return s;
        }
        if (user.isChannelCommander()) {
            s = ModColor.cl("6");
        }
        if (user.isTalking()) {
            if (user.isChannelCommander()) {
                s = ModColor.cl("e");
            }
            else {
                s = ModColor.cl("b");
            }
        }
        if (!user.hasClientInputHardware()) {
            s = ModColor.cl("8");
        }
        if (user.hasClientInputMuted()) {
            s = ModColor.cl("7");
        }
        if (!user.hasClientOutputHardware()) {
            s = ModColor.cl("8");
        }
        if (user.hasClientOutputMuted()) {
            s = ModColor.cl("8");
        }
        if (user.isAway()) {
            s = ModColor.cl("7");
        }
        return s;
    }
    
    public static String getAway(final TeamSpeakUser user) {
        String s = "";
        if (user == null) {
            return s;
        }
        if (user.isAway() && !user.getAwayMessage().isEmpty()) {
            s = String.valueOf(ModColor.cl("7")) + " [" + user.getAwayMessage() + "]";
        }
        return s;
    }
}
