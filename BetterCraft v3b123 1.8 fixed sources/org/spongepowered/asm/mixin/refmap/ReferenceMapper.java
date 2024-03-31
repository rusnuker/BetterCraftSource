// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.refmap;

import com.google.gson.Gson;
import java.io.InputStream;
import org.spongepowered.asm.service.IMixinService;
import java.io.Reader;
import com.google.common.io.Closeables;
import com.google.gson.JsonParseException;
import javax.tools.Diagnostic;
import org.spongepowered.asm.util.logging.MessageRouter;
import java.io.InputStreamReader;
import org.spongepowered.asm.service.MixinService;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.Map;
import java.io.Serializable;

public final class ReferenceMapper implements IReferenceMapper, Serializable
{
    private static final long serialVersionUID = 2L;
    public static final String DEFAULT_RESOURCE = "mixin.refmap.json";
    public static final ReferenceMapper DEFAULT_MAPPER;
    private final Map<String, Map<String, String>> mappings;
    private final Map<String, Map<String, Map<String, String>>> data;
    private final transient boolean readOnly;
    private transient String context;
    private transient String resource;
    
    public ReferenceMapper() {
        this(false, "mixin.refmap.json");
    }
    
    private ReferenceMapper(final boolean readOnly, final String resource) {
        this.mappings = (Map<String, Map<String, String>>)Maps.newHashMap();
        this.data = (Map<String, Map<String, Map<String, String>>>)Maps.newHashMap();
        this.context = null;
        this.readOnly = readOnly;
        this.resource = resource;
    }
    
    @Override
    public boolean isDefault() {
        return this.readOnly;
    }
    
    private void setResourceName(final String resource) {
        if (!this.readOnly) {
            this.resource = ((resource != null) ? resource : "<unknown resource>");
        }
    }
    
    @Override
    public String getResourceName() {
        return this.resource;
    }
    
    @Override
    public String getStatus() {
        return this.isDefault() ? "No refMap loaded." : ("Using refmap " + this.getResourceName());
    }
    
    @Override
    public String getContext() {
        return this.context;
    }
    
    @Override
    public void setContext(final String context) {
        this.context = context;
    }
    
    @Override
    public String remap(final String className, final String reference) {
        return this.remapWithContext(this.context, className, reference);
    }
    
    @Override
    public String remapWithContext(final String context, final String className, final String reference) {
        Map<String, Map<String, String>> mappings = this.mappings;
        if (context != null) {
            mappings = this.data.get(context);
            if (mappings == null) {
                mappings = this.mappings;
            }
        }
        return this.remap(mappings, className, reference);
    }
    
    private String remap(final Map<String, Map<String, String>> mappings, final String className, final String reference) {
        if (className == null) {
            for (final Map<String, String> mapping : mappings.values()) {
                if (mapping.containsKey(reference)) {
                    return mapping.get(reference);
                }
            }
        }
        final Map<String, String> classMappings = mappings.get(className);
        if (classMappings == null) {
            return reference;
        }
        final String remappedReference = classMappings.get(reference);
        return (remappedReference != null) ? remappedReference : reference;
    }
    
    public String addMapping(final String context, final String className, final String reference, final String newReference) {
        if (this.readOnly || reference == null || newReference == null) {
            return null;
        }
        final String conformedReference = reference.replaceAll("\\s", "");
        if (conformedReference.equals(newReference)) {
            return null;
        }
        Map<String, Map<String, String>> mappings = this.mappings;
        if (context != null) {
            mappings = this.data.get(context);
            if (mappings == null) {
                mappings = (Map<String, Map<String, String>>)Maps.newHashMap();
                this.data.put(context, mappings);
            }
        }
        Map<String, String> classMappings = mappings.get(className);
        if (classMappings == null) {
            classMappings = new HashMap<String, String>();
            mappings.put(className, classMappings);
        }
        return classMappings.put(conformedReference, newReference);
    }
    
    public void write(final Appendable writer) {
        new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this, writer);
    }
    
    public static ReferenceMapper read(final String resourcePath) {
        Reader reader = null;
        try {
            final IMixinService service = MixinService.getService();
            final InputStream resource = service.getResourceAsStream(resourcePath);
            if (resource != null) {
                reader = new InputStreamReader(resource);
                final ReferenceMapper mapper = readJson(reader);
                mapper.setResourceName(resourcePath);
                return mapper;
            }
            return ReferenceMapper.DEFAULT_MAPPER;
        }
        catch (final JsonParseException ex) {
            MessageRouter.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("Invalid REFMAP JSON in %s: %s %s", resourcePath, ex.getClass().getName(), ex.getMessage()));
        }
        catch (final Exception ex2) {
            MessageRouter.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("Failed reading REFMAP JSON from %s: %s %s", resourcePath, ex2.getClass().getName(), ex2.getMessage()));
        }
        finally {
            Closeables.closeQuietly(reader);
        }
        return ReferenceMapper.DEFAULT_MAPPER;
    }
    
    public static ReferenceMapper read(final Reader reader, final String name) {
        try {
            final ReferenceMapper mapper = readJson(reader);
            mapper.setResourceName(name);
            return mapper;
        }
        catch (final Exception ex) {
            return ReferenceMapper.DEFAULT_MAPPER;
        }
    }
    
    private static ReferenceMapper readJson(final Reader reader) {
        return new Gson().fromJson(reader, ReferenceMapper.class);
    }
    
    static {
        DEFAULT_MAPPER = new ReferenceMapper(true, "invalid");
    }
}
