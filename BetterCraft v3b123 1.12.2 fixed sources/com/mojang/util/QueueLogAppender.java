// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.BlockingQueue;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.AbstractAppender;

@Plugin(name = "Queue", category = "Core", elementType = "appender", printObject = true)
public class QueueLogAppender extends AbstractAppender
{
    private static final int MAX_CAPACITY = 250;
    private static final Map<String, BlockingQueue<String>> QUEUES;
    private static final ReadWriteLock QUEUE_LOCK;
    private final BlockingQueue<String> queue;
    
    public QueueLogAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final BlockingQueue<String> queue) {
        super(name, filter, layout, ignoreExceptions);
        this.queue = queue;
    }
    
    @Override
    public void append(final LogEvent event) {
        if (this.queue.size() >= 250) {
            this.queue.clear();
        }
        this.queue.add(this.getLayout().toSerializable(event).toString());
    }
    
    @PluginFactory
    public static QueueLogAppender createAppender(@PluginAttribute("name") final String name, @PluginAttribute("ignoreExceptions") final String ignore, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filters") final Filter filter, @PluginAttribute("target") String target) {
        final boolean ignoreExceptions = Boolean.parseBoolean(ignore);
        if (name == null) {
            QueueLogAppender.LOGGER.error("No name provided for QueueLogAppender");
            return null;
        }
        if (target == null) {
            target = name;
        }
        QueueLogAppender.QUEUE_LOCK.writeLock().lock();
        BlockingQueue<String> queue = QueueLogAppender.QUEUES.get(target);
        if (queue == null) {
            queue = new LinkedBlockingQueue<String>();
            QueueLogAppender.QUEUES.put(target, queue);
        }
        QueueLogAppender.QUEUE_LOCK.writeLock().unlock();
        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, null);
        }
        return new QueueLogAppender(name, filter, layout, ignoreExceptions, queue);
    }
    
    public static String getNextLogEvent(final String queueName) {
        QueueLogAppender.QUEUE_LOCK.readLock().lock();
        final BlockingQueue<String> queue = QueueLogAppender.QUEUES.get(queueName);
        QueueLogAppender.QUEUE_LOCK.readLock().unlock();
        if (queue != null) {
            try {
                return queue.take();
            }
            catch (final InterruptedException ex) {}
        }
        return null;
    }
    
    static {
        QUEUES = new HashMap<String, BlockingQueue<String>>();
        QUEUE_LOCK = new ReentrantReadWriteLock();
    }
}
