// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.thealtening.api.retriever;

import java.util.Iterator;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.List;
import me.amkgre.bettercraft.client.mods.thealtening.api.response.Account;
import me.amkgre.bettercraft.client.mods.thealtening.api.TheAlteningException;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import me.amkgre.bettercraft.client.mods.thealtening.api.response.License;

public class BasicDataRetriever implements DataRetriever
{
    private String apiKey;
    
    public BasicDataRetriever(final String apiKey) {
        this.apiKey = apiKey;
    }
    
    @Override
    public void updateKey(final String newApiKey) {
        this.apiKey = newApiKey;
    }
    
    @Override
    public License getLicense() throws TheAlteningException {
        final JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v2/license?key=" + this.apiKey).getAsJsonObject();
        return BasicDataRetriever.gson.fromJson(jsonObject, License.class);
    }
    
    @Override
    public Account getAccount() throws TheAlteningException {
        final JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v2/generate?info=true&key=" + this.apiKey).getAsJsonObject();
        return BasicDataRetriever.gson.fromJson(jsonObject, Account.class);
    }
    
    @Override
    public boolean isPrivate(final String token) throws TheAlteningException {
        final JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v2/private?token=" + token + "&key=" + this.apiKey).getAsJsonObject();
        return this.isSuccess(jsonObject);
    }
    
    @Override
    public boolean isFavorite(final String token) throws TheAlteningException {
        final JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v2/favorite?token=" + token + "&key=" + this.apiKey).getAsJsonObject();
        return this.isSuccess(jsonObject);
    }
    
    @Override
    public List<Account> getPrivatedAccounts() {
        final ArrayList<Account> privatedAccountList = new ArrayList<Account>();
        final JsonArray privatedAccountsObject = this.retrieveData("http://api.thealtening.com/v2/privates?key=" + this.apiKey).getAsJsonArray();
        for (final JsonElement jsonElement : privatedAccountsObject) {
            if (!jsonElement.isJsonObject()) {
                continue;
            }
            privatedAccountList.add(BasicDataRetriever.gson.fromJson(jsonElement, Account.class));
        }
        return privatedAccountList;
    }
    
    @Override
    public List<Account> getFavoriteAccounts() {
        final ArrayList<Account> favoritedAccountList = new ArrayList<Account>();
        final JsonArray favoritedAccountsObject = this.retrieveData("http://api.thealtening.com/v2/favorites?key=" + this.apiKey).getAsJsonArray();
        for (final JsonElement jsonElement : favoritedAccountsObject) {
            if (!jsonElement.isJsonObject()) {
                continue;
            }
            favoritedAccountList.add(BasicDataRetriever.gson.fromJson(jsonElement, Account.class));
        }
        return favoritedAccountList;
    }
    
    public AsynchronousDataRetriever toAsync() {
        return new AsynchronousDataRetriever(this.apiKey);
    }
}
