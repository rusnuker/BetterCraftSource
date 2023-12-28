// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import org.apache.logging.log4j.Logger;

public class UploadInfo
{
    private static final Logger LOGGER;
    @Expose
    private boolean worldClosed;
    @Expose
    private String token;
    @Expose
    private String uploadEndpoint;
    private int port;
    
    public UploadInfo() {
        this.token = "";
        this.uploadEndpoint = "";
    }
    
    public static UploadInfo parse(final String json) {
        final UploadInfo uploadInfo = new UploadInfo();
        try {
            final JsonParser parser = new JsonParser();
            final JsonObject jsonObject = parser.parse(json).getAsJsonObject();
            uploadInfo.worldClosed = JsonUtils.getBooleanOr("worldClosed", jsonObject, false);
            uploadInfo.token = JsonUtils.getStringOr("token", jsonObject, null);
            uploadInfo.uploadEndpoint = JsonUtils.getStringOr("uploadEndpoint", jsonObject, null);
            uploadInfo.port = JsonUtils.getIntOr("port", jsonObject, 8080);
        }
        catch (final Exception e) {
            UploadInfo.LOGGER.error("Could not parse UploadInfo: " + e.getMessage());
        }
        return uploadInfo;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public String getUploadEndpoint() {
        return this.uploadEndpoint;
    }
    
    public boolean isWorldClosed() {
        return this.worldClosed;
    }
    
    public void setToken(final String token) {
        this.token = token;
    }
    
    public int getPort() {
        return this.port;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
