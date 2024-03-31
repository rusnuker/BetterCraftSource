// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import org.cef.misc.BoolRef;
import org.cef.misc.IntRef;

public interface CefMenuModel
{
    boolean clear();
    
    int getCount();
    
    boolean addSeparator();
    
    boolean addItem(final int p0, final String p1);
    
    boolean addCheckItem(final int p0, final String p1);
    
    boolean addRadioItem(final int p0, final String p1, final int p2);
    
    CefMenuModel addSubMenu(final int p0, final String p1);
    
    boolean insertSeparatorAt(final int p0);
    
    boolean insertItemAt(final int p0, final int p1, final String p2);
    
    boolean insertCheckItemAt(final int p0, final int p1, final String p2);
    
    boolean insertRadioItemAt(final int p0, final int p1, final String p2, final int p3);
    
    CefMenuModel insertSubMenuAt(final int p0, final int p1, final String p2);
    
    boolean remove(final int p0);
    
    boolean removeAt(final int p0);
    
    int getIndexOf(final int p0);
    
    int getCommandIdAt(final int p0);
    
    boolean setCommandIdAt(final int p0, final int p1);
    
    String getLabel(final int p0);
    
    String getLabelAt(final int p0);
    
    boolean setLabel(final int p0, final String p1);
    
    boolean setLabelAt(final int p0, final String p1);
    
    MenuItemType getType(final int p0);
    
    MenuItemType getTypeAt(final int p0);
    
    int getGroupId(final int p0);
    
    int getGroupIdAt(final int p0);
    
    boolean setGroupId(final int p0, final int p1);
    
    boolean setGroupIdAt(final int p0, final int p1);
    
    CefMenuModel getSubMenu(final int p0);
    
    CefMenuModel getSubMenuAt(final int p0);
    
    boolean isVisible(final int p0);
    
    boolean isVisibleAt(final int p0);
    
    boolean setVisible(final int p0, final boolean p1);
    
    boolean setVisibleAt(final int p0, final boolean p1);
    
    boolean isEnabled(final int p0);
    
    boolean isEnabledAt(final int p0);
    
    boolean setEnabled(final int p0, final boolean p1);
    
    boolean setEnabledAt(final int p0, final boolean p1);
    
    boolean isChecked(final int p0);
    
    boolean isCheckedAt(final int p0);
    
    boolean setChecked(final int p0, final boolean p1);
    
    boolean setCheckedAt(final int p0, final boolean p1);
    
    boolean hasAccelerator(final int p0);
    
    boolean hasAcceleratorAt(final int p0);
    
    boolean setAccelerator(final int p0, final int p1, final boolean p2, final boolean p3, final boolean p4);
    
    boolean setAcceleratorAt(final int p0, final int p1, final boolean p2, final boolean p3, final boolean p4);
    
    boolean removeAccelerator(final int p0);
    
    boolean removeAcceleratorAt(final int p0);
    
    boolean getAccelerator(final int p0, final IntRef p1, final BoolRef p2, final BoolRef p3, final BoolRef p4);
    
    boolean getAcceleratorAt(final int p0, final IntRef p1, final BoolRef p2, final BoolRef p3, final BoolRef p4);
    
    public enum MenuItemType
    {
        MENUITEMTYPE_NONE("MENUITEMTYPE_NONE", 0), 
        MENUITEMTYPE_COMMAND("MENUITEMTYPE_COMMAND", 1), 
        MENUITEMTYPE_CHECK("MENUITEMTYPE_CHECK", 2), 
        MENUITEMTYPE_RADIO("MENUITEMTYPE_RADIO", 3), 
        MENUITEMTYPE_SEPARATOR("MENUITEMTYPE_SEPARATOR", 4), 
        MENUITEMTYPE_SUBMENU("MENUITEMTYPE_SUBMENU", 5);
        
        private MenuItemType(final String s, final int n) {
        }
    }
    
    public static final class MenuId
    {
        public static final int MENU_ID_BACK = 100;
        public static final int MENU_ID_FORWARD = 101;
        public static final int MENU_ID_RELOAD = 102;
        public static final int MENU_ID_RELOAD_NOCACHE = 103;
        public static final int MENU_ID_STOPLOAD = 104;
        public static final int MENU_ID_UNDO = 110;
        public static final int MENU_ID_REDO = 111;
        public static final int MENU_ID_CUT = 112;
        public static final int MENU_ID_COPY = 113;
        public static final int MENU_ID_PASTE = 114;
        public static final int MENU_ID_DELETE = 115;
        public static final int MENU_ID_SELECT_ALL = 116;
        public static final int MENU_ID_FIND = 130;
        public static final int MENU_ID_PRINT = 131;
        public static final int MENU_ID_VIEW_SOURCE = 132;
        public static final int MENU_ID_SPELLCHECK_SUGGESTION_0 = 200;
        public static final int MENU_ID_SPELLCHECK_SUGGESTION_1 = 201;
        public static final int MENU_ID_SPELLCHECK_SUGGESTION_2 = 202;
        public static final int MENU_ID_SPELLCHECK_SUGGESTION_3 = 203;
        public static final int MENU_ID_SPELLCHECK_SUGGESTION_4 = 204;
        public static final int MENU_ID_SPELLCHECK_SUGGESTION_LAST = 204;
        public static final int MENU_ID_NO_SPELLING_SUGGESTIONS = 205;
        public static final int MENU_ID_USER_FIRST = 26500;
        public static final int MENU_ID_USER_LAST = 28500;
    }
}
