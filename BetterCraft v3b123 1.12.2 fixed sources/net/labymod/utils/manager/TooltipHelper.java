// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.manager;

import net.lenni0451.eventapi.events.EventTarget;
import me.nzxtercode.bettercraft.client.events.ClientTickEvent;
import java.util.List;
import net.labymod.utils.DrawUtils;
import net.labymod.main.LabyMod;
import java.util.Arrays;

public class TooltipHelper
{
    private static final long HOLD_TIME = 1000L;
    private static TooltipHelper instance;
    private String[] runningTooltipArray;
    private int runningTooltipId;
    private long runningTooltipDuration;
    private long keepAlive;
    private int mouseX;
    private int mouseY;
    
    public TooltipHelper() {
        this.runningTooltipId = -1;
        this.runningTooltipDuration = -1L;
        this.keepAlive = -1L;
        this.mouseX = 0;
        this.mouseY = 0;
        TooltipHelper.instance = this;
    }
    
    public static TooltipHelper getHelper() {
        return TooltipHelper.instance;
    }
    
    public boolean pointTooltip(final int mouseX, final int mouseY, final long customHoldTime, final String[] lines) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        if (!this.isRunning(lines) || customHoldTime == 0L) {
            this.runningTooltipDuration = System.currentTimeMillis() + customHoldTime;
            this.runningTooltipId = Arrays.deepHashCode(lines);
            this.runningTooltipArray = lines;
            if (customHoldTime != 0L) {
                return false;
            }
        }
        this.keepAlive = System.currentTimeMillis() + 100L;
        return System.currentTimeMillis() > this.runningTooltipDuration;
    }
    
    public boolean pointTooltip(final int mouseX, final int mouseY, final long customHoldTime, final String line) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final List<String> list = draw.listFormattedStringToWidth(line, LabyMod.getInstance().getDrawUtils().getWidth() / 3);
        return this.pointTooltip(mouseX, mouseY, customHoldTime, (String[])list.toArray());
    }
    
    public boolean pointTooltip(final int mouseX, final int mouseY, final String... lines) {
        return (lines.length == 1) ? this.pointTooltip(mouseX, mouseY, 1000L, lines[0]) : this.pointTooltip(mouseX, mouseY, 1000L, lines);
    }
    
    public boolean isRunning(final String... lines) {
        return this.runningTooltipId == Arrays.deepHashCode(lines);
    }
    
    @EventTarget
    public void handleEvent(final ClientTickEvent event) {
        if (System.currentTimeMillis() < this.runningTooltipDuration) {
            return;
        }
        if (this.runningTooltipId == -1) {
            return;
        }
        if (System.currentTimeMillis() > this.keepAlive) {
            this.runningTooltipId = -1;
            this.runningTooltipDuration = -1L;
            this.runningTooltipArray = null;
            return;
        }
        final DrawUtils utils = LabyMod.getInstance().getDrawUtils();
        utils.drawHoveringText(this.mouseX, this.mouseY, this.runningTooltipArray);
    }
}
