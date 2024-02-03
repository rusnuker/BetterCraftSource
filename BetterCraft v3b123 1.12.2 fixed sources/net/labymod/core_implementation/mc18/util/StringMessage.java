// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.util;

import java.util.regex.Matcher;
import net.minecraft.event.ClickEvent;
import java.util.Locale;
import java.util.ArrayList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.util.EnumChatFormatting;
import java.util.Map;

public class StringMessage
{
    private static final Map<Character, EnumChatFormatting> formatMap;
    private static final Pattern INCREMENTAL_PATTERN;
    private final List<IChatComponent> list;
    private ChatComponentText currentChatComponent;
    private ChatStyle modifier;
    private final IChatComponent[] output;
    private int currentIndex;
    private final String message;
    
    static {
        INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf('§') + "[0-9a-fk-or])|(\\n)|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf('§') + " \\n]|$))))", 2);
        final ImmutableMap.Builder<Character, EnumChatFormatting> builder = ImmutableMap.builder();
        EnumChatFormatting[] values;
        for (int length = (values = EnumChatFormatting.values()).length, i = 0; i < length; ++i) {
            final EnumChatFormatting format = values[i];
            builder.put(Character.toLowerCase(format.toString().charAt(1)), format);
        }
        formatMap = builder.build();
    }
    
    public StringMessage(final String message, final boolean keepNewlines) {
        this.list = new ArrayList<IChatComponent>();
        this.currentChatComponent = new ChatComponentText("");
        this.modifier = new ChatStyle();
        this.message = message;
        if (message == null) {
            this.output = new IChatComponent[] { this.currentChatComponent };
            return;
        }
        this.list.add(this.currentChatComponent);
        final Matcher matcher = StringMessage.INCREMENTAL_PATTERN.matcher(message);
        String match = null;
        while (matcher.find()) {
            int groupId = 0;
            while ((match = matcher.group(++groupId)) == null) {}
            this.appendNewComponent(matcher.start(groupId));
            Label_0458: {
                switch (groupId) {
                    case 1: {
                        final EnumChatFormatting format = StringMessage.formatMap.get(match.toLowerCase(Locale.ENGLISH).charAt(1));
                        if (format == EnumChatFormatting.RESET) {
                            this.modifier = new ChatStyle();
                            break;
                        }
                        if (!format.isFancyStyling()) {
                            this.modifier = new ChatStyle().setColor(format);
                            break;
                        }
                        switch (format) {
                            case BOLD: {
                                this.modifier.setBold(Boolean.TRUE);
                                break Label_0458;
                            }
                            case ITALIC: {
                                this.modifier.setItalic(Boolean.TRUE);
                                break Label_0458;
                            }
                            case STRIKETHROUGH: {
                                this.modifier.setStrikethrough(Boolean.TRUE);
                                break Label_0458;
                            }
                            case UNDERLINE: {
                                this.modifier.setUnderlined(Boolean.TRUE);
                                break Label_0458;
                            }
                            case OBFUSCATED: {
                                this.modifier.setObfuscated(Boolean.TRUE);
                                break Label_0458;
                            }
                            default: {
                                throw new AssertionError((Object)"Unexpected message format");
                            }
                        }
                        break;
                    }
                    case 2: {
                        if (keepNewlines) {
                            this.currentChatComponent.appendSibling(new ChatComponentText("\n"));
                            break;
                        }
                        this.currentChatComponent = null;
                        break;
                    }
                    case 3: {
                        if (!match.startsWith("http://") && !match.startsWith("https://")) {
                            match = "http://" + match;
                        }
                        this.modifier.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                        this.appendNewComponent(matcher.end(groupId));
                        this.modifier.setChatClickEvent(null);
                        break;
                    }
                }
            }
            this.currentIndex = matcher.end(groupId);
        }
        if (this.currentIndex < message.length()) {
            this.appendNewComponent(message.length());
        }
        this.output = this.list.toArray(new IChatComponent[this.list.size()]);
    }
    
    private void appendNewComponent(final int index) {
        if (index <= this.currentIndex) {
            return;
        }
        final IChatComponent addition = new ChatComponentText(this.message.substring(this.currentIndex, index)).setChatStyle(this.modifier);
        this.currentIndex = index;
        this.modifier = this.modifier.createShallowCopy();
        if (this.currentChatComponent == null) {
            this.currentChatComponent = new ChatComponentText("");
            this.list.add(this.currentChatComponent);
        }
        this.currentChatComponent.appendSibling(addition);
    }
    
    public IChatComponent[] getOutput() {
        return this.output;
    }
}
