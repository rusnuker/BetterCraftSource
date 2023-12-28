// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.nio.file.DirectoryStream;
import java.io.IOException;
import org.apache.logging.log4j.LoggingException;
import java.nio.file.Files;
import java.util.regex.Pattern;
import java.io.File;
import java.util.TreeMap;
import org.apache.logging.log4j.core.pattern.NotANumber;
import java.nio.file.Path;
import java.util.SortedMap;
import java.util.Collection;
import java.util.ArrayList;
import org.apache.logging.log4j.core.appender.rolling.action.CompositeAction;
import java.util.List;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.Logger;

public abstract class AbstractRolloverStrategy implements RolloverStrategy
{
    protected static final Logger LOGGER;
    protected final StrSubstitutor strSubstitutor;
    
    protected AbstractRolloverStrategy(final StrSubstitutor strSubstitutor) {
        this.strSubstitutor = strSubstitutor;
    }
    
    public StrSubstitutor getStrSubstitutor() {
        return this.strSubstitutor;
    }
    
    protected Action merge(final Action compressAction, final List<Action> custom, final boolean stopOnError) {
        if (custom.isEmpty()) {
            return compressAction;
        }
        if (compressAction == null) {
            return new CompositeAction(custom, stopOnError);
        }
        final List<Action> all = new ArrayList<Action>();
        all.add(compressAction);
        all.addAll(custom);
        return new CompositeAction(all, stopOnError);
    }
    
    protected int suffixLength(final String lowFilename) {
        for (final FileExtension extension : FileExtension.values()) {
            if (extension.isExtensionFor(lowFilename)) {
                return extension.length();
            }
        }
        return 0;
    }
    
    protected SortedMap<Integer, Path> getEligibleFiles(final RollingFileManager manager) {
        return this.getEligibleFiles(manager, true);
    }
    
    protected SortedMap<Integer, Path> getEligibleFiles(final RollingFileManager manager, final boolean isAscending) {
        final StringBuilder buf = new StringBuilder();
        final String pattern = manager.getPatternProcessor().getPattern();
        manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, NotANumber.NAN);
        return this.getEligibleFiles(buf.toString(), pattern, isAscending);
    }
    
    protected SortedMap<Integer, Path> getEligibleFiles(final String path, final String pattern) {
        return this.getEligibleFiles(path, pattern, true);
    }
    
    protected SortedMap<Integer, Path> getEligibleFiles(final String path, final String logfilePattern, final boolean isAscending) {
        final TreeMap<Integer, Path> eligibleFiles = new TreeMap<Integer, Path>();
        final File file = new File(path);
        File parent = file.getParentFile();
        if (parent == null) {
            parent = new File(".");
        }
        else {
            parent.mkdirs();
        }
        if (!logfilePattern.contains("%i")) {
            return eligibleFiles;
        }
        final Path dir = parent.toPath();
        String fileName = file.getName();
        final int suffixLength = this.suffixLength(fileName);
        if (suffixLength > 0) {
            fileName = fileName.substring(0, fileName.length() - suffixLength) + ".*";
        }
        final String filePattern = fileName.replace("\u0000", "(\\d+)");
        final Pattern pattern = Pattern.compile(filePattern);
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (final Path entry : stream) {
                final Matcher matcher = pattern.matcher(entry.toFile().getName());
                if (matcher.matches()) {
                    final Integer index = Integer.parseInt(matcher.group(1));
                    eligibleFiles.put(index, entry);
                }
            }
        }
        catch (final IOException ioe) {
            throw new LoggingException("Error reading folder " + dir + " " + ioe.getMessage(), ioe);
        }
        return isAscending ? eligibleFiles : eligibleFiles.descendingMap();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
