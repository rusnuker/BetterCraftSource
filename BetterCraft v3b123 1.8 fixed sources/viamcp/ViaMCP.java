// 
// Decompiled by Procyon v0.6.0
// 

package viamcp;

import viamcp.vialoadingbase.model.ComparableProtocolVersion;
import java.io.File;
import viamcp.vialoadingbase.ViaLoadingBase;
import viamcp.gui.AsyncVersionSlider;

public class ViaMCP
{
    public static final int NATIVE_VERSION = 47;
    private static final ViaMCP INSTANCE;
    private static AsyncVersionSlider asyncVersionSlider;
    
    static {
        INSTANCE = new ViaMCP();
    }
    
    public static ViaMCP getInstance() {
        return ViaMCP.INSTANCE;
    }
    
    public static void init() {
        ViaLoadingBase.ViaLoadingBaseBuilder.create().runDirectory(new File("ViaMCP")).nativeVersion(47).onProtocolReload(comparableProtocolVersion -> {
            if (getAsyncVersionSlider() != null) {
                getAsyncVersionSlider().setVersion(comparableProtocolVersion.getVersion());
            }
        }).build();
    }
    
    public void initAsyncSlider() {
        this.initAsyncSlider(5, 5, 110, 20);
    }
    
    public void initAsyncSlider(final int x, final int y, final int width, final int height) {
        ViaMCP.asyncVersionSlider = new AsyncVersionSlider(-1, x, y, Math.max(width, 110), height);
    }
    
    public static AsyncVersionSlider getAsyncVersionSlider() {
        return ViaMCP.asyncVersionSlider;
    }
}
