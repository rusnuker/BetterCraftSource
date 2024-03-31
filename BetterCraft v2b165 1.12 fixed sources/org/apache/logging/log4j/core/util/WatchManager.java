// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import java.util.concurrent.ScheduledFuture;
import java.io.File;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractLifeCycle;

public class WatchManager extends AbstractLifeCycle
{
    private static Logger logger;
    private final ConcurrentMap<File, FileMonitor> watchers;
    private int intervalSeconds;
    private ScheduledFuture<?> future;
    private final ConfigurationScheduler scheduler;
    
    public WatchManager(final ConfigurationScheduler scheduler) {
        this.watchers = new ConcurrentHashMap<File, FileMonitor>();
        this.intervalSeconds = 0;
        this.scheduler = scheduler;
    }
    
    public void setIntervalSeconds(final int intervalSeconds) {
        if (!this.isStarted()) {
            if (this.intervalSeconds > 0 && intervalSeconds == 0) {
                this.scheduler.decrementScheduledItems();
            }
            else if (this.intervalSeconds == 0 && intervalSeconds > 0) {
                this.scheduler.incrementScheduledItems();
            }
            this.intervalSeconds = intervalSeconds;
        }
    }
    
    public int getIntervalSeconds() {
        return this.intervalSeconds;
    }
    
    @Override
    public void start() {
        super.start();
        if (this.intervalSeconds > 0) {
            this.future = this.scheduler.scheduleWithFixedDelay(new WatchRunnable(), this.intervalSeconds, this.intervalSeconds, TimeUnit.SECONDS);
        }
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        final boolean stopped = this.stop(this.future);
        this.setStopped();
        return stopped;
    }
    
    public void watchFile(final File file, final FileWatcher watcher) {
        this.watchers.put(file, new FileMonitor(file.lastModified(), watcher));
    }
    
    public Map<File, FileWatcher> getWatchers() {
        final Map<File, FileWatcher> map = new HashMap<File, FileWatcher>();
        for (final Map.Entry<File, FileMonitor> entry : this.watchers.entrySet()) {
            map.put(entry.getKey(), entry.getValue().fileWatcher);
        }
        return map;
    }
    
    static {
        WatchManager.logger = StatusLogger.getLogger();
    }
    
    private class WatchRunnable implements Runnable
    {
        @Override
        public void run() {
            for (final Map.Entry<File, FileMonitor> entry : WatchManager.this.watchers.entrySet()) {
                final File file = entry.getKey();
                final FileMonitor fileMonitor = entry.getValue();
                final long lastModfied = file.lastModified();
                if (this.fileModified(fileMonitor, lastModfied)) {
                    WatchManager.logger.info("File {} was modified on {}, previous modification was {}", file, lastModfied, fileMonitor.lastModified);
                    fileMonitor.lastModified = lastModfied;
                    fileMonitor.fileWatcher.fileModified(file);
                }
            }
        }
        
        private boolean fileModified(final FileMonitor fileMonitor, final long lastModfied) {
            return lastModfied != fileMonitor.lastModified;
        }
    }
    
    private class FileMonitor
    {
        private final FileWatcher fileWatcher;
        private long lastModified;
        
        public FileMonitor(final long lastModified, final FileWatcher fileWatcher) {
            this.fileWatcher = fileWatcher;
            this.lastModified = lastModified;
        }
    }
}
