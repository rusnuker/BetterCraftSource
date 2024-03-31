// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import java.util.Hashtable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.event.HoverEvent;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.entity.Entity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import wdl.api.IWDLMessageType;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class WDLMessages
{
    private static Logger logger;
    public static boolean enableAllMessages;
    private static List<MessageRegistration> registrations;
    
    static {
        WDLMessages.logger = LogManager.getLogger();
        WDLMessages.enableAllMessages = false;
        WDLMessages.registrations = new ArrayList<MessageRegistration>();
    }
    
    private static MessageRegistration getRegistration(final String name) {
        for (final MessageRegistration r : WDLMessages.registrations) {
            if (r.name.equals(name)) {
                return r;
            }
        }
        return null;
    }
    
    private static MessageRegistration getRegistration(final IWDLMessageType type) {
        for (final MessageRegistration r : WDLMessages.registrations) {
            if (r.type.equals(type)) {
                return r;
            }
        }
        return null;
    }
    
    public static void registerMessage(final String name, final IWDLMessageType type, final MessageTypeCategory category) {
        WDLMessages.registrations.add(new MessageRegistration(name, type, category));
        WDL.defaultProps.setProperty("Messages." + name, Boolean.toString(type.isEnabledByDefault()));
        WDL.defaultProps.setProperty("MessageGroup." + category.internalName, "true");
    }
    
    public static boolean isEnabled(final IWDLMessageType type) {
        if (type == null) {
            return false;
        }
        if (!WDLMessages.enableAllMessages) {
            return false;
        }
        final MessageRegistration r = getRegistration(type);
        if (r == null) {
            return false;
        }
        if (!isGroupEnabled(r.category)) {
            return false;
        }
        if (!WDL.baseProps.containsKey("Messages." + r.name)) {
            if (WDL.baseProps.containsKey("Debug." + r.name)) {
                ((Hashtable<String, Object>)WDL.baseProps).put("Messages." + r.name, ((Hashtable<K, Object>)WDL.baseProps).remove("Debug." + r.name));
            }
            else {
                WDL.baseProps.setProperty("Messages." + r.name, Boolean.toString(r.type.isEnabledByDefault()));
            }
        }
        return WDL.baseProps.getProperty("Messages." + r.name).equals("true");
    }
    
    public static void toggleEnabled(final IWDLMessageType type) {
        final MessageRegistration r = getRegistration(type);
        if (r != null) {
            WDL.baseProps.setProperty("Messages." + r.name, Boolean.toString(!isEnabled(type)));
        }
    }
    
    public static boolean isGroupEnabled(final MessageTypeCategory group) {
        return WDLMessages.enableAllMessages && WDL.baseProps.getProperty("MessageGroup." + group.internalName, "true").equals("true");
    }
    
    public static void toggleGroupEnabled(final MessageTypeCategory group) {
        WDL.baseProps.setProperty("MessageGroup." + group.internalName, Boolean.toString(!isGroupEnabled(group)));
    }
    
    public static ListMultimap<MessageTypeCategory, IWDLMessageType> getTypes() {
        final ListMultimap<MessageTypeCategory, IWDLMessageType> returned = (ListMultimap<MessageTypeCategory, IWDLMessageType>)LinkedListMultimap.create();
        for (final MessageRegistration r : WDLMessages.registrations) {
            returned.put(r.category, r.type);
        }
        return (ListMultimap<MessageTypeCategory, IWDLMessageType>)ImmutableListMultimap.copyOf((Multimap<?, ?>)returned);
    }
    
    public static void resetEnabledToDefaults() {
        WDL.baseProps.setProperty("Messages.enableAll", "true");
        WDLMessages.enableAllMessages = WDL.globalProps.getProperty("Messages.enableAll", "true").equals("true");
        for (final MessageRegistration r : WDLMessages.registrations) {
            WDL.baseProps.setProperty("MessageGroup." + r.category.internalName, WDL.globalProps.getProperty("MessageGroup." + r.category.internalName, "true"));
            WDL.baseProps.setProperty("Messages." + r.name, WDL.globalProps.getProperty("Messages." + r.name));
        }
    }
    
    public static void onNewServer() {
        if (!WDL.baseProps.containsKey("Messages.enableAll")) {
            if (WDL.baseProps.containsKey("Debug.globalDebugEnabled")) {
                ((Hashtable<String, Object>)WDL.baseProps).put("Messages.enableAll", ((Hashtable<K, Object>)WDL.baseProps).remove("Debug.globalDebugEnabled"));
            }
            else {
                WDL.baseProps.setProperty("Messages.enableAll", WDL.globalProps.getProperty("Messages.enableAll", "true"));
            }
        }
        WDLMessages.enableAllMessages = WDL.baseProps.getProperty("Messages.enableAll").equals("true");
    }
    
    public static void chatMessage(final IWDLMessageType type, final String message) {
        chatMessage(type, new ChatComponentText(message));
    }
    
    public static void chatMessageTranslated(final IWDLMessageType type, final String translationKey, final Object... args) {
        final List<Throwable> exceptionsToPrint = new ArrayList<Throwable>();
        for (int i = 0; i < args.length; ++i) {
            if (args[i] instanceof Entity) {
                final Entity e = (Entity)args[i];
                final String entityType = EntityUtils.getEntityType(e);
                HoverEvent event = null;
                String customName = null;
                try {
                    event = e.getDisplayName().getChatStyle().getChatHoverEvent();
                    if (e.hasCustomName()) {
                        customName = e.getCustomNameTag();
                    }
                }
                catch (final Exception ex) {}
                IChatComponent component;
                if (customName != null) {
                    component = new ChatComponentTranslation("wdl.messages.entityTypeAndCustomName", new Object[] { entityType, customName });
                }
                else {
                    component = new ChatComponentText(entityType);
                }
                component.setChatStyle(component.getChatStyle().setChatHoverEvent(event));
                args[i] = component;
            }
            else if (args[i] instanceof Throwable) {
                final Throwable t = (Throwable)args[i];
                final IChatComponent component2 = new ChatComponentText(t.toString());
                final StringWriter sw = new StringWriter();
                t.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                exceptionAsString = exceptionAsString.replace("\r", "").replace("\t", "    ");
                final HoverEvent event2 = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(exceptionAsString));
                component2.setChatStyle(component2.getChatStyle().setChatHoverEvent(event2));
                WDLMessages.logger.warn(t);
                args[i] = component2;
                exceptionsToPrint.add(t);
            }
        }
        chatMessage(type, new ChatComponentTranslation(translationKey, args));
        for (int i = 0; i < exceptionsToPrint.size(); ++i) {
            WDLMessages.logger.warn("Exception #" + (i + 1) + ": ", exceptionsToPrint.get(i));
        }
    }
    
    public static void chatMessage(final IWDLMessageType type, final IChatComponent message) {
        final String tooltipText = I18n.format("wdl.messages.tooltip", type.getDisplayName()).replace("\r", "");
        final IChatComponent tooltip = new ChatComponentText(tooltipText);
        final IChatComponent text = new ChatComponentText("");
        final IChatComponent header = new ChatComponentText("[WorldDL]");
        header.getChatStyle().setColor(type.getTitleColor());
        header.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip));
        final ChatComponentText messageFormat = new ChatComponentText(" ");
        messageFormat.getChatStyle().setColor(type.getTextColor());
        messageFormat.appendSibling(message);
        text.appendSibling(header);
        text.appendSibling(messageFormat);
        if (isEnabled(type)) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(text);
        }
        else {
            WDLMessages.logger.info(text.getUnformattedText());
        }
    }
    
    private static class MessageRegistration
    {
        public final String name;
        public final IWDLMessageType type;
        public final MessageTypeCategory category;
        
        public MessageRegistration(final String name, final IWDLMessageType type, final MessageTypeCategory category) {
            this.name = name;
            this.type = type;
            this.category = category;
        }
        
        @Override
        public String toString() {
            return "MessageRegistration [name=" + this.name + ", type=" + this.type + ", category=" + this.category + "]";
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
            result = 31 * result + ((this.category == null) ? 0 : this.category.hashCode());
            result = 31 * result + ((this.type == null) ? 0 : this.type.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof MessageRegistration)) {
                return false;
            }
            final MessageRegistration other = (MessageRegistration)obj;
            if (this.name == null) {
                if (other.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(other.name)) {
                return false;
            }
            if (this.category == null) {
                if (other.category != null) {
                    return false;
                }
            }
            else if (!this.category.equals(other.category)) {
                return false;
            }
            if (this.type == null) {
                if (other.type != null) {
                    return false;
                }
            }
            else if (!this.type.equals(other.type)) {
                return false;
            }
            return true;
        }
    }
}
