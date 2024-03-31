// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import optifine.Config;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.OpenGlHelper;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class InfoCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("§m§8----------§r §5Infos §m§8----------", true);
            Command.clientMSG("§d" + CommandManager.syntax + "info self", true);
            Command.clientMSG("§d" + CommandManager.syntax + "info server", true);
            Command.clientMSG("§m§8----------§r §5Infos §m§8----------", true);
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("self")) {
                Command.clientMSG("§m§8----------§r §5Infos §m§8----------", true);
                Command.clientMSG("PC Name: §d" + System.getenv("LOGONSERVER"), true);
                Command.clientMSG("", true);
                Command.clientMSG("User Name: §d" + System.getenv("USERNAME"), true);
                Command.clientMSG("User Path: §d" + System.getenv("HOMEPATH"), true);
                Command.clientMSG("", true);
                Command.clientMSG("Java Name: §d" + System.getProperty("java.vm.name"), true);
                Command.clientMSG("Java Version: §d" + System.getProperty("java.runtime.version") + " §8/ §d" + System.getProperty("java.vm.version"), true);
                Command.clientMSG("Java Info: §d" + System.getProperty("java.vm.info"), true);
                Command.clientMSG("Java Vendor: §d" + System.getProperty("java.vendor"), true);
                Command.clientMSG("Java Path: §d" + System.getProperty("java.home"), true);
                Command.clientMSG("", true);
                Command.clientMSG("OS Legacy: §d" + System.getenv("OS"), true);
                Command.clientMSG("OS Name: §d" + System.getProperty("os.name"), true);
                Command.clientMSG("OS Version: §d" + System.getProperty("os.version"), true);
                Command.clientMSG("OS Arch: §d" + System.getProperty("os.arch"), true);
                Command.clientMSG("", true);
                Command.clientMSG("CPU Arch: §d" + System.getenv("PROCESSOR_ARCHITECTURE"), true);
                Command.clientMSG("CPU Revision: §d" + System.getenv("PROCESSOR_REVISION"), true);
                Command.clientMSG("CPU Numbers: §d" + System.getenv("NUMBER_OF_PROCESSORS"), true);
                Command.clientMSG("CPU Type: §d" + System.getenv("PROCESSOR_IDENTIFIER"), true);
                Command.clientMSG("CPU Name: §d" + OpenGlHelper.getCpu(), true);
                Command.clientMSG("", true);
                Command.clientMSG("GPU Name: §d" + GL11.glGetString(7937), true);
                Command.clientMSG("GPU Version: §d" + GL11.glGetString(7938), true);
                Command.clientMSG("GPU Vendor: §d" + GL11.glGetString(7936), true);
                Command.clientMSG("", true);
                Command.clientMSG("OpenGL Version: §d" + Config.getOpenGlVersionString(), true);
                Command.clientMSG("", true);
                Command.clientMSG("Optifine Version: §d" + Config.getVersion(), true);
                Command.clientMSG("", true);
                Command.clientMSG("LWJGL Version: §d" + Sys.getVersion(), true);
                Command.clientMSG("§m§8----------§r §5Infos §m§8----------", true);
            }
            else if (args[0].equalsIgnoreCase("server")) {
                Command.clientMSG("§m§8----------§r §5Infos §m§8----------", true);
                Command.clientMSG("IP/Domain: §d" + Minecraft.getMinecraft().getCurrentServerData().serverIP, true);
                Command.clientMSG("Ping: §d" + Minecraft.getMinecraft().getCurrentServerData().pingToServer, true);
                Command.clientMSG("Game Version: §d" + Minecraft.getMinecraft().getCurrentServerData().gameVersion, true);
                Command.clientMSG("MOTD: §d" + Minecraft.getMinecraft().getCurrentServerData().serverMOTD, true);
                Command.clientMSG("PlayerList: §d" + Minecraft.getMinecraft().getCurrentServerData().playerList, true);
                Command.clientMSG("§m§8----------§r §5Infos §m§8----------", true);
            }
        }
    }
    
    @Override
    public String getName() {
        return "info";
    }
}
