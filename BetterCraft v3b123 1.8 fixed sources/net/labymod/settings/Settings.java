// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings;

import java.util.Comparator;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import net.labymod.settings.elements.SettingsElement;
import java.util.List;

public class Settings
{
    private List<SettingsElement> elements;
    
    public Settings() {
        this.elements = new ArrayList<SettingsElement>();
    }
    
    public Settings(final SettingsElement... element) {
        (this.elements = new ArrayList<SettingsElement>()).addAll(Arrays.asList(element));
    }
    
    public List<SettingsElement> getElements() {
        return this.elements;
    }
    
    public Settings add(final SettingsElement settingsElement) {
        this.elements.add(settingsElement);
        return this;
    }
    
    public Settings addAll(final ArrayList<SettingsElement> settingsElements) {
        this.elements.addAll(settingsElements);
        this.sort();
        return this;
    }
    
    private void sort() {
        this.elements.sort(Comparator.comparingInt(SettingsElement::getSortingId));
    }
}
