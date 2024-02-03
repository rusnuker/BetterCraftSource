// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.mods;

import me.nzxtercode.bettercraft.client.hud.IRender;
import me.nzxtercode.bettercraft.client.hud.HUDManager;
import me.nzxtercode.bettercraft.client.mods.impl.ModRadar;
import me.nzxtercode.bettercraft.client.mods.impl.ModSkin;
import me.nzxtercode.bettercraft.client.mods.impl.ModKeystrokes;
import me.nzxtercode.bettercraft.client.mods.impl.ModInfo;
import me.nzxtercode.bettercraft.client.mods.impl.ModArmorStatus;
import java.util.ArrayList;

public class ModInstances
{
    public static ArrayList<ModRender> getAllMods;
    private static ModArmorStatus modArmorStatus;
    private static ModInfo modInfo;
    private static ModKeystrokes modKeyStrokes;
    private static ModSkin modSkin;
    private static ModRadar modRadar;
    
    static {
        ModInstances.getAllMods = new ArrayList<ModRender>();
    }
    
    public static void register(final HUDManager hud) {
        ModInstances.modArmorStatus = new ModArmorStatus();
        hud.register(ModInstances.modArmorStatus);
        ModInstances.getAllMods.add(ModInstances.modArmorStatus);
        ModInstances.modInfo = new ModInfo();
        hud.register(ModInstances.modInfo);
        ModInstances.getAllMods.add(ModInstances.modInfo);
        ModInstances.modKeyStrokes = new ModKeystrokes();
        hud.register(ModInstances.modKeyStrokes);
        ModInstances.getAllMods.add(ModInstances.modKeyStrokes);
        ModInstances.modSkin = new ModSkin();
        hud.register(ModInstances.modSkin);
        ModInstances.getAllMods.add(ModInstances.modSkin);
        ModInstances.modRadar = new ModRadar();
        hud.register(ModInstances.modRadar);
        ModInstances.getAllMods.add(ModInstances.modRadar);
    }
    
    public static ArrayList<ModRender> getAllMods() {
        return ModInstances.getAllMods;
    }
}
