// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tools.filter;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import net.labymod.utils.ModColor;
import net.labymod.core.ChatComponent;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import java.util.HashMap;
import net.labymod.ingamechat.renderer.ChatLine;
import java.util.Map;

public class FilterChatManager
{
    private static Map<ChatLine, Filters.Filter> filterResults;
    
    static {
        FilterChatManager.filterResults = new HashMap<ChatLine, Filters.Filter>();
    }
    
    public static Map<ChatLine, Filters.Filter> getFilterResults() {
        return FilterChatManager.filterResults;
    }
    
    public static Filters.Filter getFilterComponent(final ChatLine line) {
        if (!LabyMod.getSettings().chatFilter) {
            return null;
        }
        if (FilterChatManager.filterResults.containsKey(line)) {
            return FilterChatManager.filterResults.get(line);
        }
        final Filters.Filter foundComponent = getFilterComponent(LabyModCore.getMinecraft().getChatComponent(line.getComponent()));
        FilterChatManager.filterResults.put(line, foundComponent);
        return foundComponent;
    }
    
    public static Filters.Filter getFilterComponent(final ChatComponent chatComponent) {
        if (!LabyMod.getSettings().chatFilter) {
            return null;
        }
        final String message = ModColor.removeColor(chatComponent.getUnformattedText()).toLowerCase();
        final String messageJson = ModColor.removeColor(chatComponent.getJson()).toLowerCase();
        Filters.Filter foundComponent = null;
        for (final Filters.Filter component : LabyMod.getInstance().getChatToolManager().getFilters()) {
            boolean contains = false;
            String[] wordsContains;
            for (int length = (wordsContains = component.getWordsContains()).length, i = 0; i < length; ++i) {
                final String containsMsg = wordsContains[i];
                if (message.contains(containsMsg.toLowerCase()) || (component.isFilterTooltips() && messageJson.contains(containsMsg.toLowerCase()))) {
                    contains = true;
                    break;
                }
            }
            if (contains) {
                String[] wordsContainsNot;
                for (int length2 = (wordsContainsNot = component.getWordsContainsNot()).length, j = 0; j < length2; ++j) {
                    final String containsNot = wordsContainsNot[j];
                    if (!containsNot.isEmpty()) {
                        if (message.contains(containsNot.toLowerCase())) {
                            contains = false;
                            break;
                        }
                        if (component.isFilterTooltips() && messageJson.contains(containsNot.toLowerCase())) {
                            contains = false;
                            break;
                        }
                    }
                }
            }
            if (!contains) {
                continue;
            }
            if (foundComponent == null) {
                foundComponent = component.clone();
            }
            if (!foundComponent.isDisplayInSecondChat() && component.isDisplayInSecondChat()) {
                foundComponent.setDisplayInSecondChat(true);
            }
            if (!foundComponent.isHideMessage() && component.isHideMessage()) {
                foundComponent.setHideMessage(true);
            }
            if (!foundComponent.isPlaySound() && component.isPlaySound()) {
                foundComponent.setPlaySound(true);
                foundComponent.setSoundPath(component.getSoundPath());
            }
            if (!foundComponent.isHighlightMessage() && component.isHighlightMessage()) {
                foundComponent.setHighlightMessage(true);
                foundComponent.setHighlightColorR(component.getHighlightColorR());
                foundComponent.setHighlightColorG(component.getHighlightColorG());
                foundComponent.setHighlightColorB(component.getHighlightColorB());
            }
            foundComponent.setRoom(component.getRoom());
        }
        return foundComponent;
    }
    
    public static void unloadMessage(final ChatLine line) {
        FilterChatManager.filterResults.remove(line);
    }
    
    public static void removeFilterComponent(final Filters.Filter component) {
        final Set<ChatLine> removeLines = new HashSet<ChatLine>();
        for (final Map.Entry<ChatLine, Filters.Filter> filterResult : FilterChatManager.filterResults.entrySet()) {
            if (filterResult.getValue() == component) {
                removeLines.add(filterResult.getKey());
            }
        }
        for (final ChatLine remove : removeLines) {
            FilterChatManager.filterResults.remove(remove);
        }
    }
}
