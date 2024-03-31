// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import java.util.Date;
import org.apache.logging.log4j.core.util.CronExpression;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractLifeCycle;

public class ConfigurationScheduler extends AbstractLifeCycle
{
    private static final Logger LOGGER;
    private static final String SIMPLE_NAME;
    private static final int MAX_SCHEDULED_ITEMS = 5;
    private ScheduledExecutorService executorService;
    private int scheduledItems;
    
    public ConfigurationScheduler() {
        this.scheduledItems = 0;
    }
    
    @Override
    public void start() {
        super.start();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        if (this.isExecutorServiceSet()) {
            ConfigurationScheduler.LOGGER.debug("{} shutting down threads in {}", ConfigurationScheduler.SIMPLE_NAME, this.getExecutorService());
            this.executorService.shutdown();
            try {
                this.executorService.awaitTermination(timeout, timeUnit);
            }
            catch (final InterruptedException ie) {
                this.executorService.shutdownNow();
                try {
                    this.executorService.awaitTermination(timeout, timeUnit);
                }
                catch (final InterruptedException inner) {
                    ConfigurationScheduler.LOGGER.warn("ConfigurationScheduler stopped but some scheduled services may not have completed.");
                }
                Thread.currentThread().interrupt();
            }
        }
        this.setStopped();
        return true;
    }
    
    public boolean isExecutorServiceSet() {
        return this.executorService != null;
    }
    
    public void incrementScheduledItems() {
        if (this.isExecutorServiceSet()) {
            ConfigurationScheduler.LOGGER.error("{} attempted to increment scheduled items after start", ConfigurationScheduler.SIMPLE_NAME);
        }
        else {
            ++this.scheduledItems;
        }
    }
    
    public void decrementScheduledItems() {
        if (!this.isStarted() && this.scheduledItems > 0) {
            --this.scheduledItems;
        }
    }
    
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        return this.getExecutorService().schedule(callable, delay, unit);
    }
    
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return this.getExecutorService().schedule(command, delay, unit);
    }
    
    public CronScheduledFuture<?> scheduleWithCron(final CronExpression cronExpression, final Runnable command) {
        return this.scheduleWithCron(cronExpression, new Date(), command);
    }
    
    public CronScheduledFuture<?> scheduleWithCron(final CronExpression cronExpression, final Date startDate, final Runnable command) {
        final Date fireDate = cronExpression.getNextValidTimeAfter((startDate == null) ? new Date() : startDate);
        final CronRunnable runnable = new CronRunnable(command, cronExpression);
        final ScheduledFuture<?> future = this.schedule(runnable, this.nextFireInterval(fireDate), TimeUnit.MILLISECONDS);
        final CronScheduledFuture<?> cronScheduledFuture = new CronScheduledFuture<Object>(future, fireDate);
        runnable.setScheduledFuture(cronScheduledFuture);
        ConfigurationScheduler.LOGGER.debug("Scheduled cron expression {} to fire at {}", cronExpression.getCronExpression(), fireDate);
        return cronScheduledFuture;
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return this.getExecutorService().scheduleAtFixedRate(command, initialDelay, period, unit);
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return this.getExecutorService().scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
    
    public long nextFireInterval(final Date fireDate) {
        return fireDate.getTime() - new Date().getTime();
    }
    
    private ScheduledExecutorService getExecutorService() {
        if (this.executorService == null) {
            if (this.scheduledItems > 0) {
                ConfigurationScheduler.LOGGER.debug("{} starting {} threads", ConfigurationScheduler.SIMPLE_NAME, this.scheduledItems);
                this.scheduledItems = Math.min(this.scheduledItems, 5);
                final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(this.scheduledItems, Log4jThreadFactory.createDaemonThreadFactory("Scheduled"));
                executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
                executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
                this.executorService = executor;
            }
            else {
                ConfigurationScheduler.LOGGER.debug("{}: No scheduled items", ConfigurationScheduler.SIMPLE_NAME);
            }
        }
        return this.executorService;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConfigurationScheduler {");
        final Queue<Runnable> queue = ((ScheduledThreadPoolExecutor)this.executorService).getQueue();
        boolean first = true;
        for (final Runnable runnable : queue) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(runnable.toString());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        SIMPLE_NAME = "Log4j2 " + ConfigurationScheduler.class.getSimpleName();
    }
    
    public class CronRunnable implements Runnable
    {
        private final CronExpression cronExpression;
        private final Runnable runnable;
        private CronScheduledFuture<?> scheduledFuture;
        
        public CronRunnable(final Runnable runnable, final CronExpression cronExpression) {
            this.cronExpression = cronExpression;
            this.runnable = runnable;
        }
        
        public void setScheduledFuture(final CronScheduledFuture<?> future) {
            this.scheduledFuture = future;
        }
        
        @Override
        public void run() {
            try {
                final long millis = this.scheduledFuture.getFireTime().getTime() - System.currentTimeMillis();
                if (millis > 0L) {
                    ConfigurationScheduler.LOGGER.debug("Cron thread woke up {} millis early. Sleeping", (Object)millis);
                    try {
                        Thread.sleep(millis);
                    }
                    catch (final InterruptedException ex2) {}
                }
                this.runnable.run();
            }
            catch (final Throwable ex) {
                ConfigurationScheduler.LOGGER.error("{} caught error running command", ConfigurationScheduler.SIMPLE_NAME, ex);
            }
            finally {
                final Date fireDate = this.cronExpression.getNextValidTimeAfter(new Date());
                final ScheduledFuture<?> future = ConfigurationScheduler.this.schedule(this, ConfigurationScheduler.this.nextFireInterval(fireDate), TimeUnit.MILLISECONDS);
                ConfigurationScheduler.LOGGER.debug("Cron expression {} scheduled to fire again at {}", this.cronExpression.getCronExpression(), fireDate);
                this.scheduledFuture.reset(future, fireDate);
            }
        }
        
        @Override
        public String toString() {
            return "CronRunnable{" + this.cronExpression.getCronExpression() + " - " + this.scheduledFuture.getFireTime();
        }
    }
}
