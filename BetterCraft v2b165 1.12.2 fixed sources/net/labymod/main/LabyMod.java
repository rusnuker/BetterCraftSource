// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main;

import com.mojang.authlib.GameProfile;
import net.labymod.utils.Consumer;
import net.labymod.labyconnect.packets.Protocol;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.util.UUID;
import net.labymod.labyconnect.Session;
import net.labymod.api.LabyModAPI;
import net.labymod.labyplay.LabyPlay;
import net.labymod.labyconnect.LabyConnect;
import net.labymod.utils.ServerData;
import net.labymod.user.UserManager;
import java.util.Random;

public class LabyMod
{
    private static LabyMod labyMod;
    private static Random random;
    private UserManager userManager;
    private boolean hasLeftHand;
    private ServerData currentServerData;
    private float partialTicks;
    private LabyConnect labyConnect;
    private LabyPlay labyPlay;
    protected String playerId;
    private LabyModAPI api;
    private String motd;
    private String labyModVersion;
    private Session session;
    private UUID mcLeaksUUID;
    
    static {
        LabyMod.random = new Random();
    }
    
    public LabyMod() {
        this.playerId = null;
        this.motd = "Lul";
        this.labyModVersion = "3.4.8";
    }
    
    public void login(final Session session) {
        this.session = session;
        (LabyMod.labyMod = this).initLabyMod();
    }
    
    public void login(final String email, final String password) throws AuthenticationException {
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString()).createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(email);
        auth.setPassword(password);
        auth.logIn();
        this.session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        this.initLabyMod();
    }
    
    public void login(final String email, final String password, final Proxy httpProxy) throws AuthenticationException {
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(httpProxy, UUID.randomUUID().toString()).createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(email);
        auth.setPassword(password);
        auth.logIn();
        this.session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        this.initLabyMod();
    }
    
    private void initLabyMod() {
        Protocol.INSTANCE = new Protocol(this);
        this.labyConnect = new LabyConnect(this);
        this.labyPlay = new LabyPlay(this);
        this.api = new LabyModAPI(this);
        (this.userManager = new UserManager(this)).init(this.getPlayerUUID(), new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean success) {
            }
        });
    }
    
    public UUID getPlayerUUID() {
        return this.session.getProfile().getId();
    }
    
    public String getLabyModVersion() {
        return this.labyModVersion;
    }
    
    public String getPlayerName() {
        return this.session.getUsername();
    }
    
    public Session getSession() {
        return this.session;
    }
    
    public void setLabyModVersion(final String labyModVersion) {
        this.labyModVersion = labyModVersion;
    }
    
    public boolean isInGame() {
        return true;
    }
    
    public static boolean isForge() {
        return true;
    }
    
    public String getMotd() {
        return this.motd;
    }
    
    public void setMotd(final String motd) {
        this.motd = motd;
    }
    
    public UserManager getUserManager() {
        return this.userManager;
    }
    
    public boolean isHasLeftHand() {
        return this.hasLeftHand;
    }
    
    public LabyModAPI getLabyModAPI() {
        return this.api;
    }
    
    public ServerData getCurrentServerData() {
        return this.currentServerData;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public LabyConnect getLabyConnect() {
        return this.labyConnect;
    }
    
    public LabyPlay getLabyPlay() {
        return this.labyPlay;
    }
    
    public GameProfile getGameProfile() {
        return this.session.getProfile();
    }
    
    public String getPlayerId() {
        return this.playerId;
    }
    
    public static Random getRandom() {
        return LabyMod.random;
    }
    
    public void setPartialTicks(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public static LabyMod getLabyMod() {
        return LabyMod.labyMod;
    }
}
