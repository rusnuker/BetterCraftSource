// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.background;

import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.AbstractMap;
import com.google.common.base.Strings;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;
import com.google.common.collect.Lists;
import java.util.concurrent.ConcurrentHashMap;
import me.nzxtercode.bettercraft.client.Config;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.util.Map;
import java.util.List;

public class ShaderBackgroundLoader
{
    private static ShaderBackgroundLoader loader;
    private ShaderUniform shaderUniform;
    private boolean isEnabledShader;
    private int currentShaderId;
    private final List<Map.Entry<String, String>> shaderList;
    private final String Custom;
    private boolean isPEnabledBackground;
    private boolean isCEnabledBackground;
    private final Map<String, DynamicTexture> imageCache;
    
    static {
        ShaderBackgroundLoader.loader = new ShaderBackgroundLoader();
    }
    
    public static ShaderBackgroundLoader getLoader() {
        return ShaderBackgroundLoader.loader;
    }
    
    public ShaderBackgroundLoader() {
        this.Custom = Config.getInstance().getBackground("Shader").get("custom").getAsString();
        this.imageCache = new ConcurrentHashMap<String, DynamicTexture>();
        this.shaderList = (List<Map.Entry<String, String>>)Lists.newCopyOnWriteArrayList();
        Stream.of((List[])new List[] { Arrays.asList(this.getClass().getDeclaredFields()), Arrays.asList(ShaderList1.class.getDeclaredFields()), Arrays.asList(ShaderList2.class.getDeclaredFields()) }).flatMap(list -> list.stream()).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()).forEach(field -> {
            try {
                final String shaderContext = (String)field.get(this);
                if (!Strings.isNullOrEmpty(shaderContext)) {
                    this.shaderList.add(new AbstractMap.SimpleEntry<String, String>(field.getName(), shaderContext));
                }
            }
            catch (final Throwable t2) {}
            return;
        });
        this.setEnabledShader(Config.getInstance().getBackground("Shader").get("enabled").getAsBoolean());
        this.setCurrentShaderId(Config.getInstance().getBackground("Shader").get("id").getAsInt());
    }
    
    public void renderShader() {
        this.shaderUniform.renderFirst();
        this.shaderUniform.addDefaultUniforms(true);
        this.shaderUniform.renderSecond();
    }
    
    private void updateShaderUniform() {
        this.shaderUniform = new ShaderUniform((String)this.shaderList.get(this.currentShaderId).getValue());
        Config.getInstance().editBackground("Shader", json -> json.add("id", new JsonPrimitive(this.currentShaderId)));
    }
    
    public void setCurrentShaderId(final int currentShaderId) {
        this.currentShaderId = currentShaderId;
        this.updateShaderUniform();
    }
    
    public int getCurrentShaderId() {
        return this.currentShaderId;
    }
    
    public void setEnabledShader(final boolean isEnabledShader) {
        this.isEnabledShader = isEnabledShader;
        Config.getInstance().editBackground("Shader", json -> json.add("enabled", new JsonPrimitive(this.isEnabledShader)));
    }
    
    public final boolean isEnabledShader() {
        return this.isEnabledShader;
    }
    
    public final List<Map.Entry<String, String>> getShaderList() {
        return this.shaderList;
    }
    
    public final boolean isPEnabledBackground() {
        return this.isPEnabledBackground;
    }
    
    public void setPEnabledBackground(final boolean isPEnabledBackground) {
        this.isPEnabledBackground = isPEnabledBackground;
        Config.getInstance().editBackground("Background", json -> json.addProperty("penabled", this.isPEnabledBackground));
    }
    
    public final boolean isCEnabledBackground() {
        return this.isCEnabledBackground;
    }
    
    public void setCEnabledBackground(final boolean isCEnabledBackground) {
        this.isCEnabledBackground = isCEnabledBackground;
        Config.getInstance().editBackground("Background", json -> json.addProperty("cenabled", this.isCEnabledBackground));
    }
    
    public final Map<String, DynamicTexture> getImageCache() {
        return this.imageCache;
    }
    
    public static final List<String> getBackgrounds() {
        return Arrays.asList("mojang", "wood", "town", "block");
    }
}
