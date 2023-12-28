// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.jackson;

import org.apache.logging.log4j.ThreadContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import com.fasterxml.jackson.databind.Module;

class Initializers
{
    static class SetupContextInitializer
    {
        void setupModule(final Module.SetupContext context, final boolean includeStacktrace) {
            context.setMixInAnnotations((Class)StackTraceElement.class, (Class)StackTraceElementMixIn.class);
            context.setMixInAnnotations((Class)Marker.class, (Class)MarkerMixIn.class);
            context.setMixInAnnotations((Class)Level.class, (Class)LevelMixIn.class);
            context.setMixInAnnotations((Class)LogEvent.class, (Class)LogEventWithContextListMixIn.class);
            context.setMixInAnnotations((Class)ExtendedStackTraceElement.class, (Class)ExtendedStackTraceElementMixIn.class);
            context.setMixInAnnotations((Class)ThrowableProxy.class, (Class)(includeStacktrace ? ThrowableProxyMixIn.class : ThrowableProxyWithoutStacktraceMixIn.class));
        }
    }
    
    static class SetupContextJsonInitializer
    {
        void setupModule(final Module.SetupContext context, final boolean includeStacktrace) {
            context.setMixInAnnotations((Class)StackTraceElement.class, (Class)StackTraceElementMixIn.class);
            context.setMixInAnnotations((Class)Marker.class, (Class)MarkerMixIn.class);
            context.setMixInAnnotations((Class)Level.class, (Class)LevelMixIn.class);
            context.setMixInAnnotations((Class)LogEvent.class, (Class)LogEventJsonMixIn.class);
            context.setMixInAnnotations((Class)ExtendedStackTraceElement.class, (Class)ExtendedStackTraceElementMixIn.class);
            context.setMixInAnnotations((Class)ThrowableProxy.class, (Class)(includeStacktrace ? ThrowableProxyMixIn.class : ThrowableProxyWithoutStacktraceMixIn.class));
        }
    }
    
    static class SimpleModuleInitializer
    {
        void initialize(final SimpleModule simpleModule) {
            simpleModule.addDeserializer((Class)StackTraceElement.class, (JsonDeserializer)new Log4jStackTraceElementDeserializer());
            simpleModule.addDeserializer((Class)ThreadContext.ContextStack.class, (JsonDeserializer)new MutableThreadContextStackDeserializer());
        }
    }
}
