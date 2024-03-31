// 
// Decompiled by Procyon v0.6.0
// 

package wdl.update;

import java.util.Arrays;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import java.util.regex.Matcher;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.regex.Pattern;

public class Release
{
    private static final Pattern HIDDEN_JSON_REGEX;
    private static final JsonParser PARSER;
    public final JsonObject object;
    public final String URL;
    public final String tag;
    public final String title;
    public final String date;
    public final boolean prerelease;
    public final String markdownBody;
    public final String textOnlyBody;
    public final HiddenInfo hiddenInfo;
    
    static {
        HIDDEN_JSON_REGEX = Pattern.compile("^\\[\\]\\(# '(.+?)'\\)");
        PARSER = new JsonParser();
    }
    
    public Release(final JsonObject object) {
        this.object = object;
        this.markdownBody = object.get("body").getAsString();
        final Matcher hiddenJSONMatcher = Release.HIDDEN_JSON_REGEX.matcher(this.markdownBody);
        if (hiddenJSONMatcher.find()) {
            final String hiddenJSONStr = this.markdownBody.substring(hiddenJSONMatcher.start(1), hiddenJSONMatcher.end(1));
            final JsonObject hiddenJSON = Release.PARSER.parse(hiddenJSONStr).getAsJsonObject();
            this.hiddenInfo = new HiddenInfo(hiddenJSON, (HiddenInfo)null);
        }
        else {
            this.hiddenInfo = null;
        }
        this.URL = object.get("html_url").getAsString();
        this.textOnlyBody = object.get("body_text").getAsString();
        this.tag = object.get("tag_name").getAsString();
        this.title = object.get("name").getAsString();
        this.date = object.get("published_at").getAsString();
        this.prerelease = object.get("prerelease").getAsBoolean();
    }
    
    @Override
    public String toString() {
        return "Release [URL=" + this.URL + ", tag=" + this.tag + ", title=" + this.title + ", date=" + this.date + ", prerelease=" + this.prerelease + ", markdownBody=" + this.markdownBody + ", textOnlyBody=" + this.textOnlyBody + ", hiddenInfo=" + this.hiddenInfo + "]";
    }
    
    public class HiddenInfo
    {
        public final String mainMinecraftVersion;
        public final String[] supportedMinecraftVersions;
        public final String loader;
        public final String post;
        public final HashData[] hashes;
        
        private HiddenInfo(final JsonObject object) {
            this.mainMinecraftVersion = object.get("Minecraft").getAsString();
            final JsonArray compatibleVersions = object.get("MinecraftCompatible").getAsJsonArray();
            this.supportedMinecraftVersions = new String[compatibleVersions.size()];
            for (int i = 0; i < compatibleVersions.size(); ++i) {
                this.supportedMinecraftVersions[i] = compatibleVersions.get(i).getAsString();
            }
            this.loader = object.get("Loader").getAsString();
            final JsonElement post = object.get("Post");
            if (post.isJsonNull()) {
                this.post = null;
            }
            else {
                this.post = post.getAsString();
            }
            final JsonArray hashes = object.get("Hashes").getAsJsonArray();
            this.hashes = new HashData[hashes.size()];
            for (int j = 0; j < hashes.size(); ++j) {
                this.hashes[j] = new HashData(hashes.get(j).getAsJsonObject());
            }
        }
        
        @Override
        public String toString() {
            return "HiddenInfo [mainMinecraftVersion=" + this.mainMinecraftVersion + ", supportedMinecraftVersions=" + Arrays.toString(this.supportedMinecraftVersions) + ", loader=" + this.loader + ", post=" + this.post + ", hashes=" + Arrays.toString(this.hashes) + "]";
        }
    }
    
    public class HashData
    {
        public final String relativeTo;
        public final String file;
        public final String[] validHashes;
        
        public HashData(final JsonObject object) {
            this.relativeTo = object.get("RelativeTo").getAsString();
            this.file = object.get("File").getAsString();
            final JsonArray hashes = object.get("Hash").getAsJsonArray();
            this.validHashes = new String[hashes.size()];
            for (int i = 0; i < this.validHashes.length; ++i) {
                this.validHashes[i] = hashes.get(i).getAsString();
            }
        }
        
        @Override
        public String toString() {
            return "HashData [relativeTo=" + this.relativeTo + ", file=" + this.file + ", validHashes=" + Arrays.toString(this.validHashes) + "]";
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + this.getOuterType().hashCode();
            result = 31 * result + ((this.file == null) ? 0 : this.file.hashCode());
            result = 31 * result + ((this.relativeTo == null) ? 0 : this.relativeTo.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof HashData)) {
                return false;
            }
            final HashData other = (HashData)obj;
            if (!this.getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (this.file == null) {
                if (other.file != null) {
                    return false;
                }
            }
            else if (!this.file.equals(other.file)) {
                return false;
            }
            if (this.relativeTo == null) {
                if (other.relativeTo != null) {
                    return false;
                }
            }
            else if (!this.relativeTo.equals(other.relativeTo)) {
                return false;
            }
            return true;
        }
        
        private Release getOuterType() {
            return Release.this;
        }
    }
}
