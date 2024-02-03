// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.support.report;

import net.labymod.api.LabyModAddon;
import java.util.UUID;
import net.labymod.addon.AddonLoader;
import net.labymod.main.lang.LanguageManager;
import net.labymod.main.Updater;
import net.labymod.main.LabyMod;
import net.minecraft.crash.CrashReport;
import java.io.File;

public class CrashReportHandler
{
    private static final CrashReportHandler INSTANCE;
    
    static {
        INSTANCE = new CrashReportHandler();
    }
    
    public void report(final File mcReportFile, final CrashReport mcCrashReport) {
        final ReportData reportData = new ReportData(mcCrashReport.getCrashCause());
        Updater updater = (LabyMod.getInstance() == null) ? null : LabyMod.getInstance().getUpdater();
        if (updater == null) {
            updater = new Updater();
        }
        final ReportArguments reportArguments = new ReportArguments();
        reportArguments.setMinecraftCrashReportFile(mcReportFile.getAbsolutePath());
        reportArguments.setCrashReportJson(reportData.createJsonReport());
        final String key = (mcCrashReport.getCrashCause() instanceof OutOfMemoryError) ? "memory" : (this.isCrashCausedByMinecraft(mcCrashReport) ? "minecraft" : "labymod");
        reportArguments.setTitle(LanguageManager.translate("crash_reporter_title_" + key));
        final UUID uuid = this.getTargetAddonUUID(mcCrashReport);
        if (uuid != null) {
            reportArguments.setAddonUUID(uuid);
            final LabyModAddon addon = AddonLoader.getAddonByUUID(uuid);
            final File jarFile = AddonLoader.getFiles().get(uuid);
            if (jarFile != null) {
                reportArguments.setAddonPath(jarFile.getAbsolutePath());
                reportArguments.setAddonName(jarFile.getName());
            }
            if (addon != null && addon.about != null && addon.about.name != null && !addon.about.name.isEmpty()) {
                reportArguments.setAddonName(addon.about.name);
            }
            if (reportArguments.getAddonName() != null) {
                reportArguments.setTitle(LanguageManager.translate("crash_reporter_title_addon", reportArguments.getAddonName()));
            }
        }
        updater.executeReport(reportArguments);
    }
    
    private boolean isCrashCausedByMinecraft(final CrashReport mcCrashReport) {
        try {
            StackTraceElement[] stackTrace;
            for (int length = (stackTrace = mcCrashReport.getCrashCause().getStackTrace()).length, i = 0; i < length; ++i) {
                final StackTraceElement frame = stackTrace[i];
                if (frame != null && frame.getClassName() != null && frame.getClassName().toLowerCase().contains("labymod")) {
                    return false;
                }
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
        return true;
    }
    
    private UUID getTargetAddonUUID(final CrashReport mcCrashReport) {
        try {
            StackTraceElement[] stackTrace;
            for (int length = (stackTrace = mcCrashReport.getCrashCause().getStackTrace()).length, i = 0; i < length; ++i) {
                final StackTraceElement frame = stackTrace[i];
                try {
                    final Class<?> type = Class.forName(frame.getClassName());
                    final UUID uuid = AddonLoader.getUUIDByClass(type);
                    if (uuid != null) {
                        return uuid;
                    }
                }
                catch (final ClassNotFoundException ex) {}
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
        return null;
    }
    
    public static CrashReportHandler getInstance() {
        return CrashReportHandler.INSTANCE;
    }
}
