// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.group;

import java.beans.ConstructorProperties;
import net.minecraft.util.ResourceLocation;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.labymod.main.ModTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.utils.ModColor;
import java.awt.Color;
import com.google.gson.annotations.SerializedName;

public class LabyGroup
{
    private int id;
    private String name;
    @SerializedName("nice_name")
    private String displayName;
    @SerializedName("color_hex")
    private String colorHex;
    @SerializedName("color_minecraft")
    private char colorMinecraft;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("display_type")
    private String displayTypeString;
    private Color color;
    private EnumGroupDisplayType displayType;
    
    protected LabyGroup init() {
        try {
            final EnumGroupDisplayType type = EnumGroupDisplayType.valueOf(this.displayTypeString);
            this.displayType = ((type == null) ? EnumGroupDisplayType.NONE : type);
        }
        catch (final Exception error) {
            error.printStackTrace();
            this.displayType = EnumGroupDisplayType.NONE;
        }
        try {
            if (this.colorHex != null && !this.colorHex.isEmpty()) {
                this.color = Color.decode("#" + this.colorHex);
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
        return this;
    }
    
    public String getDisplayTag() {
        return String.valueOf(ModColor.cl("f")) + ModColor.cl("l") + "LABYMOD " + '§' + this.colorMinecraft + this.tagName;
    }
    
    public void renderBadge(final double x, final double y, final double width, final double height, final boolean small) {
        final boolean familiar = this.color == null;
        if (!familiar) {
            GlStateManager.color(this.color.getRed() / 255.0f, this.color.getGreen() / 255.0f, this.color.getBlue() / 255.0f);
        }
        final ResourceLocation texture = familiar ? (small ? ModTextures.BADGE_FAMILIAR_SMALL : ModTextures.BADGE_FAMILIAR) : (small ? ModTextures.BADGE_GROUP_SMALL : ModTextures.BADGE_GROUP);
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        LabyMod.getInstance().getDrawUtils().drawTexture(x, y, 255.0, 255.0, 8.0, 8.0, 1.1f);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }
    
    public LabyGroup() {
    }
    
    @ConstructorProperties({ "id", "name", "displayName", "colorHex", "colorMinecraft", "tagName", "displayTypeString", "color", "displayType" })
    public LabyGroup(final int id, final String name, final String displayName, final String colorHex, final char colorMinecraft, final String tagName, final String displayTypeString, final Color color, final EnumGroupDisplayType displayType) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.colorHex = colorHex;
        this.colorMinecraft = colorMinecraft;
        this.tagName = tagName;
        this.displayTypeString = displayTypeString;
        this.color = color;
        this.displayType = displayType;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public char getColorMinecraft() {
        return this.colorMinecraft;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public EnumGroupDisplayType getDisplayType() {
        return this.displayType;
    }
}
