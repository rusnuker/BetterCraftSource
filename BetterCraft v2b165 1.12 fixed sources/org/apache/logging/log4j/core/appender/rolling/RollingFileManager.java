// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.io.FileOutputStream;
import org.apache.logging.log4j.core.util.FileUtils;
import java.io.File;
import org.apache.logging.log4j.core.appender.ConfigurationFactoryData;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import org.apache.logging.log4j.core.LifeCycle2;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.core.util.Constants;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.ExecutorService;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import java.util.concurrent.Semaphore;
import org.apache.logging.log4j.core.appender.FileManager;

public class RollingFileManager extends FileManager
{
    private static RollingFileManagerFactory factory;
    private static final int MAX_TRIES = 3;
    private static final int MIN_DURATION = 100;
    protected long size;
    private long initialTime;
    private final PatternProcessor patternProcessor;
    private final Semaphore semaphore;
    private final Log4jThreadFactory threadFactory;
    private volatile TriggeringPolicy triggeringPolicy;
    private volatile RolloverStrategy rolloverStrategy;
    private volatile boolean renameEmptyFiles;
    private volatile boolean initialized;
    private volatile String fileName;
    private FileExtension fileExtension;
    private ExecutorService asyncExecutor;
    private static final AtomicReferenceFieldUpdater<RollingFileManager, TriggeringPolicy> triggeringPolicyUpdater;
    private static final AtomicReferenceFieldUpdater<RollingFileManager, RolloverStrategy> rolloverStrategyUpdater;
    
    @Deprecated
    protected RollingFileManager(final String fileName, final String pattern, final OutputStream os, final boolean append, final long size, final long time, final TriggeringPolicy triggeringPolicy, final RolloverStrategy rolloverStrategy, final String advertiseURI, final Layout<? extends Serializable> layout, final int bufferSize, final boolean writeHeader) {
        this(fileName, pattern, os, append, size, time, triggeringPolicy, rolloverStrategy, advertiseURI, layout, writeHeader, ByteBuffer.wrap(new byte[Constants.ENCODER_BYTE_BUFFER_SIZE]));
    }
    
    @Deprecated
    protected RollingFileManager(final String fileName, final String pattern, final OutputStream os, final boolean append, final long size, final long time, final TriggeringPolicy triggeringPolicy, final RolloverStrategy rolloverStrategy, final String advertiseURI, final Layout<? extends Serializable> layout, final boolean writeHeader, final ByteBuffer buffer) {
        super(fileName, os, append, false, advertiseURI, layout, writeHeader, buffer);
        this.semaphore = new Semaphore(1);
        this.threadFactory = Log4jThreadFactory.createThreadFactory("RollingFileManager");
        this.renameEmptyFiles = false;
        this.initialized = false;
        this.asyncExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, new EmptyQueue(), this.threadFactory);
        this.size = size;
        this.initialTime = time;
        this.triggeringPolicy = triggeringPolicy;
        this.rolloverStrategy = rolloverStrategy;
        (this.patternProcessor = new PatternProcessor(pattern)).setPrevFileTime(time);
        this.fileName = fileName;
        this.fileExtension = FileExtension.lookupForFile(pattern);
    }
    
    protected RollingFileManager(final LoggerContext loggerContext, final String fileName, final String pattern, final OutputStream os, final boolean append, final boolean createOnDemand, final long size, final long time, final TriggeringPolicy triggeringPolicy, final RolloverStrategy rolloverStrategy, final String advertiseURI, final Layout<? extends Serializable> layout, final boolean writeHeader, final ByteBuffer buffer) {
        super(loggerContext, fileName, os, append, false, createOnDemand, advertiseURI, layout, writeHeader, buffer);
        this.semaphore = new Semaphore(1);
        this.threadFactory = Log4jThreadFactory.createThreadFactory("RollingFileManager");
        this.renameEmptyFiles = false;
        this.initialized = false;
        this.asyncExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, new EmptyQueue(), this.threadFactory);
        this.size = size;
        this.initialTime = time;
        this.triggeringPolicy = triggeringPolicy;
        this.rolloverStrategy = rolloverStrategy;
        (this.patternProcessor = new PatternProcessor(pattern)).setPrevFileTime(time);
        this.fileName = fileName;
        this.fileExtension = FileExtension.lookupForFile(pattern);
    }
    
    public void initialize() {
        if (!this.initialized) {
            RollingFileManager.LOGGER.debug("Initializing triggering policy {}", this.triggeringPolicy);
            this.initialized = true;
            this.triggeringPolicy.initialize(this);
            if (this.triggeringPolicy instanceof LifeCycle) {
                ((LifeCycle)this.triggeringPolicy).start();
            }
        }
    }
    
    public static RollingFileManager getFileManager(final String fileName, final String pattern, final boolean append, final boolean bufferedIO, final TriggeringPolicy policy, final RolloverStrategy strategy, final String advertiseURI, final Layout<? extends Serializable> layout, final int bufferSize, final boolean immediateFlush, final boolean createOnDemand, final Configuration configuration) {
        final String name = (fileName == null) ? pattern : fileName;
        return (RollingFileManager)OutputStreamManager.getManager(name, new FactoryData(fileName, pattern, append, bufferedIO, policy, strategy, advertiseURI, layout, bufferSize, immediateFlush, createOnDemand, configuration), RollingFileManager.factory);
    }
    
    @Override
    public String getFileName() {
        if (this.rolloverStrategy instanceof DirectFileRolloverStrategy) {
            this.fileName = ((DirectFileRolloverStrategy)this.rolloverStrategy).getCurrentFileName(this);
        }
        return this.fileName;
    }
    
    public FileExtension getFileExtension() {
        return this.fileExtension;
    }
    
    @Override
    protected synchronized void write(final byte[] bytes, final int offset, final int length, final boolean immediateFlush) {
        super.write(bytes, offset, length, immediateFlush);
    }
    
    @Override
    protected synchronized void writeToDestination(final byte[] bytes, final int offset, final int length) {
        this.size += length;
        super.writeToDestination(bytes, offset, length);
    }
    
    public boolean isRenameEmptyFiles() {
        return this.renameEmptyFiles;
    }
    
    public void setRenameEmptyFiles(final boolean renameEmptyFiles) {
        this.renameEmptyFiles = renameEmptyFiles;
    }
    
    public long getFileSize() {
        return this.size + this.byteBuffer.position();
    }
    
    public long getFileTime() {
        return this.initialTime;
    }
    
    public synchronized void checkRollover(final LogEvent event) {
        if (this.triggeringPolicy.isTriggeringEvent(event)) {
            this.rollover();
        }
    }
    
    @Override
    public boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        RollingFileManager.LOGGER.debug("Shutting down RollingFileManager {}" + this.getName());
        boolean stopped = true;
        if (this.triggeringPolicy instanceof LifeCycle2) {
            stopped &= ((LifeCycle2)this.triggeringPolicy).stop(timeout, timeUnit);
        }
        else if (this.triggeringPolicy instanceof LifeCycle) {
            ((LifeCycle)this.triggeringPolicy).stop();
            stopped &= true;
        }
        final boolean status = super.releaseSub(timeout, timeUnit) && stopped;
        this.asyncExecutor.shutdown();
        try {
            final long millis = timeUnit.toMillis(timeout);
            final long waitInterval = (100L < millis) ? millis : 100L;
            for (int count = 1; count <= 3 && !this.asyncExecutor.isTerminated(); ++count) {
                this.asyncExecutor.awaitTermination(waitInterval * count, TimeUnit.MILLISECONDS);
            }
            if (this.asyncExecutor.isTerminated()) {
                RollingFileManager.LOGGER.debug("All asynchronous threads have terminated");
            }
            else {
                this.asyncExecutor.shutdownNow();
                try {
                    this.asyncExecutor.awaitTermination(timeout, timeUnit);
                    if (this.asyncExecutor.isTerminated()) {
                        RollingFileManager.LOGGER.debug("All asynchronous threads have terminated");
                    }
                    else {
                        RollingFileManager.LOGGER.debug("RollingFileManager shutting down but some asynchronous services may not have completed");
                    }
                }
                catch (final InterruptedException inner) {
                    RollingFileManager.LOGGER.warn("RollingFileManager stopped but some asynchronous services may not have completed.");
                }
            }
        }
        catch (final InterruptedException ie) {
            this.asyncExecutor.shutdownNow();
            try {
                this.asyncExecutor.awaitTermination(timeout, timeUnit);
                if (this.asyncExecutor.isTerminated()) {
                    RollingFileManager.LOGGER.debug("All asynchronous threads have terminated");
                }
            }
            catch (final InterruptedException inner2) {
                RollingFileManager.LOGGER.warn("RollingFileManager stopped but some asynchronous services may not have completed.");
            }
            Thread.currentThread().interrupt();
        }
        RollingFileManager.LOGGER.debug("RollingFileManager shutdown completed with status {}", (Object)status);
        return status;
    }
    
    public synchronized void rollover() {
        if (!this.hasOutputStream()) {
            return;
        }
        if (this.rollover(this.rolloverStrategy)) {
            try {
                this.size = 0L;
                this.initialTime = System.currentTimeMillis();
                this.createFileAfterRollover();
            }
            catch (final IOException e) {
                this.logError("Failed to create file after rollover", e);
            }
        }
    }
    
    protected void createFileAfterRollover() throws IOException {
        this.setOutputStream(this.createOutputStream());
    }
    
    public PatternProcessor getPatternProcessor() {
        return this.patternProcessor;
    }
    
    public void setTriggeringPolicy(final TriggeringPolicy triggeringPolicy) {
        triggeringPolicy.initialize(this);
        final TriggeringPolicy policy = this.triggeringPolicy;
        int count = 0;
        boolean policyUpdated = false;
        do {
            ++count;
        } while (!(policyUpdated = RollingFileManager.triggeringPolicyUpdater.compareAndSet(this, this.triggeringPolicy, triggeringPolicy)) && count < 3);
        if (policyUpdated) {
            if (triggeringPolicy instanceof LifeCycle) {
                ((LifeCycle)triggeringPolicy).start();
            }
            if (policy instanceof LifeCycle) {
                ((LifeCycle)policy).stop();
            }
        }
        else if (triggeringPolicy instanceof LifeCycle) {
            ((LifeCycle)triggeringPolicy).stop();
        }
    }
    
    public void setRolloverStrategy(final RolloverStrategy rolloverStrategy) {
        RollingFileManager.rolloverStrategyUpdater.compareAndSet(this, this.rolloverStrategy, rolloverStrategy);
    }
    
    public <T extends TriggeringPolicy> T getTriggeringPolicy() {
        return (T)this.triggeringPolicy;
    }
    
    public RolloverStrategy getRolloverStrategy() {
        return this.rolloverStrategy;
    }
    
    private boolean rollover(final RolloverStrategy strategy) {
        boolean releaseRequired = false;
        try {
            this.semaphore.acquire();
            releaseRequired = true;
        }
        catch (final InterruptedException e) {
            this.logError("Thread interrupted while attempting to check rollover", e);
            return false;
        }
        boolean success = true;
        try {
            final RolloverDescription descriptor = strategy.rollover(this);
            if (descriptor != null) {
                this.writeFooter();
                this.closeOutputStream();
                if (descriptor.getSynchronous() != null) {
                    RollingFileManager.LOGGER.debug("RollingFileManager executing synchronous {}", descriptor.getSynchronous());
                    try {
                        success = descriptor.getSynchronous().execute();
                    }
                    catch (final Exception ex) {
                        success = false;
                        this.logError("Caught error in synchronous task", ex);
                    }
                }
                if (success && descriptor.getAsynchronous() != null) {
                    RollingFileManager.LOGGER.debug("RollingFileManager executing async {}", descriptor.getAsynchronous());
                    this.asyncExecutor.execute(new AsyncAction(descriptor.getAsynchronous(), this));
                    releaseRequired = false;
                }
                return true;
            }
            return false;
        }
        finally {
            if (releaseRequired) {
                this.semaphore.release();
            }
        }
    }
    
    @Override
    public void updateData(final Object data) {
        final FactoryData factoryData = (FactoryData)data;
        this.setRolloverStrategy(factoryData.getRolloverStrategy());
        this.setTriggeringPolicy(factoryData.getTriggeringPolicy());
    }
    
    static {
        RollingFileManager.factory = new RollingFileManagerFactory();
        triggeringPolicyUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, TriggeringPolicy.class, "triggeringPolicy");
        rolloverStrategyUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, RolloverStrategy.class, "rolloverStrategy");
    }
    
    private static class AsyncAction extends AbstractAction
    {
        private final Action action;
        private final RollingFileManager manager;
        
        public AsyncAction(final Action act, final RollingFileManager manager) {
            this.action = act;
            this.manager = manager;
        }
        
        @Override
        public boolean execute() throws IOException {
            try {
                return this.action.execute();
            }
            finally {
                this.manager.semaphore.release();
            }
        }
        
        @Override
        public void close() {
            this.action.close();
        }
        
        @Override
        public boolean isComplete() {
            return this.action.isComplete();
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(super.toString());
            builder.append("[action=");
            builder.append(this.action);
            builder.append(", manager=");
            builder.append(this.manager);
            builder.append(", isComplete()=");
            builder.append(this.isComplete());
            builder.append(", isInterrupted()=");
            builder.append(this.isInterrupted());
            builder.append("]");
            return builder.toString();
        }
    }
    
    private static class FactoryData extends ConfigurationFactoryData
    {
        private final String fileName;
        private final String pattern;
        private final boolean append;
        private final boolean bufferedIO;
        private final int bufferSize;
        private final boolean immediateFlush;
        private final boolean createOnDemand;
        private final TriggeringPolicy policy;
        private final RolloverStrategy strategy;
        private final String advertiseURI;
        private final Layout<? extends Serializable> layout;
        
        public FactoryData(final String fileName, final String pattern, final boolean append, final boolean bufferedIO, final TriggeringPolicy policy, final RolloverStrategy strategy, final String advertiseURI, final Layout<? extends Serializable> layout, final int bufferSize, final boolean immediateFlush, final boolean createOnDemand, final Configuration configuration) {
            super(configuration);
            this.fileName = fileName;
            this.pattern = pattern;
            this.append = append;
            this.bufferedIO = bufferedIO;
            this.bufferSize = bufferSize;
            this.policy = policy;
            this.strategy = strategy;
            this.advertiseURI = advertiseURI;
            this.layout = layout;
            this.immediateFlush = immediateFlush;
            this.createOnDemand = createOnDemand;
        }
        
        public TriggeringPolicy getTriggeringPolicy() {
            return this.policy;
        }
        
        public RolloverStrategy getRolloverStrategy() {
            return this.strategy;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(super.toString());
            builder.append("[pattern=");
            builder.append(this.pattern);
            builder.append(", append=");
            builder.append(this.append);
            builder.append(", bufferedIO=");
            builder.append(this.bufferedIO);
            builder.append(", bufferSize=");
            builder.append(this.bufferSize);
            builder.append(", policy=");
            builder.append(this.policy);
            builder.append(", strategy=");
            builder.append(this.strategy);
            builder.append(", advertiseURI=");
            builder.append(this.advertiseURI);
            builder.append(", layout=");
            builder.append(this.layout);
            builder.append("]");
            return builder.toString();
        }
    }
    
    private static class RollingFileManagerFactory implements ManagerFactory<RollingFileManager, FactoryData>
    {
        @Override
        public RollingFileManager createManager(final String name, final FactoryData data) {
            long size = 0L;
            boolean writeHeader = !data.append;
            File file = null;
            if (data.fileName != null) {
                file = new File(data.fileName);
                writeHeader = (!data.append || !file.exists());
                try {
                    FileUtils.makeParentDirs(file);
                    final boolean created = !data.createOnDemand && file.createNewFile();
                    RollingFileManager.LOGGER.trace("New file '{}' created = {}", name, created);
                }
                catch (final IOException ioe) {
                    RollingFileManager.LOGGER.error("Unable to create file " + name, ioe);
                    return null;
                }
                size = (data.append ? file.length() : 0L);
            }
            try {
                final int actualSize = data.bufferedIO ? data.bufferSize : Constants.ENCODER_BYTE_BUFFER_SIZE;
                final ByteBuffer buffer = ByteBuffer.wrap(new byte[actualSize]);
                final OutputStream os = (data.createOnDemand || data.fileName == null) ? null : new FileOutputStream(data.fileName, data.append);
                final long time = (data.createOnDemand || file == null) ? System.currentTimeMillis() : file.lastModified();
                return new RollingFileManager(data.getLoggerContext(), data.fileName, data.pattern, os, data.append, data.createOnDemand, size, time, data.policy, data.strategy, data.advertiseURI, data.layout, writeHeader, buffer);
            }
            catch (final IOException ex) {
                RollingFileManager.LOGGER.error("RollingFileManager (" + name + ") " + ex, ex);
                return null;
            }
        }
    }
    
    private static class EmptyQueue extends ArrayBlockingQueue<Runnable>
    {
        EmptyQueue() {
            super(1);
        }
        
        @Override
        public int remainingCapacity() {
            return 0;
        }
        
        @Override
        public boolean add(final Runnable runnable) {
            throw new IllegalStateException("Queue is full");
        }
        
        @Override
        public void put(final Runnable runnable) throws InterruptedException {
            throw new InterruptedException("Unable to insert into queue");
        }
        
        @Override
        public boolean offer(final Runnable runnable, final long timeout, final TimeUnit timeUnit) throws InterruptedException {
            Thread.sleep(timeUnit.toMillis(timeout));
            return false;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Runnable> collection) {
            if (collection.size() > 0) {
                throw new IllegalArgumentException("Too many items in collection");
            }
            return false;
        }
    }
}
