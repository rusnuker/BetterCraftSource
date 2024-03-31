// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import java.util.Iterator;
import java.util.ArrayList;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.types.minecraft.MetaListTypeTemplate;

public class MetadataListType extends MetaListTypeTemplate
{
    private MetadataType metadataType;
    
    public MetadataListType() {
        this.metadataType = new MetadataType();
    }
    
    @Override
    public List<Metadata> read(final ByteBuf buffer) throws Exception {
        final ArrayList<Metadata> list = new ArrayList<Metadata>();
        Metadata m;
        do {
            m = Types1_7_6_10.METADATA.read(buffer);
            if (m != null) {
                list.add(m);
            }
        } while (m != null);
        if (this.find(2, "Slot", list) != null && this.find(8, "Slot", list) != null) {
            list.removeIf(metadata -> metadata.id() == 2 || metadata.id() == 3);
        }
        return list;
    }
    
    private Metadata find(final int id, final String type, final List<Metadata> list) {
        for (final Metadata metadata : list) {
            if (metadata.id() == id && metadata.metaType().toString().equals(type)) {
                return metadata;
            }
        }
        return null;
    }
    
    @Override
    public void write(final ByteBuf buffer, final List<Metadata> metadata) throws Exception {
        for (final Metadata meta : metadata) {
            Types1_7_6_10.METADATA.write(buffer, meta);
        }
        if (metadata.isEmpty()) {
            Types1_7_6_10.METADATA.write(buffer, new Metadata(0, MetaType1_7_6_10.Byte, 0));
        }
        buffer.writeByte(127);
    }
}
