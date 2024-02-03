// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.texture;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import java.util.concurrent.Executors;
import java.awt.image.BufferedImage;
import net.labymod.support.util.Debug;
import javax.net.ssl.SSLSocketFactory;
import net.labymod.utils.Consumer;
import java.util.concurrent.ExecutorService;
import net.minecraft.client.renderer.texture.SimpleTexture;

public class ThreadDownloadTextureImage extends SimpleTexture
{
    private static ExecutorService executorService;
    private final String imageUrl;
    private final Consumer<Boolean> consumer;
    private String userAgent;
    private SSLSocketFactory socketFactory;
    private TextureImageParser textureImageParser;
    private Debug.EnumDebugMode debugMode;
    private BufferedImage bufferedImage;
    private boolean textureLoaded;
    
    static {
        ThreadDownloadTextureImage.executorService = Executors.newFixedThreadPool(5);
    }
    
    public ThreadDownloadTextureImage(final String imageUrl, final ResourceLocation textureResourceLocation, final Consumer<Boolean> consumer, final String userAgent) {
        super(textureResourceLocation);
        this.userAgent = "Unknown";
        this.debugMode = Debug.EnumDebugMode.GENERAL;
        this.textureLoaded = false;
        this.imageUrl = imageUrl;
        this.consumer = consumer;
        this.userAgent = userAgent;
    }
    
    @Override
    public int getGlTextureId() {
        final int textureId = super.getGlTextureId();
        if (!this.textureLoaded && this.bufferedImage != null) {
            this.textureLoaded = true;
            TextureUtil.uploadTextureImage(textureId, this.bufferedImage);
        }
        return textureId;
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        this.downloadTexture(null);
    }
    
    public void downloadTexture(final Consumer<ThreadDownloadTextureImage> loadTextureCallback) {
        ThreadDownloadTextureImage.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(ThreadDownloadTextureImage.this.imageUrl).openConnection();
                    httpURLConnection.setRequestProperty("User-Agent", ThreadDownloadTextureImage.this.userAgent);
                    if (ThreadDownloadTextureImage.this.socketFactory != null && httpURLConnection instanceof HttpsURLConnection) {
                        ((HttpsURLConnection)httpURLConnection).setSSLSocketFactory(ThreadDownloadTextureImage.this.socketFactory);
                    }
                    httpURLConnection.connect();
                    final int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode / 100 == 2) {
                        BufferedImage loadedImage = TextureUtil.readBufferedImage(httpURLConnection.getInputStream());
                        if (ThreadDownloadTextureImage.this.textureImageParser != null) {
                            loadedImage = ThreadDownloadTextureImage.this.textureImageParser.parse(loadedImage);
                        }
                        ThreadDownloadTextureImage.access$4(ThreadDownloadTextureImage.this, loadedImage);
                        if (ThreadDownloadTextureImage.this.bufferedImage == null) {
                            Debug.log(ThreadDownloadTextureImage.this.debugMode, "Failed to download texture " + ThreadDownloadTextureImage.this.imageUrl + " (" + responseCode + ")");
                        }
                        else {
                            Debug.log(ThreadDownloadTextureImage.this.debugMode, "Downloaded texture " + ThreadDownloadTextureImage.this.imageUrl);
                        }
                    }
                    else {
                        Debug.log(ThreadDownloadTextureImage.this.debugMode, "Response code for " + ThreadDownloadTextureImage.this.imageUrl + " is " + responseCode);
                    }
                }
                catch (final Exception error) {
                    error.printStackTrace();
                }
                ThreadDownloadTextureImage.this.consumer.accept(ThreadDownloadTextureImage.this.bufferedImage != null);
                if (loadTextureCallback != null) {
                    loadTextureCallback.accept(ThreadDownloadTextureImage.this);
                }
            }
        });
    }
    
    public void setSocketFactory(final SSLSocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }
    
    public void setTextureImageParser(final TextureImageParser textureImageParser) {
        this.textureImageParser = textureImageParser;
    }
    
    public void setDebugMode(final Debug.EnumDebugMode debugMode) {
        this.debugMode = debugMode;
    }
    
    static /* synthetic */ void access$4(final ThreadDownloadTextureImage threadDownloadTextureImage, final BufferedImage bufferedImage) {
        threadDownloadTextureImage.bufferedImage = bufferedImage;
    }
    
    public interface TextureImageParser
    {
        BufferedImage parse(final BufferedImage p0);
    }
}
