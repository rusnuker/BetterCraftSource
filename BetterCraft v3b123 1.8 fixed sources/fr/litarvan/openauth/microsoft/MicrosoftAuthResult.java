// 
// Decompiled by Procyon v0.6.0
// 

package fr.litarvan.openauth.microsoft;

import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;

public class MicrosoftAuthResult
{
    private final MinecraftProfile profile;
    private final String accessToken;
    private final String refreshToken;
    
    public MicrosoftAuthResult(final MinecraftProfile profile, final String accessToken, final String refreshToken) {
        this.profile = profile;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    
    public MinecraftProfile getProfile() {
        return this.profile;
    }
    
    public String getAccessToken() {
        return this.accessToken;
    }
    
    public String getRefreshToken() {
        return this.refreshToken;
    }
}
