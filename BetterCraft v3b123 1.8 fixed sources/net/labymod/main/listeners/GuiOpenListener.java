// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main.listeners;

import java.lang.reflect.Method;
import net.labymod.support.util.Debug;
import net.labymod.main.LabyModForge;
import net.labymod.account.AccountLoginHandler;
import net.minecraft.client.gui.GuiChat;
import net.labymod.core.LabyModCore;
import net.labymod.main.Source;
import java.lang.reflect.Field;
import net.minecraft.client.gui.GuiScreen;

public class GuiOpenListener
{
    private static final boolean IS_MC_18;
    private GuiScreen lastScreen;
    private Field defaultInputTextField;
    private Boolean replayModInstalled;
    private boolean replacedThings;
    private static final String[] serverListServerData;
    private static final String[] entityRendererItemRendererNames;
    private static final String[] entityRendererNames;
    private static final String[] itemRendererNames;
    private static final String[] upperChestInventory;
    private static final String[] lowerChestInventory;
    
    static {
        IS_MC_18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
        serverListServerData = LabyModCore.getMappingAdapter().getServerListServerDataMappings();
        entityRendererItemRendererNames = LabyModCore.getMappingAdapter().getEntityRendererItemRendererMappings();
        entityRendererNames = LabyModCore.getMappingAdapter().getEntityRendererMappings();
        itemRendererNames = LabyModCore.getMappingAdapter().getItemRendererMappings();
        upperChestInventory = LabyModCore.getMappingAdapter().getUpperChestInventoryMappings();
        lowerChestInventory = LabyModCore.getMappingAdapter().getLowerChestInventoryMappings();
    }
    
    public GuiOpenListener() {
        this.replayModInstalled = null;
        try {
            this.defaultInputTextField = AccountLoginHandler.ReflectionHelper.findField(GuiChat.class, LabyModCore.getMappingAdapter().getDefaultInputFieldTextMappings());
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private boolean isReplayModInstalled() {
        if (!LabyModForge.isForge()) {
            return false;
        }
        if (this.replayModInstalled != null) {
            return this.replayModInstalled;
        }
        try {
            final Class<?> loader = Class.forName("net.minecraftforge.fml.common.Loader");
            final Method method = loader.getMethod("isModLoaded", String.class);
            final boolean result = (boolean)method.invoke(null, "replaymod");
            this.replayModInstalled = result;
            if (result) {
                Debug.log(Debug.EnumDebugMode.GENERAL, "ReplayMod detected!");
            }
        }
        catch (final Exception e) {
            this.replayModInstalled = false;
        }
        return this.replayModInstalled;
    }
}
