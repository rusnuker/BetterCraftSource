// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.remote;

import net.montoyo.mcef.setup.FileListing;
import net.montoyo.mcef.utilities.Version;
import net.montoyo.mcef.MCEF;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import com.google.gson.JsonArray;
import net.minecraft.client.Minecraft;
import com.google.gson.JsonElement;
import org.cef.OS;
import net.montoyo.mcef.utilities.IProgressListener;
import net.montoyo.mcef.utilities.Util;
import net.montoyo.mcef.client.ClientProxy;
import java.io.FileNotFoundException;
import com.google.gson.JsonIOException;
import net.montoyo.mcef.utilities.Log;
import java.io.Reader;
import java.io.FileReader;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import java.io.File;
import java.util.ArrayList;

public class RemoteConfig
{
    private static String PLATFORM;
    private ResourceList resources;
    private ArrayList<String> extract;
    private String version;
    
    public RemoteConfig() {
        this.resources = new ResourceList();
        this.extract = new ArrayList<String>();
        this.version = null;
    }
    
    private JsonObject readConfig(final File f) {
        try {
            return new JsonParser().parse(new FileReader(f)).getAsJsonObject();
        }
        catch (final JsonIOException e) {
            Log.error("IOException while reading remote config.", new Object[0]);
            e.printStackTrace();
            return null;
        }
        catch (final FileNotFoundException e2) {
            Log.error("Couldn't find remote config.", new Object[0]);
            e2.printStackTrace();
            return null;
        }
        catch (final Exception e3) {
            Log.error("Syntax error in remote config.", new Object[0]);
            e3.printStackTrace();
            return null;
        }
    }
    
    private JsonObject readConfig() {
        final File newCfg = new File(ClientProxy.ROOT, "mcef2.new");
        final File cfgFle = new File(ClientProxy.ROOT, "mcef2.json");
        final boolean ok = Util.download("config2.json", newCfg, null);
        if (!ok) {
            Log.warning("Couldn't read remote config. Using local configuration file.", new Object[0]);
            return this.readConfig(cfgFle);
        }
        Util.delete(cfgFle);
        if (newCfg.renameTo(cfgFle)) {
            return this.readConfig(cfgFle);
        }
        Log.warning("Couldn't rename mcef2.new to mcef2.json.", new Object[0]);
        return this.readConfig(newCfg);
    }
    
    public void load() {
        final JsonObject json = this.readConfig();
        if (json == null) {
            Log.error("Could NOT read either remote and local configuration files. Entering virtual mode.", new Object[0]);
            ClientProxy.VIRTUAL = true;
            return;
        }
        String id;
        if (OS.isWindows()) {
            id = "win";
        }
        else if (OS.isMacintosh()) {
            id = "mac";
        }
        else {
            if (!OS.isLinux()) {
                Log.error("Your OS isn't supported by MCEF. Entering virtual mode.", new Object[0]);
                ClientProxy.VIRTUAL = true;
                return;
            }
            id = "linux";
        }
        final String arch = System.getProperty("sun.arch.data.model");
        if (!arch.equals("32") && !arch.equals("64")) {
            Log.error("Your CPU arch isn't supported by MCEF. Entering virtual mode.", new Object[0]);
            ClientProxy.VIRTUAL = true;
            return;
        }
        RemoteConfig.PLATFORM = String.valueOf(id) + arch;
        Log.info("Detected platform: %s", RemoteConfig.PLATFORM);
        final JsonElement ver = json.get("1.11");
        if (ver == null || !ver.isJsonObject()) {
            Log.error("Config file does NOT contain the latest MCEF version (wtf??). Entering virtual mode.", new Object[0]);
            ClientProxy.VIRTUAL = true;
            return;
        }
        final JsonObject vData = ver.getAsJsonObject();
        final JsonElement cat = vData.get("platforms");
        if (cat == null || !cat.isJsonObject()) {
            Log.error("Config file is missing \"platforms\" object. Entering virtual mode.", new Object[0]);
            ClientProxy.VIRTUAL = true;
            return;
        }
        final JsonObject catObj = cat.getAsJsonObject();
        JsonElement res = catObj.get(RemoteConfig.PLATFORM);
        if (res == null || !res.isJsonObject()) {
            Log.error("Your platform isn't supported by MCEF yet. Entering virtual mode.", new Object[0]);
            ClientProxy.VIRTUAL = true;
            return;
        }
        this.resources.clear();
        this.addResources(res.getAsJsonObject(), RemoteConfig.PLATFORM);
        res = catObj.get("shared");
        if (res != null && res.isJsonObject()) {
            this.addResources(res.getAsJsonObject(), "shared");
        }
        final JsonElement ext = vData.get("extract");
        if (ext != null && ext.isJsonArray()) {
            final JsonArray ray = ext.getAsJsonArray();
            for (final JsonElement e : ray) {
                if (e != null && e.isJsonPrimitive()) {
                    this.extract.add(e.getAsString());
                }
            }
        }
        final JsonElement mcVersions = json.get("latestVersions");
        if (mcVersions != null && mcVersions.isJsonObject()) {
            final JsonElement cVer = mcVersions.getAsJsonObject().get(Minecraft.getMinecraft().getVersion());
            if (cVer != null && cVer.isJsonPrimitive()) {
                this.version = cVer.getAsString();
            }
        }
    }
    
    private void addResources(final JsonObject res, final String pform) {
        final Set<Map.Entry<String, JsonElement>> files = res.entrySet();
        for (final Map.Entry<String, JsonElement> e : files) {
            if (e.getValue() != null) {
                if (!e.getValue().isJsonPrimitive()) {
                    continue;
                }
                final String key = e.getKey();
                if (key.length() >= 2 && key.charAt(0) == '@') {
                    final Resource eRes = new Resource(key.substring(1), e.getValue().getAsString(), pform);
                    eRes.setShouldExtract();
                    this.resources.add(eRes);
                }
                else {
                    this.resources.add(new Resource(key, e.getValue().getAsString(), pform));
                }
            }
        }
    }
    
    public boolean downloadMissing(final IProgressListener ipl) {
        if (MCEF.SKIP_UPDATES) {
            Log.warning("NOT downloading resources as specified in the configuration file", new Object[0]);
            return true;
        }
        Log.info("Checking for missing resources...", new Object[0]);
        this.resources.removeExistings();
        if (this.resources.size() > 0) {
            Log.info("Found %d missing resources. Downloading...", this.resources.size());
            for (final Resource r : this.resources) {
                if (!r.download(ipl)) {
                    return false;
                }
            }
            for (final String r2 : this.extract) {
                final Resource res = this.resources.fromFileName(r2);
                if (res == null) {
                    continue;
                }
                if (res.extract(ipl)) {
                    continue;
                }
                Log.warning("Couldn't extract %s. MCEF may not work because of this.", r2);
            }
            Log.info("Done; all resources were downloaded.", new Object[0]);
        }
        else {
            Log.info("None are missing. Good.", new Object[0]);
        }
        return true;
    }
    
    public String getUpdateString() {
        if (this.version == null) {
            return null;
        }
        final Version cur = new Version("1.11");
        final Version cfg = new Version(this.version);
        if (cfg.isBiggerThan(cur)) {
            return "New MCEF version available. Current: " + cur + ", latest: " + cfg + '.';
        }
        return null;
    }
    
    public boolean updateFileListing(final File configDir, final boolean zipOnly) {
        if (this.resources.isEmpty()) {
            return true;
        }
        final FileListing fl = new FileListing(configDir);
        if (!fl.load()) {
            Log.warning("Could not load file listing; trying to overwrite...", new Object[0]);
        }
        if (!zipOnly) {
            for (final Resource r : this.resources) {
                fl.addFile(r.getFileName());
            }
        }
        boolean allOk = true;
        for (final String r2 : this.extract) {
            final File rf = Resource.getLocationOf(r2);
            if (rf.exists() && !fl.addZip(rf.getAbsolutePath())) {
                allOk = false;
            }
        }
        return fl.save() && allOk;
    }
    
    public File[] getResourceArray() {
        return this.resources.stream().map(r -> Resource.getLocationOf(r.getFileName())).toArray(File[]::new);
    }
}
