// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui.section;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.EnumChatFormatting;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import me.nzxtercode.bettercraft.client.Config;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiScreen;

public class GuiUISettings extends GuiScreen
{
    private GuiScreen parent;
    public final Map<String, List<String>> sectionList;
    public static boolean[] enabledBackgrounds;
    public static boolean[] enabledAnimations;
    public static boolean[] enabledItems;
    public static boolean[] enabledUI;
    
    static {
        GuiUISettings.enabledBackgrounds = getEnabledBackgrounds();
        GuiUISettings.enabledAnimations = getEnabledAnimations();
        GuiUISettings.enabledItems = getEnabledItems();
        GuiUISettings.enabledUI = getEnabledUI();
    }
    
    public GuiUISettings(final GuiScreen parent) {
        this.sectionList = (Map<String, List<String>>)Maps.newConcurrentMap();
        this.parent = parent;
    }
    
    private static boolean[] getEnabledBackgrounds() {
        final boolean[] array = { Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("Background").getAsJsonObject().get("Tab").getAsBoolean(), Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("Background").getAsJsonObject().get("Scoreboard").getAsBoolean(), Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("Background").getAsJsonObject().get("Chat").getAsBoolean(), Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("Background").getAsJsonObject().get("Particel").getAsBoolean() };
        return array;
    }
    
    private static boolean[] getEnabledAnimations() {
        final boolean[] array = { Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("Animation").getAsJsonObject().get("Button").getAsBoolean(), Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("Animation").getAsJsonObject().get("Chat").getAsBoolean(), Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("Animation").getAsJsonObject().get("Disconnect").getAsBoolean() };
        return array;
    }
    
    private static boolean[] getEnabledItems() {
        final boolean[] array = { Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("Items").getAsJsonObject().get("Physic").getAsBoolean() };
        return array;
    }
    
    private static boolean[] getEnabledUI() {
        final boolean[] array = { Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("UI").getAsJsonObject().get("Hotbar").getAsBoolean(), Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("UI").getAsJsonObject().get("BlockOverlay").getAsBoolean(), Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get("UI").getAsJsonObject().get("TransButton").getAsBoolean() };
        return array;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.sectionList.clear();
        this.buttonList.add(new GuiButton(0, GuiUISettings.width - 93, GuiUISettings.height - 23, 90, 20, "Back"));
        final AtomicInteger xPosition = new AtomicInteger(25);
        Config.getInstance().getSettings("Section").get("list").getAsJsonObject().entrySet().forEach(entry -> {
            final String name = entry.getKey();
            final AtomicInteger yPosition = new AtomicInteger(50);
            entry.getValue().getAsJsonObject().entrySet().forEach(elementEntry -> {
                this.sectionList.computeIfAbsent(s, elements -> Lists.newArrayList()).add(elementEntry.getKey());
                final List<GuiButton> buttonList = this.buttonList;
                new(net.minecraft.client.gui.GuiButton.class)();
                final int buttonId = atomicInteger2.get() - 24;
                final int x = atomicInteger2.get() + 10;
                atomicInteger3.getAndAdd(25);
                new StringBuilder(String.valueOf(elementEntry.getValue().getAsBoolean() ? EnumChatFormatting.GREEN.toString() : EnumChatFormatting.RED.toString()));
                final StringBuilder sb;
                final int y;
                final int widthIn;
                final int heightIn;
                new GuiButton(buttonId, x, y, widthIn, heightIn, sb.append(elementEntry.getKey()).toString());
                final GuiButton guiButton;
                buttonList.add(guiButton);
                return;
            });
            atomicInteger.getAndAdd(105);
        });
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parent);
        }
        else {
            final int index = button.id / 105;
            final String name = String.valueOf(this.sectionList.keySet().toArray()[index]);
            final String element = button.displayString.substring(2);
            final boolean value = Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get(name).getAsJsonObject().get(element).getAsBoolean();
            Config.getInstance().editSettings("Section", json -> {
                final JsonObject jsonObject = json.get("list").getAsJsonObject();
                final JsonObject asJsonObject;
                final JsonObject sectionJson = asJsonObject = jsonObject.get(s).getAsJsonObject();
                new JsonPrimitive(!sectionJson.get(s2).getAsBoolean());
                final JsonPrimitive value2;
                asJsonObject.add(s2, value2);
                jsonObject.add(s, sectionJson);
                json.add("list", jsonObject);
                return;
            });
            this.buttonList.forEach(buttonObj -> {
                if (buttonObj.id != 0) {
                    final int indexObj = buttonObj.id / 105;
                    final String nameObj = String.valueOf(this.sectionList.keySet().toArray()[indexObj]);
                    final String elementObj = buttonObj.displayString.substring(2);
                    final boolean valueObj = Config.getInstance().getSettings("Section").get("list").getAsJsonObject().get(nameObj).getAsJsonObject().get(elementObj).getAsBoolean();
                    new StringBuilder(String.valueOf(valueObj ? EnumChatFormatting.GREEN.toString() : EnumChatFormatting.RED.toString()));
                    final StringBuilder sb;
                    buttonObj.displayString = sb.append(elementObj).toString();
                }
                return;
            });
        }
        GuiUISettings.enabledBackgrounds = getEnabledBackgrounds();
        GuiUISettings.enabledAnimations = getEnabledAnimations();
        GuiUISettings.enabledItems = getEnabledItems();
        GuiUISettings.enabledUI = getEnabledUI();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        final AtomicInteger xPosition = new AtomicInteger(25);
        this.sectionList.forEach((name, elements) -> {
            final int x = atomicInteger.getAndAdd(105);
            Gui.drawRect(x, 25, x + 100, GuiUISettings.height - 25, Integer.MIN_VALUE);
            Gui.drawCenteredString(this.mc.fontRendererObj, name, x + 50, 30, -1);
            return;
        });
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
