// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.api;

import java.io.Writer;
import org.apache.commons.io.IOUtils;
import java.io.StringWriter;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import net.labymod.addons.resourcepacks24.api.util.enums.EnumFeedType;
import net.labymod.addons.resourcepacks24.api.util.enums.EnumResourcepackType;
import net.labymod.addons.resourcepacks24.api.model.Pack;
import net.labymod.addons.resourcepacks24.api.util.interfaces.RawResponse;
import net.labymod.addons.resourcepacks24.api.util.enums.EnumApiAction;
import net.labymod.addons.resourcepacks24.api.util.interfaces.ActionResponse;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import net.labymod.utils.texture.ThreadDownloadTextureImage;
import net.labymod.main.Source;
import net.labymod.addons.resourcepacks24.gui.elements.basement.PackElement;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.util.HashMap;
import java.util.concurrent.Executors;
import net.labymod.utils.texture.DynamicTextureManager;
import javax.net.ssl.SSLSocketFactory;
import java.util.Map;
import java.util.concurrent.Executor;
import com.google.gson.Gson;

public class Resourcepacks24Api
{
    private static final String API_URL = "https://resourcepacks24.de/api/%s/%s";
    private static final Gson GSON;
    private final String token;
    private Executor executor;
    private Map<String, String> jsonCache;
    private SSLSocketFactory socketFactory;
    private DynamicTextureManager dynamicIconManager;
    
    static {
        GSON = new Gson();
    }
    
    public Resourcepacks24Api(final String token) {
        this.executor = Executors.newSingleThreadExecutor();
        this.jsonCache = new HashMap<String, String>();
        this.token = token;
        final TrustManager[] trustAllCerts = { new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                
                @Override
                public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                }
                
                @Override
                public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                }
            } };
        try {
            final SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trustAllCerts, new SecureRandom());
            this.socketFactory = context.getSocketFactory();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        (this.dynamicIconManager = new DynamicTextureManager("rp24", PackElement.DEFAULT_ICON)).init();
        this.dynamicIconManager.setSocketFactory(this.socketFactory);
        this.dynamicIconManager.setUserAgent(Source.getUserAgent());
        this.dynamicIconManager.setTextureImageParser(new ThreadDownloadTextureImage.TextureImageParser() {
            @Override
            public BufferedImage parse(final BufferedImage input) {
                final int dWidth = 64;
                final int dHeight = 64;
                final BufferedImage scaledImage = new BufferedImage(64, 64, input.getType());
                final Graphics2D graphics2D = scaledImage.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.drawImage(input, 0, 0, 64, 64, null);
                graphics2D.dispose();
                return scaledImage;
            }
        });
    }
    
    public void download(final int id, final ActionResponse<String> response) {
        this.perform(EnumApiAction.DOWNLOAD, String.valueOf(id), new RawResponse() {
            @Override
            public void success(final String url) {
                response.success(url);
            }
            
            @Override
            public void error(final String message, final Exception exception) {
                exception.printStackTrace();
                response.failed(message);
            }
        });
    }
    
    public void category(final ActionResponse<String[]> response) {
        this.perform(EnumApiAction.CATEGORY, "", new RawResponse() {
            @Override
            public void success(final String json) {
                response.success(Resourcepacks24Api.GSON.fromJson(json, String[].class));
            }
            
            @Override
            public void error(final String message, final Exception exception) {
                exception.printStackTrace();
                response.failed(message);
            }
        });
    }
    
    public void category(final String name, final int page, final ActionResponse<Pack[]> response) {
        final String param = String.valueOf(name) + "?page=" + page;
        this.perform(EnumApiAction.CATEGORY, param, new RawResponse() {
            @Override
            public void success(final String json) {
                response.success(Resourcepacks24Api.GSON.fromJson(json, Pack[].class));
            }
            
            @Override
            public void error(final String message, final Exception exception) {
                exception.printStackTrace();
                response.failed(message);
            }
        });
    }
    
    public void resourcepack(final int id, final ActionResponse<Pack> response) {
        this.perform(EnumApiAction.RESOURCEPACK, String.valueOf(id), new RawResponse() {
            @Override
            public void success(final String json) {
                response.success(Resourcepacks24Api.GSON.fromJson(json, Pack.class));
            }
            
            @Override
            public void error(final String message, final Exception exception) {
                exception.printStackTrace();
                response.failed(message);
            }
        });
    }
    
    public void resourcepacks(final EnumResourcepackType type, final ActionResponse<Pack[]> response) {
        this.perform(EnumApiAction.RESOURCEPACKS, type.getKey(), new RawResponse() {
            @Override
            public void success(final String json) {
                response.success(Resourcepacks24Api.GSON.fromJson(json, Pack[].class));
            }
            
            @Override
            public void error(final String message, final Exception exception) {
                exception.printStackTrace();
                response.failed(message);
            }
        });
    }
    
    public void feed(final EnumFeedType type, final int page, final ActionResponse<Pack[]> response) {
        final String param = String.valueOf(type.getKey()) + "?page=" + page;
        this.perform(EnumApiAction.FEED, param, new RawResponse() {
            @Override
            public void success(final String json) {
                response.success(Resourcepacks24Api.GSON.fromJson(json, Pack[].class));
            }
            
            @Override
            public void error(final String message, final Exception exception) {
                exception.printStackTrace();
                response.failed(message);
            }
        });
    }
    
    public void search(final String query, final ActionResponse<Pack[]> response) {
        try {
            this.perform(EnumApiAction.SEARCH, URLEncoder.encode(query, StandardCharsets.UTF_8.toString()), new RawResponse() {
                @Override
                public void success(final String json) {
                    response.success(Resourcepacks24Api.GSON.fromJson(json, Pack[].class));
                }
                
                @Override
                public void error(final String message, final Exception exception) {
                    exception.printStackTrace();
                    response.failed(message);
                }
            });
        }
        catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    private void perform(final EnumApiAction action, final String value, final RawResponse response) {
        final String cacheKey = String.valueOf(action.name()) + "/" + value;
        final String cachedJson = this.jsonCache.get(cacheKey);
        if (cachedJson != null) {
            response.success(cachedJson);
        }
        else {
            this.executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final URL url = new URL(String.format("https://resourcepacks24.de/api/%s/%s", action.getKey(), value));
                        final HttpsURLConnection httpConnection = (HttpsURLConnection)url.openConnection();
                        httpConnection.setRequestProperty("Content-Type", "application/json");
                        httpConnection.setRequestProperty("RP24-Token", Resourcepacks24Api.this.token);
                        httpConnection.setRequestProperty("User-Agent", Source.getUserAgent());
                        httpConnection.setSSLSocketFactory(Resourcepacks24Api.this.socketFactory);
                        final StringWriter writer = new StringWriter();
                        IOUtils.copy(httpConnection.getInputStream(), writer, StandardCharsets.UTF_8);
                        final String json = writer.toString();
                        Resourcepacks24Api.this.jsonCache.put(cacheKey, json);
                        response.success(json);
                    }
                    catch (final Exception error) {
                        response.error(error.getMessage(), error);
                    }
                }
            });
        }
    }
    
    public SSLSocketFactory getSocketFactory() {
        return this.socketFactory;
    }
    
    public DynamicTextureManager getDynamicIconManager() {
        return this.dynamicIconManager;
    }
}
