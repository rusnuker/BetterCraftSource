// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine;

import net.minecraft.src.Config;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import java.nio.IntBuffer;
import org.lwjgl.opengl.ARBDebugOutput;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.ARBDebugOutputCallback;

public class GlDebugHandler implements ARBDebugOutputCallback.Handler
{
    public static void createDisplayDebug() throws LWJGLException {
        final boolean flag = GLContext.getCapabilities().GL_ARB_debug_output;
        final ContextAttribs contextattribs = new ContextAttribs().withDebug(true);
        Display.create(new PixelFormat().withDepthBits(24), contextattribs);
        ARBDebugOutput.glDebugMessageCallbackARB(new ARBDebugOutputCallback(new GlDebugHandler()));
        ARBDebugOutput.glDebugMessageControlARB(4352, 4352, 4352, null, true);
        GL11.glEnable(33346);
    }
    
    @Override
    public void handleMessage(final int source, final int type, final int id, final int severity, final String message) {
        if (!message.contains("glBindFramebuffer") && !message.contains("Wide lines") && !message.contains("shader recompiled")) {
            Config.dbg("[LWJGL] source: " + this.getSource(source) + ", type: " + this.getType(type) + ", id: " + id + ", severity: " + this.getSeverity(severity) + ", message: " + message);
            new Throwable("StackTrace").printStackTrace();
        }
    }
    
    public String getSource(final int source) {
        switch (source) {
            case 33350: {
                return "API";
            }
            case 33351: {
                return "WIN";
            }
            case 33352: {
                return "SHADER";
            }
            case 33353: {
                return "EXT";
            }
            case 33354: {
                return "APP";
            }
            case 33355: {
                return "OTHER";
            }
            default: {
                return this.getUnknown(source);
            }
        }
    }
    
    public String getType(final int type) {
        switch (type) {
            case 33356: {
                return "ERROR";
            }
            case 33357: {
                return "DEPRECATED";
            }
            case 33358: {
                return "UNDEFINED";
            }
            case 33359: {
                return "PORTABILITY";
            }
            case 33360: {
                return "PERFORMANCE";
            }
            case 33361: {
                return "OTHER";
            }
            default: {
                return this.getUnknown(type);
            }
        }
    }
    
    public String getSeverity(final int severity) {
        switch (severity) {
            case 37190: {
                return "HIGH";
            }
            case 37191: {
                return "MEDIUM";
            }
            case 37192: {
                return "LOW";
            }
            default: {
                return this.getUnknown(severity);
            }
        }
    }
    
    private String getUnknown(final int token) {
        return "Unknown (0x" + Integer.toHexString(token).toUpperCase() + ")";
    }
}
