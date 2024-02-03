// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.background;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import net.minecraft.client.Minecraft;

public class ShaderUniform
{
    private static final String VERTEX_SHADER = "#version 130\n\nvoid main() {\n    gl_TexCoord[0] = gl_MultiTexCoord0;\n    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n}";
    private Minecraft mc;
    private int program;
    private long startTime;
    float mousemove;
    
    public ShaderUniform(final String fragment) {
        this.mc = Minecraft.getMinecraft();
        this.program = GL20.glCreateProgram();
        this.startTime = System.currentTimeMillis();
        this.mousemove = 0.01f;
        this.initShader(fragment);
    }
    
    private void initShader(final String frag) {
        final int vertex = GL20.glCreateShader(35633);
        final int fragment = GL20.glCreateShader(35632);
        GL20.glShaderSource(vertex, "#version 130\n\nvoid main() {\n    gl_TexCoord[0] = gl_MultiTexCoord0;\n    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n}");
        GL20.glShaderSource(fragment, frag);
        GL20.glValidateProgram(this.program);
        GL20.glCompileShader(vertex);
        GL20.glCompileShader(fragment);
        GL20.glAttachShader(this.program, vertex);
        GL20.glAttachShader(this.program, fragment);
        GL20.glLinkProgram(this.program);
    }
    
    public void renderFirst() {
        GL11.glClear(16640);
        GL20.glUseProgram(this.program);
    }
    
    public void renderSecond() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        final ScaledResolution sr = new ScaledResolution(this.mc);
        GL11.glBegin(7);
        GL11.glTexCoord2d(0.0, 1.0);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glTexCoord2d(0.0, 0.0);
        GL11.glVertex2d(0.0, sr.getScaledHeight());
        GL11.glTexCoord2d(1.0, 0.0);
        GL11.glVertex2d(sr.getScaledWidth(), sr.getScaledHeight());
        GL11.glTexCoord2d(1.0, 1.0);
        GL11.glVertex2d(sr.getScaledWidth(), 0.0);
        GL11.glEnd();
        GL20.glUseProgram(0);
    }
    
    public void bind() {
        GL20.glUseProgram(this.program);
    }
    
    public int getProgram() {
        return this.program;
    }
    
    public ShaderUniform uniform1i(final String loc, final int i) {
        GL20.glUniform1i(GL20.glGetUniformLocation(this.program, loc), i);
        return this;
    }
    
    public ShaderUniform uniform2i(final String loc, final int i, final int i1) {
        GL20.glUniform2i(GL20.glGetUniformLocation(this.program, loc), i, i1);
        return this;
    }
    
    public ShaderUniform uniform3i(final String loc, final int i, final int i1, final int i2) {
        GL20.glUniform3i(GL20.glGetUniformLocation(this.program, loc), i, i1, i2);
        return this;
    }
    
    public ShaderUniform uniform4i(final String loc, final int i, final int i1, final int i2, final int i3) {
        GL20.glUniform4i(GL20.glGetUniformLocation(this.program, loc), i, i1, i2, i3);
        return this;
    }
    
    public ShaderUniform uniform1f(final String loc, final float f) {
        GL20.glUniform1f(GL20.glGetUniformLocation(this.program, loc), f);
        return this;
    }
    
    public ShaderUniform uniform2f(final String loc, final float f, final float f1) {
        GL20.glUniform2f(GL20.glGetUniformLocation(this.program, loc), f, f1);
        return this;
    }
    
    public ShaderUniform uniform3f(final String loc, final float f, final float f1, final float f2) {
        GL20.glUniform3f(GL20.glGetUniformLocation(this.program, loc), f, f1, f2);
        return this;
    }
    
    public ShaderUniform uniform4f(final String loc, final float f, final float f1, final float f2, final float f3) {
        GL20.glUniform4f(GL20.glGetUniformLocation(this.program, loc), f, f1, f2, f3);
        return this;
    }
    
    public ShaderUniform uniform1b(final String loc, final boolean b) {
        GL20.glUniform1i(GL20.glGetUniformLocation(this.program, loc), b ? 1 : 0);
        return this;
    }
    
    public void addDefaultUniforms(final boolean detectmouse) {
        this.mousemove = ((Mouse.getX() > 957) ? (this.mousemove -= 0.002f) : (this.mousemove += 0.002f));
        final float n3 = Mouse.getX() / (float)this.mc.displayWidth;
        final float n4 = Mouse.getY() / (float)this.mc.displayHeight;
        final FloatBuffer floatBuffer3 = BufferUtils.createFloatBuffer(2);
        floatBuffer3.position(0);
        floatBuffer3.put(n3);
        floatBuffer3.put(n4);
        floatBuffer3.flip();
        GL20.glUniform2f(GL20.glGetUniformLocation(this.program, "resolution"), (float)this.mc.displayWidth, (float)this.mc.displayHeight);
        final float time = (System.currentTimeMillis() - this.startTime) / 1000.0f;
        GL20.glUniform1f(GL20.glGetUniformLocation(this.program, "time"), time);
        if (detectmouse) {
            GL20.glUniform2f(GL20.glGetUniformLocation(this.program, "mouse"), this.mousemove, 0.0f);
        }
        else {
            GL20.glUniform2(GL20.glGetUniformLocation(this.program, "mouse"), floatBuffer3);
        }
    }
}
