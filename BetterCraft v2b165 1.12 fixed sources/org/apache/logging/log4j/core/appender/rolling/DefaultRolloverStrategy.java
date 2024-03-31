// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.SortedMap;
import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
import java.io.File;
import java.util.Map;
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

@Plugin(name = "DefaultRolloverStrategy", category = "Core", printObject = true)
public class DefaultRolloverStrategy extends AbstractRolloverStrategy
{
    private static final int MIN_WINDOW_SIZE = 1;
    private static final int DEFAULT_WINDOW_SIZE = 7;
    private final int maxIndex;
    private final int minIndex;
    private final boolean useMax;
    private final int compressionLevel;
    private final List<Action> customActions;
    private final boolean stopCustomActionsOnError;
    
    @PluginFactory
    public static DefaultRolloverStrategy createStrategy(@PluginAttribute("max") final String max, @PluginAttribute("min") final String min, @PluginAttribute("fileIndex") final String fileIndex, @PluginAttribute("compressionLevel") final String compressionLevelStr, @PluginElement("Actions") final Action[] customActions, @PluginAttribute(value = "stopCustomActionsOnError", defaultBoolean = true) final boolean stopCustomActionsOnError, @PluginConfiguration final Configuration config) {
        int minIndex;
        int maxIndex;
        boolean useMax;
        if (fileIndex != null && fileIndex.equalsIgnoreCase("nomax")) {
            minIndex = Integer.MIN_VALUE;
            maxIndex = Integer.MAX_VALUE;
            useMax = false;
        }
        else {
            useMax = (fileIndex == null || fileIndex.equalsIgnoreCase("max"));
            minIndex = 1;
            if (min != null) {
                minIndex = Integer.parseInt(min);
                if (minIndex < 1) {
                    DefaultRolloverStrategy.LOGGER.error("Minimum window size too small. Limited to 1");
                    minIndex = 1;
                }
            }
            maxIndex = 7;
            if (max != null) {
                maxIndex = Integer.parseInt(max);
                if (maxIndex < minIndex) {
                    maxIndex = ((minIndex < 7) ? 7 : minIndex);
                    DefaultRolloverStrategy.LOGGER.error("Maximum window size must be greater than the minimum windows size. Set to " + maxIndex);
                }
            }
        }
        final int compressionLevel = Integers.parseInt(compressionLevelStr, -1);
        return new DefaultRolloverStrategy(minIndex, maxIndex, useMax, compressionLevel, config.getStrSubstitutor(), customActions, stopCustomActionsOnError);
    }
    
    protected DefaultRolloverStrategy(final int minIndex, final int maxIndex, final boolean useMax, final int compressionLevel, final StrSubstitutor strSubstitutor, final Action[] customActions, final boolean stopCustomActionsOnError) {
        super(strSubstitutor);
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
        this.useMax = useMax;
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
    
    public int getMaxIndex() {
        return this.maxIndex;
    }
    
    public int getMinIndex() {
        return this.minIndex;
    }
    
    public boolean isStopCustomActionsOnError() {
        return this.stopCustomActionsOnError;
    }
    
    public boolean isUseMax() {
        return this.useMax;
    }
    
    private int purge(final int lowIndex, final int highIndex, final RollingFileManager manager) {
        return this.useMax ? this.purgeAscending(lowIndex, highIndex, manager) : this.purgeDescending(lowIndex, highIndex, manager);
    }
    
    private int purgeAscending(final int lowIndex, final int highIndex, final RollingFileManager manager) {
        final SortedMap<Integer, Path> eligibleFiles = this.getEligibleFiles(manager);
        final int maxFiles = highIndex - lowIndex + 1;
        boolean renameFiles = false;
        while (eligibleFiles.size() >= maxFiles) {
            try {
                DefaultRolloverStrategy.LOGGER.debug("Eligible files: {}", eligibleFiles);
                final Integer key = eligibleFiles.firstKey();
                DefaultRolloverStrategy.LOGGER.debug("Deleting {}", eligibleFiles.get(key).toFile().getAbsolutePath());
                Files.delete(eligibleFiles.get(key));
                eligibleFiles.remove(key);
                renameFiles = true;
                continue;
            }
            catch (final IOException ioe) {
                DefaultRolloverStrategy.LOGGER.error("Unable to delete {}, {}", eligibleFiles.firstKey(), ioe.getMessage(), ioe);
            }
            break;
        }
        final StringBuilder buf = new StringBuilder();
        if (renameFiles) {
            for (final Map.Entry<Integer, Path> entry : eligibleFiles.entrySet()) {
                buf.setLength(0);
                manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, entry.getKey() - 1);
                final String currentName = entry.getValue().toFile().getName();
                String renameTo = buf.toString();
                final int suffixLength = this.suffixLength(renameTo);
                if (suffixLength > 0 && this.suffixLength(currentName) == 0) {
                    renameTo = renameTo.substring(0, renameTo.length() - suffixLength);
                }
                final Action action = new FileRenameAction(entry.getValue().toFile(), new File(renameTo), true);
                try {
                    DefaultRolloverStrategy.LOGGER.debug("DefaultRolloverStrategy.purgeAscending executing {}", action);
                    if (!action.execute()) {
                        return -1;
                    }
                    continue;
                }
                catch (final Exception ex) {
                    DefaultRolloverStrategy.LOGGER.warn("Exception during purge in RollingFileAppender", ex);
                    return -1;
                }
            }
        }
        return (eligibleFiles.size() > 0) ? ((eligibleFiles.lastKey() < highIndex) ? (eligibleFiles.lastKey() + 1) : highIndex) : lowIndex;
    }
    
    private int purgeDescending(final int lowIndex, final int highIndex, final RollingFileManager manager) {
        final SortedMap<Integer, Path> eligibleFiles = this.getEligibleFiles(manager, false);
        final int maxFiles = highIndex - lowIndex + 1;
        while (eligibleFiles.size() >= maxFiles) {
            try {
                final Integer key = eligibleFiles.firstKey();
                Files.delete(eligibleFiles.get(key));
                eligibleFiles.remove(key);
                continue;
            }
            catch (final IOException ioe) {
                DefaultRolloverStrategy.LOGGER.error("Unable to delete {}, {}", eligibleFiles.firstKey(), ioe.getMessage(), ioe);
            }
            break;
        }
        final StringBuilder buf = new StringBuilder();
        for (final Map.Entry<Integer, Path> entry : eligibleFiles.entrySet()) {
            buf.setLength(0);
            manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, entry.getKey() + 1);
            final String currentName = entry.getValue().toFile().getName();
            String renameTo = buf.toString();
            final int suffixLength = this.suffixLength(renameTo);
            if (suffixLength > 0 && this.suffixLength(currentName) == 0) {
                renameTo = renameTo.substring(0, renameTo.length() - suffixLength);
            }
            final Action action = new FileRenameAction(entry.getValue().toFile(), new File(renameTo), true);
            try {
                DefaultRolloverStrategy.LOGGER.debug("DefaultRolloverStrategy.purgeDescending executing {}", action);
                if (!action.execute()) {
                    return -1;
                }
                continue;
            }
            catch (final Exception ex) {
                DefaultRolloverStrategy.LOGGER.warn("Exception during purge in RollingFileAppender", ex);
                return -1;
            }
        }
        return lowIndex;
    }
    
    @Override
    public RolloverDescription rollover(final RollingFileManager manager) throws SecurityException {
        int fileIndex;
        if (this.minIndex == Integer.MIN_VALUE) {
            final SortedMap<Integer, Path> eligibleFiles = this.getEligibleFiles(manager);
            fileIndex = ((eligibleFiles.size() > 0) ? (eligibleFiles.lastKey() + 1) : 1);
        }
        else {
            if (this.maxIndex < 0) {
                return null;
            }
            final long startNanos = System.nanoTime();
            fileIndex = this.purge(this.minIndex, this.maxIndex, manager);
            if (fileIndex < 0) {
                return null;
            }
            if (DefaultRolloverStrategy.LOGGER.isTraceEnabled()) {
                final double durationMillis = (double)TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
                DefaultRolloverStrategy.LOGGER.trace("DefaultRolloverStrategy.purge() took {} milliseconds", (Object)durationMillis);
            }
        }
        final StringBuilder buf = new StringBuilder(255);
        manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, fileIndex);
        final String currentFileName = manager.getFileName();
        final String compressedName;
        String renameTo = compressedName = buf.toString();
        Action compressAction = null;
        final FileExtension fileExtension = manager.getFileExtension();
        if (fileExtension != null) {
            renameTo = renameTo.substring(0, renameTo.length() - fileExtension.length());
            compressAction = fileExtension.createCompressAction(renameTo, compressedName, true, this.compressionLevel);
        }
        if (currentFileName.equals(renameTo)) {
            DefaultRolloverStrategy.LOGGER.warn("Attempt to rename file {} to itself will be ignored", currentFileName);
            return new RolloverDescriptionImpl(currentFileName, false, null, null);
        }
        final FileRenameAction renameAction = new FileRenameAction(new File(currentFileName), new File(renameTo), manager.isRenameEmptyFiles());
        final Action asyncAction = this.merge(compressAction, this.customActions, this.stopCustomActionsOnError);
        return new RolloverDescriptionImpl(currentFileName, false, renameAction, asyncAction);
    }
    
    @Override
    public String toString() {
        return "DefaultRolloverStrategy(min=" + this.minIndex + ", max=" + this.maxIndex + ", useMax=" + this.useMax + ")";
    }
}
