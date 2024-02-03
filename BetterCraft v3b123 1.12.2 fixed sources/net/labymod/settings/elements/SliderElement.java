// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import net.labymod.utils.DrawUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.labymod.utils.ModColor;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.main.ModSettings;
import net.labymod.utils.Consumer;
import net.minecraft.util.ResourceLocation;

public class SliderElement extends ControlElement
{
    public static final ResourceLocation buttonTextures;
    private Integer currentValue;
    private Consumer<Integer> changeListener;
    private Consumer<Integer> callback;
    private int minValue;
    private int maxValue;
    private boolean dragging;
    private boolean hover;
    private int dragValue;
    private int steps;
    
    static {
        buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    }
    
    public SliderElement(final String displayName, final String configEntryName, final IconData iconData) {
        super(displayName, configEntryName, iconData);
        this.minValue = 0;
        this.maxValue = 10;
        this.steps = 1;
        if (!configEntryName.isEmpty()) {
            try {
                this.currentValue = (Integer)ModSettings.class.getDeclaredField(configEntryName).get(LabyMod.getSettings());
            }
            catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (final NoSuchFieldException e2) {
                e2.printStackTrace();
            }
        }
        if (this.currentValue == null) {
            this.currentValue = this.minValue;
        }
        this.changeListener = new Consumer<Integer>() {
            @Override
            public void accept(final Integer accepted) {
                try {
                    ModSettings.class.getDeclaredField(configEntryName).set(LabyMod.getSettings(), accepted);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                if (SliderElement.this.callback != null) {
                    SliderElement.this.callback.accept(accepted);
                }
            }
        };
    }
    
    public SliderElement(final String displayName, final IconData iconData, final int currentValue) {
        super(displayName, null, iconData);
        this.minValue = 0;
        this.maxValue = 10;
        this.steps = 1;
        this.currentValue = currentValue;
        this.changeListener = new Consumer<Integer>() {
            @Override
            public void accept(final Integer accepted) {
                if (SliderElement.this.callback != null) {
                    SliderElement.this.callback.accept(accepted);
                }
            }
        };
    }
    
    public SliderElement(final String displayName, final LabyModAddon addon, final IconData iconData, final String attribute, final int currentValue) {
        super(displayName, iconData);
        this.minValue = 0;
        this.maxValue = 10;
        this.steps = 1;
        this.currentValue = currentValue;
        this.changeListener = new Consumer<Integer>() {
            @Override
            public void accept(final Integer accepted) {
                addon.getConfig().addProperty(attribute, accepted);
                addon.loadConfig();
                if (SliderElement.this.callback != null) {
                    SliderElement.this.callback.accept(accepted);
                }
            }
        };
    }
    
    public SliderElement(final String configEntryName, final IconData iconData) {
        this(configEntryName, configEntryName, iconData);
    }
    
    public SliderElement setMinValue(final int minValue) {
        this.minValue = minValue;
        if (this.currentValue < this.minValue) {
            this.currentValue = this.minValue;
        }
        return this;
    }
    
    public SliderElement setMaxValue(final int maxValue) {
        this.maxValue = maxValue;
        if (this.currentValue > this.maxValue) {
            this.currentValue = this.maxValue;
        }
        return this;
    }
    
    public SliderElement setRange(final int min, final int max) {
        this.setMinValue(min);
        this.setMaxValue(max);
        return this;
    }
    
    public SliderElement setSteps(final int steps) {
        this.steps = steps;
        return this;
    }
    
    @Override
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        if (this.displayName != null) {
            draw.drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(SliderElement.buttonTextures);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        final double maxSliderPos = maxX;
        final double totalValueDiff = this.maxValue - this.minValue;
        final double currentValue = this.currentValue;
        this.hover = (mouseX > x && mouseX < maxX && mouseY > y + 1 && mouseY < maxY);
        if (!this.isMouseOver()) {
            this.mouseRelease(mouseX, mouseY, 0);
        }
        else if (this.dragging) {
            this.mouseClickMove(mouseX, mouseY, 0);
        }
    }
    
    @Override
    public void unfocus(final int mouseX, final int mouseY, final int mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hover) {
            this.dragging = true;
        }
    }
    
    @Override
    public void mouseRelease(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseRelease(mouseX, mouseY, mouseButton);
        if (this.dragging) {
            this.dragging = false;
            this.currentValue = (int)(this.dragValue / (double)this.steps) * this.steps;
            if (this.currentValue > this.maxValue) {
                this.currentValue = this.maxValue;
            }
            if (this.currentValue < this.minValue) {
                this.currentValue = this.minValue;
            }
            this.changeListener.accept(this.currentValue);
        }
    }
    
    @Override
    public void mouseClickMove(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClickMove(mouseX, mouseY, mouseButton);
        if (this.dragging) {
            this.currentValue = (int)Math.round(this.dragValue / (double)this.steps * this.steps);
            if (this.currentValue > this.maxValue) {
                this.currentValue = this.maxValue;
            }
            if (this.currentValue < this.minValue) {
                this.currentValue = this.minValue;
            }
            this.changeListener.accept(this.currentValue);
        }
    }
    
    public SliderElement addCallback(final Consumer<Integer> callback) {
        this.callback = callback;
        return this;
    }
    
    public void setCurrentValue(final Integer currentValue) {
        this.currentValue = currentValue;
    }
}
