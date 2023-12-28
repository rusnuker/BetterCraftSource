// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.net.MalformedURLException;
import java.awt.Rectangle;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Frame;
import java.awt.Window;

public class StartScreenUtils
{
    public static Window window;
    public static int loadingProgress;
    public static int progressTo;
    public static int i1;
    public static int i2;
    public static int i3;
    public static int i4;
    public static int i5;
    public static int ticksToNextUpdate;
    
    static {
        StartScreenUtils.progressTo = 10;
        StartScreenUtils.ticksToNextUpdate = 10;
    }
    
    public static void draw() throws MalformedURLException {
        (StartScreenUtils.window = new Window(null) {
            @Override
            public void paint(final Graphics g) {
                Font font = this.getFont().deriveFont(48.0f);
                g.setFont(font);
                g.setColor(Color.WHITE);
                final String message = "Starting BetterCraft Client...";
                final String message2 = "Please wait a moment.";
                FontMetrics metrics = g.getFontMetrics();
                g.drawString("Starting BetterCraft Client...", (this.getWidth() - metrics.stringWidth("Starting BetterCraft Client...")) / 2, (this.getHeight() - metrics.getHeight()) / 2);
                font = this.getFont().deriveFont(30.0f);
                g.setFont(font);
                g.setColor(Color.WHITE);
                metrics = g.getFontMetrics();
                g.drawString("Please wait a moment.", (this.getWidth() - metrics.stringWidth("Please wait a moment.")) / 2, (this.getHeight() - metrics.getHeight()) / 2 + 30);
                if (StartScreenUtils.loadingProgress < StartScreenUtils.progressTo) {
                    if (StartScreenUtils.ticksToNextUpdate < 700) {
                        ++StartScreenUtils.ticksToNextUpdate;
                    }
                    else {
                        if (++StartScreenUtils.loadingProgress >= 100) {
                            this.setVisible(false);
                        }
                        StartScreenUtils.ticksToNextUpdate = 0;
                    }
                }
                g.drawLine(0, StartScreenUtils.window.getHeight() / 2 + StartScreenUtils.window.getHeight() / 4, StartScreenUtils.window.getWidth() / 100 * StartScreenUtils.loadingProgress, StartScreenUtils.window.getHeight() / 2 + StartScreenUtils.window.getHeight() / 4);
                super.paint(g);
            }
            
            @Override
            public void update(final Graphics g) {
                this.paint(g);
            }
        }).setFocusableWindowState(true);
        StartScreenUtils.window.setFocusable(true);
        final Rectangle bounds;
        final Rectangle rect = bounds = StartScreenUtils.window.getGraphicsConfiguration().getBounds();
        bounds.height /= 2;
        final Rectangle rectangle = rect;
        rectangle.width /= 2;
        rect.x = rect.width / 2;
        rect.y = rect.height / 2;
        StartScreenUtils.window.setBounds(rect);
        StartScreenUtils.window.setBackground(new Color(2302498));
        StartScreenUtils.window.setVisible(true);
        new Thread(() -> {
            while (StartScreenUtils.window.isVisible()) {
                StartScreenUtils.window.update(StartScreenUtils.window.getGraphics());
            }
        }).start();
    }
    
    public static void update(final int newProgress) {
        StartScreenUtils.progressTo = newProgress;
    }
}
