// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.shader.browser;

import java.io.File;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.amkgre.bettercraft.client.utils.WebUtils;
import java.util.concurrent.CopyOnWriteArrayList;

public class ShaderGlsSandboxAPI
{
    public CopyOnWriteArrayList<ShaderGlsSandoxShaderBrowser> getShadersByPage(final int pageNumber) {
        final CopyOnWriteArrayList<ShaderGlsSandoxShaderBrowser> shader = new CopyOnWriteArrayList<ShaderGlsSandoxShaderBrowser>();
        final String websiteResponse = WebUtils.getInformationsFromWebsite("http://glslsandbox.com/?page=" + pageNumber);
        final String splitOne = websiteResponse.split("</div><div id=\"paginate\"><a")[0];
        final String splitZwo = splitOne.replace(String.valueOf(splitOne.split("/div><div id=\"gallery\">\t")[0]) + "/div><div id=\"gallery\">\t", "");
        final String[] shaderObjs = splitZwo.split("></a>\t<a href=");
        String[] array;
        for (int length = (array = shaderObjs).length, i = 0; i < length; ++i) {
            final String shaderObj = array[i];
            final String shaderID = shaderObj.split("><img ")[0].replace("'", "").replace("<a href=", "");
            final String pictureID = shaderObj.replace(String.valueOf(shaderObj.split("><img src=")[0]) + "><img src=", "").replace("'", "").replace("></a>", "");
            shader.add(new ShaderGlsSandoxShaderBrowser(pictureID, shaderID));
        }
        return shader;
    }
    
    public String getShaderByID(final String shaderID) {
        final String websiteResponse = WebUtils.getInformationsFromWebsite("http://glslsandbox.com/item/" + shaderID.replace("e#", ""));
        final JsonObject json = new JsonParser().parse(websiteResponse).getAsJsonObject();
        return json.get("code").getAsString();
    }
    
    public boolean saveShader(final ShaderGlsSandoxShaderBrowser shader) {
        final File shadersFile = new File("BetterCraft", "shader.bc");
        try {
            if (!shadersFile.exists()) {
                shadersFile.createNewFile();
                ShaderSaveFile.setTextFromFile("{}", shadersFile);
            }
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
        final JsonObject json = new JsonParser().parse(ShaderSaveFile.getTextFromFile(shadersFile)).getAsJsonObject();
        if (this.isShaderSaved(shader, json)) {
            return false;
        }
        final JsonObject jsonObj = new JsonParser().parse("{\"text\":\"" + this.getShaderByID(shader.getShaderID()) + "\"}").getAsJsonObject();
        json.add(shader.getShaderID(), jsonObj.get("text"));
        ShaderSaveFile.setTextFromFile(String.valueOf(json), shadersFile);
        return true;
    }
    
    private boolean isShaderSaved(final ShaderGlsSandoxShaderBrowser shader, final JsonObject json) {
        try {
            json.get(shader.getShaderID()).getAsString().equals("");
            return true;
        }
        catch (final Throwable throwable) {
            return false;
        }
    }
}
