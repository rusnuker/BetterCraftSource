// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import java.lang.annotation.Annotation;
import java.io.File;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import wdl.MessageTypeCategory;
import wdl.WDLMessages;
import wdl.WDL;
import wdl.WDLPluginChannels;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import wdl.EntityRealigner;
import wdl.HologramHandler;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class WDLApi
{
    private static Logger logger;
    private static Map<String, ModInfo<?>> wdlMods;
    
    static {
        WDLApi.logger = LogManager.getLogger();
        WDLApi.wdlMods = new HashMap<String, ModInfo<?>>();
        WDLApi.logger.info("Loading default WDL extensions");
        addWDLMod("Hologram", "1.0", new HologramHandler());
        addWDLMod("EntityRealigner", "1.0", new EntityRealigner());
    }
    
    public static void saveTileEntity(final BlockPos pos, final TileEntity te) {
        if (!WDLPluginChannels.canSaveTileEntities(pos.getX() << 16, pos.getZ() << 16)) {
            WDLApi.logger.warn("API attempted to call saveTileEntity when saving TileEntities is not allowed!  Pos: " + pos + ", te: " + te + ".  StackTrace: ");
            logStackTrace();
            return;
        }
        WDL.saveTileEntity(pos, te);
    }
    
    public static void addWDLMod(final String id, final String version, final IWDLMod mod) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null!  (mod=" + mod + ", version=" + version + ")");
        }
        if (version == null) {
            throw new IllegalArgumentException("version must not be null!  (mod=" + mod + ", id=" + version + ")");
        }
        if (mod == null) {
            throw new IllegalArgumentException("mod must not be null!  (id=" + id + ", version=" + version + ")");
        }
        final ModInfo<IWDLMod> info = new ModInfo<IWDLMod>(id, version, mod, null);
        if (WDLApi.wdlMods.containsKey(id)) {
            throw new IllegalArgumentException("A mod by the name of '" + id + "' is already registered by " + WDLApi.wdlMods.get(id) + " (tried to register " + info + " over it)");
        }
        if (mod.isValidEnvironment("1.11a-beta1")) {
            WDLApi.wdlMods.put(id, info);
            if (mod instanceof IMessageTypeAdder) {
                final Map<String, IWDLMessageType> types = ((IMessageTypeAdder)mod).getMessageTypes();
                final ModMessageTypeCategory category = new ModMessageTypeCategory(info);
                for (final Map.Entry<String, IWDLMessageType> e : types.entrySet()) {
                    WDLMessages.registerMessage(e.getKey(), e.getValue(), category);
                }
            }
            return;
        }
        final String errorMessage = mod.getEnvironmentErrorMessage("1.11a-beta1");
        if (errorMessage != null) {
            throw new IllegalArgumentException(errorMessage);
        }
        throw new IllegalArgumentException("Environment for " + info + " is incorrect!  Perhaps it is for a different" + " version of WDL?  You are running " + "1.11a-beta1" + ".");
    }
    
    public static <T extends IWDLMod> List<ModInfo<T>> getImplementingExtensions(final Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null!");
        }
        final List<ModInfo<T>> returned = new ArrayList<ModInfo<T>>();
        for (final ModInfo<?> info : WDLApi.wdlMods.values()) {
            if (!info.isEnabled()) {
                continue;
            }
            if (!clazz.isAssignableFrom(info.mod.getClass())) {
                continue;
            }
            final ModInfo<T> infoCasted = (ModInfo<T>)info;
            returned.add(infoCasted);
        }
        return returned;
    }
    
    public static <T extends IWDLMod> List<ModInfo<T>> getAllImplementingExtensions(final Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null!");
        }
        final List<ModInfo<T>> returned = new ArrayList<ModInfo<T>>();
        for (final ModInfo<?> info : WDLApi.wdlMods.values()) {
            if (clazz.isAssignableFrom(info.mod.getClass())) {
                final ModInfo<T> infoCasted = (ModInfo<T>)info;
                returned.add(infoCasted);
            }
        }
        return returned;
    }
    
    public static Map<String, ModInfo<?>> getWDLMods() {
        return (Map<String, ModInfo<?>>)ImmutableMap.copyOf((Map<?, ?>)WDLApi.wdlMods);
    }
    
    public static String getModInfo(final String name) {
        if (!WDLApi.wdlMods.containsKey(name)) {
            return null;
        }
        return WDLApi.wdlMods.get(name).getInfo();
    }
    
    private static void logStackTrace() {
        final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement[] array;
        for (int length = (array = elements).length, i = 0; i < length; ++i) {
            final StackTraceElement e = array[i];
            WDLApi.logger.warn(e.toString());
        }
    }
    
    private static class ModMessageTypeCategory extends MessageTypeCategory
    {
        private ModInfo<?> mod;
        
        public ModMessageTypeCategory(final ModInfo<?> mod) {
            super(mod.id);
        }
        
        @Override
        public String getDisplayName() {
            return this.mod.getDisplayName();
        }
    }
    
    public static class ModInfo<T extends IWDLMod>
    {
        public final String id;
        public final String version;
        public final T mod;
        
        private ModInfo(final String id, final String version, final T mod) {
            this.id = id;
            this.version = version;
            this.mod = mod;
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.id) + "v" + this.version + " (" + this.mod.toString() + "/" + this.mod.getClass().getName() + ")";
        }
        
        public String getDisplayName() {
            if (this.mod instanceof IWDLModDescripted) {
                final String name = ((IWDLModDescripted)this.mod).getDisplayName();
                if (name != null && !name.isEmpty()) {
                    return name;
                }
            }
            return this.id;
        }
        
        public String getInfo() {
            final StringBuilder info = new StringBuilder();
            info.append("Id: ").append(this.id).append('\n');
            info.append("Version: ").append(this.version).append('\n');
            if (this.mod instanceof IWDLModDescripted) {
                final IWDLModDescripted dmod = (IWDLModDescripted)this.mod;
                final String displayName = dmod.getDisplayName();
                final String mainAuthor = dmod.getMainAuthor();
                final String[] authors = dmod.getAuthors();
                final String url = dmod.getURL();
                final String description = dmod.getDescription();
                if (displayName != null && !displayName.isEmpty()) {
                    info.append("Display name: ").append(displayName).append('\n');
                }
                if (mainAuthor != null && !mainAuthor.isEmpty()) {
                    info.append("Main author: ").append(mainAuthor).append('\n');
                }
                if (authors != null && authors.length > 0) {
                    info.append("Authors: ");
                    for (int i = 0; i < authors.length; ++i) {
                        if (!authors[i].equals(mainAuthor)) {
                            if (i <= authors.length - 2) {
                                info.append(", ");
                            }
                            else if (i == authors.length - 1) {
                                info.append(", and ");
                            }
                            else {
                                info.append('\n');
                            }
                        }
                    }
                }
                if (url != null && !url.isEmpty()) {
                    info.append("URL: ").append(url).append('\n');
                }
                if (description != null && !description.isEmpty()) {
                    info.append("Description: \n").append(description).append('\n');
                }
            }
            info.append("Main class: ").append(this.mod.getClass().getName()).append('\n');
            info.append("Containing file: ");
            try {
                String path = new File(this.mod.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
                final String username = System.getProperty("user.name");
                path = path.replace(username, "<USERNAME>");
                info.append(path);
            }
            catch (final Exception e) {
                info.append("Unknown (").append(e.toString()).append(')');
            }
            info.append('\n');
            final Class[] interfaces = this.mod.getClass().getInterfaces();
            info.append("Implemented interfaces (").append(interfaces.length).append(")\n");
            for (int j = 0; j < interfaces.length; ++j) {
                info.append(j).append(": ").append(interfaces[j].getName()).append('\n');
            }
            info.append("Superclass: ").append(this.mod.getClass().getSuperclass().getName()).append('\n');
            final ClassLoader loader = this.mod.getClass().getClassLoader();
            info.append("Classloader: ").append(loader);
            if (loader != null) {
                info.append(" (").append(loader.getClass().getName()).append(')');
            }
            info.append('\n');
            final Annotation[] annotations = this.mod.getClass().getAnnotations();
            info.append("Annotations (").append(annotations.length).append(")\n");
            for (int k = 0; k < annotations.length; ++k) {
                info.append(k).append(": ").append(annotations[k].toString()).append(" (").append(annotations[k].annotationType().getName()).append(")\n");
            }
            return info.toString();
        }
        
        public boolean isEnabled() {
            return WDL.globalProps.getProperty("Extensions." + this.id + ".enabled", "true").equals("true");
        }
        
        public void setEnabled(final boolean enabled) {
            WDL.globalProps.setProperty("Extensions." + this.id + ".enabled", Boolean.toString(enabled));
            WDL.saveGlobalProps();
        }
        
        public void toggleEnabled() {
            this.setEnabled(!this.isEnabled());
        }
    }
}
