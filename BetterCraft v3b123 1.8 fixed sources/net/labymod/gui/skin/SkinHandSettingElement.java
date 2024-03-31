// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.skin;

import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.CheckBox;
import net.labymod.utils.Consumer;

public class SkinHandSettingElement extends SkinCustomizationSettingElement
{
    public SkinHandSettingElement(final String displayString, final String iconName) {
        super(displayString, iconName);
        this.initCheckBox();
        this.checkBox.setUpdateListener(new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept(final CheckBox.EnumCheckBoxValue accepted) {
                LabyModCore.getMinecraft().setUseLeftHand(accepted == CheckBox.EnumCheckBoxValue.ENABLED);
            }
        });
    }
    
    @Override
    protected CheckBox.EnumCheckBoxValue loadValue() {
        return LabyModCore.getMinecraft().isUsingLeftHand() ? CheckBox.EnumCheckBoxValue.ENABLED : CheckBox.EnumCheckBoxValue.DISABLED;
    }
}
