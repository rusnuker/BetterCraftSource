// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.setup;

import java.util.Iterator;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.io.File;
import javax.swing.JFrame;

public class Processes
{
    public static boolean install(final JFrame parent, final File dst) {
        final File curJar = SetupUtil.getSelfJarLocation();
        if (curJar == null) {
            JOptionPane.showMessageDialog(parent, "Could not locate the current JAR file.\nThis shouldn't happen, contact mod author.\nCannot continue.", "Error", 0);
            return false;
        }
        final File mods = new File(dst, "mods");
        if (mods.exists()) {
            final File[] modList = mods.listFiles();
            File[] array;
            for (int length = (array = modList).length, i = 0; i < length; ++i) {
                final File f = array[i];
                final String fname = f.getName().toLowerCase();
                if (SetupUtil.areFileEqual(f, curJar)) {
                    SetupUI.INSTANCE.abortSelfDestruct();
                    JOptionPane.showMessageDialog(parent, "MCEF was successfully installed!\nIn fact, it was already installed here.\nAlso make sure Forge is installed!", "Well... there was nothing to do!", 1);
                    return true;
                }
                if (f.isFile() && fname.startsWith("mcef") && fname.endsWith(".jar")) {
                    while (!SetupUtil.tryDelete(f)) {
                        if (JOptionPane.showConfirmDialog(parent, "An older version of MCEF has been found and cannot be deleted.\nPlease close Minecraft or remove the following file manually:\n" + f.getAbsolutePath(), "WARNING", 2) == 2) {
                            return false;
                        }
                    }
                }
            }
        }
        else if (!mods.mkdir()) {
            JOptionPane.showMessageDialog(parent, "Could not create mods directory. Make sure you have the rights to do that.\nCannot continue.", "Error", 0);
            return false;
        }
        if (SetupUtil.copyFile(curJar, new File(mods, curJar.getName()))) {
            JOptionPane.showMessageDialog(parent, "MCEF was successfully installed!\nDon't forget to install Forge!", "All done!", 1);
            return true;
        }
        JOptionPane.showMessageDialog(parent, "Installation failed\nCould not copy JAR to mods folder.", "Critical error", 0);
        return false;
    }
    
    private static boolean recursiveDelete(final File dir) {
        if (!dir.exists()) {
            return true;
        }
        final File[] files = dir.listFiles();
        boolean allOk = true;
        File[] array;
        for (int length = (array = files).length, i = 0; i < length; ++i) {
            final File f = array[i];
            if (f.isDirectory()) {
                if (!recursiveDelete(f)) {
                    allOk = false;
                }
            }
            else if (!SetupUtil.tryDelete(f)) {
                allOk = false;
            }
        }
        return SetupUtil.tryDelete(dir) && allOk;
    }
    
    public static boolean uninstall(final JFrame parent, final File dst) {
        final File configDir = new File(dst, "config");
        final FileListing fl = new FileListing(configDir);
        if (!fl.load()) {
            JOptionPane.showMessageDialog(parent, "Could not locate MCEF file listing. It is either missing,\nor you selected the wrong Minecraft location.\nCannot continue.", "Error", 0);
            return false;
        }
        boolean allDeleted = true;
        final Iterator<String> files = fl.iterator();
        while (files.hasNext()) {
            if (!SetupUtil.tryDelete(new File(dst, files.next()))) {
                allDeleted = false;
            }
        }
        if (!recursiveDelete(new File(dst, "MCEFCache"))) {
            allDeleted = false;
        }
        if (!fl.selfDestruct()) {
            allDeleted = false;
        }
        if (!SetupUtil.tryDelete(new File(configDir, "MCEF.cfg"))) {
            allDeleted = false;
        }
        if (!SetupUtil.tryDelete(new File(dst, "mcef2.json"))) {
            allDeleted = false;
        }
        final File curJar = SetupUtil.getSelfJarLocation();
        final File mods = new File(dst, "mods");
        if (mods.exists()) {
            final File[] modList = mods.listFiles();
            File[] array;
            for (int length = (array = modList).length, i = 0; i < length; ++i) {
                final File f = array[i];
                final String fname = f.getName().toLowerCase();
                if (SetupUtil.areFileEqual(f, curJar)) {
                    SetupUI.INSTANCE.initiateSelfDestruct(f);
                }
                else if (f.isFile() && fname.startsWith("mcef") && fname.endsWith(".jar") && !SetupUtil.tryDelete(f)) {
                    allDeleted = false;
                }
            }
        }
        if (allDeleted) {
            JOptionPane.showMessageDialog(parent, "MCEF was successfully uninstalled!\nThanks for using it!", "All done!", 1);
        }
        else {
            JOptionPane.showMessageDialog(parent, "MCEF was uninstalled, but some files couldn't be removed; sorry about that...", "Almost everything done!", 2);
        }
        return true;
    }
}
