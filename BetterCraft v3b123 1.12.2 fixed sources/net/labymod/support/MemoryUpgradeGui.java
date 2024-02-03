// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.support;

import net.labymod.main.Source;
import net.minecraft.client.gui.GuiMainMenu;
import java.io.IOException;
import net.labymod.utils.ModUtils;
import com.google.gson.JsonObject;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import com.google.gson.JsonElement;
import net.labymod.utils.JsonParse;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.FileInputStream;
import net.labymod.utils.Consumer;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import net.labymod.utils.ModColor;
import net.labymod.utils.DrawUtils;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.main.LabyMod;
import net.labymod.account.MCBaseFolder;
import net.minecraft.client.gui.GuiButton;
import net.labymod.settings.elements.SliderElement;
import com.google.gson.GsonBuilder;
import java.io.File;
import net.minecraft.client.gui.GuiScreen;

public class MemoryUpgradeGui extends GuiScreen
{
    private static final long TRIGGER_MAX_MEMORY = 2048000000L;
    private static final int TRIGGER_SCORE = 3;
    private static final long TRIGGER_FRAME_DURATION = 100L;
    private static long maxMemoryUsed;
    private static long lastMemoryMax;
    private static boolean outOfMemoryDetected;
    private static long lastFrameMemory;
    private static long lastFrameTimestamp;
    private static int frameHits;
    private final File launcherProfilesFile;
    private final GsonBuilder gsonBuilder;
    private final int totalMB;
    private SliderElement element;
    private GuiButton buttonRestartGame;
    private String errorMessage;
    private int updatedMB;
    
    static {
        MemoryUpgradeGui.maxMemoryUsed = 0L;
        MemoryUpgradeGui.lastMemoryMax = 0L;
        MemoryUpgradeGui.outOfMemoryDetected = false;
        MemoryUpgradeGui.lastFrameMemory = -1L;
        MemoryUpgradeGui.lastFrameTimestamp = -1L;
        MemoryUpgradeGui.frameHits = 0;
    }
    
    public MemoryUpgradeGui() {
        this.launcherProfilesFile = new File(MCBaseFolder.getWorkingDirectory(), "launcher_profiles.json");
        this.gsonBuilder = new GsonBuilder().setPrettyPrinting();
        this.errorMessage = null;
        final long maxMemory = Runtime.getRuntime().maxMemory();
        int totalMB = (int)(maxMemory / 1000000L);
        if (totalMB < 1) {
            totalMB = 1;
        }
        this.totalMB = totalMB;
        this.updatedMB = totalMB;
    }
    
    public static void renderTickOutOfMemoryDetector() {
        final long maxMemory = Runtime.getRuntime().maxMemory();
        if (maxMemory >= 2048000000L) {
            return;
        }
        final long totalMemory = Runtime.getRuntime().totalMemory();
        final long freeMemory = Runtime.getRuntime().freeMemory();
        final long usedMemory = totalMemory - freeMemory;
        MemoryUpgradeGui.maxMemoryUsed = Math.max(usedMemory, (MemoryUpgradeGui.lastMemoryMax == maxMemory) ? MemoryUpgradeGui.maxMemoryUsed : usedMemory);
        MemoryUpgradeGui.lastMemoryMax = maxMemory;
        if (!MemoryUpgradeGui.outOfMemoryDetected && MemoryUpgradeGui.lastFrameMemory != -1L) {
            final long frameDuration = System.currentTimeMillis() - MemoryUpgradeGui.lastFrameTimestamp;
            if (MemoryUpgradeGui.maxMemoryUsed >= 99L) {
                MemoryUpgradeGui.outOfMemoryDetected = true;
            }
            else if (MemoryUpgradeGui.lastFrameMemory > usedMemory) {
                if (frameDuration > 100L) {
                    ++MemoryUpgradeGui.frameHits;
                    if (MemoryUpgradeGui.frameHits >= 3) {
                        MemoryUpgradeGui.outOfMemoryDetected = true;
                    }
                }
                else if (MemoryUpgradeGui.frameHits > 0) {
                    --MemoryUpgradeGui.frameHits;
                }
            }
            else if (frameDuration > 100L && MemoryUpgradeGui.frameHits > 0) {
                --MemoryUpgradeGui.frameHits;
            }
        }
        final long percent = MemoryUpgradeGui.maxMemoryUsed * 100L / maxMemory;
        if (percent >= 90L) {
            MemoryUpgradeGui.lastFrameMemory = usedMemory;
            MemoryUpgradeGui.lastFrameTimestamp = System.currentTimeMillis();
        }
        else {
            MemoryUpgradeGui.lastFrameMemory = -1L;
        }
        if (MemoryUpgradeGui.outOfMemoryDetected) {
            renderWarning(percent);
            MemoryUpgradeGui.frameHits = 0;
        }
    }
    
    private static void renderWarning(final long percent) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.0f, 10.0f);
        final String string = LanguageManager.translate("out_of_memory_warning_explanation");
        final int margin = 3;
        final int warningWidth = 200;
        final List<String> lines = draw.listFormattedStringToWidth(string, 191);
        final int warningHeight = 10 + lines.size() * 10 + 3 + 1;
        final double warningX = draw.getWidth() - 200 - 1;
        final int warningY = 2;
        DrawUtils.drawRect(warningX, 2.0, warningX + 200.0, 2 + warningHeight, Integer.MIN_VALUE);
        draw.drawRectBorder(warningX, 2.0, warningX + 200.0, 2 + warningHeight, Integer.MIN_VALUE, 1.0);
        draw.drawString(String.valueOf(ModColor.cl('c')) + ModColor.cl('n') + LanguageManager.translate("out_of_memory_warning_title"), warningX + 3.0, 5.0);
        draw.drawRightString(String.valueOf(ModColor.cl('4')) + percent + "%", warningX + 200.0 - 3.0, 5.0);
        int lineY = 11;
        for (final String line : lines) {
            draw.drawString(String.valueOf(ModColor.cl('f')) + line, warningX + 3.0, 5 + lineY);
            lineY += 10;
        }
        if (Keyboard.isKeyDown(50)) {
            MemoryUpgradeGui.outOfMemoryDetected = false;
            Minecraft.getMinecraft().displayGuiScreen(new MemoryUpgradeGui());
        }
        if (Keyboard.isKeyDown(37)) {
            MemoryUpgradeGui.outOfMemoryDetected = false;
            LabyMod.getSettings().outOfMemoryWarning = false;
        }
        GlStateManager.translate(0.0f, 0.0f, -10.0f);
        GlStateManager.popMatrix();
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        final int divY = MemoryUpgradeGui.height / 2 - 40 + 50;
        this.buttonList.add(new GuiButton(0, MemoryUpgradeGui.width / 2 - 100, divY + 30, 90, 20, LanguageManager.translate("button_cancel")));
        this.buttonList.add(this.buttonRestartGame = new GuiButton(1, MemoryUpgradeGui.width / 2 + 10, divY + 30, 90, 20, LanguageManager.translate("button_restart")));
        this.buttonRestartGame.enabled = (this.totalMB != this.updatedMB);
        this.element = new SliderElement(LanguageManager.translate("out_of_memory_slider"), new ControlElement.IconData(Material.IRON_PICKAXE), this.updatedMB).addCallback(new Consumer<Integer>() {
            @Override
            public void accept(final Integer accepted) {
                MemoryUpgradeGui.this.buttonRestartGame.enabled = (MemoryUpgradeGui.this.totalMB != accepted);
                MemoryUpgradeGui.access$2(MemoryUpgradeGui.this, accepted);
            }
        }).setRange(512, 4096).setSteps(512);
        super.initGui();
    }
    
    private boolean updateMemoryArguments(final String version, final int mb) throws Exception {
        final JsonObject mainObject = JsonParse.parse(IOUtils.toString(new FileInputStream(this.launcherProfilesFile))).getAsJsonObject();
        final JsonObject profiles = mainObject.get("profiles").getAsJsonObject();
        if (!profiles.has(version)) {
            return false;
        }
        final JsonObject versionObject = profiles.get(version).getAsJsonObject();
        final String javaArgs = "javaArgs";
        if (versionObject.has("javaArgs")) {
            final String arguments = versionObject.get("javaArgs").getAsString();
            final String newArguments = arguments.replaceAll("-[x|X][m|M][x|X][0-9]+\\w+", "-Xmx" + mb + "M");
            versionObject.addProperty("javaArgs", newArguments);
        }
        else {
            versionObject.addProperty("javaArgs", "-Xmx" + mb + "M");
        }
        IOUtils.write(this.gsonBuilder.create().toJson(mainObject), new FileOutputStream(this.launcherProfilesFile), StandardCharsets.UTF_8);
        return true;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final int divY = MemoryUpgradeGui.height / 2 - 40;
        draw.drawCenteredString(String.valueOf(ModColor.cl('c')) + LanguageManager.translate("out_of_memory_slider"), MemoryUpgradeGui.width / 2, divY);
        draw.drawCenteredString(LanguageManager.translate("out_of_memory_subtitle"), MemoryUpgradeGui.width / 2, divY + 10);
        draw.drawCenteredString((this.errorMessage == null) ? (String.valueOf(ModColor.cl('a')) + LanguageManager.translate("out_of_memory_recommendation")) : (String.valueOf(ModColor.cl("4")) + this.errorMessage), MemoryUpgradeGui.width / 2, divY + 30);
        final int centerPosX = MemoryUpgradeGui.width / 2;
        final int centerPosY = divY + 55;
        final int elementWidth = 200;
        final int elementHeight = 22;
        final String value = String.valueOf(ModColor.cl('b')) + "= " + ModUtils.humanReadableByteCount(this.updatedMB * 1000000L, true, true);
        draw.drawString(value, centerPosX + 100 + 5, centerPosY - 3);
        this.element.draw(centerPosX - 100, centerPosY - 11, centerPosX + 100, centerPosY + 11, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.element.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.element.mouseRelease(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                Minecraft.getMinecraft().displayGuiScreen(LabyMod.getInstance().isInGame() ? null : new GuiMainMenu());
                break;
            }
            case 1: {
                try {
                    final String version = Minecraft.getMinecraft().getVersion();
                    this.killMinecraftLauncher();
                    if (this.updateMemoryArguments(Source.PROFILE_VERSION_NAME, this.updatedMB) | this.updateMemoryArguments(version, this.updatedMB)) {
                        Minecraft.getMinecraft().shutdown();
                    }
                    else {
                        this.errorMessage = LanguageManager.translate("out_of_memory_profile_not_found", version);
                    }
                }
                catch (final Exception e) {
                    e.printStackTrace();
                    this.errorMessage = "ERROR: " + e.getMessage();
                }
                break;
            }
        }
        super.actionPerformed(button);
    }
    
    private void killMinecraftLauncher() {
        try {
            final Runtime runtime = Runtime.getRuntime();
            runtime.exec("taskkill /F /IM MinecraftLauncher.exe");
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
    
    static /* synthetic */ void access$2(final MemoryUpgradeGui memoryUpgradeGui, final int updatedMB) {
        memoryUpgradeGui.updatedMB = updatedMB;
    }
}
