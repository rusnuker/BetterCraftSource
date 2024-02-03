// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

import com.google.common.collect.ObjectArrays;
import org.spongepowered.asm.logging.LoggerAdapterConsole;
import java.util.List;
import com.google.common.base.Joiner;
import java.util.ServiceConfigurationError;
import java.util.ArrayList;
import org.spongepowered.asm.logging.ILogger;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.ServiceLoader;

public final class MixinService
{
    private static LogBuffer logBuffer;
    private static MixinService instance;
    private ServiceLoader<IMixinServiceBootstrap> bootstrapServiceLoader;
    private final Set<String> bootedServices;
    private ServiceLoader<IMixinService> serviceLoader;
    private IMixinService service;
    private IGlobalPropertyService propertyService;
    
    private MixinService() {
        this.bootedServices = new HashSet<String>();
        this.service = null;
        this.runBootServices();
    }
    
    private void runBootServices() {
        this.bootstrapServiceLoader = ServiceLoader.load(IMixinServiceBootstrap.class, this.getClass().getClassLoader());
        final Iterator<IMixinServiceBootstrap> iter = this.bootstrapServiceLoader.iterator();
        while (iter.hasNext()) {
            try {
                final IMixinServiceBootstrap bootService = iter.next();
                bootService.bootstrap();
                this.bootedServices.add(bootService.getServiceClassName());
            }
            catch (final ServiceInitialisationException ex) {
                MixinService.logBuffer.debug("Mixin bootstrap service {} is not available: {}", ex.getStackTrace()[0].getClassName(), ex.getMessage());
            }
            catch (final Throwable th) {
                MixinService.logBuffer.debug("Catching {}:{} initialising service", th.getClass().getName(), th.getMessage(), th);
            }
        }
    }
    
    private static MixinService getInstance() {
        if (MixinService.instance == null) {
            MixinService.instance = new MixinService();
        }
        return MixinService.instance;
    }
    
    public static void boot() {
        getInstance();
    }
    
    public static IMixinService getService() {
        return getInstance().getServiceInstance();
    }
    
    private synchronized IMixinService getServiceInstance() {
        if (this.service == null) {
            try {
                this.service = this.initService();
                final ILogger serviceLogger = this.service.getLogger("mixin");
                MixinService.logBuffer.flush(serviceLogger);
            }
            catch (final Error err) {
                final ILogger defaultLogger = getDefaultLogger();
                MixinService.logBuffer.flush(defaultLogger);
                defaultLogger.error(err.getMessage(), err);
                throw err;
            }
        }
        return this.service;
    }
    
    private IMixinService initService() {
        this.serviceLoader = ServiceLoader.load(IMixinService.class, this.getClass().getClassLoader());
        final Iterator<IMixinService> iter = this.serviceLoader.iterator();
        final List<String> badServices = new ArrayList<String>();
        int brokenServiceCount = 0;
        while (iter.hasNext()) {
            try {
                final IMixinService service = iter.next();
                if (this.bootedServices.contains(service.getClass().getName())) {
                    MixinService.logBuffer.debug("MixinService [{}] was successfully booted in {}", service.getName(), this.getClass().getClassLoader());
                }
                if (service.isValid()) {
                    return service;
                }
                MixinService.logBuffer.debug("MixinService [{}] is not valid", service.getName());
                badServices.add(String.format("INVALID[%s]", service.getName()));
            }
            catch (final ServiceConfigurationError sce) {
                ++brokenServiceCount;
            }
            catch (final Throwable th) {
                final String faultingClassName = th.getStackTrace()[0].getClassName();
                MixinService.logBuffer.debug("MixinService [{}] failed initialisation: {}", faultingClassName, th.getMessage());
                final int pos = faultingClassName.lastIndexOf(46);
                badServices.add(String.format("ERROR[%s]", (pos < 0) ? faultingClassName : faultingClassName.substring(pos + 1)));
            }
        }
        final String brokenServiceNote = (brokenServiceCount == 0) ? "" : (" and " + brokenServiceCount + " other invalid services.");
        throw new ServiceNotAvailableError("No mixin host service is available. Services: " + Joiner.on(", ").join(badServices) + brokenServiceNote);
    }
    
    public static IGlobalPropertyService getGlobalPropertyService() {
        return getInstance().getGlobalPropertyServiceInstance();
    }
    
    private IGlobalPropertyService getGlobalPropertyServiceInstance() {
        if (this.propertyService == null) {
            this.propertyService = this.initPropertyService();
        }
        return this.propertyService;
    }
    
    private IGlobalPropertyService initPropertyService() {
        final ServiceLoader<IGlobalPropertyService> serviceLoader = ServiceLoader.load(IGlobalPropertyService.class, this.getClass().getClassLoader());
        final Iterator<IGlobalPropertyService> iter = serviceLoader.iterator();
        while (iter.hasNext()) {
            try {
                final IGlobalPropertyService service = iter.next();
                return service;
            }
            catch (final ServiceConfigurationError serviceConfigurationError) {
                continue;
            }
            catch (final Throwable t) {
                continue;
            }
            break;
        }
        throw new ServiceNotAvailableError("No mixin global property service is available");
    }
    
    private static <T> T getDefaultLogger() {
        return (T)new LoggerAdapterConsole("mixin").setDebugStream(System.err);
    }
    
    static {
        MixinService.logBuffer = new LogBuffer();
    }
    
    static class LogBuffer
    {
        private final List<LogEntry> buffer;
        private ILogger logger;
        
        LogBuffer() {
            this.buffer = new ArrayList<LogEntry>();
        }
        
        synchronized void debug(final String message, final Object... params) {
            if (this.logger != null) {
                this.logger.debug(message, params);
                return;
            }
            this.buffer.add(new LogEntry(message, params, null));
        }
        
        synchronized void debug(final String message, final Throwable t) {
            if (this.logger != null) {
                this.logger.debug(message, t);
                return;
            }
            this.buffer.add(new LogEntry(message, new Object[0], t));
        }
        
        synchronized void flush(final ILogger logger) {
            for (final LogEntry buffered : this.buffer) {
                if (buffered.t != null) {
                    logger.debug(buffered.message, ObjectArrays.concat(buffered.params, buffered.t));
                }
                else {
                    logger.debug(buffered.message, buffered.params);
                }
            }
            this.buffer.clear();
            this.logger = logger;
        }
        
        public static class LogEntry
        {
            public String message;
            public Object[] params;
            public Throwable t;
            
            public LogEntry(final String message, final Object[] params, final Throwable t) {
                this.message = message;
                this.params = params;
                this.t = t;
            }
        }
    }
}
