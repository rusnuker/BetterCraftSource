// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config;

import java.util.Iterator;
import java.io.File;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import java.util.List;
import org.apache.logging.log4j.core.util.FileWatcher;

public class ConfiguratonFileWatcher implements FileWatcher
{
    private final Reconfigurable reconfigurable;
    private final List<ConfigurationListener> configurationListeners;
    private final Log4jThreadFactory threadFactory;
    
    public ConfiguratonFileWatcher(final Reconfigurable reconfigurable, final List<ConfigurationListener> configurationListeners) {
        this.reconfigurable = reconfigurable;
        this.configurationListeners = configurationListeners;
        this.threadFactory = Log4jThreadFactory.createDaemonThreadFactory("ConfiguratonFileWatcher");
    }
    
    public List<ConfigurationListener> getListeners() {
        return this.configurationListeners;
    }
    
    @Override
    public void fileModified(final File file) {
        for (final ConfigurationListener configurationListener : this.configurationListeners) {
            final Thread thread = this.threadFactory.newThread(new ReconfigurationRunnable(configurationListener, this.reconfigurable));
            thread.start();
        }
    }
    
    private static class ReconfigurationRunnable implements Runnable
    {
        private final ConfigurationListener configurationListener;
        private final Reconfigurable reconfigurable;
        
        public ReconfigurationRunnable(final ConfigurationListener configurationListener, final Reconfigurable reconfigurable) {
            this.configurationListener = configurationListener;
            this.reconfigurable = reconfigurable;
        }
        
        @Override
        public void run() {
            this.configurationListener.onChange(this.reconfigurable);
        }
    }
}
