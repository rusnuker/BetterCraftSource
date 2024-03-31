// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import java.awt.Component;
import javax.swing.JOptionPane;
import net.montoyo.mcef.utilities.Log;
import java.io.IOException;
import net.montoyo.mcef.utilities.Util;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

@Deprecated
public class LinuxPatch
{
    private static String[] getBinDirs() {
        return new String[] { "/bin", "/usr/bin", "/usr/sbin", "/usr/local/bin", "/usr/local/sbin" };
    }
    
    private static String[] getPatchFiles() {
        return new String[] { "icudtl.dat", "natives_blob.bin", "snapshot_blob.bin", "v8_context_snapshot.bin" };
    }
    
    private static String getExeLocation(final String exe) {
        final String[] bins = getBinDirs();
        String[] array;
        for (int length = (array = bins).length, i = 0; i < length; ++i) {
            final String b = array[i];
            final File f = new File(b, exe);
            if (f.exists()) {
                return f.getAbsolutePath();
            }
        }
        return null;
    }
    
    private static String getGksudoLocation() {
        final String ret = getExeLocation("gksudo");
        return (ret == null) ? getExeLocation("gksu") : ret;
    }
    
    public static File getScriptFile() {
        return new File(ClientProxy.ROOT, "mcefLinuxPatch.sh");
    }
    
    public static void generateScript() throws Throwable {
        final String[] files = getPatchFiles();
        final BufferedWriter bw = new BufferedWriter(new FileWriter(getScriptFile()));
        bw.write("#!/bin/sh\n");
        bw.write("MCEF_ROOT=\"" + ClientProxy.ROOT + "\"\n");
        bw.write("JAVA_ROOT=\"" + System.getProperty("java.home") + "/bin\"\n\n");
        String[] array;
        for (int length = (array = files).length, i = 0; i < length; ++i) {
            final String f = array[i];
            bw.write("rm -f \"$JAVA_ROOT/" + f + "\"\n");
        }
        bw.write("\n\n");
        String[] array2;
        for (int length2 = (array2 = files).length, j = 0; j < length2; ++j) {
            final String f = array2[j];
            bw.write("ln -s \"$MCEF_ROOT/" + f + "\" \"$JAVA_ROOT/" + f + "\"\n");
        }
        bw.write("\n\n");
        Util.close(bw);
    }
    
    public static boolean chmodX(final File p) {
        try {
            return Runtime.getRuntime().exec(new String[] { "chmod", "+x", p.getAbsolutePath() }).waitFor() == 0;
        }
        catch (final IOException | InterruptedException ex) {
            return false;
        }
    }
    
    public static boolean runScript() {
        final String cmd = getGksudoLocation();
        if (cmd == null) {
            return false;
        }
        try {
            if (!chmodX(getScriptFile())) {
                Log.error("chmod failed!", new Object[0]);
                return false;
            }
            if (Runtime.getRuntime().exec(new String[] { cmd, getScriptFile().getAbsolutePath() }).waitFor() != 0) {
                Log.error("gksudo failed!", new Object[0]);
                return false;
            }
            for (int i = 0; i < 6 && !isPatched(); ++i) {
                try {
                    Thread.sleep(1000L);
                }
                catch (final Throwable t) {}
            }
            return true;
        }
        catch (final IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean isPatched() {
        final File root = new File(System.getProperty("java.home"), "bin");
        final String[] files = getPatchFiles();
        String[] array;
        for (int length = (array = files).length, i = 0; i < length; ++i) {
            final String f = array[i];
            if (!new File(root, f).exists()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean doPatch(final File[] resourceArray) {
        for (final File f : resourceArray) {
            if (f.exists() && !chmodX(f)) {
                Log.warning("Couldn't give execution access to %s", f.getAbsolutePath());
            }
        }
        if (isPatched()) {
            return true;
        }
        try {
            generateScript();
        }
        catch (final Throwable t) {
            Log.error("Could not apply linux patch:", new Object[0]);
            t.printStackTrace();
            return false;
        }
        final int ans = JOptionPane.showConfirmDialog(null, "An existing bug in JCEF requires some files to be copied\ninto the Java home directory in order to make MCEF working.\nThis operations requires root privileges.\nDo you want MCEF to try to do it automatically?", "MCEF Linux", 0);
        if (ans != 0) {
            JOptionPane.showMessageDialog(null, "MCEF will enter virtual mode.\nA script containing the patch was generated here:\n" + getScriptFile().getAbsolutePath(), "MCEF Linux", 1);
            return false;
        }
        return runScript();
    }
}
