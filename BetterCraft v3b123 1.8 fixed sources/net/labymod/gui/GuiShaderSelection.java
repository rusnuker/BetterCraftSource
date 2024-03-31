// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import java.io.IOException;
import net.labymod.main.ModTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.gui.GuiButton;
import net.labymod.main.lang.LanguageManager;
import java.util.Objects;
import net.labymod.main.ModSettings;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import net.labymod.utils.Consumer;
import net.minecraft.client.Minecraft;
import net.labymod.utils.ReflectionHelper;
import net.labymod.core.LabyModCore;
import net.minecraft.client.renderer.EntityRenderer;
import net.labymod.main.LabyMod;
import net.minecraft.util.ResourceLocation;
import net.labymod.gui.elements.CheckBox;
import net.labymod.gui.elements.Scrollbar;
import net.minecraft.client.gui.GuiScreen;

public class GuiShaderSelection extends GuiScreen
{
    private static int shaderIndex;
    private final GuiScreen lastScreen;
    private final Scrollbar scrollbar;
    private final CheckBox checkBox;
    private int hoveredShader;
    private ResourceLocation[] shaderResourceLocations;
    private boolean enabled;
    
    public GuiShaderSelection(final GuiScreen lastScreen) {
        this.scrollbar = new Scrollbar(12);
        this.hoveredShader = -1;
        this.enabled = false;
        this.lastScreen = lastScreen;
        if (LabyMod.getInstance().isInGame()) {
            try {
                Field field = ReflectionHelper.findField(EntityRenderer.class, LabyModCore.getMappingAdapter().getShaderResourceLocationsMappings());
                field.setAccessible(true);
                this.shaderResourceLocations = (ResourceLocation[])field.get(Minecraft.getMinecraft().entityRenderer);
                if (!Minecraft.getMinecraft().entityRenderer.isShaderActive()) {
                    GuiShaderSelection.shaderIndex = -1;
                }
                field = ReflectionHelper.findField(EntityRenderer.class, LabyModCore.getMappingAdapter().getUseShaderMappings());
                field.setAccessible(true);
                this.enabled = (boolean)field.get(Minecraft.getMinecraft().entityRenderer);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        (this.checkBox = new CheckBox("Enabled", this.enabled ? CheckBox.EnumCheckBoxValue.ENABLED : CheckBox.EnumCheckBoxValue.DISABLED, null, 0, 0, 20, 20)).setUpdateListener(new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept(final CheckBox.EnumCheckBoxValue accepted) {
                Minecraft.getMinecraft().entityRenderer.switchUseShader();
                if (accepted == CheckBox.EnumCheckBoxValue.DISABLED) {
                    LabyMod.getSettings().loadedShader = null;
                    LabyMod.getMainConfig().save();
                }
            }
        });
    }
    
    public static void loadShader(final ResourceLocation resourceLocation) throws Exception {
        final String[] methodNames = LabyModCore.getMappingAdapter().getLoadShaderMappings();
        Exception exception = null;
        final String[] array = methodNames;
        final int length = array.length;
        int i = 0;
        while (i < length) {
            final String methodName = array[i];
            try {
                final Method method = EntityRenderer.class.getDeclaredMethod(methodName, ResourceLocation.class);
                method.setAccessible(true);
                method.invoke(Minecraft.getMinecraft().entityRenderer, resourceLocation);
                break;
            }
            catch (final Exception e) {
                exception = e;
                ++i;
            }
        }
        if (exception != null) {
            exception.printStackTrace();
        }
    }
    
    private void loadShader(final int index) {
        if (index == -1) {
            GuiShaderSelection.shaderIndex = -1;
            this.mc.entityRenderer.stopUseShader();
            Objects.requireNonNull(LabyMod.getSettings()).loadedShader = null;
            LabyMod.getMainConfig().save();
            return;
        }
        try {
            final ResourceLocation[] shaderResourceLocations = this.shaderResourceLocations;
            GuiShaderSelection.shaderIndex = index;
            final ResourceLocation resourceLocation = shaderResourceLocations[index];
            Objects.requireNonNull(LabyMod.getSettings()).loadedShader = resourceLocation.getResourcePath();
            LabyMod.getMainConfig().save();
            loadShader(resourceLocation);
            this.checkBox.setCurrentValue(CheckBox.EnumCheckBoxValue.ENABLED);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(3, GuiShaderSelection.width / 2 - 100 + 30, GuiShaderSelection.height - 26, 170, 20, LanguageManager.translate("button_done")));
        this.scrollbar.setPosition(GuiShaderSelection.width / 2 + 100 - 5, 48, GuiShaderSelection.width / 2 + 100, GuiShaderSelection.height - 45);
        this.checkBox.setX(GuiShaderSelection.width / 2 - 100);
        this.checkBox.setY(GuiShaderSelection.height - 26);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawAutoDimmedBackground(this.scrollbar.getScrollY());
        this.hoveredShader = -2;
        if (LabyMod.getInstance().isInGame() && this.shaderResourceLocations != null) {
            double y = 48.0 + this.scrollbar.getScrollY();
            int index = -1;
            this.drawEntry("None " + ModColor.cl('e') + "(Default)", index, y, mouseX, mouseY);
            y += 12.0;
            ++index;
            ResourceLocation[] shaderResourceLocations;
            for (int length = (shaderResourceLocations = this.shaderResourceLocations).length, i = 0; i < length; ++i) {
                final ResourceLocation resourceLocation = shaderResourceLocations[i];
                String name = resourceLocation.getResourcePath();
                if (name.contains("/")) {
                    final String[] path = name.split("/");
                    name = path[path.length - 1];
                    name = name.split("\\.")[0];
                    name = String.valueOf(Character.toUpperCase(name.charAt(0))) + name.substring(1);
                }
                this.drawEntry(name, index, y, mouseX, mouseY);
                y += 12.0;
                ++index;
            }
            this.scrollbar.update(this.shaderResourceLocations.length + 1);
            this.scrollbar.draw();
        }
        else {
            draw.drawCenteredString(String.valueOf(ModColor.cl('c')) + LanguageManager.translate("shader_selection_not_ingame"), GuiShaderSelection.width / 2, GuiShaderSelection.height / 2);
        }
        draw.drawOverlayBackground(0, 41);
        draw.drawOverlayBackground(GuiShaderSelection.height - 38, GuiShaderSelection.height);
        draw.drawGradientShadowTop(41.0, 0.0, GuiShaderSelection.width);
        draw.drawGradientShadowBottom(GuiShaderSelection.height - 38, 0.0, GuiShaderSelection.width);
        draw.drawCenteredString(LanguageManager.translate("title_shaders"), GuiShaderSelection.width / 2, 29.0);
        this.checkBox.drawCheckbox(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private void drawEntry(final String name, final int index, final double y, final int mouseX, final int mouseY) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        if (GuiShaderSelection.shaderIndex == index) {
            DrawUtils.drawRect(GuiShaderSelection.width / 2 - 102, y - 2.0, GuiShaderSelection.width / 2 + 100 - 6, y + 10.0, Integer.MIN_VALUE);
            draw.drawRectBorder(GuiShaderSelection.width / 2 - 102, y - 2.0, GuiShaderSelection.width / 2 + 100 - 6, y + 10.0, Integer.MAX_VALUE, 1.0);
            if (index == -1) {
                GlStateManager.color(1.0f, 0.5f, 0.5f, 1.0f);
            }
            else {
                GlStateManager.color(0.5f, 1.0f, 0.5f, 1.0f);
            }
        }
        else {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_MENU_POINT);
        draw.drawTexture(GuiShaderSelection.width / 2 - 100, y, 255.0, 255.0, 8.0, 8.0, 1.1f);
        draw.drawString(name, GuiShaderSelection.width / 2 - 100 + 10, y);
        if (mouseX > GuiShaderSelection.width / 2 - 100 && mouseX < GuiShaderSelection.width / 2 + 100 && mouseY > y && mouseY < y + 12.0 && mouseY > 41 && mouseY < GuiShaderSelection.height - 38) {
            this.hoveredShader = index;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 3) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }
        super.actionPerformed(button);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hoveredShader != -2 && this.hoveredShader != GuiShaderSelection.shaderIndex) {
            this.loadShader(this.hoveredShader);
        }
        this.checkBox.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
}
