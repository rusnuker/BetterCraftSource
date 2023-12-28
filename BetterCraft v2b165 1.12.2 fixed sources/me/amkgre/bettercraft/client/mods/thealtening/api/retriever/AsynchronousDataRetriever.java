// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.thealtening.api.retriever;

import me.amkgre.bettercraft.client.mods.thealtening.api.TheAlteningException;
import java.util.function.Function;
import java.util.List;
import me.amkgre.bettercraft.client.mods.thealtening.api.response.Account;
import me.amkgre.bettercraft.client.mods.thealtening.api.response.License;
import java.util.concurrent.CompletableFuture;

public class AsynchronousDataRetriever extends BasicDataRetriever
{
    public AsynchronousDataRetriever(final String apiKey) {
        super(apiKey);
    }
    
    public CompletableFuture<License> getLicenseDataAsync() {
        return this.completeTask(BasicDataRetriever::getLicense);
    }
    
    public CompletableFuture<Account> getAccountDataAsync() {
        return this.completeTask(BasicDataRetriever::getAccount);
    }
    
    public CompletableFuture<Boolean> isPrivateAsync(final String token) {
        return this.completeTask(dr -> dr.isPrivate(token2));
    }
    
    public CompletableFuture<Boolean> isFavoriteAsync(final String token) {
        return this.completeTask(dr -> dr.isFavorite(token2));
    }
    
    public CompletableFuture<List<Account>> getPrivatedAccountsAsync() {
        return (CompletableFuture<List<Account>>)this.completeTask(BasicDataRetriever::getPrivatedAccounts);
    }
    
    public CompletableFuture<List<Account>> getFavoritedAccountsAsync() {
        return (CompletableFuture<List<Account>>)this.completeTask(BasicDataRetriever::getFavoriteAccounts);
    }
    
    private <T> CompletableFuture<T> completeTask(final Function<BasicDataRetriever, T> function) {
        final CompletableFuture<T> returnValue = new CompletableFuture<T>();
        try {
            returnValue.complete(function.apply(this));
        }
        catch (final TheAlteningException exception) {
            returnValue.completeExceptionally(exception);
        }
        return returnValue;
    }
}
