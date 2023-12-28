// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.awt.AWTException;
import java.awt.Image;
import me.amkgre.bettercraft.client.Client;
import java.awt.Toolkit;
import java.awt.SystemTray;
import java.awt.TrayIcon;

public class DisplayTrayUtils
{
    public static void displayTray(final String sub, final String msg, final TrayIcon.MessageType type) throws AWTException {
        if (SystemTray.isSupported()) {
            final SystemTray tray = SystemTray.getSystemTray();
            final Image image = Toolkit.getDefaultToolkit().createImage("textures/icons/icon_32x32.png");
            final TrayIcon trayIcon = new TrayIcon(image, "");
            trayIcon.setImageAutoSize(true);
            final TrayIcon trayIcon2 = trayIcon;
            Client.getInstance();
            final StringBuilder append = new StringBuilder(String.valueOf(Client.clientName)).append(" ");
            Client.getInstance();
            trayIcon2.setToolTip(append.append(Client.clientVersion).toString());
            tray.add(trayIcon);
            trayIcon.displayMessage(sub, msg, type);
        }
        else {
            System.out.println("Can't display notification! Not supported on this OS version!");
        }
    }
}
