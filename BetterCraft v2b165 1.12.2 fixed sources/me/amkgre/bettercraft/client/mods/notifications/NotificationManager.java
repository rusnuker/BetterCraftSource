// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.notifications;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager
{
    private static LinkedBlockingQueue<Notification> pendingNotifications;
    private static Notification currentNotification;
    
    static {
        NotificationManager.pendingNotifications = new LinkedBlockingQueue<Notification>();
        NotificationManager.currentNotification = null;
    }
    
    public static void show(final Notification notification) {
        NotificationManager.pendingNotifications.add(notification);
    }
    
    public static void update() {
        if (NotificationManager.currentNotification != null && !NotificationManager.currentNotification.isShown()) {
            NotificationManager.currentNotification = null;
        }
        if (NotificationManager.currentNotification == null && !NotificationManager.pendingNotifications.isEmpty()) {
            (NotificationManager.currentNotification = NotificationManager.pendingNotifications.poll()).show();
        }
    }
    
    public static void render() {
        update();
        if (NotificationManager.currentNotification != null) {
            NotificationManager.currentNotification.render();
        }
    }
}
