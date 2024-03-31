// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader extends WorldVertexBufferUploader
{
    private VertexBuffer vertexBuffer;
    
    @Override
    public void draw(final BufferBuilder vertexBufferIn) {
        vertexBufferIn.reset();
        this.vertexBuffer.bufferData(vertexBufferIn.getByteBuffer());
    }
    
    public void setVertexBuffer(final VertexBuffer vertexBufferIn) {
        this.vertexBuffer = vertexBufferIn;
    }
}
