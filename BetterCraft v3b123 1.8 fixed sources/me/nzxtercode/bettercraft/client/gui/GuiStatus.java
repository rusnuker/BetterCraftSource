// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui;

import net.minecraft.util.EnumChatFormatting;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Objects;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import java.util.concurrent.Executor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.Map;
import net.minecraft.client.gui.GuiScreen;

public class GuiStatus extends GuiScreen
{
    private GuiScreen parent;
    private Map<String, String> lines;
    
    public GuiStatus(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        CompletableFuture.runAsync(() -> this.lines = this.getStatus(), Executors.newSingleThreadExecutor());
        this.getStatus();
        this.buttonList.add(new GuiButton(0, GuiStatus.width / 2 - 75, GuiStatus.height / 2 + 80, 150, 20, "Back"));
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, "Minecraft Server Status", GuiStatus.width / 2, GuiStatus.height / 2 - 100, -1);
        if (Objects.nonNull(this.lines)) {
            final AtomicInteger y = new AtomicInteger();
            this.lines.forEach((key, value) -> Gui.drawCenteredString(this.fontRendererObj, String.format("%s: %s", StringUtils.capitalize(key), value), GuiStatus.width / 2, GuiStatus.height / 2 - 70 + atomicInteger.getAndAdd(this.mc.fontRendererObj.FONT_HEIGHT), -1));
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public final Map<String, String> getStatus() {
        final Map<String, String> status = new ConcurrentHashMap<String, String>();
        try {
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
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            final URL url = new URL("https://minecraftstatus.net/");
            final HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:103.0) Gecko/20100101 Firefox/103.0");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String string = null;
            final String content = bufferedReader.lines().collect(() -> new StringBuilder(), (builder, string) -> builder.append(string), (builder1, builder2) -> builder1.append((CharSequence)builder2)).toString();
            String[] split;
            for (int length = (split = content.split("<h3>Minecraft Java</h3>")[1].split("</div><div class=\"w3-container w3-border w3-padding-16 w3-margin-bottom\">")[0].split("<div class=\"w3-row w3-container w3-border w3-padding-8\">")).length, i = 0; i < length; ++i) {
                string = split[i];
                if (!string.isEmpty()) {
                    status.put(string.split("<h4>")[1].split("</h4>")[0], String.format("%s%s", EnumChatFormatting.getValueByName(string.split("w3-right-align w3-text-")[1].split("\"><h5>")[0]), string.split("<h5>")[1].split("</h5>")[0]));
                }
            }
            bufferedReader.close();
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
        return status;
    }
}
