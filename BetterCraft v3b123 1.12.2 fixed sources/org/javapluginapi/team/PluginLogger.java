// 
// Decompiled by Procyon v0.6.0
// 

package org.javapluginapi.team;

import org.javapluginapi.team.api.Plugin;
import org.javapluginapi.team.api.PluginDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PluginLogger
{
    private static final Logger logger;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public String logInfo(final String i) {
        final String returned = String.valueOf(JavaPluginApi.outName) + "(" + Plugin.descriptionFile.get().getName() + ") [Info] " + i;
        PluginLogger.logger.info(returned);
        return returned;
    }
    
    public String logError(final String i) {
        final String returned = String.valueOf(JavaPluginApi.outName) + "(" + Plugin.descriptionFile.get().getName() + ") [Error] " + i;
        PluginLogger.logger.error(returned);
        return returned;
    }
}
