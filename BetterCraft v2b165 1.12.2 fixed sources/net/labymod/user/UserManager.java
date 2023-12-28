// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user;

import java.io.InputStream;
import java.util.zip.Inflater;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Scanner;
import java.net.URL;
import java.net.HttpURLConnection;
import net.labymod.user.util.CosmeticData;
import net.labymod.user.util.EnumUserRank;
import net.labymod.user.util.UserResolvedCallback;
import net.labymod.utils.Consumer;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import net.labymod.utils.ModColor;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import net.labymod.main.LabyMod;
import com.google.gson.JsonParser;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class UserManager
{
    private ExecutorService executorService;
    private Map<UUID, User> users;
    private boolean whitelistLoaded;
    private boolean subTitlesModified;
    private List<Long> whitelistedUsers;
    private Map<UUID, Boolean> checkedUsers;
    private Map<Integer, Class<?>> cosmeticIdToClassData;
    private JsonParser jsonParser;
    private FamiliarManager familiarManager;
    private LabyMod labyMod;
    
    public UserManager(final LabyMod labyMod) {
        this.executorService = Executors.newFixedThreadPool(5);
        this.users = new HashMap<UUID, User>();
        this.whitelistLoaded = false;
        this.subTitlesModified = false;
        this.whitelistedUsers = new ArrayList<Long>();
        this.checkedUsers = new HashMap<UUID, Boolean>();
        this.cosmeticIdToClassData = new HashMap<Integer, Class<?>>();
        this.jsonParser = new JsonParser();
        this.labyMod = labyMod;
        this.familiarManager = new FamiliarManager(labyMod);
    }
    
    public String getUserAgent() {
        return "LabyMod " + this.labyMod.getLabyModVersion() + ("".isEmpty() ? "" : " ") + " on mc1.8.9";
    }
    
    public void onServerMessage(final String messageKey, final JsonElement serverMessage) {
        if (messageKey.equals("account_subtitle")) {
            this.subTitlesModified = true;
            try {
                final JsonArray jsonarray = serverMessage.getAsJsonArray();
                for (int i = 0; i < jsonarray.size(); ++i) {
                    final JsonObject jsonobject = jsonarray.get(i).getAsJsonObject();
                    if (jsonobject.has("uuid")) {
                        final UUID uuid = UUID.fromString(jsonobject.get("uuid").getAsString());
                        final User user = this.getUser(uuid);
                        final boolean flag = user.getSubTitle() != null;
                        final String string;
                        String s = string = (jsonobject.has("value") ? jsonobject.get("value").getAsString() : null);
                        if (s != null) {
                            s = ModColor.createColors(s);
                        }
                        final double d2;
                        double d0 = d2 = (jsonobject.has("size") ? jsonobject.get("size").getAsDouble() : 0.5);
                        if (d0 < 0.8) {
                            d0 = 0.8;
                        }
                        if (d0 > 1.6) {
                            d0 = 1.6;
                        }
                        user.setSubTitle(s);
                        user.setSubTitleSize(d0);
                        if (flag) {}
                    }
                }
            }
            catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    
    public void init(final UUID clientUUID, final Consumer<Boolean> consumer) {
        this.loadWhitelist(new Consumer<Integer>() {
            @Override
            public void accept(final Integer accepted) {
                UserManager.this.setChecked(clientUUID, false);
                if (clientUUID != null && UserManager.this.containsInCSV(clientUUID)) {
                    UserManager.this.getUserDataOf(clientUUID, new Consumer<User>() {
                        @Override
                        public void accept(final User user) {
                            UserManager.this.setChecked(clientUUID, !user.getCosmetics().isEmpty() || !user.getEmotes().isEmpty());
                            consumer.accept(true);
                        }
                    });
                }
                else {
                    consumer.accept(accepted != 0);
                }
            }
        });
    }
    
    public User getUser(final UUID uuid) {
        User user = this.users.get(uuid);
        if (user == null) {
            user = new User(uuid, this.labyMod);
            this.users.put(uuid, user);
        }
        return user;
    }
    
    public boolean isWhitelisted(final UUID uuid) {
        if (!this.whitelistLoaded) {
            return false;
        }
        final Boolean obool = this.checkedUsers.get(uuid);
        if (obool == null) {
            this.checkedUsers.put(uuid, false);
            if (this.containsInCSV(uuid)) {
                this.getUserDataOf(uuid, new Consumer<User>() {
                    @Override
                    public void accept(final User user) {
                        UserManager.this.setChecked(uuid, !user.getCosmetics().isEmpty() || !user.getEmotes().isEmpty());
                    }
                });
            }
            return false;
        }
        return obool;
    }
    
    private boolean containsInCSV(final UUID uuid) {
        final long i = uuid.getMostSignificantBits() >> 32 & 0xFFFFFFFFL;
        return this.whitelistedUsers.contains(i);
    }
    
    private void getUserDataOf(final UUID uuid, final Consumer<User> callback) {
        try {
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final User user = UserManager.this.getUser(uuid);
                        UserManager.this.loadUserData(user, new UserResolvedCallback() {
                            @Override
                            public void resolvedRankVisibility(final boolean visible) {
                                user.setRankVisible(visible);
                            }
                            
                            @Override
                            public void resolvedEnumRank(final EnumUserRank enumUserRank) {
                                user.setRank(enumUserRank);
                            }
                            
                            @Override
                            public void resolvedCosmetics(final Map<Integer, CosmeticData> cosmetics) {
                                user.setCosmetics(cosmetics);
                            }
                            
                            @Override
                            public void complete() {
                                if (callback != null) {
                                    callback.accept(user);
                                }
                            }
                        });
                    }
                    catch (final Exception exception1) {
                        exception1.printStackTrace();
                    }
                }
            });
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void updateUsersJson(final UUID uuid, final String json, final Consumer<Boolean> callback) {
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final User user = UserManager.this.getUser(uuid);
                    UserManager.this.handleJsonString(user, json, new UserResolvedCallback() {
                        @Override
                        public void resolvedRankVisibility(final boolean visible) {
                            user.setRankVisible(visible);
                        }
                        
                        @Override
                        public void resolvedEnumRank(final EnumUserRank enumUserRank) {
                            user.setRank(enumUserRank);
                        }
                        
                        @Override
                        public void resolvedCosmetics(final Map<Integer, CosmeticData> cosmetics) {
                            user.setCosmetics(cosmetics);
                        }
                        
                        @Override
                        public void complete() {
                            if (callback != null) {
                                callback.accept(true);
                            }
                        }
                    });
                }
                catch (final Exception exception) {
                    exception.printStackTrace();
                    if (callback != null) {
                        callback.accept(false);
                    }
                }
            }
        });
    }
    
    private void loadUserData(final User user, final UserResolvedCallback callback) throws Exception {
        final HttpURLConnection httpurlconnection = (HttpURLConnection)new URL(String.format("http://dl.labymod.net/userdata/%s.json", user.getUuid().toString())).openConnection();
        httpurlconnection.setRequestProperty("User-Agent", this.getUserAgent());
        httpurlconnection.setReadTimeout(5000);
        httpurlconnection.setConnectTimeout(2000);
        httpurlconnection.connect();
        final int i = httpurlconnection.getResponseCode();
        if (i / 100 == 2) {
            String s = "";
            final Scanner scanner = new Scanner(httpurlconnection.getInputStream());
            while (scanner.hasNext()) {
                s = String.valueOf(String.valueOf(s)) + scanner.next();
            }
            scanner.close();
            this.handleJsonString(user, s, callback);
        }
        callback.complete();
    }
    
    private void handleJsonString(final User user, final String jsonString, final UserResolvedCallback callback) throws Exception {
        final boolean flag = user.getUuid().equals(this.labyMod.getPlayerUUID());
        final JsonElement jsonelement = this.jsonParser.parse(jsonString);
        final JsonObject jsonobject = jsonelement.getAsJsonObject();
        if (jsonobject.has("c")) {
            final JsonArray jsonarray = jsonobject.get("c").getAsJsonArray();
            final Iterator iterator = jsonarray.iterator();
            final HashMap<Integer, CosmeticData> map = new HashMap<Integer, CosmeticData>();
            while (iterator.hasNext()) {
                final JsonObject jsonobject2 = iterator.next().getAsJsonObject();
                if (!jsonobject2.has("i")) {
                    continue;
                }
                final int i = jsonobject2.get("i").getAsInt();
                if (!jsonobject2.has("d")) {
                    continue;
                }
                final JsonArray jsonarray2 = jsonobject2.get("d").getAsJsonArray();
                final Iterator iterator2 = jsonarray2.iterator();
                final ArrayList<String> list = new ArrayList<String>();
                while (iterator2.hasNext()) {
                    final JsonElement jsonelement2 = iterator2.next();
                    if (jsonelement2.isJsonNull()) {
                        continue;
                    }
                    final String s = jsonelement2.getAsString();
                    list.add(s);
                }
                final Class<?> oclass = this.cosmeticIdToClassData.get(i);
                if (oclass == null) {
                    continue;
                }
                final CosmeticData cosmeticdata1 = (CosmeticData)oclass.newInstance();
                map.put(i, cosmeticdata1);
                try {
                    cosmeticdata1.init(user);
                    final String[] astring = list.toArray(new String[list.size()]);
                    if (astring.length == 0) {
                        continue;
                    }
                    cosmeticdata1.loadData(astring);
                }
                catch (final Exception exception) {
                    exception.printStackTrace();
                }
            }
            callback.resolvedCosmetics(map);
            for (final CosmeticData cosmeticdata2 : map.values()) {
                cosmeticdata2.completed(user);
            }
        }
        if (jsonobject.has("e")) {
            final JsonArray jsonarray3 = jsonobject.get("e").getAsJsonArray();
            final Iterator iterator3 = jsonarray3.iterator();
            final ArrayList<Short> list2 = new ArrayList<Short>();
            while (iterator3.hasNext()) {
                final JsonElement jsonelement3 = iterator3.next();
                final short short1 = jsonelement3.getAsShort();
                list2.add(short1);
            }
            user.setEmotes(list2);
        }
        final JsonObject jsonobject3;
        if (jsonobject.has("r") && (jsonobject3 = jsonobject.get("r").getAsJsonObject()).has("i") && jsonobject3.has("v")) {
            final int j = jsonobject3.get("i").getAsInt();
            final boolean flag2 = jsonobject3.get("v").getAsBoolean();
            callback.resolvedRankVisibility(flag2);
            final EnumUserRank enumuserrank = EnumUserRank.getById(j);
            if (enumuserrank != null) {
                callback.resolvedEnumRank(enumuserrank);
            }
        }
        callback.complete();
    }
    
    public void loadWhitelist(final Consumer<Integer> callback) {
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final HttpURLConnection httpurlconnection = (HttpURLConnection)new URL("http://dl.labymod.net/whitelist.bin").openConnection();
                    httpurlconnection.setRequestProperty("User-Agent", UserManager.this.getUserAgent());
                    httpurlconnection.setReadTimeout(5000);
                    httpurlconnection.setConnectTimeout(2000);
                    httpurlconnection.connect();
                    final int i = httpurlconnection.getResponseCode();
                    if (i / 100 == 2) {
                        final InputStream inputstream = httpurlconnection.getInputStream();
                        final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                        final byte[] abyte = new byte[4096];
                        int j;
                        while ((j = inputstream.read(abyte)) != -1) {
                            bytearrayoutputstream.write(abyte, 0, j);
                        }
                        inputstream.close();
                        bytearrayoutputstream.close();
                        final byte[] abyte2 = bytearrayoutputstream.toByteArray();
                        final Inflater inflater = new Inflater();
                        inflater.setInput(abyte2);
                        final ByteArrayOutputStream bytearrayoutputstream2 = new ByteArrayOutputStream(abyte2.length);
                        final byte[] abyte3 = new byte[1024];
                        while (!inflater.finished()) {
                            final int k = inflater.inflate(abyte3);
                            bytearrayoutputstream2.write(abyte3, 0, k);
                        }
                        bytearrayoutputstream2.close();
                        final byte[] abyte4 = bytearrayoutputstream2.toByteArray();
                        for (int l = 0; l < abyte4.length; l += 8) {
                            long i2 = 0L;
                            for (int j2 = 0; j2 < 8; ++j2) {
                                i2 += ((long)abyte4[l + j2] & 0xFFL) << 8 * j2;
                            }
                            UserManager.this.whitelistedUsers.add(i2);
                        }
                    }
                    UserManager.access$5(UserManager.this, true);
                    callback.accept(UserManager.this.whitelistedUsers.size());
                }
                catch (final Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
    
    public void setChecked(final UUID uuid, final boolean value) {
        this.checkedUsers.put(uuid, value);
    }
    
    public void removeCheckedUser(final UUID uuid) {
        this.checkedUsers.remove(uuid);
    }
    
    public void clearCache() {
        this.whitelistLoaded = false;
        this.users.clear();
        this.whitelistedUsers.clear();
        this.checkedUsers.clear();
    }
    
    public Map<UUID, User> getUsers() {
        return this.users;
    }
    
    public FamiliarManager getFamiliarManager() {
        return this.familiarManager;
    }
    
    static void access$5(final UserManager userManager, final boolean whitelistLoaded) {
        userManager.whitelistLoaded = whitelistLoaded;
    }
}
