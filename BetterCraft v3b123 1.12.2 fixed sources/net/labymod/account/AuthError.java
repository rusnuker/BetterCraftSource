// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.account;

import net.labymod.main.lang.LanguageManager;

public enum AuthError
{
    NO_FILE("NO_FILE", 0, "account_manager_error_no_launcher_profiles"), 
    INVALID_CONTENTS("INVALID_CONTENTS", 1, "account_manager_error_invalid_launcher_profiles_contents"), 
    EXCEPTION("EXCEPTION", 2, (String)null), 
    NOT_READY_TO_PLAY("NOT_READY_TO_PLAY", 3, "account_manager_error_not_ready_to_play"), 
    FAILED_TO_REFRESH("FAILED_TO_REFRESH", 4, (String)null);
    
    private String errorMessage;
    
    public String toMessage(final String... format) {
        final String translate = (format.length == 0 && this.errorMessage != null) ? LanguageManager.translate(this.errorMessage) : ((format.length == 0) ? this.name() : format[0]);
        return String.valueOf(LanguageManager.translate("account_manager_error")) + " " + this.getErrorCode() + ": " + translate;
    }
    
    public int getErrorCode() {
        return this.ordinal() + 1;
    }
    
    private AuthError(final String s, final int n, final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
