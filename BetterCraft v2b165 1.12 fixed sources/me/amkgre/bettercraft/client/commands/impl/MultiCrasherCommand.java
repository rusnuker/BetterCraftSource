// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher14;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher13;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher12;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher11;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher10;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher9;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher8;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher7;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher6;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher5;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher4;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.cpc.CustomPayloadCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.obc.OpenBookCrasher10;
import me.amkgre.bettercraft.client.mods.crasher.obc.OpenBookCrasher9;
import me.amkgre.bettercraft.client.mods.crasher.obc.OpenBookCrasher8;
import me.amkgre.bettercraft.client.mods.crasher.obc.OpenBookCrasher7;
import me.amkgre.bettercraft.client.mods.crasher.obc.OpenBookCrasher5;
import me.amkgre.bettercraft.client.mods.crasher.obc.OpenBookCrasher4;
import me.amkgre.bettercraft.client.mods.crasher.obc.OpenBookCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.obc.OpenBookCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.obc.OpenBookCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.opc.OnePacketCrasher8;
import me.amkgre.bettercraft.client.mods.crasher.opc.OnePacketCrasher7;
import me.amkgre.bettercraft.client.mods.crasher.opc.OnePacketCrasher6;
import me.amkgre.bettercraft.client.mods.crasher.opc.OnePacketCrasher5;
import me.amkgre.bettercraft.client.mods.crasher.opc.OnePacketCrasher4;
import me.amkgre.bettercraft.client.mods.crasher.opc.OnePacketCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.opc.OnePacketCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.opc.OnePacketCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.gbc.GiveBookCrasher7;
import me.amkgre.bettercraft.client.mods.crasher.gbc.GiveBookCrasher6;
import me.amkgre.bettercraft.client.mods.crasher.gbc.GiveBookCrasher5;
import me.amkgre.bettercraft.client.mods.crasher.gbc.GiveBookCrasher4;
import me.amkgre.bettercraft.client.mods.crasher.gbc.GiveBookCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.gbc.GiveBookCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.gbc.GiveBookCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.netty.NettyCrasher6;
import me.amkgre.bettercraft.client.mods.crasher.netty.NettyCrasher5;
import me.amkgre.bettercraft.client.mods.crasher.netty.NettyCrasher4;
import me.amkgre.bettercraft.client.mods.crasher.netty.NettyCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.netty.NettyCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.netty.NettyCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.acc.AutoCompleteCrasher6;
import me.amkgre.bettercraft.client.mods.crasher.acc.AutoCompleteCrasher5;
import me.amkgre.bettercraft.client.mods.crasher.acc.AutoCompleteCrasher4;
import me.amkgre.bettercraft.client.mods.crasher.acc.AutoCompleteCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.acc.AutoCompleteCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.acc.AutoCompleteCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.cc.KlickCrasher5;
import me.amkgre.bettercraft.client.mods.crasher.cc.KlickCrasher4;
import me.amkgre.bettercraft.client.mods.crasher.cc.KlickCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.cc.KlickCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.cc.KlickCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.wec.WorldEditCrasher4;
import me.amkgre.bettercraft.client.mods.crasher.wec.WorldEditCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.wec.WorldEditCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.wec.WorldEditCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.ims.ItemCrasher4;
import me.amkgre.bettercraft.client.mods.crasher.ims.ItemCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.ims.ItemCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.ims.ItemCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.aac.AacCrasher4;
import me.amkgre.bettercraft.client.mods.crasher.aac.AacCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.aac.AacCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.aac.AacCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.mc.MoveCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.mc.MoveCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.mc.MoveCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.flc.FlyCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.flc.FlyCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.flc.FlyCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.fc.GiveFireworkCrasher3;
import me.amkgre.bettercraft.client.mods.crasher.fc.GiveFireworkCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.fc.GiveFireworkCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.sc.SkriptCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.sc.SkriptCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.posc.PositionCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.posc.PositionCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.pc.PermissionsExCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.pc.PermissionsExCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.pbc.PlaceBookCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.pbc.PlaceBookCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.mclc.MassChunkLoadCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.mclc.MassChunkLoadCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.fawe.FaweCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.fawe.FaweCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.cic.CreativeItemCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.cic.CreativeItemCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.bpc.BookPayloadCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.bpc.BookPayloadCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.bp.BlockPlaceCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.bp.BlockPlaceCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.bk.BlockKlickCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.bk.BlockKlickCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.mvc.MultiVerseCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.mvc.MultiVerseCrasher1;
import me.amkgre.bettercraft.client.mods.crasher.ac.AnimationCrasher2;
import me.amkgre.bettercraft.client.mods.crasher.ac.AnimationCrasher1;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class MultiCrasherCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("§m§8----------§r §5Crasher 126 §m§8----------", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher anim1 anim2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher multiverse1 multiverse2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher blockclick1 blockclick2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher blockplace1 blockplace2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher bookpayload1 bookpayload2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher creativeitem1 creativeitem2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher fawe1 fawe2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher masschunk1 masschunk2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher placebook1 placebook2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher pex1 pex2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher pos1 pos2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher skript1 skript2", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher givefirework1 givefirework2 givefirework3", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher fly1 fly2 fly3", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher move1 move2 move3", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher anticheat1 anticheat2", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher anticheat3 anticheat4", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher item1 item2 item3 item4", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher worldedit1 worldedit2", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher worldedit3 worldedit4", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher click1 click2 click3 click4 click5", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher autocomp1 autocomp2 autocomp3", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher autocomp4 autocomp5 autocomp6", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher netty1 netty2 netty3 ", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher netty4 netty5 netty6", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher givebook1 givebook2 givebook3 givebook4", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher givebook5 givebook6 givebook7", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher onepacket1 onepacket2 onepacket3 onepacket4", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher onepacket5 onepacket6 onepacket7 onepacket8", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher openbook1 openbook2 openbook3 openbook4", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher openbook5 openbook6 openbook7 openbook8", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher openbook9 openbook10", true);
            Command.clientMSG("", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher custompayload1 custompayload2 custompayload3", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher custompayload4 custompayload5 custompayload6", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher custompayload7 custompayload8 custompayload9", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher custompayload10 custompayload11 ", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher custompayload12 custompayload13", true);
            Command.clientMSG("§d" + CommandManager.syntax + "multicrasher custompayload14", true);
            Command.clientMSG("§m§8----------§r §5Crasher 126 §m§8----------", true);
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("anim1")) {
                Command.clientMSG("Try to crash...", true);
                AnimationCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("anim2")) {
                Command.clientMSG("Try to crash...", true);
                AnimationCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("anim2")) {
                Command.clientMSG("Try to crash...", true);
                AnimationCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("multiverse1")) {
                Command.clientMSG("Try to crash...", true);
                MultiVerseCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("multiverse2")) {
                Command.clientMSG("Try to crash...", true);
                MultiVerseCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("blockclick1")) {
                Command.clientMSG("Try to crash...", true);
                BlockKlickCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("blockclick2")) {
                Command.clientMSG("Try to crash...", true);
                BlockKlickCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("blockplace1")) {
                Command.clientMSG("Try to crash...", true);
                BlockPlaceCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("blockplace2")) {
                Command.clientMSG("Try to crash...", true);
                BlockPlaceCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("bookpayload1")) {
                Command.clientMSG("Try to crash...", true);
                BookPayloadCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("bookpayload2")) {
                Command.clientMSG("Try to crash...", true);
                BookPayloadCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("creativeitem1")) {
                Command.clientMSG("Try to crash...", true);
                CreativeItemCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("creativeitem2")) {
                Command.clientMSG("Try to crash...", true);
                CreativeItemCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("fawe1")) {
                Command.clientMSG("Try to crash...", true);
                FaweCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("fawe2")) {
                Command.clientMSG("Try to crash...", true);
                FaweCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("masschunk1")) {
                Command.clientMSG("Try to crash...", true);
                MassChunkLoadCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("masschunk2")) {
                Command.clientMSG("Try to crash...", true);
                MassChunkLoadCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("placebook1")) {
                Command.clientMSG("Try to crash...", true);
                PlaceBookCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("placebook2")) {
                Command.clientMSG("Try to crash...", true);
                PlaceBookCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("pex1")) {
                Command.clientMSG("Try to crash...", true);
                PermissionsExCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("pex2")) {
                Command.clientMSG("Try to crash...", true);
                PermissionsExCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("pos1")) {
                Command.clientMSG("Try to crash...", true);
                PositionCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("pos2")) {
                Command.clientMSG("Try to crash...", true);
                PositionCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("skript1")) {
                Command.clientMSG("Try to crash...", true);
                SkriptCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("skript2")) {
                Command.clientMSG("Try to crash...", true);
                SkriptCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("givefirework1")) {
                Command.clientMSG("Try to crash...", true);
                GiveFireworkCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("givefirework2")) {
                Command.clientMSG("Try to crash...", true);
                GiveFireworkCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("givefirework3")) {
                Command.clientMSG("Try to crash...", true);
                GiveFireworkCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("fly1")) {
                Command.clientMSG("Try to crash...", true);
                FlyCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("fly2")) {
                Command.clientMSG("Try to crash...", true);
                FlyCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("fly3")) {
                Command.clientMSG("Try to crash...", true);
                FlyCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("move1")) {
                Command.clientMSG("Try to crash...", true);
                MoveCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("move2")) {
                Command.clientMSG("Try to crash...", true);
                MoveCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("move3")) {
                Command.clientMSG("Try to crash...", true);
                MoveCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("anticheat1")) {
                Command.clientMSG("Try to crash...", true);
                AacCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("anticheat2")) {
                Command.clientMSG("Try to crash...", true);
                AacCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("anticheat3")) {
                Command.clientMSG("Try to crash...", true);
                AacCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("anticheat4")) {
                Command.clientMSG("Try to crash...", true);
                AacCrasher4.start();
            }
            else if (args[0].equalsIgnoreCase("item1")) {
                Command.clientMSG("Try to crash...", true);
                ItemCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("item2")) {
                Command.clientMSG("Try to crash...", true);
                ItemCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("item3")) {
                Command.clientMSG("Try to crash...", true);
                ItemCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("item4")) {
                Command.clientMSG("Try to crash...", true);
                ItemCrasher4.start();
            }
            else if (args[0].equalsIgnoreCase("worldedit1")) {
                Command.clientMSG("Try to crash...", true);
                WorldEditCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("worldedit2")) {
                Command.clientMSG("Try to crash...", true);
                WorldEditCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("worldedit3")) {
                Command.clientMSG("Try to crash...", true);
                WorldEditCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("worldedit4")) {
                Command.clientMSG("Try to crash...", true);
                WorldEditCrasher4.start();
            }
            else if (args[0].equalsIgnoreCase("click1")) {
                Command.clientMSG("Try to crash...", true);
                KlickCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("click2")) {
                Command.clientMSG("Try to crash...", true);
                KlickCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("click3")) {
                Command.clientMSG("Try to crash...", true);
                KlickCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("click4")) {
                Command.clientMSG("Try to crash...", true);
                KlickCrasher4.start();
            }
            else if (args[0].equalsIgnoreCase("click5")) {
                Command.clientMSG("Try to crash...", true);
                KlickCrasher5.start();
            }
            else if (args[0].equalsIgnoreCase("autocomp1")) {
                Command.clientMSG("Try to crash...", true);
                AutoCompleteCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("autocomp2")) {
                Command.clientMSG("Try to crash...", true);
                AutoCompleteCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("autocomp3")) {
                Command.clientMSG("Try to crash...", true);
                AutoCompleteCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("autocomp4")) {
                Command.clientMSG("Try to crash...", true);
                AutoCompleteCrasher4.start();
            }
            else if (args[0].equalsIgnoreCase("autocomp5")) {
                Command.clientMSG("Try to crash...", true);
                AutoCompleteCrasher5.start();
            }
            else if (args[0].equalsIgnoreCase("autocomp6")) {
                Command.clientMSG("Try to crash...", true);
                AutoCompleteCrasher6.start();
            }
            else if (args[0].equalsIgnoreCase("netty1")) {
                Command.clientMSG("Try to crash...", true);
                NettyCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("netty2")) {
                Command.clientMSG("Try to crash...", true);
                NettyCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("netty3")) {
                Command.clientMSG("Try to crash...", true);
                NettyCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("netty4")) {
                Command.clientMSG("Try to crash...", true);
                NettyCrasher4.start();
            }
            else if (args[0].equalsIgnoreCase("netty5")) {
                Command.clientMSG("Try to crash...", true);
                NettyCrasher5.start();
            }
            else if (args[0].equalsIgnoreCase("netty6")) {
                Command.clientMSG("Try to crash...", true);
                NettyCrasher6.start();
            }
            else if (args[0].equalsIgnoreCase("givebook1")) {
                Command.clientMSG("Try to crash...", true);
                GiveBookCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("givebook2")) {
                Command.clientMSG("Try to crash...", true);
                GiveBookCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("givebook3")) {
                Command.clientMSG("Try to crash...", true);
                GiveBookCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("givebook4")) {
                Command.clientMSG("Try to crash...", true);
                GiveBookCrasher4.start();
            }
            else if (args[0].equalsIgnoreCase("givebook5")) {
                Command.clientMSG("Try to crash...", true);
                GiveBookCrasher5.start();
            }
            else if (args[0].equalsIgnoreCase("givebook6")) {
                Command.clientMSG("Try to crash...", true);
                GiveBookCrasher6.start();
            }
            else if (args[0].equalsIgnoreCase("givebook7")) {
                Command.clientMSG("Try to crash...", true);
                GiveBookCrasher7.start();
            }
            else if (args[0].equalsIgnoreCase("onepacket1")) {
                Command.clientMSG("Try to crash...", true);
                OnePacketCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("onepacket2")) {
                Command.clientMSG("Try to crash...", true);
                OnePacketCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("onepacket3")) {
                Command.clientMSG("Try to crash...", true);
                OnePacketCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("onepacket4")) {
                Command.clientMSG("Try to crash...", true);
                OnePacketCrasher4.start();
            }
            else if (args[0].equalsIgnoreCase("onepacket5")) {
                Command.clientMSG("Try to crash...", true);
                OnePacketCrasher5.start();
            }
            else if (args[0].equalsIgnoreCase("onepacket6")) {
                Command.clientMSG("Try to crash...", true);
                OnePacketCrasher6.start();
            }
            else if (args[0].equalsIgnoreCase("onepacket7")) {
                Command.clientMSG("Try to crash...", true);
                OnePacketCrasher7.start();
            }
            else if (args[0].equalsIgnoreCase("onepacket8")) {
                Command.clientMSG("Try to crash...", true);
                OnePacketCrasher8.start();
            }
            else if (args[0].equalsIgnoreCase("openbook1")) {
                Command.clientMSG("Try to crash...", true);
                OpenBookCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("openbook2")) {
                Command.clientMSG("Try to crash...", true);
                OpenBookCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("openbook3")) {
                Command.clientMSG("Try to crash...", true);
                OpenBookCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("openbook4")) {
                Command.clientMSG("Try to crash...", true);
                OpenBookCrasher4.start();
            }
            else if (args[0].equalsIgnoreCase("openbook5")) {
                Command.clientMSG("Try to crash...", true);
                OpenBookCrasher5.start();
            }
            else if (args[0].equalsIgnoreCase("openbook6")) {
                Command.clientMSG("Try to crash...", true);
                OpenBookCrasher5.start();
            }
            else if (args[0].equalsIgnoreCase("openbook7")) {
                Command.clientMSG("Try to crash...", true);
                OpenBookCrasher7.start();
            }
            else if (args[0].equalsIgnoreCase("openbook8")) {
                Command.clientMSG("Try to crash...", true);
                OpenBookCrasher8.start();
            }
            else if (args[0].equalsIgnoreCase("openbook9")) {
                Command.clientMSG("Try to crash...", true);
                OpenBookCrasher9.start();
            }
            else if (args[0].equalsIgnoreCase("openbook10")) {
                Command.clientMSG("Try to crash...", true);
                OpenBookCrasher10.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload1")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher1.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload2")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher2.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload3")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher3.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload4")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher4.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload5")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher5.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload6")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher6.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload7")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher7.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload8")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher8.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload9")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher9.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload10")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher10.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload11")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher11.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload12")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher12.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload13")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher13.start();
            }
            else if (args[0].equalsIgnoreCase("custompayload14")) {
                Command.clientMSG("Try to crash...", true);
                CustomPayloadCrasher14.start();
            }
            else {
                Command.clientMSG("§cType multicrasher", true);
            }
        }
    }
    
    @Override
    public String getName() {
        return "multicrasher";
    }
}
