// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.splash;

import net.labymod.utils.ModColor;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.main.LabyMod;
import java.net.URLConnection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.labymod.main.Source;
import java.net.URL;
import java.lang.reflect.Type;
import net.labymod.splash.advertisement.AdColorAdapter;
import java.awt.Color;
import com.google.gson.GsonBuilder;
import net.labymod.support.DashboardConnector;
import net.labymod.utils.Consumer;
import net.labymod.splash.advertisement.Advertisement;
import com.google.gson.Gson;

public class SplashLoader
{
    private static SplashLoader loader;
    private final Gson gson;
    private SplashEntries entries;
    private Advertisement hoverAdvertisement;
    private Consumer<SplashLoader> loadListener;
    private DashboardConnector dashboardConnector;
    
    static {
        SplashLoader.loader = new SplashLoader();
    }
    
    public SplashLoader() {
        this.dashboardConnector = new DashboardConnector();
        this.gson = new GsonBuilder().registerTypeAdapter(Color.class, new AdColorAdapter()).create();
        this.load();
    }
    
    public void load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final URLConnection connection = new URL("http://dl.labymod.net/advertisement/entries.json").openConnection();
                    connection.setRequestProperty("User-Agent", Source.getUserAgent());
                    connection.connect();
                    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String json = "";
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        json = String.valueOf(json) + (json.equals("") ? "" : "\n") + line;
                    }
                    if (json.isEmpty()) {
                        return;
                    }
                    SplashLoader.access$1(SplashLoader.this, SplashLoader.this.gson.fromJson(json, SplashEntries.class));
                    if (SplashLoader.this.loadListener != null) {
                        SplashLoader.this.loadListener.accept(SplashLoader.this);
                    }
                }
                catch (final Exception error) {
                    error.printStackTrace();
                }
            }
        }).start();
    }
    
    public void render(final int mouseX, final int mouseY) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        this.hoverAdvertisement = null;
        if (this.entries != null) {
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            try {
                this.renderList(this.entries.getLeft(), false, 3, 3, mouseX, mouseY);
                this.renderList(this.entries.getRight(), true, draw.getWidth() - 3, 3, mouseX, mouseY);
            }
            catch (final Exception error) {
                error.printStackTrace();
                this.entries = null;
            }
            GlStateManager.disableAlpha();
        }
    }
    
    private void renderList(final Advertisement[] advertisements, final boolean rightBound, final int positionX, int positionY, final int mouseX, final int mouseY) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final int iconSize = 15;
        for (final Advertisement advertisement : advertisements) {
            if (advertisement != null && advertisement.isVisible()) {
                int x = positionX;
                GlStateManager.pushMatrix();
                final int width = draw.getStringWidth(advertisement.getTitle()) + 2;
                if (rightBound) {
                    x -= 15 + width;
                }
                final boolean hover = mouseX > x && mouseX < x + 15 + width && mouseY > positionY && mouseY < positionY + 15;
                final Color textColor = hover ? advertisement.getColorHover() : advertisement.getColor();
                final String url = String.format("http://dl.labymod.net/advertisement/icons/%s.png", advertisement.getIconName());
                LabyMod.getInstance().getDrawUtils().drawImageUrl(url, x - (hover ? 1 : 0) + (rightBound ? width : 0), positionY - (hover ? 1 : 0), 255.0, 255.0, 15 + (hover ? 2 : 0), 15 + (hover ? 2 : 0));
                if (rightBound) {
                    x -= 17;
                }
                GlStateManager.popMatrix();
                if (advertisement.isNew()) {
                    float pause = (float)(Math.sin(System.currentTimeMillis() / 500.0) * 3.0);
                    if (pause < 0.0f) {
                        pause = 0.0f;
                    }
                    final float bounce = (float)Math.abs(Math.cos(System.currentTimeMillis() / 100.0) * -pause);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_EXCLAMATION);
                    draw.drawTexture(x + (rightBound ? (width + 7) : 0) + 10 + (hover ? (rightBound ? -1 : 1) : 0), positionY + 5 + (hover ? 1 : 0) - bounce, hover ? 125.0 : 0.0, 0.0, 120.0, 255.0, 5.0, 10.0, 1.0f);
                }
                draw.drawString(draw.getFontRenderer(), ModColor.createColors(advertisement.getTitle()), x + 15 + 2 + (hover ? (rightBound ? -1 : 1) : 0), positionY + 7 - 4, textColor.getRGB());
                if (hover) {
                    this.hoverAdvertisement = advertisement;
                }
                positionY += 16;
            }
        }
        if (rightBound) {
            this.dashboardConnector.renderIcon(positionX - 10, positionY + 10, mouseX, mouseY);
        }
    }
    
    public void onClick(final int mouseX, final int mouseY) {
        if (this.hoverAdvertisement != null) {
            LabyMod.getInstance().openWebpage(this.hoverAdvertisement.getUrl(), true);
        }
        this.dashboardConnector.mouseClicked(mouseX, mouseY, 1);
    }
    
    public static SplashLoader getLoader() {
        return SplashLoader.loader;
    }
    
    public SplashEntries getEntries() {
        return this.entries;
    }
    
    public void setLoadListener(final Consumer<SplashLoader> loadListener) {
        this.loadListener = loadListener;
    }
    
    static /* synthetic */ void access$1(final SplashLoader splashLoader, final SplashEntries entries) {
        splashLoader.entries = entries;
    }
}
