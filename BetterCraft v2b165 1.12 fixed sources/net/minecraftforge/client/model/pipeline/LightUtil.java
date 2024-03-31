// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraftforge.client.model.pipeline;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import java.util.function.Supplier;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.apache.commons.lang3.tuple.Pair;
import java.util.concurrent.ConcurrentMap;

public class LightUtil
{
    private static final ConcurrentMap<Pair<VertexFormat, VertexFormat>, int[]> formatMaps;
    private static final VertexFormat DEFAULT_FROM;
    private static final VertexFormat DEFAULT_TO;
    private static final int[] DEFAULT_MAPPING;
    private static IVertexConsumer tessellator;
    private static ItemConsumer itemConsumer;
    private static final ThreadLocal<ItemPipeline> itemPipeline;
    
    static {
        formatMaps = new ConcurrentHashMap<Pair<VertexFormat, VertexFormat>, int[]>();
        DEFAULT_FROM = VertexLighterFlat.withNormal(DefaultVertexFormats.BLOCK);
        DEFAULT_TO = DefaultVertexFormats.ITEM;
        DEFAULT_MAPPING = generateMapping(LightUtil.DEFAULT_FROM, LightUtil.DEFAULT_TO);
        LightUtil.tessellator = null;
        LightUtil.itemConsumer = null;
        itemPipeline = ThreadLocal.withInitial((Supplier<? extends ItemPipeline>)ItemPipeline::new);
    }
    
    public static float diffuseLight(final float x, final float y, final float z) {
        return Math.min(x * x * 0.6f + y * y * ((3.0f + y) / 4.0f) + z * z * 0.8f, 1.0f);
    }
    
    public static float diffuseLight(final EnumFacing side) {
        switch (side) {
            case DOWN: {
                return 0.5f;
            }
            case UP: {
                return 1.0f;
            }
            case NORTH:
            case SOUTH: {
                return 0.8f;
            }
            default: {
                return 0.6f;
            }
        }
    }
    
    public static EnumFacing toSide(final float x, final float y, final float z) {
        if (Math.abs(x) > Math.abs(y)) {
            if (Math.abs(x) > Math.abs(z)) {
                if (x < 0.0f) {
                    return EnumFacing.WEST;
                }
                return EnumFacing.EAST;
            }
            else {
                if (z < 0.0f) {
                    return EnumFacing.NORTH;
                }
                return EnumFacing.SOUTH;
            }
        }
        else if (Math.abs(y) > Math.abs(z)) {
            if (y < 0.0f) {
                return EnumFacing.DOWN;
            }
            return EnumFacing.UP;
        }
        else {
            if (z < 0.0f) {
                return EnumFacing.NORTH;
            }
            return EnumFacing.SOUTH;
        }
    }
    
    public static void putBakedQuad(final IVertexConsumer consumer, final BakedQuad quad) {
        consumer.setQuadOrientation(quad.getFace());
        if (quad.hasTintIndex()) {
            consumer.setQuadTint(quad.getTintIndex());
        }
        final float[] data = new float[4];
        final VertexFormat formatFrom = consumer.getVertexFormat();
        final VertexFormat formatTo = quad.getFormat();
        final int countFrom = formatFrom.getElementCount();
        final int countTo = formatTo.getElementCount();
        final int[] eMap = mapFormats(formatFrom, formatTo);
        for (int v = 0; v < 4; ++v) {
            for (int e = 0; e < countFrom; ++e) {
                if (eMap[e] != countTo) {
                    unpack(quad.getVertexData(), data, formatTo, v, eMap[e]);
                    consumer.put(e, data);
                }
                else {
                    consumer.put(e, new float[0]);
                }
            }
        }
    }
    
    public static int[] mapFormats(final VertexFormat from, final VertexFormat to) {
        if (from.equals(LightUtil.DEFAULT_FROM) && to.equals(LightUtil.DEFAULT_TO)) {
            return LightUtil.DEFAULT_MAPPING;
        }
        return LightUtil.formatMaps.computeIfAbsent(Pair.of(from, to), pair -> generateMapping(pair.getLeft(), pair.getRight()));
    }
    
    private static int[] generateMapping(final VertexFormat from, final VertexFormat to) {
        final int fromCount = from.getElementCount();
        final int toCount = to.getElementCount();
        final int[] eMap = new int[fromCount];
        for (int e = 0; e < fromCount; ++e) {
            final VertexFormatElement expected = from.getElement(e);
            int e2;
            for (e2 = 0; e2 < toCount; ++e2) {
                final VertexFormatElement current = to.getElement(e2);
                if (expected.getUsage() == current.getUsage() && expected.getIndex() == current.getIndex()) {
                    break;
                }
            }
            eMap[e] = e2;
        }
        return eMap;
    }
    
    public static void unpack(final int[] from, final float[] to, final VertexFormat formatFrom, final int v, final int e) {
        final int length = (4 < to.length) ? 4 : to.length;
        final VertexFormatElement element = formatFrom.getElement(e);
        final int vertexStart = v * formatFrom.getNextOffset() + formatFrom.getOffset(e);
        final int count = element.getElementCount();
        final VertexFormatElement.EnumType type = element.getType();
        final int size = type.getSize();
        final int mask = (256 << 8 * (size - 1)) - 1;
        for (int i = 0; i < length; ++i) {
            if (i < count) {
                final int pos = vertexStart + size * i;
                final int index = pos >> 2;
                final int offset = pos & 0x3;
                int bits = from[index];
                bits >>>= offset * 8;
                if ((pos + size - 1) / 4 != index) {
                    bits |= from[index + 1] << (4 - offset) * 8;
                }
                bits &= mask;
                if (type == VertexFormatElement.EnumType.FLOAT) {
                    to[i] = Float.intBitsToFloat(bits);
                }
                else if (type == VertexFormatElement.EnumType.UBYTE || type == VertexFormatElement.EnumType.USHORT) {
                    to[i] = bits / (float)mask;
                }
                else if (type == VertexFormatElement.EnumType.UINT) {
                    to[i] = (float)(((long)bits & 0xFFFFFFFFL) / 4.294967295E9);
                }
                else if (type == VertexFormatElement.EnumType.BYTE) {
                    to[i] = (byte)bits / (float)(mask >> 1);
                }
                else if (type == VertexFormatElement.EnumType.SHORT) {
                    to[i] = (short)bits / (float)(mask >> 1);
                }
                else if (type == VertexFormatElement.EnumType.INT) {
                    to[i] = (float)(((long)bits & 0xFFFFFFFFL) / 2.147483647E9);
                }
            }
            else {
                to[i] = 0.0f;
            }
        }
    }
    
    public static void pack(final float[] from, final int[] to, final VertexFormat formatTo, final int v, final int e) {
        final VertexFormatElement element = formatTo.getElement(e);
        final int vertexStart = v * formatTo.getNextOffset() + formatTo.getOffset(e);
        final int count = element.getElementCount();
        final VertexFormatElement.EnumType type = element.getType();
        final int size = type.getSize();
        final int mask = (256 << 8 * (size - 1)) - 1;
        for (int i = 0; i < 4; ++i) {
            if (i < count) {
                final int pos = vertexStart + size * i;
                final int index = pos >> 2;
                final int offset = pos & 0x3;
                int bits = 0;
                final float f = (i < from.length) ? from[i] : 0.0f;
                if (type == VertexFormatElement.EnumType.FLOAT) {
                    bits = Float.floatToRawIntBits(f);
                }
                else if (type == VertexFormatElement.EnumType.UBYTE || type == VertexFormatElement.EnumType.USHORT || type == VertexFormatElement.EnumType.UINT) {
                    bits = Math.round(f * mask);
                }
                else {
                    bits = Math.round(f * (mask >> 1));
                }
                final int n = index;
                to[n] &= ~(mask << offset * 8);
                final int n2 = index;
                to[n2] |= (bits & mask) << offset * 8;
            }
        }
    }
    
    @Deprecated
    public static IVertexConsumer getTessellator() {
        if (LightUtil.tessellator == null) {
            final Tessellator tes = Tessellator.getInstance();
            final BufferBuilder wr = tes.getBuffer();
            LightUtil.tessellator = new VertexBufferConsumer(wr);
        }
        return LightUtil.tessellator;
    }
    
    @Deprecated
    public static ItemConsumer getItemConsumer() {
        if (LightUtil.itemConsumer == null) {
            LightUtil.itemConsumer = new ItemConsumer(getTessellator());
        }
        return LightUtil.itemConsumer;
    }
    
    public static void renderQuadColorSlow(final BufferBuilder buffer, final BakedQuad quad, final int auxColor) {
        final ItemPipeline pipeline = LightUtil.itemPipeline.get();
        pipeline.bufferConsumer.setBuffer(buffer);
        final ItemConsumer cons = pipeline.itemConsumer;
        final float b = (auxColor & 0xFF) / 255.0f;
        final float g = (auxColor >>> 8 & 0xFF) / 255.0f;
        final float r = (auxColor >>> 16 & 0xFF) / 255.0f;
        final float a = (auxColor >>> 24 & 0xFF) / 255.0f;
        cons.setAuxColor(r, g, b, a);
        quad.pipe(cons);
    }
    
    public static void renderQuadColor(final BufferBuilder buffer, final BakedQuad quad, final int auxColor) {
        if (quad.getFormat().equals(buffer.getVertexFormat())) {
            buffer.addVertexData(quad.getVertexData());
            buffer.getVertexFormat().hasColor();
        }
        else {
            renderQuadColorSlow(buffer, quad, auxColor);
        }
    }
    
    private static final class ItemPipeline
    {
        final VertexBufferConsumer bufferConsumer;
        final ItemConsumer itemConsumer;
        
        ItemPipeline() {
            this.bufferConsumer = new VertexBufferConsumer();
            this.itemConsumer = new ItemConsumer(this.bufferConsumer);
        }
    }
    
    public static class ItemConsumer extends VertexTransformer
    {
        private int vertices;
        private float[] auxColor;
        private float[] buf;
        
        public ItemConsumer(final IVertexConsumer parent) {
            super(parent);
            this.vertices = 0;
            this.auxColor = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
            this.buf = new float[4];
        }
        
        public void setAuxColor(final float... auxColor) {
            System.arraycopy(auxColor, 0, this.auxColor, 0, this.auxColor.length);
        }
        
        @Override
        public void put(final int element, final float... data) {
            if (this.getVertexFormat().getElement(element).getUsage() == VertexFormatElement.EnumUsage.COLOR) {
                System.arraycopy(this.auxColor, 0, this.buf, 0, this.buf.length);
                for (int n = Math.min(4, data.length), i = 0; i < n; ++i) {
                    final float[] buf = this.buf;
                    final int n2 = i;
                    buf[n2] *= data[i];
                }
                super.put(element, this.buf);
            }
            else {
                super.put(element, data);
            }
            if (element == this.getVertexFormat().getElementCount() - 1) {
                ++this.vertices;
                if (this.vertices == 4) {
                    this.vertices = 0;
                }
            }
        }
    }
}
