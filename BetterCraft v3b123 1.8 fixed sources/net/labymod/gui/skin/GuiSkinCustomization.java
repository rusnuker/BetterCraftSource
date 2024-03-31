// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.skin;

import net.labymod.support.util.Debug;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import net.labymod.splash.SplashLoader;
import net.labymod.user.sticker.StickerLoader;
import net.labymod.user.emote.EmoteLoader;
import net.labymod.utils.Consumer;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.labymod.core.LabyModCore;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.labymod.main.LabyMod;
import net.labymod.main.Source;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class GuiSkinCustomization extends GuiScreen
{
    private GuiScreen lastScreen;
    private long screenCalled;
    private boolean enabledPreviewDragging;
    private boolean currentDragging;
    private boolean currentMoving;
    private boolean mouseOverPreview;
    private double mouseClickedX;
    private double mouseClickedY;
    private double dragPreviewX;
    private double dragPreviewY;
    private double clickedYaw;
    private double xRotationGoal;
    private double yRotationGoal;
    private long dragStartCalled;
    private long lastGoalTracking;
    private double startMoveClickX;
    private double startMoveClickY;
    private double moveX;
    private double moveY;
    private double zoomValue;
    private List<SkinCustomizationSettingElement> settingElements;
    
    public GuiSkinCustomization(final GuiScreen lastScreen) {
        this.screenCalled = 0L;
        this.enabledPreviewDragging = false;
        this.currentDragging = false;
        this.currentMoving = false;
        this.mouseOverPreview = false;
        this.dragPreviewX = 0.0;
        this.dragPreviewY = 0.0;
        this.clickedYaw = 0.0;
        this.xRotationGoal = 0.0;
        this.yRotationGoal = 0.0;
        this.dragStartCalled = 0L;
        this.lastGoalTracking = 0L;
        this.startMoveClickX = 0.0;
        this.startMoveClickY = 0.0;
        this.moveX = 0.0;
        this.moveY = 0.0;
        this.zoomValue = 50.0;
        this.settingElements = new ArrayList<SkinCustomizationSettingElement>();
        this.lastScreen = lastScreen;
        this.screenCalled = System.currentTimeMillis();
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.settingElements.clear();
        this.settingElements.add(new SkinLayerSettingElement(this, null, "hat", new EnumPlayerModelParts[] { EnumPlayerModelParts.HAT }));
        final SkinLayerSettingElement jacketElement = new SkinLayerSettingElement(this, LanguageManager.translate("skinpart_jacket"), "jacket", new EnumPlayerModelParts[] { EnumPlayerModelParts.JACKET, EnumPlayerModelParts.RIGHT_SLEEVE, EnumPlayerModelParts.LEFT_SLEEVE });
        jacketElement.addSubSetting(new SkinLayerSettingElement(this, null, "right_sleeve", new EnumPlayerModelParts[] { EnumPlayerModelParts.RIGHT_SLEEVE }));
        jacketElement.addSubSetting(new SkinLayerSettingElement(this, null, "jacket_base", new EnumPlayerModelParts[] { EnumPlayerModelParts.JACKET }));
        jacketElement.addSubSetting(new SkinLayerSettingElement(this, null, "left_sleeve", new EnumPlayerModelParts[] { EnumPlayerModelParts.LEFT_SLEEVE }));
        this.settingElements.add(jacketElement);
        final SkinLayerSettingElement pantsElement = new SkinLayerSettingElement(this, LanguageManager.translate("skinpart_pants"), "pants", new EnumPlayerModelParts[] { EnumPlayerModelParts.RIGHT_PANTS_LEG, EnumPlayerModelParts.LEFT_PANTS_LEG });
        pantsElement.addSubSetting(new SkinLayerSettingElement(this, null, "right_pants", new EnumPlayerModelParts[] { EnumPlayerModelParts.RIGHT_PANTS_LEG }));
        pantsElement.addSubSetting(new SkinLayerSettingElement(this, null, "left_pants", new EnumPlayerModelParts[] { EnumPlayerModelParts.LEFT_PANTS_LEG }));
        this.settingElements.add(pantsElement);
        this.settingElements.add(new SkinLayerSettingElement(this, null, "cape", new EnumPlayerModelParts[] { EnumPlayerModelParts.CAPE }));
        if (!Source.ABOUT_MC_VERSION.startsWith("1.8")) {
            this.settingElements.add(new SkinHandSettingElement(LanguageManager.translate("skinpart_hand"), "hand"));
        }
        int listElementWidth = GuiSkinCustomization.width / 3;
        if (listElementWidth < 130) {
            listElementWidth = 130;
        }
        if (listElementWidth > 200) {
            listElementWidth = 200;
        }
        this.buttonList.add(new GuiButton(5, (LabyMod.getInstance().isInGame() ? (listElementWidth / 2) : (GuiSkinCustomization.width / 2 - 10)) - listElementWidth / 2 + 10, GuiSkinCustomization.height - 30, listElementWidth, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(6, GuiSkinCustomization.width - 100, 4, 97, 20, LanguageManager.translate("button_refresh_labymod")));
        if (this.getOptifineCapeScreen() != null) {
            this.buttonList.add(new GuiButton(7, GuiSkinCustomization.width - 100, 26, 97, 20, LanguageManager.translate("optifine_cape")));
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Minecraft.getMinecraft().gameSettings.saveOptions();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        double elementWidth = GuiSkinCustomization.width / 3;
        double elementHeight = GuiSkinCustomization.height / 70.0 * this.settingElements.size();
        if (elementWidth < 130.0) {
            elementWidth = 130.0;
        }
        if (elementHeight < 17.0) {
            elementHeight = 17.0;
        }
        if (elementWidth > 200.0) {
            elementWidth = 200.0;
        }
        if (LabyMod.getInstance().isInGame()) {
            draw.drawOverlayBackground(0, 0, (int)elementWidth + 20, GuiSkinCustomization.height, 32);
            draw.drawGradientShadowRight(elementWidth + 20.0, 0.0, GuiSkinCustomization.height);
        }
        int totalHeight = 0;
        for (final SkinCustomizationSettingElement element : this.settingElements) {
            totalHeight += (int)elementHeight;
            for (final SkinLayerSettingElement subElement : element.getSubSettingElements()) {
                totalHeight += (int)elementHeight;
            }
            totalHeight += 5;
        }
        final int posX = (int)(LabyMod.getInstance().isInGame() ? 10.0 : (GuiSkinCustomization.width / 2 - elementWidth / 2.0));
        int posY = (GuiSkinCustomization.height - totalHeight) / 2 - 10;
        for (final SkinCustomizationSettingElement element2 : this.settingElements) {
            element2.draw(false, posX, posY, elementWidth, elementHeight, mouseX, mouseY);
            posY += (int)elementHeight;
            posY += 2;
            int subIndex = 1;
            for (final SkinLayerSettingElement subElement2 : element2.getSubSettingElements()) {
                final int lineColor = ModColor.toRGB(150, 150, 150, 155);
                DrawUtils.drawRect(posX + 20 - 10, posY, posX + 20 - 8, posY + elementHeight / 2.0, lineColor);
                DrawUtils.drawRect(posX + 20 - 10, posY + elementHeight / 2.0, posX + 20, posY + elementHeight / 2.0 + 2.0, lineColor);
                if (element2.getSubSettingElements().size() != subIndex) {
                    DrawUtils.drawRect(posX + 20 - 10, posY + elementHeight / 2.0 + 2.0, posX + 20 - 8, posY + elementHeight + 2.0, lineColor);
                }
                subElement2.draw(true, posX + 20, posY, elementWidth - 50.0, elementHeight, mouseX, mouseY);
                posY += (int)(elementHeight + 2.0);
                ++subIndex;
            }
            posY += 2;
        }
        draw.drawCenteredString(I18n.format("options.skinCustomisation.title", new Object[0]), LabyMod.getInstance().isInGame() ? (10.0 + elementWidth / 2.0) : ((double)(GuiSkinCustomization.width / 2)), posY - totalHeight - 17);
        final double rightSideMiddle = elementWidth + 20.0 + (GuiSkinCustomization.width - elementWidth + 20.0) / 2.0;
        if (LabyModCore.getMinecraft().getPlayer() != null) {
            final int entityPosX = (int)rightSideMiddle - 20;
            int entityPosY = GuiSkinCustomization.height / 2 + 80;
            double rotationIntroAnimation = (double)(this.screenCalled + 200L - System.currentTimeMillis());
            if (rotationIntroAnimation <= 0.0) {
                rotationIntroAnimation = 0.0;
            }
            else {
                rotationIntroAnimation = rotationIntroAnimation / 50.0 * rotationIntroAnimation / 50.0;
            }
            double rotationIntroAnimationPointer = (this.screenCalled + 500L - System.currentTimeMillis() + 200L) / 200.0;
            if (this.enabledPreviewDragging) {
                rotationIntroAnimationPointer = (System.currentTimeMillis() - this.dragStartCalled) / 200.0;
            }
            if (rotationIntroAnimationPointer <= 1.0) {
                rotationIntroAnimationPointer = 1.0;
            }
            rotationIntroAnimationPointer *= rotationIntroAnimationPointer;
            double mousePointX = -mouseX + entityPosX;
            double mousePointY = -mouseY + entityPosY - 130;
            if (rotationIntroAnimationPointer > 0.0) {
                mousePointX /= rotationIntroAnimationPointer;
            }
            if (rotationIntroAnimationPointer > 0.0) {
                mousePointY /= rotationIntroAnimationPointer;
            }
            double dragRotationX = 0.0;
            double dragRotationY = 0.0;
            if (this.enabledPreviewDragging) {
                dragRotationX = -this.dragPreviewX;
                dragRotationY = -this.dragPreviewY;
            }
            if (dragRotationY == 0.0) {
                ++dragRotationY;
            }
            entityPosY -= 80;
            DrawUtils.drawEntityOnScreen((int)(entityPosX + this.moveX), (int)(entityPosY + this.moveY), (int)this.zoomValue, (float)(int)mousePointX, (float)(int)mousePointY, (int)(dragRotationX + rotationIntroAnimation), (int)dragRotationY, 0, LabyModCore.getMinecraft().getPlayer());
        }
        this.mouseOverPreview = (mouseX > rightSideMiddle + this.moveX - 60.0 && mouseX < rightSideMiddle + this.moveX + 60.0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.currentMoving || this.currentDragging) {
            this.mouseClickMove(mouseX, mouseY, 0, 0L);
        }
        if ((this.xRotationGoal != 0.0 || this.yRotationGoal != 0.0) && this.lastGoalTracking < System.currentTimeMillis()) {
            this.lastGoalTracking = System.currentTimeMillis() + 15L;
            final int f = 10;
            if (this.xRotationGoal > this.dragPreviewX + 10.0) {
                this.dragPreviewX += 10.0;
            }
            if (this.xRotationGoal < this.dragPreviewX - 10.0) {
                this.dragPreviewX -= 10.0;
            }
            if (this.yRotationGoal > this.dragPreviewY + 10.0) {
                this.dragPreviewY += 10.0;
            }
            if (this.yRotationGoal < this.dragPreviewY - 10.0) {
                this.dragPreviewY -= 10.0;
            }
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 5: {
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
            }
            case 6: {
                button.enabled = false;
                LabyMod.getInstance().getUserManager().getFamiliarManager().clear();
                LabyMod.getInstance().getUserManager().getFamiliarManager().refresh();
                LabyMod.getInstance().getUserManager().clearCache();
                LabyMod.getInstance().getUserManager().init(LabyMod.getInstance().getPlayerUUID(), new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean success) {
                        LabyMod.getInstance().getUserManager().getCosmeticImageManager().loadPlayersInView();
                        button.enabled = true;
                    }
                });
                LabyMod.getInstance().getUserManager().getGroupManager().load();
                new EmoteLoader(LabyMod.getInstance().getEmoteRegistry()).start();
                new StickerLoader(LabyMod.getInstance().getStickerRegistry()).start();
                SplashLoader.getLoader().load();
                break;
            }
            case 7: {
                final GuiScreen screen = this.getOptifineCapeScreen();
                if (screen != null) {
                    Minecraft.getMinecraft().displayGuiScreen(screen);
                    break;
                }
                break;
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.enabledPreviewDragging && this.mouseOverPreview && mouseButton == 0) {
            this.enabledPreviewDragging = true;
            this.dragStartCalled = System.currentTimeMillis();
        }
        if (this.enabledPreviewDragging && this.mouseOverPreview && mouseButton == 0) {
            this.currentDragging = true;
            this.mouseClickedX = mouseX + this.dragPreviewX;
            this.mouseClickedY = ((this.clickedYaw > 180.0) ? (-mouseY) : mouseY) + this.dragPreviewY;
            this.clickedYaw = (this.dragPreviewX + 90.0) % 360.0;
            this.xRotationGoal = 0.0;
            this.yRotationGoal = 0.0;
        }
        if (mouseButton == 2) {
            this.startMoveClickX = -this.moveX + mouseX;
            this.startMoveClickY = -this.moveY + mouseY;
            this.currentMoving = true;
        }
        for (final SkinCustomizationSettingElement element : this.settingElements) {
            element.getCheckBox().mouseClicked(mouseX, mouseY, mouseButton);
            for (final SkinLayerSettingElement subElement : element.getSubSettingElements()) {
                subElement.getCheckBox().mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            this.currentDragging = false;
            this.clickedYaw = (this.dragPreviewX + 90.0) % 360.0;
        }
        if (state == 2) {
            this.currentMoving = false;
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        final int mouseScroll = Mouse.getDWheel();
        if (mouseScroll > 0) {
            this.zoomValue += 10.0;
        }
        if (mouseScroll < 0) {
            this.zoomValue -= 10.0;
        }
        if (!Debug.isActive()) {
            if (this.zoomValue < 50.0) {
                this.zoomValue = 50.0;
            }
            final int maxZoom = 150;
            if (this.zoomValue > 150.0) {
                this.zoomValue = 150.0;
            }
        }
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (this.currentDragging) {
            this.dragPreviewX = (-mouseX + this.mouseClickedX) % 360.0;
            this.dragPreviewY = ((this.clickedYaw > 180.0) ? mouseY : (-mouseY)) + this.mouseClickedY;
            if (!Debug.isActive()) {
                if (this.dragPreviewY > 45.0) {
                    this.dragPreviewY = 45.0;
                }
                if (this.dragPreviewY < -45.0) {
                    this.dragPreviewY = -45.0;
                }
            }
        }
        if (this.currentMoving) {
            this.moveX = -this.startMoveClickX + mouseX;
            this.moveY = -this.startMoveClickY + mouseY;
            if (!Debug.isActive()) {
                if (this.moveX < -150.0) {
                    this.moveX = -150.0;
                }
                if (this.moveX > 150.0) {
                    this.moveX = 150.0;
                }
                if (this.moveY < -150.0) {
                    this.moveY = -150.0;
                }
                if (this.moveY > 150.0) {
                    this.moveY = 150.0;
                }
            }
        }
    }
    
    public void updatePart(final EnumPlayerModelParts part, final boolean value) {
        Minecraft.getMinecraft().gameSettings.setModelPartEnabled(part, value);
        if (this.enabledPreviewDragging) {
            if (part == EnumPlayerModelParts.CAPE) {
                this.xRotationGoal = 180.0;
                this.yRotationGoal = 0.0;
            }
            else if (part == EnumPlayerModelParts.HAT) {
                this.xRotationGoal = 20.0;
                this.yRotationGoal = -20.0;
            }
            else if (part == EnumPlayerModelParts.RIGHT_PANTS_LEG || part == EnumPlayerModelParts.LEFT_PANTS_LEG) {
                this.xRotationGoal = 20.0;
                this.yRotationGoal = 10.0;
            }
            else {
                this.xRotationGoal = 20.0;
                this.yRotationGoal = -5.0;
            }
        }
    }
    
    private GuiScreen getOptifineCapeScreen() {
        try {
            final Class<?> optifineClass = Class.forName("net.optifine.gui.GuiScreenCapeOF");
            if (optifineClass != null) {
                return (GuiScreen)optifineClass.getConstructor(GuiScreen.class).newInstance(this);
            }
        }
        catch (final Exception ex) {}
        return null;
    }
}
