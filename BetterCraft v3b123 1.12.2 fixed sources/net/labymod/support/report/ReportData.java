// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.support.report;

import com.google.gson.JsonObject;
import java.util.Base64;
import java.io.Serializable;
import org.apache.commons.lang3.SerializationUtils;
import net.labymod.core.asm.LabyModCoreMod;
import java.util.Iterator;
import net.labymod.api.LabyModAddon;
import net.labymod.addon.AddonLoader;
import java.util.ArrayList;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.lang.management.ManagementFactory;
import com.google.gson.Gson;
import net.labymod.main.Source;
import net.labymod.main.LabyMod;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.Map;

public class ReportData
{
    private final Throwable cause;
    private Map<String, Callable<String>> entries;
    
    public ReportData(final Throwable causeThrowable) {
        this.entries = new HashMap<String, Callable<String>>();
        this.cause = causeThrowable;
        this.putAllEntries();
    }
    
    private void putAllEntries() {
        final Gson gson = new GsonBuilder().create();
        this.entries.put("mc_uuid", new Callable<String>() {
            @Override
            public String call() {
                return LabyMod.getInstance().getPlayerUUID().toString();
            }
        });
        this.entries.put("mc_username", new Callable<String>() {
            @Override
            public String call() {
                return LabyMod.getInstance().getPlayerName();
            }
        });
        this.entries.put("mc_version", new Callable<String>() {
            @Override
            public String call() {
                return Source.ABOUT_MC_VERSION;
            }
        });
        this.entries.put("labymod_version", new Callable<String>() {
            @Override
            public String call() {
                return "3.6.6";
            }
        });
        this.entries.put("os", new Callable<String>() {
            @Override
            public String call() {
                return String.valueOf(System.getProperty("os.name")) + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
            }
        });
        this.entries.put("java_version", new Callable<String>() {
            @Override
            public String call() {
                return String.valueOf(System.getProperty("java.version")) + ", " + System.getProperty("java.vendor");
            }
        });
        this.entries.put("java_vm_version", new Callable<String>() {
            @Override
            public String call() {
                return String.valueOf(System.getProperty("java.vm.name")) + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
            }
        });
        this.entries.put("mc_memory", new Callable<String>() {
            @Override
            public String call() {
                final Runtime runtime = Runtime.getRuntime();
                final long i = runtime.maxMemory();
                final long j = runtime.totalMemory();
                final long k = runtime.freeMemory();
                final long l = i / 1024L / 1024L;
                final long i2 = j / 1024L / 1024L;
                final long j2 = k / 1024L / 1024L;
                return String.valueOf(k) + " bytes (" + j2 + " MB) / " + j + " bytes (" + i2 + " MB) up to " + i + " bytes (" + l + " MB)";
            }
        });
        this.entries.put("jvm_flags", new Callable<String>() {
            @Override
            public String call() {
                final RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
                final List<String> list = runtimemxbean.getInputArguments();
                return gson.toJson(list, new TypeToken<List<String>>() {}.getType());
            }
        });
        this.entries.put("addons", new Callable<String>() {
            @Override
            public String call() {
                final List<String> list = new ArrayList<String>();
                for (final LabyModAddon addon : AddonLoader.getAddons()) {
                    if (addon != null && addon.about != null && addon.about.uuid != null && addon.about.name != null) {
                        list.add(String.valueOf(addon.about.name) + " (" + addon.about.uuid.toString() + ")");
                    }
                }
                return gson.toJson(list, new TypeToken<List<String>>() {}.getType());
            }
        });
        this.entries.put("vanilla_forge", new Callable<String>() {
            @Override
            public String call() {
                return String.valueOf(LabyModCoreMod.isForge() ? 0 : 1);
            }
        });
        this.entries.put("exception", new Callable<String>() {
            @Override
            public String call() {
                final byte[] bytes = SerializationUtils.serialize(ReportData.this.cause);
                return Base64.getEncoder().encodeToString(bytes);
            }
        });
    }
    
    public String createJsonReport() {
        final JsonObject reportData = new JsonObject();
        for (final Map.Entry<String, Callable<String>> entry : this.entries.entrySet()) {
            final String key = entry.getKey();
            String value = "Unknown";
            try {
                value = entry.getValue().call();
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            reportData.addProperty(key, value);
        }
        return reportData.toString();
    }
}
