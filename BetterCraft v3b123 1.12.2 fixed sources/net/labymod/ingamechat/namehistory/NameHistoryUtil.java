// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.namehistory;

import net.labymod.utils.Consumer;
import net.labymod.utils.UUIDFetcher;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.HashMap;

public class NameHistoryUtil
{
    private static HashMap<String, NameHistory> cacheHistory;
    private static final ExecutorService EXECUTOR_SERVICE;
    
    static {
        NameHistoryUtil.cacheHistory = new HashMap<String, NameHistory>();
        EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    }
    
    public static NameHistory getNameHistory(final String name) {
        if (NameHistoryUtil.cacheHistory.containsKey(name)) {
            return NameHistoryUtil.cacheHistory.get(name);
        }
        final NameHistory nameHistory = new NameHistory(UUID.randomUUID(), new UUIDFetcher[0]);
        NameHistoryUtil.cacheHistory.put(name, nameHistory);
        NameHistoryUtil.EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                final NameHistory nameHistory = requestHistory(name);
                NameHistoryUtil.cacheHistory.put(name, nameHistory);
            }
        });
        return nameHistory;
    }
    
    public static void getNameHistory(final String name, final Consumer<NameHistory> callback) {
        if (NameHistoryUtil.cacheHistory.containsKey(name)) {
            callback.accept(NameHistoryUtil.cacheHistory.get(name));
            return;
        }
        final NameHistory nameHistory = new NameHistory(UUID.randomUUID(), new UUIDFetcher[0]);
        NameHistoryUtil.cacheHistory.put(name, nameHistory);
        NameHistoryUtil.EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                final NameHistory history = requestHistory(name);
                NameHistoryUtil.cacheHistory.put(name, history);
                callback.accept(history);
            }
        });
    }
    
    public static boolean isInCache(final String name) {
        return NameHistoryUtil.cacheHistory.containsKey(name);
    }
    
    private static NameHistory requestHistory(final String name) {
        final UUID uuid = UUIDFetcher.getUUID(name);
        if (uuid == null) {
            return null;
        }
        return UUIDFetcher.getHistory(uuid);
    }
}
