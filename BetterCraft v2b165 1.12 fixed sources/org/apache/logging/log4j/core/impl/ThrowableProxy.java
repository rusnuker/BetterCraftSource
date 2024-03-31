// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.ArrayList;
import java.net.URL;
import java.security.CodeSource;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.util.LoaderUtil;
import java.util.Iterator;
import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
import org.apache.logging.log4j.core.pattern.TextRenderer;
import java.util.List;
import java.util.Arrays;
import java.util.Stack;
import java.util.Map;
import java.util.HashSet;
import org.apache.logging.log4j.util.ReflectionUtil;
import java.util.HashMap;
import java.util.Set;
import java.io.Serializable;

public class ThrowableProxy implements Serializable
{
    private static final String TAB = "\t";
    private static final String CAUSED_BY_LABEL = "Caused by: ";
    private static final String SUPPRESSED_LABEL = "Suppressed: ";
    private static final String WRAPPED_BY_LABEL = "Wrapped by: ";
    private static final ThrowableProxy[] EMPTY_THROWABLE_PROXY_ARRAY;
    private static final char EOL = '\n';
    private static final String EOL_STR;
    private static final long serialVersionUID = -2752771578252251910L;
    private final ThrowableProxy causeProxy;
    private int commonElementCount;
    private final ExtendedStackTraceElement[] extendedStackTrace;
    private final String localizedMessage;
    private final String message;
    private final String name;
    private final ThrowableProxy[] suppressedProxies;
    private final transient Throwable throwable;
    
    private ThrowableProxy() {
        this.throwable = null;
        this.name = null;
        this.extendedStackTrace = null;
        this.causeProxy = null;
        this.message = null;
        this.localizedMessage = null;
        this.suppressedProxies = ThrowableProxy.EMPTY_THROWABLE_PROXY_ARRAY;
    }
    
    public ThrowableProxy(final Throwable throwable) {
        this(throwable, null);
    }
    
    private ThrowableProxy(final Throwable throwable, final Set<Throwable> visited) {
        this.throwable = throwable;
        this.name = throwable.getClass().getName();
        this.message = throwable.getMessage();
        this.localizedMessage = throwable.getLocalizedMessage();
        final Map<String, CacheEntry> map = new HashMap<String, CacheEntry>();
        final Stack<Class<?>> stack = ReflectionUtil.getCurrentStackTrace();
        this.extendedStackTrace = this.toExtendedStackTrace(stack, map, null, throwable.getStackTrace());
        final Throwable throwableCause = throwable.getCause();
        final Set<Throwable> causeVisited = new HashSet<Throwable>(1);
        this.causeProxy = ((throwableCause == null) ? null : new ThrowableProxy(throwable, stack, map, throwableCause, visited, causeVisited));
        this.suppressedProxies = this.toSuppressedProxies(throwable, visited);
    }
    
    private ThrowableProxy(final Throwable parent, final Stack<Class<?>> stack, final Map<String, CacheEntry> map, final Throwable cause, final Set<Throwable> suppressedVisited, final Set<Throwable> causeVisited) {
        causeVisited.add(cause);
        this.throwable = cause;
        this.name = cause.getClass().getName();
        this.message = this.throwable.getMessage();
        this.localizedMessage = this.throwable.getLocalizedMessage();
        this.extendedStackTrace = this.toExtendedStackTrace(stack, map, parent.getStackTrace(), cause.getStackTrace());
        final Throwable causeCause = cause.getCause();
        this.causeProxy = ((causeCause == null || causeVisited.contains(causeCause)) ? null : new ThrowableProxy(parent, stack, map, causeCause, suppressedVisited, causeVisited));
        this.suppressedProxies = this.toSuppressedProxies(cause, suppressedVisited);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final ThrowableProxy other = (ThrowableProxy)obj;
        if (this.causeProxy == null) {
            if (other.causeProxy != null) {
                return false;
            }
        }
        else if (!this.causeProxy.equals(other.causeProxy)) {
            return false;
        }
        if (this.commonElementCount != other.commonElementCount) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(other.name)) {
            return false;
        }
        return Arrays.equals(this.extendedStackTrace, other.extendedStackTrace) && Arrays.equals(this.suppressedProxies, other.suppressedProxies);
    }
    
    private void formatCause(final StringBuilder sb, final String prefix, final ThrowableProxy cause, final List<String> ignorePackages, final TextRenderer textRenderer) {
        this.formatThrowableProxy(sb, prefix, "Caused by: ", cause, ignorePackages, textRenderer);
    }
    
    private void formatThrowableProxy(final StringBuilder sb, final String prefix, final String causeLabel, final ThrowableProxy throwableProxy, final List<String> ignorePackages, final TextRenderer textRenderer) {
        if (throwableProxy == null) {
            return;
        }
        textRenderer.render(prefix, sb, "Prefix");
        textRenderer.render(causeLabel, sb, "CauseLabel");
        throwableProxy.renderOn(sb, textRenderer);
        textRenderer.render(ThrowableProxy.EOL_STR, sb, "Text");
        this.formatElements(sb, prefix, throwableProxy.commonElementCount, throwableProxy.getStackTrace(), throwableProxy.extendedStackTrace, ignorePackages, textRenderer);
        this.formatSuppressed(sb, prefix + "\t", throwableProxy.suppressedProxies, ignorePackages, textRenderer);
        this.formatCause(sb, prefix, throwableProxy.causeProxy, ignorePackages, textRenderer);
    }
    
    void renderOn(final StringBuilder output, final TextRenderer textRenderer) {
        final String msg = this.message;
        textRenderer.render(this.name, output, "Name");
        if (msg != null) {
            textRenderer.render(": ", output, "NameMessageSeparator");
            textRenderer.render(msg, output, "Message");
        }
    }
    
    private void formatSuppressed(final StringBuilder sb, final String prefix, final ThrowableProxy[] suppressedProxies, final List<String> ignorePackages, final TextRenderer textRenderer) {
        if (suppressedProxies == null) {
            return;
        }
        for (final ThrowableProxy suppressedProxy : suppressedProxies) {
            this.formatThrowableProxy(sb, prefix, "Suppressed: ", suppressedProxy, ignorePackages, textRenderer);
        }
    }
    
    private void formatElements(final StringBuilder sb, final String prefix, final int commonCount, final StackTraceElement[] causedTrace, final ExtendedStackTraceElement[] extStackTrace, final List<String> ignorePackages, final TextRenderer textRenderer) {
        if (ignorePackages == null || ignorePackages.isEmpty()) {
            for (final ExtendedStackTraceElement element : extStackTrace) {
                this.formatEntry(element, sb, prefix, textRenderer);
            }
        }
        else {
            int count = 0;
            for (int i = 0; i < extStackTrace.length; ++i) {
                if (!this.ignoreElement(causedTrace[i], ignorePackages)) {
                    if (count > 0) {
                        this.appendSuppressedCount(sb, prefix, count, textRenderer);
                        count = 0;
                    }
                    this.formatEntry(extStackTrace[i], sb, prefix, textRenderer);
                }
                else {
                    ++count;
                }
            }
            if (count > 0) {
                this.appendSuppressedCount(sb, prefix, count, textRenderer);
            }
        }
        if (commonCount != 0) {
            textRenderer.render(prefix, sb, "Prefix");
            textRenderer.render("\t... ", sb, "More");
            textRenderer.render(Integer.toString(commonCount), sb, "More");
            textRenderer.render(" more", sb, "More");
            textRenderer.render(ThrowableProxy.EOL_STR, sb, "Text");
        }
    }
    
    private void appendSuppressedCount(final StringBuilder sb, final String prefix, final int count, final TextRenderer textRenderer) {
        textRenderer.render(prefix, sb, "Prefix");
        if (count == 1) {
            textRenderer.render("\t... ", sb, "Suppressed");
        }
        else {
            textRenderer.render("\t... suppressed ", sb, "Suppressed");
            textRenderer.render(Integer.toString(count), sb, "Suppressed");
            textRenderer.render(" lines", sb, "Suppressed");
        }
        textRenderer.render(ThrowableProxy.EOL_STR, sb, "Text");
    }
    
    private void formatEntry(final ExtendedStackTraceElement extStackTraceElement, final StringBuilder sb, final String prefix, final TextRenderer textRenderer) {
        textRenderer.render(prefix, sb, "Prefix");
        textRenderer.render("\tat ", sb, "At");
        extStackTraceElement.renderOn(sb, textRenderer);
        textRenderer.render(ThrowableProxy.EOL_STR, sb, "Text");
    }
    
    public void formatWrapper(final StringBuilder sb, final ThrowableProxy cause) {
        this.formatWrapper(sb, cause, null, PlainTextRenderer.getInstance());
    }
    
    public void formatWrapper(final StringBuilder sb, final ThrowableProxy cause, final List<String> ignorePackages) {
        this.formatWrapper(sb, cause, ignorePackages, PlainTextRenderer.getInstance());
    }
    
    public void formatWrapper(final StringBuilder sb, final ThrowableProxy cause, final List<String> ignorePackages, final TextRenderer textRenderer) {
        final Throwable caused = (cause.getCauseProxy() != null) ? cause.getCauseProxy().getThrowable() : null;
        if (caused != null) {
            this.formatWrapper(sb, cause.causeProxy, ignorePackages, textRenderer);
            sb.append("Wrapped by: ");
        }
        cause.renderOn(sb, textRenderer);
        textRenderer.render(ThrowableProxy.EOL_STR, sb, "Text");
        this.formatElements(sb, "", cause.commonElementCount, cause.getThrowable().getStackTrace(), cause.extendedStackTrace, ignorePackages, textRenderer);
    }
    
    public ThrowableProxy getCauseProxy() {
        return this.causeProxy;
    }
    
    public String getCauseStackTraceAsString() {
        return this.getCauseStackTraceAsString(null, PlainTextRenderer.getInstance());
    }
    
    public String getCauseStackTraceAsString(final List<String> packages) {
        return this.getCauseStackTraceAsString(packages, PlainTextRenderer.getInstance());
    }
    
    public String getCauseStackTraceAsString(final List<String> ignorePackages, final TextRenderer textRenderer) {
        final StringBuilder sb = new StringBuilder();
        if (this.causeProxy != null) {
            this.formatWrapper(sb, this.causeProxy, ignorePackages, textRenderer);
            sb.append("Wrapped by: ");
        }
        this.renderOn(sb, textRenderer);
        textRenderer.render(ThrowableProxy.EOL_STR, sb, "Text");
        this.formatElements(sb, "", 0, this.throwable.getStackTrace(), this.extendedStackTrace, ignorePackages, textRenderer);
        return sb.toString();
    }
    
    public int getCommonElementCount() {
        return this.commonElementCount;
    }
    
    public ExtendedStackTraceElement[] getExtendedStackTrace() {
        return this.extendedStackTrace;
    }
    
    public String getExtendedStackTraceAsString() {
        return this.getExtendedStackTraceAsString(null, PlainTextRenderer.getInstance());
    }
    
    public String getExtendedStackTraceAsString(final List<String> ignorePackages) {
        return this.getExtendedStackTraceAsString(ignorePackages, PlainTextRenderer.getInstance());
    }
    
    public String getExtendedStackTraceAsString(final List<String> ignorePackages, final TextRenderer textRenderer) {
        final StringBuilder sb = new StringBuilder(1024);
        textRenderer.render(this.name, sb, "Name");
        textRenderer.render(": ", sb, "NameMessageSeparator");
        textRenderer.render(this.message, sb, "Message");
        textRenderer.render(ThrowableProxy.EOL_STR, sb, "Text");
        final StackTraceElement[] causedTrace = (StackTraceElement[])((this.throwable != null) ? this.throwable.getStackTrace() : null);
        this.formatElements(sb, "", 0, causedTrace, this.extendedStackTrace, ignorePackages, textRenderer);
        this.formatSuppressed(sb, "\t", this.suppressedProxies, ignorePackages, textRenderer);
        this.formatCause(sb, "", this.causeProxy, ignorePackages, textRenderer);
        return sb.toString();
    }
    
    public String getLocalizedMessage() {
        return this.localizedMessage;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String getName() {
        return this.name;
    }
    
    public StackTraceElement[] getStackTrace() {
        return (StackTraceElement[])((this.throwable == null) ? null : this.throwable.getStackTrace());
    }
    
    public ThrowableProxy[] getSuppressedProxies() {
        return this.suppressedProxies;
    }
    
    public String getSuppressedStackTrace() {
        final ThrowableProxy[] suppressed = this.getSuppressedProxies();
        if (suppressed == null || suppressed.length == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder("Suppressed Stack Trace Elements:").append('\n');
        for (final ThrowableProxy proxy : suppressed) {
            sb.append(proxy.getExtendedStackTraceAsString());
        }
        return sb.toString();
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.causeProxy == null) ? 0 : this.causeProxy.hashCode());
        result = 31 * result + this.commonElementCount;
        result = 31 * result + ((this.extendedStackTrace == null) ? 0 : Arrays.hashCode(this.extendedStackTrace));
        result = 31 * result + ((this.suppressedProxies == null) ? 0 : Arrays.hashCode(this.suppressedProxies));
        result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }
    
    private boolean ignoreElement(final StackTraceElement element, final List<String> ignorePackages) {
        if (ignorePackages != null) {
            final String className = element.getClassName();
            for (final String pkg : ignorePackages) {
                if (className.startsWith(pkg)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private Class<?> loadClass(final ClassLoader lastLoader, final String className) {
        if (lastLoader != null) {
            try {
                final Class<?> clazz = lastLoader.loadClass(className);
                if (clazz != null) {
                    return clazz;
                }
            }
            catch (final Throwable t) {}
        }
        Class<?> clazz;
        try {
            clazz = LoaderUtil.loadClass(className);
        }
        catch (final ClassNotFoundException | NoClassDefFoundError e) {
            return this.loadClass(className);
        }
        catch (final SecurityException e2) {
            return null;
        }
        return clazz;
    }
    
    private Class<?> loadClass(final String className) {
        try {
            return Loader.loadClass(className, this.getClass().getClassLoader());
        }
        catch (final ClassNotFoundException | NoClassDefFoundError | SecurityException e) {
            return null;
        }
    }
    
    private CacheEntry toCacheEntry(final StackTraceElement stackTraceElement, final Class<?> callerClass, final boolean exact) {
        String location = "?";
        String version = "?";
        ClassLoader lastLoader = null;
        if (callerClass != null) {
            try {
                final CodeSource source = callerClass.getProtectionDomain().getCodeSource();
                if (source != null) {
                    final URL locationURL = source.getLocation();
                    if (locationURL != null) {
                        final String str = locationURL.toString().replace('\\', '/');
                        int index = str.lastIndexOf("/");
                        if (index >= 0 && index == str.length() - 1) {
                            index = str.lastIndexOf("/", index - 1);
                            location = str.substring(index + 1);
                        }
                        else {
                            location = str.substring(index + 1);
                        }
                    }
                }
            }
            catch (final Exception ex) {}
            final Package pkg = callerClass.getPackage();
            if (pkg != null) {
                final String ver = pkg.getImplementationVersion();
                if (ver != null) {
                    version = ver;
                }
            }
            lastLoader = callerClass.getClassLoader();
        }
        return new CacheEntry(new ExtendedClassInfo(exact, location, version), lastLoader);
    }
    
    ExtendedStackTraceElement[] toExtendedStackTrace(final Stack<Class<?>> stack, final Map<String, CacheEntry> map, final StackTraceElement[] rootTrace, final StackTraceElement[] stackTrace) {
        int stackLength;
        if (rootTrace != null) {
            int rootIndex;
            int stackIndex;
            for (rootIndex = rootTrace.length - 1, stackIndex = stackTrace.length - 1; rootIndex >= 0 && stackIndex >= 0 && rootTrace[rootIndex].equals(stackTrace[stackIndex]); --rootIndex, --stackIndex) {}
            this.commonElementCount = stackTrace.length - 1 - stackIndex;
            stackLength = stackIndex + 1;
        }
        else {
            this.commonElementCount = 0;
            stackLength = stackTrace.length;
        }
        final ExtendedStackTraceElement[] extStackTrace = new ExtendedStackTraceElement[stackLength];
        Class<?> clazz = stack.isEmpty() ? null : stack.peek();
        ClassLoader lastLoader = null;
        for (int i = stackLength - 1; i >= 0; --i) {
            final StackTraceElement stackTraceElement = stackTrace[i];
            final String className = stackTraceElement.getClassName();
            ExtendedClassInfo extClassInfo;
            if (clazz != null && className.equals(clazz.getName())) {
                final CacheEntry entry = this.toCacheEntry(stackTraceElement, clazz, true);
                extClassInfo = entry.element;
                lastLoader = entry.loader;
                stack.pop();
                clazz = (stack.isEmpty() ? null : stack.peek());
            }
            else {
                final CacheEntry cacheEntry = map.get(className);
                if (cacheEntry != null) {
                    final CacheEntry entry2 = cacheEntry;
                    extClassInfo = entry2.element;
                    if (entry2.loader != null) {
                        lastLoader = entry2.loader;
                    }
                }
                else {
                    final CacheEntry entry2 = this.toCacheEntry(stackTraceElement, this.loadClass(lastLoader, className), false);
                    extClassInfo = entry2.element;
                    map.put(stackTraceElement.toString(), entry2);
                    if (entry2.loader != null) {
                        lastLoader = entry2.loader;
                    }
                }
            }
            extStackTrace[i] = new ExtendedStackTraceElement(stackTraceElement, extClassInfo);
        }
        return extStackTrace;
    }
    
    @Override
    public String toString() {
        final String msg = this.message;
        return (msg != null) ? (this.name + ": " + msg) : this.name;
    }
    
    private ThrowableProxy[] toSuppressedProxies(final Throwable thrown, Set<Throwable> suppressedVisited) {
        try {
            final Throwable[] suppressed = thrown.getSuppressed();
            if (suppressed == null) {
                return ThrowableProxy.EMPTY_THROWABLE_PROXY_ARRAY;
            }
            final List<ThrowableProxy> proxies = new ArrayList<ThrowableProxy>(suppressed.length);
            if (suppressedVisited == null) {
                suppressedVisited = new HashSet<Throwable>(proxies.size());
            }
            for (int i = 0; i < suppressed.length; ++i) {
                final Throwable candidate = suppressed[i];
                if (!suppressedVisited.contains(candidate)) {
                    suppressedVisited.add(candidate);
                    proxies.add(new ThrowableProxy(candidate, suppressedVisited));
                }
            }
            return proxies.toArray(new ThrowableProxy[proxies.size()]);
        }
        catch (final Exception e) {
            StatusLogger.getLogger().error(e);
            return null;
        }
    }
    
    static {
        EMPTY_THROWABLE_PROXY_ARRAY = new ThrowableProxy[0];
        EOL_STR = String.valueOf('\n');
    }
    
    static class CacheEntry
    {
        private final ExtendedClassInfo element;
        private final ClassLoader loader;
        
        public CacheEntry(final ExtendedClassInfo element, final ClassLoader loader) {
            this.element = element;
            this.loader = loader;
        }
    }
}
