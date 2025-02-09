// 
// Decompiled by Procyon v0.6.0
// 

package shadersmod.client;

public class ScreenShaderOptions
{
    private String name;
    private ShaderOption[] shaderOptions;
    private int columns;
    
    public ScreenShaderOptions(final String name, final ShaderOption[] shaderOptions, final int columns) {
        this.name = name;
        this.shaderOptions = shaderOptions;
        this.columns = columns;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ShaderOption[] getShaderOptions() {
        return this.shaderOptions;
    }
    
    public int getColumns() {
        return this.columns;
    }
}
