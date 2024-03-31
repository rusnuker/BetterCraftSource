// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.elements;

import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import net.labymod.main.Source;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.io.File;
import net.labymod.addons.resourcepacks24.Resourcepacks24;
import net.labymod.addons.resourcepacks24.api.util.interfaces.ActionResponse;
import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.utils.ModUtils;
import net.labymod.utils.ModColor;
import net.labymod.utils.DrawUtils;
import net.labymod.main.LabyMod;
import net.minecraft.util.ResourceLocation;
import net.labymod.addons.resourcepacks24.gui.views.OnlineView;
import net.labymod.gui.elements.GuiImageButton;
import net.labymod.addons.resourcepacks24.gui.views.View;
import net.labymod.utils.texture.DynamicTextureManager;
import net.labymod.addons.resourcepacks24.api.model.Pack;
import net.labymod.addons.resourcepacks24.gui.elements.basement.PackElement;

public class OnlinePackElement extends PackElement
{
    private Pack pack;
    private DynamicTextureManager dynamicIconManager;
    private View view;
    protected GuiImageButton buttonDownload;
    private double downloadProgress;
    private boolean downloadStarted;
    
    public OnlinePackElement(final Pack pack, final DynamicTextureManager dynamicIconManager, final View view) {
        super(false);
        this.downloadProgress = 0.0;
        this.downloadStarted = false;
        this.pack = pack;
        this.dynamicIconManager = dynamicIconManager;
        this.view = view;
        (this.buttonDownload = new GuiImageButton(OnlineView.TEXTURE_ONLINE, 127, 127, 127, 127)).setImageAlpha(0.3f);
    }
    
    @Override
    public ResourceLocation getIcon() {
        return this.dynamicIconManager.getTexture("icon/" + this.pack.rp_id, this.pack.thumbnail);
    }
    
    @Override
    public String getDisplayName() {
        return this.pack.ingame_name;
    }
    
    @Override
    public String getDescription() {
        return this.pack.description;
    }
    
    @Override
    public boolean draw(final double x, final double y, final double width, final double height, final int mouseX, final int mouseY) {
        final boolean mouseOver = super.draw(x, y, width, height, mouseX, mouseY);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        DrawUtils.drawRect(x, y + height - 6.0, x + height, y + height, Integer.MIN_VALUE);
        if (this.downloadStarted) {
            String status = String.valueOf(Math.round(this.downloadProgress)) + "%";
            if (this.downloadProgress == 0.0) {
                status = "Preparing..";
            }
            if (this.downloadProgress == 100.0) {
                status = "Finished";
            }
            DrawUtils.drawRect(x, y + height - 1.0, x + height / 100.0 * this.downloadProgress, y + height, ModColor.toRGB(80, 255, 80, 255));
            draw.drawString(String.valueOf(ModColor.cl('f')) + status, x + 1.0, y + height - 5.0, 0.5);
        }
        else {
            draw.drawString(String.valueOf(ModColor.cl('f')) + ModUtils.humanReadableByteCount(this.pack.size, true, true), x + 1.0, y + height - 5.0, 0.5);
        }
        if (mouseOver && this.view.isInsideView(mouseX, mouseY) && mouseX < x + height) {
            final String key = ModColor.cl('e');
            final String bracket = ModColor.cl('7');
            final String value = ModColor.cl('f');
            String toolTip = "";
            toolTip = String.valueOf(toolTip) + key + "Name" + bracket + ": " + value + this.pack.website_name + "\n";
            toolTip = String.valueOf(toolTip) + key + "Downloads" + bracket + ": " + value + this.pack.download + "\n";
            toolTip = String.valueOf(toolTip) + key + "Category" + bracket + ": " + value + this.pack.category + "\n\n";
            toolTip = String.valueOf(toolTip) + key + "Tags" + bracket + ": ";
            String[] tags;
            for (int length = (tags = this.pack.getTags()).length, i = 0; i < length; ++i) {
                final String tag = tags[i];
                toolTip = String.valueOf(toolTip) + value + tag + ", ";
            }
            TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 2000L, toolTip);
        }
        return mouseOver;
    }
    
    @Override
    public void drawControls(final double x, final double y, final double width, final double height, final boolean isFirstEntry, final boolean isLastEntry, final boolean isSelected, final int mouseX, final int mouseY, final GuiResourcepacks24 gui) {
        super.drawControls(x, y, width, height, isFirstEntry, isLastEntry, isSelected, mouseX, mouseY, gui);
        if (gui.getSharedView().draggingElement == null && !this.downloadStarted) {
            this.buttonDownload.draw(x + width - 18.0, y, 10.0, mouseX, mouseY);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton, final GuiResourcepacks24 gui) {
        super.mouseClicked(mouseX, mouseY, mouseButton, gui);
        if (this.buttonDownload.isMouseOver(mouseX, mouseY) && !this.downloadStarted && gui.getSharedView().isInsideView(mouseX, mouseY)) {
            this.downloadStarted = true;
            gui.getResourcepacks24().getRp24Api().download(this.pack.rp_id, new ActionResponse<String>() {
                @Override
                public void success(final String url) {
                    final String fileName = OnlinePackElement.this.pack.ingame_name.replaceAll("[\\\\/:*?\"<>|]", "");
                    final File file = new File(Resourcepacks24.getInstance().resourcepacksDir, String.valueOf(fileName) + ".zip");
                    OnlinePackElement.this.downloadAsync(url, file, gui);
                }
                
                @Override
                public void failed(final String message) {
                    gui.getSharedView().lastErrorMessage = message;
                }
            });
        }
    }
    
    protected void downloadAsync(final String urlString, final File file, final GuiResourcepacks24 gui) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final File tmpFile = new File(file.getParentFile(), String.valueOf(file.getName()) + ".download");
                    final URL url = new URL(urlString);
                    final HttpsURLConnection httpConnection = (HttpsURLConnection)url.openConnection();
                    httpConnection.setSSLSocketFactory(gui.getResourcepacks24().getRp24Api().getSocketFactory());
                    httpConnection.setRequestProperty("User-Agent", Source.getUserAgent());
                    final long contentLength = httpConnection.getContentLength();
                    final BufferedInputStream bufferedInputStream = new BufferedInputStream(httpConnection.getInputStream());
                    final FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
                    final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);
                    final byte[] data = new byte[6144];
                    long currentLength = 0L;
                    int length = 0;
                    while ((length = bufferedInputStream.read(data, 0, data.length)) >= 0) {
                        currentLength += length;
                        bufferedOutputStream.write(data, 0, length);
                        OnlinePackElement.access$1(OnlinePackElement.this, 100.0 / contentLength * currentLength);
                    }
                    bufferedOutputStream.close();
                    bufferedInputStream.close();
                    tmpFile.renameTo(file);
                    gui.reloadRepositories();
                }
                catch (final Exception e) {
                    e.printStackTrace();
                    gui.getSharedView().lastErrorMessage = e.getMessage();
                }
            }
        }).start();
    }
    
    static /* synthetic */ void access$1(final OnlinePackElement onlinePackElement, final double downloadProgress) {
        onlinePackElement.downloadProgress = downloadProgress;
    }
}
