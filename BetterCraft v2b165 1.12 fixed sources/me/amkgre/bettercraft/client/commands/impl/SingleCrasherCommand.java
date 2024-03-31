// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import me.amkgre.bettercraft.client.mods.crasher.ZeroCrasher;
import me.amkgre.bettercraft.client.mods.crasher.WalkCrasher;
import me.amkgre.bettercraft.client.mods.crasher.SkullCrasher;
import me.amkgre.bettercraft.client.mods.crasher.ReplayCrasher;
import me.amkgre.bettercraft.client.mods.crasher.ReloadCrasher;
import me.amkgre.bettercraft.client.mods.crasher.PlaceFireworkCrasher;
import me.amkgre.bettercraft.client.mods.crasher.PickUpCrasher;
import me.amkgre.bettercraft.client.mods.crasher.OnGroundCrasher;
import me.amkgre.bettercraft.client.mods.crasher.OneSmasherCrasher;
import me.amkgre.bettercraft.client.mods.crasher.NullPointerCrasher;
import me.amkgre.bettercraft.client.mods.crasher.NormalBookCrasher;
import me.amkgre.bettercraft.client.mods.crasher.NcpCrasher;
import me.amkgre.bettercraft.client.mods.crasher.NanCrasher;
import me.amkgre.bettercraft.client.mods.crasher.NameCrasher;
import me.amkgre.bettercraft.client.mods.crasher.MultiCrasher;
import me.amkgre.bettercraft.client.mods.crasher.KeepAliveCrasher;
import me.amkgre.bettercraft.client.mods.crasher.ExploitFixerCrasher;
import me.amkgre.bettercraft.client.mods.crasher.ExceptionerCrasher;
import me.amkgre.bettercraft.client.mods.crasher.EssentialsCrasher;
import me.amkgre.bettercraft.client.mods.crasher.DoubleClickCrasher;
import me.amkgre.bettercraft.client.mods.crasher.CryptoCrasher;
import me.amkgre.bettercraft.client.mods.crasher.CreativeItemControlCrasher;
import me.amkgre.bettercraft.client.mods.crasher.ColorCrasher;
import me.amkgre.bettercraft.client.mods.crasher.BookBypassCrasher;
import me.amkgre.bettercraft.client.mods.crasher.BookFloodCrasher;
import me.amkgre.bettercraft.client.mods.crasher.AnotherBookCrasher;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class SingleCrasherCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("§m§8----------§r §5Crasher 126 §m§8----------", true);
            Command.clientMSG("§d" + CommandManager.syntax + "singlecrasher anotherbook bookflood bookbypass color", true);
            Command.clientMSG("§d" + CommandManager.syntax + "singlecrasher cic crypto doubleclick essentials", true);
            Command.clientMSG("§d" + CommandManager.syntax + "singlecrasher exception exploitfixer keepalive multi", true);
            Command.clientMSG("§d" + CommandManager.syntax + "singlecrasher zero name nan ncp normalbook", true);
            Command.clientMSG("§d" + CommandManager.syntax + "singlecrasher nullpointer onesmasher onground pickup", true);
            Command.clientMSG("§d" + CommandManager.syntax + "singlecrasher placefirework reload replay skull walk", true);
            Command.clientMSG("§m§8----------§r §5Crasher 126 §m§8----------", true);
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("anotherbook")) {
                Command.clientMSG("Try to crash...", true);
                AnotherBookCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("bookflood")) {
                Command.clientMSG("Try to crash...", true);
                BookFloodCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("bookbypass")) {
                Command.clientMSG("Try to crash...", true);
                BookBypassCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("color")) {
                Command.clientMSG("Try to crash...", true);
                ColorCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("cic")) {
                Command.clientMSG("Try to crash...", true);
                CreativeItemControlCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("crypto")) {
                Command.clientMSG("Try to crash...", true);
                CryptoCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("doubleclick")) {
                Command.clientMSG("Try to crash...", true);
                DoubleClickCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("essentials")) {
                Command.clientMSG("Try to crash...", true);
                EssentialsCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("exception")) {
                Command.clientMSG("Try to crash...", true);
                ExceptionerCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("exploitfixer")) {
                Command.clientMSG("Try to crash...", true);
                ExploitFixerCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("keepalive")) {
                Command.clientMSG("Try to crash...", true);
                KeepAliveCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("multi")) {
                Command.clientMSG("Try to crash...", true);
                MultiCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("name")) {
                Command.clientMSG("Try to crash...", true);
                NameCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("nan")) {
                Command.clientMSG("Try to crash...", true);
                NanCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("ncp")) {
                Command.clientMSG("Try to crash...", true);
                NcpCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("normalbook")) {
                Command.clientMSG("Try to crash...", true);
                NormalBookCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("nullpointer")) {
                Command.clientMSG("Try to crash...", true);
                NullPointerCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("onesmasher")) {
                Command.clientMSG("Try to crash...", true);
                OneSmasherCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("onground")) {
                Command.clientMSG("Try to crash...", true);
                OnGroundCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("pickup")) {
                Command.clientMSG("Try to crash...", true);
                PickUpCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("placefirework")) {
                Command.clientMSG("Try to crash...", true);
                PlaceFireworkCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("reload")) {
                Command.clientMSG("Try to crash...", true);
                ReloadCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("replay")) {
                Command.clientMSG("Try to crash...", true);
                ReplayCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("skull")) {
                Command.clientMSG("Try to crash...", true);
                SkullCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("walk")) {
                Command.clientMSG("Try to crash...", true);
                WalkCrasher.start();
            }
            else if (args[0].equalsIgnoreCase("zero")) {
                Command.clientMSG("Try to crash...", true);
                ZeroCrasher.start();
            }
            else {
                Command.clientMSG("§cType singlecrasher", true);
            }
        }
    }
    
    @Override
    public String getName() {
        return "singlecrasher";
    }
}
