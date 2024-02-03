// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.altmanager.impl;

import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Reader;
import java.io.FileReader;
import com.google.gson.JsonParser;
import java.io.IOException;
import com.google.gson.JsonElement;
import java.io.PrintWriter;
import com.google.gson.GsonBuilder;
import me.nzxtercode.bettercraft.client.BetterCraft;
import java.io.File;
import com.google.gson.Gson;
import java.util.ArrayList;

public class AccountManager
{
    private static final AccountManager INSTANCE;
    private ArrayList<Account> accounts;
    private final Gson gson;
    private String alteningKey;
    private String lastAlteningAlt;
    private Account lastAlt;
    private static File altsFile;
    
    static {
        INSTANCE = new AccountManager();
        BetterCraft.getInstance();
        AccountManager.altsFile = new File(BetterCraft.clientName, "alts.json");
    }
    
    public AccountManager() {
        this.accounts = new ArrayList<Account>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    public static AccountManager getInstance() {
        return AccountManager.INSTANCE;
    }
    
    public void save() {
        if (AccountManager.altsFile == null) {
            return;
        }
        try {
            if (!AccountManager.altsFile.exists()) {
                AccountManager.altsFile.createNewFile();
            }
            final PrintWriter printWriter = new PrintWriter(AccountManager.altsFile);
            printWriter.write(this.gson.toJson(this.toJson()));
            printWriter.close();
        }
        catch (final IOException ex) {}
    }
    
    public void init() {
        if (!AccountManager.altsFile.exists()) {
            this.save();
            return;
        }
        try {
            final JsonObject json = new JsonParser().parse(new FileReader(AccountManager.altsFile)).getAsJsonObject();
            this.fromJson(json);
        }
        catch (final IOException ex) {}
    }
    
    public JsonObject toJson() {
        final JsonObject jsonObject = new JsonObject();
        final JsonArray jsonArray = new JsonArray();
        this.getAccounts().forEach(account -> jsonArray2.add(account.toJson()));
        if (this.alteningKey != null) {
            jsonObject.addProperty("altening", this.alteningKey);
        }
        if (this.lastAlteningAlt != null) {
            jsonObject.addProperty("alteningAlt", this.lastAlteningAlt);
        }
        if (this.lastAlt != null) {
            jsonObject.add("lastalt", this.lastAlt.toJson());
        }
        jsonObject.add("accounts", jsonArray);
        return jsonObject;
    }
    
    public void fromJson(final JsonObject json) {
        if (json.has("altening")) {
            this.alteningKey = json.get("altening").getAsString();
        }
        if (json.has("alteningAlt")) {
            this.lastAlteningAlt = json.get("alteningAlt").getAsString();
        }
        if (json.has("lastalt")) {
            final Account account = new Account();
            account.fromJson(json.get("lastalt").getAsJsonObject());
            this.lastAlt = account;
        }
        final JsonArray jsonArray = json.get("accounts").getAsJsonArray();
        jsonArray.forEach(jsonElement -> {
            final JsonObject jsonObject = (JsonObject)jsonElement;
            final Account account2 = new Account();
            account2.fromJson(jsonObject);
            this.getAccounts().add(account2);
        });
    }
    
    public void remove(final String username) {
        for (final Account account : this.getAccounts()) {
            if (account.getName().equalsIgnoreCase(username)) {
                this.getAccounts().remove(account);
            }
        }
    }
    
    public Account getAccountByEmail(final String email) {
        for (final Account account : this.getAccounts()) {
            if (account.getEmail().equalsIgnoreCase(email)) {
                return account;
            }
        }
        return null;
    }
    
    public String getLastAlteningAlt() {
        return this.lastAlteningAlt;
    }
    
    public void setLastAlteningAlt(final String lastAlteningAlt) {
        this.lastAlteningAlt = lastAlteningAlt;
    }
    
    public String getAlteningKey() {
        return this.alteningKey;
    }
    
    public void setAlteningKey(final String alteningKey) {
        this.alteningKey = alteningKey;
    }
    
    public Account getLastAlt() {
        return this.lastAlt;
    }
    
    public void setLastAlt(final Account lastAlt) {
        this.lastAlt = lastAlt;
    }
    
    public ArrayList<Account> getNotBannedAccounts() {
        final List<Account> accounts = new ArrayList<Account>(this.accounts);
        for (int i = 0; i < accounts.size(); ++i) {
            if (accounts.get(i).isBanned()) {
                accounts.remove(i);
            }
        }
        return this.accounts;
    }
    
    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }
}
