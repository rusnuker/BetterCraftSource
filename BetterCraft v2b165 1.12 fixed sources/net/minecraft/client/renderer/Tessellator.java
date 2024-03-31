// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

public class Tessellator
{
    private final BufferBuilder worldRenderer;
    private final WorldVertexBufferUploader vboUploader;
    private static final Tessellator INSTANCE;
    
    static {
        INSTANCE = new Tessellator(2097152);
    }
    
    public static Tessellator getInstance() {
        return Tessellator.INSTANCE;
    }
    
    public Tessellator(final int bufferSize) {
        this.vboUploader = new WorldVertexBufferUploader();
        this.worldRenderer = new BufferBuilder(bufferSize);
    }
    
    public void draw() {
        this.worldRenderer.finishDrawing();
        this.vboUploader.draw(this.worldRenderer);
    }
    
    public BufferBuilder getBuffer() {
        return this.worldRenderer;
    }
}
