// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import java.io.InputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.FileInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.FileOutputStream;
import com.mojang.realmsclient.dto.UploadInfo;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.io.IOException;
import com.mojang.realmsclient.client.FileUpload;
import java.io.File;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.util.UploadTokenCache;
import java.util.concurrent.TimeUnit;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.opengl.GL11;
import net.minecraft.realms.Realms;
import org.lwjgl.input.Keyboard;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.client.UploadStatus;
import net.minecraft.realms.RealmsLevelSummary;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsUploadScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static final int CANCEL_BUTTON = 0;
    private static final int BACK_BUTTON = 1;
    private final RealmsResetWorldScreen lastScreen;
    private final RealmsLevelSummary selectedLevel;
    private final long worldId;
    private final int slotId;
    private final UploadStatus uploadStatus;
    private volatile String errorMessage;
    private volatile String status;
    private volatile String progress;
    private volatile boolean cancelled;
    private volatile boolean uploadFinished;
    private volatile boolean showDots;
    private volatile boolean uploadStarted;
    private RealmsButton backButton;
    private RealmsButton cancelButton;
    private int animTick;
    private static final String[] DOTS;
    private int dotIndex;
    private Long previousWrittenBytes;
    private Long previousTimeSnapshot;
    private long bytesPersSecond;
    private static final ReentrantLock uploadLock;
    private static final int baseUnit = 1024;
    
    public RealmsUploadScreen(final long worldId, final int slotId, final RealmsResetWorldScreen lastScreen, final RealmsLevelSummary selectedLevel) {
        this.showDots = true;
        this.worldId = worldId;
        this.slotId = slotId;
        this.lastScreen = lastScreen;
        this.selectedLevel = selectedLevel;
        this.uploadStatus = new UploadStatus();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.backButton = RealmsScreen.newButton(1, this.width() / 2 - 100, this.height() - 42, 200, 20, RealmsScreen.getLocalizedString("gui.back"));
        this.buttonsAdd(this.cancelButton = RealmsScreen.newButton(0, this.width() / 2 - 100, this.height() - 42, 200, 20, RealmsScreen.getLocalizedString("gui.cancel")));
        if (!this.uploadStarted) {
            if (this.lastScreen.slot == -1) {
                this.upload();
            }
            else {
                this.lastScreen.switchSlot(this);
            }
        }
    }
    
    @Override
    public void confirmResult(final boolean result, final int buttonId) {
        if (result && !this.uploadStarted) {
            this.uploadStarted = true;
            Realms.setScreen(this);
            this.upload();
        }
    }
    
    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        if (button.id() == 1) {
            this.lastScreen.confirmResult(true, 0);
        }
        else if (button.id() == 0) {
            this.cancelled = true;
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        if (eventKey == 1) {
            if (this.showDots) {
                this.buttonClicked(this.cancelButton);
            }
            else {
                this.buttonClicked(this.backButton);
            }
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        if (!this.uploadFinished && this.uploadStatus.bytesWritten != 0L && this.uploadStatus.bytesWritten == (long)this.uploadStatus.totalBytes) {
            this.status = RealmsScreen.getLocalizedString("mco.upload.verifying");
            this.cancelButton.active(false);
        }
        this.drawCenteredString(this.status, this.width() / 2, 50, 16777215);
        if (this.showDots) {
            this.drawDots();
        }
        if (this.uploadStatus.bytesWritten != 0L && !this.cancelled) {
            this.drawProgressBar();
            this.drawUploadSpeed();
        }
        if (this.errorMessage != null) {
            final String[] errorMessages = this.errorMessage.split("\\\\n");
            for (int i = 0; i < errorMessages.length; ++i) {
                this.drawCenteredString(errorMessages[i], this.width() / 2, 110 + 12 * i, 16711680);
            }
        }
        super.render(xm, ym, a);
    }
    
    private void drawDots() {
        final int statusWidth = this.fontWidth(this.status);
        if (this.animTick % 10 == 0) {
            ++this.dotIndex;
        }
        this.drawString(RealmsUploadScreen.DOTS[this.dotIndex % RealmsUploadScreen.DOTS.length], this.width() / 2 + statusWidth / 2 + 5, 50, 16777215);
    }
    
    private void drawProgressBar() {
        double percentage = this.uploadStatus.bytesWritten / (double)this.uploadStatus.totalBytes * 100.0;
        if (percentage > 100.0) {
            percentage = 100.0;
        }
        this.progress = String.format("%.1f", percentage);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3553);
        final double base = this.width() / 2 - 100;
        final double diff = 0.5;
        final Tezzelator t = Tezzelator.instance;
        t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
        t.vertex(base - 0.5, 95.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0 + 0.5, 95.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0 + 0.5, 79.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base - 0.5, 79.5, 0.0).color(217, 210, 210, 255).endVertex();
        t.vertex(base, 95.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0, 95.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.vertex(base + 200.0 * percentage / 100.0, 80.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.vertex(base, 80.0, 0.0).color(128, 128, 128, 255).endVertex();
        t.end();
        GL11.glEnable(3553);
        this.drawCenteredString(this.progress + " %", this.width() / 2, 84, 16777215);
    }
    
    private void drawUploadSpeed() {
        if (this.animTick % RealmsSharedConstants.TICKS_PER_SECOND == 0) {
            if (this.previousWrittenBytes != null) {
                long timeElapsed = System.currentTimeMillis() - this.previousTimeSnapshot;
                if (timeElapsed == 0L) {
                    timeElapsed = 1L;
                }
                this.drawUploadSpeed0(this.bytesPersSecond = 1000L * (this.uploadStatus.bytesWritten - this.previousWrittenBytes) / timeElapsed);
            }
            this.previousWrittenBytes = this.uploadStatus.bytesWritten;
            this.previousTimeSnapshot = System.currentTimeMillis();
        }
        else {
            this.drawUploadSpeed0(this.bytesPersSecond);
        }
    }
    
    private void drawUploadSpeed0(final long bytesPersSecond) {
        if (bytesPersSecond > 0L) {
            final int progressLength = this.fontWidth(this.progress);
            final String stringPresentation = "(" + humanReadableByteCount(bytesPersSecond) + ")";
            this.drawString(stringPresentation, this.width() / 2 + progressLength / 2 + 15, 84, 16777215);
        }
    }
    
    public static String humanReadableByteCount(final long bytes) {
        final int unit = 1024;
        if (bytes < 1024L) {
            return bytes + " B";
        }
        final int exp = (int)(Math.log((double)bytes) / Math.log(1024.0));
        final String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB/s", bytes / Math.pow(1024.0, exp), pre);
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
    }
    
    @Override
    public void tick() {
        super.tick();
        ++this.animTick;
    }
    
    public static Unit getLargestUnit(final long bytes) {
        if (bytes < 1024L) {
            return Unit.B;
        }
        final int exp = (int)(Math.log((double)bytes) / Math.log(1024.0));
        final String pre = "KMGTPE".charAt(exp - 1) + "";
        try {
            return Unit.valueOf(pre + "B");
        }
        catch (final Exception ignored) {
            return Unit.GB;
        }
    }
    
    public static double convertToUnit(final long bytes, final Unit unit) {
        if (unit.equals(Unit.B)) {
            return (double)bytes;
        }
        return bytes / Math.pow(1024.0, unit.ordinal());
    }
    
    public static String humanReadableSize(final long bytes, final Unit unit) {
        return String.format("%." + (unit.equals(Unit.GB) ? "1" : "0") + "f %s", convertToUnit(bytes, unit), unit.name());
    }
    
    private void upload() {
        this.uploadStarted = true;
        new Thread() {
            @Override
            public void run() {
                File archive = null;
                final RealmsClient client = RealmsClient.createRealmsClient();
                final long wid = RealmsUploadScreen.this.worldId;
                try {
                    if (!RealmsUploadScreen.uploadLock.tryLock(1L, TimeUnit.SECONDS)) {
                        return;
                    }
                    RealmsUploadScreen.this.status = RealmsScreen.getLocalizedString("mco.upload.preparing");
                    UploadInfo uploadInfo = null;
                    int i = 0;
                    while (i < 20) {
                        try {
                            if (RealmsUploadScreen.this.cancelled) {
                                RealmsUploadScreen.this.uploadCancelled(wid);
                                return;
                            }
                            uploadInfo = client.upload(wid, UploadTokenCache.get(wid));
                        }
                        catch (final RetryCallException e) {
                            Thread.sleep(e.delaySeconds * 1000);
                            ++i;
                            continue;
                        }
                        break;
                    }
                    if (uploadInfo == null) {
                        RealmsUploadScreen.this.status = RealmsScreen.getLocalizedString("mco.upload.close.failure");
                        return;
                    }
                    UploadTokenCache.put(wid, uploadInfo.getToken());
                    if (!uploadInfo.isWorldClosed()) {
                        RealmsUploadScreen.this.status = RealmsScreen.getLocalizedString("mco.upload.close.failure");
                        return;
                    }
                    if (RealmsUploadScreen.this.cancelled) {
                        RealmsUploadScreen.this.uploadCancelled(wid);
                        return;
                    }
                    final File saves = new File(Realms.getGameDirectoryPath(), "saves");
                    archive = RealmsUploadScreen.this.tarGzipArchive(new File(saves, RealmsUploadScreen.this.selectedLevel.getLevelId()));
                    if (RealmsUploadScreen.this.cancelled) {
                        RealmsUploadScreen.this.uploadCancelled(wid);
                        return;
                    }
                    if (RealmsUploadScreen.this.verify(archive)) {
                        RealmsUploadScreen.this.status = RealmsScreen.getLocalizedString("mco.upload.uploading", RealmsUploadScreen.this.selectedLevel.getLevelName());
                        final FileUpload fileUpload = new FileUpload();
                        fileUpload.upload(archive, RealmsUploadScreen.this.worldId, RealmsUploadScreen.this.slotId, uploadInfo, Realms.getSessionId(), Realms.getName(), RealmsSharedConstants.VERSION_STRING, RealmsUploadScreen.this.uploadStatus);
                        while (!fileUpload.isFinished()) {
                            if (RealmsUploadScreen.this.cancelled) {
                                fileUpload.cancel();
                                RealmsUploadScreen.this.uploadCancelled(wid);
                                return;
                            }
                            try {
                                Thread.sleep(500L);
                            }
                            catch (final InterruptedException ignored) {
                                RealmsUploadScreen.LOGGER.error("Failed to check Realms file upload status");
                            }
                        }
                        if (fileUpload.getStatusCode() >= 200 && fileUpload.getStatusCode() < 300) {
                            RealmsUploadScreen.this.uploadFinished = true;
                            RealmsUploadScreen.this.status = RealmsScreen.getLocalizedString("mco.upload.done");
                            RealmsUploadScreen.this.backButton.msg(RealmsScreen.getLocalizedString("gui.done"));
                            UploadTokenCache.invalidate(wid);
                        }
                        else if (fileUpload.getStatusCode() == 400 && fileUpload.getErrorMessage() != null) {
                            RealmsUploadScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.upload.failed", fileUpload.getErrorMessage());
                        }
                        else {
                            RealmsUploadScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.upload.failed", fileUpload.getStatusCode());
                        }
                        return;
                    }
                    final long length = archive.length();
                    final Unit lengthUnit = RealmsUploadScreen.getLargestUnit(length);
                    final Unit maxUnit = RealmsUploadScreen.getLargestUnit(5368709120L);
                    if (RealmsUploadScreen.humanReadableSize(length, lengthUnit).equals(RealmsUploadScreen.humanReadableSize(5368709120L, maxUnit)) && lengthUnit != Unit.B) {
                        final Unit unitToUse = Unit.values()[lengthUnit.ordinal() - 1];
                        RealmsUploadScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.upload.size.failure.line1", RealmsUploadScreen.this.selectedLevel.getLevelName()) + "\\n" + RealmsScreen.getLocalizedString("mco.upload.size.failure.line2", RealmsUploadScreen.humanReadableSize(length, unitToUse), RealmsUploadScreen.humanReadableSize(5368709120L, unitToUse));
                        return;
                    }
                    RealmsUploadScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.upload.size.failure.line1", RealmsUploadScreen.this.selectedLevel.getLevelName()) + "\\n" + RealmsScreen.getLocalizedString("mco.upload.size.failure.line2", RealmsUploadScreen.humanReadableSize(length, lengthUnit), RealmsUploadScreen.humanReadableSize(5368709120L, maxUnit));
                }
                catch (final IOException e2) {
                    RealmsUploadScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.upload.failed", e2.getMessage());
                }
                catch (final RealmsServiceException e3) {
                    RealmsUploadScreen.this.errorMessage = RealmsScreen.getLocalizedString("mco.upload.failed", e3.toString());
                }
                catch (final InterruptedException ignored2) {
                    RealmsUploadScreen.LOGGER.error("Could not acquire upload lock");
                }
                finally {
                    RealmsUploadScreen.this.uploadFinished = true;
                    if (!RealmsUploadScreen.uploadLock.isHeldByCurrentThread()) {
                        return;
                    }
                    RealmsUploadScreen.uploadLock.unlock();
                    RealmsUploadScreen.this.showDots = false;
                    RealmsUploadScreen.this.buttonsClear();
                    RealmsUploadScreen.this.buttonsAdd(RealmsUploadScreen.this.backButton);
                    if (archive != null) {
                        RealmsUploadScreen.LOGGER.debug("Deleting file " + archive.getAbsolutePath());
                        archive.delete();
                    }
                }
            }
        }.start();
    }
    
    private void uploadCancelled(final long worldId) {
        this.status = RealmsScreen.getLocalizedString("mco.upload.cancelled");
        RealmsUploadScreen.LOGGER.debug("Upload was cancelled");
    }
    
    private boolean verify(final File archive) {
        return archive.length() < 5368709120L;
    }
    
    private File tarGzipArchive(final File pathToDirectoryFile) throws IOException {
        TarArchiveOutputStream tar = null;
        try {
            final File file = File.createTempFile("realms-upload-file", ".tar.gz");
            tar = new TarArchiveOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            this.addFileToTarGz(tar, pathToDirectoryFile.getAbsolutePath(), "world", true);
            tar.finish();
            return file;
        }
        finally {
            if (tar != null) {
                tar.close();
            }
        }
    }
    
    private void addFileToTarGz(final TarArchiveOutputStream tOut, final String path, final String base, final boolean root) throws IOException {
        if (this.cancelled) {
            return;
        }
        final File f = new File(path);
        final String entryName = root ? base : (base + f.getName());
        final TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
        tOut.putArchiveEntry(tarEntry);
        if (f.isFile()) {
            IOUtils.copy(new FileInputStream(f), tOut);
            tOut.closeArchiveEntry();
        }
        else {
            tOut.closeArchiveEntry();
            final File[] children = f.listFiles();
            if (children != null) {
                for (final File child : children) {
                    this.addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/", false);
                }
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DOTS = new String[] { "", ".", ". .", ". . ." };
        uploadLock = new ReentrantLock();
    }
    
    enum Unit
    {
        B, 
        KB, 
        MB, 
        GB;
    }
}
