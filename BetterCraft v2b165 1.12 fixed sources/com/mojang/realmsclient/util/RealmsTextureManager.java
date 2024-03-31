// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.util;

import org.apache.logging.log4j.LogManager;
import java.util.HashMap;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.ARBMultitexture;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.io.ByteArrayInputStream;
import org.apache.commons.codec.binary.Base64;
import java.awt.image.BufferedImage;
import javax.xml.bind.DatatypeConverter;
import java.io.OutputStream;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import org.apache.commons.io.IOUtils;
import javax.imageio.ImageIO;
import net.minecraft.realms.Realms;
import java.net.URL;
import java.net.HttpURLConnection;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.util.UUIDTypeAdapter;
import java.util.UUID;
import org.lwjgl.opengl.GL11;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.Logger;
import java.util.Map;

public class RealmsTextureManager
{
    private static final Map<String, RealmsTexture> textures;
    private static final Map<String, Boolean> skinFetchStatus;
    private static final Map<String, String> fetchedSkins;
    private static Boolean useMultitextureArb;
    public static int GL_TEXTURE0;
    private static final Logger LOGGER;
    private static final String STEVE_LOCATION = "minecraft:textures/entity/steve.png";
    private static final String ALEX_LOCATION = "minecraft:textures/entity/alex.png";
    
    public static void bindWorldTemplate(final String id, final String image) {
        if (image == null) {
            RealmsScreen.bind("textures/gui/presets/isles.png");
            return;
        }
        final int textureId = getTextureId(id, image);
        GL11.glBindTexture(3553, textureId);
    }
    
    public static void bindDefaultFace(final UUID uuid) {
        RealmsScreen.bind(((uuid.hashCode() & 0x1) == 0x1) ? "minecraft:textures/entity/alex.png" : "minecraft:textures/entity/steve.png");
    }
    
    public static void bindFace(final String uuid) {
        final UUID actualUuid = UUIDTypeAdapter.fromString(uuid);
        if (RealmsTextureManager.textures.containsKey(uuid)) {
            GL11.glBindTexture(3553, RealmsTextureManager.textures.get(uuid).textureId);
            return;
        }
        if (RealmsTextureManager.skinFetchStatus.containsKey(uuid)) {
            if (!RealmsTextureManager.skinFetchStatus.get(uuid)) {
                bindDefaultFace(actualUuid);
            }
            else if (RealmsTextureManager.fetchedSkins.containsKey(uuid)) {
                final int textureId = getTextureId(uuid, RealmsTextureManager.fetchedSkins.get(uuid));
                GL11.glBindTexture(3553, textureId);
            }
            else {
                bindDefaultFace(actualUuid);
            }
            return;
        }
        RealmsTextureManager.skinFetchStatus.put(uuid, false);
        bindDefaultFace(actualUuid);
        final Thread thread = new Thread("Realms Texture Downloader") {
            @Override
            public void run() {
                final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> fetchedTextures = RealmsUtil.getTextures(uuid);
                if (fetchedTextures.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                    final MinecraftProfileTexture textureInfo = fetchedTextures.get(MinecraftProfileTexture.Type.SKIN);
                    final String url = textureInfo.getUrl();
                    HttpURLConnection connection = null;
                    RealmsTextureManager.LOGGER.debug("Downloading http texture from {}", url);
                    try {
                        connection = (HttpURLConnection)new URL(url).openConnection(Realms.getProxy());
                        connection.setDoInput(true);
                        connection.setDoOutput(false);
                        connection.connect();
                        if (connection.getResponseCode() / 100 != 2) {
                            RealmsTextureManager.skinFetchStatus.remove(uuid);
                            return;
                        }
                        BufferedImage loadedImage;
                        try {
                            loadedImage = ImageIO.read(connection.getInputStream());
                        }
                        catch (final Exception ignored) {
                            RealmsTextureManager.skinFetchStatus.remove(uuid);
                            return;
                        }
                        finally {
                            IOUtils.closeQuietly(connection.getInputStream());
                        }
                        loadedImage = new SkinProcessor().process(loadedImage);
                        final ByteArrayOutputStream output = new ByteArrayOutputStream();
                        ImageIO.write(loadedImage, "png", output);
                        RealmsTextureManager.fetchedSkins.put(uuid, DatatypeConverter.printBase64Binary(output.toByteArray()));
                        RealmsTextureManager.skinFetchStatus.put(uuid, true);
                    }
                    catch (final Exception e) {
                        RealmsTextureManager.LOGGER.error("Couldn't download http texture", e);
                        RealmsTextureManager.skinFetchStatus.remove(uuid);
                    }
                    finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                    return;
                }
                RealmsTextureManager.skinFetchStatus.put(uuid, true);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
    
    public static int getTextureId(final String id, final String image) {
        int textureId;
        if (RealmsTextureManager.textures.containsKey(id)) {
            final RealmsTexture texture = RealmsTextureManager.textures.get(id);
            if (texture.image.equals(image)) {
                return texture.textureId;
            }
            GL11.glDeleteTextures(texture.textureId);
            textureId = texture.textureId;
        }
        else {
            textureId = GL11.glGenTextures();
        }
        IntBuffer buf = null;
        int width = 0;
        int height = 0;
        try {
            final InputStream in = new ByteArrayInputStream(new Base64().decode(image));
            BufferedImage img;
            try {
                img = ImageIO.read(in);
            }
            finally {
                IOUtils.closeQuietly(in);
            }
            width = img.getWidth();
            height = img.getHeight();
            final int[] data = new int[width * height];
            img.getRGB(0, 0, width, height, data, 0, width);
            buf = ByteBuffer.allocateDirect(4 * width * height).order(ByteOrder.nativeOrder()).asIntBuffer();
            buf.put(data);
            buf.flip();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        if (RealmsTextureManager.GL_TEXTURE0 == -1) {
            if (getUseMultiTextureArb()) {
                RealmsTextureManager.GL_TEXTURE0 = 33984;
            }
            else {
                RealmsTextureManager.GL_TEXTURE0 = 33984;
            }
        }
        glActiveTexture(RealmsTextureManager.GL_TEXTURE0);
        GL11.glBindTexture(3553, textureId);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 32993, 33639, buf);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10241, 9729);
        RealmsTextureManager.textures.put(id, new RealmsTexture(image, textureId));
        return textureId;
    }
    
    public static void glActiveTexture(final int texture) {
        if (getUseMultiTextureArb()) {
            ARBMultitexture.glActiveTextureARB(texture);
        }
        else {
            GL13.glActiveTexture(texture);
        }
    }
    
    public static boolean getUseMultiTextureArb() {
        if (RealmsTextureManager.useMultitextureArb == null) {
            final ContextCapabilities caps = GLContext.getCapabilities();
            RealmsTextureManager.useMultitextureArb = (caps.GL_ARB_multitexture && !caps.OpenGL13);
        }
        return RealmsTextureManager.useMultitextureArb;
    }
    
    static {
        textures = new HashMap<String, RealmsTexture>();
        skinFetchStatus = new HashMap<String, Boolean>();
        fetchedSkins = new HashMap<String, String>();
        RealmsTextureManager.GL_TEXTURE0 = -1;
        LOGGER = LogManager.getLogger();
    }
    
    public static class RealmsTexture
    {
        String image;
        int textureId;
        
        public RealmsTexture(final String image, final int textureId) {
            this.image = image;
            this.textureId = textureId;
        }
    }
}
