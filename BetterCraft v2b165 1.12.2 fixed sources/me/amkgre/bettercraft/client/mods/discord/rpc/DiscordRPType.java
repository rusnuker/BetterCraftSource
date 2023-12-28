// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc;

public enum DiscordRPType
{
    BETTERCRAFT("BETTERCRAFT", 0, "BetterCraft", "811322861372309525", "811323175908409415"), 
    TERRARIA("TERRARIA", 1, "Terraria", "784809903297396756", ""), 
    AMONGUS("AMONGUS", 2, "Among Us", "784810829715079188", ""), 
    VALORANT("VALORANT", 3, "VALORANT", "784812243211911198", ""), 
    GARRYSMOD("GARRYSMOD", 4, "Garry's Mod", "784838422065578064", ""), 
    BADLION("BADLION", 5, "Badlion Client", "784812995032383508", ""), 
    LABYMOD("LABYMOD", 6, "LabyMod", "784815101319446528", ""), 
    RAINBOWSIXSIEGE("RAINBOWSIXSIEGE", 7, "Rainbow Six Siege", "784818992044048465", ""), 
    OSU("OSU", 8, "osu!", "784819991576313927", "784820004956143686"), 
    LEAGUEOFLEGENDS("LEAGUEOFLEGENDS", 9, "League of Leagends", "784821382915031040", ""), 
    GRANDTHEFTAUTOV("GRANDTHEFTAUTOV", 10, "Grand Theft Auto V", "784822296212537374", ""), 
    ECLIPSE("ECLIPSE", 11, "Eclipse IDE", "784825053652189205", "784825054398644264"), 
    PYCHARM("PYCHARM", 12, "PyCharm Professional", "784826752480182272", "784826771542376458"), 
    BLUESTACKS("BLUESTACKS", 13, "BlueStacks", "784827745908949002", ""), 
    LIQUIDBOUNCE("LIQUIDBOUNCE", 14, "LiquidBounce", "784828840781545503", ""), 
    COUNTERSTRIKE("COUNTERSTRIKE", 15, "Counter-Strike: Global Offensive", "784814422370811975", ""), 
    BILDSCHIRM\u00dcBERTRAGUNG("BILDSCHIRM\u00dcBERTRAGUNG", 16, "Bildschirm\u00fcbertragung", "784841924799365150", ""), 
    CIPHER("CIPHER", 17, "Cipher", "785113010699894804", "");
    
    private String name;
    private String bigImageAssetsKey;
    private String smallImageAssetsKey;
    
    private DiscordRPType(final String s, final int n, final String name, final String bigImageAssetsKey, final String smallImageAssetsKey) {
        this.name = name;
        this.bigImageAssetsKey = bigImageAssetsKey;
        this.smallImageAssetsKey = smallImageAssetsKey;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getBigImageAssetsKey() {
        return this.bigImageAssetsKey;
    }
    
    public String getSmallImageAssetsKey() {
        return this.smallImageAssetsKey;
    }
}
