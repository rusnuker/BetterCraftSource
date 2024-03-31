// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Component;
import javax.swing.JFrame;

class JFrameUtils
{
    public static JFrame setupJFrameAndGet(final String title, final int width, final int height) {
        final JFrame tmpJF = new JFrame(title);
        tmpJF.setSize(width, height);
        tmpJF.setLocationRelativeTo(null);
        tmpJF.setLayout(null);
        tmpJF.setDefaultCloseOperation(1);
        tmpJF.setBackground(Color.BLACK);
        return tmpJF;
    }
    
    public static JTextArea setupJTextAreaAndGet(final String text, final int rows, final int columns, final boolean setEditableFlag, final boolean setLineWrapFlag, final boolean setWrapStyleWordFlag, final boolean setBoundsFlag, final int xpos, final int ypos, final int width, final int height) {
        final JTextArea tmpJTA = new JTextArea(text, rows, columns);
        tmpJTA.setEditable(setEditableFlag);
        tmpJTA.setLineWrap(setLineWrapFlag);
        tmpJTA.setWrapStyleWord(setWrapStyleWordFlag);
        if (setBoundsFlag) {
            tmpJTA.setBounds(xpos, ypos, width, height);
        }
        tmpJTA.setBackground(Color.DARK_GRAY);
        tmpJTA.setForeground(Color.WHITE);
        return tmpJTA;
    }
    
    public static JScrollPane setupScrollableJTextAreaAndGet(final JTextArea jta, final int xpos, final int ypos, final int width, final int height) {
        final JScrollPane tmpJSP = new JScrollPane(jta);
        tmpJSP.setBounds(xpos, ypos, width, height);
        tmpJSP.setBackground(Color.BLACK);
        return tmpJSP;
    }
    
    public static JMenuBar setupJMenuBarAndGet() {
        final JMenuBar tmpJMB = new JMenuBar();
        tmpJMB.setBackground(Color.BLACK);
        return tmpJMB;
    }
    
    public static JMenu setupJMenuAndGet(final String text) {
        final JMenu tmpJM = new JMenu(text);
        tmpJM.setBackground(Color.BLACK);
        return tmpJM;
    }
    
    public static JMenuItem setupJMenuItemAndGet(final String text) {
        final JMenuItem tmpJMI = new JMenuItem(text);
        tmpJMI.setBackground(Color.BLACK);
        return tmpJMI;
    }
}
