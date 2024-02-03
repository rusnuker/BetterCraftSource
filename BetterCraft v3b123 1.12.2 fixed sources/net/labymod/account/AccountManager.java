// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.account;

import net.minecraft.util.Session;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.io.FileOutputStream;
import net.labymod.utils.Consumer;
import java.util.Iterator;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import com.google.gson.JsonElement;
import java.util.Map;
import net.labymod.utils.JsonParse;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.GsonBuilder;
import java.io.File;

public class AccountManager
{
    private static final File launcherProfilesFile;
    private GsonBuilder gsonBuilder;
    private AuthError lastErrorType;
    private String lastErrorMessage;
    private List<LauncherProfile> launcherProfiles;
    private AccountLoginHandler accountLoginHandler;
    private String clientToken;
    
    static {
        launcherProfilesFile = new File(MCBaseFolder.getWorkingDirectory(), "launcher_profiles.json");
    }
    
    public AccountManager() {
        this.gsonBuilder = new GsonBuilder().setPrettyPrinting();
        this.launcherProfiles = new ArrayList<LauncherProfile>();
        this.accountLoginHandler = new AccountLoginHandler(this);
        this.loadLauncherProfiles();
    }
    
    public void loadLauncherProfiles() {
        if (!AccountManager.launcherProfilesFile.exists()) {
            this.throwError(AuthError.NO_FILE, new String[0]);
            return;
        }
        final List<LauncherProfile> launcherProfiles = new ArrayList<LauncherProfile>();
        try {
            final JsonObject mainObject = JsonParse.parse(IOUtils.toString(new FileInputStream(AccountManager.launcherProfilesFile))).getAsJsonObject();
            final int profilesFormat = mainObject.has("launcherVersion") ? mainObject.get("launcherVersion").getAsJsonObject().get("profilesFormat").getAsInt() : 1;
            if (mainObject.has("authenticationDatabase") && mainObject.has("clientToken")) {
                final JsonObject authDatabase = mainObject.getAsJsonObject("authenticationDatabase");
                this.clientToken = mainObject.get("clientToken").getAsString();
                for (final Map.Entry<String, JsonElement> authEntry : authDatabase.entrySet()) {
                    try {
                        final JsonObject currentProfile = authEntry.getValue().getAsJsonObject();
                        if (currentProfile == null) {
                            continue;
                        }
                        final String accessToken = currentProfile.has("accessToken") ? currentProfile.get("accessToken").getAsString() : "";
                        final String mojangUsername = currentProfile.get("username").getAsString();
                        String userId = authEntry.getKey();
                        GameProfile gameProfile = null;
                        if (profilesFormat == 1) {
                            final String minecraftName = currentProfile.get("displayName").getAsString();
                            final UUID minecraftUUID = UUID.fromString(currentProfile.get("uuid").getAsString());
                            userId = currentProfile.get("userid").getAsString();
                            gameProfile = new GameProfile(minecraftUUID, minecraftName);
                        }
                        else {
                            final JsonObject profilesObject = currentProfile.get("profiles").getAsJsonObject();
                            JsonObject mcProfile = null;
                            String id = null;
                            final Iterator<Map.Entry<String, JsonElement>> iterator2 = profilesObject.entrySet().iterator();
                            if (iterator2.hasNext()) {
                                final Map.Entry<String, JsonElement> profileEntry = iterator2.next();
                                id = profileEntry.getKey();
                                mcProfile = profileEntry.getValue().getAsJsonObject();
                            }
                            final UUID minecraftUUID2 = UUID.fromString(String.valueOf(id.substring(0, 8)) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
                            final String minecraftName2 = mcProfile.get("displayName").getAsString();
                            gameProfile = new GameProfile(minecraftUUID2, minecraftName2);
                        }
                        final LauncherProfile launcherProfile = new LauncherProfile(mojangUsername, userId, gameProfile, accessToken);
                        launcherProfiles.add(launcherProfile);
                    }
                    catch (final Exception error) {
                        error.printStackTrace();
                    }
                }
            }
            else {
                this.throwError(AuthError.INVALID_CONTENTS, new String[0]);
            }
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            this.throwError(AuthError.EXCEPTION, ex.getMessage());
        }
        this.launcherProfiles = launcherProfiles;
    }
    
    public void saveLauncherProfiles(final Consumer<Boolean> response) {
        if (!AccountManager.launcherProfilesFile.exists()) {
            response.accept(false);
            this.throwError(AuthError.NO_FILE, new String[0]);
            return;
        }
        try {
            final JsonObject mainObject = JsonParse.parse(IOUtils.toString(new FileInputStream(AccountManager.launcherProfilesFile))).getAsJsonObject();
            final int profilesFormat = mainObject.has("launcherVersion") ? mainObject.get("launcherVersion").getAsJsonObject().get("profilesFormat").getAsInt() : 1;
            if (mainObject.has("authenticationDatabase") && mainObject.has("clientToken")) {
                final JsonObject authenticationDatabase = new JsonObject();
                for (final LauncherProfile launcherProfile : this.launcherProfiles) {
                    if (profilesFormat == 1) {
                        final JsonObject userEntry = new JsonObject();
                        userEntry.addProperty("displayName", launcherProfile.getGameProfile().getName());
                        userEntry.addProperty("accessToken", launcherProfile.getAccessToken());
                        userEntry.addProperty("userid", launcherProfile.getUserid());
                        userEntry.addProperty("uuid", launcherProfile.getGameProfile().getId().toString());
                        userEntry.addProperty("username", launcherProfile.getMojangUsername());
                        authenticationDatabase.add(launcherProfile.getGameProfile().getId().toString().replaceAll("-", ""), userEntry);
                    }
                    else {
                        final JsonObject userEntry = new JsonObject();
                        userEntry.addProperty("accessToken", launcherProfile.getAccessToken());
                        userEntry.addProperty("username", launcherProfile.getMojangUsername());
                        final JsonObject profileEntry = new JsonObject();
                        final JsonObject gameProfileEntry = new JsonObject();
                        gameProfileEntry.addProperty("displayName", launcherProfile.getGameProfile().getName());
                        profileEntry.add(launcherProfile.getGameProfile().getId().toString().replaceAll("-", ""), gameProfileEntry);
                        userEntry.add("profiles", profileEntry);
                        authenticationDatabase.add(launcherProfile.getUserid(), userEntry);
                    }
                }
                mainObject.add("authenticationDatabase", authenticationDatabase);
                IOUtils.write(this.gsonBuilder.create().toJson(mainObject), new FileOutputStream(AccountManager.launcherProfilesFile), Charset.forName("UTF-8"));
                response.accept(true);
            }
            else {
                response.accept(false);
                this.throwError(AuthError.INVALID_CONTENTS, new String[0]);
            }
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            response.accept(false);
            this.throwError(AuthError.EXCEPTION, ex.getMessage());
        }
    }
    
    public void addAccount(final String mojangUsername, final Session session) {
        final LauncherProfile launcherProfile = new LauncherProfile(mojangUsername, session.getProfile().getId().toString().replaceAll("-", ""), session.getProfile(), session.getToken());
        this.launcherProfiles.add(launcherProfile);
    }
    
    public void removeAccount(final UUID uuid) {
        final Iterator<LauncherProfile> iterator = this.launcherProfiles.iterator();
        while (iterator.hasNext()) {
            final LauncherProfile next = iterator.next();
            if (next.getGameProfile().getId().equals(uuid)) {
                iterator.remove();
            }
        }
    }
    
    public LauncherProfile getAccount(final UUID uuid) {
        for (final LauncherProfile next : this.launcherProfiles) {
            if (next.getGameProfile().getId().equals(uuid)) {
                return next;
            }
        }
        return null;
    }
    
    protected void throwError(final AuthError type, final String... format) {
        this.lastErrorType = type;
        this.lastErrorMessage = type.toMessage(format);
    }
    
    public String getLastErrorMessage() {
        return this.lastErrorMessage;
    }
    
    public List<LauncherProfile> getLauncherProfiles() {
        return this.launcherProfiles;
    }
    
    public void setLauncherProfiles(final List<LauncherProfile> launcherProfiles) {
        this.launcherProfiles = launcherProfiles;
    }
    
    public AccountLoginHandler getAccountLoginHandler() {
        return this.accountLoginHandler;
    }
    
    public String getClientToken() {
        return this.clientToken;
    }
}
