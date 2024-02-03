// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tools.filter;

import java.beans.ConstructorProperties;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Filters
{
    private List<Filter> filters;
    
    public Filters() {
        this.filters = new ArrayList<Filter>();
    }
    
    public List<Filter> getFilters() {
        return this.filters;
    }
    
    public static class Filter
    {
        private String filterName;
        private String[] wordsContains;
        private String[] wordsContainsNot;
        private boolean playSound;
        private String soundPath;
        private boolean highlightMessage;
        private short highlightColorR;
        private short highlightColorG;
        private short highlightColorB;
        private boolean hideMessage;
        private boolean displayInSecondChat;
        private boolean filterTooltips;
        private String room;
        
        public Filter(final Filter component) {
            this(component.getFilterName(), component.getWordsContains(), component.getWordsContainsNot(), component.isPlaySound(), component.getSoundPath(), component.isHighlightMessage(), component.getHighlightColorR(), component.getHighlightColorG(), component.getHighlightColorB(), component.isHideMessage(), component.isDisplayInSecondChat(), component.isFilterTooltips(), component.getRoom());
        }
        
        public Filter clone() {
            return new Filter(this.filterName, this.wordsContains, this.wordsContainsNot, this.playSound, this.soundPath, this.highlightMessage, this.highlightColorR, this.highlightColorG, this.highlightColorB, this.hideMessage, this.displayInSecondChat, this.filterTooltips, this.room);
        }
        
        public Color getHighlightColor() {
            return new Color(this.getHighlightColorR(), this.getHighlightColorG(), this.getHighlightColorB());
        }
        
        public String getFilterName() {
            return this.filterName;
        }
        
        public String[] getWordsContains() {
            return this.wordsContains;
        }
        
        public String[] getWordsContainsNot() {
            return this.wordsContainsNot;
        }
        
        public boolean isPlaySound() {
            return this.playSound;
        }
        
        public String getSoundPath() {
            return this.soundPath;
        }
        
        public boolean isHighlightMessage() {
            return this.highlightMessage;
        }
        
        public short getHighlightColorR() {
            return this.highlightColorR;
        }
        
        public short getHighlightColorG() {
            return this.highlightColorG;
        }
        
        public short getHighlightColorB() {
            return this.highlightColorB;
        }
        
        public boolean isHideMessage() {
            return this.hideMessage;
        }
        
        public boolean isDisplayInSecondChat() {
            return this.displayInSecondChat;
        }
        
        public boolean isFilterTooltips() {
            return this.filterTooltips;
        }
        
        public String getRoom() {
            return this.room;
        }
        
        public void setFilterName(final String filterName) {
            this.filterName = filterName;
        }
        
        public void setWordsContains(final String[] wordsContains) {
            this.wordsContains = wordsContains;
        }
        
        public void setWordsContainsNot(final String[] wordsContainsNot) {
            this.wordsContainsNot = wordsContainsNot;
        }
        
        public void setPlaySound(final boolean playSound) {
            this.playSound = playSound;
        }
        
        public void setSoundPath(final String soundPath) {
            this.soundPath = soundPath;
        }
        
        public void setHighlightMessage(final boolean highlightMessage) {
            this.highlightMessage = highlightMessage;
        }
        
        public void setHighlightColorR(final short highlightColorR) {
            this.highlightColorR = highlightColorR;
        }
        
        public void setHighlightColorG(final short highlightColorG) {
            this.highlightColorG = highlightColorG;
        }
        
        public void setHighlightColorB(final short highlightColorB) {
            this.highlightColorB = highlightColorB;
        }
        
        public void setHideMessage(final boolean hideMessage) {
            this.hideMessage = hideMessage;
        }
        
        public void setDisplayInSecondChat(final boolean displayInSecondChat) {
            this.displayInSecondChat = displayInSecondChat;
        }
        
        public void setFilterTooltips(final boolean filterTooltips) {
            this.filterTooltips = filterTooltips;
        }
        
        public void setRoom(final String room) {
            this.room = room;
        }
        
        @ConstructorProperties({ "filterName", "wordsContains", "wordsContainsNot", "playSound", "soundPath", "highlightMessage", "highlightColorR", "highlightColorG", "highlightColorB", "hideMessage", "displayInSecondChat", "filterTooltips", "room" })
        public Filter(final String filterName, final String[] wordsContains, final String[] wordsContainsNot, final boolean playSound, final String soundPath, final boolean highlightMessage, final short highlightColorR, final short highlightColorG, final short highlightColorB, final boolean hideMessage, final boolean displayInSecondChat, final boolean filterTooltips, final String room) {
            this.filterName = filterName;
            this.wordsContains = wordsContains;
            this.wordsContainsNot = wordsContainsNot;
            this.playSound = playSound;
            this.soundPath = soundPath;
            this.highlightMessage = highlightMessage;
            this.highlightColorR = highlightColorR;
            this.highlightColorG = highlightColorG;
            this.highlightColorB = highlightColorB;
            this.hideMessage = hideMessage;
            this.displayInSecondChat = displayInSecondChat;
            this.filterTooltips = filterTooltips;
            this.room = room;
        }
    }
}
