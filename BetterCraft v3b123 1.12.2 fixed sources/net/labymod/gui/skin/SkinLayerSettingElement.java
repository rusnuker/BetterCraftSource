// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.skin;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.labymod.gui.elements.CheckBox;
import net.labymod.utils.Consumer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SkinLayerSettingElement extends SkinCustomizationSettingElement
{
    private EnumPlayerModelParts[] modelParts;
    
    public SkinLayerSettingElement(final GuiSkinCustomization skinCustomization, final String displayString, final String iconName, final EnumPlayerModelParts... modelParts) {
        super((modelParts.length == 1) ? I18n.format("options.modelPart." + modelParts[0].getPartName(), new Object[0]) : displayString, iconName);
        this.modelParts = modelParts;
        this.initCheckBox();
        this.checkBox.setUpdateListener(new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept(final CheckBox.EnumCheckBoxValue accepted) {
                EnumPlayerModelParts[] val$modelParts;
                for (int length = (val$modelParts = modelParts).length, i = 0; i < length; ++i) {
                    final EnumPlayerModelParts part = val$modelParts[i];
                    if (accepted != CheckBox.EnumCheckBoxValue.INDETERMINATE) {
                        skinCustomization.updatePart(part, accepted == CheckBox.EnumCheckBoxValue.ENABLED);
                    }
                }
            }
        });
    }
    
    @Override
    protected CheckBox.EnumCheckBoxValue loadValue() {
        final Set<EnumPlayerModelParts> enabledList = Minecraft.getMinecraft().gameSettings.getModelParts();
        if (this.modelParts.length == 1) {
            return enabledList.contains(this.modelParts[0]) ? CheckBox.EnumCheckBoxValue.ENABLED : CheckBox.EnumCheckBoxValue.DISABLED;
        }
        boolean allContains = true;
        boolean allContainsNot = true;
        EnumPlayerModelParts[] modelParts;
        for (int length = (modelParts = this.modelParts).length, i = 0; i < length; ++i) {
            final EnumPlayerModelParts part = modelParts[i];
            if (!enabledList.contains(part)) {
                allContains = false;
            }
            else {
                allContainsNot = false;
            }
        }
        return allContains ? CheckBox.EnumCheckBoxValue.ENABLED : (allContainsNot ? CheckBox.EnumCheckBoxValue.DISABLED : CheckBox.EnumCheckBoxValue.INDETERMINATE);
    }
}
