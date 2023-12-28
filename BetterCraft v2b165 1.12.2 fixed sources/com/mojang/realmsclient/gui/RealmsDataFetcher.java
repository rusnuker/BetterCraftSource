// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.gui;

import com.mojang.realmsclient.dto.RealmsNews;
import com.mojang.realmsclient.util.RealmsPersistence;
import com.mojang.realmsclient.client.RealmsClient;
import org.apache.logging.log4j.LogManager;
import java.util.Comparator;
import java.util.Collections;
import net.minecraft.realms.Realms;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.concurrent.Executors;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import com.mojang.realmsclient.dto.RealmsServerPlayerLists;
import java.util.List;
import com.mojang.realmsclient.dto.RealmsServer;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.logging.log4j.Logger;

public class RealmsDataFetcher
{
    private static final Logger LOGGER;
    private final ScheduledExecutorService scheduler;
    private static final int SERVER_UPDATE_INTERVAL = 60;
    private static final int PENDING_INVITES_INTERVAL = 10;
    private static final int TRIAL_UPDATE_INTERVAL = 60;
    private static final int LIVE_STATS_INTERVAL = 10;
    private static final int UNREAD_NEWS_INTERVAL = 300;
    private volatile boolean stopped;
    private final ServerListUpdateTask serverListUpdateTask;
    private final PendingInviteUpdateTask pendingInviteUpdateTask;
    private final TrialAvailabilityTask trialAvailabilityTask;
    private final LiveStatsTask liveStatsTask;
    private final UnreadNewsTask unreadNewsTask;
    private final Set<RealmsServer> removedServers;
    private List<RealmsServer> servers;
    private RealmsServerPlayerLists livestats;
    private int pendingInvitesCount;
    private boolean trialAvailable;
    private boolean hasUnreadNews;
    private String newsLink;
    private ScheduledFuture<?> serverListScheduledFuture;
    private ScheduledFuture<?> pendingInviteScheduledFuture;
    private ScheduledFuture<?> trialAvailableScheduledFuture;
    private ScheduledFuture<?> liveStatsScheduledFuture;
    private ScheduledFuture<?> unreadNewsScheduledFuture;
    private final Map<Task, Boolean> fetchStatus;
    
    public RealmsDataFetcher() {
        this.scheduler = Executors.newScheduledThreadPool(3);
        this.stopped = true;
        this.serverListUpdateTask = new ServerListUpdateTask();
        this.pendingInviteUpdateTask = new PendingInviteUpdateTask();
        this.trialAvailabilityTask = new TrialAvailabilityTask();
        this.liveStatsTask = new LiveStatsTask();
        this.unreadNewsTask = new UnreadNewsTask();
        this.removedServers = (Set<RealmsServer>)Sets.newHashSet();
        this.servers = (List<RealmsServer>)Lists.newArrayList();
        this.fetchStatus = new ConcurrentHashMap<Task, Boolean>(Task.values().length);
    }
    
    public boolean isStopped() {
        return this.stopped;
    }
    
    public synchronized void init() {
        if (this.stopped) {
            this.stopped = false;
            this.cancelTasks();
            this.scheduleTasks();
        }
    }
    
    public synchronized void initWithSpecificTaskList(final List<Task> tasks) {
        if (this.stopped) {
            this.stopped = false;
            this.cancelTasks();
            for (final Task task : tasks) {
                this.fetchStatus.put(task, false);
                switch (task) {
                    case SERVER_LIST: {
                        this.serverListScheduledFuture = this.scheduler.scheduleAtFixedRate(this.serverListUpdateTask, 0L, 60L, TimeUnit.SECONDS);
                        continue;
                    }
                    case PENDING_INVITE: {
                        this.pendingInviteScheduledFuture = this.scheduler.scheduleAtFixedRate(this.pendingInviteUpdateTask, 0L, 10L, TimeUnit.SECONDS);
                        continue;
                    }
                    case TRIAL_AVAILABLE: {
                        this.trialAvailableScheduledFuture = this.scheduler.scheduleAtFixedRate(this.trialAvailabilityTask, 0L, 60L, TimeUnit.SECONDS);
                        continue;
                    }
                    case LIVE_STATS: {
                        this.liveStatsScheduledFuture = this.scheduler.scheduleAtFixedRate(this.liveStatsTask, 0L, 10L, TimeUnit.SECONDS);
                        continue;
                    }
                    case UNREAD_NEWS: {
                        this.unreadNewsScheduledFuture = this.scheduler.scheduleAtFixedRate(this.unreadNewsTask, 0L, 300L, TimeUnit.SECONDS);
                        continue;
                    }
                }
            }
        }
    }
    
    public boolean isFetchedSinceLastTry(final Task task) {
        final Boolean result = this.fetchStatus.get(task);
        return result != null && result;
    }
    
    public void markClean() {
        for (final Task task : this.fetchStatus.keySet()) {
            this.fetchStatus.put(task, false);
        }
    }
    
    public synchronized void forceUpdate() {
        this.stop();
        this.init();
    }
    
    public synchronized List<RealmsServer> getServers() {
        return (List<RealmsServer>)Lists.newArrayList((Iterable<?>)this.servers);
    }
    
    public synchronized int getPendingInvitesCount() {
        return this.pendingInvitesCount;
    }
    
    public synchronized boolean isTrialAvailable() {
        return this.trialAvailable;
    }
    
    public synchronized RealmsServerPlayerLists getLivestats() {
        return this.livestats;
    }
    
    public synchronized boolean hasUnreadNews() {
        return this.hasUnreadNews;
    }
    
    public synchronized String newsLink() {
        return this.newsLink;
    }
    
    public synchronized void stop() {
        this.stopped = true;
        this.cancelTasks();
    }
    
    private void scheduleTasks() {
        for (final Task task : Task.values()) {
            this.fetchStatus.put(task, false);
        }
        this.serverListScheduledFuture = this.scheduler.scheduleAtFixedRate(this.serverListUpdateTask, 0L, 60L, TimeUnit.SECONDS);
        this.pendingInviteScheduledFuture = this.scheduler.scheduleAtFixedRate(this.pendingInviteUpdateTask, 0L, 10L, TimeUnit.SECONDS);
        this.trialAvailableScheduledFuture = this.scheduler.scheduleAtFixedRate(this.trialAvailabilityTask, 0L, 60L, TimeUnit.SECONDS);
        this.liveStatsScheduledFuture = this.scheduler.scheduleAtFixedRate(this.liveStatsTask, 0L, 10L, TimeUnit.SECONDS);
        this.unreadNewsScheduledFuture = this.scheduler.scheduleAtFixedRate(this.unreadNewsTask, 0L, 300L, TimeUnit.SECONDS);
    }
    
    private void cancelTasks() {
        try {
            if (this.serverListScheduledFuture != null) {
                this.serverListScheduledFuture.cancel(false);
            }
            if (this.pendingInviteScheduledFuture != null) {
                this.pendingInviteScheduledFuture.cancel(false);
            }
            if (this.trialAvailableScheduledFuture != null) {
                this.trialAvailableScheduledFuture.cancel(false);
            }
            if (this.liveStatsScheduledFuture != null) {
                this.liveStatsScheduledFuture.cancel(false);
            }
            if (this.unreadNewsScheduledFuture != null) {
                this.unreadNewsScheduledFuture.cancel(false);
            }
        }
        catch (final Exception e) {
            RealmsDataFetcher.LOGGER.error("Failed to cancel Realms tasks", e);
        }
    }
    
    private synchronized void setServers(final List<RealmsServer> newServers) {
        int removedCnt = 0;
        for (final RealmsServer server : this.removedServers) {
            if (newServers.remove(server)) {
                ++removedCnt;
            }
        }
        if (removedCnt == 0) {
            this.removedServers.clear();
        }
        this.servers = newServers;
    }
    
    public synchronized void removeItem(final RealmsServer server) {
        this.servers.remove(server);
        this.removedServers.add(server);
    }
    
    private void sort(final List<RealmsServer> servers) {
        Collections.sort(servers, new RealmsServer.McoServerComparator(Realms.getName()));
    }
    
    private boolean isActive() {
        return !this.stopped;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    private class ServerListUpdateTask implements Runnable
    {
        @Override
        public void run() {
            if (RealmsDataFetcher.this.isActive()) {
                this.updateServersList();
            }
        }
        
        private void updateServersList() {
            try {
                final RealmsClient client = RealmsClient.createRealmsClient();
                if (client != null) {
                    final List<RealmsServer> servers = client.listWorlds().servers;
                    if (servers != null) {
                        RealmsDataFetcher.this.sort(servers);
                        RealmsDataFetcher.this.setServers(servers);
                        RealmsDataFetcher.this.fetchStatus.put(Task.SERVER_LIST, true);
                    }
                    else {
                        RealmsDataFetcher.LOGGER.warn("Realms server list was null or empty");
                    }
                }
            }
            catch (final Exception e) {
                RealmsDataFetcher.this.fetchStatus.put(Task.SERVER_LIST, true);
                RealmsDataFetcher.LOGGER.error("Couldn't get server list", e);
            }
        }
    }
    
    private class PendingInviteUpdateTask implements Runnable
    {
        @Override
        public void run() {
            if (RealmsDataFetcher.this.isActive()) {
                this.updatePendingInvites();
            }
        }
        
        private void updatePendingInvites() {
            try {
                final RealmsClient client = RealmsClient.createRealmsClient();
                if (client != null) {
                    RealmsDataFetcher.this.pendingInvitesCount = client.pendingInvitesCount();
                    RealmsDataFetcher.this.fetchStatus.put(Task.PENDING_INVITE, true);
                }
            }
            catch (final Exception e) {
                RealmsDataFetcher.LOGGER.error("Couldn't get pending invite count", e);
            }
        }
    }
    
    private class TrialAvailabilityTask implements Runnable
    {
        @Override
        public void run() {
            if (RealmsDataFetcher.this.isActive()) {
                this.getTrialAvailable();
            }
        }
        
        private void getTrialAvailable() {
            try {
                final RealmsClient client = RealmsClient.createRealmsClient();
                if (client != null) {
                    RealmsDataFetcher.this.trialAvailable = client.trialAvailable();
                    RealmsDataFetcher.this.fetchStatus.put(Task.TRIAL_AVAILABLE, true);
                }
            }
            catch (final Exception e) {
                RealmsDataFetcher.LOGGER.error("Couldn't get trial availability", e);
            }
        }
    }
    
    private class LiveStatsTask implements Runnable
    {
        @Override
        public void run() {
            if (RealmsDataFetcher.this.isActive()) {
                this.getLiveStats();
            }
        }
        
        private void getLiveStats() {
            try {
                final RealmsClient client = RealmsClient.createRealmsClient();
                if (client != null) {
                    RealmsDataFetcher.this.livestats = client.getLiveStats();
                    RealmsDataFetcher.this.fetchStatus.put(Task.LIVE_STATS, true);
                }
            }
            catch (final Exception e) {
                RealmsDataFetcher.LOGGER.error("Couldn't get live stats", e);
            }
        }
    }
    
    private class UnreadNewsTask implements Runnable
    {
        @Override
        public void run() {
            if (RealmsDataFetcher.this.isActive()) {
                this.getUnreadNews();
            }
        }
        
        private void getUnreadNews() {
            try {
                final RealmsClient client = RealmsClient.createRealmsClient();
                if (client != null) {
                    RealmsNews fetchedNews = null;
                    try {
                        fetchedNews = client.getNews();
                    }
                    catch (final Exception ex) {}
                    final RealmsPersistence.RealmsPersistenceData data = RealmsPersistence.readFile();
                    if (fetchedNews != null) {
                        final String fetchedNewsLink = fetchedNews.newsLink;
                        if (fetchedNewsLink != null && !fetchedNewsLink.equals(data.newsLink)) {
                            data.hasUnreadNews = true;
                            data.newsLink = fetchedNewsLink;
                            RealmsPersistence.writeFile(data);
                        }
                    }
                    RealmsDataFetcher.this.hasUnreadNews = data.hasUnreadNews;
                    RealmsDataFetcher.this.newsLink = data.newsLink;
                    RealmsDataFetcher.this.fetchStatus.put(Task.UNREAD_NEWS, true);
                }
            }
            catch (final Exception e) {
                RealmsDataFetcher.LOGGER.error("Couldn't get unread news", e);
            }
        }
    }
    
    public enum Task
    {
        SERVER_LIST, 
        PENDING_INVITE, 
        TRIAL_AVAILABLE, 
        LIVE_STATS, 
        UNREAD_NEWS;
    }
}
