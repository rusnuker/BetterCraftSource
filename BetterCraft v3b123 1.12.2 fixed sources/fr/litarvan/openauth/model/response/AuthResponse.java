// 
// Decompiled by Procyon v0.6.0
// 

package fr.litarvan.openauth.model.response;

import fr.litarvan.openauth.model.AuthProfile;

public class AuthResponse
{
    private String accessToken;
    private String clientToken;
    private AuthProfile[] availableProfiles;
    private AuthProfile selectedProfile;
    
    public AuthResponse(final String accessToken, final String clientToken, final AuthProfile[] availableProfiles, final AuthProfile selectedProfile) {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
        this.availableProfiles = availableProfiles;
        this.selectedProfile = selectedProfile;
    }
    
    public String getAccessToken() {
        return this.accessToken;
    }
    
    public String getClientToken() {
        return this.clientToken;
    }
    
    public AuthProfile[] getAvailableProfiles() {
        return this.availableProfiles;
    }
    
    public AuthProfile getSelectedProfile() {
        return this.selectedProfile;
    }
}
