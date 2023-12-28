// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import org.cef.misc.BoolRef;
import org.cef.misc.IntRef;

class CefMenuModel_N extends CefNativeAdapter implements CefMenuModel
{
    public CefMenuModel_N() {
    }
    
    @Override
    public boolean clear() {
        try {
            return this.N_Clear(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public int getCount() {
        try {
            return this.N_GetCount(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public boolean addSeparator() {
        try {
            return this.N_AddSeparator(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean addItem(final int command_id, final String label) {
        try {
            return this.N_AddItem(this.getNativeRef(null), command_id, label);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean addCheckItem(final int command_id, final String label) {
        try {
            return this.N_AddCheckItem(this.getNativeRef(null), command_id, label);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean addRadioItem(final int command_id, final String label, final int group_id) {
        try {
            return this.N_AddRadioItem(this.getNativeRef(null), command_id, label, group_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public CefMenuModel addSubMenu(final int command_id, final String label) {
        try {
            return this.N_AddSubMenu(this.getNativeRef(null), command_id, label);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean insertSeparatorAt(final int index) {
        try {
            return this.N_InsertSeparatorAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean insertItemAt(final int index, final int command_id, final String label) {
        try {
            return this.N_InsertItemAt(this.getNativeRef(null), index, command_id, label);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean insertCheckItemAt(final int index, final int command_id, final String label) {
        try {
            return this.N_InsertCheckItemAt(this.getNativeRef(null), index, command_id, label);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean insertRadioItemAt(final int index, final int command_id, final String label, final int group_id) {
        try {
            return this.N_InsertRadioItemAt(this.getNativeRef(null), index, command_id, label, group_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public CefMenuModel insertSubMenuAt(final int index, final int command_id, final String label) {
        try {
            return this.N_InsertSubMenuAt(this.getNativeRef(null), index, command_id, label);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean remove(final int command_id) {
        try {
            return this.N_Remove(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean removeAt(final int index) {
        try {
            return this.N_RemoveAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public int getIndexOf(final int command_id) {
        try {
            return this.N_GetIndexOf(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public int getCommandIdAt(final int index) {
        try {
            return this.N_GetCommandIdAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public boolean setCommandIdAt(final int index, final int command_id) {
        try {
            return this.N_SetCommandIdAt(this.getNativeRef(null), index, command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public String getLabel(final int command_id) {
        try {
            return this.N_GetLabel(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getLabelAt(final int index) {
        try {
            return this.N_GetLabelAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean setLabel(final int command_id, final String label) {
        try {
            return this.N_SetLabel(this.getNativeRef(null), command_id, label);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setLabelAt(final int index, final String label) {
        try {
            return this.N_SetLabelAt(this.getNativeRef(null), index, label);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public MenuItemType getType(final int command_id) {
        try {
            return this.N_GetType(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public MenuItemType getTypeAt(final int index) {
        try {
            return this.N_GetTypeAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public int getGroupId(final int command_id) {
        try {
            return this.N_GetGroupId(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public int getGroupIdAt(final int index) {
        try {
            return this.N_GetGroupIdAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public boolean setGroupId(final int command_id, final int group_id) {
        try {
            return this.N_SetGroupId(this.getNativeRef(null), command_id, group_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setGroupIdAt(final int index, final int group_id) {
        try {
            return this.N_SetGroupIdAt(this.getNativeRef(null), index, group_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public CefMenuModel getSubMenu(final int command_id) {
        try {
            return this.N_GetSubMenu(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public CefMenuModel getSubMenuAt(final int index) {
        try {
            return this.N_GetSubMenuAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean isVisible(final int command_id) {
        try {
            return this.N_IsVisible(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isVisibleAt(final int index) {
        try {
            return this.N_IsVisibleAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setVisible(final int command_id, final boolean visible) {
        try {
            return this.N_SetVisible(this.getNativeRef(null), command_id, visible);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setVisibleAt(final int index, final boolean visible) {
        try {
            return this.N_SetVisibleAt(this.getNativeRef(null), index, visible);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isEnabled(final int command_id) {
        try {
            return this.N_IsEnabled(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isEnabledAt(final int index) {
        try {
            return this.N_IsEnabledAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setEnabled(final int command_id, final boolean enabled) {
        try {
            return this.N_SetEnabled(this.getNativeRef(null), command_id, enabled);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setEnabledAt(final int index, final boolean enabled) {
        try {
            return this.N_SetEnabledAt(this.getNativeRef(null), index, enabled);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isChecked(final int command_id) {
        try {
            return this.N_IsChecked(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isCheckedAt(final int index) {
        try {
            return this.N_IsCheckedAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setChecked(final int command_id, final boolean checked) {
        try {
            return this.N_SetChecked(this.getNativeRef(null), command_id, checked);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setCheckedAt(final int index, final boolean checked) {
        try {
            return this.N_SetCheckedAt(this.getNativeRef(null), index, checked);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean hasAccelerator(final int command_id) {
        try {
            return this.N_HasAccelerator(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean hasAcceleratorAt(final int index) {
        try {
            return this.N_HasAcceleratorAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setAccelerator(final int command_id, final int key_code, final boolean shift_pressed, final boolean ctrl_pressed, final boolean alt_pressed) {
        try {
            return this.N_SetAccelerator(this.getNativeRef(null), command_id, key_code, shift_pressed, ctrl_pressed, alt_pressed);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setAcceleratorAt(final int index, final int key_code, final boolean shift_pressed, final boolean ctrl_pressed, final boolean alt_pressed) {
        try {
            return this.N_SetAcceleratorAt(this.getNativeRef(null), index, key_code, shift_pressed, ctrl_pressed, alt_pressed);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean removeAccelerator(final int command_id) {
        try {
            return this.N_RemoveAccelerator(this.getNativeRef(null), command_id);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean removeAcceleratorAt(final int index) {
        try {
            return this.N_RemoveAcceleratorAt(this.getNativeRef(null), index);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean getAccelerator(final int command_id, final IntRef key_code, final BoolRef shift_pressed, final BoolRef ctrl_pressed, final BoolRef alt_pressed) {
        try {
            return this.N_GetAccelerator(this.getNativeRef(null), command_id, key_code, shift_pressed, ctrl_pressed, alt_pressed);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean getAcceleratorAt(final int index, final IntRef key_code, final BoolRef shift_pressed, final BoolRef ctrl_pressed, final BoolRef alt_pressed) {
        try {
            return this.N_GetAcceleratorAt(this.getNativeRef(null), index, key_code, shift_pressed, ctrl_pressed, alt_pressed);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    private final native boolean N_Clear(final long p0);
    
    private final native int N_GetCount(final long p0);
    
    private final native boolean N_AddSeparator(final long p0);
    
    private final native boolean N_AddItem(final long p0, final int p1, final String p2);
    
    private final native boolean N_AddCheckItem(final long p0, final int p1, final String p2);
    
    private final native boolean N_AddRadioItem(final long p0, final int p1, final String p2, final int p3);
    
    private final native CefMenuModel N_AddSubMenu(final long p0, final int p1, final String p2);
    
    private final native boolean N_InsertSeparatorAt(final long p0, final int p1);
    
    private final native boolean N_InsertItemAt(final long p0, final int p1, final int p2, final String p3);
    
    private final native boolean N_InsertCheckItemAt(final long p0, final int p1, final int p2, final String p3);
    
    private final native boolean N_InsertRadioItemAt(final long p0, final int p1, final int p2, final String p3, final int p4);
    
    private final native CefMenuModel N_InsertSubMenuAt(final long p0, final int p1, final int p2, final String p3);
    
    private final native boolean N_Remove(final long p0, final int p1);
    
    private final native boolean N_RemoveAt(final long p0, final int p1);
    
    private final native int N_GetIndexOf(final long p0, final int p1);
    
    private final native int N_GetCommandIdAt(final long p0, final int p1);
    
    private final native boolean N_SetCommandIdAt(final long p0, final int p1, final int p2);
    
    private final native String N_GetLabel(final long p0, final int p1);
    
    private final native String N_GetLabelAt(final long p0, final int p1);
    
    private final native boolean N_SetLabel(final long p0, final int p1, final String p2);
    
    private final native boolean N_SetLabelAt(final long p0, final int p1, final String p2);
    
    private final native MenuItemType N_GetType(final long p0, final int p1);
    
    private final native MenuItemType N_GetTypeAt(final long p0, final int p1);
    
    private final native int N_GetGroupId(final long p0, final int p1);
    
    private final native int N_GetGroupIdAt(final long p0, final int p1);
    
    private final native boolean N_SetGroupId(final long p0, final int p1, final int p2);
    
    private final native boolean N_SetGroupIdAt(final long p0, final int p1, final int p2);
    
    private final native CefMenuModel N_GetSubMenu(final long p0, final int p1);
    
    private final native CefMenuModel N_GetSubMenuAt(final long p0, final int p1);
    
    private final native boolean N_IsVisible(final long p0, final int p1);
    
    private final native boolean N_IsVisibleAt(final long p0, final int p1);
    
    private final native boolean N_SetVisible(final long p0, final int p1, final boolean p2);
    
    private final native boolean N_SetVisibleAt(final long p0, final int p1, final boolean p2);
    
    private final native boolean N_IsEnabled(final long p0, final int p1);
    
    private final native boolean N_IsEnabledAt(final long p0, final int p1);
    
    private final native boolean N_SetEnabled(final long p0, final int p1, final boolean p2);
    
    private final native boolean N_SetEnabledAt(final long p0, final int p1, final boolean p2);
    
    private final native boolean N_IsChecked(final long p0, final int p1);
    
    private final native boolean N_IsCheckedAt(final long p0, final int p1);
    
    private final native boolean N_SetChecked(final long p0, final int p1, final boolean p2);
    
    private final native boolean N_SetCheckedAt(final long p0, final int p1, final boolean p2);
    
    private final native boolean N_HasAccelerator(final long p0, final int p1);
    
    private final native boolean N_HasAcceleratorAt(final long p0, final int p1);
    
    private final native boolean N_SetAccelerator(final long p0, final int p1, final int p2, final boolean p3, final boolean p4, final boolean p5);
    
    private final native boolean N_SetAcceleratorAt(final long p0, final int p1, final int p2, final boolean p3, final boolean p4, final boolean p5);
    
    private final native boolean N_RemoveAccelerator(final long p0, final int p1);
    
    private final native boolean N_RemoveAcceleratorAt(final long p0, final int p1);
    
    private final native boolean N_GetAccelerator(final long p0, final int p1, final IntRef p2, final BoolRef p3, final BoolRef p4, final BoolRef p5);
    
    private final native boolean N_GetAcceleratorAt(final long p0, final int p1, final IntRef p2, final BoolRef p3, final BoolRef p4, final BoolRef p5);
}
