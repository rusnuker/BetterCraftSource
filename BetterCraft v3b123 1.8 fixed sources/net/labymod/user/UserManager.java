// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user;

import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketActionPlay;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.FutureCallback;
import net.labymod.core.LabyModCore;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.Minecraft;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.io.ByteArrayOutputStream;
import java.util.Scanner;
import java.net.URL;
import java.net.HttpURLConnection;
import net.labymod.user.group.LabyGroup;
import net.labymod.user.util.UserResolvedCallback;
import net.labymod.utils.Consumer;
import net.minecraft.network.PacketBuffer;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import net.labymod.utils.ModColor;
import com.google.gson.JsonElement;
import java.util.Iterator;
import net.labymod.main.LabyMod;
import net.labymod.support.util.Debug;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.labymod.user.cosmetic.CosmeticRenderer;
import net.labymod.main.Source;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import net.labymod.labyconnect.packets.PacketActionPlayResponse;
import net.labymod.user.util.FutureMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.user.group.GroupManager;
import net.labymod.user.cosmetic.util.CosmeticClassLoader;
import net.labymod.user.gui.UserActionGui;
import net.labymod.user.cosmetic.CosmeticImageManager;
import com.google.gson.JsonParser;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import net.labymod.api.events.PluginMessageEvent;
import net.labymod.api.events.ServerMessageEvent;

public class UserManager implements ServerMessageEvent, PluginMessageEvent
{
    private ExecutorService executorService;
    private Map<UUID, User> users;
    private boolean whitelistLoaded;
    private boolean subTitlesModified;
    private List<Long> whitelistedUsers;
    private Map<UUID, Boolean> checkedUsers;
    private Map<Integer, Class<?>> cosmeticIdToClassData;
    private JsonParser jsonParser;
    private CosmeticImageManager cosmeticImageManager;
    private FamiliarManager familiarManager;
    private UserActionGui userActionGui;
    private CosmeticClassLoader cosmeticClassLoader;
    private GroupManager groupManager;
    protected AtomicInteger currentRequestId;
    protected FutureMap<Short, PacketActionPlayResponse> responseFutureMap;
    protected boolean lastSpamProtectedLegState;
    
    public UserManager() {
        this.executorService = Executors.newFixedThreadPool(5);
        this.users = new HashMap<UUID, User>();
        this.whitelistLoaded = false;
        this.subTitlesModified = false;
        this.whitelistedUsers = new ArrayList<Long>();
        this.checkedUsers = new HashMap<UUID, Boolean>();
        this.cosmeticIdToClassData = new HashMap<Integer, Class<?>>();
        this.jsonParser = new JsonParser();
        this.cosmeticImageManager = new CosmeticImageManager(Source.getUserAgent());
        this.familiarManager = new FamiliarManager();
        this.userActionGui = new UserActionGui(this);
        this.cosmeticClassLoader = new CosmeticClassLoader();
        this.groupManager = new GroupManager(this.executorService);
        this.currentRequestId = new AtomicInteger(-32768);
        this.responseFutureMap = new FutureMap<Short, PacketActionPlayResponse>(requestId -> {}, 1000L, null);
        this.lastSpamProtectedLegState = false;
        try {
            for (final Class<?> loadedClassInfo : this.cosmeticClassLoader.getCosmeticClasses()) {
                final CosmeticRenderer<CosmeticData> cosmeticRenderer = (CosmeticRenderer<CosmeticData>)loadedClassInfo.newInstance();
                Class<?>[] classes;
                for (int length = (classes = loadedClassInfo.getClasses()).length, i = 0; i < length; ++i) {
                    final Class<?> subClasses = classes[i];
                    if (CosmeticData.class.isAssignableFrom(subClasses)) {
                        this.cosmeticIdToClassData.put(cosmeticRenderer.getCosmeticId(), subClasses);
                    }
                }
            }
            Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Registered " + this.cosmeticIdToClassData.size() + " cosmetics!");
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        LabyMod.getInstance().getEventManager().register((ServerMessageEvent)this);
        LabyMod.getInstance().getEventManager().register((PluginMessageEvent)this);
    }
    
    @Override
    public void onServerMessage(final String messageKey, final JsonElement serverMessage) {
        if (messageKey.equals("account_subtitle")) {
            this.subTitlesModified = true;
            try {
                final JsonArray jsonArray = serverMessage.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); ++i) {
                    final JsonObject accountEntry = jsonArray.get(i).getAsJsonObject();
                    if (accountEntry.has("uuid")) {
                        final UUID uuid = UUID.fromString(accountEntry.get("uuid").getAsString());
                        final User user = this.getUser(uuid);
                        final boolean prevHasSubTitle = user.getSubTitle() != null;
                        String subTitle = accountEntry.has("value") ? accountEntry.get("value").getAsString() : null;
                        if (subTitle != null) {
                            subTitle = ModColor.createColors(subTitle);
                        }
                        double subTitleSize = accountEntry.has("size") ? accountEntry.get("size").getAsDouble() : 0.5;
                        if (subTitleSize < 0.8) {
                            subTitleSize = 0.8;
                        }
                        if (subTitleSize > 1.6) {
                            subTitleSize = 1.6;
                        }
                        user.setSubTitle(subTitle);
                        user.setSubTitleSize(subTitleSize);
                        if (!prevHasSubTitle && subTitle != null) {
                            Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Added subtitle for " + uuid.toString() + ": " + subTitle);
                        }
                        else if (prevHasSubTitle && subTitle == null) {
                            Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Removed subtitle of " + uuid.toString() + "!");
                        }
                        else {
                            Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Updated subtitle of " + uuid.toString() + " to " + subTitle);
                        }
                    }
                }
            }
            catch (final Exception error) {
                error.printStackTrace();
            }
        }
    }
    
    @Override
    public void receiveMessage(final String channelName, final PacketBuffer packetBuffer) {
        if (this.subTitlesModified && channelName.equals("MC|Brand")) {
            for (final User user : this.users.values()) {
                user.setSubTitle(null);
            }
            this.subTitlesModified = false;
        }
    }
    
    public void init(final UUID clientUUID, final Consumer<Boolean> consumer) {
        this.loadWhitelist(new Consumer<Integer>() {
            @Override
            public void accept(final Integer accepted) {
                Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Loaded " + accepted + " whitelisted users.");
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
            this.users.put(uuid, user = new User(uuid));
        }
        return user;
    }
    
    public boolean isWhitelisted(final UUID uuid) {
        if (!this.whitelistLoaded) {
            return false;
        }
        final Boolean result = this.checkedUsers.get(uuid);
        if (result == null) {
            this.checkedUsers.put(uuid, false);
            if (this.containsInCSV(uuid)) {
                this.getUserDataOf(uuid, new Consumer<User>() {
                    @Override
                    public void accept(final User user) {
                        UserManager.this.setChecked(uuid, true);
                    }
                });
            }
            return false;
        }
        return result;
    }
    
    private boolean containsInCSV(final UUID uuid) {
        final long uuidPart = uuid.getMostSignificantBits() >> 32 & 0xFFFFFFFFL;
        return this.whitelistedUsers.contains(uuidPart);
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
                            public void resolvedGroup(final LabyGroup group) {
                                user.setGroup(group);
                            }
                            
                            @Override
                            public void resolvedCosmetics(final Map<Integer, CosmeticData> cosmetics) {
                                user.setCosmetics(cosmetics);
                            }
                            
                            @Override
                            public void resolvedDailyEmoteFlat(final boolean value) {
                                user.setDailyEmoteFlat(value);
                            }
                            
                            @Override
                            public void complete() {
                                if (callback != null) {
                                    callback.accept(user);
                                }
                            }
                        });
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (final Exception error) {
            Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Error while resolving user data of " + uuid.toString() + " (" + error.getMessage() + ")");
            error.printStackTrace();
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
                        public void resolvedGroup(final LabyGroup group) {
                            user.setGroup(group);
                        }
                        
                        @Override
                        public void resolvedCosmetics(final Map<Integer, CosmeticData> cosmetics) {
                            user.setCosmetics(cosmetics);
                        }
                        
                        @Override
                        public void resolvedDailyEmoteFlat(final boolean value) {
                            user.setDailyEmoteFlat(value);
                        }
                        
                        @Override
                        public void complete() {
                            if (callback != null) {
                                callback.accept(true);
                            }
                        }
                    });
                }
                catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.accept(false);
                    }
                }
            }
        });
    }
    
    private void loadUserData(final User user, final UserResolvedCallback callback) throws Exception {
        final HttpURLConnection connection = (HttpURLConnection)new URL(String.format("http://dl.labymod.net/userdata/%s.json", user.getUuid().toString())).openConnection();
        Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Load user data of " + user.getUuid().toString());
        connection.setRequestProperty("User-Agent", Source.getUserAgent());
        connection.setReadTimeout(5000);
        connection.setConnectTimeout(2000);
        connection.connect();
        final int responseCode = connection.getResponseCode();
        if (responseCode / 100 == 2) {
            String jsonString = "";
            final Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                jsonString = String.valueOf(jsonString) + scanner.next();
            }
            scanner.close();
            this.handleJsonString(user, jsonString, callback);
        }
        else {
            Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Response code for " + user.getUuid().toString() + " is " + responseCode);
        }
        callback.complete();
    }
    
    private void handleJsonString(final User user, final String jsonString, final UserResolvedCallback callback) throws Exception {
        final boolean isClient = user.getUuid().equals(LabyMod.getInstance().getPlayerUUID());
        if (isClient) {
            Debug.log(Debug.EnumDebugMode.USER_MANAGER, jsonString);
        }
        final JsonElement jsonElement = this.jsonParser.parse(jsonString);
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        try {
            if (jsonObject.has("c")) {
                final JsonArray jsonArray = jsonObject.get("c").getAsJsonArray();
                final Iterator<JsonElement> cosmeticIterator = jsonArray.iterator();
                final Map<Integer, CosmeticData> storedCosmeticData = new HashMap<Integer, CosmeticData>();
                while (cosmeticIterator.hasNext()) {
                    final JsonObject cosmeticJsonObject = cosmeticIterator.next().getAsJsonObject();
                    if (!cosmeticJsonObject.has("i")) {
                        Debug.log(Debug.EnumDebugMode.USER_MANAGER, String.valueOf(cosmeticJsonObject.toString()) + " has no id");
                    }
                    else {
                        final int id = cosmeticJsonObject.get("i").getAsInt();
                        if (!cosmeticJsonObject.has("d")) {
                            Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Cosmetic id " + id + " has no data (" + cosmeticJsonObject.toString() + ")");
                        }
                        else {
                            final JsonArray dataArray = cosmeticJsonObject.get("d").getAsJsonArray();
                            final Iterator<JsonElement> dataIterator = dataArray.iterator();
                            final List<String> dataList = new ArrayList<String>();
                            while (dataIterator.hasNext()) {
                                final JsonElement dataElement = dataIterator.next();
                                if (!dataElement.isJsonNull()) {
                                    final String dataString = dataElement.getAsString();
                                    dataList.add(dataString);
                                }
                            }
                            final Class<?> dataClass = this.cosmeticIdToClassData.get(id);
                            if (dataClass == null) {
                                Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Cosmetic id " + id + " not found in cosmeticIdToClassData (size=" + this.cosmeticIdToClassData.size() + ")");
                            }
                            else {
                                final CosmeticData cosmeticData = (CosmeticData)dataClass.newInstance();
                                storedCosmeticData.put(id, cosmeticData);
                                try {
                                    cosmeticData.init(user);
                                    final String[] array = dataList.toArray(new String[dataList.size()]);
                                    if (array.length != 0) {
                                        cosmeticData.loadData(array);
                                    }
                                    if (isClient) {
                                        Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Loaded cosmetic " + id + " for client");
                                    }
                                }
                                catch (final Exception error) {
                                    Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Parse error while loading " + dataClass.getSimpleName() + ": " + error.getMessage());
                                }
                                if (id == 0) {
                                    user.getCloakContainer().resolved();
                                }
                                if (id == 22) {
                                    user.getBandanaContainer().resolved();
                                }
                                if (id == 27) {
                                    user.getShoesContainer().resolved();
                                }
                                if (id == 34) {
                                    user.getKawaiiMaskContainer().resolved();
                                }
                                if (id == 31) {
                                    user.getCoverMaskContainer().resolved();
                                }
                                if (id == 33) {
                                    user.getWatchContainer().resolved();
                                }
                                if (id != 24) {
                                    continue;
                                }
                                user.getAngelWingsContainer().resolved();
                            }
                        }
                    }
                }
                callback.resolvedCosmetics(storedCosmeticData);
                for (final CosmeticData data : storedCosmeticData.values()) {
                    data.completed(user);
                }
            }
            if (jsonObject.has("e")) {
                final JsonArray emoteArray = jsonObject.get("e").getAsJsonArray();
                final Iterator<JsonElement> emoteIterator = emoteArray.iterator();
                final List<Short> emotes = new ArrayList<Short>();
                while (emoteIterator.hasNext()) {
                    final JsonElement element = emoteIterator.next();
                    final short emoteId = element.getAsShort();
                    emotes.add(emoteId);
                }
                user.setEmotes(emotes);
            }
            if (jsonObject.has("st") || jsonObject.has("s")) {
                final JsonObject stickerObject = (jsonObject.has("s") ? jsonObject.get("s") : jsonObject.get("st")).getAsJsonObject();
                final Iterator<JsonElement> packsArray = stickerObject.get("p").getAsJsonArray().iterator();
                final List<Short> packs = new ArrayList<Short>();
                while (packsArray.hasNext()) {
                    final short packId = packsArray.next().getAsShort();
                    packs.add(packId);
                }
                user.setStickerPacks(packs);
            }
            if (jsonObject.has("g")) {
                final JsonArray groupArray = jsonObject.get("g").getAsJsonArray();
                if (groupArray.size() > 0) {
                    final JsonObject group = groupArray.get(0).getAsJsonObject();
                    final short id2 = group.get("i").getAsShort();
                    final LabyGroup labyGroup = this.groupManager.getGroupById(id2);
                    if (labyGroup != null) {
                        callback.resolvedGroup(labyGroup);
                    }
                }
            }
            if (jsonObject.has("f")) {
                final JsonObject flatObject = jsonObject.get("f").getAsJsonObject();
                if (flatObject.has("e")) {
                    callback.resolvedDailyEmoteFlat(flatObject.get("e").getAsBoolean());
                }
            }
        }
        catch (final Exception error2) {
            error2.printStackTrace();
        }
        callback.complete();
    }
    
    public void loadWhitelist(final Consumer<Integer> callback) {
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                Debug.log(Debug.EnumDebugMode.USER_MANAGER, "Load whitelist..");
                try {
                    final HttpURLConnection connection = (HttpURLConnection)new URL("http://dl.labymod.net/whitelist.bin").openConnection();
                    connection.setRequestProperty("User-Agent", Source.getUserAgent());
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(2000);
                    connection.connect();
                    final int responseCode = connection.getResponseCode();
                    if (responseCode / 100 == 2) {
                        final InputStream is = connection.getInputStream();
                        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        final byte[] buffer = new byte[4096];
                        int size;
                        while ((size = is.read(buffer)) != -1) {
                            baos.write(buffer, 0, size);
                        }
                        is.close();
                        baos.close();
                        final byte[] compressedBytes = baos.toByteArray();
                        final Inflater inflater = new Inflater();
                        inflater.setInput(compressedBytes);
                        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedBytes.length);
                        final byte[] buffer2 = new byte[1024];
                        while (!inflater.finished()) {
                            final int count = inflater.inflate(buffer2);
                            outputStream.write(buffer2, 0, count);
                        }
                        outputStream.close();
                        final byte[] decompressedBytes = outputStream.toByteArray();
                        for (int b = 0; b < decompressedBytes.length; b += 8) {
                            long uuidPart = 0L;
                            for (int i = 0; i < 8; ++i) {
                                uuidPart += ((long)decompressedBytes[b + i] & 0xFFL) << 8 * i;
                            }
                            UserManager.this.whitelistedUsers.add(uuidPart);
                        }
                    }
                    UserManager.access$5(UserManager.this, true);
                    callback.accept(UserManager.this.whitelistedUsers.size());
                }
                catch (final Exception error) {
                    error.printStackTrace();
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
        this.cosmeticImageManager.unloadUnusedTextures(true, true);
    }
    
    public void broadcastBitUpdate(final boolean bit) {
        final GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        int i = 0;
        for (final EnumPlayerModelParts enumplayermodelparts : gameSettings.getModelParts()) {
            i |= enumplayermodelparts.getPartMask();
        }
        final boolean spamProtection = LabyMod.getInstance().isServerHasEmoteSpamProtection();
        if (bit) {
            if (spamProtection && this.lastSpamProtectedLegState) {
                i ^= 0x10;
            }
            this.sendBitMask(i ^ 0x90);
            if (spamProtection) {
                this.lastSpamProtectedLegState = !this.lastSpamProtectedLegState;
            }
            else {
                this.sendBitMask(i ^ 0x80);
            }
        }
        else if (!spamProtection) {
            this.sendBitMask(i);
        }
    }
    
    private void sendBitMask(final int bitMask) {
        final GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        LabyModCore.getMinecraft().sendClientSettings(gameSettings.language, gameSettings.renderDistanceChunks, gameSettings.chatVisibility, gameSettings.chatColours, bitMask);
    }
    
    public void requestAction(final short id, final byte[] bytes, final FutureCallback<PacketActionPlayResponse> callback) {
        int requestId = 0;
        if ((requestId = this.currentRequestId.incrementAndGet()) > 32767) {
            this.currentRequestId.set(-32768);
            requestId = -32768;
        }
        Futures.addCallback(this.responseFutureMap.get((short)requestId), callback);
        LabyMod.getInstance().getLabyConnect().getClientConnection().sendPacket(new PacketActionPlay((short)requestId, id, bytes));
    }
    
    public void resolveAction(final short requestId, final PacketActionPlayResponse packetActionPlayResponse) {
        this.responseFutureMap.resolve(requestId, packetActionPlayResponse);
    }
    
    public Map<UUID, User> getUsers() {
        return this.users;
    }
    
    public CosmeticImageManager getCosmeticImageManager() {
        return this.cosmeticImageManager;
    }
    
    public FamiliarManager getFamiliarManager() {
        return this.familiarManager;
    }
    
    public UserActionGui getUserActionGui() {
        return this.userActionGui;
    }
    
    public CosmeticClassLoader getCosmeticClassLoader() {
        return this.cosmeticClassLoader;
    }
    
    public GroupManager getGroupManager() {
        return this.groupManager;
    }
    
    static /* synthetic */ void access$5(final UserManager userManager, final boolean whitelistLoaded) {
        userManager.whitelistLoaded = whitelistLoaded;
    }
}
