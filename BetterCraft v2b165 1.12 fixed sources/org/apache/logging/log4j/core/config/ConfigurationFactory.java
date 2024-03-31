// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.core.config.composite.CompositeConfiguration;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.status.StatusLogger;
import java.net.URISyntaxException;
import java.io.IOException;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.Level;
import java.net.URL;
import java.io.File;
import java.net.MalformedURLException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import org.apache.logging.log4j.core.util.FileUtils;
import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.apache.logging.log4j.util.LoaderUtil;
import java.util.Iterator;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import java.util.Collection;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.util.ArrayList;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.Interpolator;
import java.util.concurrent.locks.Lock;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;

public abstract class ConfigurationFactory extends ConfigurationBuilderFactory
{
    public static final String CONFIGURATION_FACTORY_PROPERTY = "log4j.configurationFactory";
    public static final String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";
    public static final String CATEGORY = "ConfigurationFactory";
    protected static final Logger LOGGER;
    protected static final String TEST_PREFIX = "log4j2-test";
    protected static final String DEFAULT_PREFIX = "log4j2";
    private static final String CLASS_LOADER_SCHEME = "classloader";
    private static final String CLASS_PATH_SCHEME = "classpath";
    private static volatile List<ConfigurationFactory> factories;
    private static ConfigurationFactory configFactory;
    protected final StrSubstitutor substitutor;
    private static final Lock LOCK;
    
    public ConfigurationFactory() {
        this.substitutor = new StrSubstitutor(new Interpolator());
    }
    
    public static ConfigurationFactory getInstance() {
        if (ConfigurationFactory.factories == null) {
            ConfigurationFactory.LOCK.lock();
            try {
                if (ConfigurationFactory.factories == null) {
                    final List<ConfigurationFactory> list = new ArrayList<ConfigurationFactory>();
                    final String factoryClass = PropertiesUtil.getProperties().getStringProperty("log4j.configurationFactory");
                    if (factoryClass != null) {
                        addFactory(list, factoryClass);
                    }
                    final PluginManager manager = new PluginManager("ConfigurationFactory");
                    manager.collectPlugins();
                    final Map<String, PluginType<?>> plugins = manager.getPlugins();
                    final List<Class<? extends ConfigurationFactory>> ordered = new ArrayList<Class<? extends ConfigurationFactory>>(plugins.size());
                    for (final PluginType<?> type : plugins.values()) {
                        try {
                            ordered.add(type.getPluginClass().asSubclass(ConfigurationFactory.class));
                        }
                        catch (final Exception ex) {
                            ConfigurationFactory.LOGGER.warn("Unable to add class {}", type.getPluginClass(), ex);
                        }
                    }
                    Collections.sort(ordered, OrderComparator.getInstance());
                    for (final Class<? extends ConfigurationFactory> clazz : ordered) {
                        addFactory(list, clazz);
                    }
                    ConfigurationFactory.factories = Collections.unmodifiableList((List<? extends ConfigurationFactory>)list);
                }
            }
            finally {
                ConfigurationFactory.LOCK.unlock();
            }
        }
        ConfigurationFactory.LOGGER.debug("Using configurationFactory {}", ConfigurationFactory.configFactory);
        return ConfigurationFactory.configFactory;
    }
    
    private static void addFactory(final Collection<ConfigurationFactory> list, final String factoryClass) {
        try {
            addFactory(list, LoaderUtil.loadClass(factoryClass).asSubclass(ConfigurationFactory.class));
        }
        catch (final Exception ex) {
            ConfigurationFactory.LOGGER.error("Unable to load class {}", factoryClass, ex);
        }
    }
    
    private static void addFactory(final Collection<ConfigurationFactory> list, final Class<? extends ConfigurationFactory> factoryClass) {
        try {
            list.add(ReflectionUtil.instantiate(factoryClass));
        }
        catch (final Exception ex) {
            ConfigurationFactory.LOGGER.error("Unable to create instance of {}", factoryClass.getName(), ex);
        }
    }
    
    public static void setConfigurationFactory(final ConfigurationFactory factory) {
        ConfigurationFactory.configFactory = factory;
    }
    
    public static void resetConfigurationFactory() {
        ConfigurationFactory.configFactory = new Factory();
    }
    
    public static void removeConfigurationFactory(final ConfigurationFactory factory) {
        if (ConfigurationFactory.configFactory == factory) {
            ConfigurationFactory.configFactory = new Factory();
        }
    }
    
    protected abstract String[] getSupportedTypes();
    
    protected boolean isActive() {
        return true;
    }
    
    public abstract Configuration getConfiguration(final LoggerContext p0, final ConfigurationSource p1);
    
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        if (!this.isActive()) {
            return null;
        }
        if (configLocation != null) {
            final ConfigurationSource source = this.getInputFromUri(configLocation);
            if (source != null) {
                return this.getConfiguration(loggerContext, source);
            }
        }
        return null;
    }
    
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation, final ClassLoader loader) {
        if (!this.isActive()) {
            return null;
        }
        if (loader == null) {
            return this.getConfiguration(loggerContext, name, configLocation);
        }
        if (isClassLoaderUri(configLocation)) {
            final String path = extractClassLoaderUriPath(configLocation);
            final ConfigurationSource source = this.getInputFromResource(path, loader);
            if (source != null) {
                final Configuration configuration = this.getConfiguration(loggerContext, source);
                if (configuration != null) {
                    return configuration;
                }
            }
        }
        return this.getConfiguration(loggerContext, name, configLocation);
    }
    
    protected ConfigurationSource getInputFromUri(final URI configLocation) {
        final File configFile = FileUtils.fileFromUri(configLocation);
        if (configFile != null && configFile.exists() && configFile.canRead()) {
            try {
                return new ConfigurationSource(new FileInputStream(configFile), configFile);
            }
            catch (final FileNotFoundException ex) {
                ConfigurationFactory.LOGGER.error("Cannot locate file {}", configLocation.getPath(), ex);
            }
        }
        if (isClassLoaderUri(configLocation)) {
            final ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
            final String path = extractClassLoaderUriPath(configLocation);
            final ConfigurationSource source = this.getInputFromResource(path, loader);
            if (source != null) {
                return source;
            }
        }
        if (!configLocation.isAbsolute()) {
            ConfigurationFactory.LOGGER.error("File not found in file system or classpath: {}", configLocation.toString());
            return null;
        }
        try {
            return new ConfigurationSource(configLocation.toURL().openStream(), configLocation.toURL());
        }
        catch (final MalformedURLException ex2) {
            ConfigurationFactory.LOGGER.error("Invalid URL {}", configLocation.toString(), ex2);
        }
        catch (final Exception ex3) {
            ConfigurationFactory.LOGGER.error("Unable to access {}", configLocation.toString(), ex3);
        }
        return null;
    }
    
    private static boolean isClassLoaderUri(final URI uri) {
        if (uri == null) {
            return false;
        }
        final String scheme = uri.getScheme();
        return scheme == null || scheme.equals("classloader") || scheme.equals("classpath");
    }
    
    private static String extractClassLoaderUriPath(final URI uri) {
        return (uri.getScheme() == null) ? uri.getPath() : uri.getSchemeSpecificPart();
    }
    
    protected ConfigurationSource getInputFromString(final String config, final ClassLoader loader) {
        try {
            final URL url = new URL(config);
            return new ConfigurationSource(url.openStream(), FileUtils.fileFromUri(url.toURI()));
        }
        catch (final Exception ex) {
            final ConfigurationSource source = this.getInputFromResource(config, loader);
            if (source == null) {
                try {
                    final File file = new File(config);
                    return new ConfigurationSource(new FileInputStream(file), file);
                }
                catch (final FileNotFoundException fnfe) {
                    ConfigurationFactory.LOGGER.catching(Level.DEBUG, fnfe);
                }
            }
            return source;
        }
    }
    
    protected ConfigurationSource getInputFromResource(final String resource, final ClassLoader loader) {
        final URL url = Loader.getResource(resource, loader);
        if (url == null) {
            return null;
        }
        InputStream is = null;
        try {
            is = url.openStream();
        }
        catch (final IOException ioe) {
            ConfigurationFactory.LOGGER.catching(Level.DEBUG, ioe);
            return null;
        }
        if (is == null) {
            return null;
        }
        if (FileUtils.isFile(url)) {
            try {
                return new ConfigurationSource(is, FileUtils.fileFromUri(url.toURI()));
            }
            catch (final URISyntaxException ex) {
                ConfigurationFactory.LOGGER.catching(Level.DEBUG, ex);
            }
        }
        return new ConfigurationSource(is, url);
    }
    
    static List<ConfigurationFactory> getFactories() {
        return ConfigurationFactory.factories;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        ConfigurationFactory.factories = null;
        ConfigurationFactory.configFactory = new Factory();
        LOCK = new ReentrantLock();
    }
    
    private static class Factory extends ConfigurationFactory
    {
        private static final String ALL_TYPES = "*";
        
        @Override
        public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
            if (configLocation == null) {
                final String configLocationStr = this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty("log4j.configurationFile"));
                if (configLocationStr != null) {
                    final String[] sources = configLocationStr.split(",");
                    if (sources.length > 1) {
                        final List<AbstractConfiguration> configs = new ArrayList<AbstractConfiguration>();
                        for (final String sourceLocation : sources) {
                            final Configuration config = this.getConfiguration(loggerContext, sourceLocation.trim());
                            if (config == null || !(config instanceof AbstractConfiguration)) {
                                Factory.LOGGER.error("Failed to created configuration at {}", sourceLocation);
                                return null;
                            }
                            configs.add((AbstractConfiguration)config);
                        }
                        return new CompositeConfiguration(configs);
                    }
                    return this.getConfiguration(loggerContext, configLocationStr);
                }
                else {
                    for (final ConfigurationFactory factory : ConfigurationFactory.getFactories()) {
                        final String[] types = factory.getSupportedTypes();
                        if (types != null) {
                            for (final String type : types) {
                                if (type.equals("*")) {
                                    final Configuration config2 = factory.getConfiguration(loggerContext, name, configLocation);
                                    if (config2 != null) {
                                        return config2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                final String configLocationStr = configLocation.toString();
                for (final ConfigurationFactory factory : ConfigurationFactory.getFactories()) {
                    final String[] types = factory.getSupportedTypes();
                    if (types != null) {
                        for (final String type : types) {
                            if (type.equals("*") || configLocationStr.endsWith(type)) {
                                final Configuration config2 = factory.getConfiguration(loggerContext, name, configLocation);
                                if (config2 != null) {
                                    return config2;
                                }
                            }
                        }
                    }
                }
            }
            Configuration config3 = this.getConfiguration(loggerContext, true, name);
            if (config3 == null) {
                config3 = this.getConfiguration(loggerContext, true, null);
                if (config3 == null) {
                    config3 = this.getConfiguration(loggerContext, false, name);
                    if (config3 == null) {
                        config3 = this.getConfiguration(loggerContext, false, null);
                    }
                }
            }
            if (config3 != null) {
                return config3;
            }
            Factory.LOGGER.error("No log4j2 configuration file found. Using default configuration: logging only errors to the console. Set system property 'org.apache.logging.log4j.simplelog.StatusLogger.level' to TRACE to show Log4j2 internal initialization logging.");
            return new DefaultConfiguration();
        }
        
        private Configuration getConfiguration(final LoggerContext loggerContext, final String configLocationStr) {
            ConfigurationSource source = null;
            try {
                source = this.getInputFromUri(NetUtils.toURI(configLocationStr));
            }
            catch (final Exception ex) {
                Factory.LOGGER.catching(Level.DEBUG, ex);
            }
            if (source == null) {
                final ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
                source = this.getInputFromString(configLocationStr, loader);
            }
            if (source != null) {
                for (final ConfigurationFactory factory : ConfigurationFactory.getFactories()) {
                    final String[] types = factory.getSupportedTypes();
                    if (types != null) {
                        for (final String type : types) {
                            if (type.equals("*") || configLocationStr.endsWith(type)) {
                                final Configuration config = factory.getConfiguration(loggerContext, source);
                                if (config != null) {
                                    return config;
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
        
        private Configuration getConfiguration(final LoggerContext loggerContext, final boolean isTest, final String name) {
            final boolean named = Strings.isNotEmpty(name);
            final ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
            for (final ConfigurationFactory factory : ConfigurationFactory.getFactories()) {
                final String prefix = isTest ? "log4j2-test" : "log4j2";
                final String[] types = factory.getSupportedTypes();
                if (types == null) {
                    continue;
                }
                for (final String suffix : types) {
                    if (!suffix.equals("*")) {
                        final String configName = named ? (prefix + name + suffix) : (prefix + suffix);
                        final ConfigurationSource source = this.getInputFromResource(configName, loader);
                        if (source != null) {
                            return factory.getConfiguration(loggerContext, source);
                        }
                    }
                }
            }
            return null;
        }
        
        public String[] getSupportedTypes() {
            return null;
        }
        
        @Override
        public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
            if (source != null) {
                final String config = source.getLocation();
                for (final ConfigurationFactory factory : ConfigurationFactory.getFactories()) {
                    final String[] types = factory.getSupportedTypes();
                    if (types != null) {
                        final String[] arr$ = types;
                        final int len$ = arr$.length;
                        int i$2 = 0;
                        while (i$2 < len$) {
                            final String type = arr$[i$2];
                            if (type.equals("*") || (config != null && config.endsWith(type))) {
                                final Configuration c = factory.getConfiguration(loggerContext, source);
                                if (c != null) {
                                    Factory.LOGGER.debug("Loaded configuration from {}", source);
                                    return c;
                                }
                                Factory.LOGGER.error("Cannot determine the ConfigurationFactory to use for {}", config);
                                return null;
                            }
                            else {
                                ++i$2;
                            }
                        }
                    }
                }
            }
            Factory.LOGGER.error("Cannot process configuration, input source is null");
            return null;
        }
    }
}
