// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

import org.lwjgl.opengl.GLContext;
import optifine.GlBlendState;
import optifine.Config;
import java.nio.ByteBuffer;
import org.lwjgl.util.vector.Quaternion;
import javax.annotation.Nullable;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

public class GlStateManager
{
    private static final FloatBuffer BUF_FLOAT_16;
    private static final FloatBuffer BUF_FLOAT_4;
    private static final AlphaState alphaState;
    private static final BooleanState lightingState;
    private static final BooleanState[] lightState;
    private static final ColorMaterialState colorMaterialState;
    private static final BlendState blendState;
    private static final DepthState depthState;
    private static final FogState fogState;
    private static final CullState cullState;
    private static final PolygonOffsetState polygonOffsetState;
    private static final ColorLogicState colorLogicState;
    private static final TexGenState texGenState;
    private static final ClearState clearState;
    private static final StencilState stencilState;
    private static final BooleanState normalizeState;
    public static int activeTextureUnit;
    public static final TextureState[] textureState;
    private static int activeShadeModel;
    private static final BooleanState rescaleNormalState;
    private static final ColorMask colorMaskState;
    private static final Color colorState;
    public static boolean clearEnabled;
    
    static {
        BUF_FLOAT_16 = BufferUtils.createFloatBuffer(16);
        BUF_FLOAT_4 = BufferUtils.createFloatBuffer(4);
        alphaState = new AlphaState(null);
        lightingState = new BooleanState(2896);
        lightState = new BooleanState[8];
        GlStateManager.clearEnabled = true;
        for (int i = 0; i < 8; ++i) {
            GlStateManager.lightState[i] = new BooleanState(16384 + i);
        }
        colorMaterialState = new ColorMaterialState(null);
        blendState = new BlendState(null);
        depthState = new DepthState(null);
        fogState = new FogState(null);
        cullState = new CullState(null);
        polygonOffsetState = new PolygonOffsetState(null);
        colorLogicState = new ColorLogicState(null);
        texGenState = new TexGenState(null);
        clearState = new ClearState(null);
        stencilState = new StencilState(null);
        normalizeState = new BooleanState(2977);
        textureState = new TextureState[32];
        for (int j = 0; j < GlStateManager.textureState.length; ++j) {
            GlStateManager.textureState[j] = new TextureState(null);
        }
        GlStateManager.activeShadeModel = 7425;
        rescaleNormalState = new BooleanState(32826);
        colorMaskState = new ColorMask(null);
        colorState = new Color();
    }
    
    public static void pushAttrib() {
        GL11.glPushAttrib(8256);
    }
    
    public static void popAttrib() {
        GL11.glPopAttrib();
    }
    
    public static void disableAlpha() {
        GlStateManager.alphaState.alphaTest.setDisabled();
    }
    
    public static void enableAlpha() {
        GlStateManager.alphaState.alphaTest.setEnabled();
    }
    
    public static void alphaFunc(final int func, final float ref) {
        if (func != GlStateManager.alphaState.func || ref != GlStateManager.alphaState.ref) {
            GL11.glAlphaFunc(GlStateManager.alphaState.func = func, GlStateManager.alphaState.ref = ref);
        }
    }
    
    public static void enableLighting() {
        GlStateManager.lightingState.setEnabled();
    }
    
    public static void disableLighting() {
        GlStateManager.lightingState.setDisabled();
    }
    
    public static void enableLight(final int light) {
        GlStateManager.lightState[light].setEnabled();
    }
    
    public static void disableLight(final int light) {
        GlStateManager.lightState[light].setDisabled();
    }
    
    public static void enableColorMaterial() {
        GlStateManager.colorMaterialState.colorMaterial.setEnabled();
    }
    
    public static void disableColorMaterial() {
        GlStateManager.colorMaterialState.colorMaterial.setDisabled();
    }
    
    public static void colorMaterial(final int face, final int mode) {
        if (face != GlStateManager.colorMaterialState.face || mode != GlStateManager.colorMaterialState.mode) {
            GL11.glColorMaterial(GlStateManager.colorMaterialState.face = face, GlStateManager.colorMaterialState.mode = mode);
        }
    }
    
    public static void glLight(final int light, final int pname, final FloatBuffer params) {
        GL11.glLight(light, pname, params);
    }
    
    public static void glLightModel(final int pname, final FloatBuffer params) {
        GL11.glLightModel(pname, params);
    }
    
    public static void glNormal3f(final float nx, final float ny, final float nz) {
        GL11.glNormal3f(nx, ny, nz);
    }
    
    public static void disableDepth() {
        GlStateManager.depthState.depthTest.setDisabled();
    }
    
    public static void enableDepth() {
        GlStateManager.depthState.depthTest.setEnabled();
    }
    
    public static void depthFunc(final int depthFunc) {
        if (depthFunc != GlStateManager.depthState.depthFunc) {
            GL11.glDepthFunc(GlStateManager.depthState.depthFunc = depthFunc);
        }
    }
    
    public static void depthMask(final boolean flagIn) {
        if (flagIn != GlStateManager.depthState.maskEnabled) {
            GL11.glDepthMask(GlStateManager.depthState.maskEnabled = flagIn);
        }
    }
    
    public static void disableBlend() {
        GlStateManager.blendState.blend.setDisabled();
    }
    
    public static void enableBlend() {
        GlStateManager.blendState.blend.setEnabled();
    }
    
    public static void blendFunc(final SourceFactor srcFactor, final DestFactor dstFactor) {
        blendFunc(srcFactor.factor, dstFactor.factor);
    }
    
    public static void blendFunc(final int srcFactor, final int dstFactor) {
        if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor) {
            GL11.glBlendFunc(GlStateManager.blendState.srcFactor = srcFactor, GlStateManager.blendState.dstFactor = dstFactor);
        }
    }
    
    public static void tryBlendFuncSeparate(final SourceFactor srcFactor, final DestFactor dstFactor, final SourceFactor srcFactorAlpha, final DestFactor dstFactorAlpha) {
        tryBlendFuncSeparate(srcFactor.factor, dstFactor.factor, srcFactorAlpha.factor, dstFactorAlpha.factor);
    }
    
    public static void tryBlendFuncSeparate(final int srcFactor, final int dstFactor, final int srcFactorAlpha, final int dstFactorAlpha) {
        if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor || srcFactorAlpha != GlStateManager.blendState.srcFactorAlpha || dstFactorAlpha != GlStateManager.blendState.dstFactorAlpha) {
            OpenGlHelper.glBlendFunc(GlStateManager.blendState.srcFactor = srcFactor, GlStateManager.blendState.dstFactor = dstFactor, GlStateManager.blendState.srcFactorAlpha = srcFactorAlpha, GlStateManager.blendState.dstFactorAlpha = dstFactorAlpha);
        }
    }
    
    public static void glBlendEquation(final int blendEquation) {
        GL14.glBlendEquation(blendEquation);
    }
    
    public static void enableOutlineMode(final int p_187431_0_) {
        GlStateManager.BUF_FLOAT_4.put(0, (p_187431_0_ >> 16 & 0xFF) / 255.0f);
        GlStateManager.BUF_FLOAT_4.put(1, (p_187431_0_ >> 8 & 0xFF) / 255.0f);
        GlStateManager.BUF_FLOAT_4.put(2, (p_187431_0_ >> 0 & 0xFF) / 255.0f);
        GlStateManager.BUF_FLOAT_4.put(3, (p_187431_0_ >> 24 & 0xFF) / 255.0f);
        glTexEnv(8960, 8705, GlStateManager.BUF_FLOAT_4);
        glTexEnvi(8960, 8704, 34160);
        glTexEnvi(8960, 34161, 7681);
        glTexEnvi(8960, 34176, 34166);
        glTexEnvi(8960, 34192, 768);
        glTexEnvi(8960, 34162, 7681);
        glTexEnvi(8960, 34184, 5890);
        glTexEnvi(8960, 34200, 770);
    }
    
    public static void disableOutlineMode() {
        glTexEnvi(8960, 8704, 8448);
        glTexEnvi(8960, 34161, 8448);
        glTexEnvi(8960, 34162, 8448);
        glTexEnvi(8960, 34176, 5890);
        glTexEnvi(8960, 34184, 5890);
        glTexEnvi(8960, 34192, 768);
        glTexEnvi(8960, 34200, 770);
    }
    
    public static void enableFog() {
        GlStateManager.fogState.fog.setEnabled();
    }
    
    public static void disableFog() {
        GlStateManager.fogState.fog.setDisabled();
    }
    
    public static void setFog(final FogMode fogMode) {
        setFog(fogMode.capabilityId);
    }
    
    private static void setFog(final int param) {
        if (param != GlStateManager.fogState.mode) {
            GL11.glFogi(2917, GlStateManager.fogState.mode = param);
        }
    }
    
    public static void setFogDensity(final float param) {
        if (param != GlStateManager.fogState.density) {
            GL11.glFogf(2914, GlStateManager.fogState.density = param);
        }
    }
    
    public static void setFogStart(final float param) {
        if (param != GlStateManager.fogState.start) {
            GL11.glFogf(2915, GlStateManager.fogState.start = param);
        }
    }
    
    public static void setFogEnd(final float param) {
        if (param != GlStateManager.fogState.end) {
            GL11.glFogf(2916, GlStateManager.fogState.end = param);
        }
    }
    
    public static void glFog(final int pname, final FloatBuffer param) {
        GL11.glFog(pname, param);
    }
    
    public static void glFogi(final int pname, final int param) {
        GL11.glFogi(pname, param);
    }
    
    public static void enableCull() {
        GlStateManager.cullState.cullFace.setEnabled();
    }
    
    public static void disableCull() {
        GlStateManager.cullState.cullFace.setDisabled();
    }
    
    public static void cullFace(final CullFace cullFace) {
        cullFace(cullFace.mode);
    }
    
    private static void cullFace(final int mode) {
        if (mode != GlStateManager.cullState.mode) {
            GL11.glCullFace(GlStateManager.cullState.mode = mode);
        }
    }
    
    public static void glPolygonMode(final int face, final int mode) {
        GL11.glPolygonMode(face, mode);
    }
    
    public static void enablePolygonOffset() {
        GlStateManager.polygonOffsetState.polygonOffsetFill.setEnabled();
    }
    
    public static void disablePolygonOffset() {
        GlStateManager.polygonOffsetState.polygonOffsetFill.setDisabled();
    }
    
    public static void doPolygonOffset(final float factor, final float units) {
        if (factor != GlStateManager.polygonOffsetState.factor || units != GlStateManager.polygonOffsetState.units) {
            GL11.glPolygonOffset(GlStateManager.polygonOffsetState.factor = factor, GlStateManager.polygonOffsetState.units = units);
        }
    }
    
    public static void enableColorLogic() {
        GlStateManager.colorLogicState.colorLogicOp.setEnabled();
    }
    
    public static void disableColorLogic() {
        GlStateManager.colorLogicState.colorLogicOp.setDisabled();
    }
    
    public static void colorLogicOp(final LogicOp logicOperation) {
        colorLogicOp(logicOperation.opcode);
    }
    
    public static void colorLogicOp(final int opcode) {
        if (opcode != GlStateManager.colorLogicState.opcode) {
            GL11.glLogicOp(GlStateManager.colorLogicState.opcode = opcode);
        }
    }
    
    public static void enableTexGenCoord(final TexGen texGen) {
        texGenCoord(texGen).textureGen.setEnabled();
    }
    
    public static void disableTexGenCoord(final TexGen texGen) {
        texGenCoord(texGen).textureGen.setDisabled();
    }
    
    public static void texGen(final TexGen texGen, final int param) {
        final TexGenCoord glstatemanager$texgencoord = texGenCoord(texGen);
        if (param != glstatemanager$texgencoord.param) {
            glstatemanager$texgencoord.param = param;
            GL11.glTexGeni(glstatemanager$texgencoord.coord, 9472, param);
        }
    }
    
    public static void texGen(final TexGen texGen, final int pname, final FloatBuffer params) {
        GL11.glTexGen(texGenCoord(texGen).coord, pname, params);
    }
    
    private static TexGenCoord texGenCoord(final TexGen texGen) {
        switch (texGen) {
            case S: {
                return GlStateManager.texGenState.s;
            }
            case T: {
                return GlStateManager.texGenState.t;
            }
            case R: {
                return GlStateManager.texGenState.r;
            }
            case Q: {
                return GlStateManager.texGenState.q;
            }
            default: {
                return GlStateManager.texGenState.s;
            }
        }
    }
    
    public static void setActiveTexture(final int texture) {
        if (GlStateManager.activeTextureUnit != texture - OpenGlHelper.defaultTexUnit) {
            GlStateManager.activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(texture);
        }
    }
    
    public static void enableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setEnabled();
    }
    
    public static void disableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setDisabled();
    }
    
    public static void glTexEnv(final int p_187448_0_, final int p_187448_1_, final FloatBuffer p_187448_2_) {
        GL11.glTexEnv(p_187448_0_, p_187448_1_, p_187448_2_);
    }
    
    public static void glTexEnvi(final int p_187399_0_, final int p_187399_1_, final int p_187399_2_) {
        GL11.glTexEnvi(p_187399_0_, p_187399_1_, p_187399_2_);
    }
    
    public static void glTexEnvf(final int p_187436_0_, final int p_187436_1_, final float p_187436_2_) {
        GL11.glTexEnvf(p_187436_0_, p_187436_1_, p_187436_2_);
    }
    
    public static void glTexParameterf(final int p_187403_0_, final int p_187403_1_, final float p_187403_2_) {
        GL11.glTexParameterf(p_187403_0_, p_187403_1_, p_187403_2_);
    }
    
    public static void glTexParameteri(final int p_187421_0_, final int p_187421_1_, final int p_187421_2_) {
        GL11.glTexParameteri(p_187421_0_, p_187421_1_, p_187421_2_);
    }
    
    public static int glGetTexLevelParameteri(final int p_187411_0_, final int p_187411_1_, final int p_187411_2_) {
        return GL11.glGetTexLevelParameteri(p_187411_0_, p_187411_1_, p_187411_2_);
    }
    
    public static int generateTexture() {
        return GL11.glGenTextures();
    }
    
    public static void deleteTexture(final int texture) {
        if (texture != 0) {
            GL11.glDeleteTextures(texture);
            TextureState[] textureState;
            for (int length = (textureState = GlStateManager.textureState).length, i = 0; i < length; ++i) {
                final TextureState glstatemanager$texturestate = textureState[i];
                if (glstatemanager$texturestate.textureName == texture) {
                    glstatemanager$texturestate.textureName = 0;
                }
            }
        }
    }
    
    public static void bindTexture(final int texture) {
        if (texture != GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName) {
            GL11.glBindTexture(3553, GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = texture);
        }
    }
    
    public static void glTexImage2D(final int p_187419_0_, final int p_187419_1_, final int p_187419_2_, final int p_187419_3_, final int p_187419_4_, final int p_187419_5_, final int p_187419_6_, final int p_187419_7_, @Nullable final IntBuffer p_187419_8_) {
        GL11.glTexImage2D(p_187419_0_, p_187419_1_, p_187419_2_, p_187419_3_, p_187419_4_, p_187419_5_, p_187419_6_, p_187419_7_, p_187419_8_);
    }
    
    public static void glTexSubImage2D(final int p_187414_0_, final int p_187414_1_, final int p_187414_2_, final int p_187414_3_, final int p_187414_4_, final int p_187414_5_, final int p_187414_6_, final int p_187414_7_, final IntBuffer p_187414_8_) {
        GL11.glTexSubImage2D(p_187414_0_, p_187414_1_, p_187414_2_, p_187414_3_, p_187414_4_, p_187414_5_, p_187414_6_, p_187414_7_, p_187414_8_);
    }
    
    public static void glCopyTexSubImage2D(final int p_187443_0_, final int p_187443_1_, final int p_187443_2_, final int p_187443_3_, final int p_187443_4_, final int p_187443_5_, final int p_187443_6_, final int p_187443_7_) {
        GL11.glCopyTexSubImage2D(p_187443_0_, p_187443_1_, p_187443_2_, p_187443_3_, p_187443_4_, p_187443_5_, p_187443_6_, p_187443_7_);
    }
    
    public static void glGetTexImage(final int p_187433_0_, final int p_187433_1_, final int p_187433_2_, final int p_187433_3_, final IntBuffer p_187433_4_) {
        GL11.glGetTexImage(p_187433_0_, p_187433_1_, p_187433_2_, p_187433_3_, p_187433_4_);
    }
    
    public static void enableNormalize() {
        GlStateManager.normalizeState.setEnabled();
    }
    
    public static void disableNormalize() {
        GlStateManager.normalizeState.setDisabled();
    }
    
    public static void shadeModel(final int mode) {
        if (mode != GlStateManager.activeShadeModel) {
            GL11.glShadeModel(GlStateManager.activeShadeModel = mode);
        }
    }
    
    public static void enableRescaleNormal() {
        GlStateManager.rescaleNormalState.setEnabled();
    }
    
    public static void disableRescaleNormal() {
        GlStateManager.rescaleNormalState.setDisabled();
    }
    
    public static void viewport(final int x, final int y, final int width, final int height) {
        GL11.glViewport(x, y, width, height);
    }
    
    public static void colorMask(final boolean red, final boolean green, final boolean blue, final boolean alpha) {
        if (red != GlStateManager.colorMaskState.red || green != GlStateManager.colorMaskState.green || blue != GlStateManager.colorMaskState.blue || alpha != GlStateManager.colorMaskState.alpha) {
            GL11.glColorMask(GlStateManager.colorMaskState.red = red, GlStateManager.colorMaskState.green = green, GlStateManager.colorMaskState.blue = blue, GlStateManager.colorMaskState.alpha = alpha);
        }
    }
    
    public static void clearDepth(final double depth) {
        if (depth != GlStateManager.clearState.depth) {
            GL11.glClearDepth(GlStateManager.clearState.depth = depth);
        }
    }
    
    public static void clearColor(final float red, final float green, final float blue, final float alpha) {
        if (red != GlStateManager.clearState.color.red || green != GlStateManager.clearState.color.green || blue != GlStateManager.clearState.color.blue || alpha != GlStateManager.clearState.color.alpha) {
            GL11.glClearColor(GlStateManager.clearState.color.red = red, GlStateManager.clearState.color.green = green, GlStateManager.clearState.color.blue = blue, GlStateManager.clearState.color.alpha = alpha);
        }
    }
    
    public static void clear(final int mask) {
        if (GlStateManager.clearEnabled) {
            GL11.glClear(mask);
        }
    }
    
    public static void matrixMode(final int mode) {
        GL11.glMatrixMode(mode);
    }
    
    public static void loadIdentity() {
        GL11.glLoadIdentity();
    }
    
    public static void pushMatrix() {
        GL11.glPushMatrix();
    }
    
    public static void popMatrix() {
        GL11.glPopMatrix();
    }
    
    public static void getFloat(final int pname, final FloatBuffer params) {
        GL11.glGetFloat(pname, params);
    }
    
    public static void ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        GL11.glOrtho(left, right, bottom, top, zNear, zFar);
    }
    
    public static void rotate(final float angle, final float x, final float y, final float z) {
        GL11.glRotatef(angle, x, y, z);
    }
    
    public static void scale(final float x, final float y, final float z) {
        GL11.glScalef(x, y, z);
    }
    
    public static void scale(final double x, final double y, final double z) {
        GL11.glScaled(x, y, z);
    }
    
    public static void translate(final float x, final float y, final float z) {
        GL11.glTranslatef(x, y, z);
    }
    
    public static void translate(final double x, final double y, final double z) {
        GL11.glTranslated(x, y, z);
    }
    
    public static void multMatrix(final FloatBuffer matrix) {
        GL11.glMultMatrix(matrix);
    }
    
    public static void rotate(final Quaternion p_187444_0_) {
        multMatrix(quatToGlMatrix(GlStateManager.BUF_FLOAT_16, p_187444_0_));
    }
    
    public static FloatBuffer quatToGlMatrix(final FloatBuffer p_187418_0_, final Quaternion p_187418_1_) {
        p_187418_0_.clear();
        final float f = p_187418_1_.x * p_187418_1_.x;
        final float f2 = p_187418_1_.x * p_187418_1_.y;
        final float f3 = p_187418_1_.x * p_187418_1_.z;
        final float f4 = p_187418_1_.x * p_187418_1_.w;
        final float f5 = p_187418_1_.y * p_187418_1_.y;
        final float f6 = p_187418_1_.y * p_187418_1_.z;
        final float f7 = p_187418_1_.y * p_187418_1_.w;
        final float f8 = p_187418_1_.z * p_187418_1_.z;
        final float f9 = p_187418_1_.z * p_187418_1_.w;
        p_187418_0_.put(1.0f - 2.0f * (f5 + f8));
        p_187418_0_.put(2.0f * (f2 + f9));
        p_187418_0_.put(2.0f * (f3 - f7));
        p_187418_0_.put(0.0f);
        p_187418_0_.put(2.0f * (f2 - f9));
        p_187418_0_.put(1.0f - 2.0f * (f + f8));
        p_187418_0_.put(2.0f * (f6 + f4));
        p_187418_0_.put(0.0f);
        p_187418_0_.put(2.0f * (f3 + f7));
        p_187418_0_.put(2.0f * (f6 - f4));
        p_187418_0_.put(1.0f - 2.0f * (f + f5));
        p_187418_0_.put(0.0f);
        p_187418_0_.put(0.0f);
        p_187418_0_.put(0.0f);
        p_187418_0_.put(0.0f);
        p_187418_0_.put(1.0f);
        p_187418_0_.rewind();
        return p_187418_0_;
    }
    
    public static void color(final float colorRed, final float colorGreen, final float colorBlue, final float colorAlpha) {
        if (colorRed != GlStateManager.colorState.red || colorGreen != GlStateManager.colorState.green || colorBlue != GlStateManager.colorState.blue || colorAlpha != GlStateManager.colorState.alpha) {
            GL11.glColor4f(GlStateManager.colorState.red = colorRed, GlStateManager.colorState.green = colorGreen, GlStateManager.colorState.blue = colorBlue, GlStateManager.colorState.alpha = colorAlpha);
        }
    }
    
    public static void color(final float colorRed, final float colorGreen, final float colorBlue) {
        color(colorRed, colorGreen, colorBlue, 1.0f);
    }
    
    public static void glTexCoord2f(final float p_187426_0_, final float p_187426_1_) {
        GL11.glTexCoord2f(p_187426_0_, p_187426_1_);
    }
    
    public static void glVertex3f(final float p_187435_0_, final float p_187435_1_, final float p_187435_2_) {
        GL11.glVertex3f(p_187435_0_, p_187435_1_, p_187435_2_);
    }
    
    public static void resetColor() {
        GlStateManager.colorState.red = -1.0f;
        GlStateManager.colorState.green = -1.0f;
        GlStateManager.colorState.blue = -1.0f;
        GlStateManager.colorState.alpha = -1.0f;
    }
    
    public static void glNormalPointer(final int p_187446_0_, final int p_187446_1_, final ByteBuffer p_187446_2_) {
        GL11.glNormalPointer(p_187446_0_, p_187446_1_, p_187446_2_);
    }
    
    public static void glTexCoordPointer(final int p_187405_0_, final int p_187405_1_, final int p_187405_2_, final int p_187405_3_) {
        GL11.glTexCoordPointer(p_187405_0_, p_187405_1_, p_187405_2_, p_187405_3_);
    }
    
    public static void glTexCoordPointer(final int p_187404_0_, final int p_187404_1_, final int p_187404_2_, final ByteBuffer p_187404_3_) {
        GL11.glTexCoordPointer(p_187404_0_, p_187404_1_, p_187404_2_, p_187404_3_);
    }
    
    public static void glVertexPointer(final int p_187420_0_, final int p_187420_1_, final int p_187420_2_, final int p_187420_3_) {
        GL11.glVertexPointer(p_187420_0_, p_187420_1_, p_187420_2_, p_187420_3_);
    }
    
    public static void glVertexPointer(final int p_187427_0_, final int p_187427_1_, final int p_187427_2_, final ByteBuffer p_187427_3_) {
        GL11.glVertexPointer(p_187427_0_, p_187427_1_, p_187427_2_, p_187427_3_);
    }
    
    public static void glColorPointer(final int p_187406_0_, final int p_187406_1_, final int p_187406_2_, final int p_187406_3_) {
        GL11.glColorPointer(p_187406_0_, p_187406_1_, p_187406_2_, p_187406_3_);
    }
    
    public static void glColorPointer(final int p_187400_0_, final int p_187400_1_, final int p_187400_2_, final ByteBuffer p_187400_3_) {
        GL11.glColorPointer(p_187400_0_, p_187400_1_, p_187400_2_, p_187400_3_);
    }
    
    public static void glDisableClientState(final int p_187429_0_) {
        GL11.glDisableClientState(p_187429_0_);
    }
    
    public static void glEnableClientState(final int p_187410_0_) {
        GL11.glEnableClientState(p_187410_0_);
    }
    
    public static void glBegin(final int p_187447_0_) {
        GL11.glBegin(p_187447_0_);
    }
    
    public static void glEnd() {
        GL11.glEnd();
    }
    
    public static void glDrawArrays(final int p_187439_0_, final int p_187439_1_, final int p_187439_2_) {
        GL11.glDrawArrays(p_187439_0_, p_187439_1_, p_187439_2_);
    }
    
    public static void glLineWidth(final float p_187441_0_) {
        GL11.glLineWidth(p_187441_0_);
    }
    
    public static void callList(final int list) {
        GL11.glCallList(list);
    }
    
    public static void glDeleteLists(final int p_187449_0_, final int p_187449_1_) {
        GL11.glDeleteLists(p_187449_0_, p_187449_1_);
    }
    
    public static void glNewList(final int p_187423_0_, final int p_187423_1_) {
        GL11.glNewList(p_187423_0_, p_187423_1_);
    }
    
    public static void glEndList() {
        GL11.glEndList();
    }
    
    public static int glGenLists(final int p_187442_0_) {
        return GL11.glGenLists(p_187442_0_);
    }
    
    public static void glPixelStorei(final int p_187425_0_, final int p_187425_1_) {
        GL11.glPixelStorei(p_187425_0_, p_187425_1_);
    }
    
    public static void glReadPixels(final int p_187413_0_, final int p_187413_1_, final int p_187413_2_, final int p_187413_3_, final int p_187413_4_, final int p_187413_5_, final IntBuffer p_187413_6_) {
        GL11.glReadPixels(p_187413_0_, p_187413_1_, p_187413_2_, p_187413_3_, p_187413_4_, p_187413_5_, p_187413_6_);
    }
    
    public static int glGetError() {
        return GL11.glGetError();
    }
    
    public static String glGetString(final int p_187416_0_) {
        return GL11.glGetString(p_187416_0_);
    }
    
    public static void glGetInteger(final int p_187445_0_, final IntBuffer p_187445_1_) {
        GL11.glGetInteger(p_187445_0_, p_187445_1_);
    }
    
    public static int glGetInteger(final int p_187397_0_) {
        return GL11.glGetInteger(p_187397_0_);
    }
    
    public static void enableBlendProfile(final Profile p_187408_0_) {
        p_187408_0_.apply();
    }
    
    public static void disableBlendProfile(final Profile p_187440_0_) {
        p_187440_0_.clean();
    }
    
    public static int getActiveTextureUnit() {
        return OpenGlHelper.defaultTexUnit + GlStateManager.activeTextureUnit;
    }
    
    public static void bindCurrentTexture() {
        GL11.glBindTexture(3553, GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName);
    }
    
    public static int getBoundTexture() {
        return GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName;
    }
    
    public static void checkBoundTexture() {
        if (Config.isMinecraftThread()) {
            final int i = GL11.glGetInteger(34016);
            final int j = GL11.glGetInteger(32873);
            final int k = getActiveTextureUnit();
            final int l = getBoundTexture();
            if (l > 0 && (i != k || j != l)) {
                Config.dbg("checkTexture: act: " + k + ", glAct: " + i + ", tex: " + l + ", glTex: " + j);
            }
        }
    }
    
    public static void deleteTextures(final IntBuffer p_deleteTextures_0_) {
        p_deleteTextures_0_.rewind();
        while (p_deleteTextures_0_.position() < p_deleteTextures_0_.limit()) {
            final int i = p_deleteTextures_0_.get();
            deleteTexture(i);
        }
        p_deleteTextures_0_.rewind();
    }
    
    public static boolean isFogEnabled() {
        return GlStateManager.fogState.fog.currentState;
    }
    
    public static void setFogEnabled(final boolean p_setFogEnabled_0_) {
        GlStateManager.fogState.fog.setState(p_setFogEnabled_0_);
    }
    
    public static void getBlendState(final GlBlendState p_getBlendState_0_) {
        p_getBlendState_0_.enabled = GlStateManager.blendState.blend.currentState;
        p_getBlendState_0_.srcFactor = GlStateManager.blendState.srcFactor;
        p_getBlendState_0_.dstFactor = GlStateManager.blendState.dstFactor;
    }
    
    public static void setBlendState(final GlBlendState p_setBlendState_0_) {
        GlStateManager.blendState.blend.setState(p_setBlendState_0_.enabled);
        blendFunc(p_setBlendState_0_.srcFactor, p_setBlendState_0_.dstFactor);
    }
    
    public enum CullFace
    {
        FRONT("FRONT", 0, 1028), 
        BACK("BACK", 1, 1029), 
        FRONT_AND_BACK("FRONT_AND_BACK", 2, 1032);
        
        public final int mode;
        
        private CullFace(final String s, final int n, final int modeIn) {
            this.mode = modeIn;
        }
    }
    
    public enum DestFactor
    {
        CONSTANT_ALPHA("CONSTANT_ALPHA", 0, 32771), 
        CONSTANT_COLOR("CONSTANT_COLOR", 1, 32769), 
        DST_ALPHA("DST_ALPHA", 2, 772), 
        DST_COLOR("DST_COLOR", 3, 774), 
        ONE("ONE", 4, 1), 
        ONE_MINUS_CONSTANT_ALPHA("ONE_MINUS_CONSTANT_ALPHA", 5, 32772), 
        ONE_MINUS_CONSTANT_COLOR("ONE_MINUS_CONSTANT_COLOR", 6, 32770), 
        ONE_MINUS_DST_ALPHA("ONE_MINUS_DST_ALPHA", 7, 773), 
        ONE_MINUS_DST_COLOR("ONE_MINUS_DST_COLOR", 8, 775), 
        ONE_MINUS_SRC_ALPHA("ONE_MINUS_SRC_ALPHA", 9, 771), 
        ONE_MINUS_SRC_COLOR("ONE_MINUS_SRC_COLOR", 10, 769), 
        SRC_ALPHA("SRC_ALPHA", 11, 770), 
        SRC_COLOR("SRC_COLOR", 12, 768), 
        ZERO("ZERO", 13, 0);
        
        public final int factor;
        
        private DestFactor(final String s, final int n, final int factorIn) {
            this.factor = factorIn;
        }
    }
    
    public enum FogMode
    {
        LINEAR("LINEAR", 0, 9729), 
        EXP("EXP", 1, 2048), 
        EXP2("EXP2", 2, 2049);
        
        public final int capabilityId;
        
        private FogMode(final String s, final int n, final int capabilityIn) {
            this.capabilityId = capabilityIn;
        }
    }
    
    public enum LogicOp
    {
        AND("AND", 0, 5377), 
        AND_INVERTED("AND_INVERTED", 1, 5380), 
        AND_REVERSE("AND_REVERSE", 2, 5378), 
        CLEAR("CLEAR", 3, 5376), 
        COPY("COPY", 4, 5379), 
        COPY_INVERTED("COPY_INVERTED", 5, 5388), 
        EQUIV("EQUIV", 6, 5385), 
        INVERT("INVERT", 7, 5386), 
        NAND("NAND", 8, 5390), 
        NOOP("NOOP", 9, 5381), 
        NOR("NOR", 10, 5384), 
        OR("OR", 11, 5383), 
        OR_INVERTED("OR_INVERTED", 12, 5389), 
        OR_REVERSE("OR_REVERSE", 13, 5387), 
        SET("SET", 14, 5391), 
        XOR("XOR", 15, 5382);
        
        public final int opcode;
        
        private LogicOp(final String s, final int n, final int opcodeIn) {
            this.opcode = opcodeIn;
        }
    }
    
    public enum Profile
    {
        DEFAULT {
            @Override
            public void apply() {
                GlStateManager.disableAlpha();
                GlStateManager.alphaFunc(519, 0.0f);
                GlStateManager.disableLighting();
                GL11.glLightModel(2899, RenderHelper.setColorBuffer(0.2f, 0.2f, 0.2f, 1.0f));
                for (int i = 0; i < 8; ++i) {
                    GlStateManager.disableLight(i);
                    GL11.glLight(16384 + i, 4608, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
                    GL11.glLight(16384 + i, 4611, RenderHelper.setColorBuffer(0.0f, 0.0f, 1.0f, 0.0f));
                    if (i == 0) {
                        GL11.glLight(16384 + i, 4609, RenderHelper.setColorBuffer(1.0f, 1.0f, 1.0f, 1.0f));
                        GL11.glLight(16384 + i, 4610, RenderHelper.setColorBuffer(1.0f, 1.0f, 1.0f, 1.0f));
                    }
                    else {
                        GL11.glLight(16384 + i, 4609, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
                        GL11.glLight(16384 + i, 4610, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
                    }
                }
                GlStateManager.disableColorMaterial();
                GlStateManager.colorMaterial(1032, 5634);
                GlStateManager.disableDepth();
                GlStateManager.depthFunc(513);
                GlStateManager.depthMask(true);
                GlStateManager.disableBlend();
                GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ZERO);
                GlStateManager.tryBlendFuncSeparate(SourceFactor.ONE, DestFactor.ZERO, SourceFactor.ONE, DestFactor.ZERO);
                GL14.glBlendEquation(32774);
                GlStateManager.disableFog();
                GL11.glFogi(2917, 2048);
                GlStateManager.setFogDensity(1.0f);
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(1.0f);
                GL11.glFog(2918, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                if (GLContext.getCapabilities().GL_NV_fog_distance) {
                    GL11.glFogi(2917, 34140);
                }
                GlStateManager.doPolygonOffset(0.0f, 0.0f);
                GlStateManager.disableColorLogic();
                GlStateManager.colorLogicOp(5379);
                GlStateManager.disableTexGenCoord(TexGen.S);
                GlStateManager.texGen(TexGen.S, 9216);
                GlStateManager.texGen(TexGen.S, 9474, RenderHelper.setColorBuffer(1.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.texGen(TexGen.S, 9217, RenderHelper.setColorBuffer(1.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.disableTexGenCoord(TexGen.T);
                GlStateManager.texGen(TexGen.T, 9216);
                GlStateManager.texGen(TexGen.T, 9474, RenderHelper.setColorBuffer(0.0f, 1.0f, 0.0f, 0.0f));
                GlStateManager.texGen(TexGen.T, 9217, RenderHelper.setColorBuffer(0.0f, 1.0f, 0.0f, 0.0f));
                GlStateManager.disableTexGenCoord(TexGen.R);
                GlStateManager.texGen(TexGen.R, 9216);
                GlStateManager.texGen(TexGen.R, 9474, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.texGen(TexGen.R, 9217, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.disableTexGenCoord(TexGen.Q);
                GlStateManager.texGen(TexGen.Q, 9216);
                GlStateManager.texGen(TexGen.Q, 9474, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.texGen(TexGen.Q, 9217, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.setActiveTexture(0);
                GL11.glTexParameteri(3553, 10240, 9729);
                GL11.glTexParameteri(3553, 10241, 9986);
                GL11.glTexParameteri(3553, 10242, 10497);
                GL11.glTexParameteri(3553, 10243, 10497);
                GL11.glTexParameteri(3553, 33085, 1000);
                GL11.glTexParameteri(3553, 33083, 1000);
                GL11.glTexParameteri(3553, 33082, -1000);
                GL11.glTexParameterf(3553, 34049, 0.0f);
                GL11.glTexEnvi(8960, 8704, 8448);
                GL11.glTexEnv(8960, 8705, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                GL11.glTexEnvi(8960, 34161, 8448);
                GL11.glTexEnvi(8960, 34162, 8448);
                GL11.glTexEnvi(8960, 34176, 5890);
                GL11.glTexEnvi(8960, 34177, 34168);
                GL11.glTexEnvi(8960, 34178, 34166);
                GL11.glTexEnvi(8960, 34184, 5890);
                GL11.glTexEnvi(8960, 34185, 34168);
                GL11.glTexEnvi(8960, 34186, 34166);
                GL11.glTexEnvi(8960, 34192, 768);
                GL11.glTexEnvi(8960, 34193, 768);
                GL11.glTexEnvi(8960, 34194, 770);
                GL11.glTexEnvi(8960, 34200, 770);
                GL11.glTexEnvi(8960, 34201, 770);
                GL11.glTexEnvi(8960, 34202, 770);
                GL11.glTexEnvf(8960, 34163, 1.0f);
                GL11.glTexEnvf(8960, 3356, 1.0f);
                GlStateManager.disableNormalize();
                GlStateManager.shadeModel(7425);
                GlStateManager.disableRescaleNormal();
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.clearDepth(1.0);
                GL11.glLineWidth(1.0f);
                GL11.glNormal3f(0.0f, 0.0f, 1.0f);
                GL11.glPolygonMode(1028, 6914);
                GL11.glPolygonMode(1029, 6914);
            }
            
            @Override
            public void clean() {
            }
        }, 
        PLAYER_SKIN {
            @Override
            public void apply() {
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            }
            
            @Override
            public void clean() {
                GlStateManager.disableBlend();
            }
        }, 
        TRANSPARENT_MODEL {
            @Override
            public void apply() {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 0.15f);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.alphaFunc(516, 0.003921569f);
            }
            
            @Override
            public void clean() {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                GlStateManager.depthMask(true);
            }
        };
        
        private Profile(final String s, final int n) {
        }
        
        public abstract void apply();
        
        public abstract void clean();
    }
    
    public enum SourceFactor
    {
        CONSTANT_ALPHA("CONSTANT_ALPHA", 0, 32771), 
        CONSTANT_COLOR("CONSTANT_COLOR", 1, 32769), 
        DST_ALPHA("DST_ALPHA", 2, 772), 
        DST_COLOR("DST_COLOR", 3, 774), 
        ONE("ONE", 4, 1), 
        ONE_MINUS_CONSTANT_ALPHA("ONE_MINUS_CONSTANT_ALPHA", 5, 32772), 
        ONE_MINUS_CONSTANT_COLOR("ONE_MINUS_CONSTANT_COLOR", 6, 32770), 
        ONE_MINUS_DST_ALPHA("ONE_MINUS_DST_ALPHA", 7, 773), 
        ONE_MINUS_DST_COLOR("ONE_MINUS_DST_COLOR", 8, 775), 
        ONE_MINUS_SRC_ALPHA("ONE_MINUS_SRC_ALPHA", 9, 771), 
        ONE_MINUS_SRC_COLOR("ONE_MINUS_SRC_COLOR", 10, 769), 
        SRC_ALPHA("SRC_ALPHA", 11, 770), 
        SRC_ALPHA_SATURATE("SRC_ALPHA_SATURATE", 12, 776), 
        SRC_COLOR("SRC_COLOR", 13, 768), 
        ZERO("ZERO", 14, 0);
        
        public final int factor;
        
        private SourceFactor(final String s, final int n, final int factorIn) {
            this.factor = factorIn;
        }
    }
    
    public enum TexGen
    {
        S("S", 0), 
        T("T", 1), 
        R("R", 2), 
        Q("Q", 3);
        
        private TexGen(final String s, final int n) {
        }
    }
    
    static class AlphaState
    {
        public BooleanState alphaTest;
        public int func;
        public float ref;
        
        private AlphaState() {
            this.alphaTest = new BooleanState(3008);
            this.func = 519;
            this.ref = -1.0f;
        }
    }
    
    static class BlendState
    {
        public BooleanState blend;
        public int srcFactor;
        public int dstFactor;
        public int srcFactorAlpha;
        public int dstFactorAlpha;
        
        private BlendState() {
            this.blend = new BooleanState(3042);
            this.srcFactor = 1;
            this.dstFactor = 0;
            this.srcFactorAlpha = 1;
            this.dstFactorAlpha = 0;
        }
    }
    
    static class BooleanState
    {
        private final int capability;
        private boolean currentState;
        
        public BooleanState(final int capabilityIn) {
            this.capability = capabilityIn;
        }
        
        public void setDisabled() {
            this.setState(false);
        }
        
        public void setEnabled() {
            this.setState(true);
        }
        
        public void setState(final boolean state) {
            if (state != this.currentState) {
                this.currentState = state;
                if (state) {
                    GL11.glEnable(this.capability);
                }
                else {
                    GL11.glDisable(this.capability);
                }
            }
        }
    }
    
    static class ClearState
    {
        public double depth;
        public Color color;
        
        private ClearState() {
            this.depth = 1.0;
            this.color = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
    
    static class Color
    {
        public float red;
        public float green;
        public float blue;
        public float alpha;
        
        public Color() {
            this(1.0f, 1.0f, 1.0f, 1.0f);
        }
        
        public Color(final float redIn, final float greenIn, final float blueIn, final float alphaIn) {
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
            this.alpha = 1.0f;
            this.red = redIn;
            this.green = greenIn;
            this.blue = blueIn;
            this.alpha = alphaIn;
        }
    }
    
    static class ColorLogicState
    {
        public BooleanState colorLogicOp;
        public int opcode;
        
        private ColorLogicState() {
            this.colorLogicOp = new BooleanState(3058);
            this.opcode = 5379;
        }
    }
    
    static class ColorMask
    {
        public boolean red;
        public boolean green;
        public boolean blue;
        public boolean alpha;
        
        private ColorMask() {
            this.red = true;
            this.green = true;
            this.blue = true;
            this.alpha = true;
        }
    }
    
    static class ColorMaterialState
    {
        public BooleanState colorMaterial;
        public int face;
        public int mode;
        
        private ColorMaterialState() {
            this.colorMaterial = new BooleanState(2903);
            this.face = 1032;
            this.mode = 5634;
        }
    }
    
    static class CullState
    {
        public BooleanState cullFace;
        public int mode;
        
        private CullState() {
            this.cullFace = new BooleanState(2884);
            this.mode = 1029;
        }
    }
    
    static class DepthState
    {
        public BooleanState depthTest;
        public boolean maskEnabled;
        public int depthFunc;
        
        private DepthState() {
            this.depthTest = new BooleanState(2929);
            this.maskEnabled = true;
            this.depthFunc = 513;
        }
    }
    
    static class FogState
    {
        public BooleanState fog;
        public int mode;
        public float density;
        public float start;
        public float end;
        
        private FogState() {
            this.fog = new BooleanState(2912);
            this.mode = 2048;
            this.density = 1.0f;
            this.end = 1.0f;
        }
    }
    
    static class PolygonOffsetState
    {
        public BooleanState polygonOffsetFill;
        public BooleanState polygonOffsetLine;
        public float factor;
        public float units;
        
        private PolygonOffsetState() {
            this.polygonOffsetFill = new BooleanState(32823);
            this.polygonOffsetLine = new BooleanState(10754);
        }
    }
    
    static class StencilFunc
    {
        public int func;
        public int mask;
        
        private StencilFunc() {
            this.func = 519;
            this.mask = -1;
        }
    }
    
    static class StencilState
    {
        public StencilFunc func;
        public int mask;
        public int fail;
        public int zfail;
        public int zpass;
        
        private StencilState() {
            this.func = new StencilFunc(null);
            this.mask = -1;
            this.fail = 7680;
            this.zfail = 7680;
            this.zpass = 7680;
        }
    }
    
    static class TexGenCoord
    {
        public BooleanState textureGen;
        public int coord;
        public int param;
        
        public TexGenCoord(final int coordIn, final int capabilityIn) {
            this.param = -1;
            this.coord = coordIn;
            this.textureGen = new BooleanState(capabilityIn);
        }
    }
    
    static class TexGenState
    {
        public TexGenCoord s;
        public TexGenCoord t;
        public TexGenCoord r;
        public TexGenCoord q;
        
        private TexGenState() {
            this.s = new TexGenCoord(8192, 3168);
            this.t = new TexGenCoord(8193, 3169);
            this.r = new TexGenCoord(8194, 3170);
            this.q = new TexGenCoord(8195, 3171);
        }
    }
    
    public static class TextureState
    {
        public BooleanState texture2DState;
        public int textureName;
        
        private TextureState() {
            this.texture2DState = new BooleanState(3553);
        }
    }
}
