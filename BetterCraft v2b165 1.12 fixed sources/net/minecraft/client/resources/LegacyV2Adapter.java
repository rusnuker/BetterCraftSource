// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import java.util.Set;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;

public class LegacyV2Adapter implements IResourcePack
{
    private final IResourcePack field_191383_a;
    
    public LegacyV2Adapter(final IResourcePack p_i47182_1_) {
        this.field_191383_a = p_i47182_1_;
    }
    
    @Override
    public InputStream getInputStream(final ResourceLocation location) throws IOException {
        return this.field_191383_a.getInputStream(this.func_191382_c(location));
    }
    
    private ResourceLocation func_191382_c(final ResourceLocation p_191382_1_) {
        final String s = p_191382_1_.getResourcePath();
        if (!"lang/swg_de.lang".equals(s) && s.startsWith("lang/") && s.endsWith(".lang")) {
            final int i = s.indexOf(95);
            if (i != -1) {
                final String s2 = String.valueOf(s.substring(0, i + 1)) + s.substring(i + 1, s.indexOf(46, i)).toUpperCase() + ".lang";
                return new ResourceLocation(p_191382_1_.getResourceDomain(), "") {
                    @Override
                    public String getResourcePath() {
                        return s2;
                    }
                };
            }
        }
        return p_191382_1_;
    }
    
    @Override
    public boolean resourceExists(final ResourceLocation location) {
        return this.field_191383_a.resourceExists(this.func_191382_c(location));
    }
    
    @Override
    public Set<String> getResourceDomains() {
        return this.field_191383_a.getResourceDomains();
    }
    
    @Nullable
    @Override
    public <T extends IMetadataSection> T getPackMetadata(final MetadataSerializer metadataSerializer, final String metadataSectionName) throws IOException {
        return this.field_191383_a.getPackMetadata(metadataSerializer, metadataSectionName);
    }
    
    @Override
    public BufferedImage getPackImage() throws IOException {
        return this.field_191383_a.getPackImage();
    }
    
    @Override
    public String getPackName() {
        return this.field_191383_a.getPackName();
    }
}
