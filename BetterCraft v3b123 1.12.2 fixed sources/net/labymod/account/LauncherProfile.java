// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.account;

import java.beans.ConstructorProperties;
import net.minecraft.util.Session;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import com.mojang.authlib.GameProfile;

public class LauncherProfile
{
    private String mojangUsername;
    private String userid;
    private GameProfile gameProfile;
    private String accessToken;
    
    public void login(final Consumer<Boolean> response) {
        LabyMod.getInstance().getAccountManager().getAccountLoginHandler().loginWithToken(this.gameProfile.getId(), this.gameProfile.getName(), this.accessToken, response);
    }
    
    public void refresh(final Consumer<Boolean> response) {
        try {
            LabyMod.getInstance().getAccountManager().getAccountLoginHandler().refreshToken(this.accessToken, new Consumer<String>() {
                @Override
                public void accept(final String accepted) {
                    if (accepted != null) {
                        LauncherProfile.access$0(LauncherProfile.this, accepted);
                    }
                    LabyMod.getInstance().getAccountManager().saveLauncherProfiles(response);
                }
            }, response);
        }
        catch (final Exception e) {
            response.accept(false);
            e.printStackTrace();
        }
    }
    
    public Session buildSession() {
        return new Session(this.gameProfile.getName(), this.gameProfile.getId().toString(), this.accessToken, Session.Type.MOJANG.toString());
    }
    
    @ConstructorProperties({ "mojangUsername", "userid", "gameProfile", "accessToken" })
    public LauncherProfile(final String mojangUsername, final String userid, final GameProfile gameProfile, final String accessToken) {
        this.mojangUsername = mojangUsername;
        this.userid = userid;
        this.gameProfile = gameProfile;
        this.accessToken = accessToken;
    }
    
    public String getMojangUsername() {
        return this.mojangUsername;
    }
    
    public String getUserid() {
        return this.userid;
    }
    
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
    
    public String getAccessToken() {
        return this.accessToken;
    }
    
    public void setMojangUsername(final String mojangUsername) {
        this.mojangUsername = mojangUsername;
    }
    
    public void setUserid(final String userid) {
        this.userid = userid;
    }
    
    public void setGameProfile(final GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }
    
    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }
    
    static /* synthetic */ void access$0(final LauncherProfile launcherProfile, final String accessToken) {
        launcherProfile.accessToken = accessToken;
    }
}
