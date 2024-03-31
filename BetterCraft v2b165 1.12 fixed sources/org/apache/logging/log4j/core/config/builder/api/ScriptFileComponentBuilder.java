// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config.builder.api;

public interface ScriptFileComponentBuilder extends ComponentBuilder<ScriptFileComponentBuilder>
{
    ScriptFileComponentBuilder addLanguage(final String p0);
    
    ScriptFileComponentBuilder addIsWatched(final boolean p0);
    
    ScriptFileComponentBuilder addIsWatched(final String p0);
    
    ScriptFileComponentBuilder addCharset(final String p0);
}
