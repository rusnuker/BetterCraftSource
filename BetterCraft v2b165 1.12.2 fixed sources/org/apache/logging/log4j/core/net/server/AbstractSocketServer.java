// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.server;

import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import java.io.IOException;
import java.net.URL;
import java.io.FileNotFoundException;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import java.io.FileInputStream;
import java.io.File;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.config.Configuration;
import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.apache.logging.log4j.core.util.InetAddressConverter;
import java.net.InetAddress;
import com.beust.jcommander.validators.PositiveInteger;
import com.beust.jcommander.Parameter;
import org.apache.logging.log4j.core.util.BasicCommandLineArguments;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.logging.log4j.core.util.Log4jThread;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEventListener;
import java.io.InputStream;

public abstract class AbstractSocketServer<T extends InputStream> extends LogEventListener implements Runnable
{
    protected static final int MAX_PORT = 65534;
    private volatile boolean active;
    protected final LogEventBridge<T> logEventInput;
    protected final Logger logger;
    
    public AbstractSocketServer(final int port, final LogEventBridge<T> logEventInput) {
        this.active = true;
        this.logger = LogManager.getLogger(this.getClass().getName() + '.' + port);
        this.logEventInput = Objects.requireNonNull(logEventInput, "LogEventInput");
    }
    
    protected boolean isActive() {
        return this.active;
    }
    
    protected void setActive(final boolean isActive) {
        this.active = isActive;
    }
    
    public Thread startNewThread() {
        final Thread thread = new Log4jThread(this);
        thread.start();
        return thread;
    }
    
    public abstract void shutdown() throws Exception;
    
    public void awaitTermination(final Thread serverThread) throws Exception {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        do {
            line = reader.readLine();
        } while (line != null && !line.equalsIgnoreCase("quit") && !line.equalsIgnoreCase("stop") && !line.equalsIgnoreCase("exit"));
        this.shutdown();
        serverThread.join();
    }
    
    protected static class CommandLineArguments extends BasicCommandLineArguments
    {
        @Parameter(names = { "--config", "-c" }, description = "Log4j configuration file location (path or URL).")
        private String configLocation;
        @Parameter(names = { "--interactive", "-i" }, description = "Accepts commands on standard input (\"exit\" is the only command).")
        private boolean interactive;
        @Parameter(names = { "--port", "-p" }, validateWith = PositiveInteger.class, description = "Server socket port.")
        private int port;
        @Parameter(names = { "--localbindaddress", "-a" }, converter = InetAddressConverter.class, description = "Server socket local bind address.")
        private InetAddress localBindAddress;
        
        String getConfigLocation() {
            return this.configLocation;
        }
        
        int getPort() {
            return this.port;
        }
        
        protected boolean isInteractive() {
            return this.interactive;
        }
        
        void setConfigLocation(final String configLocation) {
            this.configLocation = configLocation;
        }
        
        void setInteractive(final boolean interactive) {
            this.interactive = interactive;
        }
        
        void setPort(final int port) {
            this.port = port;
        }
        
        InetAddress getLocalBindAddress() {
            return this.localBindAddress;
        }
        
        void setLocalBindAddress(final InetAddress localBindAddress) {
            this.localBindAddress = localBindAddress;
        }
    }
    
    protected static class ServerConfigurationFactory extends XmlConfigurationFactory
    {
        private final String path;
        
        public ServerConfigurationFactory(final String path) {
            this.path = path;
        }
        
        @Override
        public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
            if (Strings.isNotEmpty(this.path)) {
                File file = null;
                ConfigurationSource source = null;
                try {
                    file = new File(this.path);
                    final FileInputStream is = new FileInputStream(file);
                    source = new ConfigurationSource(is, file);
                }
                catch (final FileNotFoundException ex) {}
                if (source == null) {
                    try {
                        final URL url = new URL(this.path);
                        source = new ConfigurationSource(url.openStream(), url);
                    }
                    catch (final IOException ex2) {}
                }
                try {
                    if (source != null) {
                        return new XmlConfiguration(loggerContext, source);
                    }
                }
                catch (final Exception ex3) {}
                System.err.println("Unable to process configuration at " + this.path + ", using default.");
            }
            return super.getConfiguration(loggerContext, name, configLocation);
        }
    }
}
