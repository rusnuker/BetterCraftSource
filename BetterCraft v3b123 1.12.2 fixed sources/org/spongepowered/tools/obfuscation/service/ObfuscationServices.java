// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import com.google.common.base.Joiner;
import java.util.Map;
import java.util.ServiceConfigurationError;
import javax.tools.Diagnostic;
import java.util.LinkedHashSet;
import org.spongepowered.tools.obfuscation.ObfuscationType;
import java.util.LinkedHashMap;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import java.util.HashSet;
import java.util.Set;
import java.util.ServiceLoader;

public final class ObfuscationServices
{
    private static ObfuscationServices instance;
    private final ServiceLoader<IObfuscationService> serviceLoader;
    private final Set<IObfuscationService> services;
    private boolean providerInitDone;
    
    private ObfuscationServices() {
        this.services = new HashSet<IObfuscationService>();
        this.providerInitDone = false;
        this.serviceLoader = ServiceLoader.load(IObfuscationService.class, this.getClass().getClassLoader());
    }
    
    public static ObfuscationServices getInstance() {
        if (ObfuscationServices.instance == null) {
            ObfuscationServices.instance = new ObfuscationServices();
        }
        return ObfuscationServices.instance;
    }
    
    public void initProviders(final IMixinAnnotationProcessor ap) {
        if (this.providerInitDone) {
            return;
        }
        this.providerInitDone = true;
        boolean defaultIsPresent = false;
        final Map<String, Set<String>> supportedTypes = new LinkedHashMap<String, Set<String>>();
        try {
            for (final IObfuscationService service : this.serviceLoader) {
                if (!this.services.contains(service)) {
                    this.services.add(service);
                    final String serviceName = service.getClass().getSimpleName();
                    final Collection<ObfuscationTypeDescriptor> obfTypes = service.getObfuscationTypes(ap);
                    if (obfTypes == null) {
                        continue;
                    }
                    for (final ObfuscationTypeDescriptor obfType : obfTypes) {
                        try {
                            final ObfuscationType type = ObfuscationType.create(obfType, ap);
                            Set<String> types = supportedTypes.get(serviceName);
                            if (types == null) {
                                supportedTypes.put(serviceName, types = new LinkedHashSet<String>());
                            }
                            types.add(type.getKey());
                            defaultIsPresent |= type.isDefault();
                        }
                        catch (final Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        catch (final ServiceConfigurationError serviceError) {
            ap.printMessage(Diagnostic.Kind.ERROR, serviceError.getClass().getSimpleName() + ": " + serviceError.getMessage());
            serviceError.printStackTrace();
        }
        if (supportedTypes.size() > 0) {
            final StringBuilder sb = new StringBuilder("Supported obfuscation types:");
            for (final Map.Entry<String, Set<String>> supportedType : supportedTypes.entrySet()) {
                sb.append(' ').append(supportedType.getKey()).append(" supports [").append(Joiner.on(',').join(supportedType.getValue())).append(']');
            }
            ap.printMessage(IMessagerEx.MessageType.INFO, sb.toString());
        }
        if (!defaultIsPresent) {
            final String defaultEnv = ap.getOption("defaultObfuscationEnv");
            if (defaultEnv == null) {
                ap.printMessage(Diagnostic.Kind.WARNING, "No default obfuscation environment was specified and \"searge\" is not available. Please ensure defaultObfuscationEnv is specified in your build configuration");
            }
            else {
                ap.printMessage(Diagnostic.Kind.WARNING, "Specified default obfuscation environment \"" + defaultEnv.toLowerCase(Locale.ROOT) + "\" was not defined. This probably means your build configuration is out of date or a required service is missing");
            }
        }
    }
    
    public Set<String> getSupportedOptions() {
        final Set<String> supportedOptions = new HashSet<String>();
        for (final IObfuscationService provider : this.services) {
            final Set<String> options = provider.getSupportedOptions();
            if (options != null) {
                supportedOptions.addAll(options);
            }
        }
        return supportedOptions;
    }
    
    public IObfuscationService getService(final Class<? extends IObfuscationService> serviceClass) {
        for (final IObfuscationService service : this.services) {
            if (serviceClass.getName().equals(service.getClass().getName())) {
                return service;
            }
        }
        return null;
    }
}
