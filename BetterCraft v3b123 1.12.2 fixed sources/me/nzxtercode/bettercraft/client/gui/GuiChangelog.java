// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui;

import java.net.URLConnection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Objects;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import java.util.concurrent.Executor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class GuiChangelog extends GuiScreen
{
    private GuiScreen parent;
    private List<String> lines;
    
    public GuiChangelog(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        CompletableFuture.runAsync(() -> this.lines = this.getStatus(), Executors.newSingleThreadExecutor());
        this.buttonList.add(new GuiButton(0, GuiChangelog.width / 2 - 75, GuiChangelog.height / 2 + 80, 150, 20, "Back"));
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
        Gui.drawCenteredString(this.fontRendererObj, "Changelog/Info", GuiChangelog.width / 2, GuiChangelog.height / 2 - 85, -1);
        if (Objects.nonNull(this.lines)) {
            final AtomicInteger y = new AtomicInteger();
            this.lines.forEach(line -> Gui.drawCenteredString(this.fontRendererObj, line, GuiChangelog.width / 2, GuiChangelog.height / 2 - 40 + atomicInteger.getAndAdd(this.mc.fontRendererObj.FONT_HEIGHT), -1));
        }
        else {
            Gui.drawCenteredString(this.fontRendererObj, "Loading...", GuiChangelog.width / 2, GuiChangelog.height / 2 - 40, -1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public final List<String> getStatus() {
        final AtomicReference<List<String>> returnValue = new AtomicReference<List<String>>();
        try {
            final URL url = new URL("https://pastebin.com/raw/1wgquMNW");
            final URLConnection connection = url.openConnection();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            returnValue.set(bufferedReader.lines().collect((Collector<? super String, ?, List<String>>)Collectors.toList()));
            bufferedReader.close();
        }
        catch (final IOException exception) {
            exception.printStackTrace();
        }
        return returnValue.get();
    }
}
