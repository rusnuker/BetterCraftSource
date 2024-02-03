// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.commands.impl;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import java.util.concurrent.CompletableFuture;
import com.google.gson.JsonElement;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;
import me.nzxtercode.bettercraft.client.BetterCraft;
import net.minecraft.util.ChatComponentText;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import net.minecraft.client.Minecraft;
import java.net.URL;
import me.nzxtercode.bettercraft.client.commands.Command;

public class GetGeoServerDataCommand extends Command
{
    public GetGeoServerDataCommand() {
        super("getgeo", new String[] { "checkhost", "checkgeodata", "getgeodata", "checkip" });
    }
    
    @Override
    public void run(final String alias, final String[] args) {
        CompletableFuture.runAsync(() -> {
            try {
                new URL("http://ip-api.com/json/" + Minecraft.getMinecraft().getCurrentServerData().serverIP + "?fields=status,message,continent,continentCode,country,countryCode,region,regionName,city,district,zgeodata,lat,lon,timezone,currency,isp,org,as,asname,reverse,mobile,proxy,query");
                final URL url;
                final URL servergeodata = url;
                new BufferedReader(new InputStreamReader(servergeodata.openStream()));
                final BufferedReader bufferedReader2;
                final BufferedReader bufferedReader = bufferedReader2;
                final String geodata = bufferedReader.readLine();
                final JsonObject json = new JsonParser().parse(geodata).getAsJsonObject();
                json.entrySet().forEach(entry -> {
                    Minecraft.getMinecraft().ingameGUI.getChatGUI();
                    new(net.minecraft.util.ChatComponentText.class)();
                    final Object[] array = new Object[4];
                    BetterCraft.getInstance();
                    final Object o;
                    array[o] = BetterCraft.clientPrefix;
                    array[1] = StringUtils.capitalize(entry.getKey());
                    array[2] = EnumChatFormatting.RESET;
                    array[3] = entry.getValue().getAsString();
                    final String s;
                    new ChatComponentText(String.format(s, array));
                    final IChatComponent chatComponent;
                    final GuiNewChat guiNewChat;
                    guiNewChat.printChatMessage(chatComponent);
                });
            }
            catch (final Exception ex) {}
        });
    }
    
    @Override
    public List<String> autocomplete(final int arg, final String[] args) {
        return new ArrayList<String>();
    }
}
