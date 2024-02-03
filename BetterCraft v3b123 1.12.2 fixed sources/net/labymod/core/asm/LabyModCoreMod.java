// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm;

import java.util.Map;
import java.util.List;
import java.util.Collection;
import java.net.URLClassLoader;
import net.labymod.addon.AddonLoader;
import net.minecraft.launchwrapper.Launch;
import java.util.ArrayList;
import net.labymod.support.util.Debug;
import java.io.PrintStream;
import java.io.OutputStream;
import net.labymod.support.util.CapturePrintStream;

public class LabyModCoreMod
{
    private static boolean obfuscated;
    private static boolean forge;
    
    static {
        System.setErr(new CapturePrintStream(System.err));
        System.setOut(new CapturePrintStream(System.out));
        System.setProperty("java.net.preferIPv4Stack", "true");
        Debug.init();
    }
    
    public static boolean isObfuscated() {
        return LabyModCoreMod.obfuscated;
    }
    
    public static void setObfuscated(final boolean obfuscated) {
        LabyModCoreMod.obfuscated = obfuscated;
    }
    
    public static boolean isForge() {
        return LabyModCoreMod.forge;
    }
    
    public static void setForge(final boolean forge) {
        LabyModCoreMod.forge = forge;
    }
    
    public String[] getASMTransformerClass() {
        StackTraceElement[] stackTrace;
        for (int length = (stackTrace = Thread.currentThread().getStackTrace()).length, i = 0; i < length; ++i) {
            final StackTraceElement stackTraceElement = stackTrace[i];
            if (stackTraceElement.getClassName().contains("FMLPluginWrapper")) {
                LabyModCoreMod.forge = true;
                break;
            }
        }
        final List<String> transformers = new ArrayList<String>();
        transformers.add("net.labymod.core.asm.LabyModTransformer");
        if (LabyModCoreMod.forge) {
            transformers.addAll(AddonLoader.getTransformerClasses(Launch.classLoader));
        }
        else {
            transformers.addAll(AddonLoader.getTransformerClasses((URLClassLoader)this.getClass().getClassLoader()));
        }
        return transformers.toArray(new String[transformers.size()]);
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        LabyModCoreMod.obfuscated = data.get("runtimeDeobfuscationEnabled");
        LabyModCoreMod.forge = true;
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
}
