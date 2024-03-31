// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

import java.util.concurrent.TimeUnit;
import java.util.SortedMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DirectWriteRolloverStrategy", category = "Core", printObject = true)
public class DirectWriteRolloverStrategy extends AbstractRolloverStrategy implements DirectFileRolloverStrategy
{
    private static final int DEFAULT_MAX_FILES = 7;
    private final int maxFiles;
    private final int compressionLevel;
    private final List<Action> customActions;
    private final boolean stopCustomActionsOnError;
    private volatile String currentFileName;
    private int nextIndex;
    
    @PluginFactory
    public static DirectWriteRolloverStrategy createStrategy(@PluginAttribute("maxFiles") final String maxFiles, @PluginAttribute("compressionLevel") final String compressionLevelStr, @PluginElement("Actions") final Action[] customActions, @PluginAttribute(value = "stopCustomActionsOnError", defaultBoolean = true) final boolean stopCustomActionsOnError, @PluginConfiguration final Configuration config) {
        int maxIndex = Integer.MAX_VALUE;
        if (maxFiles != null) {
            maxIndex = Integer.parseInt(maxFiles);
            if (maxIndex < 0) {
                maxIndex = Integer.MAX_VALUE;
            }
            else if (maxIndex < 2) {
                DirectWriteRolloverStrategy.LOGGER.error("Maximum files too small. Limited to 7");
                maxIndex = 7;
            }
        }
        final int compressionLevel = Integers.parseInt(compressionLevelStr, -1);
        return new DirectWriteRolloverStrategy(maxIndex, compressionLevel, config.getStrSubstitutor(), customActions, stopCustomActionsOnError);
    }
    
    protected DirectWriteRolloverStrategy(final int maxFiles, final int compressionLevel, final StrSubstitutor strSubstitutor, final Action[] customActions, final boolean stopCustomActionsOnError) {
        super(strSubstitutor);
        this.nextIndex = -1;
        this.maxFiles = maxFiles;
        this.compressionLevel = compressionLevel;
        this.stopCustomActionsOnError = stopCustomActionsOnError;
        this.customActions = ((customActions == null) ? Collections.emptyList() : Arrays.asList(customActions));
    }
    
    public int getCompressionLevel() {
        return this.compressionLevel;
    }
    
    public List<Action> getCustomActions() {
        return this.customActions;
    }
    
    public int getMaxFiles() {
        return this.maxFiles;
    }
    
    public boolean isStopCustomActionsOnError() {
        return this.stopCustomActionsOnError;
    }
    
    private int purge(final RollingFileManager manager) {
        final SortedMap<Integer, Path> eligibleFiles = this.getEligibleFiles(manager);
        DirectWriteRolloverStrategy.LOGGER.debug("Found {} eligible files, max is  {}", (Object)eligibleFiles.size(), this.maxFiles);
        while (eligibleFiles.size() >= this.maxFiles) {
            try {
                final Integer key = eligibleFiles.firstKey();
                Files.delete(eligibleFiles.get(key));
                eligibleFiles.remove(key);
                continue;
            }
            catch (final IOException ioe) {
                DirectWriteRolloverStrategy.LOGGER.error("Unable to delete {}", eligibleFiles.firstKey(), ioe);
            }
            break;
        }
        return (eligibleFiles.size() > 0) ? eligibleFiles.lastKey() : 1;
    }
    
    @Override
    public String getCurrentFileName(final RollingFileManager manager) {
        if (this.currentFileName == null) {
            final SortedMap<Integer, Path> eligibleFiles = this.getEligibleFiles(manager);
            final int fileIndex = (eligibleFiles.size() > 0) ? ((this.nextIndex > 0) ? this.nextIndex : eligibleFiles.size()) : 1;
            final StringBuilder buf = new StringBuilder(255);
            manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, true, fileIndex);
            final int suffixLength = this.suffixLength(buf.toString());
            final String name = (suffixLength > 0) ? buf.substring(0, buf.length() - suffixLength) : buf.toString();
            this.currentFileName = name;
        }
        return this.currentFileName;
    }
    
    @Override
    public RolloverDescription rollover(final RollingFileManager manager) throws SecurityException {
        DirectWriteRolloverStrategy.LOGGER.debug("Rolling " + this.currentFileName);
        if (this.maxFiles < 0) {
            return null;
        }
        final long startNanos = System.nanoTime();
        final int fileIndex = this.purge(manager);
        if (DirectWriteRolloverStrategy.LOGGER.isTraceEnabled()) {
            final double durationMillis = (double)TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
            DirectWriteRolloverStrategy.LOGGER.trace("DirectWriteRolloverStrategy.purge() took {} milliseconds", (Object)durationMillis);
        }
        Action compressAction = null;
        final String sourceName = this.currentFileName;
        this.currentFileName = null;
        this.nextIndex = fileIndex + 1;
        final FileExtension fileExtension = manager.getFileExtension();
        if (fileExtension != null) {
            compressAction = fileExtension.createCompressAction(sourceName, sourceName + fileExtension.getExtension(), true, this.compressionLevel);
        }
        final Action asyncAction = this.merge(compressAction, this.customActions, this.stopCustomActionsOnError);
        return new RolloverDescriptionImpl(sourceName, false, null, asyncAction);
    }
    
    @Override
    public String toString() {
        return "DirectWriteRolloverStrategy(maxFiles=" + this.maxFiles + ')';
    }
}
