// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.manager;

import net.minecraft.util.IChatComponent;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.minecraft.tileentity.TileEntitySign;
import java.util.HashMap;
import net.labymod.core.BlockPosition;
import java.util.Map;

public class SignManager
{
    private static SignSearchSettings signSearchSettings;
    private static Map<BlockPosition, SignData> signDataMap;
    
    static {
        SignManager.signSearchSettings = new SignSearchSettings();
        SignManager.signDataMap = new HashMap<BlockPosition, SignData>();
    }
    
    public static void render(final TileEntitySign tileEntitySign) {
        if (!LabyMod.getSettings().signSearch || !SignManager.signSearchSettings.isEnabled()) {
            if (!SignManager.signDataMap.isEmpty()) {
                SignManager.signDataMap.clear();
            }
            return;
        }
        final BlockPosition blockPosition = LabyModCore.getMinecraft().getPosition(tileEntitySign.getPos());
        SignData signData = SignManager.signDataMap.get(blockPosition);
        if (signData == null || signData.getLastSignUpdated() + 500L < System.currentTimeMillis()) {
            SignManager.signDataMap.put(blockPosition, signData = new SignData(tileEntitySign));
        }
        signData.getSignColor().applyColor();
    }
    
    public static SignSearchSettings getSignSearchSettings() {
        return SignManager.signSearchSettings;
    }
    
    public enum SignColor
    {
        NONE("NONE", 0, 1.0f, 1.0f, 1.0f, 1.0f), 
        GREEN("GREEN", 1, 0.6f, 23.6f, 0.6f, 0.6f), 
        RED("RED", 2, 23.6f, 0.6f, 0.6f, 0.6f), 
        ORANGE("ORANGE", 3, 10.0f, 10.0f, 0.6f, 0.6f), 
        GRAY("GRAY", 4, 0.6f, 0.6f, 0.6f, 0.6f);
        
        private float red;
        private float green;
        private float blue;
        private float alpha;
        
        public void applyColor() {
            GlStateManager.color(this.red, this.green, this.blue, this.alpha);
        }
        
        private SignColor(final String s, final int n, final float red, final float green, final float blue, final float alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
        
        public float getRed() {
            return this.red;
        }
        
        public float getGreen() {
            return this.green;
        }
        
        public float getBlue() {
            return this.blue;
        }
        
        public float getAlpha() {
            return this.alpha;
        }
    }
    
    public static class SignData
    {
        private TileEntitySign tileEntitySign;
        private SignColor signColor;
        private long lastSignUpdated;
        
        public SignData(final TileEntitySign sign) {
            this.signColor = SignColor.NONE;
            this.tileEntitySign = sign;
            this.lastSignUpdated = System.currentTimeMillis();
            this.parseSignData();
        }
        
        private void parseSignData() {
            String fullString = "";
            final String[] lines = new String[4];
            int lineCount = -1;
            IChatComponent[] signText;
            for (int length = (signText = this.tileEntitySign.signText).length, i = 0; i < length; ++i) {
                final Object chatComponentObj = signText[i];
                ++lineCount;
                if (chatComponentObj != null) {
                    final String line = LabyModCore.getMinecraft().getChatComponent(chatComponentObj).getUnformattedText();
                    if (line != null) {
                        fullString = String.valueOf(fullString) + line;
                        lines[lineCount] = line;
                    }
                }
            }
            if (fullString.isEmpty()) {
                return;
            }
            final SignSearchSettings settings = SignManager.signSearchSettings;
            fullString = fullString.toLowerCase();
            final String searchString = settings.getSearchString().toLowerCase();
            boolean searchFound = searchString.isEmpty() || fullString.contains(searchString);
            if (!searchFound && searchString.contains(",")) {
                String[] split;
                for (int length2 = (split = searchString.split("\\,")).length, j = 0; j < length2; ++j) {
                    final String word = split[j];
                    if (fullString.contains(word)) {
                        searchFound = true;
                    }
                }
            }
            if (settings.isUseAdvancedOptions()) {
                final boolean blacklistFound = !settings.getBlacklistString().isEmpty() && fullString.contains(settings.getBlacklistString().toLowerCase());
                final Integer currentUserCount = this.getUserCount(lines, true);
                final Integer maxUserCount = this.getUserCount(lines, false);
                if (searchFound && !blacklistFound) {
                    final boolean isEmpty = currentUserCount != null && currentUserCount == 0 && settings.isFilterEmptyServer();
                    final boolean isFull = maxUserCount != null && currentUserCount != null && currentUserCount >= maxUserCount && settings.isFilterFullServer();
                    if (!isEmpty && !isFull) {
                        this.signColor = SignColor.GREEN;
                    }
                    else {
                        this.signColor = (isEmpty ? SignColor.GRAY : (isFull ? SignColor.ORANGE : SignColor.RED));
                    }
                }
                else {
                    this.signColor = SignColor.RED;
                }
            }
            else if (searchFound) {
                this.signColor = SignColor.GREEN;
            }
            else {
                this.signColor = SignColor.RED;
            }
        }
        
        private Integer getUserCount(final String[] lines, final boolean pre) {
            for (final String line : lines) {
                if (line != null && line.contains("/")) {
                    line.split("/");
                }
            }
            return null;
        }
        
        public TileEntitySign getTileEntitySign() {
            return this.tileEntitySign;
        }
        
        public SignColor getSignColor() {
            return this.signColor;
        }
        
        public long getLastSignUpdated() {
            return this.lastSignUpdated;
        }
        
        public void setTileEntitySign(final TileEntitySign tileEntitySign) {
            this.tileEntitySign = tileEntitySign;
        }
        
        public void setSignColor(final SignColor signColor) {
            this.signColor = signColor;
        }
        
        public void setLastSignUpdated(final long lastSignUpdated) {
            this.lastSignUpdated = lastSignUpdated;
        }
    }
    
    public static class SignSearchSettings
    {
        private String searchString;
        private String blacklistString;
        private boolean useAdvancedOptions;
        private boolean filterFullServer;
        private boolean filterEmptyServer;
        private boolean enabled;
        
        public SignSearchSettings() {
            this.searchString = "";
            this.blacklistString = "";
            this.useAdvancedOptions = false;
            this.filterFullServer = false;
            this.filterEmptyServer = false;
        }
        
        public void update() {
            this.enabled = ((this.useAdvancedOptions && (this.filterFullServer || this.filterEmptyServer || !this.blacklistString.isEmpty())) || !this.searchString.isEmpty());
        }
        
        public String getSearchString() {
            return this.searchString;
        }
        
        public String getBlacklistString() {
            return this.blacklistString;
        }
        
        public boolean isUseAdvancedOptions() {
            return this.useAdvancedOptions;
        }
        
        public boolean isFilterFullServer() {
            return this.filterFullServer;
        }
        
        public boolean isFilterEmptyServer() {
            return this.filterEmptyServer;
        }
        
        public boolean isEnabled() {
            return this.enabled;
        }
        
        public void setSearchString(final String searchString) {
            this.searchString = searchString;
        }
        
        public void setBlacklistString(final String blacklistString) {
            this.blacklistString = blacklistString;
        }
        
        public void setUseAdvancedOptions(final boolean useAdvancedOptions) {
            this.useAdvancedOptions = useAdvancedOptions;
        }
        
        public void setFilterFullServer(final boolean filterFullServer) {
            this.filterFullServer = filterFullServer;
        }
        
        public void setFilterEmptyServer(final boolean filterEmptyServer) {
            this.filterEmptyServer = filterEmptyServer;
        }
        
        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }
    }
}
