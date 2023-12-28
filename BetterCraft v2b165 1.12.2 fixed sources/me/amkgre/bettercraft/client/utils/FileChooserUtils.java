// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.GuiMultiplayer;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileChooserUtils extends JFrame
{
    private static final JFileChooser chooser;
    
    static {
        chooser = new JFileChooser();
    }
    
    public FileChooserUtils() {
        CompletableFuture.runAsync(() -> {
            this.setSize(800, 600);
            this.setVisible(false);
            FileChooserUtils.chooser.showOpenDialog(null);
            FileChooserUtils.chooser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    final JFileChooser fc = FileChooserUtils.chooser;
                    final int returnVal = fc.showOpenDialog(null);
                    if (returnVal != 1) {
                        FileManagerUtils.customBackgroundFile = fc.getSelectedFile();
                        try {
                            FileManagerUtils.copyFileUsingStream(FileManagerUtils.customBackgroundFile, new File(FileManagerUtils.clientDir + "/customBG.bc"));
                            ClientSettingsUtils.strToField(ClientSettingsUtils.class.getDeclaredField("isCurrentBackgroundImageCustom"), "true");
                        }
                        catch (final Exception e2) {
                            e2.printStackTrace();
                        }
                        GuiMultiplayer.dynamicTexture = null;
                    }
                }
            });
        });
    }
    
    public static JFileChooser getChooser() {
        return FileChooserUtils.chooser;
    }
}
