// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.support.report;

import java.beans.ConstructorProperties;
import java.util.UUID;

public class ReportArguments
{
    private String title;
    private String minecraftCrashReportFile;
    private String crashReportJson;
    private String addonPath;
    private UUID addonUUID;
    private String addonName;
    
    public String getTitle() {
        return this.title;
    }
    
    public String getMinecraftCrashReportFile() {
        return this.minecraftCrashReportFile;
    }
    
    public String getCrashReportJson() {
        return this.crashReportJson;
    }
    
    public String getAddonPath() {
        return this.addonPath;
    }
    
    public UUID getAddonUUID() {
        return this.addonUUID;
    }
    
    public String getAddonName() {
        return this.addonName;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setMinecraftCrashReportFile(final String minecraftCrashReportFile) {
        this.minecraftCrashReportFile = minecraftCrashReportFile;
    }
    
    public void setCrashReportJson(final String crashReportJson) {
        this.crashReportJson = crashReportJson;
    }
    
    public void setAddonPath(final String addonPath) {
        this.addonPath = addonPath;
    }
    
    public void setAddonUUID(final UUID addonUUID) {
        this.addonUUID = addonUUID;
    }
    
    public void setAddonName(final String addonName) {
        this.addonName = addonName;
    }
    
    public ReportArguments() {
    }
    
    @ConstructorProperties({ "title", "minecraftCrashReportFile", "crashReportJson", "addonPath", "addonUUID", "addonName" })
    public ReportArguments(final String title, final String minecraftCrashReportFile, final String crashReportJson, final String addonPath, final UUID addonUUID, final String addonName) {
        this.title = title;
        this.minecraftCrashReportFile = minecraftCrashReportFile;
        this.crashReportJson = crashReportJson;
        this.addonPath = addonPath;
        this.addonUUID = addonUUID;
        this.addonName = addonName;
    }
}
