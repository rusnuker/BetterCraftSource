// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Script", category = "Core", printObject = true)
public class Script extends AbstractScript
{
    public Script(final String name, final String language, final String scriptText) {
        super(name, language, scriptText);
    }
    
    @PluginFactory
    public static Script createScript(@PluginAttribute("name") final String name, @PluginAttribute("language") String language, @PluginValue("scriptText") final String scriptText) {
        if (language == null) {
            Script.LOGGER.info("No script language supplied, defaulting to {}", "JavaScript");
            language = "JavaScript";
        }
        if (scriptText == null) {
            Script.LOGGER.error("No scriptText attribute provided for ScriptFile {}", name);
            return null;
        }
        return new Script(name, language, scriptText);
    }
    
    @Override
    public String toString() {
        return (this.getName() != null) ? this.getName() : super.toString();
    }
}
