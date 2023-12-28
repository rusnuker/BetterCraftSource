// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "TimeBasedTriggeringPolicy", category = "Core", printObject = true)
public final class TimeBasedTriggeringPolicy extends AbstractTriggeringPolicy
{
    private long nextRolloverMillis;
    private final int interval;
    private final boolean modulate;
    private RollingFileManager manager;
    
    private TimeBasedTriggeringPolicy(final int interval, final boolean modulate) {
        this.interval = interval;
        this.modulate = modulate;
    }
    
    public int getInterval() {
        return this.interval;
    }
    
    public long getNextRolloverMillis() {
        return this.nextRolloverMillis;
    }
    
    @Override
    public void initialize(final RollingFileManager aManager) {
        this.manager = aManager;
        aManager.getPatternProcessor().getNextTime(aManager.getFileTime(), this.interval, this.modulate);
        this.nextRolloverMillis = aManager.getPatternProcessor().getNextTime(aManager.getFileTime(), this.interval, this.modulate);
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent event) {
        if (this.manager.getFileSize() == 0L) {
            return false;
        }
        final long nowMillis = event.getTimeMillis();
        if (nowMillis >= this.nextRolloverMillis) {
            this.nextRolloverMillis = this.manager.getPatternProcessor().getNextTime(nowMillis, this.interval, this.modulate);
            return true;
        }
        return false;
    }
    
    @PluginFactory
    public static TimeBasedTriggeringPolicy createPolicy(@PluginAttribute("interval") final String interval, @PluginAttribute("modulate") final String modulate) {
        final int increment = Integers.parseInt(interval, 1);
        final boolean mod = Boolean.parseBoolean(modulate);
        return new TimeBasedTriggeringPolicy(increment, mod);
    }
    
    @Override
    public String toString() {
        return "TimeBasedTriggeringPolicy(nextRolloverMillis=" + this.nextRolloverMillis + ", interval=" + this.interval + ", modulate=" + this.modulate + ")";
    }
}
