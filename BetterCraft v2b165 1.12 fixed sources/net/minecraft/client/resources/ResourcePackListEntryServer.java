// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.resources;

import net.minecraft.util.text.TextFormatting;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.data.PackMetadataSection;
import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class ResourcePackListEntryServer extends ResourcePackListEntry
{
    private static final Logger LOGGER;
    private final IResourcePack resourcePack;
    private final ResourceLocation resourcePackIcon;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public ResourcePackListEntryServer(final GuiScreenResourcePacks p_i46594_1_, final IResourcePack p_i46594_2_) {
        super(p_i46594_1_);
        this.resourcePack = p_i46594_2_;
        DynamicTexture dynamictexture;
        try {
            dynamictexture = new DynamicTexture(p_i46594_2_.getPackImage());
        }
        catch (final IOException var5) {
            dynamictexture = TextureUtil.MISSING_TEXTURE;
        }
        this.resourcePackIcon = this.mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamictexture);
    }
    
    @Override
    protected int getResourcePackFormat() {
        return 3;
    }
    
    @Override
    protected String getResourcePackDescription() {
        try {
            final PackMetadataSection packmetadatasection = this.resourcePack.getPackMetadata(this.mc.getResourcePackRepository().rprMetadataSerializer, "pack");
            if (packmetadatasection != null) {
                return packmetadatasection.getPackDescription().getFormattedText();
            }
        }
        catch (final JsonParseException jsonparseexception) {
            ResourcePackListEntryServer.LOGGER.error("Couldn't load metadata info", jsonparseexception);
        }
        catch (final IOException ioexception) {
            ResourcePackListEntryServer.LOGGER.error("Couldn't load metadata info", ioexception);
        }
        return TextFormatting.RED + "Missing " + "pack.mcmeta" + " :(";
    }
    
    @Override
    protected boolean canMoveRight() {
        return false;
    }
    
    @Override
    protected boolean canMoveLeft() {
        return false;
    }
    
    @Override
    protected boolean canMoveUp() {
        return false;
    }
    
    @Override
    protected boolean canMoveDown() {
        return false;
    }
    
    @Override
    protected String getResourcePackName() {
        return "Server";
    }
    
    @Override
    protected void bindResourcePackIcon() {
        this.mc.getTextureManager().bindTexture(this.resourcePackIcon);
    }
    
    @Override
    protected boolean showHoverOverlay() {
        return false;
    }
    
    @Override
    public boolean isServerPack() {
        return true;
    }
}
