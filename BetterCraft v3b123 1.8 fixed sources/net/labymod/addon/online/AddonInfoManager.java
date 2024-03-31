// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addon.online;

import java.util.Set;
import com.google.gson.JsonArray;
import java.nio.file.Path;
import java.util.Iterator;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.io.FileWriter;
import net.labymod.api.LabyModAddon;
import net.labymod.support.util.Debug;
import net.labymod.addon.AddonLoader;
import net.labymod.addon.LabyModOFAddon;
import net.labymod.addon.online.info.OnlineAddonInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.labymod.utils.JsonParse;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.labymod.main.Source;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import net.labymod.gui.elements.CheckBox;
import java.util.List;
import net.labymod.addon.online.info.AddonInfo;
import java.util.UUID;
import java.util.Map;

public class AddonInfoManager
{
    private static AddonInfoManager instance;
    private Map<UUID, AddonInfo> addonInfoMap;
    private List<AddonInfo> addonInfoList;
    private List<String> categories;
    private CheckBox[] categorieCheckboxList;
    private boolean loaded;
    private boolean calledInit;
    
    public AddonInfoManager() {
        this.addonInfoMap = new HashMap<UUID, AddonInfo>();
        this.addonInfoList = new ArrayList<AddonInfo>();
        this.categories = new ArrayList<String>(Arrays.asList("All"));
        this.categorieCheckboxList = new CheckBox[this.categories.size()];
        this.loaded = false;
        this.calledInit = false;
    }
    
    public static AddonInfoManager getInstance() {
        if (AddonInfoManager.instance == null) {
            AddonInfoManager.instance = new AddonInfoManager();
        }
        return AddonInfoManager.instance;
    }
    
    public void init() {
        if (this.calledInit) {
            return;
        }
        this.calledInit = true;
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL("http://dl.labymod.net/addons.json").openConnection();
            connection.setRequestProperty("User-Agent", Source.getUserAgent());
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.connect();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final Map<UUID, AddonInfo> addonInfoMap = new HashMap<UUID, AddonInfo>();
            final List<AddonInfo> addonInfoList = new ArrayList<AddonInfo>();
            String addonContents = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                addonContents = String.valueOf(addonContents) + (addonContents.equals("") ? "" : "\n") + line;
            }
            new JsonParse();
            final JsonObject jsonObject = (JsonObject)JsonParse.parse(addonContents);
            final JsonArray array = jsonObject.get("categories").getAsJsonArray();
            this.categorieCheckboxList = new CheckBox[array.size()];
            int i = 0;
            for (final JsonElement element : array) {
                final String displayName = element.getAsString();
                this.categories.add(displayName);
                this.categorieCheckboxList[i] = new CheckBox(displayName, CheckBox.EnumCheckBoxValue.ENABLED, null, 0, 0, 15, 15);
                ++i;
            }
            final Set<Map.Entry<String, JsonElement>> addonsSet = jsonObject.get("addons").getAsJsonObject().entrySet();
            for (final Map.Entry<String, JsonElement> entry : addonsSet) {
                if (!Source.getMajorVersion().equals(entry.getKey())) {
                    continue;
                }
                final JsonArray jsonArray = entry.getValue().getAsJsonArray();
                final Iterator<JsonElement> addonIterator = jsonArray.iterator();
                while (addonIterator.hasNext()) {
                    final JsonObject object = addonIterator.next().getAsJsonObject();
                    final JsonElement addonUUID = object.get("uuid");
                    final JsonElement addonName = object.get("name");
                    final JsonElement addonVersion = object.get("version");
                    final JsonElement addonHash = object.get("hash");
                    final JsonElement addonAuthor = object.get("author");
                    final JsonElement addonDescription = object.get("description");
                    final JsonElement addonCategory = object.get("category");
                    final JsonElement addonEnabled = object.get("enabled");
                    final JsonElement addonIncludeInJar = object.get("includeInJar");
                    final JsonElement addonRestart = object.get("restart");
                    final JsonElement addonVerified = object.get("verified");
                    int[] sorting = new int[this.categories.size()];
                    if (object.has("sorting")) {
                        final JsonArray addonSorting = object.get("sorting").getAsJsonArray();
                        sorting = new int[addonSorting.size()];
                        int index = 0;
                        for (final JsonElement element2 : addonSorting) {
                            sorting[index] = element2.getAsInt();
                            ++index;
                        }
                    }
                    if (addonUUID != null && addonName != null && addonVersion != null && addonHash != null && addonAuthor != null && addonDescription != null && addonCategory != null && addonEnabled != null && addonIncludeInJar != null && addonRestart != null && addonVerified != null) {
                        final AddonInfo addonInfo = new OnlineAddonInfo(UUID.fromString(addonUUID.getAsString()), addonName.getAsString(), addonVersion.getAsInt(), addonHash.getAsString(), addonAuthor.getAsString(), addonDescription.getAsString(), addonCategory.getAsInt(), addonEnabled.getAsBoolean(), addonIncludeInJar.getAsBoolean(), addonRestart.getAsBoolean(), addonVerified.getAsBoolean(), sorting);
                        final UUID uuid = addonInfo.getUuid();
                        addonInfoMap.put(uuid, addonInfo);
                        addonInfoList.add(addonInfo);
                        if (LabyModOFAddon.INSTALLED_VERSION != null && LabyModOFAddon.OPTIFINE_VERSIONS.containsValue(uuid)) {
                            addonInfo.setName(LabyModOFAddon.INSTALLED_VERSION);
                        }
                        AddonLoader.getOfflineAddons().remove(uuid);
                        Debug.log(Debug.EnumDebugMode.ADDON, "Resolved addon " + addonInfo.getName() + " " + addonInfo.getVersion() + " by " + addonInfo.getAuthor());
                    }
                }
            }
            this.addonInfoMap = addonInfoMap;
            this.addonInfoList = addonInfoList;
            for (final AddonInfo offlineAddon : AddonLoader.getOfflineAddons()) {
                this.addonInfoList.add(offlineAddon);
                this.addonInfoMap.put(offlineAddon.getUuid(), offlineAddon);
            }
            this.loaded = true;
        }
        catch (final Exception e) {
            Debug.log(Debug.EnumDebugMode.ADDON, "Failed getting available addons!");
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean deleteOptifine = false;
                    boolean deleteAction = false;
                    for (final LabyModAddon labyModAddon : AddonLoader.getAddons()) {
                        if (labyModAddon.about.deleted) {
                            deleteAction = true;
                        }
                    }
                    if (deleteAction) {
                        final File file = AddonLoader.getDeleteQueueFile();
                        final FileWriter fileWriter = new FileWriter(file, true);
                        try {
                            final Path path = file.toPath();
                            Files.setAttribute(path, "dos:hidden", true, new LinkOption[0]);
                        }
                        catch (final Exception error) {
                            error.printStackTrace();
                        }
                        for (final LabyModAddon labyModAddon2 : AddonLoader.getAddons()) {
                            if (!labyModAddon2.about.deleted) {
                                continue;
                            }
                            if (labyModAddon2 instanceof LabyModOFAddon) {
                                deleteOptifine = true;
                            }
                            else {
                                final File foundFile = AddonLoader.getFiles().get(labyModAddon2.about.uuid);
                                if (foundFile != null && foundFile.exists()) {
                                    fileWriter.write(String.valueOf(foundFile.getName()) + "\n");
                                }
                                else {
                                    Debug.log(Debug.EnumDebugMode.ADDON, "Error while adding " + labyModAddon2.about.name + " to delete queue");
                                }
                            }
                        }
                        fileWriter.close();
                    }
                    if (deleteOptifine) {
                        LabyModOFAddon.executeOfHandler(false);
                    }
                    else if (LabyModOFAddon.INSTALL) {
                        LabyModOFAddon.executeOfHandler(true);
                    }
                }
                catch (final Exception error2) {
                    error2.printStackTrace();
                }
            }
        }));
    }
    
    public Map<UUID, AddonInfo> getAddonInfoMap() {
        return this.addonInfoMap;
    }
    
    public List<AddonInfo> getAddonInfoList() {
        return this.addonInfoList;
    }
    
    public List<String> getCategories() {
        return this.categories;
    }
    
    public CheckBox[] getCategorieCheckboxList() {
        return this.categorieCheckboxList;
    }
    
    public boolean isLoaded() {
        return this.loaded;
    }
}
